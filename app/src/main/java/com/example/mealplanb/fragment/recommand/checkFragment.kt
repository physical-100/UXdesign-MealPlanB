package com.example.mealplanb.fragment.recommand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentCheckBinding
class checkFragment : Fragment() {
  lateinit var binding:FragmentCheckBinding
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
        binding= FragmentCheckBinding.inflate(inflater,container,false)
        binding.add.setOnClickListener {
            itemClickListener?.onItemClick("check")
        }
        binding.back.setOnClickListener {
            itemClickListener?.onItemClick("backtosearch")
        }


        return binding.root
    }
}