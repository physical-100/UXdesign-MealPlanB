package com.example.mealplanb


import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import androidx.navigation.NavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.adapter.MealaddAdapter
import com.example.mealplanb.bottomnav.MainFragment
import com.example.mealplanb.fragment.AnimationFragment
import com.example.mealplanb.fragment.MealhomeFragment
import com.example.mealplanb.initset.Profile_fragment

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jayway.jsonpath.JsonPath
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private val NOTIFICATION_PERMISSION_CODE = 123
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    var userdata: Userdata? = null
    var usercomplete: String? = null
    var usercal: User_calory? = null
    var userEatMealNumber :Int=0
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거
    val usertodayweight : TodayWeight?=null
    var userFavoriteMealDataList:ArrayList<FavoriteMealData> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userdata = Userdata.clear()
        usercal = User_calory.clear()
        createNotificationChannel()
        firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (Fuserdata in dataSnapshot.children) {
                                val key = Fuserdata.key.toString()
                                val value = Fuserdata.value.toString()
                                Log.i("firebase1", "key:$key, value:$value")
                                Log.i("firebase2", "$usercal")
                                Log.i("firebase12", "$userdata")
                                Log.i("firebase3", "$usercomplete")
                                when (key) {
                                    "이름" -> userdata?.username = value
                                    "나이" -> userdata?.age = value.toInt()
                                    "성별" -> userdata?.gender = value
                                    "키" -> userdata?.height = value.toInt()
                                    "평소 활동량" -> userdata?.activitytype = value
                                    "시작체중" -> userdata?.start_weight = value.toDouble()
                                    "목표체중" -> userdata?.goal_weight = value.toDouble()
                                    "목표식단" -> userdata?.mealtype = value
                                    "목표 탄수화물" -> usercal?.carb = value.toInt()
                                    "목표 탄수화물(%)" -> usercal?.carb_percent = value.toInt()
                                    "목표 단백질" -> usercal?.protein = value.toInt()
                                    "목표 단백질(%)" -> usercal?.protein_percent = value.toInt()
                                    "목표 지방" -> usercal?.fat = value.toInt()
                                    "목표 지방(%)" -> usercal?.fat_percent = value.toInt()
                                    "목표 칼로리" -> usercal?.goal_calory = value.toInt()
                                    "초기설정여부" -> usercomplete = value

                                }


                            }
                            UserManager.setUserData(userdata!!)
                            UserManager.setUserCal(usercal!!)
                            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
                            if (navHostFragment != null) {
                                val navController = navHostFragment.navController
                                handleDataAfterFirebase(navController)
                                Log.i("", "onDataChange: ")
                            } else {
                                // NavHostFragment not found에 대한 예외 처리
                                Log.e("MainActivity", "NavHostFragment not found")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
        meallistfromDatabase()
        weightRoadfromDatabase()
        favoritedataFromfirebase()
        Log.i("진짜확인", UserManager.getFavoriteMealDataList().toString())


    }

    private fun weightRoadfromDatabase() {
        val dataRoute =
            firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/체중기입/$realTime")
        dataRoute.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("snap",snapshot.toString())
                for (i in snapshot.children){
                    if(i.value!=null){
                        val weight=i.value.toString().toDouble()
                        val userweight=TodayWeight(realTime, weight)
                        UserManager.setUserTodayWeight(userweight)
                    }
                }
                //val value=JsonPath.parse(snapshot.value)
//                val value=snapshot.value
//
//                if(value!=null){
//                    val weight = value.toDouble()
//                    val userweight = TodayWeight(realTime, weight)
//                    UserManager.setUserTodayWeight(userweight)
//                }

                //val weight=(value.read("$['현재 사용자 체중']") as String).toDouble()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun favoritedataFromfirebase(){
        val dataRoute= firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단 추천/자주먹는음식(즐겨찾기)")
        dataRoute.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(userdata in snapshot.children){
                    val foodkey=userdata.key.toString()
                    Log.i("왜왜왜1", userdata.key.toString())
                    val value1 = JsonPath.parse(userdata.value)
                    if(foodkey!=null){
                        val foodname=value1.read("$['식품이름']")as String
                        Log.i("왜왜왜3", foodname )
                        val foodbrand=value1.read("$['가공업체']")as String
                        val foodamount=(value1.read("$['섭취량']")as String).toDouble()
                        val foodcarbo = (value1.read("$['탄수화물']") as String).toDouble()
                        val foodprotein = (value1.read("$['단백질']") as String).toDouble()
                        val foodfat = (value1.read("$['지방']") as String).toDouble()
                        val foodkcal = (value1.read("$['열량']")as String).toDouble()

                        val perFavoriteMealData =FavoriteMealData(foodkey,foodname,foodbrand,foodkcal,foodamount,foodcarbo,foodprotein,foodfat)
                        Log.i("진짜확인333", perFavoriteMealData.toString())
                        UserManager.addFavoriteMealDataList(perFavoriteMealData)
                        Log.i("진짜확인444",UserManager.getFavoriteMealDataList().toString())

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun meallistfromDatabase() {
        val dataRoute = firebaseDatabase
            .getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단기입/$realTime")
        dataRoute.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val date=dataSnapshot.key.toString()
                Log.i("key33", dataSnapshot.key.toString())
                var totalCarbo = 0.0
                var totalProtein = 0.0
                var totalFat = 0.0
                var totalkcal=0.0
                for (Fuserdata in dataSnapshot.children) {
                    val key = Fuserdata.key.toString()
                    Log.i("key22", key)
                    if(Fuserdata.children!=null){
                        userEatMealNumber+=1
                    }
                    for(Fuserdata2 in Fuserdata.children){
                        val key2= Fuserdata2.key.toString()
                        Log.i("key2",key2)
                        val value1 = JsonPath.parse(Fuserdata2.value)
                        if (key2 != null) {
                            val foodname=value1.read("$['식품이름']")as String
                            val foodbrand=value1.read("$['가공업체']")as String
                            val foodamount=(value1.read("$['섭취량']")as String).toDouble()
                            val foodcarbo = (value1.read("$['탄수화물']") as String).toDouble()
                            val foodprotein = (value1.read("$['단백질']") as String).toDouble()
                            val foodfat = (value1.read("$['지방']") as String).toDouble()
                            val foodkcal = (value1.read("$['열량']")as String).toDouble()

                            totalCarbo += foodcarbo
                            totalProtein += foodprotein
                            totalFat += foodfat
                            totalkcal+=foodkcal
                            val permealeachfood=AllListNutrition(key,key2,foodcarbo,foodprotein,foodfat,foodkcal)
                            val permealeachfood2=MealData(date,key,foodname,foodbrand,foodkcal,foodamount,foodcarbo,foodprotein,foodfat)
                            Log.i("permeal", permealeachfood.toString())
                            Log.i("permeal2", permealeachfood2.toString())
                            UserManager.addMealData(permealeachfood2)
                            UserManager.addAllListNutrionList(permealeachfood)

                        }

                    }

                }
                Log.i("mealnumber111", userEatMealNumber.toString())
                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }



    private fun createNotificationChannel() {
        // Android 버전이 Oreo(26) 이상인 경우에만 채널을 생성합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "your_channel_id"
            val channelName = "Your Channel Name"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Your channel description"
                // 다양한 설정을 추가할 수 있습니다.
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun handleDataAfterFirebase(navController: NavController) {
        if (usercomplete == null) {
            navController.navigate(R.id.action_animationFragment_to_profile_fragment)
        } else{
            // 초기 설정이 이미 완료된 경우
            UserManager.setUserData(userdata!!)
            UserManager.setUserCal(usercal!!)
            navController.navigate(R.id.action_animationFragment_to_mainFragment)
        }
    }

}
object UserManager {
        private var userdata: Userdata? = null
        private var usercal: User_calory? = null
        private var usermealdataList: ArrayList<MealData> = ArrayList()
        private var userAllListNutritionList: ArrayList<AllListNutrition> = ArrayList()
        private var usermealnumber:String?=null
        private var usertodayweight:TodayWeight?=null

        private var userFavoriteMealDataList: ArrayList<FavoriteMealData> = ArrayList()

        fun getFavoriteMealDataList() : ArrayList<FavoriteMealData> {
            return userFavoriteMealDataList
        }
        fun addFavoriteMealDataList(favoriteMealData:FavoriteMealData) {
            userFavoriteMealDataList.add(favoriteMealData)
        }
        fun removeFavoriteMealDataList(favoriteMealData: FavoriteMealData){
            userFavoriteMealDataList.remove(favoriteMealData)
        }

        private var totaldata:Totalcal?=null



        fun getUserTodayWeight():TodayWeight?{
            return usertodayweight
        }

        fun setUserTodayWeight(usertodayweight:TodayWeight){
            this.usertodayweight=usertodayweight
        }
        fun getTotalcal():Totalcal?{
            return totaldata
        }
        fun setTotalcal(totaldata: Totalcal){
            this.totaldata= totaldata
        }
        fun getUserCal(): User_calory? {
            return usercal
        }
        fun setUserCal(usercal: User_calory) {
            this.usercal = usercal
        }
        fun getUserData(): Userdata? {
            return userdata
        }
        fun setUserData(userData: Userdata) {
            this.userdata = userData
        }
        fun getAllListNutritionList(): ArrayList<AllListNutrition>{
            return userAllListNutritionList
        }
        fun addAllListNutrionList(allListNutrition: AllListNutrition){
            userAllListNutritionList.add(allListNutrition)
        }
        fun removeAllListNutrion(allListNutrition: AllListNutrition){
            userAllListNutritionList.remove(allListNutrition)
        }

        fun getMealData(): ArrayList<MealData> {
            return usermealdataList
        }
        fun addMealData(mealData: MealData) {
            usermealdataList.add(mealData)
        }
        fun removeMealdata(mealData: MealData){
            usermealdataList.remove(mealData)
        }
        fun clearUserdata(){
        userdata= Userdata.clear()
        }
        fun clearMealData(){
            usermealdataList.clear()
        }
//    <com.airbnb.lottie.LottieAnimationView
//    android:layout_width="100dp"
//    android:layout_height="100dp"
//    app:lottie_rawRes="@raw/muscle"
//    app:lottie_loop="true"
//    app:lottie_autoPlay="true"
//    app:lottie_repeatCount="2"/>
    }