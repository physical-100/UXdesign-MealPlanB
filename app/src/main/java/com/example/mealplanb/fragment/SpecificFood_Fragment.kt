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
import com.example.mealplanb.MealData
import com.example.mealplanb.R
import com.example.mealplanb.UserManager
import com.example.mealplanb.Userdata
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
    private var Usermealdata: MealData? = null
    lateinit var binding: FragmentSpecificFoodBinding
    private lateinit var specificfooddata: food
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    lateinit var mealName:String
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

        val arguments = arguments
        if (arguments != null) {
            specificfooddata= arguments?.getParcelable<food>("add food")!!

            // "mealName" 키를 사용하여 데이터 가져오기
            mealName = arguments.getString("mealName").toString()

            // 가져온 데이터를 사용하여 필요한 작업 수행
          //  Log.i("SpecificFood_Fragment", "addFood: $addFood, mealName: $mealName")
        }

       // specificfooddata= arguments?.getParcelable<food>("add food")!!
        //mealName=arguments.getString("mealName").toString()
       // data = arguments?.getParcelable("add food", food)!!
        Log.i("1234555", "$specificfooddata ")
        //일단 정보 받아오는거 성공했다이


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

        foodamountview?.hint=String.format("%.1f",foodamount/foodamount)
        carboview?.text=String.format("%.1f",carboperg*foodamount)
        proteinview?.text=String.format("%.1f",proteinperg*foodamount)
        fatview?.text=String.format("%.1f",fatperg*foodamount)
        kcalview?.text=String.format("%.1f",kcalperg*foodamount)
        perpersonbutton?.text=String.format("%.1f",foodamount)
        var editfoodamount=foodamount/specificfooddata.foodamount

        val plusbutton=binding.plus
        plusbutton.setOnClickListener{
            editfoodamount=foodamountview?.hint.toString().toDouble()+0.5
            foodamountview?.hint=String.format("%.1f",editfoodamount)
            carboview?.text=String.format("%.1f",carboperg*editfoodamount*foodamount)
            proteinview?.text=String.format("%.1f",proteinperg*editfoodamount*foodamount)
            fatview?.text=String.format("%.1f",fatperg*editfoodamount*foodamount)
            kcalview?.text=String.format("%.1f",kcalperg*editfoodamount*foodamount)
        }
        val minusbutton=binding.minus
        minusbutton.setOnClickListener{
            editfoodamount=foodamountview?.hint.toString().toDouble()-0.5
            foodamountview?.hint=String.format("%.1f",editfoodamount)
            carboview?.text=String.format("%.1f",carboperg*editfoodamount*foodamount)
            proteinview?.text=String.format("%.1f",proteinperg*editfoodamount*foodamount)
            fatview?.text=String.format("%.1f",fatperg*editfoodamount*foodamount)
            kcalview?.text=String.format("%.1f",kcalperg*editfoodamount*foodamount)
        }

        val plusmeal =binding.plusmeal
        plusmeal.setOnClickListener{
            saveDataToFirebase(editfoodamount) //데이터베이스 저장
            //findNavController().navigate(R.id.action_specificFood_Fragment_to_add_Diet_Fragment)
            //dismiss()
        }








        // Inflate the layout for this fragment
        return binding.root
    }






    private fun saveDataToFirebase(editfoodamount:Double) {
        // Get a reference to the database
        UserManager.addMealData(MealData( realTime,
            mealName,
            specificfooddata.foodname,
            specificfooddata.foodbrand,
            editfoodamount*specificfooddata.foodcal,
            editfoodamount*specificfooddata.foodamount,
            editfoodamount*specificfooddata.foodcarbo,
            editfoodamount*specificfooddata.foodprotein,
            editfoodamount*specificfooddata.foodfat ))

        Log.i("확인",UserManager.getMealData().toString())
//        val dataRoute=firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단기입/$realTime")
//        val foodkey=dataRoute.child("${specificfooddata.foodname}").push().key
//        val newData=mapOf(
//            "식품이름" to "${specificfooddata.foodname}",
//            "열량" to "${specificfooddata.foodcal}",
//            "섭취량" to "${specificfooddata.foodamount}",
//            "가공업체" to "${specificfooddata.foodbrand}",
//            "탄수화물" to "${specificfooddata.foodcarbo}",
//            "단백질" to "${specificfooddata.foodprotein}",
//            "지방" to "${specificfooddata.foodfat}"
//        )
//        if(foodkey!=null){
//            dataRoute.child("식단1").child(foodkey).updateChildren(newData)
//            //여기서 사용자가 선택한 "식단1"의 정보를 가져와야한다.
//            //식단2 선택했으면 식단2에다가 추가해야하니까
//        }

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




