package com.example.mealplanb

import Profile_fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    private var userdata: Userdata? = null
    private var usercal: User_calory?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Userdata가 null인 경우 Profile_fragment를 추가
        if (userdata == null) {
            val profileFragment = Profile_fragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_profile, profileFragment)
                .commit()
        }
    }

    // Userdata를 설정하는 메서드 (예: 데이터가 입력될 때 호출)
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