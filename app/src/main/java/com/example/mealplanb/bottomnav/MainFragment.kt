package com.example.mealplanb.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.R
import com.example.mealplanb.UserManager
import com.example.mealplanb.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    lateinit var binding:FragmentMainBinding
    val userData = UserManager.getUserData()
    val userCal = UserManager.getUserCal()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.basic_container, TodayFragment())
        transaction.commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val handled = when (item.itemId) {
                R.id.navigation_home -> {
                    // 홈 화면으로 이동
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.basic_container, TodayFragment())
                    transaction.commit()
                    true
                }
                R.id.navigation_statics -> {
                    // 통계로 이동
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.basic_container, Statics_Fragment())
                    transaction.commit()
                    true
                }
                R.id.navigation_fast -> {
                    // 단식 화면으로 이동
                    // 예: supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NotificationsFragment()).commit()
                    true
                }
                R.id.navigation_my_page -> {
                    // 마이 페이지로 이동
                    // 예: supportFragmentManager.beginTransaction().replace(R.id.fragment_container, NotificationsFragment()).commit()
                    true
                }
                else -> false
            }
            handled
        }

        return binding.root
    }


}