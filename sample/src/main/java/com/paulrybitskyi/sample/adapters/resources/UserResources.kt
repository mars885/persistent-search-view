package com.paulrybitskyi.sample.adapters.resources

import android.content.Context
import android.graphics.drawable.Drawable
import com.arthurivanets.adapster.markers.ItemResources
import com.paulrybitskyi.sample.R
import com.paulrybitskyi.sample.utils.extensions.getCompatDrawable

class UserResources(
    val drawables: List<Drawable?>
) : ItemResources {


    companion object {


        const val DRAWABLE_FIRST_BUTTON_IDLE = 0
        const val DRAWABLE_FIRST_BUTTON_ACTIVE = 1
        const val DRAWABLE_SECOND_BUTTON_IDLE = 2
        const val DRAWABLE_SECOND_BUTTON_ACTIVE = 3


        fun init(context: Context): UserResources {
            val drawables = listOf(
                context.getCompatDrawable(R.drawable.ic_bookmark_plus),
                context.getCompatDrawable(R.drawable.ic_bookmark_check),
                context.getCompatDrawable(R.drawable.ic_account_plus),
                context.getCompatDrawable(R.drawable.ic_account_check)
            )

            return UserResources(drawables)
        }

    }


}