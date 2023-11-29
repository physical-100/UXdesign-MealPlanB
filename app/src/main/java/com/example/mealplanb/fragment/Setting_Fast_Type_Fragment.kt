package com.example.mealplanb.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentSettingFastTypeBinding


class Setting_Fast_Type_Fragment : Fragment() {
    lateinit var binding:FragmentSettingFastTypeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSettingFastTypeBinding.inflate(inflater, container, false)

        binding.apply {
            f1212.setOnClickListener {
                findNavController().navigate(R.id.action_setting_Fast_Type_Fragment_to_setting_Fast_Date_Fragment)
            }
            f1410.setOnClickListener {
                findNavController().navigate(R.id.action_setting_Fast_Type_Fragment_to_setting_Fast_Date_Fragment)
            }
            f1608.setOnClickListener {
                findNavController().navigate(R.id.action_setting_Fast_Type_Fragment_to_setting_Fast_Date_Fragment)
            }
            f1806.setOnClickListener {
                findNavController().navigate(R.id.action_setting_Fast_Type_Fragment_to_setting_Fast_Date_Fragment)
            }
            f2004.setOnClickListener {
                findNavController().navigate(R.id.action_setting_Fast_Type_Fragment_to_setting_Fast_Date_Fragment)
            }
            fAll.setOnClickListener {
                findNavController().navigate(R.id.action_setting_Fast_Type_Fragment_to_setting_Fast_Date_Fragment)
            }
        }
        return binding.root
    }

}