    package com.example.mealplanb

    import android.app.PendingIntent.OnFinished
    import android.os.CountDownTimer

    open class CustomCountdownTimer(
        private val millisInFuture : Long,
        private val countDownInterval : Long
    ){
        private var millisUtilFinished = millisInFuture
        private var timer = InternalTimer(this,millisInFuture, countDownInterval)
        private var isRunning = false
        var onTick: ((millisUntilFinished: Long) -> Unit)? = null
        var onFinish: (() -> Unit)? = null
        private class InternalTimer(
            private val parent : CustomCountdownTimer,
            millisInFuture: Long,
            countDownInterval: Long
        ): CountDownTimer(millisInFuture,countDownInterval){
            var millisUntilsFinished = parent.millisUtilFinished
            override fun onTick(millisUntilsFinished: Long) {
                this.millisUntilsFinished = millisUntilsFinished
                parent.onTick?.invoke(millisUntilsFinished)
            }

            override fun onFinish() {
                millisUntilsFinished = 0
                parent.onFinish?.invoke()
            }

        }
        fun pauseTimer(){
            timer.cancel()
            isRunning = false
        }
        fun resumeTimer(){
            if(!isRunning && timer.millisUntilsFinished > 0){
                timer = InternalTimer(this, timer.millisUntilsFinished,countDownInterval)
                startTimer()
            }
        }
        fun startTimer(){
            timer.start()
            isRunning = true
        }
        fun restartTimer(){
            timer.cancel()
            timer = InternalTimer(this, millisInFuture,countDownInterval)
            startTimer()
        }
        fun destroyTimer(){
            timer.cancel()
        }
    }