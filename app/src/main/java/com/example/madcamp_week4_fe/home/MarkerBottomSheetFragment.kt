package com.example.madcamp_week4_fe.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.madcamp_week4_fe.MainActivity
import com.example.madcamp_week4_fe.R
import com.example.madcamp_week4_fe.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MarkerBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var markerDataList: List<MarkerData>
    private var sharedViewModel: SharedViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            markerDataList = it.getParcelableArrayList<MarkerData>("markerDataList") ?: listOf()
        }

        sharedViewModel?.favoritesUpdated?.observe(this) { updated ->
            if (updated) {
                // Code to update the UI or perform actions based on updated favorites
                // This might include refreshing the data in MarkerPagerAdapter
                sharedViewModel?.setFavoritesUpdated(false)
            }
        }

        // MainActivity에서 전달된 sharedViewModel 받기
        sharedViewModel = (activity as? MainActivity)?.sharedViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_marker_bottom_sheet, container, false)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)

        // sharedViewModel을 MarkerPagerAdapter에 전달
        viewPager.adapter = sharedViewModel?.let { MarkerPagerAdapter(markerDataList, it) }

        return view
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            bottomSheet.setBackgroundResource(R.drawable.rounded_corners) // Applying the rounded corners background
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            val layoutParams = bottomSheet.layoutParams
            layoutParams.height = getBottomSheetDialogDefaultHeight()
            bottomSheet.layoutParams = layoutParams
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return (resources.displayMetrics.heightPixels * 0.85).toInt()
    }

    companion object {
        fun newInstance(markerDataList: List<MarkerData>): MarkerBottomSheetFragment {
            val fragment = MarkerBottomSheetFragment()
            val args = Bundle()
            args.putParcelableArrayList("markerDataList", ArrayList(markerDataList))
            fragment.arguments = args
            return fragment
        }
    }

}
