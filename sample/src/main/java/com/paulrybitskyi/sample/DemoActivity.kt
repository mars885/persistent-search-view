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

package com.paulrybitskyi.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.WindowManager.LayoutParams.*
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthurivanets.adapster.listeners.OnItemClickListener
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate
import com.paulrybitskyi.sample.adapters.UsersRecyclerViewAdapter
import com.paulrybitskyi.sample.adapters.model.UserItem
import com.paulrybitskyi.sample.model.DemoMode
import com.paulrybitskyi.sample.utils.AnimationUtils
import com.paulrybitskyi.sample.utils.DataProvider
import com.paulrybitskyi.sample.utils.HeaderedRecyclerViewListener
import com.paulrybitskyi.sample.utils.VerticalSpacingItemDecorator
import kotlinx.android.synthetic.main.demo_activity_layout.*
import java.io.Serializable

internal class DemoActivity : AppCompatActivity(), View.OnClickListener {


    companion object {

        private const val EXTRA_DEMO = "demo"

        private const val SAVED_STATE_DEMO = "demo"
        private const val SAVED_STATE_DATA_PROVIDER = "data_provider"
        private const val SAVED_STATE_ITEMS = "items"


        fun newInstance(context: Context, mode: DemoMode): Intent {
            return context.intentFor<DemoActivity>()
                .apply { putExtra(SAVED_STATE_DEMO, mode) }
        }

    }


    private var mode = DemoMode.WITHOUT_SUGGESTIONS

    private var dataProvider = DataProvider()

    private var items: MutableList<UserItem> = mutableListOf()

