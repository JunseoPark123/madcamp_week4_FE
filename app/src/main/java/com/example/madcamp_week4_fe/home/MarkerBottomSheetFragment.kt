package com.example.madcamp_week4_fe.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.madcamp_week4_fe.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MarkerBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var markerDataList: List<MarkerData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_marker_bottom_sheet, container, false)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        viewPager.adapter = MarkerPagerAdapter(markerDataList)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            markerDataList = it.getParcelableArrayList<MarkerData>("markerDataList") ?: listOf()
        }
    }
}
