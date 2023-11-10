package com.example.mealplanb.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentDailyStaticBinding
import com.example.mealplanb.databinding.FragmentFastTimerBinding

class Fast_Timer_Fragment : Fragment() {

    lateinit var binding:FragmentFastTimerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFastTimerBinding.inflate(inflater,container,false)
        return binding.root
    }

}