package com.example.madcamp_week4_fe.analysis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.madcamp_week4_fe.R

class AnalysisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val loadingIntent = Intent(this, AnalysisLoadingActivity::class.java)
        startActivity(loadingIntent)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        val title = intent.getStringExtra("title")
        val latitude = intent.getFloatExtra("latitude", 0.0f)
        val longitude = intent.getFloatExtra("longitude", 0.0f)

        findViewById<TextView>(R.id.tvName).text = title

        findViewById<ImageView>(R.id.back).setOnClickListener {
            // Finish this activity and return to the previous one
            finish()
        }
    }
}