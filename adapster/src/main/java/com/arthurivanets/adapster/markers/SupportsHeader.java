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

package com.arthurivanets.adapster.markers;

import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.arthurivanets.adapster.model.markers.Header;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A marker interface which is to be implemented by the
 * adapter to provide the Header Item support.
 *
 * @param <VH> View Holder
 *
 * @author arthur3486
 */
public interface SupportsHeader<VH extends RecyclerView.ViewHolder> {

    /**
     * Adds the Header Item to the dataset.
     *
     * @param header the Header Item to be added to the dataset
     */
    <VHT extends VH> void addHeader(@NonNull Header<VHT> header);

    /**
     * Removes the Header Item from the dataset (if there's any).
     */
    void removeHeader();

    /**
     * Sets the Header Item click listener.
     *
     * @param onHeaderClickListener the listener to be set
     */
    void setOnHeaderClickListener(@Nullable OnItemClickListener<Header<VH>> onHeaderClickListener);

}
