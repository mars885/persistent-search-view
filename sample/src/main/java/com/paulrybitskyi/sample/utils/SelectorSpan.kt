package com.paulrybitskyi.sample.utils

import android.text.TextPaint
import android.text.style.ClickableSpan

/**
 * A custom clickable span with support for background coloring
 * when clicked.
 */
internal abstract class SelectorSpan(private val textColor: Int) : ClickableSpan() {


    var isSelected = false


    override fun updateDrawState(drawState: TextPaint) = with(drawState) {
        color = textColor
        isUnderlineText = false
    }


}
