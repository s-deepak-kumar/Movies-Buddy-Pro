package com.digital.moviesbuddypro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digital.moviesbuddypro.activity.ActivityExploredCinema;
import com.digital.moviesbuddypro.viewholder.ViewHolderCast;
import com.digital.moviesbuddypro.model.Cast;

import java.util.List;

public class AdapterCast extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Cast> mListCast;
    private Context mContext;
    private int mLayout;

    public AdapterCast(Context mContext, List<Cast> mListCast, int mLayout) {
        this.mContext = mContext;
        this.mListCast = mListCast;
        this.mLayout = mLayout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        return new ViewHolderCast(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderCast) {
            populateCastItem((ViewHolderCast) holder, position);
        }
    }

    private void populateCastItem(final ViewHolderCast holder, final int position) {

        Glide.with(mContext)
                .load(mListCast.get(position).getImage())
                /*.placeholder(R.drawable.ic_play)
                .error(R.drawable.ic_share)
                .priority( Priority.HIGH )*/
                .into(holder.castImage);

        holder.castName.setText(mListCast.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ActivityExploredCinema.class);
                intent.putExtra("SEARCHABLE_STRING_KEY", mListCast.get(position).getName());
                intent.putExtra("TYPE_KEY", "Cast");
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
        return mListCast.size();
    }

    public void clear() {
        int size = mListCast.size();
        mListCast.clear();
        notifyItemRangeRemoved(0, size);
    }
}