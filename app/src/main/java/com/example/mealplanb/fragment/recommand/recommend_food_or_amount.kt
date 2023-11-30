package com.example.mealplanb.fragment.recommand


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.R

import com.example.mealplanb.databinding.FragmentRecommendFoodOrAmountBinding


class recommend_food_or_amount : Fragment() {
    lateinit var binding:FragmentRecommendFoodOrAmountBinding
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRecommendFoodOrAmountBinding.inflate(inflater,container,false)

        binding.recommend.setOnClickListener {
                // 선택한 값 Bundle에 담기
                val bundle = Bundle()
                bundle.putString("choice", "food")

                // 선택 결과를 전달
                parentFragmentManager.setFragmentResult("choiceResult", bundle)
            itemClickListener?.onItemClick("food")
//            Log.i("bundle",bundle.toString())
            }

        binding.amount.setOnClickListener {
            // 선택한 값 Bundle에 담기
            val bundle = Bundle()
            bundle.putString("choice", "amount")

            // 선택 결과를 전달
            parentFragmentManager.setFragmentResult("Result", bundle)
            //얼마나 먹을지 골랐을떄
            itemClickListener?.onItemClick("amount")

        }

        return binding.root
    }


}