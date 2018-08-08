package com.paulrybitskyi.sample.adapters

import android.content.Context
import com.arthurivanets.adapster.listeners.OnItemClickListener
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.paulrybitskyi.sample.adapters.model.UserItem
import com.paulrybitskyi.sample.adapters.resources.UserResources

class UsersRecyclerViewAdapter(
    context: Context,
    items: MutableList<UserItem>
) : TrackableRecyclerViewAdapter<Int, UserItem, UserItem.ViewHolder>(context, items) {


    private val mResources: UserResources = UserResources.init(context)

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
        return mResources
    }


}