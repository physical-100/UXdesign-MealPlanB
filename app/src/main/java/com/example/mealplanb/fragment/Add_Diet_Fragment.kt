package com.example.mealplanb.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.R
import com.example.mealplanb.adapter.AddFoodAdapter
import com.example.mealplanb.databinding.AddfoodrowBinding
import com.example.mealplanb.databinding.FragmentAddDietBinding
import com.example.mealplanb.dataclass.food

class Add_Diet_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    var binding: FragmentAddDietBinding?=null
    var adapter: AddFoodAdapter?=null
    var data = arrayListOf<food>(
        food("닭가슴살", "하림", 120.0,100.0 ),
        food("현미밥", "", 153.0,100.0 ),
        food("야채샐러드", "", 16.0,50.0 )
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddDietBinding.inflate(layoutInflater,container,false)


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding!!.recyclerview.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        adapter = AddFoodAdapter(data)
        adapter!!.itemClickListener = object : AddFoodAdapter.OnItemClickListener{
            override fun OnItemClick(data: food, holder: AddFoodAdapter.ViewHolder) {

            }
        }
        binding!!.recyclerview.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}