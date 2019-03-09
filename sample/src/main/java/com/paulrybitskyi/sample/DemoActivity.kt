package com.paulrybitskyi.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.arthurivanets.adapster.listeners.OnItemClickListener
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate
import com.paulrybitskyi.sample.adapters.UsersRecyclerViewAdapter
import com.paulrybitskyi.sample.adapters.model.UserItem
import com.paulrybitskyi.sample.model.DemoModes
import com.paulrybitskyi.sample.utils.AnimationUtils
import com.paulrybitskyi.sample.utils.DataProvider
import com.paulrybitskyi.sample.utils.HeaderedRecyclerViewListener
import com.paulrybitskyi.sample.utils.VerticalSpacingItemDecorator
import com.paulrybitskyi.sample.utils.extensions.dpToPx
import com.paulrybitskyi.sample.utils.extensions.makeGone
import com.paulrybitskyi.sample.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.demo_activity_layout.*
import java.io.Serializable

class DemoActivity : AppCompatActivity(), View.OnClickListener {


    companion object {

        private const val EXTRA_DEMO = "demo"

        private const val SAVED_STATE_DEMO = "demo"
        private const val SAVED_STATE_DATA_PROVIDER = "data_provider"
        private const val SAVED_STATE_ITEMS = "items"


        fun newInstance(context: Context, mode: DemoModes): Intent {
            return Intent(context, DemoActivity::class.java).apply {
                putExtra(SAVED_STATE_DEMO, mode)
            }
        }

    }


    private var mMode: DemoModes = DemoModes.WITHOUT_SUGGESTIONS


    private var mDataProvider: DataProvider = DataProvider()


    private var mItems: MutableList<UserItem> = mutableListOf()


