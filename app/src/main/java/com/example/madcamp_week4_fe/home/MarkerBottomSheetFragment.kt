package com.example.madcamp_week4_fe.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.madcamp_week4_fe.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MarkerBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var markerDataList: List<MarkerData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_marker_bottom_sheet, container, false)
        val viewPager: ViewPager = view.findViewById(R.id.viewPager)

        viewPager.adapter = MarkerPagerAdapter(childFragmentManager, markerDataList)

        return view
    }

    companion object {
        fun newInstance(markerDataList: List<MarkerData>): MarkerBottomSheetFragment {
            val fragment = MarkerBottomSheetFragment()
            fragment.markerDataList = markerDataList
            return fragment
        }
    }
}
