package com.paulrybitskyi.sample.utils

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.paulrybitskyi.commons.ktx.getCompatColor
import com.paulrybitskyi.sample.R

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