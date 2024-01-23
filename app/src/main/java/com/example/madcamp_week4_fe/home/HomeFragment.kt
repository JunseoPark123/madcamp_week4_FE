package com.example.madcamp_week4_fe.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.madcamp_week4_fe.databinding.FragmentHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp_week4_fe.home.MapResetLoadingActivity


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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("HomeFragment", "onCreateView: Starting.")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        startLoadingActivity()
        return binding.root
    }
    private fun startLoadingActivity() {
        val intent = Intent(context, MapResetLoadingActivity::class.java)
        loadingActivityResultLauncher.launch(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("HomeFragment", "onMapReady: Starting.")
        map = googleMap
        val southKorea = LatLng(36.0, 128.0)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(southKorea, 7.0f))
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true

        context?.let {
            viewModel.addMarkersToMap(map, Geocoder(it))
        }
    }
    private fun onDataLoaded() {
        // 로딩 화면 종료
        loadingActivityResultLauncher.launch(Intent(context, MapResetLoadingActivity::class.java))
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