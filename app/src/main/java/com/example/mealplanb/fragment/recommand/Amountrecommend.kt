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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealplanb.adapter.recommenfoodadapter
import com.example.mealplanb.databinding.FragmentAmountrecommendBinding
import com.example.mealplanb.dataclass.food
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
    lateinit var binding: FragmentAmountrecommendBinding
    val handler = Handler()
    lateinit var Adapter: recommenfoodadapter
    var data = arrayListOf<food>()

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
        binding = FragmentAmountrecommendBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener {
            //뒤로 가기는 일단 있다가 구현하겠음
        }

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

    private fun initRecyclerView(data1: ArrayList<food>) {
        binding.searchAmount.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        Adapter = recommenfoodadapter(data1, object : recommenfoodadapter.OnItemClickListener {
            override fun OnItemClick(data: food, holder: recommenfoodadapter.ViewHolder) {
                // 아이템 클릭 이벤트 처리
                itemClickListener?.onDataClick(data)
                Log.i("clickfood", data.toString())
            }
        })
        binding.searchAmount.adapter = Adapter
    }

    private fun foodSearch(searchText: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val urlBuilder =
                    StringBuilder("https://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1") /*URL*/
                urlBuilder.append(
                    "?" + URLEncoder.encode(
                        "serviceKey",
                        "UTF-8"
                    ) + "=0cxqsWYWVGLjfocIV4MT9qrcIr%2FgK9uavD2ProqA4MV3nDtHfZgrroWPba4wxb1faltEa3KqznXN5lSEC0RSpA%3D%3D"
                ) /*Service Key*/
                urlBuilder.append(
                    "&" + URLEncoder.encode("desc_kor", "UTF-8") + "=" + URLEncoder.encode(
                        searchText,
                        "UTF-8"
                    )
                ) /*식품이름*/
                urlBuilder.append(
                    "&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(
                        "json",
                        "UTF-8"
                    )
                ) /*응답데이터 형식(xml/json) Default: xml*/

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

                    for (i in 0 until itemsArray.length()) {
                        val firstItem = itemsArray.getJSONObject(i)
                        val foodname = firstItem.getString("DESC_KOR")
                        val corporate = firstItem.getString("ANIMAL_PLANT")
                        val serving_wt = firstItem.getDouble("SERVING_WT")
                        val kcal = firstItem.getDouble("NUTR_CONT1")
                        val carbo = firstItem.getDouble("NUTR_CONT2")
                        val protein = firstItem.getDouble("NUTR_CONT3")
                        val fat = firstItem.getDouble("NUTR_CONT4")
                        val salt = firstItem.getDouble("NUTR_CONT6")
                        val appendFood =
                            food(foodname, corporate, kcal, serving_wt, carbo, protein, fat)
                        data.add(appendFood)
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
            } catch (e: JSONException) {
                Log.e("foodSearch", "JSONException: ${e.message}")
            }
        }
    }
}
