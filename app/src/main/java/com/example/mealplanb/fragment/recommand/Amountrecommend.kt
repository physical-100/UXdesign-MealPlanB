package com.example.mealplanb.fragment.recommand

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
import androidx.core.view.isVisible
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealplanb.R
import com.example.mealplanb.adapter.AddFoodAdapter
import com.example.mealplanb.adapter.recommenfoodadapter
import com.example.mealplanb.databinding.FragmentAmountrecommendBinding
import com.example.mealplanb.dataclass.food
import com.example.mealplanb.fragment.SpecificFood_Fragment
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class Amountrecommend : Fragment() {
    lateinit var binding:FragmentAmountrecommendBinding
    val handler= Handler()
    lateinit var Adapter:recommenfoodadapter
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    var data = arrayListOf<food>(
    )
    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAmountrecommendBinding.inflate(inflater,container,false)

        binding.back.setOnClickListener {
            //뒤로 가기는 일단 있다가 구현하겠음

        }
//        initRecyclerView(data)
        binding.searchfood.addTextChangedListener(object : TextWatcher {
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
                        handler.removeCallbacksAndMessages(null)
                        data.clear()
                        initRecyclerView(data)
                    }
                }, 250)


            }
        })




        return binding.root
    }
    private fun initRecyclerView(data1:ArrayList<food>){
        binding.searchAmount.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,false)
        Adapter = recommenfoodadapter(data1, object : recommenfoodadapter.OnItemClickListener {
            override fun OnItemClick(data: food, holder: recommenfoodadapter.ViewHolder) {
                // 아이템 클릭 이벤트 처리
                itemClickListener?.OnDataClick(data)
                binding.apply {
                    searchAmount.visibility= View.GONE
                    confirm.isVisible = true
                    cancle.isVisible = true
                    confirm.setOnClickListener {
                        itemClickListener?.onItemClick("confirm")
                    }
                    cancle.setOnClickListener {
                        searchAmount.isVisible = true
                        initRecyclerView(data1)
                    }

                }

            }
        })
        binding.searchAmount.adapter = Adapter
        //음식목록 클릭시 어디 프래그먼ㅈㅈ트로 갈지 정하는 코드

    }
    private fun foodSearch(searchText:String) {
        // 함수로 쓰고 싶으면 내가 입력하는 검색어 즉 음식이름을 받아오면 되겠지
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val urlBuilder = StringBuilder("https://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1") /*URL*/
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=0cxqsWYWVGLjfocIV4MT9qrcIr%2FgK9uavD2ProqA4MV3nDtHfZgrroWPba4wxb1faltEa3KqznXN5lSEC0RSpA%3D%3D") /*Service Key*/

                //앱에서 검색하는곳에 검색하는 음식 이름을 아래 갈비찜 자리에 넣는다.
                urlBuilder.append("&" + URLEncoder.encode("desc_kor", "UTF-8") + "=" + URLEncoder.encode(searchText,"UTF-8")) /*식품이름*/
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
                        val salt=firstItem.getDouble("NUTR_CONT6")//나트륨
                        val appendFood=food(foodname,corporate,kcal,serving_wt,carbo,protein,fat)//food라는 객체 형식에 맞춰서 검색된 애들 이름을 넣어준다.
                        data.add(appendFood) //원하는 정보 넣어서 food형식으로 data인 arraylist에 추가해준다 그럼 그 어레이 리스트 내의 값들 출력된다. addfoodadapter에서
                    }
                    launch(Dispatchers.Main) {
                        Log.i("appendcomplete", data.toString())

                        initRecyclerView(data)
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
    interface OnDataLoadedListener {
        fun onDataLoaded(data: ArrayList<food>)
    }


}