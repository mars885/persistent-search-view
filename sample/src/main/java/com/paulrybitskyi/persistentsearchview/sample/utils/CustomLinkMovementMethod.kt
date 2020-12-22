/*
 * Copyright 2017 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.persistentsearchview.sample.utils

import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.widget.TextView

/**
 * A custom link movement method to enable selecting text.
 */
internal class CustomLinkMovementMethod : LinkMovementMethod() {


    private var selectedSpan: SelectorSpan? = null


    override fun onTouchEvent(textView: TextView, spannable: Spannable, event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> onActionDownEvent(textView, spannable, event)
            MotionEvent.ACTION_MOVE -> onActionMoveEvent(textView, spannable, event)

            else -> onActionOtherEvent(textView, spannable, event)
        }

        return true
    }


    private fun onActionDownEvent(textView: TextView, spannable: Spannable, event: MotionEvent) {
        selectedSpan = getSelectedSpan(textView, spannable, event)
            ?.also { selectPressedSpan(spannable) }
    }


    private fun getSelectedSpan(
        textView: TextView,
        spannable: Spannable,
        event: MotionEvent
    ): SelectorSpan? {
        var x = event.x.toInt()
        var y = event.y.toInt()

        x -= textView.totalPaddingLeft
        x += textView.scrollX
        y -= textView.totalPaddingTop
        y += textView.scrollY

        val textLayout = textView.layout
        val verticalLineIndex = textLayout.getLineForVertical(y)
        val horizontalCharOffset = textLayout.getOffsetForHorizontal(verticalLineIndex, x.toFloat())

        return spannable.findSpans(horizontalCharOffset, horizontalCharOffset)
            .takeIf { it.isNotEmpty() }
            ?.firstOrNull()
    }


    private fun Spannable.findSpans(start: Int, end: Int): Array<SelectorSpan> {
        return getSpans(start, end, SelectorSpan::class.java)
    }


    private fun selectPressedSpan(spannable: Spannable) {
        selectedSpan?.isSelected = true

        Selection.setSelection(
            spannable,
            spannable.getSpanStart(selectedSpan),
            spannable.getSpanEnd(selectedSpan)
        )
    }


    private fun onActionMoveEvent(textView: TextView, spannable: Spannable, event: MotionEvent) {
        val currentSpan = getSelectedSpan(textView, spannable, event)

        if(isFingerMovedOutsideSelectedSpan(currentSpan)) {
            deselectPressedSpan(spannable)
        }
    }


    private fun isFingerMovedOutsideSelectedSpan(currentSpan: SelectorSpan?): Boolean {
        return ((selectedSpan != null) && (currentSpan != selectedSpan))
    }


    private fun deselectPressedSpan(spannable: Spannable) {
        selectedSpan?.isSelected = false
        selectedSpan = null

        Selection.removeSelection(spannable)
    }


    private fun onActionOtherEvent(textView: TextView, spannable: Spannable, event: MotionEvent) {
        if (selectedSpan != null) {
            deselectPressedSpan(spannable)

            super.onTouchEvent(textView, spannable, event)
        }
    }


}
