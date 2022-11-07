package com.digital.moviesbuddypro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.model.Episode;
import com.digital.moviesbuddypro.viewholder.ViewHolderEpisode;

import java.util.List;

public class AdapterEpisode extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Episode> mListEpisodes;
    private Context mContext;
    private ItemOnClikListener mItemOnClikListener;

    public interface ItemOnClikListener{
        void onClick(int position);
    }

    public AdapterEpisode(Context mContext, List<Episode> mListEpisodes, ItemOnClikListener mItemOnClikListener) {
        this.mContext = mContext;
        this.mListEpisodes = mListEpisodes;
        this.mItemOnClikListener = mItemOnClikListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episode, parent, false);
        return new ViewHolderEpisode(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderEpisode) {
            populateEpisodeItem((ViewHolderEpisode) holder, position);
        }
    }

    private void populateEpisodeItem(final ViewHolderEpisode holder, final int position) {

        Glide.with(mContext)
                .load(mListEpisodes.get(position).getSeasonPoster())
                .into(holder.seasonPoster);

        holder.episodeTitle.setText(mListEpisodes.get(position).getEpisodeTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.itemView.performClick();
                mItemOnClikListener.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return /*movieObjects == null ? 0 : */mListEpisodes.size();
    }

    public void clear() {
        int size = mListEpisodes.size();
        mListEpisodes.clear();
        notifyItemRangeRemoved(0, size);
    }
}