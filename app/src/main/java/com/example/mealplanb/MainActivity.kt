package com.example.mealplanb


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

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
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    var userdata: Userdata? = null
    var usercomplete: String? = null
    var usercal: User_calory? = null
    var userEatMealNumber :Int=0
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거
    val usertodayweight : TodayWeight?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userdata = Userdata.clear()
        usercal = User_calory.clear()
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
                                    "평소 활동량" -> userdata?.type = value
                                    "시작체중" -> userdata?.start_weight = value.toDouble()
                                    "목표체중" -> userdata?.goal_weight = value.toDouble()
                                    "목표식단" -> userdata?.type = value
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
                            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
                            if (navHostFragment != null) {
                                val navController = navHostFragment.navController
                                handleDataAfterFirebase(navController)
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

        fun getUserTodayWeight():TodayWeight?{
            return usertodayweight
        }
        fun setUserTodayWeight(usertodayweight:TodayWeight){
            this.usertodayweight=usertodayweight
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

        fun getMealData(): ArrayList<MealData> {
            return usermealdataList
        }
        fun addMealData(mealData: MealData) {
            usermealdataList.add(mealData)
        }
        fun clearMealData(){
            usermealdataList.clear()
        }
    }