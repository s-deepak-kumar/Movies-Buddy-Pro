package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ViewHolderCategory extends RecyclerView.ViewHolder {

    public ImageView mCategoryBanner;
    public TextView mCategoryTitle;
    public CardView mCardView;

    public ViewHolderCategory(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        mCategoryBanner = (ImageView) findViewById(R.id.categoryBanner);
        mCategoryTitle = (TextView) findViewById(R.id.categoryTitle);
        mCardView = (CardView) findViewById(R.id.cardView);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}