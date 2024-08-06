/*
 * Copyright 2017 Arthur Ivanets, arthur.ivanets.l@gmail.com
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

package com.arthurivanets.adapster.listeners;

import android.view.MotionEvent;
import android.view.View;

/**
 * A contract-interface for the observation of the dataset item touch events.
 *
 * @param <T> the item type
 * @author arthur3486
 */
public interface OnItemTouchListener<T> {

    /**
     * Called when the dataset item is touched.
     *
     * @param view the origin of the click event
     * @param motionEvent the generated motion event
     * @param item the associated item
     * @param position the associated position info
     * @return <strong>true</strong> to continue the consumption of the touch events, <strong>false</strong> otherwise.
     */
    boolean onItemTouch(View view, MotionEvent motionEvent, T item, int position);

}
