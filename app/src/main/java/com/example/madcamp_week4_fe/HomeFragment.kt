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
import com.example.madcamp_week4_fe.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        Log.d("HomeFragment", "onCreateView: RecyclerView setup initiated.")
        setupRecyclerView()
        Log.d("HomeFragment", "onCreateView: Loading gallery data.")
        loadGalleryData()

        return binding.root
    }

    private fun setupRecyclerView() {
        locationInfoAdapter = LocationInfoAdapter(locationInfoItems, requireContext())
        binding.locationInfoRecycler.adapter = locationInfoAdapter
        binding.locationInfoRecycler.layoutManager = LinearLayoutManager(context)
        Log.d("HomeFragment", "RecyclerView setup completed.")
    }

    private fun loadGalleryData() {
        val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)

        Log.d("HomeFragment", "API 호출 시작")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = service.getGalleryList().execute() // 동기 호출
                if (response.isSuccessful) {
                    // 메인 스레드로 전환하여 UI 업데이트
                    withContext(Dispatchers.Main) {
                        response.body()?.response?.body?.items?.item?.let { items ->
                            updateUI(items)
                        }
                    }
                } else {
                    Log.d("HomeFragment", "API 응답 실패: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.d("HomeFragment", "API 호출 실패: ${e.message}")
            }
        }
    }

    private fun updateUI(items: List<Item>) {
        locationInfoItems.clear()
        locationInfoItems.addAll(items.map { galleryItem ->
            LocationInfoItem(
                galleryItem.galWebImageUrl,
                galleryItem.galTitle,
                galleryItem.galPhotographyLocation,
                galleryItem.galSearchKeyword
            )
        })
        locationInfoAdapter.notifyDataSetChanged()
    }
}
