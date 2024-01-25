package com.example.madcamp_week4_fe.analysis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week4_fe.R
import com.example.madcamp_week4_fe.SharedViewModel
import com.example.madcamp_week4_fe.home.MarkerData
import com.example.madcamp_week4_fe.interfaces.OnItemClickedListener

class FavorAdapter(private var favorites: List<MarkerData>,
                   private val sharedViewModel: SharedViewModel,
                   private val listener: OnItemClickedListener
) : RecyclerView.Adapter<FavorAdapter.FavorViewHolder>() {
    inner class FavorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(markerData: MarkerData) {
            itemView.findViewById<TextView>(R.id.tvName).text = markerData.galTitle
            itemView.findViewById<TextView>(R.id.tvLocation).text = markerData.galLocation

            val ivFavor = itemView.findViewById<ImageView>(R.id.ivFavor)
            val ivButton = itemView.findViewById<ImageView>(R.id.ivBtnBackground)
            val sharedPreferences = itemView.context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)

            var isFavorite = sharedPreferences.getBoolean(markerData.galTitle, false)
            updateFavoriteStatus(ivFavor, ivButton, isFavorite)

            ivButton.setOnClickListener {
                if (isFavorite) {
                    showUnfavoriteConfirmationDialog(markerData, ivFavor, ivButton)
                } else {
                    toggleFavoriteStatus(markerData, ivFavor, ivButton, true)
                }
            }

            itemView.setOnClickListener {
                listener.onItemClicked(markerData)
            }
        }

        private fun showUnfavoriteConfirmationDialog(markerData: MarkerData, ivFavor: ImageView, ivButton: ImageView) {
            AlertDialog.Builder(itemView.context)
                .setTitle("모아보기 해제")
                .setMessage("이 항목을 모아보기에서 해제하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    toggleFavoriteStatus(markerData, ivFavor, ivButton, false)
                }
                .setNegativeButton("아니오", null)
                .show()
        }

        private fun toggleFavoriteStatus(markerData: MarkerData, ivFavor: ImageView, ivButton: ImageView, isFavorite: Boolean) {
            val sharedPreferences = itemView.context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

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
            sharedViewModel.setFavoritesUpdated(true)

        }

        private fun updateFavoriteStatus(ivFavor: ImageView, ivButton: ImageView, isFavorite: Boolean) {
            ivFavor.setImageResource(if (isFavorite) R.drawable.clickedfavor else R.drawable.favor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favor, parent, false)
        return FavorViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavorViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount() = favorites.size

    fun updateData(newFavorites: List<MarkerData>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }
}