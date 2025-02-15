package com.example.mealplanb.bottomnav

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.UserManager
import com.example.mealplanb.Userdata
import com.example.mealplanb.databinding.FragmentMypagefragmentBinding
import com.example.mealplanb.databinding.FragmentProfileFragmentBinding

//알림 설정 만들어야
class Mypagefragment : Fragment() {
    lateinit var binding: FragmentMypagefragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userData = UserManager.getUserData()
        val userCal = UserManager.getUserCal()
        Log.i("goal5", "${userData}")

        var usertodayweight =UserManager.getUserTodayWeight()

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
            findNavController().navigate(R.id.action_mainFragment_to_profile_modifyingFragment)
        }
        binding.sujungProfile.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_profile_modifyingFragment)

        }
        Log.i("체중", "오늘의 체중 $usertodayweight"+"목표 체중 ${userData?.goal_weight.toString()}")
        if (userCal != null&&userData!=null) {
            binding.apply {
                nickname.text="${userData.username}"
                mypageGoalWeight.text = "${userData.goal_weight}Kg"
                mypageGoalCal.text = "${userCal.goal_calory}Kcal"
                mypageGoalPercent.text ="${userCal.carb_percent}:${userCal.protein_percent}:${userCal.fat_percent}"
                if(usertodayweight==null){
                    val differenceweight=0
                    if(differenceweight>=0) {
                        changeWeight.text = "+${differenceweight}kg"
                    }else{
                        changeWeight.text="${differenceweight}kg"
                    }
                }
                else{

                    val differenceweight=usertodayweight!!.weight.toDouble()-userData.start_weight.toDouble()
                    if(differenceweight>=0) {
                        changeWeight.text = "+${differenceweight}kg"
                    }else{
                        changeWeight.text="${differenceweight}kg"

                    }
                }
            }
        }
        return  binding.root
    }

}