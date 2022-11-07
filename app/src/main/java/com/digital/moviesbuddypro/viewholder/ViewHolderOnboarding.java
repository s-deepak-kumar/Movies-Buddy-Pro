package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ViewHolderOnboarding extends RecyclerView.ViewHolder {

    public TextView mTitleTextView, mDescriptionTextView;
    public ImageView mPosterImageView;

    public ViewHolderOnboarding(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mDescriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        mPosterImageView = (ImageView) findViewById(R.id.posterImageView);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}
