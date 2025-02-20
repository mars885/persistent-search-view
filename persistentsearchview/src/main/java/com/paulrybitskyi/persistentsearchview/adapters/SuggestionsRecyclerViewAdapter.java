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

package com.paulrybitskyi.persistentsearchview.adapters;

import android.content.Context;

import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.arthurivanets.adapster.markers.ItemResources;
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.adapters.resources.SuggestionItemResources;
import com.paulrybitskyi.persistentsearchview.utils.Preconditions;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * A recycler view adapter implementation for suggestion items.
 */
public class SuggestionsRecyclerViewAdapter extends TrackableRecyclerViewAdapter<
    Long,
    SuggestionItem,
    SuggestionItem.ViewHolder
> {


    private ItemResources resources;

    private OnItemClickListener<SuggestionItem> onItemClickListener;
    private OnItemClickListener<SuggestionItem> onItemRemoveButtonClickListener;


    public SuggestionsRecyclerViewAdapter(
        Context context,
        List<SuggestionItem> items,
        @NonNull SuggestionItemResources resources
    ) {
        super(context, items);
        setResources(resources);
    }


    @Override
    protected final void assignListeners(SuggestionItem.ViewHolder holder, int position, SuggestionItem item) {
        super.assignListeners(holder, position, item);

        item.setOnItemClickListener(holder, onItemClickListener);
        item.setOnItemRemoveButtonClickListener(holder, onItemRemoveButtonClickListener);
    }


    /**
     * Sets the listener to invoke when the item is clicked.
     *
     * @param onItemClickListener The listener to set
     */
    public final void setOnItemClickListener(OnItemClickListener<SuggestionItem> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /**
     * Sets the listener to invoke when the remove button is clicked.
     *
     * @param onItemRemoveButtonClickListener The listener to set
     */
    public final void setOnItemRemoveButtonClickListener(OnItemClickListener<SuggestionItem> onItemRemoveButtonClickListener) {
        this.onItemRemoveButtonClickListener = onItemRemoveButtonClickListener;
    }


    /**
     * Sets the resources to be used by the adapter.
     *
     * @param resources The resources to set
     */
    public final void setResources(@NonNull SuggestionItemResources resources) {
        Preconditions.nonNull(resources);

        this.resources = resources;
        notifyDataSetChanged();
    }


    @Override
    public final ItemResources getResources() {
        return resources;
    }


}
