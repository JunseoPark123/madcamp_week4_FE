package com.example.madcamp_week4_fe.home

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.madcamp_week4_fe.R

class MapResetLoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_reset_loading)

        val loadingImageView: ImageView = findViewById(R.id.loadingMap)
        Glide.with(this)
            .asGif()
            .load(R.drawable.loading)
            .into(loadingImageView)

        val shouldCloseImmediately = intent.getBooleanExtra("close", false)
        if (shouldCloseImmediately) {
            Log.d("MapResetLoadingActivity", "Closing immediately")
            closeLoading()
        } else {
            Log.d("MapResetLoadingActivity", "Waiting 5 seconds before closing")
            Handler(Looper.getMainLooper()).postDelayed({ closeLoading() }, 7000) // 3 seconds delay
        }
    }

    private fun closeLoading() {
        setResult(Activity.RESULT_OK) // Set result
        finish() // Finish Activity
    }
}