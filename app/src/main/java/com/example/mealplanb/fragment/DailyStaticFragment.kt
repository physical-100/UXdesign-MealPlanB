package com.example.mealplanb.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.mealplanb.databinding.FragmentDailyStaticBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.example.mealplanb.R
import com.example.mealplanb.Totalcal
import com.example.mealplanb.UserManager
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DailyStaticFragment : Fragment() {
    lateinit var binding: FragmentDailyStaticBinding
    val currentTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val realTime = dateFormat.format(currentTime) //현재 시간 받아오는거짜
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    val dayOfWeek = dayFormat.format(currentTime) // 현재 요일 (문자열)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDailyStaticBinding.inflate(inflater, container, false)
        Log.i("날짜",currentTime.toString())
        binding.date.text=realTime+" "+dayOfWeek

        //클릭 했을때 일자별로 섭취한 총 칼로리를 표시

        binding.dateKcal.text = "섭취한 칼로리" +""

        // 막대 차트 그리는 코드
        val barChart = binding.barChart

        // X축 설정
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f // X축 간격을 1로 변경 (일일 간격)
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(true)//뒤에 선 지우자
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                // Convert the float value to a date string
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_MONTH, value.toInt())
                return SimpleDateFormat("MM-dd", Locale.getDefault()).format(calendar.time)
            }
        }

        // Y축 설정
        val yAxisRight: YAxis = barChart.axisRight
        yAxisRight.isEnabled = false // 오른쪽 Y축은 사용하지 않음

        val yAxisLeft: YAxis = barChart.axisLeft
        yAxisLeft.granularity = 500f // Y축 간격
        yAxisLeft.axisMinimum = 0f // Y축 최솟값
        yAxisLeft.axisMaximum = 3000f // Y축 최댓값


        // 막대 그래프에 표시할 데이터 생성
        val barDataSet = BarDataSet(getDataBar(), "칼로리")
        barDataSet.setColors(
            requireContext().resources.getColor(R.color.pink),
            requireContext().resources.getColor(R.color.white),
            requireContext().resources.getColor(R.color.black)
        )
        // 막대의 끝을 둥글게 설정


// Create a BarData and set it to the chart
        val barData = BarData(barDataSet)
        barChart.data = barData


        // 다양한 설정을 추가로 할 수 있습니다. 예: 레전드 설정 등

        // 그래프를 화면에 그립니다.
        barChart.invalidate()

        return binding.root
    }

    private fun getDataBar(): List<BarEntry> {
        val entries = ArrayList<BarEntry>()
        val data = UserManager.getTotalcal()
        // Get the date for today
        val calendar = Calendar.getInstance()

        // Loop through the past 7 days and add data to the graph
        for (i in 6 downTo 0) {
            if (i ==6) {
                entries.add(
                    BarEntry(
                        i.toFloat(),
                        floatArrayOf(
                            data!!.total_carb.toFloat(),
                            data!!.total_protein.toFloat(),
                            data.total_fat.toFloat()
                        )
                    )
                )
            }// 칼로리 데이터 (탄, 단, 지)
            else{
                entries.add(
                    BarEntry(
                        i.toFloat(),
                        floatArrayOf(
                            0.0f,0.0f,0.0f
                        )
                    )
                )
            }
            calendar.add(Calendar.DAY_OF_MONTH, -1) // Move to the previous day
        }

        // Reverse the list to display data in chronological order
        entries.reverse()

        return entries
    }

}
