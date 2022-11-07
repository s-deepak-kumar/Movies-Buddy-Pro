package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ViewHolderWebseries extends RecyclerView.ViewHolder {

    public TextView webseriesRating;
    public TextView webseriesTitle;
    public ImageView webseriesPoster;
    public TextView webseriesSeasonTag;
    public CardView cardView;

    public ViewHolderWebseries(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        webseriesRating = (TextView) findViewById(R.id.webseriesRating);
        webseriesTitle = (TextView) findViewById(R.id.webseriesTitle);
        webseriesPoster = (ImageView) findViewById(R.id.webseriesPoster);
        webseriesSeasonTag = (TextView) findViewById(R.id.webseriesSeasonTag);
        cardView = (CardView) findViewById(R.id.cardView);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}