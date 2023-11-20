package com.example.mealplanb.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentDailyStaticBinding
import com.example.mealplanb.databinding.FragmentEditweightBinding
import com.example.mealplanb.databinding.FragmentSpecificFoodBinding
import com.example.mealplanb.dataclass.food
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SpecificFood_Fragment : BottomSheetDialogFragment()  {
    interface OnNumberEnteredListener {
        fun onNumberEntered(number: Int)
    }
    private var listener: OnNumberEnteredListener? = null
    lateinit var binding: FragmentSpecificFoodBinding
    private lateinit var showfood : TextView
    private lateinit var specificfooddata: food
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("CurrentTime", "Formatted Time: $realTime")


    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding= FragmentSpecificFoodBinding.inflate(inflater, container, false)
        specificfooddata= arguments?.getParcelable<food>("add food")!!
       // data = arguments?.getParcelable("add food", food)!!
        Log.i("1234555", "$specificfooddata ")
        //일단 정보 받아오는거 성공했다이

        val plusmeal =binding.plusmeal
        plusmeal.setOnClickListener{
            saveDataToFirebase() //데이터베이스 저장
           // findNavController().navigate(R.id.action_specificFood_Fragment_to_add_Diet_Fragment)
            dismiss()
        }

        val oftenfoodadd=binding.oftenfood
        oftenfoodadd.setOnClickListener {
            favoritesFoodToFirebase()
        }
        val carboview=binding.carbotextView2
        val proteinview=binding.proteintextView2
        val fatview=binding.fattextView2
        val kcalview=binding.kcaltextView
        val foodamountview=binding.edtgram
        val perpersonbutton=binding.switch100g

        val carboperg=specificfooddata.foodcarbo/specificfooddata.foodamount
        val proteinperg=specificfooddata.foodprotein/specificfooddata.foodamount
        val fatperg=specificfooddata.foodfat/specificfooddata.foodamount
        val kcalperg=specificfooddata.foodcal/specificfooddata.foodamount

        val foodamount=specificfooddata.foodamount
        //100g을 1인분 기준
        foodamountview?.hint="${foodamount/100.0}"
        carboview?.text="${carboperg*100.0}"
        proteinview?.text="${proteinperg*100.0}"
        fatview?.text="${fatperg*100.0}"
        kcalview?.text="${kcalperg*100.0}"
        perpersonbutton?.text="${foodamount}"

        val plusbutton=binding.plus
        plusbutton.setOnClickListener{
            val editfoodamount=foodamountview?.hint.toString().toDouble()+0.5
            foodamountview?.hint=editfoodamount.toString()
            carboview?.text="${carboperg*editfoodamount*100.0.toInt()}"
            proteinview?.text="${proteinperg*editfoodamount*100.0.toInt()}"
            fatview?.text="${fatperg*editfoodamount*100.0.toInt()}"
            kcalview?.text="${kcalperg*editfoodamount*100.0.toInt()}"



        }
        val minusbutton=binding.minus
        minusbutton.setOnClickListener{
            val editfoodamount=foodamountview?.hint.toString().toDouble()-0.5
            foodamountview?.hint=editfoodamount.toString()
            carboview?.text="${carboperg*editfoodamount*100.0.toInt()}"
            proteinview?.text="${proteinperg*editfoodamount*100.0.toInt()}"
            fatview?.text="${fatperg*editfoodamount*100.0.toInt()}"
            kcalview?.text="${fatperg*editfoodamount*100.0.toInt()}"

        }








        // Inflate the layout for this fragment
        return binding.root
    }






    private fun saveDataToFirebase() {
        // Get a reference to the database
        val dataRoute=firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단기입/$realTime")
        val foodkey=dataRoute.child("${specificfooddata.foodname}").push().key
        val newData=mapOf(
            "식품이름" to "${specificfooddata.foodname}",
            "열량" to "${specificfooddata.foodcal}",
            "섭취량" to "${specificfooddata.foodamount}",
            "가공업체" to "${specificfooddata.foodbrand}",
            "탄수화물" to "${specificfooddata.foodcarbo}",
            "단백질" to "${specificfooddata.foodprotein}",
            "지방" to "${specificfooddata.foodfat}"
        )
        if(foodkey!=null){
            dataRoute.child("식단1").child(foodkey).updateChildren(newData)
            //여기서 사용자가 선택한 "식단1"의 정보를 가져와야한다.
            //식단2 선택했으면 식단2에다가 추가해야하니까
        }

    }

    private fun favoritesFoodToFirebase(){
        val dataRoute=firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단 추천/자주먹는음식(즐겨찾기)")
        val foodkey=dataRoute.child("${specificfooddata.foodname}").push().key


        val newData=mapOf(
            "식품이름" to "${specificfooddata.foodname}",
            "열량" to "${specificfooddata.foodcal}",
            "섭취량" to "${specificfooddata.foodamount}",
            "가공업체" to "${specificfooddata.foodbrand}",
            "탄수화물" to "${specificfooddata.foodcarbo}",
            "단백질" to "${specificfooddata.foodprotein}",
            "지방" to "${specificfooddata.foodfat}"
        )
        if (foodkey != null) {
            dataRoute.child(foodkey).updateChildren(newData)
        }

    }











    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        }
    fun setOnNumberEnteredListener(listener: OnNumberEnteredListener) {
        this.listener = listener
    }
    }




