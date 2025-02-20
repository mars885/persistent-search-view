/*
 * Copyright 2017 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.persistentsearchview.utils;

import android.os.Parcelable;
import android.view.View;

public final class StateUtils {


    private StateUtils() {}


    public static Parcelable fetchParentState(Parcelable state) {
        if(state == null) {
            return null;
        }

        if(Utils.IS_AT_LEAST_MARSHMALLOW) {
            return state;
        } else {
            return View.BaseSavedState.EMPTY_STATE;
        }
    }


}
