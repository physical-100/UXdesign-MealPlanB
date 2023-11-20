package com.example.mealplanb.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentEditweightBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class editweightFragment : BottomSheetDialogFragment() {

    interface OnNumberEnteredListener {
        fun onNumberEntered(number: Int)
    }
    // 인터페이스 정의: 숫자 입력 완료 이벤트를 액티비티로 전달하기 위함

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
                binding.btnConfirm.isEnabled = s.toString().toIntOrNull() != null
            }

            override fun afterTextChanged(s: Editable?) {
                // Enable the button only if edtNumber has valid input
                binding.btnConfirm.isEnabled = s.toString().toIntOrNull() != null
            }
        })
        // 확인 버튼 클릭시
        binding.cancelAddWeight.setOnClickListener{
            dismiss()
        }
        binding.btnConfirm.setOnClickListener {
            // 입력한 숫자를 가져와서 액티비티로 전달
            val enteredNumber = binding.edtNumber.text.toString().toIntOrNull()
            if (enteredNumber != null) {
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
