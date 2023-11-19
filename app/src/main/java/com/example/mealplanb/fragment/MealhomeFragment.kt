package com.example.mealplanb.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.R
import com.example.mealplanb.adapter.ItemSpacingDecoration
import com.example.mealplanb.adapter.MealaddAdapter
import com.example.mealplanb.databinding.FragmentDailyweightBinding
import com.example.mealplanb.databinding.FragmentMealhomeBinding

class MealhomeFragment : Fragment() {
    lateinit var binding: FragmentMealhomeBinding
    lateinit var mealaddAdapter: MealaddAdapter
    private var meals = mutableListOf("식단 1", "식단 2", "식단 3")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealhomeBinding.inflate(inflater, container, false)
        // Initialize RecyclerView for horizontal scrolling list of meals
        val itemSpacingDecoration = ItemSpacingDecoration(resources.getDimensionPixelSize(R.dimen.item_spacing))
        binding.recyclerviewMeal.addItemDecoration(itemSpacingDecoration)
        // 아이템 클릭 이벤트 처리
        mealaddAdapter = MealaddAdapter(meals) { clickedMeal ->
            // 클릭된 아이템에 대한 화면 전환 로직을 여기에 작성
            // 예: Navigation Component를 사용한 화면 전환
            findNavController().navigate(R.id.action_mainFragment_to_add_Diet_Fragment)
        }
        binding.recyclerviewMeal.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerviewMeal.adapter = mealaddAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        //         Set up button click to add a new meal
        binding.btnAddMeal.setOnClickListener {
            val newMeal = "식단 ${meals.size + 1}"
            meals.add(newMeal)
            mealaddAdapter = MealaddAdapter(meals) { clickedMeal ->
                // 클릭된 아이템에 대한 화면 전환 로직을 여기에 작성
                // 예: Navigation Component를 사용한 화면 전환
                findNavController().navigate(R.id.action_mainFragment_to_add_Diet_Fragment)
            }
            binding.recyclerviewMeal.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerviewMeal.adapter = mealaddAdapter

        }
    }

    // Function to dynamically add a new meal to the ScrollView
    private fun addMealToScrollView(mealName: String) {
        val newMealView = layoutInflater.inflate(R.layout.item_meal, null)
        // Customize the newMealView with meal details if needed
        // For example, you can find TextViews in newMealView and set their text

        // Add the new meal view to the linear layout inside ScrollView

    }
}
