package com.example.madcamp_week4_fe.analysis

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.madcamp_week4_fe.R
import com.example.madcamp_week4_fe.SharedViewModel
import com.example.madcamp_week4_fe.home.MarkerData
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class AnalysisPagerAdapter(
    private val markerDataList: List<MarkerData>,
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<AnalysisPagerAdapter.AnalysisViewHolder>() {


    inner class AnalysisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewGallery: ImageView = itemView.findViewById(R.id.ivGallery)
        private val textViewTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val textViewLocation: TextView = itemView.findViewById(R.id.tvLocation)
        fun bind(markerData: MarkerData) {
            textViewTitle.text = markerData.galTitle
            textViewLocation.text = markerData.galLocation

            val ivFavor = itemView.findViewById<ImageView>(R.id.ivFavor)
            val ivButton = itemView.findViewById<ImageView>(R.id.ivBtnBackground)
            val sharedPreferences = itemView.context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)

            // Update the favorite status every time bind is called
            var isFavorite = sharedPreferences.getBoolean(markerData.galTitle, false)
            updateFavoriteStatus(ivFavor, ivButton, isFavorite)

            itemView.findViewById<ImageView>(R.id.ivBtnBackground).setOnClickListener {
                isFavorite = !isFavorite // Toggle the favorite status
                val editor = sharedPreferences.edit()
                editor.putBoolean(markerData.galTitle, isFavorite)
                if (isFavorite) {
                    // 선호로 설정
                    editor.putBoolean(markerData.galTitle, true)
                    editor.putString(markerData.galTitle + "_url", markerData.galUrl)
                    editor.putString(markerData.galTitle + "_location", markerData.galLocation)
                    editor.putString(markerData.galTitle + "_keyword", markerData.galKeyword)
                    editor.putFloat(markerData.galTitle + "_lat", markerData.position.latitude.toFloat())
                    editor.putFloat(markerData.galTitle + "_lng", markerData.position.longitude.toFloat())
                } else {
                    // 선호 해제 - 모든 관련 데이터를 삭제
                    editor.remove(markerData.galTitle)
                    editor.remove(markerData.galTitle + "_url")
                    editor.remove(markerData.galTitle + "_location")
                    editor.remove(markerData.galTitle + "_keyword")
                    editor.remove(markerData.galTitle + "_lat")
                    editor.remove(markerData.galTitle + "_lng")
                }
                editor.apply()
                updateFavoriteStatus(ivFavor, ivButton, isFavorite)
                this@AnalysisPagerAdapter.sharedViewModel.setFavoritesUpdated(true)
            }

            itemView.findViewById<ImageView>(R.id.ivBtnBackground1).setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, AnalysisActivity::class.java)
                // Optionally, pass data to AnalysisActivity using intent.putExtra()
                // For example: intent.putExtra("extra_data", markerData.someField)
                intent.putExtra("title", markerData.galTitle)
                intent.putExtra("latitude", markerData.position.latitude.toFloat())
                intent.putExtra("longitude", markerData.position.longitude.toFloat())

                context.startActivity(intent)
            }





            // URL 로그 출력
            Log.d("MarkerPagerAdapter", "Loading image from URL: ${markerData.galUrl}")

            // Glide를 사용하여 이미지를 ImageView에 로드합니다.
            Glide.with(itemView.context)
                .load(markerData.galUrl)
                .transform(
                    CenterCrop(),
                    RoundedCornersTransformation(144, 0, RoundedCornersTransformation.CornerType.TOP)
                )
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        Log.e("MarkerPagerAdapter", "Image load failed", e)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        Log.d("MarkerPagerAdapter", "Image load success")
                        return false
                    }
                })
                .into(imageViewGallery)
        }

        private fun updateFavoriteStatus(ivFavor: ImageView, ivButton: ImageView, isFavorite: Boolean) {
            ivFavor.setImageResource(if (isFavorite) R.drawable.clickedfavor else R.drawable.favor)
            ivButton.setImageResource(if (isFavorite) R.drawable.btnclickedbackground else R.drawable.btnbackground)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalysisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_analysis, parent, false)
        return AnalysisViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnalysisViewHolder, position: Int) {
        val markerData = markerDataList[position]
        holder.bind(markerData) // Use the bind function to set the data
    }

    override fun getItemCount() = markerDataList.size


}
