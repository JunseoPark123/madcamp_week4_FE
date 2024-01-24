package com.example.madcamp_week4_fe.home

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
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class MarkerPagerAdapter(
    private val markerDataList: List<MarkerData>
) : RecyclerView.Adapter<MarkerPagerAdapter.MarkerViewHolder>() {

    class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewGallery: ImageView = itemView.findViewById(R.id.ivGallery)
        private val textViewTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val textViewLocation: TextView = itemView.findViewById(R.id.tvLocation)

        fun bind(markerData: MarkerData) {
            textViewTitle.text = markerData.galTitle
            textViewLocation.text = markerData.galLocation

            // URL 로그 출력
            Log.d("MarkerPagerAdapter", "Loading image from URL: ${markerData.galUrl}")

            // Glide를 사용하여 이미지를 ImageView에 로드합니다.
            Glide.with(itemView.context)
                .load(markerData.galUrl)
                .transform(
                    CenterCrop(),
                    RoundedCornersTransformation(120, 0, RoundedCornersTransformation.CornerType.TOP)
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