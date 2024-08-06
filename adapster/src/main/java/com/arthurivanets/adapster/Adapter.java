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

package com.arthurivanets.adapster;

import com.arthurivanets.adapster.listeners.OnDatasetChangeListener;
import com.arthurivanets.adapster.model.Item;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A contract-interface representing the Adapter and its required functionality.
 *
 * @param <IT> the dataset item type
 * @author arthur3486
 */
public interface Adapter<IT extends Item> {

    /**
     *  Adds the specified item to the end of the underlying dataset.
     *
     *  @param item to be added
     */
    void addItem(@NonNull IT item);

    /**
     *  Adds the specified item to the end of the underlying dataset.
     *
     *  @param item to be added
     *  @param notifyAboutTheChange whether to notify the subscribers about the dataset change via DataSetChangeListener
     */
    void addItem(@NonNull IT item, boolean notifyAboutTheChange);

    /**
     *  Adds the specified item at a specified position to the underlying dataset.
     *
     *  @param position to add the item at
     *  @param item to be added
     */
    void addItem(int position, @NonNull IT item);

    /**
     *  Adds the specified item at a specified position to the underlying dataset.
     *
     *  @param position to add the item at
     *  @param item to be added
     *  @param notifyAboutTheChange whether to notify the subscribers about the dataset change via DataSetChangeListener
     */
    void addItem(int position, @NonNull IT item, boolean notifyAboutTheChange);

    /**
     *  Adds the specified item or updates (replaces) the existing item
     *  with the specified item within the underlying dataset.
     *
     *  @param item to be added or replaced
     */
    void addOrUpdateItem(@NonNull IT item);

    /**
     *  Adds the specified item or updates (replaces) the existing item
     *  with the specified item within the underlying dataset.
     *
     *  @param item to be added or replaced
     *  @param notifyAboutTheChange whether to notify the subscribers about the dataset change via DataSetChangeListener
     */
    void addOrUpdateItem(@NonNull IT item, boolean notifyAboutTheChange);

    /**
     *  Adds the specified item at a specified position or updates (replaces) the existing item
     *  with the specified item within the underlying dataset.
     *
     *  @param position to add the item at
     *  @param item to be added or replaced
     */
    void addOrUpdateItem(int position, @NonNull IT item);

    /**
     *  Adds the specified item at a specified position or updates (replaces) the existing item
     *  with the specified item within the underlying dataset.
     *
     *  @param position to add the item at
     *  @param item to be added or replaced
     *  @param notifyAboutTheChange whether to notify the subscribers about the dataset change via DataSetChangeListener
     */
    void addOrUpdateItem(int position, @NonNull IT item, boolean notifyAboutTheChange);

    /**
     *  Updates item (causes item to be rebound by RecyclerView), if it's present within the dataset.
     *
     * @param item to be updated
     */
    void updateItem(@NonNull IT item);

    /**
     *  Updates item (causes item to be rebound by RecyclerView) at a specified position.
     *
     *  @param position item position.
     */
    void updateItem(int position);

    /**
     *  Updates (Replaces) the corresponding item within the underlying dataset with the specified item.
     *
     *  @param item replacement item
     */
    void updateItemWith(@NonNull IT item);

    /**
     *  Updates (Replaces) the corresponding item within the underlying dataset with the specified item.
     *
     * @param item replacement item
     * @param notifyAboutTheChange whether to notify the subscribers about the dataset change via DataSetChangeListener
     */
    void updateItemWith(@NonNull IT item, boolean notifyAboutTheChange);

    /**
     *  Updates (Replaces) the item at a specified position
     *  within the underlying dataset with the specified item.
     *
     *  @param position existing item position
     *  @param item replacement item
     */
    void updateItemWith(int position, @NonNull IT item);

    /**
     *  Updates (Replaces) the item at a specified position
     *  within the underlying dataset with the specified item.
     *
     *  @param position existing item position
     *  @param item replacement item
     *  @param notifyAboutTheChange whether to notify the subscribers about the dataset change via DataSetChangeListener
     */
    void updateItemWith(int position, @NonNull IT item, boolean notifyAboutTheChange);

    /**
     *  Removes the specified item from the Adapter (from the underlying dataset),
     *  if it is present.
     *
     *  @param item to be removed
     */
    void deleteItem(@NonNull IT item);

    /**
     *  Removes the item from the Adapter (from the underlying dataset),
     *  at a specified position.
     *
     *  @param position item position
     */
    void deleteItem(int position);

    /**
     *  Checks to see if the underlying dataset contains the specified item.
     *
     *  @param item whose presence will be checked
     *  @return true if the item is present, false otherwise.
     */
    boolean contains(@NonNull IT item);

    /**
     *  Looks up the index (within the dataset) of the specified item,
     *  provided that the item is present in the dataset.
     *
     *  @param item to find the index of.
     *  @return the found index, or -1 if no matching item was found.
     */
    int indexOf(@NonNull IT item);

    /**
     *  Looks up the last index within the current adapter's dataset.
     *
     *  @return the last index in this adapter's dataset, or -1 if the dataset is empty
     */
    int lastIndex();

    /**
     *  Removes all the items from the Adapter.
     */
    void clear();

    /**
     *  Sets the underlying dataset. (Notifies the adapter about the changes)
     *
     *  @param items to be used as an underlying dataset
     */
    void setItems(@NonNull List<IT> items);

    /**
     *  Sets the underlying dataset.
     *
     *  @param items to be used as an underlying dataset
     *  @param notifyAboutTheChange whether to notify the subscribers about the dataset change via DataSetChangeListener
     */
    void setItems(@NonNull List<IT> items, boolean notifyAboutTheChange);

    /**
     *  Retrieves the underlying dataset, in a form of a List of corresponding items.
     *
     *  @return the underlying dataset.
     */
    @NonNull
    List<IT> getItems();

    /**
     *  Retrieves the item at a specified position.
     *
     *  @param position item position
     *  @return the corresponding item, or null.
     */
    @Nullable
    IT getItem(int position);

    /**
     *  Retrieves the item at the very first position within this adapter's dataset.
     *
     *  @return the retrieved item, or null if the dataset is empty.
     */
    @Nullable
    IT getFirstItem();

    /**
     *  Retrieves the item at the very last position within this adapter's dataset.
     *
     *  @return the retrieved item, or null if the dataset is empty.
     */
    @Nullable
    IT getLastItem();

    /**
     *  Retrieves the item count of the underlying dataset.
     *
     *  @return the exact item count of the underlying dataset.
     */
    int getItemCount();

    /**
     *  Associates the specified OnDatasetChangeListener with the current adapter.
     */
    void addOnDatasetChangeListener(@NonNull OnDatasetChangeListener<List<IT>, IT> onDatasetChangeListener);

    /**
     *  Dissociates the specified instance of the OnDatasetChangeListener from the current adapter.
     */
    void removeOnDatasetChangeListener(@NonNull OnDatasetChangeListener<List<IT>, IT> onDatasetChangeListener);

    /**
     *  Removes all the OnDatasetChangeListener(-s) associated with the current adapter.
     */
    void removeAllOnDatasetChangeListeners();

}
