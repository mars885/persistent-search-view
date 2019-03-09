package com.paulrybitskyi.sample

import android.os.Bundle
import android.text.SpannableString
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.paulrybitskyi.sample.model.DemoModes
import com.paulrybitskyi.sample.utils.BrowserHandler
import com.paulrybitskyi.sample.utils.CustomLinkMovementMethod
import com.paulrybitskyi.sample.utils.CustomTabsProvider
import com.paulrybitskyi.sample.utils.SelectorSpan
import com.paulrybitskyi.sample.utils.extensions.getCompatColor
import kotlinx.android.synthetic.main.main_activity_layout.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)

        initButtons()
        initUserIconsAcknowledgement()
    }


    private fun initButtons() {
        withoutSuggestions.setOnClickListener {
            launchActivity(DemoModes.WITHOUT_SUGGESTIONS)
        }

        recentSuggestions.setOnClickListener {
            launchActivity(DemoModes.RECENT_SUGGESTIONS)
        }

        regularSuggestions.setOnClickListener {
            launchActivity(DemoModes.REGULAR_SUGGESTIONS)
        }
    }


    private fun initUserIconsAcknowledgement() {
        val author = "Freepik"
        val text = "User icons designed by $author"

        val spannableString = SpannableString(text)
        spannableString.setSpan(
            object : SelectorSpan(
                getCompatColor(R.color.colorLinkNormalBackground),
                getCompatColor(R.color.colorLinkSelectedBackground)
            ) {

                override fun onClick(view: View) {
                    val browserHandler = BrowserHandler(CustomTabsProvider(this@MainActivity))
                    browserHandler.launchBrowser(
                            this@MainActivity,
                            "https://www.flaticon.com/authors/freepik"
                    )
                }

            },
            text.indexOf(author),
            text.indexOf(author) + author.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        authorTv.movementMethod = CustomLinkMovementMethod()
        authorTv.text = spannableString
    }


    private fun launchActivity(mode: DemoModes) {
        startActivity(DemoActivity.newInstance(this, mode))
    }


}