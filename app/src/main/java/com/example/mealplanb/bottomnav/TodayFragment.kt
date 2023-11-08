package com.example.mealplanb.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.User_calory
import com.example.mealplanb.databinding.FragmentTodayBinding


class TodayFragment : Fragment() {
    lateinit var binding: FragmentTodayBinding
    val userData = UserManager.getUserData()
    val userCal = UserManager.getUserCal()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodayBinding.inflate(inflater, container, false)
//        val userCalory = arguments?.getParcelable<User_calory>("user_calory")
//        if (userCalory != null) {
//            // userCalory를 사용하여 필요한 작업 수행
//            binding.apply {
//                goalCarb.text = "/${userCalory.carb}g"
//                goalProtein.text = "/${userCalory.protein}g"
//                goalFat.text = "/${userCalory.fat}g"
//                goal.text= "${userCalory.goal_calory} Kcal"
//
//            }

        if (userCal != null) {
            // userCalory를 사용하여 필요한 작업 수행
            binding.apply {
                goalCarb.text = "/${userCal.carb}g"
                goalProtein.text = "/${userCal.protein}g"
                goalFat.text = "/${userCal.fat}g"
                goal.text= "${userCal.goal_calory} Kcal"

            }

//            val pieChart: PieChart = binding.chart
//
//            // 목표 칼로리와 현재 칼로리 (예: 사용자가 섭취한 칼로리)를 가져옵니다.
//            val goalCalories = userCalory.goal_calory.toFloat()
//            val currentCalories = 0 // 현재 칼로리
//
//            // 목표 칼로리에 얼마나 근접했는지 계산합니다.
//            val remainingCalories = (goalCalories - currentCalories)
//            val achievedCalories = if (remainingCalories >= 0) currentCalories else currentCalories + remainingCalories
//
//            // 원형 그래프에 표시할 데이터를 생성합니다.
//            val pieEntries = listOf(
//                PieEntry(achievedCalories, "섭취한 칼로리"),
//                PieEntry(remainingCalories, "남은 칼로리")
//            )
//
//            val dataSet = PieDataSet(pieEntries, "칼로리")
//            dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
//
//            val data = PieData(dataSet)
//            pieChart.data = data
//            // 그래프 업데이트
//            pieChart.invalidate()


        }
        binding.meal1.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_add_Diet_Fragment)
        }
    return  binding.root
    }


    }
