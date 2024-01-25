package com.example.madcamp_week4_fe.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madcamp_week4_fe.databinding.FragmentSearchBinding
import com.example.madcamp_week4_fe.interfaces.LocationApiService
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_week4_fe.SharedViewModel
import com.example.madcamp_week4_fe.home.MarkerBottomSheetFragment
import com.example.madcamp_week4_fe.home.MarkerData
import com.example.madcamp_week4_fe.interfaces.LocationInfoApi
import com.example.madcamp_week4_fe.interfaces.OnItemClickedListener
import com.example.madcamp_week4_fe.models.Item
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling

import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

@Suppress("DEPRECATION")
class SearchFragment : Fragment(), OnItemClickedListener {

    private lateinit var binding: FragmentSearchBinding
    private val searchInfoItems = mutableListOf<MarkerData>()
    private lateinit var searchInfoAdapter: SearchAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupRecyclerView()

        binding.ivSearch.setOnClickListener {
            val keyword = binding.search.text.toString()
            startSearchLoadingActivity()
            searchWithKeyword(keyword)
        }

        binding.viewSearchPicture.setOnClickListener {
            openGalleryForImage()
        }

        sharedViewModel.favoritesUpdated.observe(viewLifecycleOwner) { updated ->
            if (updated) {
                // Code to refresh search results based on updated favorites
                // For example: re-fetch search results or update UI
                sharedViewModel.setFavoritesUpdated(false)
            }
        }

        return binding.root
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            val imageUri = data?.data
            imageUri?.let { analyzeImage(it) }
        }
    }

    private fun analyzeImage(uri: Uri) {
        val image = InputImage.fromFilePath(requireContext(), uri)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                for (label in labels) {
                    val text = label.text
                    val confidence = label.confidence
                    Log.d("MLKit Labels", "Label: $text, Confidence: $confidence")
                    // 키워드를 사용하는 로직 추가
                }
            }
            .addOnFailureListener { e ->
                Log.e("MLKit Labels", "Error processing image", e)
            }
    }

    private val searchLoadingActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("SearchFragment", "SearchLoadingActivity completed.")
            // 필요한 추가 작업 수행
        }
    }

    private fun startSearchLoadingActivity() {
        val intent = Intent(context, SearchLoadingActivity::class.java)
        searchLoadingActivityResultLauncher.launch(intent)
    }


    private fun searchWithKeyword(keyword : String) {
        val randomPageNo = (1..3).random()


        Log.d("searchFragment", "API 호출 시작")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)
                val response = service.getKeywordGalleryList(pageNo = randomPageNo, keyword = keyword)
                    .execute() // 동기 호출
                if (response.isSuccessful) {
                    // 메인 스레드로 전환하여 UI 업데이트
                    Log.d("searchFragment", "API 호출 성공: ${response.body()}")
                    withContext(Dispatchers.Main) {
                        response.body()?.response?.body?.items?.item?.let { items ->
                            updateUI(items)
                            searchLoadingActivityResultLauncher.launch(
                                Intent(context, SearchLoadingActivity::class.java)
                                    .putExtra("close", true)
                            )
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
        searchInfoAdapter = SearchAdapter(searchInfoItems, this, requireContext(), sharedViewModel)
        binding.recyclerView.adapter = searchInfoAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onItemClicked(markerData: MarkerData) {
        // 클릭된 아이템에 해당하는 BottomSheetFragment 표시
        val bottomSheetFragment = MarkerBottomSheetFragment.newInstance(listOf(markerData))
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
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

        return if (!addressList.isNullOrEmpty()) {
            LatLng(addressList[0].latitude, addressList[0].longitude)
        } else {
            LatLng(0.0, 0.0) // 주소를 찾을 수 없는 경우 기본값
        }
    }


    companion object {
        private const val IMAGE_REQUEST_CODE = 123
    }
}