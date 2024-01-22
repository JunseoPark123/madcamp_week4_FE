package com.example.madcamp_week4_fe.home

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.madcamp_week4_fe.R

class MapResetLoadingActivity : AppCompatActivity() {
    private val delay = 4000L // 예를 들어 3초 후에 종료

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_reset_loading)

        // Handler를 사용하여 일정 시간 후에 Activity 종료
        Handler(Looper.getMainLooper()).postDelayed({
            finishLoading()
        }, delay)
    }

    private fun finishLoading() {
        setResult(Activity.RESULT_OK) // 결과 설정
        finish() // Activity 종료
    }
}