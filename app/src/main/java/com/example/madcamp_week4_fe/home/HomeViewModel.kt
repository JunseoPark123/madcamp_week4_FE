package com.example.madcamp_week4_fe.home


import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcamp_week4_fe.LocationInfoApi
import com.example.madcamp_week4_fe.interfaces.LocationApiService
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    fun addMarkersToMap(map: GoogleMap, geocoder: Geocoder) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
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
            } catch (e: Exception) {
                // 오류 처리
            }
        }
    }
}