package com.example.mealplanb.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.MealData
import com.example.mealplanb.R
import com.example.mealplanb.UserManager
import com.example.mealplanb.Userdata
import com.example.mealplanb.databinding.FragmentMealDetailBinding


class MealDetailFragment : Fragment() {
    lateinit var binding:FragmentMealDetailBinding
    lateinit var mealName:String
    lateinit var UsermealdataList:ArrayList<MealData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mealName = arguments?.getString("mealName").toString()
        UsermealdataList = UserManager.getMealData()!!
        binding = FragmentMealDetailBinding.inflate(inflater,container,false)


        return binding.root
    }

}