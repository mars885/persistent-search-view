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
