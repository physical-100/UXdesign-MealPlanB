package com.example.mealplanb.fragment

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.R
import com.example.mealplanb.adapter.AddFoodAdapter
import com.example.mealplanb.databinding.FragmentAddDietBinding
import com.example.mealplanb.dataclass.food
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.math.log

class Add_Diet_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    var binding: FragmentAddDietBinding?=null
    val handler=Handler()
    var adapter: AddFoodAdapter?=null
    var data = arrayListOf<food>(
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
        binding!!.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    val searchText = s.toString()
                    if (searchText.isNotEmpty()) {
                        // 검색어가 비어있지 않은 경우에만 검색 수행
                        Log.i("foodsearch", "afterTextChanged: ${searchText}")
                        data.clear()
                        foodSearch(searchText)
                    } else {
                        // 검색어가 비어있으면 모든 데이터 표시
                        handler.removeCallbacksAndMessages(null)
                        data.clear()
                        adapter?.setData(data)
                    }
                }, 250)


            }
        })
        //음식 검색한 다음에
        //현우 API에서 식품검색해서 받아오기 firebase database랑 dataclass food?
//        adapter!!.itemClickListener//어떻게 해서 일단 클릭을 했다고 가정하자 그럼 클릭한 음식의 정보들 다 전달

    }

    private fun initRecyclerView(){
        binding!!.recyclerview.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        Log.i("foodview", "initRecyclerView: ")
        Log.i("foodlist",data.toString())
        adapter = AddFoodAdapter(data)
        binding!!.recyclerview.adapter = adapter

        //음식목록 클릭시 어디 프래그먼ㅈㅈ트로 갈지 정하는 코드
        adapter!!.itemClickListener = object : AddFoodAdapter.OnItemClickListener{
            override fun OnItemClick(data: food, holder: AddFoodAdapter.ViewHolder) {
                Log.i("food1234", "$data")
                adapter!!.notifyItemChanged(holder.adapterPosition)

               // val bundle= bundleOf("add food" to data)
                //Log.i("s", "$bundle ")
                //findNavController().navigate(R.id.action_add_Diet_Fragment_to_specificFood_Fragment,bundle)
            }
            override fun OnaddBtnClick(data: food, holder: RecyclerView.ViewHolder) {
                Log.i("food123","$data add button")
                adapter!!.notifyItemChanged(holder.adapterPosition)
            }
        }
        
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    private fun foodSearch(searchText:String) { // 함수로 쓰고 싶으면 내가 입력하는 검색어 즉 음식이름을 받아오면 되겠지
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val urlBuilder = StringBuilder("https://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1") /*URL*/
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=0cxqsWYWVGLjfocIV4MT9qrcIr%2FgK9uavD2ProqA4MV3nDtHfZgrroWPba4wxb1faltEa3KqznXN5lSEC0RSpA%3D%3D") /*Service Key*/

                //앱에서 검색하는곳에 검색하는 음식 이름을 아래 갈비찜 자리에 넣는다.
                urlBuilder.append("&" + URLEncoder.encode("desc_kor", "UTF-8") + "=" + URLEncoder.encode(searchText,"UTF-8")) /*식품이름*/
//        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")) /*페이지번호*/
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("3", "UTF-8")) /*한 페이지 결과 수*/
//        urlBuilder.append("&" + URLEncoder.encode("bgn_year", "UTF-8") + "=" + URLEncoder.encode("2017", "UTF-8")) /*구축년도*/
//        urlBuilder.append("&" + URLEncoder.encode("animal_plant", "UTF-8") + "=" + URLEncoder.encode("(유)돌코리아", "UTF-8")) /*가공업체*/
                urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")) /*응답데이터 형식(xml/json) Default: xml*/
                
                val url = URL(urlBuilder.toString())
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.setRequestProperty("Content-type", "application/json")
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    Log.i("fofo1", "${conn.responseCode}")
                    val rd = BufferedReader(InputStreamReader(conn.inputStream))
                    val sb = StringBuilder()
                    var line: String?
                    while (rd.readLine().also { line = it } != null) {
                        sb.append(line)
                    }
                    rd.close()
                    val jsonObject = JSONObject(sb.toString())
                    val bodyObject = jsonObject.getJSONObject("body")
                    val itemsArray = bodyObject.getJSONArray("items")
                    bodyObject.length()

                    for(i in 0 until itemsArray.length()){  //지금 "갈비찜"으로 검색했을 때 나온 항목들의 갯수를 itemsArray.length()의미한다. ex)소갈비찜, 돼지갈비찜 등등
                        val firstItem = itemsArray.getJSONObject(i) //인덱스 0번은 첫번째 항목
                        val foodname = firstItem.getString("DESC_KOR") //음식 이름
                        val corporate = firstItem.getString("ANIMAL_PLANT") //가공업체
                        val serving_wt=firstItem.getDouble("SERVING_WT")//1회 제공량
                        val kcal=firstItem.getDouble("NUTR_CONT1")//열량
                        val carbo=firstItem.getDouble("NUTR_CONT2")//탄
                        val protein=firstItem.getDouble("NUTR_CONT3")//단
                        val fat=firstItem.getDouble("NUTR_CONT4")//지
                       // val sweet=firstItem.getDouble("NUTR_CONT5")//당류
                        val salt=firstItem.getDouble("NUTR_CONT6")//나트륨
                        //val cholesterol=firstItem.getDouble("NUTR_CONT7")//콜레스테롤
                        val appendFood=food(foodname,corporate,kcal,serving_wt,carbo,protein,fat)//food라는 객체 형식에 맞춰서 검색된 애들 이름을 넣어준다.
                        data.add(appendFood) //원하는 정보 넣어서 food형식으로 data인 arraylist에 추가해준다 그럼 그 어레이 리스트 내의 값들 출력된다. addfoodadapter에서
                    }
                    launch(Dispatchers.Main) {
                        Log.i("appendcomplete", data.toString())
                        initRecyclerView()
                        //adapter?.setData(data)

                        //binding!!.recyclerview.adapter = adapter
                        // 결과를 TextView에 출력
//                        resultTextView.text = "식품이름: $descKor\n1회 제공량 :$serving_wt\n칼로리 :$kcal\n탄수화물 :$carbo\n단백질 :$protein\n지방: $fat\n당류 :$sweet\n나트륨 :$salt\n콜레스테롤 :$cholesterol\n"
                    }
                } else {
                    // Handle error
                }
                conn.disconnect()
            } catch (e: IOException) {
                Log.i("exception", "IOException : ${e.message} ")
                // Handle exception
            }
            catch (e: JSONException) {
                Log.e("foodSearch", "JSONException: ${e.message}")
            }
        }
    }
}