package com.example.madcamp_week4_fe.analysis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week4_fe.R
import com.example.madcamp_week4_fe.home.MarkerData

class FavorAdapter(private val favorites: List<MarkerData>) : RecyclerView.Adapter<FavorAdapter.FavorViewHolder>() {

    class FavorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(markerData: MarkerData) {
            itemView.findViewById<TextView>(R.id.tvName).text = markerData.galTitle
            itemView.findViewById<TextView>(R.id.tvLocation).text = markerData.galLocation
            // Add other UI bindings as needed
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
}