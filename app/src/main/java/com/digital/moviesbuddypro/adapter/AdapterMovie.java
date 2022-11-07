package com.digital.moviesbuddypro.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digital.moviesbuddypro.activity.ActivityMovieDetails;
import com.digital.moviesbuddypro.model.Movie;
import com.digital.moviesbuddypro.sqlitedatabase.SQLiteDB;
import com.digital.moviesbuddypro.viewholder.ViewHolderMovie;

import java.util.List;

public class AdapterMovie extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> mListMovie;
    private Context mContext;
    private String mCategory;
    private int mLayout;

    public AdapterMovie(Context mContext, List<Movie> mListMovie, String mCategory, int mLayout) {
        this.mContext = mContext;
        this.mListMovie = mListMovie;
        this.mCategory = mCategory;
        this.mLayout = mLayout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        return new ViewHolderMovie(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderMovie) {
            populateMovieItem((ViewHolderMovie) holder, position);
        }
    }

    private void populateMovieItem(final ViewHolderMovie holder, final int position) {

        Glide.with(mContext)
                .load(mListMovie.get(position).getPoster())
                /*.placeholder(R.drawable.ic_play)
                .error(R.drawable.ic_share)
                .priority( Priority.HIGH )*/
                .into(holder.moviePoster);

        holder.movieTitle.setText(mListMovie.get(position).getTitle());
        holder.movieRating.setText(mListMovie.get(position).getIMDBRating());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDB.addMovieToRecent(mContext, mListMovie.get(position).getId(), mListMovie.get(position).getTitle(),
                        mListMovie.get(position).getPoster(), mCategory, mListMovie.get(position).getIMDBRating(),
                        mListMovie.get(position).getCertificate());

                String mId = mListMovie.get(position).getId();
                Intent intent = new Intent(mContext, ActivityMovieDetails.class);
                intent.putExtra("MOVIE_ID_KEY", mId);
                intent.putExtra("MOVIE_CATEGORY_KEY", mCategory);
                intent.putExtra("MOVIE_TITLE_KEY", mListMovie.get(position).getTitle());
                intent.putExtra("MOVIE_CERTIFICATE_KEY", mListMovie.get(position).getCertificate());
                intent.putExtra("MOVIE_POSTER_KEY", mListMovie.get(position).getPoster());
                intent.putExtra("MOVIE_IMDB_RATING_KEY", mListMovie.get(position).getIMDBRating());
                mContext.startActivity(intent);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return /*movieObjects == null ? 0 : */mListMovie.size();
    }

    public void clear() {
        int size = mListMovie.size();
        mListMovie.clear();
        notifyItemRangeRemoved(0, size);
    }
}