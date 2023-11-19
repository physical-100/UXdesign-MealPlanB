package com.example.mealplanb.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.example.mealplanb.CustomCountdownTimer
import com.example.mealplanb.databinding.FragmentFastTimer1Binding
import java.text.DecimalFormat
import kotlin.math.roundToInt


class Fast_Timer_1_Fragment : Fragment() {


    lateinit var binding: FragmentFastTimer1Binding
    private lateinit var timeTxt: TextView
    private lateinit var circularProgressBar: ProgressBar

    private val countdownTime = 3600 // 1hour 3600 second, 60 min
    private val clockTime = (countdownTime * 1000).toLong()
    private val progressTime = (clockTime/1000).toFloat()

    private val onBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            onBackPressedMethod()
        }

    }

    private lateinit var customCountdownTimer: CustomCountdownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFastTimer1Binding.inflate(inflater,container,false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)

        timeTxt = binding.timeTxt
        circularProgressBar = binding.circularProgressBar

        var secondsLeft = 0
        customCountdownTimer = object: CustomCountdownTimer(clockTime,1000){}
        customCountdownTimer.onTick = {millisUntilFinished ->
            val second = (millisUntilFinished / 1000.0f).roundToInt()
            if(second != secondsLeft){
                secondsLeft = second

                timerFormat(
                    secondsLeft,
                    timeTxt
                )
            }
        }
        customCountdownTimer.onFinish = {

        }
        circularProgressBar.max = progressTime.toInt()
        circularProgressBar.progress = progressTime.toInt()

        customCountdownTimer.startTimer()


        val pauseBtn = binding.pauseBtn
        val resumeBtn = binding.resumeBtn
        val resetBtn = binding.resetBtn

        pauseBtn.setOnClickListener {
            customCountdownTimer.pauseTimer()
        }
        resumeBtn.setOnClickListener {
            customCountdownTimer.resumeTimer()
        }
        resetBtn.setOnClickListener {
            circularProgressBar.progress = progressTime.toInt()
            customCountdownTimer.restartTimer()
        }
        return binding.root
    }

    private fun timerFormat(secondsLeft: Int, timeTxt: TextView) {
        circularProgressBar.progress = secondsLeft
        val decimalFormat = DecimalFormat("00")
        val hour = secondsLeft / 3600
        val min = (secondsLeft % 3600) / 60
        val seconds = secondsLeft % 60

        val timeFormat1 = decimalFormat.format(secondsLeft)
        val timeFormat2 = decimalFormat.format(min) + ":" +decimalFormat.format(seconds)
        val timeFormat3 = decimalFormat.format(hour) + ":" + decimalFormat.format(min) + ":" +decimalFormat.format(seconds)
        timeTxt.text = timeFormat1 + "\n" + timeFormat2 + "\n" + timeFormat3
    }

    override fun onPause() {
        customCountdownTimer.pauseTimer()
        super.onPause()
    }

    override fun onResume() {
        customCountdownTimer.resumeTimer()
        super.onResume()
    }

    override fun onDestroy() {
        customCountdownTimer.destroyTimer()
        super.onDestroy()
    }

    private fun onBackPressedMethod() {
        customCountdownTimer.destroyTimer()
        requireActivity().finish()
    }
}