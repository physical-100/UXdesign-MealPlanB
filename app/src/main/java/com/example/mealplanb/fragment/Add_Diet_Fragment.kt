package com.example.mealplanb.fragment

import android.os.Bundle
import android.util.Log
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
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class Add_Diet_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    var binding: FragmentAddDietBinding?=null
    var adapter: AddFoodAdapter?=null
    var data = arrayListOf<food>(
        food("현미밥","농심",100.0,100.6)
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
        foodSearch()// 검색하고자 하는 음식이름을 인자값으로 넘기면 된다.




        initRecyclerView()
        //현우 API에서 식품검색해서 받아오기 firebase database랑 dataclass food?
        
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
    private fun foodSearch(){ // 함수로 쓰고 싶으면 내가 입력하는 검색어 즉 음식이름을 받아오면 되겠지
        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.i("wkdgusdn","afshdkfj")
                val urlBuilder = StringBuilder("http://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1") /*URL*/
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=0cxqsWYWVGLjfocIV4MT9qrcIr%2FgK9uavD2ProqA4MV3nDtHfZgrroWPba4wxb1faltEa3KqznXN5lSEC0RSpA%3D%3D") /*Service Key*/

                //앱에서 검색하는곳에 검색하는 음식 이름을 아래 갈비찜 자리에 넣는다.
                urlBuilder.append("&" + URLEncoder.encode("desc_kor", "UTF-8") + "=" + URLEncoder.encode("갈비찜","UTF-8")) /*식품이름*/
//        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")) /*페이지번호*/
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("3", "UTF-8")) /*한 페이지 결과 수*/
//        urlBuilder.append("&" + URLEncoder.encode("bgn_year", "UTF-8") + "=" + URLEncoder.encode("2017", "UTF-8")) /*구축년도*/
//        urlBuilder.append("&" + URLEncoder.encode("animal_plant", "UTF-8") + "=" + URLEncoder.encode("(유)돌코리아", "UTF-8")) /*가공업체*/
                urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")) /*응답데이터 형식(xml/json) Default: xml*/


                val url = URL(urlBuilder.toString())
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty("Content-type", "application/json")
                Log.i("fofo", "foodSearch: ")
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    Log.i("fofo1", "foodSearch: ")
                    val rd = BufferedReader(InputStreamReader(conn.inputStream))
                    val sb = StringBuilder()
                    var line: String?
                    while (rd.readLine().also { line = it } != null) {
                        sb.append(line)
                    }
                    Log.i("fofa", "foodSearch: ")
                    rd.close()
                    val jsonObject = JSONObject(sb.toString())
                    val bodyObject = jsonObject.getJSONObject("body")
                    val itemsArray = bodyObject.getJSONArray("items")
                    // bodyObject.length()
                    Log.i("fofo2", "${itemsArray.length()} ")
                    for(i in 1..itemsArray.length()){  //지금 "갈비찜"으로 검색했을 때 나온 항목들의 갯수를 itemsArray.length()의미한다. ex)소갈비찜, 돼지갈비찜 등등
                        Log.i("fofo3", "foodSearch: ")
                        val firstItem = itemsArray.getJSONObject(i) //인덱스 0번은 첫번째 항목
                        val foodname = firstItem.getString("DESC_KOR") //음식 이름
                        val corporate= firstItem.getString("animal_plant") //가공업체
                        val serving_wt=firstItem.getDouble("SERVING_WT")//1회 제공량
                        val kcal=firstItem.getDouble("NUTR_CONT1")//열량
                        val carbo=firstItem.getDouble("NUTR_CONT2")//탄
                        val protein=firstItem.getDouble("NUTR_CONT3")//단
                        val fat=firstItem.getDouble("NUTR_CONT4")//지
                        val sweet=firstItem.getDouble("NUTR_CONT5")//당류
                        val salt=firstItem.getDouble("NUTR_CONT6")//나트륨
                        val cholesterol=firstItem.getDouble("NUTR_CONT7")//콜레스테롤
                        val appendFood=food(foodname,corporate,kcal,serving_wt)//food라는 객체 형식에 맞춰서 검색된 애들 이름을 넣어준다.
                        Log.i("foodname","$foodname")
                        Log.i("foodname1","wkfjsdjf")
                        data.add(appendFood) //원하는 정보 넣어서 food형식으로 data인 arraylist에 추가해준다 그럼 그 어레이 리스트 내의 값들 출력된다. addfoodadapter에서

                        //resultTextView.text = "식품이름: $descKor\n1회 제공량 :$serving_wt\n칼로리 :$kcal\n탄수화물 :$carbo\n단백질 :$protein\n지방: $fat\n당류 :$sweet\n나트륨 :$salt\n콜레스테롤 :$cholesterol\n"
                        //각 항목에 대한 정보들을 출력하는 느낌





                    }
                    launch(Dispatchers.Main) {
                        // 결과를 TextView에 출력
//                        resultTextView.text = "식품이름: $descKor\n1회 제공량 :$serving_wt\n칼로리 :$kcal\n탄수화물 :$carbo\n단백질 :$protein\n지방: $fat\n당류 :$sweet\n나트륨 :$salt\n콜레스테롤 :$cholesterol\n"
                    }

                    //val servingWt = firstItem.getString("SERVING_WT")



//                    launch(Dispatchers.Main) {
//                        resultTextView.text = sb.toString()
//                    }
                } else {
                    // Handle error
                }
                conn.disconnect()
            } catch (e: IOException) {
                // Handle exception
            }
        }
    }
}