package com.example.mealplanb.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView


import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.R
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
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거
    lateinit var carboview: TextView
    lateinit var proteinview: TextView
    lateinit var fatview: TextView

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            mealsUpdate(mealcount)
            binding = FragmentMealhomeBinding.inflate(inflater, container, false)
            Adapter = MealaddAdapter(meals,
                onItemClick = { clickedMeal ->
                    // 클릭된 아이템에 대한 화면 전환 로직을 여기에 작성
                    // 예: Navigation Component를 사용한 화면 전환
                    Log.i("식단은 무엇?", clickedMeal)

                    // Bundle을 생성하고 클릭된 Meal의 이름을 전달
                    val bundle = Bundle()
                    bundle.putString("mealName", clickedMeal)

                    // Navigation Component를 사용한 화면 전환 및 Bundle 전달
                    findNavController().navigate(R.id.action_mainFragment_to_add_Diet_Fragment, bundle)
                },
                onDeleteClick = { deletedMeal ->
                    // 삭제 버튼이 클릭되었을 때의 동작을 여기에 정의
                    meals.remove(deletedMeal)
                    mealcount= meals.size
                    mealsUpdate(mealcount)
                    initrecyclerview(mealsUpdate(mealcount))
                })
            initrecyclerview(meals)

            //식단 1~3까지 만약 사용자가 식단 4까지 추가했으면 식단 4까지 확인해서 for문 돌려서
            //meallistfromDatabase로 부터 하나하나 정보를 다 받아온다.
            for(meal in meals){
                meallistfromDatabase(meal)
                Log.i("meal", meal)
            }
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
//        //         Set up button click to add a new meal
            binding.btnAddMeal.setOnClickListener {
                val newMeal = "식단 ${meals.size + 1}"
                meals.add(newMeal)
                Log.i("meals",meals.toString())
                initrecyclerview(meals)
            }
            return binding.root
        }

        private fun initrecyclerview(data: MutableList<String>){

            // 아이템 클릭 이벤트 처리
            binding.recyclerviewMeal.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerviewMeal.adapter = Adapter
            Adapter.notifyDataSetChanged()
        }

        // Function to dynamically add a new meal to the ScrollView
        private fun mealsUpdate(count:Int): MutableList<String> {
            meals.clear()
            for (i in 1..count){
                meals.add("식단 ${i}")
            }
            return meals
        }

        private fun meallistfromDatabase(clickedMeal:String) {
            val dataRoute = firebaseDatabase
                .getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단기입/$realTime/$clickedMeal")
            dataRoute.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (Fuserdata in dataSnapshot.children) {
                        carboview = view!!.findViewById<TextView>(R.id.carbotextView3)
                        proteinview = view!!.findViewById<TextView>(R.id.proteintextView3)
                        fatview = view!!.findViewById<TextView>(R.id.fattextView3)
                        val key = Fuserdata.key.toString()
                        val value1 = JsonPath.parse(Fuserdata.value)
                        if (key != null) {
                            //val foodname = value1.read("$['식품이름']") as String
                            //val foodbrand = value1.read("$['가공업체']") as String
                            val foodcarbo = (value1.read("$['탄수화물']") as String).toDouble()
                            Log.i("carbobobo", "onDataChange: ${foodcarbo}")
                            val foodprotein = (value1.read("$['단백질']") as String).toDouble()
                            val foodfat = (value1.read("$['지방']") as String).toDouble()
                            //val foodamount = (value1.read("$['섭취량']") as String).toDouble()
                           // val foodkcal = (value1.read("$['열량']") as String).toDouble()
                            Log.i("carboview", "onDataChange:${carboview?.text} ")
                            carboview?.text =
                                String.format("%.1f",carboview?.text.toString().toDouble() + foodcarbo)
                            proteinview?.text =
                                String.format("%.1f",proteinview?.text.toString().toDouble() + foodprotein)
                            fatview?.text =
                                String.format("%.1f",fatview?.text.toString().toDouble() + foodfat)
                        }


                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }






