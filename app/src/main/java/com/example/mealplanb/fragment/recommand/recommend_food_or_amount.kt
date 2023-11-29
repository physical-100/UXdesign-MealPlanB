package com.example.mealplanb.fragment.recommand


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.R

import com.example.mealplanb.databinding.FragmentRecommendFoodOrAmountBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class recommend_food_or_amount : Fragment() {
    // TODO: Rename and change types of parameters
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
            val recommendFragment = RecommendFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, recommendFragment)
            transaction?.commit()
            }

        binding.amount.setOnClickListener {
            // 선택한 값 Bundle에 담기
            val bundle = Bundle()
            bundle.putString("choice", "amount")

            // 선택 결과를 전달
            parentFragmentManager.setFragmentResult("Result", bundle)
            //얼마나 먹을지 골랐을떄
            itemClickListener?.onItemClick("amount")

            val searchFragment = Amountrecommend()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, searchFragment)
            transaction?.commit()
        }

        return binding.root
    }


}