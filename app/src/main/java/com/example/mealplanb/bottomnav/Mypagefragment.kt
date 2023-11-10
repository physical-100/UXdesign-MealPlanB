package com.example.mealplanb.bottomnav

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.Userdata
import com.example.mealplanb.databinding.FragmentMypagefragmentBinding
import com.example.mealplanb.databinding.FragmentProfileFragmentBinding

//알림 설정 만들어야
class Mypagefragment : Fragment() {
    lateinit var binding: FragmentMypagefragmentBinding
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypagefragmentBinding.inflate(inflater, container, false)
        binding.goToInitialSetting.setOnClickListener{
            // 초기 설정 이동
            findNavController().navigate(R.id.action_mainFragment_to_profile_fragment)
        }
        binding.alarmSetting.setOnClickListener {
            // 앱 설정 변경 구현 부분

        }
        binding.modifiyNickname.setOnClickListener {

        }
        binding.sujungProfile.setOnClickListener {

        }
        return  binding.root
    }

}