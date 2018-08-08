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

import com.paulrybitskyi.persistentsearchview.PersistentSearchView;

/**
 * A listener used for notifying when a search has been confirmed.
 */
public interface OnSearchConfirmedListener {

    /**
     * Gets called when a search has been confirmed.
     *
     * @param searchView The search view
     * @param query The query to perform a search on
     */
    void onSearchConfirmed(PersistentSearchView searchView, String query);

}
