package com.example.madcamp_week4_fe.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.madcamp_week4_fe.R
import android.util.Log
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.madcamp_week4_fe.SharedViewModel
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class MarkerPagerAdapter(
    private val markerDataList: List<MarkerData>,
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<MarkerPagerAdapter.MarkerViewHolder>() {


    inner class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewGallery: ImageView = itemView.findViewById(R.id.ivGallery)
        private val textViewTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val textViewLocation: TextView = itemView.findViewById(R.id.tvLocation)
        //private val viewToBlur: View = itemView.findViewById(R.id.view)

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
                    // Save additional details when marked as favorite
                    editor.putString(markerData.galTitle + "_url", markerData.galUrl)
                    editor.putString(markerData.galTitle + "_location", markerData.galLocation)
                    editor.putString(markerData.galTitle + "_keyword", markerData.galKeyword)
                    editor.putFloat(markerData.galTitle + "_lat", markerData.position.latitude.toFloat())
                    editor.putFloat(markerData.galTitle + "_lng", markerData.position.longitude.toFloat())
                } else {
                    // Remove saved details when unmarked as favorite
                    editor.remove(markerData.galTitle + "_url")
                    editor.remove(markerData.galTitle + "_location")
                    editor.remove(markerData.galTitle + "_keyword")
                    editor.remove(markerData.galTitle + "_lat")
                    editor.remove(markerData.galTitle + "_lng")
                }
                editor.apply()
                updateFavoriteStatus(ivFavor, ivButton, isFavorite)
                this@MarkerPagerAdapter.sharedViewModel.setFavoritesUpdated(true)
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

//            Blurry.with(itemView.context)
//                .radius(10)
//                .sampling(8)
//                .async()
//                .onto(viewToBlur as ViewGroup?)

        }

        private fun updateFavoriteStatus(ivFavor: ImageView, ivButton: ImageView, isFavorite: Boolean) {
            ivFavor.setImageResource(if (isFavorite) R.drawable.clickedfavor else R.drawable.favor)
            ivButton.setImageResource(if (isFavorite) R.drawable.btnclickedbackground else R.drawable.btnbackground)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_marker_page, parent, false)
        return MarkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val markerData = markerDataList[position]
        holder.bind(markerData) // Use the bind function to set the data
    }

    override fun getItemCount() = markerDataList.size


}