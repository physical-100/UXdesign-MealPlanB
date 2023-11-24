package com.example.mealplanb.fragment

import android.os.Bundle
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
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.utils.MPPointF

class DailyStaticFragment : Fragment() {
    lateinit var binding: FragmentDailyStaticBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDailyStaticBinding.inflate(inflater, container, false)

        // 막대 차트 그리는 코드
        val barChart = binding.barChart

        // X축 설정
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 0.1f // X축 간격
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)//뒤에 선 지우자

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
        entries.add(BarEntry(1f, floatArrayOf(800f, 400f, 1200f))) // 칼로리 데이터 (탄, 단, 지)
        entries.add(BarEntry(2f, floatArrayOf(500f, 200f, 800f)))
        entries.add(BarEntry(3f, floatArrayOf(1500f, 700f, 200f)))
        entries.add(BarEntry(4f, floatArrayOf(700f, 300f, 1000f)))
        entries.add(BarEntry(5f, floatArrayOf(2000f, 800f, 200f)))
        entries.add(BarEntry(6f, floatArrayOf(1000f, 500f, 700f)))
        entries.add(BarEntry(7f, floatArrayOf(2500f, 1000f, 500f)))
        return entries
    }
}
