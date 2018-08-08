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

import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.makeGone;
import static com.paulrybitskyi.persistentsearchview.utils.ViewUtils.makeVisible;

/**
 * A recycler view item for a suggestion.
 */
public class SuggestionItem extends BaseItem<Suggestion, SuggestionItem.ViewHolder, SuggestionItemResources> implements Trackable<Long> {


    /**
     * A constant holding a layout resource ID for the suggestion item.
     */
    public static final int MAIN_LAYOUT_ID = R.layout.persistent_search_view_suggestion_item_layout;




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
    public ViewHolder init(Adapter adapter,
                           ViewGroup parent,
                           LayoutInflater inflater,
                           SuggestionItemResources resources) {
        final View view = inflater.inflate(MAIN_LAYOUT_ID, parent, false);
        return new ViewHolder(view);
    }




    @Override
    public void bind(Adapter adapter,
                     ViewHolder viewHolder,
                     SuggestionItemResources resources) {
        final Suggestion suggestion = getItemModel();
        final boolean isRecentSearchSuggestion = Suggestion.TYPE_RECENT_SEARCH_SUGGESTION.equals(suggestion.getType());

        // text related
        viewHolder.mTextTv.setTextColor(resources.getTextColor());
        handleText(viewHolder, resources);

        // icon related
        viewHolder.mIconIv.setImageDrawable(Utils.getColoredDrawable(
            viewHolder.mIconIv.getContext(),
            (isRecentSearchSuggestion ? R.drawable.ic_history_black_24dp : R.drawable.ic_magnify_black_24dp),
            (isRecentSearchSuggestion ? resources.getRecentSearchIconColor() : resources.getSearchSuggestionIconColor())
        ));

        // remove button related
        if(isRecentSearchSuggestion) {
            viewHolder.mRemoveBtnIv.setImageDrawable(Utils.getColoredDrawable(
                viewHolder.mRemoveBtnIv.getContext(),
                R.drawable.ic_close_black_24dp,
                resources.getIconColor()
            ));
            makeVisible(viewHolder.mRemoveBtnIv);
        } else {
            makeGone(viewHolder.mRemoveBtnIv);
        }
    }




    private void handleText(ViewHolder viewHolder, SuggestionItemResources resources) {
        final Suggestion suggestion = getItemModel();
        final String text = suggestion.getText();
        final int startIndex = suggestion.getText().toLowerCase().indexOf(resources.getCurrentQuery().toLowerCase());
        final int endIndex = Math.min(resources.getCurrentQuery().length(), text.length());

        if(!TextUtils.isEmpty(resources.getCurrentQuery())
                && (startIndex != -1)
                && (startIndex <= endIndex)) {
            final SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(
                new ForegroundColorSpan(resources.getSelectedTextColor()),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            viewHolder.mTextTv.setText(spannableString);
        } else {
            viewHolder.mTextTv.setText(text);
        }
    }




    /**
     * Sets a listener to invoke when the item is clicked.
     *
     * @param viewHolder The view holder
     * @param onItemClickListener The listener to set
     */
    public void setOnItemClickListener(ViewHolder viewHolder, OnItemClickListener<SuggestionItem> onItemClickListener) {
        viewHolder.itemView.setOnClickListener(new ItemClickListener<>(
            this,
            viewHolder.getAdapterPosition(),
            onItemClickListener
        ));
    }




    /**
     * Sets a listener to invoke when the remove button is clicked.
     *
     * @param viewHolder The view holder
     * @param onItemRemoveButtonClickListener The listener to set
     */
    public void setOnItemRemoveButtonClickListener(ViewHolder viewHolder, OnItemClickListener<SuggestionItem> onItemRemoveButtonClickListener) {
        viewHolder.mRemoveBtnIv.setOnClickListener(new ItemClickListener<>(
            this,
            viewHolder.getAdapterPosition(),
            onItemRemoveButtonClickListener
        ));
    }




    @Override
    public int getLayout() {
        return MAIN_LAYOUT_ID;
    }




    @Override
    public Long getTrackKey() {
        final Suggestion suggestion = getItemModel();
        return (suggestion.hasValidId() ? suggestion.getId() : ((long) suggestion.getText().hashCode()));
    }




    /**
     * A view holder containing suggestion item related views.
     */
    public static class ViewHolder extends BaseItem.ViewHolder<Suggestion> {

        private TextView mTextTv;

        private ImageView mIconIv;
        private ImageView mRemoveBtnIv;


        public ViewHolder(View itemView) {
            super(itemView);

            mTextTv = itemView.findViewById(R.id.textTv);
            mIconIv = itemView.findViewById(R.id.iconIv);
            mRemoveBtnIv = itemView.findViewById(R.id.removeBtnIv);
        }


    }




}
