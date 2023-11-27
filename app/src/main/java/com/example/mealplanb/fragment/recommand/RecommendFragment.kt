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

/**
 * A simple [Fragment] subclass.
 * Use the [RecommendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class RecommendFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding:FragmentRecommendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        binding.cheat.setOnClickListener {  }
        binding.like.setOnClickListener {  }
        binding.popluar.setOnClickListener {  }

    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            RecommendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}