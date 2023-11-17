package com.example.mealplanb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mealplanb.UserManager
import android.util.Log
import android.view.ViewTreeObserver
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.bottomnav.MainFragment
import com.example.mealplanb.fragment.AnimationFragment
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
        val initFragment = AnimationFragment()
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
    }
    private fun handleDataAfterFirebase(navController: NavController) {
        if (usercomplete == null) {
            navController.navigate(R.id.action_animationFragment_to_profile_fragment)
        } else {
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
    }