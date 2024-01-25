package com.example.madcamp_week4_fe.analysis

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.madcamp_week4_fe.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AnalysisLoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis_loading)

        val loadingImageView: ImageView = findViewById(R.id.loadingMap)
        Glide.with(this)
            .asGif()
            .load(R.drawable.loading)
            .into(loadingImageView)
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000) // 5초 딜레이
            finish() // Activity 종료
        }
    }
}
//        val shouldCloseImmediately = intent.getBooleanExtra("close", false)
//        if (shouldCloseImmediately) {
//            closeLoading()
//        } else {
//            Handler(Looper.getMainLooper()).postDelayed({ closeLoading() }, 7000) // 7초 후 종료
//        }
//    }
//
//    private fun closeLoading() {
//        setResult(Activity.RESULT_OK) // 결과 설정
//        finish() // Activity 종료
//    }
//}