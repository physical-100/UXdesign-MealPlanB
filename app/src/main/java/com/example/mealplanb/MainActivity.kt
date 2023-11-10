package com.example.mealplanb
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mealplanb.bottomnav.MainFragment
import com.example.mealplanb.initset.Profile_fragment
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    private var userdata: Userdata? =null
    private var usercal: User_calory?=null
    private val firebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id").child("초기설정여부").get().addOnSuccessListener {dataSnapshot ->
//            userdata=dataSnapshot.value
//            Log.i("firebase1", "Got value ${userdata}")
//            if (userdata == null) {
//                val profileFragment = Profile_fragment()
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.profile_fragment, profileFragment)
//                    .commit()
//            }else{ //유저데이터가 있을  때 mainfragment로 감
//                val mainFragment = MainFragment()
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.nav_host_fragment, mainFragment)
//                    .commit()
//            }
//        }.addOnFailureListener{
//            Log.e("firebase", "Error getting data", it)
//        }
        // Userdata가 null인 경우 Profile_fragment를 추가
        if (userdata == null) {
            val profileFragment = Profile_fragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, profileFragment)
                .commit()
        }else{ //유저데이터가 있을  때 mainfragment로 감
            val mainFragment = MainFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, mainFragment)
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