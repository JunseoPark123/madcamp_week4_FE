package com.example.madcamp_week4_fe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val loadingImageView: ImageView = findViewById(R.id.loadingMap)
        Glide.with(this)
            .asGif()
            .load(R.drawable.splash)
            .into(loadingImageView)
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000) // 5초 딜레이
            finish() // Activity 종료
        }

        // Handler to introduce a delay
        Handler().postDelayed({
            // Start MainActivity after 5 seconds
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Finish SplashActivity so it's not returned to when pressing back button
            finish()
        }, 5000) // 5000 milliseconds = 5 seconds
    }
}