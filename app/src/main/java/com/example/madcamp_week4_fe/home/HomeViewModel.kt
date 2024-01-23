package com.example.madcamp_week4_fe.home


import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    val isDataLoaded = MutableLiveData<Boolean>()
    fun addMarkersToMap(map: GoogleMap, geocoder: Geocoder) {
        val randomPageNo = (1..100).random()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)
                val response = service.getGalleryList(pageNo = randomPageNo).execute()

                val markerJobs = response.body()?.response?.body?.items?.item?.map { galleryItem ->
                    launch {
                        val location = geocoder.getFromLocationName(galleryItem.galPhotographyLocation, 1)
                        location?.firstOrNull()?.let {
                            val markerOptions = MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(galleryItem.galTitle)
                            withContext(Dispatchers.Main) {
                                map.addMarker(markerOptions)
                            }
                        }
                    }
                }
                // Wait for all marker addition jobs to complete
                markerJobs?.forEach { it.join() }

                // Now all markers are added, post that data is loaded
                isDataLoaded.postValue(true)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading markers: ${e.message}")
                isDataLoaded.postValue(false)
            }
        }
    }

    fun addMoreMarkersToMap(map: GoogleMap, geocoder: Geocoder) {
        val randomPageNo = (1..100).random()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)
                val response = service.getGalleryList(pageNo = randomPageNo).execute()

                val markerJobs = response.body()?.response?.body?.items?.item?.map { galleryItem ->
                    launch {
                        val location = geocoder.getFromLocationName(galleryItem.galPhotographyLocation, 1)
                        location?.firstOrNull()?.let {
                            val markerOptions = MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(galleryItem.galTitle)
                            withContext(Dispatchers.Main) {
                                map.addMarker(markerOptions)
                            }
                        }
                    }
                }
                // Wait for all marker addition jobs to complete
                markerJobs?.forEach { it.join() }

                // Now all markers are added, post that data is loaded
                isDataLoaded.postValue(true)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading markers: ${e.message}")
                isDataLoaded.postValue(false)
            }
        }
    }

}
