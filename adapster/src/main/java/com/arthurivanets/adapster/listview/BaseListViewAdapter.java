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

package com.arthurivanets.adapster.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.arthurivanets.adapster.Adapter;
import com.arthurivanets.adapster.listeners.OnDatasetChangeListener;
import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.arthurivanets.adapster.markers.SupportsFooter;
import com.arthurivanets.adapster.markers.SupportsHeader;
import com.arthurivanets.adapster.model.BaseItem;
import com.arthurivanets.adapster.model.markers.Footer;
import com.arthurivanets.adapster.model.markers.Header;
import com.arthurivanets.adapster.markers.ItemResources;
import com.arthurivanets.adapster.util.Preconditions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A base class for the implementation of any ListView adapter.
 *
 * @param <IT> the dataset item type
 * @param <VH> the item view holder type
 * @author arthur3486
 */
public abstract class BaseListViewAdapter<IT extends BaseItem, VH extends BaseItem.ViewHolder<?>> extends ArrayAdapter<IT> implements Adapter<IT>,
        SupportsHeader<VH>, SupportsFooter<VH> {


    private List<IT> mItems;

    private final LayoutInflater mLayoutInflater;
    private final Set<OnDatasetChangeListener<List<IT>, IT>> mOnDatasetChangeListeners;

    private OnItemClickListener<Header<VH>> mOnHeaderClickListener;
    private OnItemClickListener<Footer<VH>> mOnFooterClickListener;




    public BaseListViewAdapter(@NonNull Context context, @NonNull List<IT> items) {
        super(context, 0, items);

        Preconditions.nonNull(context);
        Preconditions.nonNull(items);

        mItems = items;
        mLayoutInflater = LayoutInflater.from(context);
        mOnDatasetChangeListeners = new HashSet<>();
    }




    @Override
    public final void addItem(@NonNull IT item) {
        addItem(item, true);
    }




    @Override
    public final void addItem(@NonNull IT item, boolean notifyAboutTheChange) {
        Preconditions.nonNull(item);

        addItem(mItems.size(), item, notifyAboutTheChange);
    }




    @Override
    public final void addItem(int position, @NonNull IT item) {
        addItem(position, item, true);
    }




    @Override
    public final void addOrUpdateItem(@NonNull IT item) {
        addOrUpdateItem(item, true);
    }




    @Override
    public final void addOrUpdateItem(@NonNull IT item, boolean notifyAboutTheChange) {
        Preconditions.nonNull(item);

        addOrUpdateItem(mItems.size(), item, notifyAboutTheChange);
    }




    @Override
    public final void addOrUpdateItem(int position, @NonNull IT item) {
        addOrUpdateItem(position, item, true);
    }




    @Override
    public final void updateItem(@NonNull IT item) {
        notifyDataSetChanged();
    }




    @Override
    public void updateItem(int position) {
        notifyDataSetChanged();
    }




    @Override
    public final void updateItemWith(@NonNull IT item) {
        updateItemWith(item, true);
    }




    @Override
    public final void updateItemWith(@NonNull IT item, boolean notifyAboutTheChange) {
        final int itemIndex = indexOf(item);

        if(itemIndex != -1) {
            updateItemWith(itemIndex, item, notifyAboutTheChange);
        }
    }




    @Override
    public final void updateItemWith(int position, @NonNull IT item) {
        updateItemWith(position, item, true);
    }




    @Override
    public final void deleteItem(@NonNull IT item) {
        final int itemIndex = indexOf(item);

        if(itemIndex != -1) {
            deleteItem(itemIndex);
        }
    }




    /**
     *
     * <br>
     *      Implements the default functionality. For more complex presence checks
     *      (like with a dedicated HashMap for Item tracking), please make sure
     *      to override this method and add the necessary functionality.
     * <br>
     *
     */
    @Override
    public boolean contains(@NonNull IT item) {
        Preconditions.nonNull(item);
        return mItems.contains(item);
    }




    /**
     *
     * <br>
     *      Implements the default functionality. For more complex index determining
     *      (like with a dedicated HashMap for Item tracking), please make sure
     *      to override this method and add the necessary functionality.
     * <br>
     *
     */
    @Override
    public int indexOf(@NonNull IT item) {
        Preconditions.nonNull(item);
        return mItems.indexOf(item);
    }




    @Override
    public final int lastIndex() {
        return (mItems.size() - 1);
    }




    /**
     * Notifies the Dataset Change Observers about the addition of a new item.
     *
     * @param item the added item
     */
    protected final void notifyItemAdded(@NonNull IT item) {
        Preconditions.nonNull(item);

        for(OnDatasetChangeListener<List<IT>, IT> listener : mOnDatasetChangeListeners) {
            listener.onItemAdded(mItems, item);
        }
    }




    /**
     * Notifies the Dataset Change Observers about the item update.
     *
     * @param item the updated item
     */
    protected final void notifyItemUpdated(@NonNull IT item) {
        Preconditions.nonNull(item);

        for(OnDatasetChangeListener<List<IT>, IT> listener : mOnDatasetChangeListeners) {
            listener.onItemUpdated(mItems, item);
        }
    }




    /**
     * Notifies the Dataset Change Observers about the replacement of the item.
     *
     * @param oldItem the old item that got replaced
     * @param newItem the new replacement item
     */
    protected final void notifyItemReplaced(@NonNull IT oldItem, @NonNull IT newItem) {
        Preconditions.nonNull(oldItem);
        Preconditions.nonNull(newItem);

        for(OnDatasetChangeListener<List<IT>, IT> listener : mOnDatasetChangeListeners) {
            listener.onItemReplaced(mItems, oldItem, newItem);
        }
    }




    /**
     * Notifies the Dataset Change Observers about the deletion of the item.
     *
     * @param item the deleted item
     */
    protected final void notifyItemDeleted(@NonNull IT item) {
        Preconditions.nonNull(item);

        for(OnDatasetChangeListener<List<IT>, IT> listener : mOnDatasetChangeListeners) {
            listener.onItemDeleted(mItems, item);
        }
    }




    /**
     * Notifies the Dataset Change Observers about the Dataset size changes.
     *
     * @param oldSize
     * @param newSize
     */
    protected final void notifyDatasetSizeChanged(int oldSize, int newSize) {
        for(OnDatasetChangeListener<List<IT>, IT> listener : mOnDatasetChangeListeners) {
            listener.onDatasetSizeChanged(oldSize, newSize);
        }
    }




    /**
     * Notifies the Dataset Change Observers about the replacement of the dataset.
     *
     * @param newDataset the new replacement dataset
     */
    protected final void notifyDatasetReplaced(@NonNull List<IT> newDataset) {
        Preconditions.nonNull(newDataset);

        for(OnDatasetChangeListener<List<IT>, IT> listener : mOnDatasetChangeListeners) {
            listener.onDatasetReplaced(newDataset);
        }
    }




    /**
     * Notifies the Dataset Change Observers about the clearing of the dataset.
     *
     * @param dataset the cleared dataset
     */
    protected final void notifyDatasetCleared(@NonNull List<IT> dataset) {
        Preconditions.nonNull(dataset);

        for(OnDatasetChangeListener<List<IT>, IT> listener : mOnDatasetChangeListeners) {
            listener.onDatasetCleared(dataset);
        }
    }




    /**
     *  Performs the Item View(and ViewHolder) initialization.
     */
    @SuppressWarnings("unchecked")
    protected VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType, @NonNull IT item) {
        return (VH) item.init(this, parent, mLayoutInflater, getResources());
    }




    @SuppressWarnings("unchecked")
    @CallSuper
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final IT item = getItem(position);

        //performing the data binding
        item.bind(this, holder, getResources());

        //allowing the extenders to assign listeners(if necessary)
        assignListeners(holder, position, item);
    }




    /**
     *  Gets called when it's the right time to assign the Listeners to the
     *  corresponding item. Override it only if you need to provide the Listener settings functionality.
     */
    @SuppressWarnings("unchecked")
    @CallSuper
    protected void assignListeners(@NonNull VH holder, int position, @NonNull IT item) {
        //assigning the default listeners
        if(item instanceof Header) {
            ((Header<VH>) item).setOnItemClickListener(holder, mOnHeaderClickListener);
        } else if(item instanceof Footer) {
            ((Footer<VH>) item).setOnItemClickListener(holder, mOnFooterClickListener);
        }
    }




    @Override
    public final void addOnDatasetChangeListener(@NonNull OnDatasetChangeListener<List<IT>, IT> onDatasetChangeListener) {
        Preconditions.nonNull(onDatasetChangeListener);

        mOnDatasetChangeListeners.add(onDatasetChangeListener);
    }




    @Override
    public final void removeOnDatasetChangeListener(@NonNull OnDatasetChangeListener<List<IT>, IT> onDatasetChangeListener) {
        Preconditions.nonNull(onDatasetChangeListener);

        mOnDatasetChangeListeners.remove(onDatasetChangeListener);
    }




    @Override
    public final void removeAllOnDatasetChangeListeners() {
        mOnDatasetChangeListeners.clear();
    }




    @Override
    public final void setOnHeaderClickListener(OnItemClickListener<Header<VH>> onHeaderClickListener) {
        mOnHeaderClickListener = onHeaderClickListener;
    }




    @Override
    public final void setOnFooterClickListener(OnItemClickListener<Footer<VH>> onFooterClickListener) {
        mOnFooterClickListener = onFooterClickListener;
    }




    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public final View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final IT item = getItem(position);
        final VH viewHolder;

        // creating/restoring the ViewHolder
        if(convertView == null) {
            viewHolder = onCreateViewHolder(
                parent,
                getItemViewType(position),
                item
            );

            convertView = viewHolder.itemView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VH) convertView.getTag();
        }

        // performing the data binding
        onBindViewHolder(viewHolder, position);

        return convertView;
    }




    @NonNull
    @Override
    public final View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }




    @CallSuper
    @Override
    public void setItems(@NonNull List<IT> items) {
        setItems(items, true);
    }




    @CallSuper
    @Override
    public void setItems(@NonNull List<IT> items, boolean notifyAboutTheChange) {
        Preconditions.nonNull(items);

        final int itemCount = getItemCount();
        mItems = items;

        if(notifyAboutTheChange) {
            notifyDataSetChanged();
        }

        notifyDatasetReplaced(mItems);
        notifyDatasetSizeChanged(itemCount, getItemCount());
    }




    @NonNull
    @Override
    public final List<IT> getItems() {
        return mItems;
    }




    @Nullable
    @Override
    public IT getItem(int position) {
        return (((position >= 0) && (position < getCount())) ? mItems.get(position) : null);
    }




    @Override
    public final IT getFirstItem() {
        return getItem(0);
    }




    @Override
    public final IT getLastItem() {
        return getItem(lastIndex());
    }




    @Override
    public long getItemId(int position) {
        final long itemId = getItem(position).getId();
        return ((itemId != BaseItem.NO_ID) ? itemId : position);
    }




    @Override
    public final int getItemViewType(int position) {
        return getItemViewType(position, getItem(position));
    }




    public abstract int getItemViewType(int position, IT item);




    @Override
    public final int getCount() {
        return mItems.size();
    }




    @Override
    public final int getItemCount() {
        return getCount();
    }




    @NonNull
    protected final LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }




    /**
     * Retrieves the reusable {@link ItemResources} associated with the current adapter.
     *
     * @return the reusable {@link ItemResources} associated with the current adapter, or <strong>null</strong> if no resources have been associated.
     */
    @Nullable
    public ItemResources getResources() {
        return null;
    }




}
