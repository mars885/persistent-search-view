package com.paulrybitskyi.sample.utils.extensions

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import com.paulrybitskyi.sample.R


fun View.setVisibilityMarker(isVisible: Boolean) {
    setTag(R.id.visibility_marker, isVisible)
}


fun View.getVisibilityMarker(): Boolean {
    return (getTag(R.id.visibility_marker) as Boolean?)
        .let { it }
        ?: (visibility == View.VISIBLE)
}


fun View.setAnimationMarker(marker: Any) {
    setTag(R.id.animation_marker, marker)
}


fun <T> View.getAnimationMarker(): T? {
    return (getTag(R.id.animation_marker) as T?)
}


fun View.cancelAllAnimations() {
    this.clearAnimation()
    this.animate().cancel()
}


fun View.makeVisible() {
    this.visibility = View.VISIBLE
}


fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}


fun View.makeGone() {
    this.visibility = View.GONE
}


fun View.setBackgroundDrawableCompat(drawable: Drawable?) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        background = drawable
    } else {
        setBackgroundDrawable(drawable)
    }
}