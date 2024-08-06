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

package com.arthurivanets.adapster.model;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.arthurivanets.adapster.Adapter;
import com.arthurivanets.adapster.markers.ItemResources;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A base contract-interface to be implemented by the dataset item.
 *
 * @param <VH> item view holder type
 * @param <IR> reusable resources type
 * @author arthur3486
 */
public interface Item<VH extends RecyclerView.ViewHolder, IR extends ItemResources> extends Serializable {

    int VIEW_TYPE_INVALID = -1;


    /**
     * Initializes the Item View Holder.
     *
     * @param adapter the adapter
     * @param parent parent view
     * @param inflater layout inflater
     * @param resources reusable resources
     * @return the created Item View Holder
     */
    @NonNull
    VH init(@Nullable Adapter<? extends Item> adapter,
            @NonNull ViewGroup parent,
            @NonNull LayoutInflater inflater,
            @Nullable IR resources);


    /**
     * Binds the data.
     *
     * @param adapter the adapter
     * @param viewHolder item view holder
     * @param resources reusable resources
     */
    void bind(@Nullable Adapter<? extends Item> adapter,
              @NonNull VH viewHolder,
              @Nullable IR resources);


    /**
     * @return the Layout Id belonging to this particular Item.
     *      (A Unique ID used to identify the View Type of this Item)
     */
    int getLayout();


}
