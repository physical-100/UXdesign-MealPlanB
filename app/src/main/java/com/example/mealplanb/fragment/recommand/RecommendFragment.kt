package com.example.mealplanb.fragment.recommand

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentRecommendBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecommendFragment : Fragment() {

    lateinit var binding: FragmentRecommendBinding
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
        val currentDate = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Date())
        binding = FragmentRecommendBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cheat.setOnClickListener {
            itemClickListener?.onItemClick("cheat")
        }
        binding.like.setOnClickListener {
            itemClickListener?.onItemClick("like")
        }
        binding.popluar.setOnClickListener {
            itemClickListener?.onItemClick("popular")
        }

    }

}