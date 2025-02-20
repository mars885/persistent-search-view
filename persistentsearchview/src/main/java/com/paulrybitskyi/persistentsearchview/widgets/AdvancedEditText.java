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

package com.paulrybitskyi.persistentsearchview.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * An EditText with the ability to intercept touch events.
 */
public class AdvancedEditText extends AppCompatEditText {


    private View.OnTouchListener touchEventInterceptor;


    public AdvancedEditText(Context context) {
        super(context);
    }


    public AdvancedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public AdvancedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        touchEventInterceptor = null;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if((touchEventInterceptor != null) && touchEventInterceptor.onTouch(this, event)) {
            return true;
        }

        return super.onTouchEvent(event);
    }


    /**
     * Sets an interceptor to be used inside the {@link #onTouchEvent(MotionEvent)}
     * method to intercept touch events.
     *
     * @param touchEventInterceptor The interceptor
     */
    public final void setTouchEventInterceptor(View.OnTouchListener touchEventInterceptor) {
        this.touchEventInterceptor = touchEventInterceptor;
    }


}
