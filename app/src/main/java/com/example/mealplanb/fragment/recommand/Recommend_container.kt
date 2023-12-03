package com.example.mealplanb.fragment.recommand

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.AllListNutrition
import com.example.mealplanb.MealData
import com.example.mealplanb.R
import com.example.mealplanb.Totalcal
import com.example.mealplanb.UserManager
import com.example.mealplanb.adapter.ChatAdapter
import com.example.mealplanb.databinding.FragmentRecommendContainerBinding
import com.example.mealplanb.dataclass.Message
import com.example.mealplanb.dataclass.MessageType
import com.example.mealplanb.dataclass.food
import com.example.mealplanb.fragment.Add_Diet_Fragment
import com.google.android.play.integrity.internal.c
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jayway.jsonpath.JsonPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

interface OnItemClickListener {
    fun onItemClick(result: String)
    fun onDataClick(food: food)
}
class Recommend_container : Fragment(),OnItemClickListener{
    lateinit var binding:FragmentRecommendContainerBinding
    lateinit var adapter:ChatAdapter
    lateinit var recommendmode:String
    val userCal = UserManager.getUserCal()
    val totalCal = UserManager.getTotalcal()
    lateinit var remaindata:Totalcal
    private val firebaseDatabase= FirebaseDatabase.getInstance()
    var recommendmeal:food?=null
    var AllListNutritionList= ArrayList<AllListNutrition>(
    )
    var UserdataList=ArrayList<MealData>()
    var favoritefood = arrayListOf<food>(
    )

    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거
    val cheatMealsampledata =arrayListOf<food> (
            food("고추 바사삭","굽네 치킨",1302.0,600.0,76.0,126.7,54.5),
            food("와퍼","버거킹",619.0,278.0,10.5,29.0,13.0),
            food("옥수수새우 피자","노모어 피자",225.0,100.0,25.5,11.9,8.4),
             food("육회 비빔밥",null,442.0,300.0,68.8,19.2,10.1)
    )


