package com.paulrybitskyi.sample.utils.extensions

import android.view.View
import androidx.core.view.isVisible
import com.paulrybitskyi.sample.R


internal fun View.setVisibilityMarker(isVisible: Boolean) {
    setTag(R.id.visibility_marker, isVisible)
}


internal fun View.getVisibilityMarker(): Boolean {
    return ((getTag(R.id.visibility_marker) as? Boolean) ?: isVisible)
}


internal fun View.setAnimationMarker(marker: Any) {
    setTag(R.id.animation_marker, marker)
}


@Suppress("UNCHECKED_CAST")
internal fun <T> View.getAnimationMarker(): T? {
    return (getTag(R.id.animation_marker) as T?)
}


internal fun View.cancelAllAnimations() {
    this.clearAnimation()
    this.animate().cancel()
}