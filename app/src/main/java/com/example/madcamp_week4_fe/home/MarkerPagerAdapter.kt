package com.example.madcamp_week4_fe.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MarkerPagerAdapter(
    fragmentManager: FragmentManager,
    private val markerDataList: List<MarkerData>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = markerDataList.size

    override fun getItem(position: Int): Fragment {
        // 각 마커 데이터에 대한 상세 정보를 표시하는 뷰 반환
        val markerData = markerDataList[position]
        val fragment = Fragment()
        fragment.arguments = Bundle().apply {
            putString("galTitle", markerData.galTitle)
            // 필요한 다른 데이터도 추가 가능
        }
        return fragment
    }
}