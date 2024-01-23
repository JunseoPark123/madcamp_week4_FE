package com.example.madcamp_week4_fe.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week4_fe.R

class MarkerPagerAdapter(
    private val markerDataList: List<MarkerData>
) : RecyclerView.Adapter<MarkerPagerAdapter.MarkerViewHolder>() {

    class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)

        fun bind(markerData: MarkerData) {
            textViewTitle.text = markerData.galTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_marker_page, parent, false)
        return MarkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val markerData = markerDataList[position]
        holder.itemView.findViewById<TextView>(R.id.textViewTitle).text = markerData.galTitle
        // 여기에 추가적인 데이터 바인딩 로직 구현
    }

    override fun getItemCount() = markerDataList.size
}