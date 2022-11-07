package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ViewHolderEpisode extends RecyclerView.ViewHolder {

    public TextView episodeTitle;
    public ImageView seasonPoster;
    public CardView cardView;

    public ViewHolderEpisode(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        episodeTitle = (TextView) findViewById(R.id.episodeTitle);
        seasonPoster = (ImageView) findViewById(R.id.seasonPoster);
        cardView = (CardView) findViewById(R.id.cardView);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}