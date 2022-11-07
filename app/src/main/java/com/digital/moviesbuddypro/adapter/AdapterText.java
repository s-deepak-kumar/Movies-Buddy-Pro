package com.digital.moviesbuddypro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.activity.ActivityExploredCinema;
import com.digital.moviesbuddypro.viewholder.ViewHolderText;

import java.util.List;

public class AdapterText extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mListString;
    private Context mContext;
    private String mType;
    private int mLayout;

    public AdapterText(Context mContext, List<String> mListString, String mType, int mLayout) {
        this.mContext = mContext;
        this.mListString = mListString;
        this.mType = mType;
        this.mLayout = mLayout;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        return new ViewHolderText(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderText) {
            populateTextItem((ViewHolderText) holder, position);
        }
    }

    private void populateTextItem(final ViewHolderText holder, final int position) {

        holder.mTitleTextView.setText(mListString.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityExploredCinema.class);
                intent.putExtra("SEARCHABLE_STRING_KEY", mListString.get(position));
                intent.putExtra("TYPE_KEY", mType);
                mContext.startActivity(intent);
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return /*movieObjects == null ? 0 : */mListString.size();
    }

    public void clear() {
        int size = mListString.size();
        mListString.clear();
        notifyItemRangeRemoved(0, size);
    }
}