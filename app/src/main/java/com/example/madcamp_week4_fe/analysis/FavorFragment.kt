package com.example.madcamp_week4_fe.analysis

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week4_fe.R
import com.example.madcamp_week4_fe.SharedViewModel
import com.example.madcamp_week4_fe.home.MarkerData
import com.google.android.gms.maps.model.LatLng

class FavorFragment : Fragment() {
    private lateinit var favorAdapter: FavorAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences

    private val sharedPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        refreshFavorites() // 즐겨찾기가 변경될 때 UI 업데이트
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favor, container, false)
        recyclerView = view.findViewById(R.id.favorRecycler)
        sharedPreferences = requireActivity().getSharedPreferences("Favorites", Context.MODE_PRIVATE)

        setupRecyclerView()

        sharedPreferences.registerOnSharedPreferenceChangeListener { _, _ ->
            refreshFavorites()
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        refreshFavorites()
    }

    private fun setupRecyclerView() {
        favorAdapter = FavorAdapter(loadFavorites())
        recyclerView.adapter = favorAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun refreshFavorites() {
        val newFavorites = loadFavorites()
        favorAdapter.updateData(newFavorites)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // 리스너 해제
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
    }


    private fun loadFavorites(): List<MarkerData> {
        val favorites = mutableListOf<MarkerData>()
        sharedPreferences.all.forEach { (key, value) ->
            if (value is Boolean && value) {
                val url = sharedPreferences.getString(key + "_url", "")
                val location = sharedPreferences.getString(key + "_location", "")
                val keyword = sharedPreferences.getString(key + "_keyword", "")
                val lat = sharedPreferences.getFloat(key + "_lat", 0f).toDouble()
                val lng = sharedPreferences.getFloat(key + "_lng", 0f).toDouble()
                favorites.add(MarkerData(LatLng(lat, lng), key, url ?: "", location ?: "", keyword ?: ""))
            }
        }
        return favorites
    }
}