    private lateinit var adapter: UsersRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_layout)
        onRestoreState(savedInstanceState)
        init()
    }


    private fun init() {
        initProgressBar()
        initSearchView()
        initEmptyView()
        initRecyclerView()
    }


    private fun initProgressBar() {
        progressBar.makeGone()
    }


    private fun initSearchView() = with(persistentSearchView) {
        setOnLeftBtnClickListener(this@DemoActivity)
        setOnClearInputBtnClickListener(this@DemoActivity)
        setOnRightBtnClickListener(this@DemoActivity)
        showRightButton()
        setVoiceRecognitionDelegate(VoiceRecognitionDelegate(this@DemoActivity))
        setOnSearchConfirmedListener(mOnSearchConfirmedListener)
        setOnSearchQueryChangeListener(mOnSearchQueryChangeListener)
        setOnSuggestionChangeListener(mOnSuggestionChangeListener)
        setDismissOnTouchOutside(true)
        setDimBackground(true)
        isProgressBarEnabled = true
        isVoiceInputButtonEnabled = true
        isClearInputButtonEnabled = true
        setSuggestionsDisabled(mode == DemoMode.WITHOUT_SUGGESTIONS)
        setQueryInputGravity(Gravity.START or Gravity.CENTER)
    }


    private fun initEmptyView() {
        emptyViewLl.isVisible = items.isEmpty()
    }


    private fun initRecyclerView() = with(recyclerView) {
        layoutManager = initLayoutManager()
        adapter = initAdapter()

        addItemDecoration(initVerticalSpacingItemDecorator())
        addOnScrollListener(initHeaderedRecyclerViewListener())
    }


    private fun initLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(this)
    }


    private fun initAdapter(): UsersRecyclerViewAdapter {
        return UsersRecyclerViewAdapter(this, items)
            .apply {
                mOnItemClickListener = OnItemClickListener { _, item, _ ->
                    showToast("Username: ${item.itemModel.username}.")
                }
                mOnFirstButtonClickListener = OnItemClickListener { _, _, _ ->
                    showToast("First button clicked.")
                }
                mOnSecondButtonClickListener = OnItemClickListener { _, _, _ ->
                    showToast("Second button clicked.")
                }
            }
            .also { adapter = it }
    }


    private fun initVerticalSpacingItemDecorator(): VerticalSpacingItemDecorator {
        return VerticalSpacingItemDecorator(
            verticalSpacing = 2.dpToPx(this),
            verticalSpacingCompensation = 2.dpToPx(this)
        )
    }


    private fun initHeaderedRecyclerViewListener(): HeaderedRecyclerViewListener {
        return object : HeaderedRecyclerViewListener(this@DemoActivity) {

            override fun showHeader() {
                AnimationUtils.showHeader(persistentSearchView)
            }

            override fun hideHeader() {
                AnimationUtils.hideHeader(persistentSearchView)
            }

        }
    }


    private fun loadInitialDataIfNecessary() {
        if(mode == DemoMode.WITHOUT_SUGGESTIONS) {
            return
        }

        val searchQueries = if(persistentSearchView.isInputQueryEmpty) {
            dataProvider.getInitialSearchQueries()
        } else {
            dataProvider.getSuggestionsForQuery(persistentSearchView.inputQuery)
        }

        setSuggestions(searchQueries, false)
    }


    private fun performSearch(query: String) {
        emptyViewLl.makeGone()
        recyclerView.alpha = 0f
        progressBar.makeVisible()
        adapter.clear()

        items = dataProvider.generateUsers(query, 100)
                .map(::UserItem)
                .toMutableList()

        val runnable = Runnable {
            persistentSearchView.hideProgressBar(false)
            persistentSearchView.showLeftButton()

            adapter.items = items
            progressBar.makeGone()
            recyclerView.animate()
                .alpha(1f)
                .setInterpolator(LinearInterpolator())
                .setDuration(300L)
                .start()
        }

        Handler().postDelayed(runnable, 1000L)

        persistentSearchView.hideLeftButton(false)
        persistentSearchView.showProgressBar()
    }


    private fun saveSearchQueryIfNecessary(query: String) {
        if(canSaveQuery()) {
            dataProvider.saveSearchQuery(query)
        }
    }


    private fun setSuggestions(queries: List<String>, expandIfNecessary: Boolean) {
        if(mode == DemoMode.WITHOUT_SUGGESTIONS) {
            return
        }

        val suggestions: List<SuggestionItem> = when(mode) {
            DemoMode.RECENT_SUGGESTIONS -> SuggestionCreationUtil.asRecentSearchSuggestions(queries)
            DemoMode.REGULAR_SUGGESTIONS -> SuggestionCreationUtil.asRegularSearchSuggestions(queries)

            else -> throw IllegalStateException()
        }

        persistentSearchView.setSuggestions(suggestions, expandIfNecessary)
    }


    private fun canSaveQuery(): Boolean {
        return when(mode) {
            DemoMode.RECENT_SUGGESTIONS -> true

            else -> false
        }
    }


    override fun onResume() {
        super.onResume()

        loadInitialDataIfNecessary()

        if(shouldExpandSearchView()) {
            persistentSearchView.expand(false)
            window.setSoftInputMode(SOFT_INPUT_STATE_VISIBLE or SOFT_INPUT_ADJUST_NOTHING)
        } else {
            window.setSoftInputMode(SOFT_INPUT_STATE_HIDDEN or SOFT_INPUT_ADJUST_NOTHING)
        }
    }


    private fun shouldExpandSearchView(): Boolean {
        return (
            (persistentSearchView.isInputQueryEmpty && (adapter.itemCount == 0)) ||
            persistentSearchView.isExpanded
        )
    }


    override fun onBackPressed() {
        if(persistentSearchView.isExpanded) {
            persistentSearchView.collapse()
            return
        }

        super.onBackPressed()
    }


    override fun onClick(view: View) {
        when(view.id) {
            R.id.leftBtnIv -> onLeftButtonClicked()
            R.id.clearInputBtnIv -> onClearInputButtonClicked()
            R.id.rightBtnIv -> onRightButtonClicked()
        }
    }


    private fun onLeftButtonClicked() {
        onBackPressed()
    }


    private fun onClearInputButtonClicked() {
        //
    }


    private fun onRightButtonClicked() {
        showToast("Right button clicked.")
    }


    private val mOnSearchConfirmedListener = OnSearchConfirmedListener { searchView, query ->
        saveSearchQueryIfNecessary(query)

        searchView.collapse()
        performSearch(query)
    }


    private val mOnSearchQueryChangeListener = OnSearchQueryChangeListener { searchView, oldQuery, newQuery ->
        setSuggestions(
            if(newQuery.isBlank()) {
                dataProvider.getInitialSearchQueries()
            } else {
                dataProvider.getSuggestionsForQuery(newQuery)
            },
            true
        )
    }


    private val mOnSuggestionChangeListener = object : OnSuggestionChangeListener {

        override fun onSuggestionPicked(suggestion: SuggestionItem) {
            val query = suggestion.itemModel.text

            saveSearchQueryIfNecessary(query)
            setSuggestions(dataProvider.getSuggestionsForQuery(query), false)
            performSearch(query)
        }

        override fun onSuggestionRemoved(suggestion: SuggestionItem) {
            dataProvider.removeSearchQuery(suggestion.itemModel.text)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data)
    }


    @Suppress("UNCHECKED_CAST")
    private fun onRestoreState(savedState: Bundle?) {
        if(savedState != null) {
            mode = savedState.getSerializableOrThrow(SAVED_STATE_DEMO)
            dataProvider = savedState.getSerializableOrThrow(SAVED_STATE_DATA_PROVIDER)
            items = savedState.getSerializableOrThrow(SAVED_STATE_ITEMS)
        } else {
            mode = intent.getSerializableExtraOrThrow(EXTRA_DEMO)
        }
    }


    override fun onSaveInstanceState(savedState: Bundle) {
        super.onSaveInstanceState(savedState)

        with(savedState) {
            putSerializable(SAVED_STATE_DEMO, mode)
            putSerializable(SAVED_STATE_DATA_PROVIDER, dataProvider)
            putSerializable(SAVED_STATE_ITEMS, (items as Serializable))
        }
    }


}