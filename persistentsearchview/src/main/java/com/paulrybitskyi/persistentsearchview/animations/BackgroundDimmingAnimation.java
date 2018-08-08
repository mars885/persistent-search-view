/*
 * Copyright 2017
 *
 * Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 * Arthur Ivanets, arthur.ivanets.l@gmail.com
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


    private int mDimColor;

    private float mFromAlpha;
    private float mToAlpha;

    private View mView;

    private ValueAnimator mAnimator;

    private boolean mIsRecycled;




    public BackgroundDimmingAnimation(@NonNull View view,
                                      @ColorInt int dimColor,
                                      float fromAlpha,
                                      float toAlpha) {
        mView = view;
        mDimColor = dimColor;
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
        mIsRecycled = false;

        initAnimator();
    }




    private void initAnimator() {
        mAnimator = ValueAnimator.ofFloat(mFromAlpha, mToAlpha);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mView.setBackgroundColor(Utils.adjustColorAlpha(
                    mDimColor,
                    ((Float) valueAnimator.getAnimatedValue())
                ));
            }
        });
    }




    /**
     * Sets the color of the dim.
     *
     * @param dimColor The color to set
     *
     * @return this
     */
    public BackgroundDimmingAnimation setDimColor(@ColorInt int dimColor) {
        mDimColor = dimColor;
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
        mFromAlpha = fromAlpha;
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
        mToAlpha = toAlpha;
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

        mAnimator.setInterpolator(interpolator);

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
        mAnimator.setDuration(duration);
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
        mAnimator.start();
    }




    /**
     * Stops the animation.
     */
    public void stop() {
        if(!isRecycled() && isRunning()) {
            mAnimator.cancel();
        }
    }




    /**
     * Checks whether the animation is running or not.
     *
     * @return true if running; false otherwise
     */
    public boolean isRunning() {
        return mAnimator.isRunning();
    }




    /**
     * Recycles the animation by releasing all underlying resources.
     */
    public void recycle() {
        if(mIsRecycled) {
            return;
        }

        mView = null;
        mAnimator = null;
        mIsRecycled = true;
    }




    /**
     * Checks whether the animation has been recycled or not.
     *
     * @return true if recycled; false otherwise
     */
    public boolean isRecycled() {
        return mIsRecycled;
    }




}
