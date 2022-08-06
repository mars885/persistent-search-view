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

package com.paulrybitskyi.persistentsearchview.adapters.model;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arthurivanets.adapster.Adapter;
import com.arthurivanets.adapster.listeners.ItemClickListener;
import com.arthurivanets.adapster.listeners.OnItemClickListener;
import com.arthurivanets.adapster.model.BaseItem;
import com.arthurivanets.adapster.model.markers.Trackable;
import com.paulrybitskyi.persistentsearchview.R;
import com.paulrybitskyi.persistentsearchview.adapters.resources.SuggestionItemResources;
import com.paulrybitskyi.persistentsearchview.model.Suggestion;
import com.paulrybitskyi.persistentsearchview.utils.Preconditions;
import com.paulrybitskyi.persistentsearchview.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.makeGone;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.makeVisible;

/**
 * A recycler view item for a suggestion.
 */
public class SuggestionItem extends BaseItem<
    Suggestion,
    SuggestionItem.ViewHolder,
    SuggestionItemResources
> implements Trackable<Long> {


    /**
     * A constant holding a layout resource ID for the suggestion item.
     */
    public static final int MAIN_LAYOUT_ID = R.layout.view_persistent_search_suggestion_item;


    /**
     * Creates a suggestion item with the specified text.
     *
     * @param suggestionText The suggestion's text
     *
     * @return An instance of a suggestion item
     */
    public static SuggestionItem of(@NonNull String suggestionText) {
        return of(-1L, suggestionText);
    }


    /**
     * Creates a suggestion item with the specified id and text.
     *
     * @param id The suggestion's id
     * @param suggestionText The suggestion's text
     *
     * @return An instance of a suggestion item
     */
    public static SuggestionItem of(long id, @NonNull String suggestionText) {
        Preconditions.nonEmpty(suggestionText);

        return new SuggestionItem(
            new Suggestion()
                .setId(id)
                .setText(suggestionText)
        );
    }


    public SuggestionItem(Suggestion itemModel) {
        super(itemModel);
    }


    @Override
    @NonNull
    public ViewHolder init(
        Adapter adapter,
        ViewGroup parent,
        LayoutInflater inflater,
        SuggestionItemResources resources
    ) {
        return new ViewHolder(inflater.inflate(MAIN_LAYOUT_ID, parent, false));
    }


    @Override
    public void bind(
        @Nullable Adapter adapter,
        @NonNull ViewHolder viewHolder,
        @Nullable SuggestionItemResources resources
    ) {
        super.bind(adapter, viewHolder, resources);

        final Suggestion suggestion = getItemModel();
        final boolean isRecentSearchSuggestion = Suggestion.TYPE_RECENT_SEARCH_SUGGESTION.equals(suggestion.getType());

        bindText(viewHolder, resources);
        bindIcon(isRecentSearchSuggestion, viewHolder, resources);
        bindButton(isRecentSearchSuggestion, viewHolder, resources);
    }


    private void bindText(
        ViewHolder viewHolder,
        SuggestionItemResources resources
    ) {
        viewHolder.textTv.setTextColor(resources.getTextColor());
        handleText(viewHolder, resources);
    }


    private void bindIcon(
        boolean isRecentSearchSuggestion,
        ViewHolder viewHolder,
        SuggestionItemResources resources
    ) {
        viewHolder.iconIv.setImageDrawable(
            Utils.getColoredDrawable(
                viewHolder.iconIv.getContext(),
                (isRecentSearchSuggestion ? R.drawable.ic_history_black_24dp : R.drawable.ic_magnify_black_24dp),
                (isRecentSearchSuggestion ? resources.getRecentSearchIconColor() : resources.getSearchSuggestionIconColor())
            )
        );
    }


    private void handleText(ViewHolder viewHolder, SuggestionItemResources resources) {
        final Suggestion suggestion = getItemModel();
        final String text = suggestion.getText();
        final int startIndex = suggestion.getText().toLowerCase().indexOf(resources.getCurrentQuery().toLowerCase());
        final int endIndex = Math.min(resources.getCurrentQuery().length(), text.length());
        final boolean isCurrentQueryValid = !TextUtils.isEmpty(resources.getCurrentQuery());
        final boolean isStartIndexValid = (startIndex != -1);
        final boolean isEndIndexValid = (startIndex <= endIndex);

        if(isCurrentQueryValid && isStartIndexValid && isEndIndexValid) {
            final SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(
                new ForegroundColorSpan(resources.getSelectedTextColor()),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            viewHolder.textTv.setText(spannableString);
        } else {
            viewHolder.textTv.setText(text);
        }
    }


    private void bindButton(
        boolean isRecentSearchSuggestion,
        ViewHolder viewHolder,
        SuggestionItemResources resources
    ) {
        if(isRecentSearchSuggestion) {
            viewHolder.removeBtnIv.setImageDrawable(
                Utils.getColoredDrawable(
                    viewHolder.removeBtnIv.getContext(),
                    R.drawable.ic_close_black_24dp,
                    resources.getIconColor()
                )
            );
            makeVisible(viewHolder.removeBtnIv);
        } else {
            makeGone(viewHolder.removeBtnIv);
        }
    }


    /**
     * Sets a listener to invoke when the item is clicked.
     *
     * @param viewHolder The view holder
     * @param onItemClickListener The listener to set
     */
    public void setOnItemClickListener(
        ViewHolder viewHolder,
        OnItemClickListener<SuggestionItem> onItemClickListener
    ) {
        viewHolder.itemView.setOnClickListener(
            new ItemClickListener<>(
                this,
                viewHolder.getAdapterPosition(),
                onItemClickListener
            )
        );
    }


    /**
     * Sets a listener to invoke when the remove button is clicked.
     *
     * @param viewHolder The view holder
     * @param onItemRemoveButtonClickListener The listener to set
     */
    public void setOnItemRemoveButtonClickListener(
        ViewHolder viewHolder,
        OnItemClickListener<SuggestionItem> onItemRemoveButtonClickListener
    ) {
        viewHolder.removeBtnIv.setOnClickListener(
            new ItemClickListener<>(
                this,
                viewHolder.getAdapterPosition(),
                onItemRemoveButtonClickListener
            )
        );
    }


    @Override
    public int getLayout() {
        return MAIN_LAYOUT_ID;
    }


    @Override
    @NonNull
    public Long getTrackKey() {
        final Suggestion suggestion = getItemModel();
        return (suggestion.hasValidId() ? suggestion.getId() : ((long) suggestion.getText().hashCode()));
    }


    /**
     * A view holder containing suggestion item related views.
     */
    public static class ViewHolder extends BaseItem.ViewHolder<Suggestion> {

        private TextView textTv;

        private ImageView iconIv;
        private ImageView removeBtnIv;

        public ViewHolder(View itemView) {
            super(itemView);

            textTv = itemView.findViewById(R.id.textTv);
            iconIv = itemView.findViewById(R.id.iconIv);
            removeBtnIv = itemView.findViewById(R.id.removeBtnIv);
        }

    }


}
