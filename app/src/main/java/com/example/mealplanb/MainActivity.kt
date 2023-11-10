package com.example.mealplanb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mealplanb.bottomnav.MainFragment
import com.example.mealplanb.initset.Profile_fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()


    var userdata: Userdata? = null
    var usercomplete: String? = null
    var usercal: User_calory? = null


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
                    if (usercomplete == null) {
                        val profileFragment = Profile_fragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_profile, profileFragment).commit()
                    } else { //유저데이터가 있을  때 mainfragment로 감
                        UserManager.setUserData(userdata!!)
                        val mainFragment = MainFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_main, mainFragment)
                            .commit()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }



            })


        // Userdata를 설정하는 메서드 (예: 데이터가 입력될 때 호출)
    }
}
object UserManager {
    private var userdata: Userdata? = null
    private var usercal: User_calory?=null
    fun getUserCal():User_calory?{
        return  usercal
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
}