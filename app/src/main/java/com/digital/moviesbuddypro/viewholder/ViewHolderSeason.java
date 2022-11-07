package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ViewHolderSeason extends RecyclerView.ViewHolder {

    public TextView seasonTitle;
    public ImageView seasonPoster;
    public CardView cardView;

    public ViewHolderSeason(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        seasonTitle = (TextView) findViewById(R.id.seasonTitle);
        seasonPoster = (ImageView) findViewById(R.id.seasonPoster);
        cardView = (CardView) findViewById(R.id.cardView);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}