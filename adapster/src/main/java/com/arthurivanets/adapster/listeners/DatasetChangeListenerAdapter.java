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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A convenience abstract class which implements the methods of the {@link OnDatasetChangeListener}.
 *
 * @param <DS> dataset type
 * @param <IT> item type
 * @author arthur3486
 */
public abstract class DatasetChangeListenerAdapter<DS extends List<IT>, IT> implements OnDatasetChangeListener<DS, IT> {




    @Override
    public void onItemAdded(@NonNull DS dataset, @Nullable IT item) {

    }




    @Override
    public void onItemUpdated(@NonNull DS dataset, @Nullable IT item) {

    }




    @Override
    public void onItemReplaced(@NonNull DS dataset, @Nullable IT oldItem, @Nullable IT newItem) {

    }




    @Override
    public void onItemDeleted(@NonNull DS dataset, @Nullable IT item) {

    }




    @Override
    public void onDatasetSizeChanged(int oldSize, int newSize) {

    }




    @Override
    public void onDatasetReplaced(@NonNull DS newDataset) {

    }




    @Override
    public void onDatasetCleared(@NonNull DS dataset) {

    }




}
