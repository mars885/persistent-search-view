package com.paulrybitskyi.sample.utils

import android.animation.Animator
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.paulrybitskyi.commons.ktx.makeGone
import com.paulrybitskyi.commons.ktx.makeVisible
import com.paulrybitskyi.persistentsearchview.listeners.AnimatorListenerAdapter
import com.paulrybitskyi.sample.utils.extensions.*

internal object AnimationUtils {


    private val HEADER_ANIMATION_DURATION = 250L
    private val HEADER_ANIMATION_INTERPOLATOR = DecelerateInterpolator()


    fun showHeader(header: View) = with(header) {
        if(getVisibilityMarker() || (AnimationType.ENTER == getAnimationMarker())) {
            return
        }

        cancelAllAnimations()
        makeVisible()
        setVisibilityMarker(true)

        animate()
            .translationY(0f)
            .setListener(object : AnimatorListenerAdapter() {

                override fun onAnimationStarted(animation: Animator?) {
                    setAnimationMarker(AnimationType.ENTER)
                }

                override fun onAnimationEnded(animation: Animator?) {
                    setAnimationMarker(AnimationType.NONE)
                }

            })
            .setInterpolator(HEADER_ANIMATION_INTERPOLATOR)
            .setDuration(HEADER_ANIMATION_DURATION)
            .start()
    }


    fun hideHeader(header: View) = with(header) {
        if(!getVisibilityMarker() || (AnimationType.EXIT == getAnimationMarker())) {
            return
        }

        cancelAllAnimations()
        setVisibilityMarker(false)

        animate()
            .translationY(-measuredHeight.toFloat())
            .setListener(object : AnimatorListenerAdapter() {

                override fun onAnimationStarted(animation: Animator?) {
                    setAnimationMarker(AnimationType.EXIT)
                }

                override fun onAnimationEnded(animation: Animator?) {
                    setAnimationMarker(AnimationType.NONE)
                    makeGone()
                }

            })
            .setInterpolator(HEADER_ANIMATION_INTERPOLATOR)
            .setDuration(HEADER_ANIMATION_DURATION)
            .start()
    }


}