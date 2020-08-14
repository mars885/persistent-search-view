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

package com.paulrybitskyi.persistentsearchview.listeners;

import android.animation.Animator;

/**
 * A listener adapter for the {@link Animator.AnimatorListener}.
 */
public abstract class AnimatorListenerAdapter implements Animator.AnimatorListener {


    private boolean isCancelled;


    public AnimatorListenerAdapter() {
        isCancelled = false;
    }


    @Override
    public final void onAnimationStart(Animator animation, boolean isReverse) {
        isCancelled = false;

        onAnimationStarted(animation);
    }


    @Override
    public final void onAnimationStart(Animator animation) {
        isCancelled = false;

        onAnimationStarted(animation);
    }


    /**
     * Gets called when an animation has been started.
     *
     * @param animation The animator
     */
    public void onAnimationStarted(Animator animation) {

    }


    @Override
    public final void onAnimationEnd(Animator animation, boolean isReverse) {
        onAnimationEnded(animation);
    }


    @Override
    public final void onAnimationEnd(Animator animation) {
        onAnimationEnded(animation);
    }


    /**
     * Gets called when an animation has been ended.
     *
     * @param animation The animator
     */
    public void onAnimationEnded(Animator animation) {

    }


    @Override
    public final void onAnimationCancel(Animator animation) {
        isCancelled = true;

        onAnimationCancelled(animation);
    }


    /**
     * Gets called when an animation has been cancelled.
     *
     * @param animation The animator
     */
    public void onAnimationCancelled(Animator animation) {

    }


    @Override
    public final void onAnimationRepeat(Animator animation) {
        isCancelled = false;

        onAnimationRepeated(animation);
    }


    /**
     * Gets called when an animation has been repeated.
     *
     * @param animation The animator
     */
    public void onAnimationRepeated(Animator animation) {

    }


    /**
     * Checks whether the animation is cancelled or not.
     *
     * @return true if cancelled; false otherwise
     */
    public final boolean isCancelled() {
        return isCancelled;
    }


}
