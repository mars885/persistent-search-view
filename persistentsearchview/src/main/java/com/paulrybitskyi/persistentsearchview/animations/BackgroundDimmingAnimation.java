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

package com.paulrybitskyi.persistentsearchview.animations;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.paulrybitskyi.persistentsearchview.utils.Preconditions;
import com.paulrybitskyi.persistentsearchview.utils.Utils;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * An animation used for dimming a background of a view by changing
 * its alpha values.
 */
public final class BackgroundDimmingAnimation {


    private boolean isRecycled;

    private int dimColor;

    private float fromAlpha;
    private float toAlpha;

    private View view;

    private ValueAnimator animator;


    public BackgroundDimmingAnimation(
        @NonNull View view,
        @ColorInt int dimColor,
        float fromAlpha,
        float toAlpha
    ) {
        this.view = view;
        this.dimColor = dimColor;
        this.fromAlpha = fromAlpha;
        this.toAlpha = toAlpha;
        this.isRecycled = false;

        initAnimator();
    }


    private void initAnimator() {
        animator = ValueAnimator.ofFloat(fromAlpha, toAlpha);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(valueAnimator -> view.setBackgroundColor(
            Utils.adjustColorAlpha(
                dimColor,
                ((Float) valueAnimator.getAnimatedValue())
            )
        ));
    }


    /**
     * Sets the color of the dim.
     *
     * @param dimColor The color to set
     *
     * @return this
     */
    public BackgroundDimmingAnimation setDimColor(@ColorInt int dimColor) {
        this.dimColor = dimColor;
        return this;
    }


    /**
     * Sets the starting alpha value of the animation.
     *
     * @param fromAlpha The starting alpha value to set
     *
     * @return this
     */
    public BackgroundDimmingAnimation setFromAlpha(float fromAlpha) {
        this.fromAlpha = fromAlpha;
        return this;
    }


    /**
     * Sets the ending alpha value to of the animation.
     *
     * @param toAlpha The ending alpha value to set
     *
     * @return this
     */
    public BackgroundDimmingAnimation setToAlpha(float toAlpha) {
        this.toAlpha = toAlpha;
        return this;
    }


    /**
     * Sets the interpolator of the animator.
     *
     * @param interpolator The interpolator to set
     *
     * @return this
     */
    public BackgroundDimmingAnimation setInterpolator(@NonNull Interpolator interpolator) {
        Preconditions.nonNull(interpolator);

        animator.setInterpolator(interpolator);

        return this;
    }


    /**
     * Sets the duration of the animation.
     *
     * @param duration The duration to set
     *
     * @return this
     */
    public BackgroundDimmingAnimation setDuration(long duration) {
        animator.setDuration(duration);
        return this;
    }


    /**
     * Starts the animation.
     */
    public void start() {
        if(isRecycled()) {
            return;
        }

        stop();
        animator.start();
    }


    /**
     * Stops the animation.
     */
    public void stop() {
        if(!isRecycled() && isRunning()) {
            animator.cancel();
        }
    }


    /**
     * Checks whether the animation is running or not.
     *
     * @return true if running; false otherwise
     */
    public boolean isRunning() {
        return animator.isRunning();
    }


    /**
     * Recycles the animation by releasing all underlying resources.
     */
    public void recycle() {
        if(isRecycled) {
            return;
        }

        view = null;
        animator = null;
        isRecycled = true;
    }


    /**
     * Checks whether the animation has been recycled or not.
     *
     * @return true if recycled; false otherwise
     */
    public boolean isRecycled() {
        return isRecycled;
    }


}
