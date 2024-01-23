package com.example.madcamp_week4_fe.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madcamp_week4_fe.databinding.FragmentSearchBinding
import com.example.madcamp_week4_fe.interfaces.LocationApiService
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week4_fe.search.SearchInfoAdapter
import com.example.madcamp_week4_fe.LocationInfoApi
import com.example.madcamp_week4_fe.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchInfoItems = mutableListOf<SearchInfoItem>()
    private lateinit var searchInfoAdapter: SearchInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
//        Log.d("searchFragment", "onCreateView: RecyclerView setup initiated.")
//        setupRecyclerView()
//        Log.d("searchFragment", "onCreateView: Loading gallery data.")
//        loadGalleryData()

        return binding.root
    }

//    private fun setupRecyclerView() {
//        searchInfoAdapter = SearchInfoAdapter(searchInfoItems, requireContext())
//        binding.locationInfoRecycler.adapter = searchInfoAdapter
//        binding.locationInfoRecycler.layoutManager = LinearLayoutManager(context)
//        Log.d("searchFragment", "RecyclerView setup completed.")
//    }

    private fun loadGalleryData() {
        val randomPageNo = (1..100).random()

        Log.d("searchFragment", "API 호출 시작")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)
                val response = service.getGalleryList(pageNo = randomPageNo).execute() // 동기 호출
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

    private fun updateUI(items: List<Item>) {
        searchInfoItems.clear()
        searchInfoItems.addAll(items.map { galleryItem ->
            SearchInfoItem(
                galleryItem.galWebImageUrl,
                galleryItem.galTitle,
                galleryItem.galPhotographyLocation,
                galleryItem.galSearchKeyword
            )
        })
        searchInfoAdapter.notifyDataSetChanged()
    }
}
