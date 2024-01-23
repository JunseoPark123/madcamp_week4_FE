package com.example.madcamp_week4_fe.home

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.madcamp_week4_fe.R

class MapAddLoadingActivity : AppCompatActivity() {
    private val delay = 5000L //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_add_loading)

        Handler(Looper.getMainLooper()).postDelayed({
            finishLoading()
        }, delay)
    }

    private fun finishLoading() {
        setResult(Activity.RESULT_OK) // 결과 설정
        finish() // Activity 종료
    }
}