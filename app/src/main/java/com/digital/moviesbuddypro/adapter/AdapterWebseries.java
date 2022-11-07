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
import com.digital.moviesbuddypro.model.WebSeries;
import com.digital.moviesbuddypro.sqlitedatabase.SQLiteDB;
import com.digital.moviesbuddypro.activity.ActivityWebseriesDetails;
import com.digital.moviesbuddypro.viewholder.ViewHolderWebseries;

import java.util.List;

public class AdapterWebseries extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WebSeries> mListWebseries;
    private Context mContext;
    private int mLayout;

    public AdapterWebseries(Context mContext, List<WebSeries> mListWebseries, int mLayout) {
        this.mContext = mContext;
        this.mListWebseries = mListWebseries;
        this.mLayout = mLayout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        return new ViewHolderWebseries(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderWebseries) {
            populateWebseriesItem((ViewHolderWebseries) holder, position);
        }
    }

    private void populateWebseriesItem(final ViewHolderWebseries holder, final int position) {

        Glide.with(mContext)
                .load(mListWebseries.get(position).getMainPoster())
                /*.placeholder(R.drawable.ic_play)
                .error(R.drawable.ic_share)
                .priority( Priority.HIGH )*/
                .into(holder.webseriesPoster);

        holder.webseriesTitle.setText(mListWebseries.get(position).getTitle());
        holder.webseriesRating.setText(mListWebseries.get(position).getIMDBRating());
        holder.webseriesSeasonTag.setText(mListWebseries.get(position).getSeasonTag());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDB.addWebseriesToRecent(mContext, mListWebseries.get(position).getId(), mListWebseries.get(position).getTitle(),
                        mListWebseries.get(position).getMainPoster(),
                        mListWebseries.get(position).getIMDBRating(), mListWebseries.get(position).getSeasonTag(),
                        mListWebseries.get(position).getCertificate());

                String mId = mListWebseries.get(position).getId();
                Intent intent = new Intent(mContext, ActivityWebseriesDetails.class);
                intent.putExtra("WEBSERIES_ID_KEY", mId);
                intent.putExtra("WEBSERIES_SEASON_TAG_KEY", mListWebseries.get(position).getSeasonTag());
                intent.putExtra("WEBSERIES_TITLE_KEY", mListWebseries.get(position).getTitle());
                intent.putExtra("WEBSERIES_CERTIFICATE_KEY", mListWebseries.get(position).getCertificate());
                intent.putExtra("WEBSERIES_POSTER_KEY", mListWebseries.get(position).getMainPoster());
                intent.putExtra("WEBSERIES_IMDB_RATING_KEY", mListWebseries.get(position).getIMDBRating());
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
        return /*movieObjects == null ? 0 : */mListWebseries.size();
    }

    public void clear() {
        int size = mListWebseries.size();
        mListWebseries.clear();
        notifyItemRangeRemoved(0, size);
    }
}