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
import com.example.mealplanb.databinding.FragmentMealSelectBinding
import com.google.firebase.database.FirebaseDatabase


class Meal_selectFragment : Fragment() {
    lateinit var binding: FragmentMealSelectBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealSelectBinding.inflate(inflater, container, false)
        val view = binding.root

        val userdata = arguments?.getParcelable<Userdata>("userdata")
        Log.i("sdsd",userdata.toString())
        binding.back.setOnClickListener{
           Userdata.clear()
            findNavController().navigate(R.id.action_meal_selectFragment_to_profile_fragment)

        }
        binding.general.setOnClickListener{
//            val mealtype="general"
            // 클릭시 색 변경 + 다음버튼 눌렀을 때 이동
            val dataRoute=firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id")
            dataRoute.child("목표식단").setValue("일반식단")
            var bundle = bundleOf("Userdata" to userdata)
            findNavController().navigate(R.id.action_meal_selectFragment_to_calorySettingFragment,bundle)
        }
        return view
    }

}