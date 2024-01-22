package com.example.madcamp_week4_fe

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class LoadingActivity : AppCompatActivity() {
    private val DELAY_MILLIS = 3000L // 예를 들어 3초 후에 종료

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // Handler를 사용하여 일정 시간 후에 Activity 종료
        Handler(Looper.getMainLooper()).postDelayed({
            finishLoading()
        }, DELAY_MILLIS)
    }

    private fun finishLoading() {
        setResult(Activity.RESULT_OK) // 결과 설정
        finish() // Activity 종료
    }
}