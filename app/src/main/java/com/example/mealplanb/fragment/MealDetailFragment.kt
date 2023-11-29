package com.example.mealplanb.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mealplanb.MealData
import com.example.mealplanb.R
import com.example.mealplanb.UserManager
import com.example.mealplanb.Userdata
import com.example.mealplanb.adapter.MealListAdapter
import com.example.mealplanb.databinding.FragmentMealDetailBinding
import com.google.firebase.database.FirebaseDatabase


class MealDetailFragment : Fragment() {
    lateinit var binding:FragmentMealDetailBinding
    lateinit var mealName:String
    lateinit var UsermealdataList:ArrayList<MealData>
    private val firebaseDatabase=FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mealName = arguments?.getString("mealName").toString()
        UsermealdataList = UserManager.getMealData()!!
        binding = FragmentMealDetailBinding.inflate(inflater,container,false)
        Log.i("확인2", UsermealdataList.toString())
        RoadMealData(UsermealdataList) //mealdetail 화면에 띄울때 사용자가 추가한 음식 정보들 다 더해서 화면에 띄우는 부분

        val mealsListView = binding.mealListView
        val adapter = MealListAdapter(requireContext(), UsermealdataList)
        mealsListView.adapter = adapter

        binding.completeMealAdd.setOnClickListener {
            saveMealDataToFirebase(UsermealdataList)
            UserManager.clearMealData() //식단1을 추가 완료하면 mealdatalist에 들어있는거 전부 초기화 해준다
            findNavController().navigate(R.id.action_mealDetailFragment_to_mainFragment)
            
        }
        binding.cancelDetailpage.setOnClickListener{
            findNavController().navigateUp()
        }

        return binding.root
    }
    private fun RoadMealData(mealData: List<MealData>){
        var wholecarboview=binding.carb
        val wholecarboviewpercent=binding.mealDetailCarbPercent
        var wholeproteinview=binding.protein
        val wholeproteinviewpercent=binding.mealDetailProteinPercent
        var wholefatview=binding.fat
        val wholefatviewpercent=binding.mealDetailFatPercent
        var wholekcalview=binding.detailmealKacl
        var wholecarbo=0.0
        var wholeprotein=0.0
        var wholefat=0.0
        var wholekcal=0.0

        for(mealData in UsermealdataList){
            wholecarbo=wholecarbo+mealData.foodcarbo
            wholeprotein=wholeprotein+mealData.foodprotein
            wholefat=wholefat+mealData.foodfat
            wholekcal=wholekcal+mealData.foodcal
        }
        wholecarboview.text=String.format("%.1f",wholecarbo)
        wholeproteinview.text=String.format("%.1f",wholeprotein)
        wholefatview.text=String.format("%.1f",wholefat)
        wholekcalview.text=String.format("%.1f",wholekcal)
        wholecarboviewpercent.text=String.format("%.1f",wholecarbo*4/wholekcal*100)
        wholeproteinviewpercent.text=String.format("%.1f",wholeprotein*4/wholekcal*100)
        wholefatviewpercent.text=String.format("%.1f",wholefat*9/wholekcal*100)

    }
    private fun saveMealDataToFirebase(mealDataList: List<MealData>) {
        val dataRoute = firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단기입/${UsermealdataList[0].date}/${UsermealdataList[0].mealname}")
        for(mealData in UsermealdataList){
            val foodkey=dataRoute.child("${mealData.foodname}").push().key
            val newData=mapOf(
                "식품이름" to "${mealData.foodname}",
                "열량" to "${mealData.foodcal}",
                "섭취량" to "${mealData.foodamount}",
                "가공업체" to "${mealData.foodbrand}",
                "탄수화물" to "${mealData.foodcarbo}",
                "단백질" to "${mealData.foodprotein}",
                "지방" to "${mealData.foodfat}"
            )
            if(foodkey!=null){
                dataRoute.child(foodkey).updateChildren(newData)

            }


        }


    }

}