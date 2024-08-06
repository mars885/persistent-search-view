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

package com.arthurivanets.adapster.recyclerview;

import android.content.Context;

import com.arthurivanets.adapster.model.BaseItem;
import com.arthurivanets.adapster.model.Item;
import com.arthurivanets.adapster.model.markers.Footer;
import com.arthurivanets.adapster.model.markers.Header;
import com.arthurivanets.adapster.model.markers.Trackable;
import com.arthurivanets.adapster.util.Preconditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

/**
 * An abstract implementation of the RecyclerView Adapter which allows for the easy item tracking
 * and prevention of the duplicates. (To prevent the item duplication an item must implement the {@link Trackable} interface
 * and provide a unique track key)
 *
 * @param <KT> the item key type
 * @param <IT> the item type
 * @param <VH> the item view holder type
 * @author arthur3486
 */
public abstract class TrackableRecyclerViewAdapter<KT, IT extends BaseItem, VH extends BaseItem.ViewHolder<?>> extends BaseRecyclerViewAdapter<IT, VH> {


    private final Map<KT, Trackable<KT>> mKeyTrackableMap;




    public TrackableRecyclerViewAdapter(@NonNull Context context, @NonNull List<IT> items) {
        super(context, items);
        mKeyTrackableMap = new HashMap<>();
        setItems(items);
    }




    @Override
    public void addItem(int position, @NonNull IT item, boolean notifyAboutTheChange) {
        Preconditions.withinBoundsInclusive(position, getItems());
        Preconditions.nonNull(item);

        // in case the item is already present, returning
        if(contains(item)) {
            return;
        }

        // processing the item
        final int itemCount = getItemCount();
        final boolean isFirstItemHeaderView = ((itemCount > 0) && (getItem(0) instanceof Header));
        final boolean isLastItemFooterView = ((itemCount > 0) && (getItem(itemCount - 1) instanceof Footer));

        if(item instanceof Header) {
            if(isFirstItemHeaderView) {
                throw new IllegalStateException("Only a single Header can be present in a dataset at a time. Please, remove the old Header first, and then proceed with adding a new one.");
            }

            getItems().add((position = 0), item);
        } else if(item instanceof Footer) {
            if(isLastItemFooterView) {
                throw new IllegalStateException("Only a single Footer can be present in a dataset at a time. Please, remove the old Footer first, and then proceed with adding a new one.");
            }

            getItems().add((position = itemCount), item);
        } else {
            if((position == 0) && isFirstItemHeaderView) {
                getItems().add((position + 1), item);
            } else if((position == itemCount) && isLastItemFooterView) {
                getItems().add((position - 1), item);
            } else {
                getItems().add(position, item);
            }
        }

        // adding the item to the map of trackables (if necessary)
        trackIfNecessary(item);

        // notifying the Adapter about the change (if necessary)
        if(notifyAboutTheChange) {
            notifyItemInserted(position);
        }

        // notifying about the dataset change
        notifyItemAdded(item);
        notifyDatasetSizeChanged(itemCount, getItemCount());
    }




    @Override
    public void addOrUpdateItem(int position, @NonNull IT item, boolean notifyAboutTheChange) {
        Preconditions.withinBoundsInclusive(position, getItems());
        Preconditions.nonNull(item);

        if(contains(item)) {
            updateItemWith(item, notifyAboutTheChange);
        } else {
            addItem(position, item, notifyAboutTheChange);
        }
    }




    @Override
    public void updateItemWith(int position, @NonNull IT item, boolean notifyAboutTheChange) {
        Preconditions.withinBoundsExclusive(position, getItems());
        Preconditions.nonNull(item);

        final IT oldItem = getItem(position);

        // untracking the old item, as it no longer relates to this dataset
        untrackIfNecessary(oldItem);

        // replacing the old item(if there was any) and retracking the item
        getItems().set(position, item);
        trackIfNecessary(item);

        // notifying the Adapter about the change(if necessary)
        if(notifyAboutTheChange) {
            notifyItemChanged(position);
        }

        // notifying about the dataset change
        notifyItemReplaced(oldItem, item);
    }




    @Override
    public void deleteItem(int position) {
        Preconditions.withinBoundsExclusive(position, getItems());

        final int itemCount = getItemCount();

        // removing the actual item, as well as untracking it (if necessary)
        final IT removedItem = getItems().remove(position);
        untrackIfNecessary(removedItem);

        // notifying about the change
        notifyItemRemoved(position);
        notifyItemDeleted(removedItem);
        notifyDatasetSizeChanged(itemCount, getItemCount());
    }




    @SuppressWarnings("unchecked")
    @Override
    public final <VHT extends VH> void addHeader(@NonNull Header<VHT> header) {
        Preconditions.nonNull(header);
        Preconditions.isTrue("The Header Item must be based on BaseItem", (header instanceof BaseItem));

        addItem((IT) header);
    }




    @Override
    public final void removeHeader() {
        if((getItemCount() > 0) && (getItem(0) instanceof Header)) {
            deleteItem(0);
        }
    }




