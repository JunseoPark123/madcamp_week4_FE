package com.example.madcamp_week4_fe.search

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.madcamp_week4_fe.R

class SearchLoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_loading)

        val loadingImageView: ImageView = findViewById(R.id.loadingMap)
        Glide.with(this)
            .asGif()
            .load(R.drawable.loading)
            .into(loadingImageView)

        val shouldCloseImmediately = intent.getBooleanExtra("close", false)
        if (shouldCloseImmediately) {
            closeLoading()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({ closeLoading() }, 7000) // 7초 후 종료
        }
    }

    private fun closeLoading() {
        setResult(Activity.RESULT_OK) // 결과 설정
        finish() // Activity 종료
    }
}