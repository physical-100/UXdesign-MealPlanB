package com.example.mealplanb.initset

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.R
import com.example.mealplanb.UserManager
import com.example.mealplanb.User_calory
import com.example.mealplanb.databinding.FragmentDetailNutritionBinding
import com.google.firebase.database.FirebaseDatabase

class DetailNutritionFragment : Fragment() {
    lateinit var binding: FragmentDetailNutritionBinding
    lateinit var name:String
    private val firebaseDatabase = FirebaseDatabase.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val userData = UserManager.getUserData()
        Log.i("goal3", "${userData}")
        if (userData != null) {
           name= userData.username
            // 예: userData.name, userData.gender, userData.age, 등등...
        }

        binding = FragmentDetailNutritionBinding.inflate(inflater, container, false)

        val calory = arguments?.getString("goal_cal")
        Log.i("calory",calory.toString())// 인자 받아오기


        // 초기 4:4:2 로 설정 되어 있음 식단에 따라 비율 변경 가능
        //4:4:2 에 맞게 설정
        binding.goalCalory.setText("$calory"+"Kcal")
        if(calory!=null) {
            var goal_calory = calory.toFloat()
            var init_carb_cal = (goal_calory * 0.4).toInt()
            var init_carb = (init_carb_cal / 4)
            var init_protein_cal = (goal_calory * 0.4).toInt()
            var init_protein = (init_protein_cal / 4)
            var init_fat_cal = (goal_calory * 0.2).toInt()
            var init_fat = (init_fat_cal / 9)


            binding.apply {
                // kcal 과 g 디자인 추가
                carbcalory.text = ("$init_carb_cal" +"Kcal")
                proteincalory.text = init_protein_cal.toString()+"Kcal"
                fatcalory.text = init_fat_cal.toString()+"Kcal"
                carbSetting.setText("$init_carb")
                proteinSetting.setText("$init_protein")
                fatSetting.setText("$init_fat")


                carbSetting.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        // 이 메서드는 텍스트 변경 전에 호출됩니다.
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // 이 메서드는 텍스트가 변경될 때 호출됩니다.
                    }
                    override fun afterTextChanged(s: Editable?) {
                        // 이 메서드는 텍스트 변경 후 호출됩니다.
                        // carbSetting EditText의 값이 변경될 때 다른 값들을 업데이트
                        val carbValue = s.toString()
                        if (carbValue.isNotEmpty()) {
                            // carbValue를 정수로 파싱
                            init_carb = carbValue.toInt()
                            init_carb_cal = init_carb *4
                            goal_calory = (init_carb_cal+init_fat_cal+init_protein_cal).toFloat()

                            // 업데이트된 값을 TextView에 설정
                            carbcalory.text = "$init_carb_cal Kcal"
                            goalCalory.text = "${goal_calory.toInt()} Kcal"
                            carbpercent.text = "${(init_carb_cal/goal_calory*100).toInt()} %"
                            proteinpercent.text = "${(init_protein_cal/goal_calory*100).toInt()} %"
                            fatpercent.text = "${(init_fat_cal/goal_calory*100).toInt()} %"
                        }else{
                            init_carb = 0
                            init_carb_cal = 0
                            goal_calory = (init_carb_cal+init_fat_cal+init_protein_cal).toFloat()
                            // 업데이트된 값을 TextView에 설정
                            carbcalory.text = "$init_carb_cal Kcal"
                            goalCalory.text = "${goal_calory.toInt()} Kcal"
                            carbpercent.text = "0 %"
                            proteinpercent.text = "${(init_protein_cal/goal_calory*100).toInt()} %"
                            fatpercent.text = "${(init_fat_cal/goal_calory*100).toInt()} %"
                        }
                    }
                })

                proteinSetting.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        // 이 메서드는 텍스트 변경 전에 호출됩니다.
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // 이 메서드는 텍스트가 변경될 때 호출됩니다.
                    }
                    override fun afterTextChanged(s: Editable?) {
                        // 이 메서드는 텍스트 변경 후 호출됩니다.
                        // proteinSetting EditText의 값이 변경될 때 다른 값들을 업데이트
                        val proteinValue = s.toString()
                        if (proteinValue.isNotEmpty()) {
                            // carbValue를 정수로 파싱
                            init_protein = proteinValue.toInt()
                            init_protein_cal = init_protein *4
                            goal_calory = (init_carb_cal+init_fat_cal+init_protein_cal).toFloat()

                            // 업데이트된 값을 TextView에 설정
                            proteincalory.text = "$init_protein_cal Kcal"
                            goalCalory.text = "${goal_calory.toInt()} Kcal"
                            carbpercent.text = "${(init_carb_cal/goal_calory*100).toInt()} %"
                            proteinpercent.text = "${(init_protein_cal/goal_calory*100).toInt()} %"
                            fatpercent.text = "${(init_fat_cal/goal_calory*100).toInt()} %"
                        }else{
                            init_protein = 0
                            init_protein_cal = 0
                            goal_calory = (init_carb_cal+init_fat_cal+init_protein_cal).toFloat()
                            // 업데이트된 값을 TextView에 설정
                            proteincalory.text = "$init_protein Kcal"
                            goalCalory.text = "${goal_calory.toInt()} Kcal"
                            carbpercent.text = "${(init_carb_cal/goal_calory*100).toInt()} %"
                            proteinpercent.text = "0 %"
                            fatpercent.text = "${(init_fat_cal/goal_calory*100).toInt()} %"
                        }
                    }
                })

                fatSetting.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        // 이 메서드는 텍스트 변경 전에 호출됩니다.
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // 이 메서드는 텍스트가 변경될 때 호출됩니다.
                    }
                    override fun afterTextChanged(s: Editable?) {
                        // 이 메서드는 텍스트 변경 후 호출됩니다.
                        // fatSetting EditText의 값이 변경될 때 다른 값들을 업데이트
                        val fatValue = s.toString()
                        if (fatValue.isNotEmpty()) {
                            // carbValue를 정수로 파싱
                            init_fat = fatValue.toInt()
                            init_fat_cal = init_fat *9
                            goal_calory = (init_carb_cal+init_fat_cal+init_protein_cal).toFloat()

                            // 업데이트된 값을 TextView에 설정
                            fatcalory.text = "$init_fat_cal Kcal"
                            goalCalory.text = "${goal_calory.toInt()} Kcal"
                            carbpercent.text = "${(init_carb_cal/goal_calory*100).toInt()} %"
                            proteinpercent.text = "${(init_protein_cal/goal_calory*100).toInt()} %"
                            fatpercent.text = "${(init_fat_cal/goal_calory*100).toInt()} %"
                        }else{
                            init_fat= 0
                            init_fat_cal = 0
                            goal_calory = (init_carb_cal+init_fat_cal+init_protein_cal).toFloat()
                            // 업데이트된 값을 TextView에 설정
                            fatcalory.text = "$init_fat_cal Kcal"
                            goalCalory.text = "${goal_calory.toInt()} Kcal"
                            carbpercent.text = "${(init_carb_cal/goal_calory*100).toInt()} %"
                            proteinpercent.text = "${(init_protein_cal/goal_calory*100).toInt()} %"
                            fatpercent.text = "0 %"
                        }
                    }
                })

                next2.setOnClickListener {
                    val dataRoute=firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id")
                    val carb_percent=(init_carb_cal/goal_calory*100).toInt()
                    val protein_percent=(init_protein_cal/goal_calory*100).toInt()
                    val fat_percent=(init_fat_cal/goal_calory*100).toInt()
                    val userCalory = User_calory(name,goal_calory.toInt(), init_carb, init_protein, init_fat,carb_percent,protein_percent,fat_percent)
                    dataRoute.child("목표 탄수화물").setValue(init_carb)
                    dataRoute.child("목표 탄수화물(%)").setValue(carb_percent)
                    dataRoute.child("목표 단백질").setValue(init_protein)
                    dataRoute.child("목표 단백질(%)").setValue(protein_percent)
                    dataRoute.child("목표 지방").setValue(init_fat)
                    dataRoute.child("목표 지방(%)").setValue(fat_percent)
                    dataRoute.child("초기설정여부").setValue("완료")



                    UserManager.setUserCal(userCalory)
                    val bundle = bundleOf(
                        "user_calory" to userCalory
                    )


                    findNavController().navigate(R.id.action_detailNutritionFragment_to_mainFragment,bundle)
                    //목표 칼로리를 인자로 넘김

                }
            }

            // EditText에 TextWatcher 추가


        }

        return  binding.root
    }


}