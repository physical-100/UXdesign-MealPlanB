package com.example.mealplanb.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.R
import com.example.mealplanb.TodayWeight
import com.example.mealplanb.UserManager
import com.example.mealplanb.databinding.FragmentEditweightBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class editweightFragment : BottomSheetDialogFragment() {
    val firedatabase = FirebaseDatabase.getInstance()
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거


    // 인터페이스 정의: 숫자 입력 완료 이벤트를 액티비티로 전달하기 위함
    interface OnNumberEnteredListener {
        fun onNumberEntered(number:Double)
    }

    private var listener: OnNumberEnteredListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editweight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentEditweightBinding.bind(view)

        binding.edtNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnConfirm.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnConfirm.isEnabled = s.toString().toDoubleOrNull() != null
            }

            override fun afterTextChanged(s: Editable?) {
                // Enable the button only if edtNumber has valid input
                binding.btnConfirm.isEnabled = s.toString().toDoubleOrNull() != null
            }
        })
        // 확인 버튼 클릭시
        binding.cancelAddWeight.setOnClickListener{
            dismiss()
        }
        val dataRoute=firedatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/체중기입")
        binding.btnConfirm.setOnClickListener {
            // 입력한 숫자를 가져와서 액티비티로 전달
            val enteredNumber = binding.edtNumber.text.toString().toDoubleOrNull()
            val newweightmap= mapOf(
                "현재 사용자 체중" to enteredNumber
            )
            Log.i("체중", enteredNumber.toString())
            if (enteredNumber != null) {
                dataRoute.child(realTime).setValue(newweightmap)
                val userweight=TodayWeight(realTime,enteredNumber.toDouble())
                UserManager.setUserTodayWeight(userweight)
                listener?.onNumberEntered(enteredNumber)
                dismiss()
            } else {
                // 유효하지 않은 숫자라면 사용자에게 알림
                binding.edtNumber.error = "유효하지 않은 숫자입니다."
            }
        }
    }

    // 외부에서 리스너를 설정하는 함수
    fun setOnNumberEnteredListener(listener: OnNumberEnteredListener) {
        this.listener = listener
    }
}
