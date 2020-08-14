package com.paulrybitskyi.sample.adapters.model

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
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

internal class UserItem(itemModel: User): BaseItem<User, UserItem.ViewHolder, UserResources>(itemModel) {


    companion object {

        const val MAIN_LAYOUT = R.layout.user_item_layout

    }


    override fun init(
        adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
        parent: ViewGroup, inflater: LayoutInflater,
        resources: UserResources?
    ): ViewHolder {
        return ViewHolder(
            inflater.inflate(
                MAIN_LAYOUT,
                parent,
                false
            )
        )
    }


    override fun bind(
        adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
        viewHolder: ViewHolder,
        resources: UserResources?
    ) {
        super.bind(adapter, viewHolder, resources)

        viewHolder.bindProfileImage(itemModel)
        viewHolder.bindUsername(itemModel)
        viewHolder.bindFullName(itemModel)
        viewHolder.bindButtons(
            itemModel,
            resources!!.buttonDrawables
        )
    }


    private fun ViewHolder.bindProfileImage(user: User) {
        profileImageIv.setImageResource(user.profileImageId)
    }


    private fun ViewHolder.bindUsername(user: User) {
        usernameTv.text = user.username
    }


    private fun ViewHolder.bindFullName(user: User) {
        fullNameTv.text = user.fullName
    }


    private fun ViewHolder.bindButtons(
        user: User,
        buttonDrawables: List<Drawable?>
    ) {
        bindFirstButton(user, buttonDrawables)
        bindSecondButton(user, buttonDrawables)
    }


    private fun ViewHolder.bindFirstButton(
        user: User,
        drawables: List<Drawable?>
    ) = with(firstButtonIv) {
        if(user.firstState) {
            setImageDrawable(drawables[UserResources.DRAWABLE_FIRST_BUTTON_ACTIVE])
            setBackgroundResource(R.drawable.user_action_button_activated_selector)
        } else {
            setImageDrawable(drawables[UserResources.DRAWABLE_FIRST_BUTTON_IDLE])
            setBackgroundResource(R.drawable.user_action_button_idle_selector)
        }
    }


    private fun ViewHolder.bindSecondButton(
        user: User,
        drawables: List<Drawable?>
    ) = with(secondButtonIv) {
        if(user.secondState) {
            setImageDrawable(drawables[UserResources.DRAWABLE_SECOND_BUTTON_ACTIVE])
            setBackgroundResource(R.drawable.user_action_button_activated_selector)
        } else {
            setImageDrawable(drawables[UserResources.DRAWABLE_SECOND_BUTTON_IDLE])
            setBackgroundResource(R.drawable.user_action_button_idle_selector)
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, onItemClickListener: OnItemClickListener<UserItem>?) {
        viewHolder.contentContainerRl.setOnClickListener(
            ItemClickListener(this, 0, onItemClickListener)
        )
    }


    fun setOnFirstButtonClickListener(viewHolder: ViewHolder, onItemClickListener: OnItemClickListener<UserItem>?) {
        viewHolder.firstButtonIv.setOnClickListener(
            ItemClickListener(this, 0, onItemClickListener)
        )
    }


    fun setOnSecondButtonClickListener(viewHolder: ViewHolder, onItemClickListener: OnItemClickListener<UserItem>?) {
        viewHolder.secondButtonIv.setOnClickListener(
            ItemClickListener(this, 0, onItemClickListener)
        )
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