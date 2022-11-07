package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ViewHolderMovie extends RecyclerView.ViewHolder {

    public TextView movieRating;
    public TextView movieTitle;
    public ImageView moviePoster;
    public CardView cardView;

    public ViewHolderMovie(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        movieRating = (TextView) findViewById(R.id.movieRating);
        movieTitle = (TextView) findViewById(R.id.movieTitle);
        moviePoster = (ImageView) findViewById(R.id.moviePoster);
        cardView = (CardView) findViewById(R.id.cardView);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}