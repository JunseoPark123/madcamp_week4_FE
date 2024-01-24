package com.example.madcamp_week4_fe.search

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week4_fe.R
import com.example.madcamp_week4_fe.databinding.ItemSearchBinding
import com.example.madcamp_week4_fe.home.MarkerData


class SearchAdapter(private val items: MutableList<MarkerData>, private val context: Context) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MarkerData, context: Context) {
            binding.tvName.text = item.galTitle
            binding.tvFavor.text = item.galLocation

            val ivFavor = itemView.findViewById<ImageView>(R.id.ivFavor)
            val ivButton = itemView.findViewById<ImageView>(R.id.ivBtnBackground)
            val sharedPreferences = itemView.context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)

            // Update the favorite status every time bind is called
            var isFavorite = sharedPreferences.getBoolean(item.galTitle, false)
            updateFavoriteStatus(ivFavor, ivButton, isFavorite)

            itemView.findViewById<ImageView>(R.id.ivBtnBackground).setOnClickListener {
                isFavorite = !isFavorite // Toggle the favorite status
                val editor = sharedPreferences.edit()
                editor.putBoolean(item.galTitle, isFavorite)
                if (isFavorite) {
                    // Save additional details when marked as favorite
                    editor.putString(item.galTitle + "_url", item.galUrl)
                    editor.putString(item.galTitle + "_location", item.galLocation)
                    editor.putString(item.galTitle + "_keyword", item.galKeyword)
                    editor.putFloat(item.galTitle + "_lat", item.position.latitude.toFloat())
                    editor.putFloat(item.galTitle + "_lng", item.position.longitude.toFloat())
                } else {
                    // Remove saved details when unmarked as favorite
                    editor.remove(item.galTitle + "_url")
                    editor.remove(item.galTitle + "_location")
                    editor.remove(item.galTitle + "_keyword")
                    editor.remove(item.galTitle + "_lat")
                    editor.remove(item.galTitle + "_lng")
                }
                editor.apply()
                updateFavoriteStatus(ivFavor, ivButton, isFavorite)
                // this@SearchViewHolder 뒤에 sharedViewModel을 추가해야 할 것으로 보입니다.
                // sharedViewModel을 가져와서 사용하는 방법을 참고하여 추가하십시오.
            }
        }

        // 임시로 빈 메서드로 정의
        private fun updateFavoriteStatus(ivFavor: ImageView, ivButton: ImageView, isFavorite: Boolean) {
            ivFavor.setImageResource(if (isFavorite) R.drawable.clickedfavor else R.drawable.favor)
            ivButton.setImageResource(if (isFavorite) R.drawable.btnclickedbackground else R.drawable.btnbackground)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, context)
    }

    override fun getItemCount() = items.size
}