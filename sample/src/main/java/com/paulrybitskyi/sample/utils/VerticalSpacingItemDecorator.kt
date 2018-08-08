package com.paulrybitskyi.sample.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpacingItemDecorator(
    private val mVerticalSpacing: Int,
    private val mVerticalSpacingCompensation: Int
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position != parent.adapter!!.itemCount - 1) {
            outRect.bottom = mVerticalSpacing - mVerticalSpacingCompensation
        } else {
            outRect.bottom = mVerticalSpacing
        }
    }


}
