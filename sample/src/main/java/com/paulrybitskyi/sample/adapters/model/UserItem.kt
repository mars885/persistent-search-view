package com.paulrybitskyi.sample.adapters.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.listeners.ItemClickListener
import com.arthurivanets.adapster.listeners.OnItemClickListener
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.paulrybitskyi.sample.R
import com.paulrybitskyi.sample.adapters.resources.UserResources
import com.paulrybitskyi.sample.model.User
import com.paulrybitskyi.sample.utils.extensions.getCompatDrawable
import com.paulrybitskyi.sample.utils.extensions.setBackgroundDrawableCompat

class UserItem(itemModel: User): BaseItem<User, UserItem.ViewHolder, UserResources>(itemModel) {


    companion object {

        const val MAIN_LAYOUT = R.layout.user_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      parent: ViewGroup, inflater: LayoutInflater, resources: UserResources?): ViewHolder {
        return ViewHolder(inflater.inflate(
            MAIN_LAYOUT,
            parent,
            false
        ))
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: UserResources?) {
        super.bind(adapter, viewHolder, resources)

        val user = itemModel
        val context = viewHolder.itemView.context
        val drawables = resources!!.drawables

        viewHolder.profileImageIv.setImageDrawable(ContextCompat.getDrawable(context, user.profileImageId))

        viewHolder.usernameTv.text = user.username
        viewHolder.fullNameTv.text = user.fullName

        with(viewHolder.firstButtonIv) {
            if(user.firstState) {
                setImageDrawable(drawables[UserResources.DRAWABLE_FIRST_BUTTON_ACTIVE])
                setBackgroundDrawableCompat(context.getCompatDrawable(
                    R.drawable.user_action_button_activated_selector
                ))
            } else {
                setImageDrawable(drawables[UserResources.DRAWABLE_FIRST_BUTTON_IDLE])
                setBackgroundDrawableCompat(context.getCompatDrawable(
                    R.drawable.user_action_button_idle_selector
                ))
            }
        }

        with(viewHolder.secondButtonIv) {
            if(user.secondState) {
                setImageDrawable(drawables[UserResources.DRAWABLE_SECOND_BUTTON_ACTIVE])
                setBackgroundDrawableCompat(context.getCompatDrawable(
                    R.drawable.user_action_button_activated_selector
                ))
            } else {
                setImageDrawable(drawables[UserResources.DRAWABLE_SECOND_BUTTON_IDLE])
                setBackgroundDrawableCompat(context.getCompatDrawable(
                    R.drawable.user_action_button_idle_selector
                ))
            }
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, onItemClickListener: OnItemClickListener<UserItem>?) {
        viewHolder.contentContainerRl.setOnClickListener(ItemClickListener(this, 0, onItemClickListener))
    }


    fun setOnFirstButtonClickListener(viewHolder: ViewHolder, onItemClickListener: OnItemClickListener<UserItem>?) {
        viewHolder.firstButtonIv.setOnClickListener(ItemClickListener(this, 0, onItemClickListener))
    }


    fun setOnSecondButtonClickListener(viewHolder: ViewHolder, onItemClickListener: OnItemClickListener<UserItem>?) {
        viewHolder.secondButtonIv.setOnClickListener(ItemClickListener(this, 0, onItemClickListener))
    }


    override fun getLayout(): Int = MAIN_LAYOUT


    class ViewHolder(itemView: View) : BaseItem.ViewHolder<User>(itemView) {

        val usernameTv = itemView.findViewById<TextView>(R.id.usernameTv)
        val fullNameTv = itemView.findViewById<TextView>(R.id.fullNameTv)

        val profileImageIv = itemView.findViewById<ImageView>(R.id.profileImageIv)

        val firstButtonIv = itemView.findViewById<ImageView>(R.id.firstButtonIv)
        val secondButtonIv = itemView.findViewById<ImageView>(R.id.secondButtonIv)

        val contentContainerRl = itemView.findViewById<RelativeLayout>(R.id.contentContainerRl)

    }


}