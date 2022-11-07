package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ViewHolderTrendingSearch extends RecyclerView.ViewHolder {

    public TextView mTrendingSearchTitle;

    public ViewHolderTrendingSearch(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        mTrendingSearchTitle = (TextView) findViewById(R.id.trendingSearchTitle);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}
