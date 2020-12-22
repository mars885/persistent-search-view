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

package com.paulrybitskyi.persistentsearchview.sample.utils

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.paulrybitskyi.commons.ktx.getCompatColor
import com.paulrybitskyi.persistentsearchview.sample.R

/**
 * A helper class used for providing browser-related functionality.
 */
internal class BrowserHandler(private val customTabsProvider: CustomTabsProvider) {


    /**
     * Launches a browser (either Chrome Custom Tabs or a standard web-view
     * if Chrome is not installed on the device) to view a specific url.
     *
     * @param url The url to view
     */
    fun launchBrowser(context: Context, url: String) {
        val uri = url.toUri()

        if(customTabsProvider.hasSupportForCustomTabs()) {
            val intentBuilder = CustomTabsIntent.Builder()
            intentBuilder.setToolbarColor(context.getCompatColor(R.color.colorPrimary))
            intentBuilder.setSecondaryToolbarColor(context.getCompatColor(R.color.colorPrimaryDark))
            intentBuilder.setShowTitle(true)

            val customTabsIntent = intentBuilder.build()
            customTabsIntent.intent.`package` = customTabsProvider.getPackageNameToUse()
            customTabsIntent.launchUrl(context, uri)
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri

            context.startActivity(intent)
        }
    }


}