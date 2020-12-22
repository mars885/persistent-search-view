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

package com.paulrybitskyi.persistentsearchview.sample

import android.os.Bundle
import android.text.SpannableString
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.paulrybitskyi.commons.ktx.getCompatColor
import com.paulrybitskyi.commons.ktx.set
import com.paulrybitskyi.persistentsearchview.sample.model.DemoMode
import com.paulrybitskyi.persistentsearchview.sample.utils.BrowserHandler
import com.paulrybitskyi.persistentsearchview.sample.utils.CustomLinkMovementMethod
import com.paulrybitskyi.persistentsearchview.sample.utils.CustomTabsProvider
import com.paulrybitskyi.persistentsearchview.sample.utils.SelectorSpan
import kotlinx.android.synthetic.main.main_activity_layout.*

internal class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        init()
    }


    private fun init() {
        initButtons()
        initUserIconsAcknowledgement()
    }


    private fun initButtons() {
        withoutSuggestions.setOnClickListener {
            launchActivity(DemoMode.WITHOUT_SUGGESTIONS)
        }

        recentSuggestions.setOnClickListener {
            launchActivity(DemoMode.RECENT_SUGGESTIONS)
        }

        regularSuggestions.setOnClickListener {
            launchActivity(DemoMode.REGULAR_SUGGESTIONS)
        }
    }


    private fun initUserIconsAcknowledgement() {
        val author = "Freepik"
        val text = "User icons designed by $author"
        val startIndex = text.indexOf(author)
        val endIndex = (startIndex + author.length)

        val spannableString = SpannableString(text).apply {
            set(
                startIndex,
                endIndex,
                initUserIconsAcknowledgementSpan()
            )
        }

        authorTv.movementMethod = CustomLinkMovementMethod()
        authorTv.text = spannableString
    }


    private fun initUserIconsAcknowledgementSpan(): SelectorSpan {
        return object : SelectorSpan(getCompatColor(R.color.colorLinkText)) {

            override fun onClick(view: View) {
                val browserHandler = BrowserHandler(CustomTabsProvider(this@MainActivity))
                browserHandler.launchBrowser(
                    this@MainActivity,
                    "https://www.flaticon.com/authors/freepik"
                )
            }

        }
    }


    private fun launchActivity(mode: DemoMode) {
        startActivity(DemoActivity.newInstance(this, mode))
    }


}