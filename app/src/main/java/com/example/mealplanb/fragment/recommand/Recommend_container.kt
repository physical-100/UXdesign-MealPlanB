package com.example.mealplanb.fragment.recommand

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.R
import com.example.mealplanb.adapter.ChatAdapter
import com.example.mealplanb.databinding.FragmentRecommendContainerBinding
import com.example.mealplanb.dataclass.Message
import com.example.mealplanb.dataclass.MessageType
import com.example.mealplanb.dataclass.food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface OnItemClickListener {
    fun onItemClick(result: String)
    fun onDataClick(food: food)
}
class Recommend_container : Fragment(),OnItemClickListener{

    lateinit var binding:FragmentRecommendContainerBinding
    lateinit var adapter:ChatAdapter

    val messages = mutableListOf(
        Message("어떻게 식사를 추천해드릴까요??", MessageType.LEFT,null,null,null,null,null)
        // 다른 메시지 추가
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val childFragment = recommend_food_or_amount()
        childFragment.setOnItemClickListener(this)
        val Fragment = Amountrecommend()
          Fragment.setOnItemClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃을 인플레이트합니다.
        binding = FragmentRecommendContainerBinding.inflate(inflater, container, false)

        val currentDate = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Date())
        binding.date.text = currentDate

        val childFragment = recommend_food_or_amount()
        childFragment.setOnItemClickListener(this)
        childFragmentManager.beginTransaction()
            .replace(R.id.container, childFragment)
            .commit()
        binding.cancle.setOnClickListener {
            findNavController().navigate(R.id.action_recommend_container2_to_mainFragment)
        }
        initrecyclerview()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Fragment = Amountrecommend()
        Fragment.setOnItemClickListener(this)
    }
    override fun onItemClick(result: String) {
        // 하위 프래그먼트의 클릭 결과를 받아와서 처리
        Log.d("BottomSheetFragment", "Received result: $result")
        if (result == "food") {
                // 음식 선택에 대한 로직 수행
                messages.add(Message("어떤 음식을 먹을까요?",MessageType.RIGHT,null,null,null,null,null))

            // 여기서 오늘 남은 칼로리와 영양성분을 가져와서 추가 해줘야 함


                messages.add(Message("오늘의 칼로리는 이만큼 남았어요",MessageType.LEFT,"500Kcal","50g","30g","20g",null))
                initrecyclerview()
            //child를 바꾸고 리스너 위치를 바꿈
            val childFragment = RecommendFragment()
            childFragment.setOnItemClickListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.container, childFragment)
                .commit()

            } else if (result == "amount") {
                // 양 선택에 대한 로직 수행

            messages.add(Message("얼마나 먹을까요?",MessageType.RIGHT,null,null,null,null,null))

            messages.add(Message("어떤 음식을 먹고 싶으세요?",MessageType.LEFT,null,null,null,null,null))
            initrecyclerview()

            //child를 바꾸고 리스너 위치를 바꿈
            val childFragment = Amountrecommend()
            childFragment.setOnItemClickListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.container, childFragment)
                .commit()
            }
        else if (result == "check") {
            // 양 선택에 대한 로직 수행

            messages.add(Message("식단을 기록할게요!!",MessageType.RIGHT,null,null,null,null,null))
            initrecyclerview()
            // 추천 목록 기록 해야햐나?

            //식단 기록후 화면 전환
            GlobalScope.launch(Dispatchers.Main) {
                    delay(1000)  // 1000 밀리초 (1초) 지연
                findNavController().navigate(R.id.action_recommend_container2_to_mainFragment)
            }
        }else if (result == "식단 확정") {
            // 양 선택에 대한 로직 수행

            messages.add(Message("이 음식으로 먹을게요",MessageType.RIGHT,null,null,null,null,null))
            // 위에서 저장한 음식 값을 가져옴
            messages.add(Message(null,MessageType.LEFT,"음식 이름",null,null,null,"자동으로 식단을 입력해드릴게요!"))
            initrecyclerview()

            //식단 기록후 화면 전환
            findNavController().navigate(R.id.action_recommend_container2_to_mainFragment)
        }
        else if (result == "othermeal") {
            // 양 선택에 대한 로직 수행

            messages.add(Message("다시 추천해주세요!!",MessageType.RIGHT,null,null,null,null,null))
            // 위에서 저장한 음식 값을 가져옴
            messages.add(Message("다시 식단을 추천해드리겠습니다.",MessageType.LEFT,null,null,null,null,null))
            initrecyclerview()

            //식단 기록후 화면 전환
            val childFragment = MealcheckFragment()
            childFragment.setOnItemClickListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.container, childFragment)
                .commit()

        }


        else if (result == "backtosearch") {
            // 양 선택에 대한 로직 수행
            messages.add(Message("다시 검색할게요!!",MessageType.RIGHT,null,null,null,null,null))

            initrecyclerview()

            //child를 바꾸고 리스너 위치를 바꿈
            val childFragment = Amountrecommend()
            childFragment.setOnItemClickListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.container, childFragment)
                .commit()
        }
        else if (result == "backtofoodoramount") {
            // 양 선택에 대한 로직 수행
            messages.clear()
            messages.add((Message("어떻게 식사를 추천해드릴까요??", MessageType.LEFT,null,null,null,null,null)))
            initrecyclerview()

            //child를 바꾸고 리스너 위치를 바꿈
            val childFragment = recommend_food_or_amount()
            childFragment.setOnItemClickListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.container, childFragment)
                .commit()
        }
        else if (result == "cheat") {
            // 양 선택에 대한 로직 수행
            messages.add(Message("치팅 식단으로 추천해줘!!",MessageType.RIGHT,null,null,null,null,null))
            // 치팅 밀 표시 함수 구현
            // 남은 칼로리랑  영양성분 가져와서 랜덤 음식 추천
            // 변수에 저장
            messages.add(Message("맛있는 치팅 식단!!",MessageType.LEFT,"음식 이름","50g","30g","20g","이 음식은 어떠세요?"))

            // 치팅 밀 표시 함수 구현
            // 남은 칼로리랑  영양성분 가져와서 랜덤 음식 추천

            initrecyclerview()
            //child를 바꾸고 리스너 위치를 바꿈
            val childFragment = MealcheckFragment()
            childFragment.setOnItemClickListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.container, childFragment)
                .commit()

        }
        else if (result == "like") {
            // 양 선택에 대한 로직 수행
            messages.add(Message("즐겨 먹는 음식으로 추천해줘",MessageType.RIGHT,null,null,null,null,null))
            // 즐겨찾기 음식 가져오기 구현
            // 즐겨 먹는 목록을 가져와서 구현
            initrecyclerview()
            //child를 바꾸고 리스너 위치를 바꿈
            val childFragment = MealcheckFragment()
            childFragment.setOnItemClickListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.container, childFragment)
                .commit()

        }
        else if (result == "popular") {
            // 양 선택에 대한 로직 수행
            messages.add(Message("인기 있는 식단으로 추천 해줘",MessageType.RIGHT,null,null,null,null,null))

            initrecyclerview()

            //child를 바꾸고 리스너 위치를 바꿈
            val childFragment = MealcheckFragment()
            childFragment.setOnItemClickListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.container, childFragment)
                .commit()

        }
    }

    override fun onDataClick(food: food) {

            // Handle the item click, you can use the food data to display the message
            Log.i("receivefood", food.toString())
            messages.add(
                Message(
                    " ${food.foodname}을 먹을래",
                    MessageType.RIGHT,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            )
            //여기 왼쪽 추가를 수정하몀 됨
            //kcal에 오늘 남은 칼로리, carb에 food 칼로리 성분 비교에서 뭘 넣을지는 차차 알고리즘 구애
            messages.add(
                Message(
                    " 200g 드세요",
                    MessageType.LEFT_2,
                    "300Kcal",
                    food.foodname,
                    food.foodcal.toString(),
                    null,
                    null
                )
            )
            initrecyclerview()
        val childFragment = checkFragment()
        childFragment.setOnItemClickListener(this)
        childFragmentManager.beginTransaction()
            .replace(R.id.container, childFragment)
            .commit()
    }

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
        val newMessage = Message(recommendationMessage, MessageType.LEFT,null,null,null,null,null)
        adapter.addMessage(newMessage)
        adapter.notifyDataSetChanged()
    }

}