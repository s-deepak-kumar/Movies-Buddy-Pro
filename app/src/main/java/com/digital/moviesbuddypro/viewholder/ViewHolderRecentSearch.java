package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ViewHolderRecentSearch extends RecyclerView.ViewHolder {

    public TextView mRecentSearchTitle;

    public ViewHolderRecentSearch(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        mRecentSearchTitle = (TextView) findViewById(R.id.recentSearchTitle);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}
