package com.example.mealplanb.fragment.recommand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentMealcheckBinding

class MealcheckFragment : Fragment() {
    private var itemClickListener: OnItemClickListener? = null
    lateinit var binding:FragmentMealcheckBinding

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
        binding = FragmentMealcheckBinding.inflate(inflater,container,false)


        binding.mealselect.setOnClickListener {
            itemClickListener?.onItemClick("식단 확정")
        }
        binding.othermeal.setOnClickListener {
            itemClickListener?.onItemClick("othermeal")

        }
        return binding.root
    }

}