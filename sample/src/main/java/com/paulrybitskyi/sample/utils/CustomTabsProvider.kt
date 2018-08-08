package com.paulrybitskyi.sample.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsService

/**
 * A helper class used for providing custom tabs functionality.
 */
class CustomTabsProvider(context: Context) {


    companion object {

        private const val STABLE_PACKAGE = "com.android.chrome"
        private const val BETA_PACKAGE = "com.chrome.beta"
        private const val DEV_PACKAGE = "com.chrome.dev"
        private const val LOCAL_PACKAGE = "com.google.android.apps.chrome"

    }


    private val context: Context = context.applicationContext


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
            return packageNameToUse!!
        }

        val packageManager = context.packageManager

        // Get default VIEW intent handler
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))

        // Get all apps that an handle VIEW intents
        val resolvedActivityList = packageManager.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs: MutableList<String> = mutableListOf()

        for(info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.`package` = info.activityInfo.packageName

            if(packageManager.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        // Now packagesSupportingCustomTabs contains all apps that can handle
        // both VIEW intents and service calls
        packageNameToUse = when {
            (packagesSupportingCustomTabs.size == 1) -> packagesSupportingCustomTabs[0]
            packagesSupportingCustomTabs.contains(STABLE_PACKAGE) -> STABLE_PACKAGE
            packagesSupportingCustomTabs.contains(BETA_PACKAGE) -> BETA_PACKAGE
            packagesSupportingCustomTabs.contains(DEV_PACKAGE) -> DEV_PACKAGE
            packagesSupportingCustomTabs.contains(LOCAL_PACKAGE) -> LOCAL_PACKAGE

            else -> null
        }

        return packageNameToUse
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