package com.example.madcamp_week4_fe.home


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcamp_week4_fe.LocationInfoApi
import com.example.madcamp_week4_fe.R
import com.example.madcamp_week4_fe.interfaces.LocationApiService
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    val isDataLoaded = MutableLiveData<Boolean>()

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        drawable?.let {
            val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            it.setBounds(0, 0, canvas.width, canvas.height)
            it.draw(canvas)
            return bitmap
        }
        return null
    }

    fun addMarkersToMap(context: Context, map: GoogleMap, geocoder: Geocoder) {
        val randomPageNo = (1..100).random()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)
                val response = service.getGalleryList(pageNo = randomPageNo).execute()

                val markerJobs = response.body()?.response?.body?.items?.item?.map { galleryItem ->
                    launch {
                        val location = geocoder.getFromLocationName(galleryItem.galPhotographyLocation, 1)
                        location?.firstOrNull()?.let {
                            val bitmap = getBitmapFromVectorDrawable(context, R.drawable.marker)
                            val markerOptions = MarkerOptions()
                                .position(LatLng(it.latitude, it.longitude))
                                .title(galleryItem.galTitle)

                            if (bitmap != null) {
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            }

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

    fun addMoreMarkersToMap(context: Context, map: GoogleMap, geocoder: Geocoder) {
        val randomPageNo = (1..100).random()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val service = LocationInfoApi.getInstance().create(LocationApiService::class.java)
                val response = service.getGalleryList(pageNo = randomPageNo).execute()

                val markerJobs = response.body()?.response?.body?.items?.item?.map { galleryItem ->
                    launch {
                        val location = geocoder.getFromLocationName(galleryItem.galPhotographyLocation, 1)
                        location?.firstOrNull()?.let {
                            val bitmap = getBitmapFromVectorDrawable(context, R.drawable.marker)
                            val markerOptions = MarkerOptions()
                                .position(LatLng(it.latitude, it.longitude))
                                .title(galleryItem.galTitle)

                            if (bitmap != null) {
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            }

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
