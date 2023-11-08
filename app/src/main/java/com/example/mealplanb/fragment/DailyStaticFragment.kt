package com.example.mealplanb.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealplanb.databinding.FragmentDailyStaticBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate


class DailyStaticFragment : Fragment() {
    lateinit var binding:FragmentDailyStaticBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentDailyStaticBinding.inflate(inflater, container, false)
        val lineChart = binding.lineChart

        // 선 그래프에 표시할 데이터 생성
        val lineDataSet = LineDataSet(getDataLine(), "선 그래프")
        lineDataSet.colors = ColorTemplate.COLORFUL_COLORS.asList()

        // 데이터를 선 그래프에 설정
        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        // 다양한 설정을 추가로 할 수 있습니다. 예: 축 설정, 레전드 설정 등

        // 그래프를 화면에 그립니다.
        lineChart.invalidate()
        // 막대 차트 그리는 코드
        val barChart = binding.barChart

        // 막대 그래프에 표시할 데이터 생성
        val barDataSet = BarDataSet(getDataBar(), "막대 그래프")
        barDataSet.colors = ColorTemplate.COLORFUL_COLORS.asList()

        // 데이터를 막대 그래프에 설정
        val barData = BarData(barDataSet)
        barChart.data = barData

        // 다양한 설정을 추가로 할 수 있습니다. 예: 축 설정, 레전드 설정 등

        // 그래프를 화면에 그립니다.
        barChart.invalidate()







    return binding.root
    }
    private fun getDataLine(): List<Entry> {
        val entries = ArrayList<Entry>()
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 5f))
        entries.add(Entry(3f, 15f))
        entries.add(Entry(4f, 7f))
        entries.add(Entry(5f, 20f))
        //통계 데이터 가져와야함

        return entries
    }
    private fun getDataBar(): List<BarEntry> {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, 10f))
        entries.add(BarEntry(2f, 5f))
        entries.add(BarEntry(3f, 15f))
        entries.add(BarEntry(4f, 7f))
        entries.add(BarEntry(5f, 20f))
        return entries
    }
}