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

package com.paulrybitskyi.persistentsearchview.listeners;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * A search query listener used for notifying about major
 * events.
 */
public abstract class QueryListener implements TextWatcher {


    private String previousQuery;


    public QueryListener() {
        previousQuery = "";
    }


    @Override
    public final void beforeTextChanged(CharSequence text, int start, int count, int after) {}


    @Override
    public final void onTextChanged(CharSequence text, int start, int before, int count) {
        final String newQuery = text.toString();

        if(!newQuery.equals(previousQuery)) {
            onQueryChanged(previousQuery, newQuery);
        }

        if(!TextUtils.isEmpty(text)) {
            onQueryEntered(newQuery);
        } else {
            onQueryRemoved();
        }

        previousQuery = newQuery;
    }


    @Override
    public final void afterTextChanged(Editable editable) {}


    /**
     * Gets called when a search query has been changed.
     *
     * @param oldQuery The old query
     * @param newQuery The new query
     */
    public void onQueryChanged(String oldQuery, String newQuery) {}


    /**
     * Gets called when a search query has been entered.
     *
     * @param query The search query
     */
    public void onQueryEntered(String query) {}


    /**
     * Gets called whenever a search query has been removed.
     */
    public void onQueryRemoved() {}


}
