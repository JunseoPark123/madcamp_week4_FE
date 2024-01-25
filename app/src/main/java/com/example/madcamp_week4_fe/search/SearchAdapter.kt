package com.example.madcamp_week4_fe.search

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week4_fe.R
import com.example.madcamp_week4_fe.SharedViewModel
import com.example.madcamp_week4_fe.databinding.ItemSearchBinding
import com.example.madcamp_week4_fe.home.MarkerData
import com.example.madcamp_week4_fe.interfaces.OnItemClickedListener


class SearchAdapter(
    private val items: MutableList<MarkerData>,
    private val listener: OnItemClickedListener,
    private val context: Context,
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MarkerData, listener: OnItemClickedListener, context: Context, sharedViewModel: SharedViewModel) {
            binding.tvName.text = item.galTitle
            binding.tvLocation.text = item.galLocation

            val ivFavor = itemView.findViewById<ImageView>(R.id.ivFavor)
            val ivButton = itemView.findViewById<ImageView>(R.id.ivBtnBackground)
            val sharedPreferences = itemView.context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)

            // 선호 상태 확인 및 UI 업데이트
            var isFavorite = sharedPreferences.getBoolean(item.galTitle, false)
            updateFavoriteStatus(ivFavor, ivButton, isFavorite)

            // 클릭 리스너 설정
            itemView.setOnClickListener {
                listener.onItemClicked(item)
            }

            ivButton.setOnClickListener {
                isFavorite = !isFavorite // 선호 상태 토글
                updateFavoriteStatusInSharedPreferences(sharedPreferences, item, isFavorite)
                updateFavoriteStatus(ivFavor, ivButton, isFavorite)
                sharedViewModel.setFavoritesUpdated(true)
            }
        }

        private fun updateFavoriteStatusInSharedPreferences(sharedPreferences: SharedPreferences, item: MarkerData, isFavorite: Boolean) {
            val editor = sharedPreferences.edit()
            if (isFavorite) {
                // 선호로 설정
                editor.putBoolean(item.galTitle, true)
                editor.putString(item.galTitle + "_url", item.galUrl)
                editor.putString(item.galTitle + "_location", item.galLocation)
                editor.putString(item.galTitle + "_keyword", item.galKeyword)
                editor.putFloat(item.galTitle + "_lat", item.position.latitude.toFloat())
                editor.putFloat(item.galTitle + "_lng", item.position.longitude.toFloat())
                editor.apply()
            } else {
                // 선호 해제
                editor.remove(item.galTitle)
                editor.remove(item.galTitle + "_url")
                editor.remove(item.galTitle + "_location")
                editor.remove(item.galTitle + "_keyword")
                editor.remove(item.galTitle + "_lat")
                editor.remove(item.galTitle + "_lng")
                editor.apply()
            }
        }

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
        holder.bind(item, listener, context, sharedViewModel)
    }

    override fun getItemCount() = items.size
}