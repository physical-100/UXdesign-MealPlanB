package com.example.mealplanb.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentSettingFastDateBinding


class Setting_Fast_Date_Fragment : Fragment() {
    lateinit var binding: FragmentSettingFastDateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingFastDateBinding.inflate(inflater, container, false)
        binding.next2.setOnClickListener {
            findNavController().navigate(R.id.action_setting_Fast_Date_Fragment_to_fast_Timer_1_Fragment)
        }
        return binding.root
    }
}