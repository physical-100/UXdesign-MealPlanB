package com.example.mealplanb.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentDailyweightBinding
import com.example.mealplanb.databinding.FragmentStaticsBinding
import com.example.mealplanb.fragment.editweightFragment

class DailyweightFragment : Fragment(),editweightFragment.OnNumberEnteredListener  {
    lateinit var binding: FragmentDailyweightBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDailyweightBinding.inflate(inflater, container, false)


        binding.addweightbutton.setOnClickListener {
            val bottomSheetFragment = editweightFragment()

            // 리스너 설정
            bottomSheetFragment.setOnNumberEnteredListener(this)

            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
        return binding.root
    }

    override fun onNumberEntered(number: Int) {
        binding.dailyWeight.text = "${number} Kg"
        binding.weightComment.text= "어제보다 - 0.6kg"
    }


}