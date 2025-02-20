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

package com.paulrybitskyi.persistentsearchview.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A general utility class holding common methods.
 */
public final class Utils {


    public static final int API_VERSION = Build.VERSION.SDK_INT;

    public static final boolean IS_AT_LEAST_JELLY_BEAN = (API_VERSION >= Build.VERSION_CODES.JELLY_BEAN);
    public static final boolean IS_AT_LEAST_LOLLIPOP = (API_VERSION >= Build.VERSION_CODES.LOLLIPOP);
    public static final boolean IS_AT_LEAST_MARSHMALLOW = (API_VERSION >= Build.VERSION_CODES.M);
    public static final boolean IS_AT_LEAST_NOUGAT = (API_VERSION >= Build.VERSION_CODES.N);
    public static final boolean IS_AT_LEAST_PIE = (API_VERSION >= Build.VERSION_CODES.P);

    public static final Typeface TOOLBAR_TITLE_TYPEFACE = Typeface.create("sans-serif-medium", Typeface.NORMAL);


    private Utils() {}


    /**
     * Retrieves a drawable specified by the resource ID with the specified color.
     *
     * @param context The context
     * @param drawableResId The drawable resource ID
     * @param color The color
     *
     * @return The colored drawable
     */
    public static Drawable getColoredDrawable(Context context, @DrawableRes int drawableResId, int color) {
        return getColoredDrawable(
            ContextCompat.getDrawable(context, drawableResId),
            color
        );
    }


    /**
     * Retrieves a colored drawable.
     *
     * @param drawable The drawable to color
     * @param color The color
     *
     * @return The colored drawable
     */
    public static Drawable getColoredDrawable(Drawable drawable, int color) {
        if(drawable == null) {
            return null;
        }

        drawable.mutate().setColorFilter(
            color,
            PorterDuff.Mode.SRC_ATOP
        );

        return drawable;
    }


    /**
     * Sets a cursor drawable of the specified {@link EditText}.
     *
     * Note: This solution won't work on PIE devices.
     *
     * @param editText The edit text
     * @param cursorDrawable The cursor drawable to set
     */
    @SuppressLint({"SoonBlockedPrivateApi", "BlockedPrivateApi", "DiscouragedPrivateApi"})
    public static void setCursorDrawable(EditText editText, Drawable cursorDrawable) {
        if (IS_AT_LEAST_PIE) {
            return;
        }

        if((editText != null) && (cursorDrawable != null)) {
            // Reflection based on the declared fields in original AOSP file
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            try {
                Field cursorDrawableResourceField = TextView.class.getDeclaredField("mCursorDrawableRes");
                cursorDrawableResourceField.setAccessible(true);

                Object cursorDrawableFieldOwner;
                Class<?> cursorDrawableFieldClass;

                if(IS_AT_LEAST_JELLY_BEAN) {
                    Field editorField = TextView.class.getDeclaredField("mEditor");
                    editorField.setAccessible(true);

                    cursorDrawableFieldOwner = editorField.get(editText);
                    cursorDrawableFieldClass = cursorDrawableFieldOwner.getClass();
                } else {
                    cursorDrawableFieldOwner = editText;
                    cursorDrawableFieldClass = TextView.class;
                }

                Field cursorDrawableField = cursorDrawableFieldClass.getDeclaredField("mCursorDrawable");
                cursorDrawableField.setAccessible(true);
                cursorDrawableField.set(
                    cursorDrawableFieldOwner,
                    new Drawable[] {cursorDrawable, cursorDrawable}
                );
            } catch(Exception e) {}
        }
    }


    /**
     * Sets the color of the specified progress bar.
     *
     * @param progressBar The progress bar
     * @param color The color to set
     */
    public static void setProgressBarColor(@NonNull ProgressBar progressBar, int color) {
        Preconditions.nonNull(progressBar);

        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }


    /**
     * Retrieves a locale of the device.
     *
     * @param context The context
     *
     * @return The device's locale
     */
    @SuppressWarnings("NewApi")
    public static Locale getLocale(Context context) {
        if(IS_AT_LEAST_NOUGAT) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }


    /**
     * Disables the animations of the specified {@link RecyclerView}.
     *
     * @param recyclerView The recycler view to disable animations of
     */
    public static void disableRecyclerViewAnimations(RecyclerView recyclerView) {
        if((recyclerView != null) && (recyclerView.getItemAnimator() != null)) {
            recyclerView.setItemAnimator(null);
        }
    }


    /**
     * Adjusts the alpha channel of the specified color.
     *
     * @param color The color to adjust alpha of
     * @param alpha The alpha value
     *
     * @return The color with the adjusted alpha channel
     */
    public static int adjustColorAlpha(int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        final int alphaChannel = (int) (255 * alpha);
        final int redChannel = Color.red(color);
        final int greenChannel = Color.green(color);
        final int blueChannel = Color.blue(color);

        return Color.argb(alphaChannel, redChannel, greenChannel, blueChannel);
    }


    /**
     * Retrieves the dimensions of the screen packaged inside an array.
     *
     * @param context The context
     *
     * @return The integer array in the following format:
     * int[2] = int[0] - width, int[1] - height
     */
    public static int[] getScreenSize(Context context) {
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        final int[] size = {
            displayMetrics.widthPixels,
            displayMetrics.heightPixels
        };

        return size;
    }


    /**
     * Checks whether the speech recognition is available on the device or not.
     *
     * @param context The context
     *
     * @return true if available; false otherwise
     */
    public static boolean isSpeechRecognitionAvailable(@NonNull Context context) {
        Preconditions.nonNull(context);

        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> activities = packageManager.queryIntentActivities(
            new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),
            PackageManager.MATCH_DEFAULT_ONLY
        );

        return (activities.size() > 0);
    }


}
