package com.paulrybitskyi.sample.utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.ktx.dpToPx
import kotlin.math.abs

internal abstract class HeaderedRecyclerViewListener(context: Context) : RecyclerView.OnScrollListener() {


    companion object {

        private const val SWIPE_DETECTION_DISTANCE_IN_DP = 10

    }


    private var scrollDetectionDistance = SWIPE_DETECTION_DISTANCE_IN_DP.dpToPx(context)

    private var firstVisiblePosition = 0
    private var previousFirstVisiblePosition = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if(dy > 0) {
            onScrolledDownwards(recyclerView, dy)
        } else if(dy < 0) {
            onScrolledUpwards(recyclerView, dy)
        }
    }


    private fun onScrolledUpwards(recyclerView: RecyclerView, deltaY: Int) {
        firstVisiblePosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0))
        val isFirstItem = (firstVisiblePosition == 0)

        if(shouldShowHeader(deltaY, isFirstItem)) {
            showHeader()
        }

        previousFirstVisiblePosition = firstVisiblePosition
    }


    private fun shouldShowHeader(deltaY: Int, isFirstItem: Boolean): Boolean {
        return (
            (abs(deltaY) >= scrollDetectionDistance) ||
            (isFirstItem && (firstVisiblePosition != previousFirstVisiblePosition))
        )
    }


    private fun onScrolledDownwards(recyclerView: RecyclerView, deltaY: Int) {
        firstVisiblePosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0))

        if(shouldHideHeader(deltaY)) {
            hideHeader()
        }

        previousFirstVisiblePosition = firstVisiblePosition
    }


    private fun shouldHideHeader(deltaY: Int): Boolean {
        return (
            (firstVisiblePosition > 0) &&
            ((abs(deltaY) >= scrollDetectionDistance) || (firstVisiblePosition > previousFirstVisiblePosition))
        )
    }


    open fun showHeader() {

    }


    open fun hideHeader() {

    }


}