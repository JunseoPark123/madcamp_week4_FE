package com.example.madcamp_week4_fe.home

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp_week4_fe.R
import com.example.madcamp_week4_fe.databinding.FragmentHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions


class HomeFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap
    private lateinit var viewModel: HomeViewModel

    private val loadingActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("HomeFragment", "LoadingActivity completed.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.isDataLoaded.observe(this) { isLoaded ->
            if (isLoaded) {
                Log.d("HomeFragment", "Data Loaded: Closing Loading Activity")
                // Close the LoadingActivity
                requireActivity().runOnUiThread {
                    loadingActivityResultLauncher.launch(
                        Intent(
                            context,
                            MapResetLoadingActivity::class.java
                        ).putExtra("close", true)
                    )
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("HomeFragment", "onCreateView: Starting.")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnReset.setOnClickListener {
            startLoadingActivity()
            map.clear() // Clear existing markers
            context?.let { nonNullContext ->
                viewModel.addMarkersToMap(nonNullContext, map, Geocoder(nonNullContext))
            }
        }

        binding.btnMore.setOnClickListener {
            context?.let { nonNullContext ->
                startAddLoadingActivity()
                viewModel.addMarkersToMap(nonNullContext, map, Geocoder(nonNullContext))
            }
        }

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        startLoadingActivity()
        return binding.root
    }

    private fun startAddLoadingActivity() {
        val intent = Intent(context, MapAddLoadingActivity::class.java)
        loadingActivityResultLauncher.launch(intent)
    }

    private fun startLoadingActivity() {
        val intent = Intent(context, MapResetLoadingActivity::class.java)
        loadingActivityResultLauncher.launch(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("HomeFragment", "onMapReady: Starting.")
        map = googleMap
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.style_json // 'style_json'은 당신의 JSON 파일 이름입니다.
                )
            )
            if (!success) {
                Log.e("MapsActivity", "스타일 파싱 실패")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MapsActivity", "스타일 리소스를 찾을 수 없음", e)
        }

        val southKorea = LatLng(36.0, 128.0)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(southKorea, 7.0f))
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true

        context?.let { nonNullContext ->
            viewModel.addMarkersToMap(nonNullContext, map, Geocoder(nonNullContext))
        }
    }

    override fun onResume() {
        // 프래그먼트가 사용자에게 보이게 되었을 때 호출,
        // binding.mapView.onResume() 호출을 통해 지도 뷰가 다시 시작될 때 필요한 작업
        super.onResume()
        binding.mapView.onResume()
        Log.d("HomeFragment", "onResume: Fragment resumed.")
    }

    override fun onPause() {
        // 사용자와 상호작용이 중지되었을 때 호출
        super.onPause()
        binding.mapView.onPause()
        Log.d("HomeFragment", "onPause: Fragment paused.")
    }

    override fun onDestroy() {
        // 프래그먼트가 파괴될 때 호출
        super.onDestroy()
        binding.mapView.onDestroy()
        _binding = null
        Log.d("HomeFragment", "onDestroy: Fragment destroyed.")
    }

    override fun onLowMemory() {
        // 시스템이 낮은 메모리 상태에 있을 때 호출
        super.onLowMemory()
        binding.mapView.onLowMemory()
        Log.d("HomeFragment", "onLowMemory: Low memory warning.")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // 프래그먼트의 상태를 저장해야 할 때
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
        Log.d("HomeFragment", "onSaveInstanceState: Saving instance state.")
    }
}