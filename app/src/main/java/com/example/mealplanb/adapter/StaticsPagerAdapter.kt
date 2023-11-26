package com.example.mealplanb.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mealplanb.fragment.CaloryStaticFragment
import com.example.mealplanb.fragment.DailyStaticFragment
import com.example.mealplanb.fragment.MonthlyStaticFragment
import com.example.mealplanb.fragment.WeeklyStaticFragment
import com.example.mealplanb.fragment.WeightStaticFragment

class StaticsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CaloryStaticFragment()
            1 -> WeightStaticFragment()
            else ->CaloryStaticFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2 // 칼로리, 체중 두개 탭
    }

}

