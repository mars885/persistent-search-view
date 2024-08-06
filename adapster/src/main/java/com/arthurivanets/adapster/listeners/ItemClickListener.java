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

import android.view.View;

/**
 * A convenience class used for the encapsulation and handling of the {@link OnItemClickListener}.
 *
 * @param <T> the item type
 * @author arthur3486
 */
public class ItemClickListener<T> implements View.OnClickListener {


    private int mPosition;
    private T mItem;

    private OnItemClickListener<T> mOnItemClickListener;




    public ItemClickListener(T item,
                             int position,
                             OnItemClickListener<T> onItemClickListener) {
        mItem = item;
        mPosition = position;
        mOnItemClickListener = onItemClickListener;
    }




    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null) {
            mOnItemClickListener.onItemClicked(v, mItem, mPosition);
        }
    }




}
