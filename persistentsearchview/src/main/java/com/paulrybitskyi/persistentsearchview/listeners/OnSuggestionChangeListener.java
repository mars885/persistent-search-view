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

package com.paulrybitskyi.persistentsearchview.listeners;

import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;

/**
 * A listener used for notifying when a particular action
 * has been performed on a suggestion.
 */
public interface OnSuggestionChangeListener {

    /**
     * Gets called when a suggestion has been picked.
     *
     * @param suggestion The suggestion item
     */
    void onSuggestionPicked(SuggestionItem suggestion);

    /**
     * Gets called when a suggestion has been removed.
     *
     * @param suggestion The suggestion item
     */
    void onSuggestionRemoved(SuggestionItem suggestion);

}
