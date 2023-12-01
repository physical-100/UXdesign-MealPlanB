package com.example.mealplanb.fragment

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView


import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.AllListNutrition
import com.example.mealplanb.R
import com.example.mealplanb.UserManager

import com.example.mealplanb.adapter.MealaddAdapter
import com.example.mealplanb.databinding.FragmentMealhomeBinding

import com.example.mealplanb.databinding.ItemMealBinding
import com.example.mealplanb.dataclass.food
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jayway.jsonpath.JsonPath
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class MealhomeFragment : Fragment() {
    lateinit var binding: FragmentMealhomeBinding
    lateinit var bindingmeal: ItemMealBinding
    lateinit var Adapter: MealaddAdapter
    var mealcount:Int=3
    var meals = mutableListOf<String>(
    )
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거
    private val mealDataMap = mutableMapOf<String, onlyCarProFat>()
    var AllListNutritionList=ArrayList<AllListNutrition>(

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mealcount  =getMealCount()
    }
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            meals=mealsUpdate(mealcount)
            binding = FragmentMealhomeBinding.inflate(inflater,container,false)
            Adapter = MealaddAdapter(meals,mealDataMap,
                onItemClick = { clickedMeal ->
                    // 클릭된 아이템에 대한 화면 전환 로직을 여기에 작성
                    // 예: Navigation Component를 사용한 화면 전환
                    Log.i("식단은 무엇?", mealDataMap[clickedMeal].toString())

                    // Bundle을 생성하고 클릭된 Meal의 이름을 전달
                    val bundle = Bundle()
                    bundle.putString("mealName", clickedMeal)
//                    if(mealDataMap[clickedMeal]!=onlyCarProFat(carbo=0.0, protein=0.0, fat=0.0)){
                    if(mealDataMap[clickedMeal]!=null||mealDataMap[clickedMeal]!=onlyCarProFat(carbo=0.0, protein=0.0, fat=0.0)){
                        findNavController().navigate(R.id.action_mainFragment_to_mealDetailFragment, bundle)

                    }else{
                    // Navigation Component를 사용한 화면 전환 및 Bundle 전달
                    findNavController().navigate(R.id.action_mainFragment_to_add_Diet_Fragment, bundle)}
                },
                onDeleteClick = { deletedMeal ->
                    // 삭제 버튼이 클릭되었을 때의 동작을 여기에 정의
                    AllListNutritionList = UserManager.getAllListNutritionList()

                    val mealsToDelete = ArrayList<AllListNutrition>()

                    for (i in AllListNutritionList) {
                        if (i.mealname == deletedMeal) {
                            mealsToDelete.add(i)
                        }
                    }

                    AllListNutritionList.removeAll(mealsToDelete)
                    meals.remove(deletedMeal)
                    initrecyclerview(meals)
                }
            )
            initrecyclerview(meals)

            //식단 1~3까지 만약 사용자가 식단 4까지 추가했으면 식단 4까지 확인해서 for문 돌려서
            //meallistfromDatabase로 부터 하나하나 정보를 다 받아온다.
//           getmealcout()

            for(i in  1..getMealCount()){
                meallistfromDatabase("식단 ${i}")
            }
            return binding.root
        }


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
//        //         Set up button click to add a new meal
            binding.btnAddMeal.setOnClickListener {
                val newMeal = "식단 ${meals.size + 1}"
                meals.add(newMeal)
                for(meal in meals){
                    meallistfromDatabase(meal)
                }
                initrecyclerview(meals)
            }
        }
    private fun getMealCount(): Int {
        AllListNutritionList = UserManager.getAllListNutritionList()
        var count: Int = 0
        var prevMealName: String? = null

        for (i in AllListNutritionList) {
            val mealName = i.mealname

            // 이전 식단 이름과 현재 식단 이름이 다르면 count 증가
            if (mealName != prevMealName) {
                count++
                prevMealName = mealName
            }
        }
        if (count<=3){
            return 3
        }
        else return count
    }
    private fun mealsUpdate(count:Int): MutableList<String> {
        meals.clear()
        for (i in 1..count){
            meals.add("식단 ${i}")
        }
        return meals
    }

        private fun initrecyclerview(data: MutableList<String>){
        binding.recyclerviewMeal.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerviewMeal.adapter = Adapter
    }
    private fun meallistfromDatabase(clickedMeal: String) {
        //초기에 firebase에서 받아온거랑 내가 식단 기입해서 추가해준것도 추가해서 받아온다.
        AllListNutritionList = UserManager.getAllListNutritionList()
        var permealtotalCarbo = 0.0
        var permealtotalProtein = 0.0
        var permealtotalFat = 0.0
        var permealtotalkcal = 0.0

        for (allListNutrion in AllListNutritionList) {
            if (clickedMeal == allListNutrion.mealname) {
                permealtotalCarbo += allListNutrion.carbo
                permealtotalProtein += allListNutrion.protein
                permealtotalFat += allListNutrion.fat
                permealtotalkcal += allListNutrion.kcal
            }
            // Function to dynamically add a new meal to the ScrollView

            mealDataMap[clickedMeal] =
                onlyCarProFat(permealtotalCarbo, permealtotalProtein, permealtotalFat)
            Log.i(
                "확인",mealDataMap[clickedMeal].toString()
            )
           initrecyclerview(meals)
        }
    }


        data class onlyCarProFat(val carbo: Double, val protein: Double, val fat: Double)
    }


