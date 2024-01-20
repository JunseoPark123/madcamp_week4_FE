package com.example.madcamp_week4_fe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.madcamp_week4_fe.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater) // 이 부분 추가
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra("galWebImageUrl")
        Log.d("GalleryActivity", "Received Image URL: $imageUrl")
        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivGallery)

        binding.ivGallery.setOnClickListener {
            Log.d("GalleryActivity", "Image clicked, returning to HomeFragment")
            finish()
        }
    }
}