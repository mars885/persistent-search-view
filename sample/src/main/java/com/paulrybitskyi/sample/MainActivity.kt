package com.paulrybitskyi.sample

import android.os.Bundle
import android.text.SpannableString
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.paulrybitskyi.commons.ktx.getCompatColor
import com.paulrybitskyi.commons.ktx.set
import com.paulrybitskyi.sample.model.DemoMode
import com.paulrybitskyi.sample.utils.BrowserHandler
import com.paulrybitskyi.sample.utils.CustomLinkMovementMethod
import com.paulrybitskyi.sample.utils.CustomTabsProvider
import com.paulrybitskyi.sample.utils.SelectorSpan
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