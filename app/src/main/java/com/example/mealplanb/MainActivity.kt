package com.example.mealplanb

import com.example.mealplanb.initset.Profile_fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private var userdata: Userdata? = null

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
    fun setUserdata(userdata: Userdata) {
        this.userdata = userdata
    }
}