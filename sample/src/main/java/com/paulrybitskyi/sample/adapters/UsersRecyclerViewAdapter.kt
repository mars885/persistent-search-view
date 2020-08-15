/*
 * Copyright 2017 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.sample.adapters

import android.content.Context
import com.arthurivanets.adapster.listeners.OnItemClickListener
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.paulrybitskyi.sample.adapters.model.UserItem
import com.paulrybitskyi.sample.adapters.resources.UserResources

internal class UsersRecyclerViewAdapter(
    context: Context,
    items: MutableList<UserItem>
) : TrackableRecyclerViewAdapter<Int, UserItem, UserItem.ViewHolder>(context, items) {


    private val resources = UserResources.init(context)

    var mOnItemClickListener: OnItemClickListener<UserItem>? = null

    var mOnFirstButtonClickListener: OnItemClickListener<UserItem>? = null
    var mOnSecondButtonClickListener: OnItemClickListener<UserItem>? = null




    override fun assignListeners(holder: UserItem.ViewHolder, position: Int, item: UserItem) {
        super.assignListeners(holder, position, item)

        with(item) {
            setOnItemClickListener(holder, mOnItemClickListener)
            setOnFirstButtonClickListener(holder, mOnFirstButtonClickListener)
            setOnSecondButtonClickListener(holder, mOnSecondButtonClickListener)
        }
    }


    override fun getResources(): ItemResources? {
        return resources
    }


}