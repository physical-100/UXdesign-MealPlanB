package com.example.mealplanb.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mealplanb.bottomnav.Statics_Fragment
import com.example.mealplanb.fragment.Fast_Record_Fragment
import com.example.mealplanb.fragment.Fast_Timer_Fragment

class FastViewPagerAdapter (fragment : FragmentActivity) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Fast_Timer_Fragment()
            1 -> Fast_Record_Fragment()
            else -> Statics_Fragment()
        }
    }
}