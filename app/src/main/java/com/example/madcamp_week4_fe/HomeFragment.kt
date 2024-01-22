package com.example.madcamp_week4_fe

import android.util.Log
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.madcamp_week4_fe.databinding.FragmentHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import android.location.Geocoder
import com.example.madcamp_week4_fe.interfaces.LocationApiService
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var map: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("HomeFragment", "onCreateView: Starting.")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("HomeFragment", "onCreateView: Starting.")
        map = googleMap
        val southKorea = LatLng(36.0, 128.0) // 남한의 대략적인 중심 좌표
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(southKorea, 7.0f)) // 지도 줌 레벨 설정
        addMarkersToMap()
    }

    private fun addMarkersToMap() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("HomeFragment", "onCreateView: Starting.")
            val context = context ?: return@launch
            val geocoder = Geocoder(context)
            val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)
            val response = service.getGalleryList().execute()
            response.body()?.response?.body?.items?.item?.forEach { galleryItem ->
                val location = geocoder.getFromLocationName(galleryItem.galPhotographyLocation, 1)
                location?.firstOrNull()?.let {
                    val markerOptions = MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(galleryItem.galTitle)
                    withContext(Dispatchers.Main) {
                        map.addMarker(markerOptions)
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        Log.d("HomeFragment", "onResume: Fragment resumed.")
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
        Log.d("HomeFragment", "onPause: Fragment paused.")
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
        _binding = null
        Log.d("HomeFragment", "onDestroy: Fragment destroyed.")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
        Log.d("HomeFragment", "onLowMemory: Low memory warning.")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
        Log.d("HomeFragment", "onSaveInstanceState: Saving instance state.")
    }
}