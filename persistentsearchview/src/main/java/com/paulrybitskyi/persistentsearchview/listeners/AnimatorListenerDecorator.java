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

package com.paulrybitskyi.persistentsearchview.listeners;

import android.animation.Animator;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

/**
 * A listener decorator for the {@link AnimatorListenerAdapter}.
 */
public class AnimatorListenerDecorator extends AnimatorListenerAdapter {


    private AnimatorListenerAdapter mAnimatorListenerAdapter;


    public AnimatorListenerDecorator(@Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        super();
        mAnimatorListenerAdapter = animatorListenerAdapter;
    }


    @CallSuper
    @Override
    public void onAnimationStarted(Animator animation) {
        super.onAnimationStarted(animation);

        if(mAnimatorListenerAdapter != null) {
            mAnimatorListenerAdapter.onAnimationStarted(animation);
        }
    }


    @CallSuper
    @Override
    public void onAnimationEnded(Animator animation) {
        super.onAnimationEnded(animation);

        if(mAnimatorListenerAdapter != null) {
            mAnimatorListenerAdapter.onAnimationEnded(animation);
        }
    }


    @CallSuper
    @Override
    public void onAnimationCancelled(Animator animation) {
        super.onAnimationCancelled(animation);

        if(mAnimatorListenerAdapter != null) {
            mAnimatorListenerAdapter.onAnimationCancelled(animation);
        }
    }


    @CallSuper
    @Override
    public void onAnimationRepeated(Animator animation) {
        super.onAnimationRepeated(animation);

        if(mAnimatorListenerAdapter != null) {
            mAnimatorListenerAdapter.onAnimationRepeated(animation);
        }
    }


}
