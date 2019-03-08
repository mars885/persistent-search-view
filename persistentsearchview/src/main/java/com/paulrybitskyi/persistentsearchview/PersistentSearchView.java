/*
 * Copyright 2017
 *
 * Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 * Arthur Ivanets, arthur.ivanets.l@gmail.com
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

package com.paulrybitskyi.persistentsearchview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.paulrybitskyi.persistentsearchview.adapters.SuggestionsRecyclerViewAdapter;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.adapters.resources.SuggestionItemResources;
import com.paulrybitskyi.persistentsearchview.animations.BackgroundDimmingAnimation;
import com.paulrybitskyi.persistentsearchview.listeners.AnimatorListenerAdapter;
import com.paulrybitskyi.persistentsearchview.listeners.AnimatorListenerDecorator;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener;
import com.paulrybitskyi.persistentsearchview.listeners.QueryListener;
import com.paulrybitskyi.persistentsearchview.utils.AnimationType;
import com.paulrybitskyi.persistentsearchview.utils.KeyboardManagingUtil;
import com.paulrybitskyi.persistentsearchview.utils.Preconditions;
import com.paulrybitskyi.persistentsearchview.utils.Utils;
import com.paulrybitskyi.persistentsearchview.utils.ViewUtils;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;
import com.paulrybitskyi.persistentsearchview.widgets.AdvancedEditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.paulrybitskyi.persistentsearchview.utils.Utils.IS_AT_LEAST_LOLLIPOP;
import static com.paulrybitskyi.persistentsearchview.utils.Utils.TOOLBAR_TITLE_TYPEFACE;
import static com.paulrybitskyi.persistentsearchview.utils.Utils.adjustColorAlpha;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.getAnimationMarker;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.getVisibilityMarker;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.isVisible;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.makeGone;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.makeInvisible;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.makeVisible;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.setAnimationMarker;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.setScale;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.setVisibilityMarker;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.updateHeight;

/**
 * A search view designed to simplify the process of implementing
 * search-related functionality that also comes with a large set
 * of useful features.
 */
public class PersistentSearchView extends FrameLayout {


    private static final int ANIMATION_DURATION_BUTTON_SCALING = 100;
    private static final int BACKGROUND_ANIMATION_MIN_DURATION = 150;
    private static final int BACKGROUND_ANIMATION_MAX_DURATION = 250;

    private static final float DEFAULT_DIM_AMOUNT = 0.5f;

    private static final Interpolator BUTTON_ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    private static final Interpolator SUGGESTIONS_CONTAINER_ANIMATION_INTERPOLATOR = new DecelerateInterpolator();


    // Colors
    private int mQueryInputHintColor;
    private int mQueryInputTextColor;
    private int mQueryInputCursorColor;

    private int mInputBarIconColor;
    private int mDividerColor;
    private int mProgressBarColor;

    private int mSuggestionIconColor;
    private int mRecentSearchIconColor;
    private int mSearchSuggestionIconColor;
    private int mSuggestionTextColor;
    private int mSuggestionSelectedTextColor;

    private int mCardBackgroundColor;

    private int mBackgroundDimColor;

    // Dimensions
    private int mSuggestionItemHeight;

    private int mCardCornerRadius;
    private int mCardElevation;

    private float mDimAmount;

    // Drawables
    private Drawable mLeftButtonDrawable;
    private Drawable mRightButtonDrawable;

    private Drawable mClearInputButtonDrawable;
    private Drawable mVoiceInputButtonDrawable;

    private Drawable mQueryInputCursorDrawable;

    // Strings
    private String mQueryInputHint;

    private Typeface mQueryTextTypeface;
    private Typeface mSuggestionTextTypeface;

    private State mState;

    private VoiceRecognitionDelegate mVoiceRecognitionDelegate;

    private List<SuggestionItem> mSuggestionItems;

    private SuggestionsRecyclerViewAdapter mAdapter;

    // Views
    private View mDividerView;

    private ImageView mLeftBtnIv;
    private ImageView mRightBtnIv;

    private ImageView mClearInputBtnIv;
    private ImageView mVoiceInputBtnIv;

    private ProgressBar mProgressBar;

    private AdvancedEditText mInputEt;

    private CardView mCardView;

    private FrameLayout mRightButtonContainerFl;

    private LinearLayout mSuggestionsContainerLL;

    private RecyclerView mSuggestionsRecyclerView;

    // Animators
    private ValueAnimator mSuggestionsContainerAnimator;

    private BackgroundDimmingAnimation mBackgroundEnterAnimation;
    private BackgroundDimmingAnimation mBackgroundExitAnimation;

    // Listeners
    private OnSearchQueryChangeListener mOnSearchQueryChangeListener;
    private OnSuggestionChangeListener mOnSuggestionChangeListener;

    private OnSearchConfirmedListener mOnSearchConfirmedListener;

    private OnClickListener mOnLeftBtnClickListener;
    private OnClickListener mOnClearInputBtnClickListener;

    private Runnable mExitAnimationEndAction;

    // Flags
    private boolean mIsDismissibleOnTouchOutside;
    private boolean mAreSuggestionsDisabled;
    private boolean mIsSpeechRecognitionAvailable;
    private boolean mShouldDimBehind;
    private boolean mShouldNotifyAboutQueryChange;




    private enum State {

        EXPANDED,
        COLLAPSED

    }




    public PersistentSearchView(Context context) {
        super(context);
        init(null);
    }




