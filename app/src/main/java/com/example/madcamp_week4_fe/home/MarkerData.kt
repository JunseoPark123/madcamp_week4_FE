package com.example.madcamp_week4_fe.home

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarkerData(
    val position: LatLng,
    val galTitle: String,
    val galUrl: String,
    val galLocation: String,
    val galKeyword: String,
    // 기타 필요한 데이터
) : Parcelable