package com.example.mealplanb.initset

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.Userdata
import com.example.mealplanb.databinding.FragmentProfileFragmentBinding

class Profile_fragment : Fragment() {
    lateinit var binding: FragmentProfileFragmentBinding
    private var Userdata: Userdata? = null
    lateinit var sex: String
    lateinit var activity: String
    private var isSexSelected = false
    private var isActivitySelected = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        setbackground()

        binding.nextButton.isEnabled=false
        // 성별, 활동량이 선택되어 잇는지 확인


        binding.imagefemale.setOnClickListener {
            sex="여성"
            isSexSelected= true
            binding.femaleLayout.setBackgroundResource(R.drawable.image_border)
            binding.maleLayout.setBackgroundResource(0)
        }
        binding.imagemale.setOnClickListener {
            sex="남성"
            isSexSelected=true
            binding.maleLayout.setBackgroundResource(R.drawable.image_border)
            binding.femaleLayout.setBackgroundResource(0)
        }
        binding.imageView.setOnClickListener {
            activity= "활동 많음"
            isActivitySelected= true
            binding.Layout1.setBackgroundResource(R.drawable.image_border)
            binding.Layout2.setBackgroundResource(0)
            binding.Layout3.setBackgroundResource(0)
        }
        binding.imageView2.setOnClickListener {
            activity= "활동 많음"
            isActivitySelected= true
            binding.Layout1.setBackgroundResource(0)
            binding.Layout2.setBackgroundResource(R.drawable.image_border)
            binding.Layout3.setBackgroundResource(0)
        }
        binding.imageView3.setOnClickListener {
            activity= "활동 많음"
            isActivitySelected= true
            binding.Layout1.setBackgroundResource(0)
            binding.Layout2.setBackgroundResource(0)
            binding.Layout3.setBackgroundResource(R.drawable.image_border)
        }

        val editTextList = listOf(
            binding.username, binding.age, binding.height,
            binding.startWeight, binding.goalWeight
        )
        editTextList.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // 모든 EditText의 텍스트가 비어있지 않으면 버튼 활성화
                    val allFieldsNotEmpty = editTextList.all { it.text.isNotEmpty() }
                    binding.nextButton.isEnabled = allFieldsNotEmpty&&isSexSelected&&isActivitySelected
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
        }
        // 버튼 클릭 이벤트 처리
        binding.nextButton.setOnClickListener {
            // 입력 값 가져오기
            val name = binding.username.text.toString()
            val gender = sex
            val age = binding.age.text.toString().toInt()
            val height = binding.height.text.toString().toInt()
            val start_weight = binding.startWeight.text.toString().toDouble()
            val goal_weight = binding.goalWeight.text.toString().toDouble()
            val activityLevel = activity

            // 데이터 클래스 업데이트
            Userdata = Userdata(name, gender, age, height, start_weight, goal_weight, activityLevel)
            // 이거를 db에 저장하고 싶음


            val bundle = Bundle()
            bundle.putParcelable("userdata", Userdata!!)

            // 다음 프래그먼트로 이동
            findNavController().navigate(R.id.action_profile_fragment_to_meal_selectFragment,bundle)
            Log.i("UserData", Userdata.toString())
        }

        return view
    }
    private  fun setbackground(){
        binding.femaleLayout.setBackgroundResource(0)
        binding.maleLayout.setBackgroundResource(0)
        binding.Layout1.setBackgroundResource(0)
        binding.Layout2.setBackgroundResource(0)
        binding.Layout3.setBackgroundResource(0)
    }
}