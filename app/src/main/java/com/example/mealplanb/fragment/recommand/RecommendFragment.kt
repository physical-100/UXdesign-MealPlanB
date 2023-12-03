package com.example.mealplanb.fragment.recommand

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentRecommendBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecommendFragment : Fragment() {

    lateinit var binding: FragmentRecommendBinding
    private var itemClickListener: OnItemClickListener? = null
    var availability:Boolean=true

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        availability = arguments?.getBoolean("available_cheat")!!
        val arg= arguments?.getBoolean("available_cheat")!!
        Log.i(" 로그",arg.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val currentDate = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Date())
        binding = FragmentRecommendBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(availability!=false) {
            binding.cheat.setOnClickListener {
                itemClickListener?.onItemClick("cheat")
            }
            binding.like.setOnClickListener {
                itemClickListener?.onItemClick("like")
            }
            binding.popular.setOnClickListener {
                itemClickListener?.onItemClick("popular")
            }
        }
        else{
            binding.cheat.setOnClickListener {
                Toast.makeText(requireContext(), "치팅 식단 추천은 불가능 합니다", Toast.LENGTH_SHORT).show()

            }
            binding.like.setOnClickListener {
                itemClickListener?.onItemClick("like")
            }
            binding.popular.setOnClickListener {
                Toast.makeText(requireContext(), "인기 있는 식단 추천은 불가능 합니다", Toast.LENGTH_SHORT).show()
            }

        }

    }

}