    @SuppressWarnings("unchecked")
    @Override
    public final <VHT extends VH> void addFooter(@NonNull Footer<VHT> footer) {
        Preconditions.nonNull(footer);
        Preconditions.isTrue("The Footer Item must be based on BaseItem", (footer instanceof BaseItem));

        addItem((IT) footer);
    }




    @Override
    public final void removeFooter() {
        final int itemCount = getItemCount();

        if((itemCount > 0) && (getItem(itemCount - 1) instanceof Footer)) {
            deleteItem(itemCount - 1);
        }
    }




    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(@NonNull IT item) {
        Preconditions.nonNull(item);

        if(item instanceof Trackable) {
            return containsTrackable((Trackable<KT>) item);
        } else {
            return super.contains(item);
        }
    }




    @SuppressWarnings("unchecked")
    @Override
    public int indexOf(@NonNull IT item) {
        Preconditions.nonNull(item);

        if(item instanceof Trackable) {
            final Trackable<KT> trackable = (Trackable<KT>) item;

            if(contains(item)) {
                return getItems().indexOf(getTrackable(trackable.getTrackKey()));
            } else {
                return -1;
            }
        } else {
            return super.indexOf(item);
        }
    }




    /**
     * Adds the specified items to the Map of {@link Trackable} items.
     * The Items must implement the {@link Trackable} interface.
     *
     * @param items the item to be tracked
     */
    protected final void trackIfNecessary(@NonNull List<IT> items) {
        Preconditions.nonNull(items);

        for(IT item : items) {
            trackIfNecessary(item);
        }
    }




    /**
     * Adds the specified item to the Map of {@link Trackable} items.
     * The Item must implement the {@link Trackable} interface.
     *
     * @param item the item to be tracked
     */
    @SuppressWarnings("unchecked")
    protected final void trackIfNecessary(@NonNull IT item) {
        Preconditions.nonNull(item);

        if(item instanceof Trackable) {
            addTrackable((Trackable<KT>) item);
        }
    }




    /**
     * Removes the specified item from the map of {@link Trackable} items (if there's any).
     * The Item must implement the {@link Trackable} interface.
     *
     * @param item the item to be untracked
     */
    @SuppressWarnings("unchecked")
    protected final void untrackIfNecessary(@NonNull IT item) {
        Preconditions.nonNull(item);

        if(item instanceof Trackable) {
            removeTrackable((Trackable<KT>) item);
        }
    }




    /**
     * Adds the specified {@link Trackable} to the Map of {@link Trackable} items.
     *
     * @param trackable trackable to be added
     */
    protected final void addTrackable(@NonNull Trackable<KT> trackable) {
        Preconditions.nonNull(trackable);

        mKeyTrackableMap.put(trackable.getTrackKey(), trackable);
    }




    /**
     * Retrieves the {@link Trackable} from the Map of the {@link Trackable} items.
     *
     * @param key the {@link Trackable}'s key
     * @return the corresponding {@link Trackable}, or <strong>null</strong> if no {@link Trackable} was found.
     */
    @Nullable
    public final Trackable<KT> getTrackable(@NonNull KT key) {
        Preconditions.nonNull(key);
        return mKeyTrackableMap.get(key);
    }




    /**
     * Removes the {@link Trackable} from the Map of the {@link Trackable} items.
     *
     * @param trackable the trackable to be removed
     */
    protected final void removeTrackable(@NonNull Trackable<KT> trackable) {
        Preconditions.nonNull(trackable);

        mKeyTrackableMap.remove(trackable.getTrackKey());
    }




    /**
     * Checks whether the Map of {@link Trackable} items contains the specified {@link Trackable}.
     *
     * @param trackable
     * @return <strong>true</strong> if contains, <strong>false</strong> otherwise
     */
    protected final boolean containsTrackable(@NonNull Trackable<KT> trackable) {
        Preconditions.nonNull(trackable);
        return (mKeyTrackableMap.get(trackable.getTrackKey()) != null);
    }




    @Override
    public final void setItems(@NonNull final List<IT> items, @NonNull DiffUtil.Callback callback) {
        Preconditions.nonNull(items);
        Preconditions.nonNull(callback);

        applyDiffUtils(
            callback,
            new Runnable() {
                @Override
                public void run() {
                    setItems(items, false);
                }
            }
        );
    }




    @Override
    public final void setItems(@NonNull List<IT> items, boolean notifyAboutTheChange) {
        Preconditions.nonNull(items);

        mKeyTrackableMap.clear();
        trackIfNecessary(items);

        super.setItems(items, notifyAboutTheChange);
    }




    @Override
    public void clear() {
        final int itemCount = getItemCount();

        getItems().clear();
        mKeyTrackableMap.clear();

        // notifying about the performed event
        notifyDataSetChanged();
        notifyDatasetSizeChanged(itemCount, getItemCount());
        notifyDatasetCleared(getItems());
    }




    @Override
    public final int getItemViewType(int position, IT item) {
        return ((item != null) ? item.getLayout() : Item.VIEW_TYPE_INVALID);
    }




}
