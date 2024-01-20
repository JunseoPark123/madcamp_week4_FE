package com.example.madcamp_week4_fe

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week4_fe.databinding.ItemLocationInfoBinding


class LocationInfoAdapter(private val items: MutableList<LocationInfoItem>, private val context: Context)
    : RecyclerView.Adapter<LocationInfoAdapter.LocationInfoViewHolder>() {

    class LocationInfoViewHolder(private val binding: ItemLocationInfoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationInfoItem, context: Context) {
            binding.tvGalTitle.text = item.galTitle
            binding.tvGalLocation.text = item.galLocation
            binding.tvGalKeyword.text = item.galKeyword

            binding.btnPicture.setOnClickListener {
                Log.d("LocationInfoAdapter", "Picture button clicked. URL: ${item.galUrl}")
                val intent = Intent(context, GalleryActivity::class.java).apply {
                    putExtra("galWebImageUrl", item.galUrl)
                }
                context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationInfoViewHolder {
        val binding = ItemLocationInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationInfoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, context)
    }

    override fun getItemCount() = items.size
}