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

package com.paulrybitskyi.persistentsearchview.model;

import java.io.Serializable;

/**
 * A model class representing a suggestion.
 */
public class Suggestion implements Serializable {


    public static final String TYPE_RECENT_SEARCH_SUGGESTION = "recent_search_suggestion";
    public static final String TYPE_REGULAR_SEARCH_SUGGESTION = "regular_search_suggestion";


    private long id;

    private String type;
    private String text;


    public Suggestion() {
        this.id = -1L;
        this.type = TYPE_REGULAR_SEARCH_SUGGESTION;
        this.text = "";
    }


    /**
     * Sets the id of the suggestion.
     *
     * @param id The id to set
     *
     * @return this
     */
    public Suggestion setId(long id) {
        this.id = id;
        return this;
    }


    /**
     * Gets the id of the suggestion.
     *
     * @return The suggestion's id
     */
    public long getId() {
        return this.id;
    }


    /**
     * Checks whether this suggestion has a valid ID (bigger than 0).
     *
     * @return true if valid; false otherwise
     */
    public boolean hasValidId() {
        return (this.id > 0);
    }


    /**
     * Sets the type of the suggestion. Can be either TYPE_REGULAR_SEARCH_SUGGESTION
     * or TYPE_REGULAR_SEARCH_SUGGESTION.
     *
     * @param type The type to set
     *
     * @return this
     */
    public Suggestion setType(String type) {
        this.type = type;
        return this;
    }


    /**
     * Gets the type of the suggestion.
     *
     * @return The suggestion's type
     */
    public String getType() {
        return this.type;
    }


    /**
     * Sets the text of the suggestion.
     *
     * @param text The text to set
     *
     * @return this
     */
    public Suggestion setText(String text) {
        this.text = text;
        return this;
    }


    /**
     * Gets the text of the suggestion.
     *
     * @return The suggestion's text
     */
    public String getText() {
        return this.text;
    }


}
