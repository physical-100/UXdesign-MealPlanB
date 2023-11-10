package com.example.mealplanb.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.databinding.FragmentMypagefragmentBinding
import com.example.mealplanb.databinding.FragmentProfileModifyingBinding

class Profile_modifyingFragment : Fragment() {
    lateinit var binding:FragmentProfileModifyingBinding
    // 사진 수정하는거 알고리즘 구현

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileModifyingBinding.inflate(inflater, container, false)


        binding.nicknameChange.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 모든 EditText의 텍스트가 비어있지 않으면 버튼 활성화
                val NotEmpty = binding.nicknameChange.text.isNotEmpty()
                binding.complete.isEnabled = NotEmpty
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 필요 없음
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    // 모든 EditText의 텍스트가 비어있지 않으면 버튼 활성화
//                    val allFieldsNotEmpty = editTextList.all { it.text.isNotEmpty() }
//                    binding.nextButton.isEnabled = allFieldsNotEmpty&&isSexSelected&&isActivitySelected
            }
        })
        binding.backToMypage.setOnClickListener {
            findNavController().navigateUp()

        }
        binding.complete.setOnClickListener {

        }
        return binding.root
    }


}