    private lateinit var mAdapter: UsersRecyclerViewAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_layout)
        onRestoreState(savedInstanceState)

        initProgressBar()
        initSearchView()
        initEmptyView()
        initRecyclerView()
    }


    private fun initProgressBar() {
        progressBar.makeGone()
    }


    private fun initSearchView() {
        with(persistentSearchView) {
            setOnLeftBtnClickListener(this@DemoActivity)
            setOnClearInputBtnClickListener(this@DemoActivity)
            setOnRightBtnClickListener(this@DemoActivity)
            showRightButton()
            setVoiceRecognitionDelegate(VoiceRecognitionDelegate(this@DemoActivity))
            setOnSearchConfirmedListener(mOnSearchConfirmedListener)
            setOnSearchQueryChangeListener(mOnSearchQueryChangeListener)
            setOnSuggestionChangeListener(mOnSuggestionChangeListener)
            setSuggestionsDisabled(mMode == DemoModes.WITHOUT_SUGGESTIONS)
        }
    }


    private fun initEmptyView() {
        if(mItems.isNotEmpty()) {
            emptyViewLl.visibility = if(mItems.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }


    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(VerticalSpacingItemDecorator(dpToPx(2), dpToPx(2)))

        mAdapter = UsersRecyclerViewAdapter(this, mItems)
        mAdapter.mOnItemClickListener = OnItemClickListener { _, item, _ ->
            showToast("Username: ${item.itemModel.username}.")
        }
        mAdapter.mOnFirstButtonClickListener = OnItemClickListener { _, _, _ ->
            showToast("First button clicked.")
        }
        mAdapter.mOnSecondButtonClickListener = OnItemClickListener { _, _, _ ->
            showToast("Second button clicked.")
        }

        recyclerView.adapter = mAdapter
        recyclerView.addOnScrollListener(object : HeaderedRecyclerViewListener(this@DemoActivity) {

            override fun showHeader() {
                AnimationUtils.showHeader(persistentSearchView)
            }

            override fun hideHeader() {
                AnimationUtils.hideHeader(persistentSearchView)
            }

        })
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun loadInitialDataIfNecessary() {
        if(mMode == DemoModes.WITHOUT_SUGGESTIONS) {
            return
        }

        val searchQueries = if(persistentSearchView.isInputQueryEmpty) {
            mDataProvider.getInitialSearchQueries()
        } else {
            mDataProvider.getSuggestionsForQuery(persistentSearchView.inputQuery)
        }

        setSuggestions(searchQueries, false)
    }


    private fun performSearch(query: String) {
        emptyViewLl.makeGone()
        recyclerView.alpha = 0f
        progressBar.makeVisible()
        mAdapter.clear()

        mItems = mDataProvider.generateUsers(query, 100)
                .map { UserItem(it) }
                .toMutableList()

        val runnable = Runnable {
            mAdapter.items = mItems
            progressBar.makeGone()
            recyclerView.animate()
                    .alpha(1f)
                    .setInterpolator(LinearInterpolator())
                    .setDuration(300L)
                    .start()
        }

        Handler().postDelayed(runnable, 1000L)
    }


    private fun saveSearchQueryIfNecessary(query: String) {
        if(canSaveQuery()) {
            mDataProvider.saveSearchQuery(query)
        }
    }


    private fun setSuggestions(queries: List<String>, expandIfNecessary: Boolean) {
        if(mMode == DemoModes.WITHOUT_SUGGESTIONS) {
            return
        }

        val suggestions: List<SuggestionItem> = when(mMode) {
            DemoModes.RECENT_SUGGESTIONS -> SuggestionCreationUtil.asRecentSearchSuggestions(queries)
            DemoModes.REGULAR_SUGGESTIONS -> SuggestionCreationUtil.asRegularSearchSuggestions(queries)

            else -> throw IllegalStateException()
        }

        persistentSearchView.setSuggestions(suggestions, expandIfNecessary)
    }


    private fun canSaveQuery(): Boolean {
        return when(mMode) {
            DemoModes.RECENT_SUGGESTIONS -> true

            else -> false
        }
    }


    override fun onResume() {
        super.onResume()

        loadInitialDataIfNecessary()

        if((persistentSearchView.isInputQueryEmpty && (mAdapter.itemCount == 0)) || persistentSearchView.isExpanded) {
            persistentSearchView.expand(false)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
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
        setSuggestions(if(newQuery.isBlank()) {
            mDataProvider.getInitialSearchQueries()
        } else {
            mDataProvider.getSuggestionsForQuery(newQuery)
        }, true)
    }


    private val mOnSuggestionChangeListener = object : OnSuggestionChangeListener {

        override fun onSuggestionPicked(suggestion: SuggestionItem) {
            val query = suggestion.itemModel.text

            saveSearchQueryIfNecessary(query)
            setSuggestions(mDataProvider.getSuggestionsForQuery(query), false)
            performSearch(query)
        }

        override fun onSuggestionRemoved(suggestion: SuggestionItem) {
            mDataProvider.removeSearchQuery(suggestion.itemModel.text)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data)
    }


    @Suppress("UNCHECKED_CAST")
    private fun onRestoreState(savedState: Bundle?) {
        if(savedState != null) {
            mMode = (savedState.getSerializable(SAVED_STATE_DEMO) as DemoModes)
            mDataProvider = (savedState.getSerializable(SAVED_STATE_DATA_PROVIDER) as DataProvider)
            mItems = (savedState.getSerializable(SAVED_STATE_ITEMS) as MutableList<UserItem>)
        } else {
            mMode = (intent.getSerializableExtra(EXTRA_DEMO) as DemoModes)
        }
    }


    override fun onSaveInstanceState(savedState: Bundle) {
        super.onSaveInstanceState(savedState)

        with(savedState) {
            putSerializable(SAVED_STATE_DEMO, mMode)
            putSerializable(SAVED_STATE_DATA_PROVIDER, mDataProvider)
            putSerializable(SAVED_STATE_ITEMS, (mItems as Serializable))
        }
    }


}