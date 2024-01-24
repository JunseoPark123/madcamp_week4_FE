package com.example.madcamp_week4_fe.search

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madcamp_week4_fe.databinding.FragmentSearchBinding
import com.example.madcamp_week4_fe.interfaces.LocationApiService
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week4_fe.home.MarkerData
import com.example.madcamp_week4_fe.interfaces.LocationInfoApi
import com.example.madcamp_week4_fe.models.Item
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchInfoItems = mutableListOf<MarkerData>()
    private lateinit var searchInfoAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupRecyclerView()

        loadGalleryData()

        return binding.root
    }


    private fun loadGalleryData() {
        val randomPageNo = (1..3).random()

        Log.d("searchFragment", "API 호출 시작")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)
                val response = service.getKeywordGalleryList(pageNo = randomPageNo, keyword = "서울")
                    .execute() // 동기 호출
                if (response.isSuccessful) {
                    // 메인 스레드로 전환하여 UI 업데이트

                    Log.d("searchFragment", "API 호출 성공: ${response.body()}")
                    withContext(Dispatchers.Main) {
                        response.body()?.response?.body?.items?.item?.let { items ->
                            updateUI(items)
                        }
                    }
                } else {
                    Log.d("searchFragment", "API 응답 실패: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("searchFragment", "API 호출 실패: ${e.message}")
            }
        }
    }

    private fun setupRecyclerView() {
        searchInfoAdapter = SearchAdapter(searchInfoItems, requireContext())
        binding.recyclerView.adapter = searchInfoAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun updateUI(items: List<Item>) {
        val markerDataList = items.map { item ->
            val position = getLocationFromAddress(requireContext(), item.galPhotographyLocation)
            MarkerData(
                position = position,
                galTitle = item.galTitle,
                galUrl = item.galWebImageUrl,
                galLocation = item.galPhotographyLocation,
                galKeyword = item.galSearchKeyword
            )
        }

        searchInfoItems.clear()
        searchInfoItems.addAll(markerDataList)
        searchInfoAdapter.notifyDataSetChanged()
    }

    private fun getLocationFromAddress(context: Context, address: String): LatLng {
        val geocoder = Geocoder(context)
        val addressList = try {
            geocoder.getFromLocationName(address, 1)
        } catch (e: IOException) {
            // Geocoder가 실패하거나 예외를 던진 경우
            return LatLng(0.0, 0.0)
        }

        return if (addressList != null && addressList.isNotEmpty()) {
            LatLng(addressList[0].latitude, addressList[0].longitude)
        } else {
            LatLng(0.0, 0.0) // 주소를 찾을 수 없는 경우 기본값
        }
    }
}