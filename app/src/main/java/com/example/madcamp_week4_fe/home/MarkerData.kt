package com.example.madcamp_week4_fe.home

import com.google.android.gms.maps.model.LatLng

data class MarkerData(
    val position: LatLng,
    val galTitle: String,
//    val galUrl: String,
//    val galLocation: String,
//    val galKeyword: String,
    // 기타 필요한 데이터
)