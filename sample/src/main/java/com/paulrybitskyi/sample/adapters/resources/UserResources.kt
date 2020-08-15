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

package com.paulrybitskyi.sample.adapters.resources

import android.content.Context
import android.graphics.drawable.Drawable
import com.arthurivanets.adapster.markers.ItemResources
import com.paulrybitskyi.commons.ktx.getCompatDrawable
import com.paulrybitskyi.sample.R

internal class UserResources(
    val buttonDrawables: List<Drawable?>
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