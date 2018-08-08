package com.paulrybitskyi.sample.utils

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan

/**
 * A custom clickable span with support for background coloring
 * when clicked.
 */
abstract class SelectorSpan(
    private val mTextColor: Int,
    private val mBackgroundSelectedStateColor: Int
) : ClickableSpan() {


    var isSelected: Boolean = false




    override fun updateDrawState(ds: TextPaint) {
        ds.color = mTextColor
        ds.bgColor = if (isSelected) mBackgroundSelectedStateColor else Color.TRANSPARENT
        ds.isUnderlineText = false
    }


}
