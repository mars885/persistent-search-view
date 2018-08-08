package com.paulrybitskyi.sample.utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.sample.utils.extensions.dpToPx

abstract class HeaderedRecyclerViewListener(context: Context) : RecyclerView.OnScrollListener() {


    companion object {

        private const val SWIPE_DETECTION_DISTANCE = 10

    }


    private var mScrollDetectionDistance: Int = context.dpToPx(SWIPE_DETECTION_DISTANCE)

    private var mFirstVisiblePosition: Int = 0
    private var mPreviousFirstVisiblePosition: Int = 0




    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if(dy > 0) {
            onScrolledDownwards(recyclerView, dy)
        } else if(dy < 0) {
            onScrolledUpwards(recyclerView, dy)
        }
    }


    private fun onScrolledUpwards(recyclerView: RecyclerView, deltaY: Int) {
        mFirstVisiblePosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0))
        val isFirstItem = (mFirstVisiblePosition == 0)

        if((Math.abs(deltaY) >= mScrollDetectionDistance) || (isFirstItem && (mFirstVisiblePosition != mPreviousFirstVisiblePosition))) {
            showHeader()
        }

        mPreviousFirstVisiblePosition = mFirstVisiblePosition
    }


    private fun onScrolledDownwards(recyclerView: RecyclerView, deltaY: Int) {
        mFirstVisiblePosition = recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0))

        if((mFirstVisiblePosition > 0)
            && ((Math.abs(deltaY) >= mScrollDetectionDistance) || (mFirstVisiblePosition > mPreviousFirstVisiblePosition))) {
            hideHeader()
        }

        mPreviousFirstVisiblePosition = mFirstVisiblePosition
    }


    open fun showHeader() {

    }


    open fun hideHeader() {

    }


}