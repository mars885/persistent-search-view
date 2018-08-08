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

package com.paulrybitskyi.persistentsearchview.utils;

import android.view.View;
import android.view.ViewGroup;

import com.paulrybitskyi.persistentsearchview.R;

import androidx.annotation.NonNull;

/**
 * A utility class holding common view methods.
 */
public final class ViewUtils {




    /**
     * Cancels all view's animations.
     *
     * @param view The view to cancel animations of
     */
    public static void cancelAllAnimations(@NonNull View view) {
        Preconditions.nonNull(view);

        view.clearAnimation();
        view.animate().cancel();
    }




    /**
     * Updates the height of the specified view.
     *
     * @param view The view to update height of
     * @param height The height to set
     */
    public static void updateHeight(@NonNull View view, int height) {
        Preconditions.nonNull(view);

        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;

        view.setLayoutParams(layoutParams);
    }




    /**
     * Changes the visibility flags of the specified view to the {@link View#VISIBLE}.
     *
     * @param view The view to make visible
     */
    public static void makeVisible(@NonNull View view) {
        setVisibility(view, View.VISIBLE);
    }




    /**
     * Changes the visibility flags of the specified view to the {@link View#INVISIBLE}.
     *
     * @param view The view to make invisible
     */
    public static void makeInvisible(@NonNull View view) {
        setVisibility(view, View.INVISIBLE);
    }




    /**
     * Changes the visibility flags of the specified view to the {@link View#GONE}.
     *
     * @param view The view to make gone
     */
    public static void makeGone(@NonNull View view) {
        setVisibility(view, View.GONE);
    }




    /**
     * Changes the visibility flags of the specified view to the passed one.
     *
     * @param view The view to change visibility of
     * @param visibility The new visibility
     */
    public static void setVisibility(@NonNull View view, int visibility) {
        Preconditions.nonNull(view);

        view.setVisibility(visibility);
    }




    /**
     * Retrieves the visibility of the view.
     *
     * @param view The view to get visibility of
     *
     * @return The view's visibility
     */
    public static int getVisibility(@NonNull View view) {
        Preconditions.nonNull(view);
        return view.getVisibility();
    }




    /**
     * Sets the scale of the view to the passed on.
     *
     * @param view The view to change the scale of
     * @param scale The scale to set
     */
    public static void setScale(@NonNull View view, float scale) {
        Preconditions.nonNull(view);

        view.setScaleX(scale);
        view.setScaleY(scale);
    }




    /**
     * Sets the visibility marker tag of the specified view.
     *
     * @param view The view to set the visibility marker tag of
     * @param isVisible The visibility marker tag value
     */
    public static void setVisibilityMarker(@NonNull View view, boolean isVisible) {
        Preconditions.nonNull(view);

        view.setTag(R.id.visibility_marker, isVisible);
    }




    /**
     * Retrieves the visibility marker tag value of the specified view.
     *
     * @param view The view to get the visibility marker tag value of
     *
     * @return The visibility marker tag value of the specified view
     */
    public static boolean getVisibilityMarker(@NonNull View view) {
        Preconditions.nonNull(view);

        final Boolean visibilityMarker = (Boolean) view.getTag(R.id.visibility_marker);

        return ((visibilityMarker != null) ? visibilityMarker : isVisible(view));
    }




    /**
     * Sets the animation marker tag of the specified view.
     *
     * @param view The view to set the animation marker tag of
     * @param marker The animation marker tag value
     */
    public static void setAnimationMarker(@NonNull View view, Object marker) {
        Preconditions.nonNull(view);

        view.setTag(R.id.animation_marker, marker);
    }




    /**
     * Retrieves the animation marker tag value of the specified view.
     *
     * @param view The view to get the animation marker tag value of
     *
     * @return The animation marker tag value of the specified view
     */
    public static <T> T getAnimationMarker(@NonNull View view) {
        Preconditions.nonNull(view);
        return (T) view.getTag(R.id.animation_marker);
    }




    /**
     * Checks whether the visibility flags of the view is {@link View#VISIBLE}.
     *
     * @param view The view to check visibility flags of
     *
     * @return true if visible; false otherwise
     */
    public static boolean isVisible(@NonNull View view) {
        return (getVisibility(view) == View.VISIBLE);
    }




    /**
     * Checks whether the visibility flags of the view is {@link View#INVISIBLE}.
     *
     * @param view The view to check visibility flags of
     *
     * @return true if invisible; false otherwise
     */
    public static boolean isInvisible(@NonNull View view) {
        return (getVisibility(view) == View.INVISIBLE);
    }




    /**
     * Checks whether the visibility flags of the view is {@link View#GONE}.
     *
     * @param view The view to check visibility flags of
     *
     * @return true if gone; false otherwise
     */
    public static boolean isGone(@NonNull View view) {
        return (getVisibility(view) == View.GONE);
    }




}
