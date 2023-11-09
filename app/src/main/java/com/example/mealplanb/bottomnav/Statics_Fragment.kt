package com.example.mealplanb.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.mealplanb.UserManager
import com.example.mealplanb.adapter.StaticsPagerAdapter
import com.example.mealplanb.databinding.FragmentStaticsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class Statics_Fragment : Fragment() {
    lateinit var binding: FragmentStaticsBinding
    val userData = UserManager.getUserData()
    val userCal = UserManager.getUserCal()
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentStaticsBinding.inflate(inflater, container, false)

            val tabLayout: TabLayout = binding.tabLayout
            val viewPager: ViewPager2 = binding.viewPager

            val adapter = StaticsPagerAdapter(requireActivity())
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "일간"
                    1 -> tab.text = "주간"
                    2 -> tab.text = "월간"
                }
            }.attach()
            if (userCal != null&&userData!=null) {
                binding.apply {
                    staticWeight.text = "${userData.goal_weight}Kg"
                    staticCalory.text = "${userCal.goal_calory}Kg"
                    staticRatio.text ="${userCal.carb_percent}:${userCal.protein_percent}:${userCal.fat_percent}"
                }
            }

            return binding.root
        }

}
