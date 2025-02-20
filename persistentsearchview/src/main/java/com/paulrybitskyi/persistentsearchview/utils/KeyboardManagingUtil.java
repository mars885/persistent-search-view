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

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

/**
 * A utility class responsible for interacting with the keyboard.
 */
public final class KeyboardManagingUtil {


    private KeyboardManagingUtil() {}


    /**
     * Shows the keyboard to the user.
     *
     * @param view The view making a request
     */
    public static void showKeyboard(@NonNull View view) {
        Preconditions.nonNull(view);

        InputMethodManager manager = ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));

        if(manager != null) {
            manager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }


    /**
     * Hides the keyboard from the user.
     *
     * @param view The view making the request
     */
    public static void hideKeyboard(@NonNull View view) {
        Preconditions.nonNull(view);

        InputMethodManager manager = ((InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));

        if(manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}