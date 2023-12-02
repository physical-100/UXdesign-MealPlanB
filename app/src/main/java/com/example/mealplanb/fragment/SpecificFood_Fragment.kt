package com.example.mealplanb.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.mealplanb.MealData
import com.example.mealplanb.databinding.FragmentSpecificFoodBinding
import com.example.mealplanb.dataclass.food
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SpecificFood_Fragment : BottomSheetDialogFragment()  {
    interface OnfoodEnteredListener {
        fun onfoodEntered(mealData: MealData)
    }

    private var foodaddlistener: OnfoodEnteredListener?=null
    private var temporalmealdata: MealData? = null
    private var temporalmealdatalist= arrayListOf<MealData>()
    lateinit var binding: FragmentSpecificFoodBinding
    private var specificfooddata: food? = null
    private var modifyfooddata: MealData? = null
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    lateinit var mealName:String
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거




    fun setOnItemClickListener(listener: OnfoodEnteredListener){
        foodaddlistener = listener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.i("CurrentTime", "Formatted Time: $realTime")
        //인자를 받아와서 저장
        if (arguments != null) {
            specificfooddata= arguments?.getParcelable<food>("add food")
            // detail에서 수정된 값을 받아옴
            modifyfooddata = arguments?.getParcelable<MealData>("modify food")
            if(modifyfooddata!=null){
                //modifydata에서 받아오는건 여기로 저장
                specificfooddata = food(
                    modifyfooddata!!.foodname,
                    modifyfooddata!!.foodbrand,
                    modifyfooddata!!.foodcal,
                    modifyfooddata!!.foodamount,
                    modifyfooddata!!.foodcarbo,
                    modifyfooddata!!.foodprotein,
                    modifyfooddata!!.foodfat)
            }

            // "mealName" 키를 사용하여 데이터 가져오기
            mealName = arguments?.getString("mealName").toString()

        }


    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding= FragmentSpecificFoodBinding.inflate(inflater, container, false)

//        if (arguments != null) {
//            specificfooddata= arguments?.getParcelable<food>("add food")!!
//            // detail에서 수정된 값을 받아옴
//            modifyfooddata = arguments?.getParcelable<MealData>("modify food")!!
//
//            // "mealName" 키를 사용하여 데이터 가져오기
//            mealName = arguments?.getString("mealName").toString()
//
//        }

        Log.i("1234555", "$specificfooddata ")
        //일단 정보 받아오는거 성공했다이

        binding.oftenfood.setOnClickListener {
            favoritesFoodToFirebase()
        }

// 바인딩
        val carboview=binding.carbotextView2
        val proteinview=binding.proteintextView2
        val fatview=binding.fattextView2
        val kcalview=binding.kcaltextView
        val foodamountview=binding.edtgram
        val perpersonbutton=binding.switch100g


        // specificfooddata로 수정

        val carboperg=specificfooddata!!.foodcarbo/specificfooddata!!.foodamount
        val proteinperg=specificfooddata!!.foodprotein/specificfooddata!!.foodamount
        val fatperg=specificfooddata!!.foodfat/specificfooddata!!.foodamount
        val kcalperg=specificfooddata!!.foodcal/specificfooddata!!.foodamount
        val foodamount=specificfooddata!!.foodamount

        foodamountview?.hint=String.format("%.1f",foodamount/foodamount)
        carboview?.text=String.format("%.1f",carboperg*foodamount)
        proteinview?.text=String.format("%.1f",proteinperg*foodamount)
        fatview?.text=String.format("%.1f",fatperg*foodamount)
        kcalview?.text=String.format("%.1f",kcalperg*foodamount)
        perpersonbutton?.text=String.format("%.1f",foodamount)
        var editfoodamount=foodamount/specificfooddata!!.foodamount




        binding.plus.setOnClickListener{
            editfoodamount=foodamountview?.hint.toString().toDouble()+0.5
            foodamountview?.hint=String.format("%.1f",editfoodamount)
            carboview?.text=String.format("%.1f",carboperg*editfoodamount*foodamount)
            proteinview?.text=String.format("%.1f",proteinperg*editfoodamount*foodamount)
            fatview?.text=String.format("%.1f",fatperg*editfoodamount*foodamount)
            kcalview?.text=String.format("%.1f",kcalperg*editfoodamount*foodamount)
        }

        binding.minus.setOnClickListener{
            editfoodamount=foodamountview?.hint.toString().toDouble()-0.5
            foodamountview?.hint=String.format("%.1f",editfoodamount)
            carboview?.text=String.format("%.1f",carboperg*editfoodamount*foodamount)
            proteinview?.text=String.format("%.1f",proteinperg*editfoodamount*foodamount)
            fatview?.text=String.format("%.1f",fatperg*editfoodamount*foodamount)
            kcalview?.text=String.format("%.1f",kcalperg*editfoodamount*foodamount)
        }

        binding.plusmeal.setOnClickListener{
            saveDataToFirebase(editfoodamount) //데이터베이스 저장
            dismiss()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun saveDataToFirebase(editfoodamount:Double) {
        // Get a reference to the database
//        UserManager.addMealData(MealData( realTime,
//            mealName,
//            specificfooddata.foodname,
//            specificfooddata.foodbrand,
//            editfoodamount*specificfooddata.foodcal,
//            editfoodamount*specificfooddata.foodamount,
//            editfoodamount*specificfooddata.foodcarbo,
//            editfoodamount*specificfooddata.foodprotein,
//            editfoodamount*specificfooddata.foodfat ))

        //mealdata를 보냄
        foodaddlistener?.onfoodEntered(
            MealData(realTime,mealName,
            specificfooddata!!.foodname,
            specificfooddata!!.foodbrand,
            editfoodamount*specificfooddata!!.foodcal,
            editfoodamount*specificfooddata!!.foodamount,
            editfoodamount*specificfooddata!!.foodcarbo,
            editfoodamount*specificfooddata!!.foodprotein,
            editfoodamount*specificfooddata!!.foodfat )
        )
    }

    private fun favoritesFoodToFirebase(){
        val dataRoute=firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단 추천/자주먹는음식(즐겨찾기)")
        val foodkey=dataRoute.child("${specificfooddata!!.foodname}").push().key


        val newData=mapOf(
            "식품이름" to "${specificfooddata!!.foodname}",
            "열량" to "${specificfooddata!!.foodcal}",
            "섭취량" to "${specificfooddata!!.foodamount}",
            "가공업체" to "${specificfooddata!!.foodbrand}",
            "탄수화물" to "${specificfooddata!!.foodcarbo}",
            "단백질" to "${specificfooddata!!.foodprotein}",
            "지방" to "${specificfooddata!!.foodfat}"
        )
        if (foodkey != null) {
            dataRoute.child(foodkey).updateChildren(newData)
        }

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        }
    fun setOnNumberEnteredListener(listener: OnfoodEnteredListener) {
        this.foodaddlistener = listener
    }
    }