    public PersistentSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }




    public PersistentSearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }




    @SuppressWarnings("NewApi")
    public PersistentSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }




    @SuppressWarnings("NewApi")
    private void init(AttributeSet attrs) {
        View.inflate(getContext(), R.layout.persistent_search_view_layout, this);

        // Initialization related stuff
        initDefaults();
        initResources(attrs);
        initMainContainer();
        initQueryInputBar();
        initSuggestionsContainer();

        // By default the view is collapsed
        collapse(false);

        // Allowing the view to be drawn atop all other views within the same view group
        if(IS_AT_LEAST_LOLLIPOP) {
            setTranslationZ(999);
        }
    }




    private void initDefaults() {
        mDimAmount = DEFAULT_DIM_AMOUNT;

        mSuggestionItems = new ArrayList<>();

        // initializing the default resources
        initDefaultColors();
        initDefaultDimensions();
        initDefaultDrawables();
        initDefaultStrings();
        initDefaultTypefaces();
        initDefaultAnimations();
        initDefaultFlags();
    }




    private void initDefaultColors() {
        mBackgroundDimColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_background_dim_color);

        mQueryInputHintColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_query_input_hint_color);
        mQueryInputTextColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_query_input_text_color);
        mQueryInputCursorColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_query_input_cursor_color);

        mInputBarIconColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_query_input_bar_icon_color);
        mDividerColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_divider_color);
        mProgressBarColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_progress_bar_color);

        mSuggestionIconColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_suggestion_item_icon_color);
        mRecentSearchIconColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_suggestion_item_recent_search_icon_color);
        mSearchSuggestionIconColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_suggestion_item_search_suggestion_icon_color);
        mSuggestionTextColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_suggestion_item_text_color);
        mSuggestionSelectedTextColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_suggestion_item_selected_text_color);

        mCardBackgroundColor = ContextCompat.getColor(getContext(), R.color.persistent_search_view_card_background_color);
    }




    private void initDefaultDimensions() {
        final Resources resources = getResources();

        mSuggestionItemHeight = resources.getDimensionPixelSize(R.dimen.persistent_search_view_item_height);
        mCardCornerRadius = resources.getDimensionPixelSize(R.dimen.persistent_search_view_card_view_corner_radius);
        mCardElevation = resources.getDimensionPixelSize(R.dimen.persistent_search_view_card_view_elevation);
    }




    private void initDefaultDrawables() {
        mLeftButtonDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_left_black_24dp);
        mClearInputButtonDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_close_black_24dp);
        mVoiceInputButtonDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_microphone_black_24dp);
        mQueryInputCursorDrawable = ContextCompat.getDrawable(getContext(), R.drawable.persistent_search_view_cursor_drawable);
    }




    private void initDefaultStrings() {
        mQueryInputHint = getResources().getString(R.string.persistent_search_view_query_input_hint);
    }




    private void initDefaultTypefaces() {
        mQueryTextTypeface = TOOLBAR_TITLE_TYPEFACE;
        mSuggestionTextTypeface = Typeface.DEFAULT;
    }




    private void initDefaultAnimations() {
        mBackgroundEnterAnimation = new BackgroundDimmingAnimation(
            this,
            mBackgroundDimColor,
            0f,
            mDimAmount
        );

        mBackgroundExitAnimation = new BackgroundDimmingAnimation(
            this,
            mBackgroundDimColor,
            mDimAmount,
            0f
        );
    }




    private void initDefaultFlags() {
        mIsSpeechRecognitionAvailable = Utils.isSpeechRecognitionAvailable(getContext());
        mIsDismissibleOnTouchOutside = true;
        mAreSuggestionsDisabled = false;
        mShouldDimBehind = true;
        mShouldNotifyAboutQueryChange = true;
    }




    private void initResources(AttributeSet attrs) {
        if(attrs == null) {
            return;
        }

        final TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.PersistentSearchView);

        initColorResources(attributes);
        initDimensionResources(attributes);
        initDrawableResources(attributes);
        initStringResources(attributes);
        initSearchOptions(attributes);

        // Recycling the typed array
        attributes.recycle();
    }




    private void initColorResources(TypedArray attributes) {
        mBackgroundDimColor = attributes.getColor(R.styleable.PersistentSearchView_dimColor, mBackgroundDimColor);

        mQueryInputHintColor = attributes.getColor(R.styleable.PersistentSearchView_queryInputHintColor, mQueryInputHintColor);
        mQueryInputTextColor = attributes.getColor(R.styleable.PersistentSearchView_queryInputTextColor, mQueryInputTextColor);
        mQueryInputCursorColor = attributes.getColor(R.styleable.PersistentSearchView_queryInputCursorColor, mQueryInputCursorColor);

        mInputBarIconColor = attributes.getColor(R.styleable.PersistentSearchView_queryInputBarIconColor, mInputBarIconColor);
        mDividerColor = attributes.getColor(R.styleable.PersistentSearchView_dividerColor, mDividerColor);
        mProgressBarColor = attributes.getColor(R.styleable.PersistentSearchView_progressBarColor, mProgressBarColor);

        mSuggestionIconColor = attributes.getColor(R.styleable.PersistentSearchView_suggestionIconColor, mSuggestionIconColor);
        mRecentSearchIconColor = attributes.getColor(R.styleable.PersistentSearchView_suggestionRecentSearchIconColor, mRecentSearchIconColor);
        mSearchSuggestionIconColor = attributes.getColor(R.styleable.PersistentSearchView_suggestionSearchSuggestionIconColor, mSearchSuggestionIconColor);
        mSuggestionTextColor = attributes.getColor(R.styleable.PersistentSearchView_suggestionTextColor, mSuggestionTextColor);
        mSuggestionSelectedTextColor = attributes.getColor(R.styleable.PersistentSearchView_suggestionSelectedTextColor, mSuggestionSelectedTextColor);

        mCardBackgroundColor = attributes.getColor(R.styleable.PersistentSearchView_cardBackgroundColor, mCardBackgroundColor);
    }




    private void initDimensionResources(TypedArray attributes) {
        mDimAmount = attributes.getFloat(R.styleable.PersistentSearchView_dimAmount, mDimAmount);
        mCardCornerRadius = attributes.getDimensionPixelSize(R.styleable.PersistentSearchView_cardCornerRadius, mCardCornerRadius);
        mCardElevation = attributes.getDimensionPixelSize(R.styleable.PersistentSearchView_cardElevation, mCardElevation);
    }




    private void initDrawableResources(TypedArray attributes) {
        if(attributes.hasValue(R.styleable.PersistentSearchView_leftButtonDrawable)) {
            mLeftButtonDrawable = attributes.getDrawable(R.styleable.PersistentSearchView_leftButtonDrawable);
        }

        if(attributes.hasValue(R.styleable.PersistentSearchView_rightButtonDrawable)) {
            mRightButtonDrawable = attributes.getDrawable(R.styleable.PersistentSearchView_rightButtonDrawable);
        }

        if(attributes.hasValue(R.styleable.PersistentSearchView_clearInputButtonDrawable)) {
            mClearInputButtonDrawable = attributes.getDrawable(R.styleable.PersistentSearchView_clearInputButtonDrawable);
        }

        if(attributes.hasValue(R.styleable.PersistentSearchView_voiceInputButtonDrawable)) {
            mVoiceInputButtonDrawable = attributes.getDrawable(R.styleable.PersistentSearchView_voiceInputButtonDrawable);
        }

        if(attributes.hasValue(R.styleable.PersistentSearchView_queryInputCursorDrawable)) {
            mQueryInputCursorDrawable = attributes.getDrawable(R.styleable.PersistentSearchView_queryInputCursorDrawable);
        }
    }




    private void initStringResources(TypedArray attributes) {
        if(attributes.hasValue(R.styleable.PersistentSearchView_queryInputHint)) {
            mQueryInputHint = attributes.getString(R.styleable.PersistentSearchView_queryInputHint);
        }
    }

    private void initSearchOptions(TypedArray attributes) {
        if (attributes.hasValue(R.styleable.PersistentSearchView_voiceSearchEnabled)) {
            mIsSpeechRecognitionAvailable =
                    attributes.getBoolean(R.styleable.PersistentSearchView_voiceSearchEnabled, Utils.isSpeechRecognitionAvailable(getContext()));
        }
    }


    private void initMainContainer() {
        mCardView = findViewById(R.id.cardView);
        setCardBackgroundColor(mCardBackgroundColor);
        setCardCornerRadius(mCardCornerRadius);
        setCardElevation(mCardElevation);

        // For dismissibility on touch outside
        setOnClickListener(mOnParentOutsideClickListener);
    }




    private void initQueryInputBar() {
        // Query input related
        initQueryInputEditText();

        // Progress bar related
        mProgressBar = findViewById(R.id.progressBar);
        setProgressBarColor(mProgressBarColor);

        // Left button related
        mLeftBtnIv = findViewById(R.id.leftBtnIv);
        setLeftButtonDrawable(mLeftButtonDrawable);
        mLeftBtnIv.setOnClickListener(mOnLeftButtonClickListener);

        // Right button related
        mRightButtonContainerFl = findViewById(R.id.rightBtnContainerFl);

        mRightBtnIv = findViewById(R.id.rightBtnIv);
        setRightButtonDrawable(mRightButtonDrawable);

        // Clear input button related
        mClearInputBtnIv = findViewById(R.id.clearInputBtnIv);
        setClearInputButtonDrawable(mClearInputButtonDrawable);
        ViewUtils.setScale(mClearInputBtnIv, (isInputQueryEmpty() ? 0f : 1f));
        ViewUtils.setVisibility(mClearInputBtnIv, (isInputQueryEmpty() ? View.GONE : View.VISIBLE));
        mClearInputBtnIv.setOnClickListener(mOnClearInputButtonClickListener);

        // Voice input button related
        mVoiceInputBtnIv = findViewById(R.id.voiceInputBtnIv);
        setVoiceInputButtonDrawable(mVoiceInputButtonDrawable);
        mVoiceInputBtnIv.setEnabled(mIsSpeechRecognitionAvailable);
        ViewUtils.setScale(mVoiceInputBtnIv, ((isInputQueryEmpty() && mIsSpeechRecognitionAvailable) ? 1f : 0f));
        setVoiceInputButtonVisibility();
        mVoiceInputBtnIv.setOnClickListener(mOnVoiceInputButtonClickListener);

        // Button icon coloring
        setQueryInputBarIconColor(mInputBarIconColor);
    }




    private void initQueryInputEditText() {
        mInputEt = findViewById(R.id.inputEt);
        setQueryInputHint(mQueryInputHint);
        setQueryInputHintColor(mQueryInputHintColor);
        setQueryInputTextColor(mQueryInputTextColor);
        setQueryInputCursorDrawable(mQueryInputCursorDrawable, mQueryInputCursorColor);
        setQueryTextTypeface(mQueryTextTypeface);
        mInputEt.setOnEditorActionListener(mInternalEditorActionListener);
        mInputEt.addTextChangedListener(mQueryListener);
        mInputEt.setTouchEventInterceptor(mInputEditTextTouchEventInterceptor);
    }




    private void initSuggestionsContainer() {
        mDividerView = findViewById(R.id.divider);
        setDividerColor(mDividerColor);

        mSuggestionsContainerLL = findViewById(R.id.suggestionsContainerLl);

        initSuggestionsRecyclerView();
    }




    private void initSuggestionsRecyclerView() {
        mSuggestionsRecyclerView = findViewById(R.id.suggestionsRecyclerView);
        Utils.disableRecyclerViewAnimations(mSuggestionsRecyclerView);
        mSuggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSuggestionsRecyclerView.addOnScrollListener(mSuggestionsRecyclerViewScrollListener);

        // Adapter related
        mAdapter = new SuggestionsRecyclerViewAdapter(
            getContext(),
            mSuggestionItems,
            new SuggestionItemResources()
        );
        setRecentSearchIconColor(mRecentSearchIconColor);
        setSearchSuggestionIconColor(mSearchSuggestionIconColor);
        setSuggestionIconColor(mSuggestionIconColor);
        setSuggestionTextColor(mSuggestionTextColor);
        setSuggestionSelectedTextColor(mSuggestionSelectedTextColor);
        setSuggestionTextTypeface(mSuggestionTextTypeface);
        setAdapterQuery(getInputQuery());
        mAdapter.setOnItemClickListener(mOnSuggestionClickListener);
        mAdapter.setOnItemRemoveButtonClickListener(mOnRemoveButtonClickListener);

        mSuggestionsRecyclerView.setAdapter(mAdapter);
    }




    private void showClearInputButtonWithVoiceInputButton(boolean animate) {
        if(isClearInputButtonVisible()) {
            hideVoiceInputButton(false);
        } else {
            if(mIsSpeechRecognitionAvailable && isVoiceInputButtonVisible()) {
                if(animate) {
                    hideVoiceInputButton(
                        true,
                        new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnded(Animator animation) {
                                showClearInputButton(true);
                            }
                        }
                    );
                } else {
                    hideVoiceInputButton(false);
                    showClearInputButton(false);
                }
            } else {
                showClearInputButton(animate);
            }
        }
    }




    private void showClearInputButton() {
        showClearInputButton(true);
    }




    private void showClearInputButton(boolean animate) {
        showClearInputButton(animate, null);
    }




    private void showClearInputButton(boolean animate, @Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        if(getVisibilityMarker(mClearInputBtnIv) || (animate && AnimationType.ENTER.equals(getAnimationMarker(mClearInputBtnIv)))) {
            return;
        }

        ViewUtils.cancelAllAnimations(mClearInputBtnIv);
        makeVisible(mClearInputBtnIv);
        setVisibilityMarker(mClearInputBtnIv, true);

        if(animate) {
            mClearInputBtnIv.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setListener(new AnimatorListenerDecorator(animatorListenerAdapter) {

                    @Override
                    public void onAnimationStarted(Animator animation) {
                        super.onAnimationStarted(animation);
                        setAnimationMarker(mClearInputBtnIv, AnimationType.ENTER);
                    }

                    @Override
                    public void onAnimationEnded(Animator animation) {
                        super.onAnimationEnded(animation);
                        setAnimationMarker(mClearInputBtnIv, AnimationType.NO_ANIMATION);
                    }

                })
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION_BUTTON_SCALING)
                .start();
        } else {
            setScale(mClearInputBtnIv, 1f);
            setAnimationMarker(mClearInputBtnIv, AnimationType.NO_ANIMATION);
        }
    }




    private void hideClearInputButtonWithVoiceInputButton(boolean animate) {
        if(mIsSpeechRecognitionAvailable) {
            if(animate) {
                hideClearInputButton(
                    true,
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnded(Animator animation) {
                            showVoiceInputButton(true);
                        }
                    }
                );
            } else {
                hideClearInputButton(false);
                showVoiceInputButton(false);
            }
        } else {
            hideClearInputButton(animate);
        }
    }




    private void hideClearInputButton() {
        hideClearInputButton(true);
    }




    private void hideClearInputButton(boolean animate) {
        hideClearInputButton(animate, null);
    }




    private void hideClearInputButton(boolean animate, @Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        if(!getVisibilityMarker(mClearInputBtnIv) || (animate && AnimationType.EXIT.equals(getAnimationMarker(mClearInputBtnIv)))) {
            return;
        }

        ViewUtils.cancelAllAnimations(mClearInputBtnIv);
        setVisibilityMarker(mClearInputBtnIv, false);

        if(animate) {
            mClearInputBtnIv.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setListener(new AnimatorListenerDecorator(animatorListenerAdapter) {

                    @Override
                    public void onAnimationStarted(Animator animation) {
                        super.onAnimationStarted(animation);
                        setAnimationMarker(mClearInputBtnIv, AnimationType.EXIT);
                    }

                    @Override
                    public void onAnimationEnded(Animator animation) {
                        super.onAnimationEnded(animation);
                        makeGone(mClearInputBtnIv);
                        setAnimationMarker(mClearInputBtnIv, AnimationType.NO_ANIMATION);
                    }

                })
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION_BUTTON_SCALING)
                .start();
        } else {
            setScale(mClearInputBtnIv, 0f);
            makeGone(mClearInputBtnIv);
            setAnimationMarker(mClearInputBtnIv, AnimationType.NO_ANIMATION);
        }
    }




    private boolean isClearInputButtonVisible() {
        return isVisible(mClearInputBtnIv);
    }




    private void showVoiceInputButton() {
        showVoiceInputButton(true);
    }




    private void showVoiceInputButton(boolean animate) {
        showVoiceInputButton(animate, null);
    }




    private void showVoiceInputButton(boolean animate, @Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        if(!mIsSpeechRecognitionAvailable
                || getVisibilityMarker(mVoiceInputBtnIv)
                || (animate && AnimationType.ENTER.equals(getAnimationMarker(mVoiceInputBtnIv)))) {
            return;
        }

        ViewUtils.cancelAllAnimations(mVoiceInputBtnIv);
        makeVisible(mVoiceInputBtnIv);
        setVisibilityMarker(mVoiceInputBtnIv, true);

        if(animate) {
            mVoiceInputBtnIv.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setListener(new AnimatorListenerDecorator(animatorListenerAdapter) {

                    @Override
                    public void onAnimationStarted(Animator animation) {
                        super.onAnimationStarted(animation);
                        setAnimationMarker(mVoiceInputBtnIv, AnimationType.ENTER);
                    }

                    @Override
                    public void onAnimationEnded(Animator animation) {
                        super.onAnimationEnded(animation);
                        setAnimationMarker(mVoiceInputBtnIv, AnimationType.NO_ANIMATION);
                    }

                })
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION_BUTTON_SCALING)
                .start();
        } else {
            setScale(mVoiceInputBtnIv, 1f);
            setAnimationMarker(mVoiceInputBtnIv, AnimationType.NO_ANIMATION);
        }
    }




    private void hideVoiceInputButton() {
        hideVoiceInputButton(true);
    }




    private void hideVoiceInputButton(boolean animate) {
        hideVoiceInputButton(animate, null);
    }




    private void hideVoiceInputButton(boolean animate, @Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        if(!getVisibilityMarker(mVoiceInputBtnIv) || (animate && AnimationType.EXIT.equals(getAnimationMarker(mVoiceInputBtnIv)))) {
            return;
        }

        ViewUtils.cancelAllAnimations(mVoiceInputBtnIv);
        setVisibilityMarker(mVoiceInputBtnIv, false);

        if(animate) {
            mVoiceInputBtnIv.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setListener(new AnimatorListenerDecorator(animatorListenerAdapter) {

                    @Override
                    public void onAnimationStarted(Animator animation) {
                        super.onAnimationStarted(animation);
                        setAnimationMarker(mVoiceInputBtnIv, AnimationType.EXIT);
                    }

                    @Override
                    public void onAnimationEnded(Animator animation) {
                        super.onAnimationEnded(animation);
                        makeGone(mVoiceInputBtnIv);
                        setAnimationMarker(mVoiceInputBtnIv, AnimationType.NO_ANIMATION);
                    }

                })
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION_BUTTON_SCALING)
                .start();
        } else {
            setScale(mVoiceInputBtnIv, 0f);
            makeGone(mVoiceInputBtnIv);
            setAnimationMarker(mVoiceInputBtnIv, AnimationType.NO_ANIMATION);
        }
    }




    private boolean isVoiceInputButtonVisible() {
        return isVisible(mVoiceInputBtnIv);
    }




    /**
     * Shows the left button by animating it.
     */
    public final void showLeftButton() {
        showLeftButton(true);
    }




    /**
     * Shows the left button by either animating it or not.
     *
     * @param animate Whether to use animation when showing the left button
     */
    public final void showLeftButton(boolean animate) {
        showLeftButtonInternal(animate);
    }




    /**
     * Shows the left button by animating it and hides the progress bar if shown.
     */
    public final void showLeftButtonWithProgressBar() {
        showLeftButtonWithProgressBar(true);
    }




    /**
     * Shows the left button by either animating it or not and hides the progress bar if shown.
     *
     * @param animate Whether to use animation when showing the left button and hiding the progress bar
     */
    public final void showLeftButtonWithProgressBar(boolean animate) {
        hideProgressBarWithLeftButton(animate);
    }




    /**
     * Hides the right button by animating it.
     */
    public final void hideLeftButton() {
        hideLeftButton(true);
    }




    /**
     * Hides the right button by either animating it or not.
     *
     * @param animate Whether to use animation when showing the left button
     */
    public final void hideLeftButton(boolean animate) {
        hideLeftButtonInternal(animate);
    }




    /**
     * Hides the left button by animating it and hides the progress bar if shown.
     */
    public final void hideLeftButtonWithProgressBar() {
        hideLeftButtonWithProgressBar(true);
    }




    /**
     * Hides the left button by either animating it or not and shows the progress bar if shown.
     *
     * @param animate Whether to use animation when hiding the left button and showing the progress bar
     */
    public final void hideLeftButtonWithProgressBar(boolean animate) {
        showProgressBarWithLeftButton(animate);
    }




    private void showLeftButtonInternal(boolean animate) {
        showLeftButtonInternal(animate, null);
    }




    private void showLeftButtonInternal(boolean animate, @Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        if(getVisibilityMarker(mLeftBtnIv) || (animate && AnimationType.ENTER.equals(getAnimationMarker(mLeftBtnIv)))) {
            return;
        }

        ViewUtils.cancelAllAnimations(mLeftBtnIv);
        makeVisible(mLeftBtnIv);
        setVisibilityMarker(mLeftBtnIv, true);

        if(animate) {
            mLeftBtnIv.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setListener(new AnimatorListenerDecorator(animatorListenerAdapter) {

                    @Override
                    public void onAnimationStarted(Animator animation) {
                        super.onAnimationStarted(animation);
                        setAnimationMarker(mLeftBtnIv, AnimationType.ENTER);
                    }

                    @Override
                    public void onAnimationEnded(Animator animation) {
                        super.onAnimationEnded(animation);
                        setAnimationMarker(mLeftBtnIv, AnimationType.NO_ANIMATION);
                    }

                })
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION_BUTTON_SCALING)
                .start();
        } else {
            setScale(mLeftBtnIv, 1f);
            setAnimationMarker(mLeftBtnIv, AnimationType.NO_ANIMATION);
        }
    }




    private void hideLeftButtonInternal(boolean animate) {
        hideLeftButtonInternal(animate, null);
    }




    private void hideLeftButtonInternal(boolean animate, @Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        if(!getVisibilityMarker(mLeftBtnIv) || (animate && AnimationType.EXIT.equals(getAnimationMarker(mLeftBtnIv)))) {
            return;
        }

        ViewUtils.cancelAllAnimations(mLeftBtnIv);
        setVisibilityMarker(mLeftBtnIv, false);

        if(animate) {
            mLeftBtnIv.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setListener(new AnimatorListenerDecorator(animatorListenerAdapter) {

                    @Override
                    public void onAnimationStarted(Animator animation) {
                        super.onAnimationStarted(animation);
                        setAnimationMarker(mLeftBtnIv, AnimationType.EXIT);
                    }

                    @Override
                    public void onAnimationEnded(Animator animation) {
                        super.onAnimationEnded(animation);
                        makeGone(mLeftBtnIv);
                        setAnimationMarker(mLeftBtnIv, AnimationType.NO_ANIMATION);
                    }

                })
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION_BUTTON_SCALING)
                .start();
        } else {
            setScale(mLeftBtnIv, 0f);
            makeGone(mLeftBtnIv);
            setAnimationMarker(mLeftBtnIv, AnimationType.NO_ANIMATION);
        }
    }




    /**
     * Checks whether the left button is visible (i.e., its visibility flags is {@link View#VISIBLE}).
     *
     * @return true if visible; false otherwise
     */
    public final boolean isLeftButtonVisible() {
        return isVisible(mLeftBtnIv);
    }




    /**
     * Shows the right button (i.e., change its visibility flags to {@link View#VISIBLE}.
     */
    public final void showRightButton() {
        makeVisible(mRightButtonContainerFl);
    }




    /**
     * Hides the right button (i.e., change its visibility flags to {@link View#GONE}.
     */
    public final void hideRightButton() {
        makeGone(mRightButtonContainerFl);
    }




    /**
     * Shows the progress bar by animating it.
     */
    public final void showProgressBar() {
        showProgressBar(true);
    }




    /**
     * Shows the progress bar by either animating it or not.
     *
     * @param animate Whether to use animation when showing the progress bar
     */
    public final void showProgressBar(boolean animate) {
        showProgressBarInternal(animate);
    }




    /**
     * Hides the progress bar by animating it.
     */
    public final void hideProgressBar() {
        hideProgressBar(true);
    }




    /**
     * Hides the progress bar by either animating it or not.
     *
     * @param animate Whether to use animation when hiding the progress bar
     */
    public final void hideProgressBar(boolean animate) {
        hideProgressBarInternal(animate);
    }




    /**
     * Shows the progress bar by animating it and hides the left button if shown.
     */
    public final void showProgressBarWithLeftButton() {
        showProgressBarWithLeftButton(true);
    }




    /**
     * Shows the progress bar by either animating it or not and hides the left button if shown.
     *
     * @param animate Whether to use animation when showing the progress bar and hiding the left button
     */
    public final void showProgressBarWithLeftButton(boolean animate) {
        if(!isLeftButtonVisible()) {
            showProgressBarInternal(animate);
        } else {
            if(animate) {
                hideLeftButtonInternal(
                    true,
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnded(Animator animation) {
                            showProgressBarInternal(true);
                        }
                    }
                );
            } else {
                hideLeftButtonInternal(false);
                showProgressBarInternal(false);
            }
        }
    }




    /**
     * Hides the progress bar by animating it and shows the left button if hidden.
     */
    public final void hideProgressBarWithLeftButton() {
        hideProgressBarWithLeftButton(true);
    }




    /**
     * Hides the progress bar by either animating it or not and shows the left button if hidden.
     *
     * @param animate Whether to use animation for hiding the progress bar and showing the left button
     */
    public final void hideProgressBarWithLeftButton(boolean animate) {
        if(!isProgressBarVisible()) {
            showLeftButtonInternal(animate);
        } else {
            if(animate) {
                hideProgressBarInternal(
                    true,
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnded(Animator animation) {
                            showLeftButtonInternal(true);
                        }
                    }
                );
            } else {
                hideProgressBarInternal(false);
                showLeftButtonInternal(false);
            }
        }
    }




    private void showProgressBarInternal(boolean animate) {
        showProgressBarInternal(animate, null);
    }




    private void showProgressBarInternal(boolean animate, @Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        if(getVisibilityMarker(mProgressBar) || (animate && AnimationType.ENTER.equals(getAnimationMarker(mProgressBar)))) {
            return;
        }

        ViewUtils.cancelAllAnimations(mProgressBar);
        makeVisible(mProgressBar);
        setVisibilityMarker(mProgressBar, true);

        if(animate) {
            mProgressBar.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setListener(new AnimatorListenerDecorator(animatorListenerAdapter) {

                    @Override
                    public void onAnimationStarted(Animator animation) {
                        super.onAnimationStarted(animation);
                        setAnimationMarker(mProgressBar, AnimationType.ENTER);
                    }

                    @Override
                    public void onAnimationEnded(Animator animation) {
                        super.onAnimationEnded(animation);
                        setAnimationMarker(mProgressBar, AnimationType.NO_ANIMATION);
                    }

                })
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION_BUTTON_SCALING)
                .start();
        } else {
            setScale(mProgressBar, 1f);
            setAnimationMarker(mProgressBar, AnimationType.NO_ANIMATION);
        }
    }




    private void hideProgressBarInternal(boolean animate) {
        hideProgressBarInternal(animate, null);
    }




    private void hideProgressBarInternal(boolean animate, @Nullable AnimatorListenerAdapter animatorListenerAdapter) {
        if(!getVisibilityMarker(mProgressBar) || (animate && AnimationType.EXIT.equals(getAnimationMarker(mProgressBar)))) {
            return;
        }

        ViewUtils.cancelAllAnimations(mProgressBar);
        setVisibilityMarker(mProgressBar, false);

        if(animate) {
            mProgressBar.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setListener(new AnimatorListenerDecorator(animatorListenerAdapter) {

                    @Override
                    public void onAnimationStarted(Animator animation) {
                        super.onAnimationStarted(animation);
                        setAnimationMarker(mProgressBar, AnimationType.EXIT);
                    }

                    @Override
                    public void onAnimationEnded(Animator animation) {
                        super.onAnimationEnded(animation);
                        makeGone(mProgressBar);
                        setAnimationMarker(mProgressBar, AnimationType.NO_ANIMATION);
                    }

                })
                .setInterpolator(BUTTON_ANIMATION_INTERPOLATOR)
                .setDuration(ANIMATION_DURATION_BUTTON_SCALING)
                .start();
        } else {
            setScale(mProgressBar, 0f);
            makeGone(mProgressBar);
            setAnimationMarker(mProgressBar, AnimationType.NO_ANIMATION);
        }
    }




    /**
     * Checks whether the progress bar is visible (i.e., its visibility flags is {@link View#VISIBLE}.
     *
     * @return true if visible; false otherwise
     */
    public final boolean isProgressBarVisible() {
        return isVisible(mProgressBar);
    }




    private void showKeyboard() {
        mInputEt.requestFocus();
        KeyboardManagingUtil.showKeyboard(mInputEt);
    }




    private void hideKeyboard() {
        mInputEt.clearFocus();
        KeyboardManagingUtil.hideKeyboard(mInputEt);
    }




    /**
     * Confirms the search action by invoking the editor action listener.
     */
    public final void confirmSearchAction() {
        if(mInternalEditorActionListener != null) {
            mInternalEditorActionListener.onEditorAction(
                mInputEt,
                EditorInfo.IME_ACTION_SEARCH,
                new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SEARCH)
            );
        }
    }




    private void postExitAnimationEndActionEvent(long delay) {
        cancelExitAnimationEndActionEvent();

        // Creating a brand-new event
        mExitAnimationEndAction = new Runnable() {
            @Override
            public void run() {
                requestLayout();
                mExitAnimationEndAction = null;
            }
        };

        postDelayed(mExitAnimationEndAction, delay);
    }




    private void cancelExitAnimationEndActionEvent() {
        if(mExitAnimationEndAction != null) {
            removeCallbacks(mExitAnimationEndAction);
            mExitAnimationEndAction = null;
        }
    }




    /**
     * Expands the search view, i.e. changes its state to {@link State#EXPANDED}, by animating it.
     */
    public final void expand() {
        expand(true);
    }




    /**
     * Expands the search view, i.e. changes its state to {@link State#EXPANDED}, by either animating
     * it or not.
     *
     * @param animate Whether to animate the expansion or not
     */
    public final void expand(boolean animate) {
        if(isExpanded()) {
            return;
        }

        // Internal states
        setEnabled(true);
        setClickable(true);
        setState(State.EXPANDED);

        // Input related
        mInputEt.setEnabled(true);
        mInputEt.setSelection(mInputEt.length());

        showKeyboard();

        if(!areSuggestionsDisabled()) {
            // Cancelling any pending events
            cancelExitAnimationEndActionEvent();

            // Background dimming related
            updateBackground(mState, animate);

            // Suggestions container related
            makeVisible(mSuggestionsContainerLL);
            mSuggestionsContainerLL.measure(
                MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getSuggestionsContainerMaxHeight(), MeasureSpec.AT_MOST)
            );

            makeVisible(mDividerView);
            updateSuggestionsContainerHeight(
                mState,
                0,
                mSuggestionsContainerLL.getMeasuredHeight(),
                animate
            );

            // Requesting a full relayout, in order to apply the new size
            requestLayout();
        }
    }




    /**
     * Collapses the search view, i.e. changes its state to {@link State#COLLAPSED}, by animating it.
     */
    public final void collapse() {
        collapse(true);
    }




    /**
     * Collapses the search view, i.e. changes its state to {@link State#COLLAPSED}, by either animating
     * it or not.
     *
     * @param animate Whether to animate the collapse or not
     */
    public final void collapse(boolean animate) {
        if(!isExpanded() || (animate && isExitAnimationRunning())) {
            return;
        }

        // Internal states
        setEnabled(false);
        setClickable(false);
        setState(State.COLLAPSED);

        // Input related
        mInputEt.setEnabled(false);

        hideKeyboard();

        if(!areSuggestionsDisabled()) {
            // Cancelling any pending events
            cancelExitAnimationEndActionEvent();

            // Animation related
            final long duration = getSuggestionsContainerAnimationDuration(mSuggestionsContainerLL.getMeasuredHeight(), 0);

            // Background dimming related
            updateBackgroundWithAnimation(mState, duration);

            // Suggestions container related
            makeInvisible(mDividerView);
            updateSuggestionsContainerHeightWithAnimation(
                mState,
                mSuggestionsContainerLL.getMeasuredHeight(),
                0,
                duration
            );

            // Relayout related stuff
            if(!animate) {
                requestLayout();
            } else {
                postExitAnimationEndActionEvent(duration);
            }
        }

    }




    private void updateBackgroundWithAnimation(State state) {
        updateBackgroundWithAnimation(state, BACKGROUND_ANIMATION_MAX_DURATION);
    }




    private void updateBackgroundWithAnimation(State state, long duration) {
        updateBackground(state, duration, true);
    }




    private void updateBackground(State state, boolean animate) {
        updateBackground(state, BACKGROUND_ANIMATION_MAX_DURATION, animate);
    }




    private void updateBackground(State state,
                                  long duration,
                                  boolean animate) {
        if(!mShouldDimBehind) {
            setBackgroundColor(Color.TRANSPARENT);
            return;
        }

        if(State.EXPANDED.equals(state)) {
            if(animate) {
                mBackgroundEnterAnimation.setDuration(duration)
                    .start();
            } else {
                setBackgroundColor(adjustColorAlpha(mBackgroundDimColor, mDimAmount));
            }
        } else {
            if(animate) {
                mBackgroundExitAnimation.setDuration(duration)
                    .start();
            } else {
                setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }




    private void updateSuggestionsContainerHeightWithAnimation(final State state,
                                                               final int fromHeight,
                                                               final int toHeight,
                                                               final long duration) {
        updateSuggestionsContainerHeight(
            state,
            fromHeight,
            toHeight,
            duration,
            true
        );
    }




    private void updateSuggestionsContainerHeight(final State state,
                                                  final int fromHeight,
                                                  final int toHeight,
                                                  final boolean animate) {
        updateSuggestionsContainerHeight(
            state,
            fromHeight,
            toHeight,
            BACKGROUND_ANIMATION_MAX_DURATION,
            animate
        );
    }




    private void updateSuggestionsContainerHeight(final State state,
                                                  final int fromHeight,
                                                  final int toHeight,
                                                  final long duration,
                                                  final boolean animate) {
        // Cancelling the active suggestions container animation (if there's any)
        cancelSuggestionsContainerAnimation();

        // Adjusting the divider visibility
        if(mSuggestionItems.isEmpty()) {
            makeGone(mDividerView);
        } else {
            makeVisible(mDividerView);
        }

        // The actual animation related stuff
        if(animate && (fromHeight != toHeight)) {
            mSuggestionsContainerAnimator = ValueAnimator.ofInt(fromHeight, toHeight);
            mSuggestionsContainerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    final int newHeight = (Integer) animation.getAnimatedValue();

                    updateHeight(mSuggestionsContainerLL, newHeight);
                }
            });
            mSuggestionsContainerAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnded(Animator animation) {
                    if(State.COLLAPSED.equals(state)) {
                        mSuggestionsContainerLL.setVisibility(View.GONE);
                    }
                }
            });
            mSuggestionsContainerAnimator.setInterpolator(SUGGESTIONS_CONTAINER_ANIMATION_INTERPOLATOR);
            mSuggestionsContainerAnimator.setDuration(duration);
            mSuggestionsContainerAnimator.start();
        } else {
            updateHeight(mSuggestionsContainerLL, toHeight);

            if(State.COLLAPSED.equals(state)) {
                mSuggestionsContainerLL.setVisibility(View.GONE);
            }
        }
    }




    private void cancelSuggestionsContainerAnimation() {
        if(mSuggestionsContainerAnimator != null) {
            mSuggestionsContainerAnimator.cancel();
        }
    }




    private void cancelAllAnimations() {
        cancelSuggestionsContainerAnimation();

        mBackgroundEnterAnimation.stop();
        mBackgroundExitAnimation.stop();
    }




    @CallSuper
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        cancelExitAnimationEndActionEvent();
        cancelAllAnimations();

        // recycling the listeners
        mVoiceRecognitionDelegate = null;
        mOnSearchQueryChangeListener = null;
        mOnSuggestionChangeListener = null;
        mOnLeftBtnClickListener = null;
        mOnClearInputBtnClickListener = null;
        mOnSearchConfirmedListener = null;
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // First measuring all the available children
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        // Then measuring the current view group
        // and changing its background color based
        // on the current state
        final int[] measuredSize;

        if(isExpanded() || isExitAnimationRunning()) {
            measuredSize = Utils.getScreenSize(getContext());
        } else {
            measuredSize = calculateTheUsedSpace(widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(measuredSize[0], measuredSize[1]);
    }




    private int[] calculateTheUsedSpace(int widthMeasureSpec, int heightMeasureSpec) {
        final int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measuredHeight = (mCardView.getMeasuredHeight() + getPaddingTop() + getPaddingBottom());

        return new int[] {measuredWidth, measuredHeight};
    }




    private void setState(State state) {
        mState = state;
    }




    private void setAdapterQuery(String query) {
        mAdapter.setResources(((SuggestionItemResources) mAdapter.getResources()).setCurrentQuery(query));
    }




    /**
     * Sets the suggestions items for this search view.
     *
     * @param suggestions The suggestions to set
     */
    public final void setSuggestions(@NonNull List<? extends SuggestionItem> suggestions) {
        setSuggestions(suggestions, true);
    }




    /**
     * Sets the suggestion items for this search view.
     *
     * @param suggestions The suggestions to set
     * @param expandIfNecessary Whether to expand the search view if it's in a collapsed state
     */
    @SuppressWarnings("unchecked")
    public final void setSuggestions(@NonNull List<? extends SuggestionItem> suggestions, boolean expandIfNecessary) {
        Preconditions.nonNull(suggestions);

        if(isExpanded()) {
            final int currentHeight = mSuggestionsContainerLL.getMeasuredHeight();

            // Swapping the items
            mAdapter.setItems(mSuggestionItems = (List<SuggestionItem>) suggestions);

            // Animation related
            mSuggestionsContainerLL.measure(
                MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getSuggestionsContainerMaxHeight(), MeasureSpec.AT_MOST)
            );

            updateSuggestionsContainerHeightWithAnimation(
                mState,
                currentHeight,
                mSuggestionsContainerLL.getMeasuredHeight(),
                getSuggestionsContainerAnimationDuration(currentHeight, mSuggestionsContainerLL.getMeasuredHeight())
            );
        } else {
            mAdapter.setItems(mSuggestionItems = (List<SuggestionItem>) suggestions);

            if(expandIfNecessary) {
                expand();
            }
        }
    }




    /**
     * Sets the color of the query input hint.
     *
     * @param color The color to set
     */
    public final void setQueryInputHintColor(@ColorInt int color) {
        mQueryInputHintColor = color;

        mInputEt.setHintTextColor(color);
    }




    /**
     * Sets the color of the query input text.
     *
     * @param textColor The color to set
     */
    public final void setQueryInputTextColor(@ColorInt int textColor) {
        mQueryInputTextColor = textColor;

        mInputEt.setTextColor(textColor);
    }




    /**
     * Sets the color of the query input cursor.
     *
     * @param color The color to set
     */
    public final void setQueryInputCursorColor(@ColorInt int color) {
        setQueryInputCursorDrawable(mQueryInputCursorDrawable, color);
    }




    /**
     * Sets the cursor drawable and its color of the query input.
     *
     * @param drawable The drawable to set
     * @param color The color of the drawable
     */
    public final void setQueryInputCursorDrawable(Drawable drawable, @ColorInt int color) {
        mQueryInputCursorColor = color;

        setQueryInputCursorDrawable(Utils.getColoredDrawable(drawable, color));
    }




    /**
     * Sets the cursor drawable of the query input.
     *
     * @param drawable The drawable to set
     */
    public final void setQueryInputCursorDrawable(Drawable drawable) {
        mQueryInputCursorDrawable = drawable;

        Utils.setCursorDrawable(mInputEt, drawable);
    }




    /**
     * Sets the color of the query input bar icons.
     *
     * @param color The color to set
     */
    public final void setQueryInputBarIconColor(@ColorInt int color) {
        mInputBarIconColor = color;

        setLeftButtonDrawable(Utils.getColoredDrawable(mLeftButtonDrawable, color));
        setRightButtonDrawable(Utils.getColoredDrawable(mRightButtonDrawable, color));
        setClearInputButtonDrawable(Utils.getColoredDrawable(mClearInputButtonDrawable, color));
        setVoiceInputButtonDrawable(Utils.getColoredDrawable(mVoiceInputButtonDrawable, color));
    }




    /**
     * Sets the color of the divider.
     *
     * @param color The color to set
     */
    public final void setDividerColor(@ColorInt int color) {
        mDividerColor = color;

        mDividerView.setBackgroundColor(color);
    }




    /**
     * Sets the color of the progress bar.
     *
     * @param color The color to set
     */
    public final void setProgressBarColor(@ColorInt int color) {
        mProgressBarColor = color;

        Utils.setProgressBarColor(mProgressBar, color);
    }




    /**
     * Sets the color of the suggestion icon.
     *
     * @param color The color to set
     */
    public final void setSuggestionIconColor(@ColorInt int color) {
        mSuggestionIconColor = color;

        mAdapter.setResources(((SuggestionItemResources) mAdapter.getResources()).setIconColor(color));
    }




    /**
     * Sets the color of the recent search icon.
     *
     * @param color The color to set
     */
    public final void setRecentSearchIconColor(@ColorInt int color) {
        mRecentSearchIconColor = color;

        mAdapter.setResources(((SuggestionItemResources) mAdapter.getResources()).setRecentSearchIconColor(color));
    }




    /**
     * Sets the color of the search suggestion icon.
     *
     * @param color The color to set
     */
    public final void setSearchSuggestionIconColor(@ColorInt int color) {
        mSearchSuggestionIconColor = color;

        mAdapter.setResources(((SuggestionItemResources) mAdapter.getResources()).setSearchSuggestionIconColor(color));
    }




    /**
     * Sets the color of the suggestion text.
     *
     * @param color The color to set
     */
    public final void setSuggestionTextColor(@ColorInt int color) {
        mSuggestionTextColor = color;

        mAdapter.setResources(((SuggestionItemResources) mAdapter.getResources()).setTextColor(color));
    }




    /**
     * Sets the color of the selected suggestion text.
     *
     * @param color The color to set
     */
    public final void setSuggestionSelectedTextColor(@ColorInt int color) {
        mSuggestionSelectedTextColor = color;

        mAdapter.setResources(((SuggestionItemResources) mAdapter.getResources()).setSelectedTextColor(color));
    }




    /**
     * Sets the color of the card background.
     *
     * @param color The color to set
     */
    public final void setCardBackgroundColor(@ColorInt int color) {
        mCardBackgroundColor = color;

        mCardView.setCardBackgroundColor(color);
    }




    /**
     * Sets the color of the background dim.
     *
     * @param color The color to set
     */
    public final void setBackgroundDimColor(@ColorInt int color) {
        mBackgroundDimColor = color;

        mBackgroundEnterAnimation.setDimColor(color);
        mBackgroundExitAnimation.setDimColor(color);
    }




    /**
     * Sets the radius of the card corner.
     *
     * @param cornerRadius The corner radius to set
     */
    public final void setCardCornerRadius(int cornerRadius) {
        mCardCornerRadius = cornerRadius;

        mCardView.setRadius(mCardCornerRadius);
    }




    /**
     * Sets the elevation of the card.
     *
     * @param cardElevation The elevation to set
     */
    public final void setCardElevation(int cardElevation) {
        mCardElevation = cardElevation;

        mCardView.setCardElevation(cardElevation);
        mCardView.setMaxCardElevation(cardElevation);
    }




    /**
     * Sets the amount of the background dim.
     *
     * @param dimAmount The dim amount to set
     */
    public final void setBackgroundDimAmount(float dimAmount) {
        mDimAmount = dimAmount;

        mBackgroundEnterAnimation.setToAlpha(dimAmount);
        mBackgroundExitAnimation.setFromAlpha(dimAmount);
    }




    /**
     * Sets the drawable of the left button.
     *
     * @param drawableResId The resource ID of the drawable
     */
    public final void setLeftButtonDrawable(@DrawableRes int drawableResId) {
        setLeftButtonDrawable(Utils.getColoredDrawable(
            getContext(),
            drawableResId,
            mInputBarIconColor
        ));
    }




    /**
     * Sets the drawable of the left button.
     *
     * @param drawable The drawable to set
     */
    public final void setLeftButtonDrawable(Drawable drawable) {
        mLeftButtonDrawable = drawable;

        mLeftBtnIv.setImageDrawable(drawable);
    }




    /**
     * Sets the drawable of the right button.
     *
     * @param drawableResId The resource ID of the drawable
     */
    public final void setRightButtonDrawable(@DrawableRes int drawableResId) {
        setRightButtonDrawable(Utils.getColoredDrawable(
            getContext(),
            drawableResId,
            mInputBarIconColor
        ));
    }




    /**
     * Sets the drawable of the right button.
     *
     * @param drawable The drawable to set
     */
    public final void setRightButtonDrawable(Drawable drawable) {
        mRightButtonDrawable = drawable;

        mRightBtnIv.setImageDrawable(drawable);
    }




    /**
     * Sets the drawable of the clear input button.
     *
     * @param drawableResId The resource ID of the drawable
     */
    public final void setClearInputButtonDrawable(@DrawableRes int drawableResId) {
        setClearInputButtonDrawable(Utils.getColoredDrawable(
            getContext(),
            drawableResId,
            mInputBarIconColor
        ));
    }




    /**
     * Sets the drawable of the clear input button.
     *
     * @param drawable The drawable to set
     */
    public final void setClearInputButtonDrawable(Drawable drawable) {
        mClearInputButtonDrawable = drawable;

        mClearInputBtnIv.setImageDrawable(drawable);
    }




    /**
     * Sets the drawable of the voice input button.
     *
     * @param drawableResId The resource ID of the drawable
     */
    public final void setVoiceInputButtonDrawable(@DrawableRes int drawableResId) {
        setVoiceInputButtonDrawable(Utils.getColoredDrawable(
            getContext(),
            drawableResId,
            mInputBarIconColor
        ));
    }


    /**
     * Set the voice input button enabled or disabled
     *
     * @param enabled: enable or disable the voice input button
     */
    public void setVoiceInputEnabled(boolean enabled) {
        mIsSpeechRecognitionAvailable = enabled;
        setVoiceInputButtonVisibility();
    }

    /**
     * Sets the drawable of the voice input button.
     *
     * @param drawable The drawable to set
     */
    public void setVoiceInputButtonDrawable(@NonNull Drawable drawable) {
        mVoiceInputButtonDrawable = drawable;
        mVoiceInputBtnIv.setImageDrawable(drawable);
    }




    /**
     * Sets the input query.
     *
     * @param query The query to set
     */
    public final void setInputQuery(@NonNull String query) {
        setInputQueryInternal(query, true);
    }


    private void setVoiceInputButtonVisibility() {
        ViewUtils.setVisibility(mVoiceInputBtnIv, (((isInputQueryEmpty()
                && mIsSpeechRecognitionAvailable)
                && mVoiceInputButtonDrawable != null) ? View.VISIBLE : View.GONE));
    }


    private final void setInputQueryInternal(@NonNull String query, boolean notifyAboutQueryChange) {
        Preconditions.nonNull(query);

        mShouldNotifyAboutQueryChange = notifyAboutQueryChange;

        mInputEt.setText(query);
        mInputEt.setSelection(mInputEt.length());

        mShouldNotifyAboutQueryChange = true;
    }




    /**
     * Sets the hint of the query input.
     *
     * @param hint The hint to set
     */
    public final void setQueryInputHint(@NonNull String hint) {
        Preconditions.nonNull(hint);

        mQueryInputHint = hint;

        mInputEt.setHint(hint);
    }




    /**
     * Sets the typeface of the query text.
     *
     * @param typeface The typeface to set
     */
    public final void setQueryTextTypeface(@NonNull Typeface typeface) {
        Preconditions.nonNull(typeface);

        mQueryTextTypeface = typeface;

        mInputEt.setTypeface(typeface);
    }




    /**
     * Sets the typeface of the suggestion text.
     *
     * @param typeface The typeface to set
     */
    public final void setSuggestionTextTypeface(@NonNull Typeface typeface) {
        Preconditions.nonNull(typeface);

        mSuggestionTextTypeface = typeface;

        mAdapter.setResources(((SuggestionItemResources) mAdapter.getResources()).setTypeface(typeface));
    }




    /**
     * Sets the delegate for voice recognition.
     *
     * @param delegate The delegate to set
     */
    public final void setVoiceRecognitionDelegate(VoiceRecognitionDelegate delegate) {
        mVoiceRecognitionDelegate = delegate;
    }




    /**
     * Sets the listener to invoke when a search has been confirmed.
     *
     * @param onSearchConfirmedListener The listener to set
     */
    public final void setOnSearchConfirmedListener(OnSearchConfirmedListener onSearchConfirmedListener) {
        mOnSearchConfirmedListener = onSearchConfirmedListener;
    }




    /**
     * Sets the listener to invoke when a search query has been changed.
     *
     * @param onSearchQueryChangeListener The listener to set
     */
    public final void setOnSearchQueryChangeListener(OnSearchQueryChangeListener onSearchQueryChangeListener) {
        mOnSearchQueryChangeListener = onSearchQueryChangeListener;
    }




    /**
     * Sets the listener to invoke when a suggestion has been changed.
     *
     * @param onSuggestionChangeListener The listener to set
     */
    public final void setOnSuggestionChangeListener(OnSuggestionChangeListener onSuggestionChangeListener) {
        mOnSuggestionChangeListener = onSuggestionChangeListener;
    }




    /**
     * Sets the listener to invoke when a left button is clicked.
     *
     * @param listener The listener to set
     */
    public final void setOnLeftBtnClickListener(OnClickListener listener) {
        mOnLeftBtnClickListener = listener;
    }




    /**
     * Sets the listener to invoke when a clear button is clicked.
     *
     * @param listener The listener to set
     */
    public final void setOnClearInputBtnClickListener(OnClickListener listener) {
        mOnClearInputBtnClickListener = listener;
    }




    /**
     * Sets the listener to invoke when a right button is clicked.
     *
     * @param listener The listener to set
     */
    public final void setOnRightBtnClickListener(OnClickListener listener) {
        mRightBtnIv.setOnClickListener(listener);
    }




    /**
     * Sets a flag indicating whether it is possible to dismiss this search view
     * (i.e. convert from {@link State#EXPANDED to {@link State#COLLAPSED} state}
     * when a user clicks on the outside area.
     *
     * @param dismissOnTouchOutside Whether this search view is dismissible when
     * a user clicks on the outside area.
     */
    public final void setDismissOnTouchOutside(boolean dismissOnTouchOutside) {
        mIsDismissibleOnTouchOutside = dismissOnTouchOutside;
    }




    /**
     * Checks whether this search view is dismissible when a user clicks on
     * the outside area.
     *
     * @return true if dismissible; false otherwise
     */
    public final boolean isDismissibleOnTouchOutside() {
        return mIsDismissibleOnTouchOutside;
    }




    /**
     * Sets whether it is possible to show suggestions or not.
     *
     * @param areSuggestionsDisabled Whether it is possible to show suggestions or not
     */
    public final void setSuggestionsDisabled(boolean areSuggestionsDisabled) {
        mAreSuggestionsDisabled = areSuggestionsDisabled;
    }




    /**
     * Checks whether it is possible to show suggestions or not.
     *
     * @return true if possible; false otherwise
     */
    public final boolean areSuggestionsDisabled() {
        return mAreSuggestionsDisabled;
    }




    /**
     * Sets whether it is possible to dim background.
     *
     * @param dimBackground Whether it is possible to dim background
     */
    public final void setDimBackground(boolean dimBackground) {
        mShouldDimBehind = dimBackground;
    }




    private boolean isExitAnimationRunning() {
        return (mExitAnimationEndAction != null);
    }




    /**
     * Checks whether this search view is in {@link State#EXPANDED} state.
     *
     * @return true if expanded; false otherwise
     */
    public final boolean isExpanded() {
        return State.EXPANDED.equals(mState);
    }




    /**
     * Checks whether the input query is empty.
     *
     * @return true if empty; false otherwise
     */
    public final boolean isInputQueryEmpty() {
        return TextUtils.isEmpty(mInputEt.getText().toString());
    }




    /**
     * Checks whether the input is currently focused.
     *
     * @return true if focused; false otherwise
     */
    public final boolean isInputFocused() {
        return mInputEt.hasFocus();
    }




    /**
     * Retrieves the input query.
     *
     * @return The input query
     */
    public final String getInputQuery() {
        return mInputEt.getText().toString();
    }




    private int getSuggestionsContainerMaxHeight() {
        final int maxRawHeight = (Utils.getScreenSize(getContext())[1] / 2);
        final int maxItemCount = (maxRawHeight / mSuggestionItemHeight);
        final int maxAllowedItemCount = 8;

        return ((maxItemCount > maxAllowedItemCount) ? (maxAllowedItemCount * mSuggestionItemHeight) : (maxItemCount * mSuggestionItemHeight));
    }




    private long getSuggestionsContainerAnimationDuration(final long fromHeight, final long toHeight) {
        final int maxHeight = getSuggestionsContainerMaxHeight();
        return (long) Math.max(BACKGROUND_ANIMATION_MIN_DURATION, ((Math.abs(toHeight - fromHeight) * 1f / maxHeight) * BACKGROUND_ANIMATION_MAX_DURATION));
    }




    private final OnClickListener mOnParentOutsideClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if(mIsDismissibleOnTouchOutside) {
                collapse();
            }
        }

    };




    private final OnClickListener mOnLeftButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            if(isExpanded()) {
                collapse();
            } else if(mOnLeftBtnClickListener != null) {
                mOnLeftBtnClickListener.onClick(view);
            }
        }

    };




    private final OnClickListener mOnClearInputButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            expand();
            setInputQuery("");

            if(mOnClearInputBtnClickListener != null) {
                mOnClearInputBtnClickListener.onClick(view);
            }
        }

    };




    private final OnClickListener mOnVoiceInputButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            expand();

            if(mVoiceRecognitionDelegate != null) {
                mVoiceRecognitionDelegate.openSpeechRecognizer();
            }
        }

    };




    private final OnTouchListener mInputEditTextTouchEventInterceptor = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                expand();
            }

            return !isExpanded();
        }

    };




    private final TextView.OnEditorActionListener mInternalEditorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            if((actionId == EditorInfo.IME_ACTION_SEARCH) && (mOnSearchConfirmedListener != null)) {
                mOnSearchConfirmedListener.onSearchConfirmed(PersistentSearchView.this, getInputQuery());
            }

            return true;
        }

    };




    private final QueryListener mQueryListener = new QueryListener() {

        @Override
        public void onQueryChanged(String oldQuery, String newQuery) {
            setAdapterQuery(newQuery);

            if((mOnSearchQueryChangeListener != null) && mShouldNotifyAboutQueryChange) {
                mOnSearchQueryChangeListener.onSearchQueryChanged(PersistentSearchView.this, oldQuery, newQuery);
            }
        }

        @Override
        public void onQueryEntered(String query) {
            if(!TextUtils.isEmpty(query)) {
                showClearInputButtonWithVoiceInputButton(true);
            }
        }

        @Override
        public void onQueryRemoved() {
            hideClearInputButtonWithVoiceInputButton(true);
        }

    };




    private final OnItemClickListener<SuggestionItem> mOnSuggestionClickListener = new OnItemClickListener<SuggestionItem>() {

        @Override
        public void onItemClicked(View view, SuggestionItem suggestion, int position) {
            if(mOnSuggestionChangeListener != null) {
                mOnSuggestionChangeListener.onSuggestionPicked(suggestion);
            }

            setInputQueryInternal(suggestion.getItemModel().getText(), false);
            collapse();
        }

    };




    private final OnItemClickListener<SuggestionItem> mOnRemoveButtonClickListener = new OnItemClickListener<SuggestionItem>() {

        @Override
        public void onItemClicked(View view, SuggestionItem item, int position) {
            // Keeping the current height
            final int currentHeight = mSuggestionsContainerLL.getMeasuredHeight();

            // Removing the item from the dataset
            mAdapter.deleteItem(item);

            // Remeasuring the container (after the item removal)
            mSuggestionsContainerLL.measure(
                MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getSuggestionsContainerMaxHeight(), MeasureSpec.AT_MOST)
            );

            // Animating the suggestions container
            updateSuggestionsContainerHeightWithAnimation(
                mState,
                currentHeight,
                mSuggestionsContainerLL.getMeasuredHeight(),
                getSuggestionsContainerAnimationDuration(
                    currentHeight,
                    mSuggestionsContainerLL.getMeasuredHeight()
                )
            );

            // Reporting the change
            if(mOnSuggestionChangeListener != null) {
                mOnSuggestionChangeListener.onSuggestionRemoved(item);
            }
        }

    };




    private final RecyclerView.OnScrollListener mSuggestionsRecyclerViewScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if((newState == RecyclerView.SCROLL_STATE_DRAGGING) && !mSuggestionItems.isEmpty()) {
                hideKeyboard();
            }
        }

    };




    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        final SavedState savedState = (SavedState) state;

        super.onRestoreInstanceState(state);

        // Restoring our state
        setQueryInputHintColor(savedState.queryInputHintColor);
        setQueryInputTextColor(savedState.queryInputTextColor);
        setQueryInputCursorColor(savedState.queryInputCursorColor);
        setQueryInputBarIconColor(savedState.inputBarIconColor);
        setDividerColor(savedState.dividerColor);
        setProgressBarColor(savedState.progressBarColor);
        setSuggestionIconColor(savedState.suggestionIconColor);
        setRecentSearchIconColor(savedState.recentSearchIconColor);
        setSearchSuggestionIconColor(savedState.searchSuggestionIconColor);
        setSuggestionTextColor(savedState.suggestionTextColor);
        setSuggestionSelectedTextColor(savedState.suggestionSelectedTextColor);
        setCardBackgroundColor(savedState.cardBackgroundColor);
        setBackgroundDimColor(savedState.backgroundDimColor);
        setCardElevation(savedState.cardElevation);
        setCardCornerRadius(savedState.cardCornerRadius);
        setBackgroundDimAmount(savedState.dimAmount);
        setInputQueryInternal(savedState.query, false);
        setQueryInputHint(savedState.inputHint);
        setDismissOnTouchOutside(savedState.isDismissibleOnTouchOutside);
        setSuggestionsDisabled(savedState.areSuggestionsDisabled);
        setDimBackground(savedState.shouldDimBehind);

        if(State.EXPANDED.equals(savedState.state)) {
            expand(false);
        } else {
            collapse(false);
        }
    }




    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState savedState = new SavedState(superState);

        // Saving our state
        savedState.queryInputHintColor = mQueryInputHintColor;
        savedState.queryInputTextColor = mQueryInputTextColor;
        savedState.queryInputCursorColor = mQueryInputCursorColor;
        savedState.inputBarIconColor = mInputBarIconColor;
        savedState.dividerColor = mDividerColor;
        savedState.progressBarColor = mProgressBarColor;
        savedState.suggestionIconColor = mSuggestionIconColor;
        savedState.recentSearchIconColor = mRecentSearchIconColor;
        savedState.searchSuggestionIconColor = mSearchSuggestionIconColor;
        savedState.suggestionTextColor = mSuggestionTextColor;
        savedState.suggestionSelectedTextColor = mSuggestionSelectedTextColor;
        savedState.cardBackgroundColor = mCardBackgroundColor;
        savedState.backgroundDimColor = mBackgroundDimColor;
        savedState.cardElevation = mCardElevation;
        savedState.cardCornerRadius = mCardCornerRadius;
        savedState.dimAmount = mDimAmount;
        savedState.query = getInputQuery();
        savedState.inputHint = mInputEt.getHint().toString();
        savedState.state = mState;
        savedState.isDismissibleOnTouchOutside = mIsDismissibleOnTouchOutside;
        savedState.areSuggestionsDisabled = mAreSuggestionsDisabled;
        savedState.shouldDimBehind = mShouldDimBehind;

        return savedState;
    }




    private static class SavedState extends BaseSavedState {

        private static final String KEY_QUERY_INPUT_HINT_COLOR = "query_input_hint_color";
        private static final String KEY_QUERY_INPUT_TEXT_COLOR = "query_input_text_color";
        private static final String KEY_QUERY_INPUT_CURSOR_COLOR = "query_input_cursor_color";
        private static final String KEY_INPUT_BAR_ICON_COLOR = "input_bar_icon_color";
        private static final String KEY_DIVIDER_COLOR = "divider_color";
        private static final String KEY_PROGRESS_BAR_COLOR = "progress_bar_color";
        private static final String KEY_SUGGESTION_ICON_COLOR = "suggestion_icon_color";
        private static final String KEY_RECENT_SEARCH_ICON_COLOR = "recent_search_icon_color";
        private static final String KEY_SEARCH_SUGGESTION_ICON_COLOR = "search_suggestion_icon_color";
        private static final String KEY_SUGGESTION_TEXT_COLOR = "suggestion_text_color";
        private static final String KEY_SUGGESTION_SELECTED_TEXT_COLOR = "suggestion_selected_text_color";
        private static final String KEY_CARD_BACKGROUND_COLOR = "card_background_color";
        private static final String KEY_BACKGROUND_DIM_COLOR = "background_dim_color";
        private static final String KEY_CARD_CORNER_RADIUS = "card_corner_radius";
        private static final String KEY_CARD_ELEVATION = "card_elevation";
        private static final String KEY_DIM_AMOUNT = "dim_amount";
        private static final String KEY_QUERY = "query";
        private static final String KEY_INPUT_HINT = "input_hint";
        private static final String KEY_STATE = "state";
        private static final String KEY_IS_DISMISSIBLE_ON_TOUCH_OUTSIDE = "is_dismissible_on_touch_outside";
        private static final String KEY_ARE_SUGGESTIONS_DISABLED = "are_suggestions_disabled";
        private static final String KEY_SHOULD_DIM_BEHIND = "should_dim_behind";

        private int queryInputHintColor;
        private int queryInputTextColor;
        private int queryInputCursorColor;
        private int inputBarIconColor;
        private int dividerColor;
        private int progressBarColor;
        private int suggestionIconColor;
        private int recentSearchIconColor;
        private int searchSuggestionIconColor;
        private int suggestionTextColor;
        private int suggestionSelectedTextColor;
        private int cardBackgroundColor;
        private int backgroundDimColor;

        private int cardCornerRadius;
        private int cardElevation;

        private float dimAmount;

        private String query;
        private String inputHint;

        private State state;

        private boolean isDismissibleOnTouchOutside;
        private boolean areSuggestionsDisabled;
        private boolean shouldDimBehind;


        private SavedState(Parcelable superState) {
            super(superState);
        }


        private SavedState(Parcel parcel) {
            super(parcel);

            // Restoring the data
            final Bundle bundle = parcel.readBundle(getClass().getClassLoader());
            this.queryInputHintColor = bundle.getInt(KEY_QUERY_INPUT_HINT_COLOR);
            this.queryInputTextColor = bundle.getInt(KEY_QUERY_INPUT_TEXT_COLOR);
            this.queryInputCursorColor = bundle.getInt(KEY_QUERY_INPUT_CURSOR_COLOR);
            this.inputBarIconColor = bundle.getInt(KEY_INPUT_BAR_ICON_COLOR);
            this.dividerColor = bundle.getInt(KEY_DIVIDER_COLOR);
            this.progressBarColor = bundle.getInt(KEY_PROGRESS_BAR_COLOR);
            this.suggestionIconColor = bundle.getInt(KEY_SUGGESTION_ICON_COLOR);
            this.recentSearchIconColor = bundle.getInt(KEY_RECENT_SEARCH_ICON_COLOR);
            this.searchSuggestionIconColor = bundle.getInt(KEY_SEARCH_SUGGESTION_ICON_COLOR);
            this.suggestionTextColor = bundle.getInt(KEY_SUGGESTION_TEXT_COLOR);
            this.suggestionSelectedTextColor = bundle.getInt(KEY_SUGGESTION_SELECTED_TEXT_COLOR);
            this.cardBackgroundColor = bundle.getInt(KEY_CARD_BACKGROUND_COLOR);
            this.backgroundDimColor = bundle.getInt(KEY_BACKGROUND_DIM_COLOR);
            this.cardCornerRadius = bundle.getInt(KEY_CARD_CORNER_RADIUS);
            this.cardElevation = bundle.getInt(KEY_CARD_ELEVATION);
            this.dimAmount = bundle.getFloat(KEY_DIM_AMOUNT, DEFAULT_DIM_AMOUNT);
            this.query = bundle.getString(KEY_QUERY);
            this.inputHint = bundle.getString(KEY_INPUT_HINT);
            this.state = (State) bundle.getSerializable(KEY_STATE);
            this.isDismissibleOnTouchOutside = bundle.getBoolean(KEY_IS_DISMISSIBLE_ON_TOUCH_OUTSIDE, true);
            this.areSuggestionsDisabled = bundle.getBoolean(KEY_ARE_SUGGESTIONS_DISABLED, false);
            this.shouldDimBehind = bundle.getBoolean(KEY_SHOULD_DIM_BEHIND, true);
        }


        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            super.writeToParcel(parcel, flags);

            // Saving the data inside the bundle
            final Bundle bundle = new Bundle();
            bundle.putInt(KEY_QUERY_INPUT_HINT_COLOR, this.queryInputHintColor);
            bundle.putInt(KEY_QUERY_INPUT_TEXT_COLOR, this.queryInputTextColor);
            bundle.putInt(KEY_QUERY_INPUT_CURSOR_COLOR, this.queryInputCursorColor);
            bundle.putInt(KEY_INPUT_BAR_ICON_COLOR, this.inputBarIconColor);
            bundle.putInt(KEY_DIVIDER_COLOR, this.dividerColor);
            bundle.putInt(KEY_PROGRESS_BAR_COLOR, this.progressBarColor);
            bundle.putInt(KEY_SUGGESTION_ICON_COLOR, this.suggestionIconColor);
            bundle.putInt(KEY_RECENT_SEARCH_ICON_COLOR, this.recentSearchIconColor);
            bundle.putInt(KEY_SEARCH_SUGGESTION_ICON_COLOR, this.searchSuggestionIconColor);
            bundle.putInt(KEY_SUGGESTION_TEXT_COLOR, this.suggestionTextColor);
            bundle.putInt(KEY_SUGGESTION_SELECTED_TEXT_COLOR, this.suggestionSelectedTextColor);
            bundle.putInt(KEY_CARD_BACKGROUND_COLOR, this.cardBackgroundColor);
            bundle.putInt(KEY_BACKGROUND_DIM_COLOR, this.backgroundDimColor);
            bundle.putInt(KEY_CARD_CORNER_RADIUS, this.cardCornerRadius);
            bundle.putInt(KEY_CARD_ELEVATION, this.cardElevation);
            bundle.putFloat(KEY_DIM_AMOUNT, this.dimAmount);
            bundle.putString(KEY_QUERY, this.query);
            bundle.putString(KEY_INPUT_HINT, this.inputHint);
            bundle.putSerializable(KEY_STATE, this.state);
            bundle.putBoolean(KEY_IS_DISMISSIBLE_ON_TOUCH_OUTSIDE, this.isDismissibleOnTouchOutside);
            bundle.putBoolean(KEY_ARE_SUGGESTIONS_DISABLED, this.areSuggestionsDisabled);
            bundle.putBoolean(KEY_SHOULD_DIM_BEHIND, this.shouldDimBehind);

            parcel.writeBundle(bundle);
        }


        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }

        };


    }




}
