package com.example.mealplanb.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.UserManager
import com.example.mealplanb.User_calory
import com.example.mealplanb.databinding.FragmentTodayBinding
import com.example.mealplanb.fragment.MealhomeFragment


class TodayFragment : Fragment() {
    lateinit var binding: FragmentTodayBinding
    val userData = UserManager.getUserData()
    val userCal = UserManager.getUserCal()
    private lateinit var scrollView: ScrollView
    private var scrollPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // ScrollView의 스크롤 위치 저장
        scrollPosition = scrollView.scrollY
        outState.putInt("scroll_position", scrollPosition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodayBinding.inflate(inflater, container, false)
        scrollView =binding.scrollView

        // 스크롤 위치를 복원
        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("scroll_position", 0)
            scrollView.post { scrollView.scrollTo(0, scrollPosition) }
        }
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
                carbohydrate.text = "순탄수\n${userCal.carb}g"
                protein.text = "단백질\n${userCal.protein}g"
                fat.text = "지방\n${userCal.fat}g"
                leftoverCal.text= "오늘은 ${userCal.goal_calory}kcal 남았어요"

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
        val mealhomeFragment = MealhomeFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.addmeal, mealhomeFragment)
            .commit()

        val dailyweightFragment = DailyweightFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.addweight, dailyweightFragment)
            .commit()

    return  binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Disable back press
        binding.scrollView.setOnClickListener {
            scrollPosition = binding.scrollView.scrollY
            // Perform your screen transition here...
        }
    }
    override fun onResume() {
        super.onResume()
        // Restore the scroll position when the fragment is resumed
        binding.scrollView.post() { binding.scrollView.scrollTo(0, scrollPosition) }
    }


    }
