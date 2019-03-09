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

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import androidx.annotation.NonNull;


/**
 * A utility class responsible for performing precondition checks
 * on arguments and throwing appropriate exceptions in case the precondition
 * is not fulfilled.
 */
public final class Preconditions {




    /**
     * Checks whether the condition is true. If not, throws {@link IllegalStateException}.
     *
     * @param condition The condition to check
     */
    public static void isTrue(boolean condition) {
        isTrue("Condition", condition);
    }




    /**
     * Checks whether the condition is true. If not, throws {@link IllegalStateException}.
     *
     * @param info The info to attach to the message of the exception
     * @param condition The condition to check
     */
    public static void isTrue(@NonNull String info, boolean condition) {
        nonNull(info);

        if(!condition) {
            throw new IllegalStateException(String.format(
                Locale.US,
                "%s - the condition is not met. The Condition must be positive.",
                info
            ));
        }
    }




    /**
     * Checks whether the object is not null. If null, throws {@link NullPointerException}.
     *
     * @param object The object to check
     */
    public static void nonNull(Object object) {
        if(object == null) {
            throw new NullPointerException("The argument must be non-null!");
        }
    }




    /**
     * Checks whether the string is not empty. If empty, throws {@link IllegalArgumentException}.
     *
     * @param string The string to check
     */
    public static void nonEmpty(String string) {
        if(TextUtils.isEmpty(string)) {
            throw new IllegalArgumentException("You must specify a valid raw text.");
        }
    }




    /**
     * Checks whether the collection is not empty. If empty, throws {@link IllegalArgumentException}.
     *
     * @param collection The collection to check
     */
    public static void nonEmpty(Collection<?> collection) {
        nonNull(collection);

        if(collection.isEmpty()) {
            throw new IllegalArgumentException("You must specify a collection that contains at least one element.");
        }
    }




    /**
     * Checks whether the index is within the bounds of the dataset, excluding the size of the dataset.
     *
     * @param index The index to check
     * @param dataset The dataset to use
     */
    public static void withinBoundsExclusive(int index, ArrayList<?> dataset) {
        nonNull(dataset);

        if((index < 0) || (index >= dataset.size())) {
            throw new IndexOutOfBoundsException("The Index must lie within the bounds of the specified dataset (0 <= index < dataset.size).");
        }
    }




    /**
     * Checks whether the index is within the bounds of the dataset, including the size of the dataset.
     *
     * @param index The index to check
     * @param dataset The dataset to use
     */
    public static void withinBoundsInclusive(int index, ArrayList<?> dataset) {
        nonNull(dataset);

        if((index < 0) || (index > dataset.size())) {
            throw new IndexOutOfBoundsException("The Index must lie within the bounds of the specified dataset (0 <= index <= dataset.size).");
        }
    }




}
