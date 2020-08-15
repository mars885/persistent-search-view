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

package com.paulrybitskyi.sample.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.browser.customtabs.CustomTabsService

/**
 * A helper class used for providing custom tabs functionality.
 */
internal class CustomTabsProvider(context: Context) {


    companion object {

        private const val STABLE_PACKAGE = "com.android.chrome"
        private const val BETA_PACKAGE = "com.chrome.beta"
        private const val DEV_PACKAGE = "com.chrome.dev"
        private const val LOCAL_PACKAGE = "com.google.android.apps.chrome"

    }


    private val context = context.applicationContext


    /**
     * A package name to use for resolving custom tabs activity.
     */
    private var packageNameToUse: String? = null


    /**
     * Returns a package name to use for custom tabs activity or null
     * if there is none.
     *
     * @return The package name to use for custom tabs or null
     * if the device does not support custom tabs
     */
    fun getPackageNameToUse(): String? {
        if(packageNameToUse != null) {
            return checkNotNull(packageNameToUse)
        }

        val packageManager = context.packageManager
        val viewIntentResolveInfoList = getViewIntentResolveInfoList()
        val packagesSupportingCustomTabs = mutableListOf<String>()

        for(resolveInfo in viewIntentResolveInfoList) {
            val serviceIntent = composeServiceIntent(resolveInfo)

            if(packageManager.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(resolveInfo.activityInfo.packageName)
            }
        }

        return findPackageNameToUse(packagesSupportingCustomTabs)
            .also { packageNameToUse = it }
    }


    private fun getViewIntentResolveInfoList(): List<ResolveInfo> {
        val packageManager = context.packageManager
        val viewIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))

        return packageManager.queryIntentActivities(viewIntent, 0)
    }


    private fun composeServiceIntent(resolveInfo: ResolveInfo): Intent {
        return Intent()
            .apply {
                action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
                `package` = resolveInfo.activityInfo.packageName
            }
    }


    private fun findPackageNameToUse(packagesSupportingCustomTabs: List<String>): String? {
        return when {
            (packagesSupportingCustomTabs.size == 1) -> packagesSupportingCustomTabs[0]
            packagesSupportingCustomTabs.contains(STABLE_PACKAGE) -> STABLE_PACKAGE
            packagesSupportingCustomTabs.contains(BETA_PACKAGE) -> BETA_PACKAGE
            packagesSupportingCustomTabs.contains(DEV_PACKAGE) -> DEV_PACKAGE
            packagesSupportingCustomTabs.contains(LOCAL_PACKAGE) -> LOCAL_PACKAGE

            else -> null
        }
    }


    /**
     * Determines whether a device supports custom tabs.
     *
     * @return true if supports; false otherwise
     */
    fun hasSupportForCustomTabs(): Boolean {
        return (getPackageNameToUse() != null)
    }


}