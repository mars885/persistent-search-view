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

import android.view.View;

import com.arthurivanets.adapster.Adapter;
import com.arthurivanets.adapster.markers.ItemResources;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An abstract class for the Dataset Item implementation.
 *
 * @param <IM> item model type
 * @param <VH> view holder type
 * @param <IR> reusable resources type
 * @author arthur3486
 */
public abstract class BaseItem<IM, VH extends BaseItem.ViewHolder<IM>, IR extends ItemResources> implements Item<VH, IR> {


    public static final long NO_ID = -1L;


    private IM mItemModel;
    private Object mTag;




    public BaseItem(IM itemModel) {
        mItemModel = itemModel;
    }




    @CallSuper
    @Override
    public void bind(@Nullable Adapter<? extends Item> adapter,
                     @NonNull VH viewHolder,
                     @Nullable IR resources) {
        viewHolder.bindData(getItemModel());
    }




    public long getId() {
        return NO_ID;
    }




    public final void setItemModel(IM itemModel) {
        mItemModel = itemModel;
    }




    public final IM getItemModel() {
        return mItemModel;
    }




    public final BaseItem setTag(Object tag) {
        mTag = tag;
        return this;
    }




    public final Object getTag() {
        return mTag;
    }




    public final boolean hasTag() {
        return (mTag != null);
    }




    @Override
    public int hashCode() {
        return ((getItemModel() != null) ? getItemModel().hashCode() : super.hashCode());
    }




    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof BaseItem) && (obj.hashCode() == hashCode()));
    }




    /**
     * An abstract Item ViewHolder class that's to be implemented by every concrete Item View Holder implementation.
     *
     * @param <Data> the type of the data represented by the host Item.
     */
    public abstract static class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private Data mBoundData;


        public ViewHolder(View itemView) {
            super(itemView);
        }


        /**
         * Binds the data to the UI.
         *
         * @param data the data item to base the binding on
         */
        @CallSuper
        public void bindData(Data data) {
            mBoundData = data;
        }


        /**
         * Retrieves the data item associated with this View Holder.
         *
         * @return the associated data item
         */
        public Data getBoundData() {
            return mBoundData;
        }


    }




}
