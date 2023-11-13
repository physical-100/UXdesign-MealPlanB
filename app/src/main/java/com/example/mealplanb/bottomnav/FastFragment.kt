package com.example.mealplanb.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.mealplanb.R
import com.example.mealplanb.adapter.FastViewPagerAdapter
import com.example.mealplanb.databinding.FragmentFastBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FastFragment : Fragment() {
    // TODO: Rename and change types of parameters

    var binding: FragmentFastBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFastBinding.inflate(layoutInflater, container, false)

        val tabLayout: TabLayout = binding!!.tabLayout
        val viewPager: ViewPager2 = binding!!.viewPager

        val adapter = FastViewPagerAdapter(requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "단식 타이머"
                1 -> tab.text = "단식 기록"
                2 -> tab.text = "통계"

            }
        }.attach()
        return binding!!.root
    }
}
