package com.example.madcamp_week4_fe.search

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week4_fe.databinding.ItemSearchInfoBinding


class SearchInfoAdapter(private val items: MutableList<SearchInfoItem>, private val context: Context)
    : RecyclerView.Adapter<SearchInfoAdapter.SearchInfoViewHolder>() {

    class SearchInfoViewHolder(private val binding: ItemSearchInfoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchInfoItem, context: Context) {
            binding.tvGalTitle.text = item.galTitle
            binding.tvGalLocation.text = item.galLocation
            binding.tvGalKeyword.text = item.galKeyword

            binding.btnPicture.setOnClickListener {
                Log.d("SearchInfoAdapter", "Picture button clicked. URL: ${item.galUrl}")
                val intent = Intent(context, GalleryActivity::class.java).apply {
                    putExtra("galWebImageUrl", item.galUrl)
                }
                context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchInfoViewHolder {
        val binding = ItemSearchInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchInfoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, context)
    }

    override fun getItemCount() = items.size
}