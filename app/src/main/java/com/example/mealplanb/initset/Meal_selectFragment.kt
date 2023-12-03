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
import com.example.mealplanb.UserManager
import com.example.mealplanb.Userdata
import com.example.mealplanb.databinding.FragmentMealSelectBinding
import com.google.firebase.database.FirebaseDatabase


class Meal_selectFragment : Fragment() {
    lateinit var binding: FragmentMealSelectBinding
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private var mealSelectFlag = false
//    private var Userdata: Userdata? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealSelectBinding.inflate(inflater, container, false)
        val view = binding.root

//        val userdata = arguments?.getParcelable<Userdata>("userdata")
//        Log.i("sdsd",userdata.toString())
        val userdata = UserManager.getUserData()
        var name =userdata!!.username
        var gender=userdata.gender
        var age=userdata.age
        var height=userdata.height
        var start_weight=userdata.start_weight
        var goal_weight=userdata.goal_weight
        var activitytype=userdata.activitytype
        var mealtype=""

        binding.back.setOnClickListener{
           UserManager.clearUserdata()
            findNavController().navigate(R.id.action_meal_selectFragment_to_profile_fragment)
        }
//        val bundle = bundleOf("Userdata" to userdata)
        binding.apply {
            general.setOnClickListener {
            mealtype="일반식단"
                // 클릭시 색 변경 + 다음버튼 눌렀을 때 이동
                mealSelectFlag = true
                binding.general.setBackgroundResource(R.drawable.image_border)
                binding.hellchang.setBackgroundResource(0)
                binding.vegan.setBackgroundResource(0)
                binding.diabetes.setBackgroundResource(0)
                val dataRoute = firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id")
                dataRoute.child("목표식단").setValue("일반식단")
            }
            hellchang.setOnClickListener {
                mealtype="운동식단"
                mealSelectFlag = true
                binding.general.setBackgroundResource(0)
                binding.hellchang.setBackgroundResource(R.drawable.image_border)
                binding.vegan.setBackgroundResource(0)
                binding.diabetes.setBackgroundResource(0)
                val dataRoute = firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id")
                dataRoute.child("목표식단").setValue("운동식단")
            }
            vegan.setOnClickListener {
                mealtype="비건"
                mealSelectFlag = true
                binding.general.setBackgroundResource(0)
                binding.hellchang.setBackgroundResource(0)
                binding.vegan.setBackgroundResource(R.drawable.image_border)
                binding.diabetes.setBackgroundResource(0)
                val dataRoute = firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id")
                dataRoute.child("목표식단").setValue("비건식단")
            }
            diabetes.setOnClickListener {
                mealtype="당뇨"
                mealSelectFlag = true
                binding.general.setBackgroundResource(0)
                binding.hellchang.setBackgroundResource(0)
                binding.vegan.setBackgroundResource(0)
                binding.diabetes.setBackgroundResource(R.drawable.image_border)
                val dataRoute = firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id")
                dataRoute.child("목표식단").setValue("당뇨식단")
            }
            next2.setOnClickListener {
                if(checkbox.isChecked){
                    findNavController().navigate(
                        R.id.action_meal_selectFragment_to_calorySettingFragment)
                    UserManager.setUserData(Userdata(name,gender ,age, height, start_weight, goal_weight, activitytype,mealtype))
                }else{
                    findNavController().navigate(R.id.action_meal_selectFragment_to_mainFragment)
                }
            }
        }
        return view
    }

}