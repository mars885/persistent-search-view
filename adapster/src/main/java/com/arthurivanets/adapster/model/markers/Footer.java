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

package com.arthurivanets.adapster.model.markers;

import com.arthurivanets.adapster.listeners.OnItemClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * To be implemented by the Items that need to be treated as Footer.
 *
 * @param <VH> the view holder type
 * @author arthur3486
 */
public interface Footer<VH extends RecyclerView.ViewHolder> {

    /**
     * Sets the click listener responsible for handling the Footer Item click events.
     *
     * @param viewHolder
     * @param onItemClickListener
     */
    void setOnItemClickListener(@NonNull VH viewHolder, @Nullable OnItemClickListener<Footer<VH>> onItemClickListener);

}
