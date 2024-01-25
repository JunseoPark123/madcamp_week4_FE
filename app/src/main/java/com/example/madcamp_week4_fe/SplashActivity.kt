package com.example.madcamp_week4_fe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

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