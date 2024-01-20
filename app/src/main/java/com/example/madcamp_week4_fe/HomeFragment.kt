package com.example.madcamp_week4_fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madcamp_week4_fe.databinding.FragmentHomeBinding
import com.example.madcamp_week4_fe.interfaces.LocationApiService
import com.example.madcamp_week4_fe.models.GalleryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val locationInfoItems = mutableListOf<LocationInfoItem>()
    private lateinit var locationInfoAdapter: LocationInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupRecyclerView()
        loadGalleryData()

        return binding.root
    }

    private fun setupRecyclerView() {
        locationInfoAdapter = LocationInfoAdapter(locationInfoItems)
        binding.locationInfoRecycler.adapter = locationInfoAdapter
        binding.locationInfoRecycler.layoutManager = LinearLayoutManager(context)
    }

    private fun loadGalleryData() {
        val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)

        Log.d("HomeFragment", "API 호출 시작")

        service.getGalleryList().enqueue(object : Callback<GalleryResponse> {
            override fun onResponse(
                call: Call<GalleryResponse>,
                response: Response<GalleryResponse>
            ) {
                Log.d("HomeFragment", "API 응답 수신: ${response.isSuccessful}")

                if (response.isSuccessful) {
                    val items = response.body()?.response?.body?.items?.item
                    items?.let {
                        locationInfoItems.clear()
                        locationInfoItems.addAll(it.map { galleryItem ->
                            LocationInfoItem(
                                galleryItem.galWebImageUrl,
                                galleryItem.galTitle,
                                galleryItem.galPhotographyLocation,
                                galleryItem.galSearchKeyword
                            )
                        })
                        locationInfoAdapter.notifyDataSetChanged()
                    } ?: Log.d("HomeFragment", "응답 항목이 null입니다")
                } else {
                    Log.d("HomeFragment", "API 응답 실패: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GalleryResponse>, t: Throwable) {
                Log.d("HomeFragment", "API 호출 실패: ${t.message}")
            }
        })
    }
}