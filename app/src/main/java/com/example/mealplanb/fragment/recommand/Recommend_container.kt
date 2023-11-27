package com.example.mealplanb.fragment.recommand

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.R
import com.example.mealplanb.adapter.ChatAdapter
import com.example.mealplanb.databinding.FragmentRecommendContainerBinding
import com.example.mealplanb.dataclass.Message
import com.example.mealplanb.dataclass.MessageType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface OnItemClickListener {
    fun onItemClick(result: String)
}
class Recommend_container : BottomSheetDialogFragment(),OnItemClickListener{

    lateinit var binding:FragmentRecommendContainerBinding
    lateinit var adapter:ChatAdapter
    val messages = mutableListOf(
        Message("어떻게 식사를 추천해드릴까요??", MessageType.LEFT,null,null,null,null),
        // 다른 메시지 추가
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentRecommendContainerBinding.inflate(inflater,container,false)


        val currentDate = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Date())
        binding.date.text = currentDate

        val childFragment = recommend_food_or_amount()
        childFragment.setOnItemClickListener(this)// 얼마나, 추천 둘중에 고르는 화면으로 전환
        childFragmentManager.beginTransaction()
            .replace(R.id.container, childFragment)
            .commit()
        binding.cancle.setOnClickListener {
            dismiss()
        }
       initrecyclerview()
     // Fragment 간의 데이터 전달을 받음
        return binding.root
    }
    override fun onItemClick(result: String) {
        // 하위 프래그먼트의 클릭 결과를 받아와서 처리
        Log.d("BottomSheetFragment", "Received result: $result")
        if (result == "food") {
                // 음식 선택에 대한 로직 수행
                messages.add(Message("어떤 음식을 먹을까요?",MessageType.RIGHT,null,null,null,null))

            // 여기서 오늘 남은 칼로리와 영양성분을 가져와서 추가 해줘야 함


                messages.add(Message("오늘의 칼로리는 이만큼 남았어요",MessageType.LEFT,"500Kcal","50g","30g","20g"))
                initrecyclerview()
            } else if (result == "amount") {
                // 양 선택에 대한 로직 수행
            }
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        parentFragmentManager.setFragmentResultListener("Result", this) { key, bundle ->
//            Log.d("리스너", "Received result: $bundle")
//            val choice = bundle.getString("choice", "")
//            // 여기에서 선택한 값을 처리하거나 다른 로직 수행
//            if (choice == "food") {
//                // 음식 선택에 대한 로직 수행
//                messages.add(Message("어떤 음식을 먹을까요?",MessageType.RIGHT))
//                initrecyclerview()
//            } else if (choice == "amount") {
//                // 양 선택에 대한 로직 수행
//            }
//        }
//
//    }

    private fun initrecyclerview(){

        binding.chatbot.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
        adapter = ChatAdapter(messages)
        binding.chatbot.adapter = adapter
        adapter.notifyDataSetChanged()
    }
    // 식단 추천 로직 추가
    private fun recommendMeal() {
        // 여기에 식단 추천 로직을 추가하세요.
        // 예를 들어, 어떤 데이터를 가져와서 추천 식단을 결정하고,
        // 그 결과를 메시지로 RecyclerView에 추가하는 등의 작업을 수행합니다.

        // 추천 메시지를 생성
        val recommendationMessage = "식단 추천: 오늘은 샐러드와 닭가슴살이 좋을 것 같아요!"

        // 어댑터에 메시지 추가 및 갱신
        val newMessage = Message(recommendationMessage, MessageType.LEFT,null,null,null,null)
        adapter.addMessage(newMessage)
        adapter.notifyDataSetChanged()
    }

}