    val messages = mutableListOf(
        Message("어떻게 식사를 추천해드릴까요??", MessageType.LEFT,null,null,null,null,null)
        // 다른 메시지 추가
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodFavoritesRoad()
        val childFragment = recommend_food_or_amount()
        childFragment.setOnItemClickListener(this)
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
            getremaincal()
            var available = true
            GlobalScope.launch(Dispatchers.Main) {
                delay(500)  // 1000 밀리초 (1초) 지연
                messages.add(
                    Message(
                        "어떤 음식을 먹을까요?",
                        MessageType.RIGHT,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )

                // 여기서 오늘 남은 칼로리와 영양성분을 가져와서 추가 해줘야 함


                messages.add(
                    Message(
                        "오늘의 칼로리는 이만큼 남았어요",
                        MessageType.LEFT,
                        remaindata.total_calory.toInt().toString()+"kcal",
                        remaindata.total_carb.toInt().toString()+"g",
                        remaindata.total_protein.toInt().toString()+"g",
                        remaindata.total_fat.toInt().toString()+"g",
                        null
                    )
                )

                if(remaindata.total_carb.toInt()==0||remaindata.total_protein.toInt()==0||remaindata.total_fat.toInt()==0){
                    messages.add(
                        Message(
                            "치팅 식단이나 인기 있는 식단은 추천해줄 수 없어요!!",
                            MessageType.LEFT,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    )
                    available=false
                }

                initrecyclerview()
             }
                // 치팅 식단이나 인기 있는이 가능한지 번들을 넘긴다
            // 일단 왜 안넘어가는지 모르겠음..
            val bundle = bundleOf("available_cheat" to available )
        //child를 바꾸고 리스너 위치를 바꿈
            val childFragment = RecommendFragment()
            childFragment.arguments = bundle  // Attach the bundle to the fragment
            childFragment.setOnItemClickListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.container, childFragment)
                .commit()

            } else if (result == "amount") {
                // 양 선택에 대한 로직 수행
                GlobalScope.launch(Dispatchers.Main) {
                    delay(500)
                    messages.add(
                        Message(
                            "얼마나 먹을까요?",
                            MessageType.RIGHT,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    )

                    messages.add(
                        Message(
                            "어떤 음식을 먹고 싶으세요?",
                            MessageType.LEFT,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                    )
                    initrecyclerview()
                }

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
                savemeal()
                findNavController().navigate(R.id.action_recommend_container2_to_mainFragment)
            }
        }else if (result == "식단 확정") {
            // 양 선택에 대한 로직 수행

            messages.add(Message("이 음식으로 먹을게요",MessageType.RIGHT,null,null,null,null,null))
            // 위에서 저장한 음식 값을 가져옴

            messages.add(Message(null,MessageType.LEFT,recommendmeal!!.foodname,null,null,null,"자동으로 식단을 입력해드릴게요!"))

            initrecyclerview()

            //식단 기록후 화면 전환
            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)  // 1000 밀리초 (1초) 지연
                savemeal()
                findNavController().navigate(R.id.action_recommend_container2_to_mainFragment)
            }
        }
        else if (result == "다른 음식 추천") {
            // 양 선택에 대한 로직 수행
            GlobalScope.launch(Dispatchers.Main) {
                delay(500)
                messages.add(
                    Message(
                        "다시 추천해주세요!!",
                        MessageType.RIGHT,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )
                // 위에서 저장한 음식 값을 가져옴
                messages.add(
                    Message(
                        "다시 ${recommendmode}식단을 추천해드리겠습니다.",
                        MessageType.LEFT,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )
                initrecyclerview()
            }
            GlobalScope.launch(Dispatchers.Main) {
                delay(500)

                //모드에 따라  새로 음식을 추천 해줌
                when (recommendmode) {
                    "치팅"-> {
                                // 위에서 저장한 음식 값을 가져옴
                                recommenCheatMeal()
                                initrecyclerview()
                            }
                    "인기 있는"->{

                    }
                    "즐겨 먹는"->{
                            recommendlikeMeal()
                            initrecyclerview()
                    }
                }
            }
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
            recommendmode="치팅"
            messages.add(Message("치팅 식단으로 추천해줘!!",MessageType.RIGHT,null,null,null,null,null))
            // 치팅 밀 표시 함수 구현

            // 변수에 저장
            //messages.add(Message("맛있는 치팅 식단!!",MessageType.LEFT,"음식 이름","50g","30g","20g","이 음식은 어떠세요?"))
            recommenCheatMeal()
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
            recommendmode = "즐겨 먹는"
            messages.add(Message("즐겨 먹는 음식으로 추천해줘",MessageType.RIGHT,null,null,null,null,null))
            // 즐겨찾기 음식 가져오기 구현
            // 즐겨 먹는 목록을 가져와서 구현
            recommendlikeMeal()
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
            recommendmode="인기 있는"

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
            getremaincal()
            calculateamountfood(food,remaindata)
            initrecyclerview()
        val childFragment = checkFragment()
        childFragment.setOnItemClickListener(this)
        childFragmentManager.beginTransaction()
            .replace(R.id.container, childFragment)
            .commit()
    }
    private fun getremaincal(){
        val remaincal = userCal!!.goal_calory - totalCal!!.total_calory
        val remaincarb = userCal!!.carb - totalCal!!.total_carb
        val remainprotein = userCal!!.protein - totalCal!!.total_protein
        val remainfat = userCal!!.fat - totalCal!!.total_fat

// 각 변수가 0보다 작으면 0으로 설정
        val remaincalNonNegative = if (remaincal < 0) 0 else remaincal
        val remaincarbNonNegative = if (remaincarb < 0) 0.0 else remaincarb
        val remainproteinNonNegative = if (remainprotein < 0) 0.0 else remainprotein
        val remainfatNonNegative = if (remainfat < 0) 0.0 else remainfat

        remaindata=Totalcal(remaincalNonNegative.toDouble(),remaincarbNonNegative,remainproteinNonNegative,remainfatNonNegative)
    }

    private fun initrecyclerview(){

        binding.chatbot.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
        adapter = ChatAdapter(messages)
        binding.chatbot.adapter = adapter
        adapter.notifyDataSetChanged()

        binding.chatbot.scrollToPosition(adapter.itemCount - 1)
    }
    // 식단 추천 로직 추가
    private fun recommendlikeMeal() {
        // 즐겨 찾는 음식으로 추가
        recommendmeal=null
        getremaincal()
        val difflist = mutableListOf<Double>()
        for(i in favoritefood){
            val aviailablequantity = remaindata.total_calory/i.foodcal
            val foodcarb= i.foodcarbo*aviailablequantity
            val foodprotein= i.foodprotein*aviailablequantity
            val foodfat= i.foodfat*aviailablequantity
            val diff = Math.abs(
                        (remaindata.total_carb - foodcarb) +
                        (remaindata.total_protein - foodprotein) +
                        (remaindata.total_fat - foodfat)
            )
            difflist.add(diff)
        }
        val indexOfMinDiff = difflist.indexOf(difflist.minOrNull())
        val likefood = favoritefood[indexOfMinDiff]
        val maxNForkcal = calculateMaxN(likefood.foodcal, remaindata.total_calory)
        val maxNForCarb = calculateMaxN(likefood.foodcarbo, remaindata.total_carb)
        val maxNForProtein = calculateMaxN(likefood.foodprotein, remaindata.total_protein)
        val maxNForFat = calculateMaxN(likefood.foodfat, remaindata.total_fat)

        val maxN = minOf(maxNForkcal,maxNForCarb, maxNForProtein, maxNForFat)
        if (maxN > 0) {
            // Add the message with the calculated nutritional values
            messages.add(
                Message(
                    "즐겨찾는 식단!!",
                    MessageType.LEFT,
                    likefood.foodname,
                    likefood.foodcarbo.toInt().toString() + "g",
                    likefood.foodprotein.toInt().toString() + "g",
                    likefood.foodfat.toInt().toString() + "g",
                    "이 음식으로 남은 영양성분을 채울 수 있어요"
                )
            )

            // Update the remaining nutritional values based on the selected cheat meal
            val recommendcal = likefood.foodcal*maxN
            val recommendcarb = likefood.foodcarbo * maxN
            val recommendprotein = likefood.foodprotein * maxN
            val recommendfat = likefood.foodfat * maxN
            val availableamount = maxN*likefood.foodamount
            recommendmeal=food(likefood.foodname,likefood.foodbrand,recommendcal,availableamount,recommendcarb,recommendprotein,recommendfat)

            messages.add(
                Message(
                    likefood.foodname+"을 "+"${availableamount.toInt()}g만큼 드실 수 있어요!",
                    MessageType.LEFT,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            )
        } else {
            // 영양성분으로 비교했을때 너무 작으면  남은 영양소로 해당 음식 못먹음을 알려줌
            // 일정량을 해서 구한다
            messages.add(Message("남은 영양소로는 치팅 음식을 먹을 수 없어요!", MessageType.LEFT, null, null, null, null, null))
            messages.add(Message("남은 영양소에 맞는 음식을 섭취해서 목표를 달성해 보아요", MessageType.LEFT, null, null, null, null, null))
        }

    }

    private fun recommenCheatMeal() {
        recommendmeal=null
        val randomIndex = Random.nextInt(cheatMealsampledata.size)
        val cheatmeal = cheatMealsampledata[randomIndex]

        val maxNForkcal = calculateMaxN(cheatmeal.foodcal, remaindata.total_calory)
        val maxNForCarb = calculateMaxN(cheatmeal.foodcarbo, remaindata.total_carb)
        val maxNForProtein = calculateMaxN(cheatmeal.foodprotein, remaindata.total_protein)
        val maxNForFat = calculateMaxN(cheatmeal.foodfat, remaindata.total_fat)

        val maxN = minOf(maxNForkcal,maxNForCarb, maxNForProtein, maxNForFat)

        if (maxN > 0) {
            // Add the message with the calculated nutritional values
            messages.add(
                Message(
                    "맛있는 치팅 식단!!",
                    MessageType.LEFT,
                    cheatmeal.foodname,
                    cheatmeal.foodcarbo.toInt().toString() + "g",
                    cheatmeal.foodprotein.toInt().toString() + "g",
                    cheatmeal.foodfat.toInt().toString() + "g",
                    "이 음식은 어떠세요?"
                )
            )

            // Update the remaining nutritional values based on the selected cheat meal
            val recommendcal = cheatmeal.foodcal*maxN
            val recommendcarb = cheatmeal.foodcarbo * maxN
            val recommendprotein = cheatmeal.foodprotein * maxN
            val recommendfat = cheatmeal.foodfat * maxN
            val availableamount = maxN*cheatmeal.foodamount
            recommendmeal=food(cheatmeal.foodname,cheatmeal.foodbrand,recommendcal,availableamount,recommendcarb,recommendprotein,recommendfat)

            messages.add(
                Message(
                    cheatmeal.foodname+"을 "+"${availableamount.toInt()}g만큼 드실 수 있어요!",
                    MessageType.LEFT,
                    null,
                    null,
                    null,
                    null,
                    null
                )
            )
        } else {
            // 영양성분으로 비교했을때 너무 작으면  남은 영양소로 해당 음식 못먹음을 알려줌
            // 일정량을 해서 구한다
            messages.add(Message("남은 영양소로는 치팅 음식을 먹을 수 없어요!", MessageType.LEFT, null, null, null, null, null))
            messages.add(Message("남은 영양소에 맞는 음식을 섭취해서 목표를 달성해 보아요", MessageType.LEFT, null, null, null, null, null))
        }
    }

    private fun calculateamountfood(food:food,remaindata:Totalcal) {
        recommendmeal=null
        val calculate_meal = food
        val maxNForkcal = calculateMaxN(calculate_meal.foodcal, remaindata.total_calory)
        val maxNForCarb = calculateMaxN(calculate_meal.foodcarbo, remaindata.total_carb)
        val maxNForProtein = calculateMaxN(calculate_meal.foodprotein, remaindata.total_protein)
        val maxNForFat = calculateMaxN(calculate_meal.foodfat, remaindata.total_fat)

        val maxN = minOf(maxNForkcal,maxNForCarb, maxNForProtein, maxNForFat)
        if (maxN > 0) {
            // Add the message with the calculated nutritional values
            messages.add(
                Message(
                    " ${(calculate_meal.foodamount*maxN).toInt()} g 드세요",
                    MessageType.LEFT_2,
                    remaindata.total_calory.toInt().toString()+"Kcal",
                    food.foodname,
                    (food.foodcal*maxN).toInt().toString()+"Kcal",
                    null,
                    null
                )
            )

            // Update the remaining nutritional values based on the selected cheat meal
            val recommendcal = calculate_meal.foodcal*maxN
            val recommendcarb = calculate_meal.foodcarbo * maxN
            val recommendprotein = calculate_meal.foodprotein * maxN
            val recommendfat = calculate_meal.foodfat * maxN
            val availableamount = maxN*calculate_meal.foodamount
            recommendmeal=food(calculate_meal.foodname,calculate_meal.foodbrand,recommendcal,availableamount,recommendcarb,recommendprotein,recommendfat)

        } else {
            // 영양성분으로 비교했을때 너무 작으면  남은 영양소로 해당 음식 못먹음을 알려줌
            // 일정량을 해서 구한다
            messages.add(Message("남은 영양소로는 해당 음식을 먹을 수 없어요!", MessageType.LEFT, null, null, null, null, null))
            messages.add(Message("남은 영양소에 맞는 음식을 섭취해서 목표를 달성해 보아요", MessageType.LEFT, null, null, null, null, null))
        }




    }
    private fun calculateMaxN(foodValue: Double, remainValue: Double): Double {
        return if (foodValue > 0 && remainValue > 0) {
            remainValue / foodValue
        } else {
            0.0
        }
    }

    private fun recommendpopluarMeal() {

        // 추천 메시지를 생성
        val recommendationMessage = "식단 추천: 오늘은 샐러드와 닭가슴살이 좋을 것 같아요!"

        // 어댑터에 메시지 추가 및 갱신
        val newMessage = Message(recommendationMessage, MessageType.LEFT,null,null,null,null,null)
        adapter.addMessage(newMessage)
        adapter.notifyDataSetChanged()
    }
    private fun savemeal(){
        AllListNutritionList = UserManager.getAllListNutritionList()
        var count: Int = 0
        var prevMealName: String? = null
        //meal개수를 가져옴
                for (i in AllListNutritionList) {
                    val mealName = i.mealname

                    // 이전 식단 이름과 현재 식단 이름이 다르면 count 증가
                    if (mealName != prevMealName) {
                        count++
                        prevMealName = mealName
                    }
                }
        count++
        UserdataList.add(MealData(realTime,"식단 ${count}",recommendmeal!!.foodname,recommendmeal!!.foodbrand,recommendmeal!!.foodcal,recommendmeal!!.foodamount,recommendmeal!!.foodcarbo,recommendmeal!!.foodprotein,recommendmeal!!.foodfat))
        saveMealDataToFirebase(UserdataList,count)
    }
    private fun saveMealDataToFirebase(mealDataList: List<MealData>,count:Int) {
        val mealName = "식단 ${count}"
        val dataRoute = firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단기입/$realTime/$mealName")
        dataRoute.removeValue()
        for(mealData in mealDataList){
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
            //AllListNutrition에 추가
            val permealeachfood =AllListNutrition(mealName,foodkey,mealData.foodcarbo,mealData.foodprotein,mealData.foodfat,mealData.foodcal)
            UserManager.addAllListNutrionList(permealeachfood)

            val permealeachfood2= MealData(realTime,mealName,mealData.foodname,mealData.foodbrand,mealData.foodcal,mealData.foodamount,mealData.foodcarbo,mealData.foodprotein,mealData.foodfat)
            UserManager.addMealData(permealeachfood2)
            if(foodkey!=null){
                dataRoute.child(foodkey).updateChildren(newData)
            }

        }
    }
    private fun foodFavoritesRoad(): ArrayList<food> {
        favoritefood.clear()
        val dataRoute = firebaseDatabase.getReference("사용자id별 초기설정값table/로그인한 사용자id/기능/식단 추천/자주먹는음식(즐겨찾기)")
        dataRoute.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (Fuserdata in dataSnapshot.children) {
                    val key = Fuserdata.key.toString()
                    val value1 = JsonPath.parse(Fuserdata.value)
                    if(key!=null){
                        val foodname=value1.read("$['식품이름']") as String
                        Log.i("ssexx", "onDataChange: $foodname")
                        val foodbrand=value1.read("$['가공업체']")as String
                        val foodcarbo=(value1.read("$['탄수화물']")as String).toDouble()
                        val foodprotein=(value1.read("$['단백질']")as String).toDouble()
                        val foodfat=(value1.read("$['지방']")as String).toDouble()
                        val foodamount=(value1.read("$['섭취량']")as String).toDouble()
                        Log.i("ssexx1", "onDataChange: $foodamount")
                        val foodkcal=(value1.read("$['열량']")as String).toDouble()
                        val appendFood=food(foodname,foodbrand,foodkcal,foodamount,foodcarbo,foodprotein,foodfat)
                        favoritefood.add(appendFood)
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return favoritefood
    }


}