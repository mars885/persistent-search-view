/*
 * Copyright 2017 Paul Rybitskyi, oss@paulrybitskyi.com
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

import android.animation.Animator
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.paulrybitskyi.commons.ktx.makeGone
import com.paulrybitskyi.commons.ktx.makeVisible
import com.paulrybitskyi.persistentsearchview.listeners.AnimatorListenerAdapter
import com.paulrybitskyi.persistentsearchview.sample.utils.extensions.*

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