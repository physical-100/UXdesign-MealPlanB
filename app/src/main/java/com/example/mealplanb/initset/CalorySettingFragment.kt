package com.example.mealplanb.initset

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.Userdata
import com.example.mealplanb.databinding.FragmentCalorySettingBinding
import com.google.firebase.database.FirebaseDatabase

class CalorySettingFragment : Fragment() {
    lateinit var binding: FragmentCalorySettingBinding
//    lateinit var Userdata:Userdata
    lateinit var goal_cal:String
    private val firebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalorySettingBinding.inflate(inflater, container, false)

//        남성 BMR = (10 * 체중[kg]) + (6.25 * 키[cm]) - (5 * 나이[세]) + 5
//        여성 BMR = (10 * 체중[kg]) + (6.25 * 키[cm]) - (5 * 나이[세]) - 161
//        활동 수준 계산:
//        활동 수준은 일상 생활에서의 활동 정도를 나타냅니다. 예를 들어, 앉아서 일하는 사무직인 경우와 운동을 많이 하는 경우와 같이 활동 수준은 다를 수 있습니다.
//        활동 수준을 평가하고, 아래와 같이 분류할 수 있습니다.
//        거의 활동하지 않음 (Sedentary): BMR * 1.2
//        가벼운 활동 (Lightly active): BMR * 1.375
//        보통 활동 (Moderately active): BMR * 1.55
//        활발한 활동 (Very active): BMR * 1.725
//        매우 활동적 (Extra active): BMR * 1.9
        // 힌트 칼로리 설정 해야함

        val userdata = arguments?.getParcelable<Userdata>("Userdata")

        // Userdata 객체를 사용하여 계산 등을 수행
        if (userdata != null) {
            if (userdata.gender == "남성") {
                val BMR = (10 * userdata.start_weight.toInt() + 6.25 * userdata.height - 5 * userdata.age + 5).toInt()
                if (userdata.type == "활동 많음") {
                    val calory = (1.725 * BMR).toInt()
                    binding.calorySetting.setText("$calory")
                    binding.explain.setText("일일 권장 섭취량"+"$calory"+"kcal예요")
                }
            }

        }
        binding.next2.setOnClickListener {
            val dataRoute=firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id")

            goal_cal = binding.calorySetting.text.toString()
            dataRoute.child("목표 칼로리").setValue(goal_cal.toInt())
            Log.i("sendcal",goal_cal)
            findNavController().navigate(R.id.action_calorySettingFragment_to_detailNutritionFragment, bundleOf("goal_cal" to goal_cal))
            //목표 칼로리를 인자로 넘김

        }












        return binding.root
    }


}
