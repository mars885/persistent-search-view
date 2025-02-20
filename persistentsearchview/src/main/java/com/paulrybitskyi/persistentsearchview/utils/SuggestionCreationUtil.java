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

import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.model.Suggestion;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * A utility class responsible for wrapping raw suggestions into
 * instances of the {@link SuggestionItem} class.
 */
public final class SuggestionCreationUtil {


    private SuggestionCreationUtil() {}


    /**
     * Wraps raw suggestions into a list of {@link SuggestionItem} objects with
     * the type set to {@link Suggestion#TYPE_RECENT_SEARCH_SUGGESTION}.
     *
     * @param rawSuggestions The suggestions to wrap
     *
     * @return A list of {@link SuggestionItem} objects
     */
    public static List<SuggestionItem> asRecentSearchSuggestions(@NonNull List<String> rawSuggestions) {
        return fromRawSuggestions(Suggestion.TYPE_RECENT_SEARCH_SUGGESTION, rawSuggestions);
    }


    /**
     * Wraps raw suggestions into a list of {@link SuggestionItem} objects with
     * the type set to {@link Suggestion#TYPE_REGULAR_SEARCH_SUGGESTION}.
     *
     * @param rawSuggestions The suggestions to wrap
     *
     * @return A list of {@link SuggestionItem} objects
     */
    public static List<SuggestionItem> asRegularSearchSuggestions(@NonNull List<String> rawSuggestions) {
        return fromRawSuggestions(Suggestion.TYPE_REGULAR_SEARCH_SUGGESTION, rawSuggestions);
    }


    /**
     * Wraps raw suggestions into a list of {@link SuggestionItem} objects with
     * the type set to passed one.
     *
     * @param suggestionType The suggestion type
     * @param rawSuggestions The suggestions to wrap
     *
     * @return A list of {@link SuggestionItem} objects
     */
    private static List<SuggestionItem> fromRawSuggestions(@NonNull String suggestionType, @NonNull List<String> rawSuggestions) {
        Preconditions.nonEmpty(suggestionType);
        Preconditions.nonNull(rawSuggestions);

        final List<SuggestionItem> suggestionItems = new ArrayList<>();

        for(String rawSuggestion : rawSuggestions) {
            suggestionItems.add(
                new SuggestionItem(
                    new Suggestion()
                        .setType(suggestionType)
                        .setText(rawSuggestion)
                )
            );
        }

        return suggestionItems;
    }


    /**
     * Wraps a list suggestions into a list of {@link SuggestionItem} objects.
     *
     * @param suggestions The suggestions to wrap
     *
     * @return A list of {@link SuggestionItem} objects
     */
    public static List<SuggestionItem> asSuggestions(@NonNull List<? super Suggestion> suggestions) {
        Preconditions.nonNull(suggestions);

        final int itemCount = suggestions.size();
        final List<SuggestionItem> suggestionItems = new ArrayList<>(itemCount);

        for(int i = 0; i < itemCount; i++) {
            suggestionItems.add(new SuggestionItem((Suggestion) suggestions.get(i)));
        }

        return suggestionItems;
    }


}
