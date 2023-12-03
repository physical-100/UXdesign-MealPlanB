package com.example.mealplanb.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.MealData
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentSpecificFoodBinding
import com.example.mealplanb.dataclass.food
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
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
    var isclickededitgram:Boolean= false
    lateinit var mealName:String
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거
    var isBookmarked = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 75 / 100
        // 기기 높이 대비 비율 설정 부분!!
        // 위 수치는 기기 높이 대비 80%로 다이얼로그 높이를 설정
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }




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
                //modifydata에서 받아오는건 여기로 저현
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


        val cancle = binding.cancle
        cancle.setOnClickListener {
            dismiss()
        }


        val bookmark=binding.bookmark
        bookmark.setOnClickListener {
            if(!isBookmarked){
                bookmark.setImageResource(R.drawable.bookmark_checked)
                //파이어베이스에 즐겨찾기 추가
                favoritesFoodToFirebase()
                isBookmarked = true
            }else{
                bookmark.setImageResource(R.drawable.bookmark_unchecked)
                //파이어베이스에서 즐겨찾기 제거
                //코드
                isBookmarked = false
            }

        }

// 바인딩
        val carboview=binding.carbotextView2
        val proteinview=binding.proteintextView2
        val fatview=binding.fattextView2
        val kcalview=binding.kcaltextView
        val foodamountview=binding.edtgram
        val perpersonbutton=binding.editamountfood


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
        perpersonbutton?.text="1인분 ("+String.format("%.1f",foodamount)+")"

        var editfoodamount=foodamount/specificfooddata!!.foodamount

        binding.editgramfood.setOnClickListener {
            isclickededitgram= true
            binding.edtgram.text = null
            foodamountview?.hint=String.format("%.1f",foodamount)
        }
        binding.editamountfood.setOnClickListener {
            isclickededitgram= false
            binding.edtgram.text = null
            foodamountview?.hint=String.format("%.1f",foodamount/specificfooddata!!.foodamount)
        }



        binding.plus.setOnClickListener{
            if(isclickededitgram==true){
                editfoodamount=foodamountview?.hint.toString().toDouble()+1
                foodamountview?.hint=String.format("%.1f",editfoodamount)
                carboview?.text=String.format("%.1f",carboperg*editfoodamount)
                proteinview?.text=String.format("%.1f",proteinperg*editfoodamount)
                fatview?.text=String.format("%.1f",fatperg*editfoodamount)
                kcalview?.text=String.format("%.1f",kcalperg*editfoodamount)

            }

            else{
            editfoodamount=foodamountview?.hint.toString().toDouble()+0.5
            foodamountview?.hint=String.format("%.1f",editfoodamount)
            carboview?.text=String.format("%.1f",carboperg*editfoodamount*foodamount)
            proteinview?.text=String.format("%.1f",proteinperg*editfoodamount*foodamount)
            fatview?.text=String.format("%.1f",fatperg*editfoodamount*foodamount)
            kcalview?.text=String.format("%.1f",kcalperg*editfoodamount*foodamount)
            }
        }

        binding.minus.setOnClickListener{
            if(isclickededitgram==true){
                editfoodamount=foodamountview?.hint.toString().toDouble()-1
                foodamountview?.hint=String.format("%.1f",editfoodamount)
                carboview?.text=String.format("%.1f",carboperg*editfoodamount)
                proteinview?.text=String.format("%.1f",proteinperg*editfoodamount)
                fatview?.text=String.format("%.1f",fatperg*editfoodamount)
                kcalview?.text=String.format("%.1f",kcalperg*editfoodamount)

            }else {
                editfoodamount = foodamountview?.hint.toString().toDouble() - 0.5
                foodamountview?.hint = String.format("%.1f", editfoodamount)
                carboview?.text = String.format("%.1f", carboperg * editfoodamount * foodamount)
                proteinview?.text = String.format("%.1f", proteinperg * editfoodamount * foodamount)
                fatview?.text = String.format("%.1f", fatperg * editfoodamount * foodamount)
                kcalview?.text = String.format("%.1f", kcalperg * editfoodamount * foodamount)
            }
        }

        binding.edtgram.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                // EditText의 키보드에서 "Done" 버튼이나 Enter 키를 눌렀을 때의 동작

                // EditText의 값을 가져옴
                val inputValue = binding.edtgram.text.toString()
                // 가져온 값을 사용하여 원하는 작업 수행
                // 예: 가져온 값이 빈 문자열이 아닌 경우
                if (inputValue.isNotEmpty()) {
                    val inputNumber = inputValue.toDoubleOrNull()
                    // inputNumber를 사용하여 원하는 작업 수행
                    // 예: Toast 메시지로 값 표시
                    if (inputNumber != null) {
                        // 변환에 성공한 경우
                       // Toast.makeText(this, "입력된 값: $inputNumber", Toast.LENGTH_SHORT).show()
                        if(isclickededitgram==true){
                            editfoodamount=inputNumber
                            foodamountview?.hint=String.format("%.1f",editfoodamount)
                            carboview?.text=String.format("%.1f",carboperg*editfoodamount)
                            proteinview?.text=String.format("%.1f",proteinperg*editfoodamount)
                            fatview?.text=String.format("%.1f",fatperg*editfoodamount)
                            kcalview?.text=String.format("%.1f",kcalperg*editfoodamount)

                        }


                    } else {
                        // 변환에 실패한 경우
                        //Toast.makeText(this, "올바른 숫자를 입력하세요.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 빈 문자열인 경우

                    //Toast.makeText(this, "값을 입력하세요.", Toast.LENGTH_SHORT).show()
                }

                // 이벤트 처리 완료를 알리기 위해 true 반환
                true
            } else {
                // 이벤트 처리를 계속 진행하기 위해 false 반환
                false
            }
        }




        binding.next2.setOnClickListener{
            if(isclickededitgram==true){
                editfoodamount=foodamountview?.hint.toString().toDouble()/specificfooddata!!.foodamount
                saveDataToFirebase(editfoodamount)
            }
            else {
                saveDataToFirebase(editfoodamount) //데이터베이스 저장
            }

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
    fun setOnNumberEnteredListener(listener: SpecificFood_Fragment.OnfoodEnteredListener) {
        this.foodaddlistener = listener
    }
    }




