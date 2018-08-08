package com.paulrybitskyi.sample.utils

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView

/**
 * A custom link movement method to enable selecting text.
 */
class CustomLinkMovementMethod : LinkMovementMethod() {


    private var mSelectedSpan: SelectorSpan? = null




    override fun onTouchEvent(textView: TextView, spannable: Spannable, event: MotionEvent): Boolean {
        val action = event.action

        if (action == MotionEvent.ACTION_DOWN) {
            mSelectedSpan = getSelectedSpan(textView, spannable, event)

            if (mSelectedSpan != null) {
                mSelectedSpan!!.isSelected = true

                // Selecting the pressed span
                Selection.setSelection(
                    spannable,
                    spannable.getSpanStart(mSelectedSpan),
                    spannable.getSpanEnd(mSelectedSpan)
                )
            }
        } else if (action == MotionEvent.ACTION_MOVE) {
            val selectedSpan = getSelectedSpan(textView, spannable, event)

            // Removing the selection if the finger is moved outside the currently selected span
            if ((mSelectedSpan != null) && (selectedSpan !== mSelectedSpan)) {
                mSelectedSpan!!.isSelected = false
                mSelectedSpan = null

                // Removing the selection from the spannable
                Selection.removeSelection(spannable)
            }
        } else {
            if (mSelectedSpan != null) {
                mSelectedSpan!!.isSelected = false
                mSelectedSpan = null

                // Allowing the necessary spans to handle click event
                super.onTouchEvent(textView, spannable, event)
            }

            // Removing the selection from the spannable
            Selection.removeSelection(spannable)
        }

        return true
    }


    private fun getSelectedSpan(textView: TextView, spannable: Spannable, event: MotionEvent): SelectorSpan? {
        // Fetching the coordinates of the event
        var x = event.x.toInt()
        var y = event.y.toInt()

        // Adjusting the coordinates
        x -= textView.totalPaddingLeft
        x += textView.scrollX

        y -= textView.totalPaddingTop
        y += textView.scrollY

        // Obtaining the text layout from the text view
        val layout = textView.layout

        // Fetching the line index and the horizontal offset
        val line = layout.getLineForVertical(y)
        val offset = layout.getOffsetForHorizontal(line, x.toFloat())

        // Fetching all the SelectorSpans that are available in the specified range
        val spans = spannable.getSpans(offset, offset, SelectorSpan::class.java)

        var selectedSpan: SelectorSpan? = null

        // Retrieving the selected span (if there is one)
        if (spans.isNotEmpty()) {
            selectedSpan = spans[0]
        }

        return selectedSpan
    }


}
