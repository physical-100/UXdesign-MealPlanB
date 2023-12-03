package com.example.mealplanb.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mealplanb.R
import com.example.mealplanb.TodayWeight
import com.example.mealplanb.UserManager
import com.example.mealplanb.databinding.FragmentDailyweightBinding
import com.example.mealplanb.databinding.FragmentStaticsBinding
import com.example.mealplanb.fragment.editweightFragment

class DailyweightFragment : Fragment(),editweightFragment.OnNumberEnteredListener  {
    lateinit var binding: FragmentDailyweightBinding
    var todayWeight:TodayWeight?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDailyweightBinding.inflate(inflater, container, false)
        todayWeight=UserManager.getUserTodayWeight()
        if(todayWeight==null){
            binding.dailyWeight.text = "오늘의 체중을 입력해주세요"
        }
        else{
            binding.dailyWeight.text = "${todayWeight?.weight} Kg"
        }
        if( binding.dailyWeight.text!=null){
            binding.addweightbutton.text = "수정하기"
        }
        else{
            binding.addweightbutton.text = "기록하기"
        }


        binding.addweightbutton.setOnClickListener {
            val bottomSheetFragment = editweightFragment()

            // 리스너 설정
            bottomSheetFragment.setOnNumberEnteredListener(this)

            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
        return binding.root
    }

    override fun onNumberEntered(number: Double) {

        binding.dailyWeight.text = "${todayWeight?.weight} Kg"

        // 어제날짜 체중 가져오는 코드 필요
    binding.weightComment.text= "어제보다 - 0.6kg"
    }


}