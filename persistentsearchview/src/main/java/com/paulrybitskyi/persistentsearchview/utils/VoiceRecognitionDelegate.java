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

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;

import com.paulrybitskyi.persistentsearchview.PersistentSearchView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * A utility class used for delegating voice recognition related
 * work to.
 */
public class VoiceRecognitionDelegate {


    private static final int REQUEST_CODE_VOICE_INPUT = 9999;


    private Activity activity;
    private AppCompatActivity supportActivity;

    private Fragment fragment;
    private androidx.fragment.app.Fragment supportFragment;


    public VoiceRecognitionDelegate(@NonNull Activity activity) {
        Preconditions.nonNull(activity);

        this.activity = activity;
    }


    public VoiceRecognitionDelegate(@NonNull AppCompatActivity activity) {
        Preconditions.nonNull(activity);

        this.supportActivity = activity;
    }


    public VoiceRecognitionDelegate(@NonNull Fragment fragment) {
        Preconditions.nonNull(fragment);

        this.fragment = fragment;
    }


    public VoiceRecognitionDelegate(@NonNull androidx.fragment.app.Fragment fragment) {
        Preconditions.nonNull(fragment);

        this.supportFragment = fragment;
    }


    /**
     * Tries to open the speech recognizer on user's device.
     */
    public final void openSpeechRecognizer() {
        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Utils.getLocale(getContext()));

        if(activity != null) {
            activity.startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
        } else if(supportActivity != null) {
            supportActivity.startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
        } else if(fragment != null) {
            fragment.startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
        } else if(supportFragment != null) {
            supportFragment.startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
        }
    }


    @SuppressWarnings("NewApi")
    private Context getContext() {
        if(activity != null) {
            return activity;
        } else if(supportActivity != null) {
            return supportActivity;
        } else if(fragment != null) {
            return fragment.getContext();
        } else if(supportFragment != null) {
            return supportFragment.getContext();
        } else {
            throw new IllegalStateException("Could not get context in VoiceRecognitionDelegate.");
        }
    }


    /**
     * Handles the speech recognizer results.
     *
     * @param searchView The search view to populate with the result
     * @param requestCode The request code
     * @param resultCode The result code
     * @param data The speech recognizer's data
     */
    public static void handleResult(
        @NonNull PersistentSearchView searchView,
        int requestCode,
        int resultCode,
        Intent data
    ) {
        Preconditions.nonNull(searchView);

        if(requestCode == REQUEST_CODE_VOICE_INPUT) {
            if((resultCode == Activity.RESULT_OK) && (data != null)) {
                final List<String> receivedData = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if((receivedData != null) && (receivedData.size() > 0)) {
                    searchView.setInputQuery(receivedData.get(0));
                    searchView.confirmSearchAction();
                }
            }
        }
    }


}
