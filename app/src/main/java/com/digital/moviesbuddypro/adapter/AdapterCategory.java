package com.digital.moviesbuddypro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.model.Category;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.activity.ActivityAllCategory;
import com.digital.moviesbuddypro.activity.ActivityCategoryWebseries;
import com.digital.moviesbuddypro.viewholder.ViewHolderCategory;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Category> mListCategory;
    private Context mContext;

    public AdapterCategory(Context mContext, List<Category> mListCategory) {
        this.mContext = mContext;
        this.mListCategory = mListCategory;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolderCategory(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderCategory) {
            populateTextItem((ViewHolderCategory) holder, position);
        }
    }

    private void populateTextItem(final ViewHolderCategory holder, final int position) {

        holder.mCategoryTitle.setText(mListCategory.get(position).getCategoryTitle());
        holder.mCategoryBanner.setImageResource(mListCategory.get(position).getCategoryBannerInt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListCategory.get(position).getCategoryTitle().equalsIgnoreCase("Web Series")){
                    Intent intent = new Intent(mContext, ActivityCategoryWebseries.class);
                    mContext.startActivity(intent);
                }else {
                    Intent intent = new Intent(mContext, ActivityAllCategory.class);
                    intent.putExtra("CATEGORY_KEY", mListCategory.get(position).getCategoryTitle());
                    intent.putExtra("CATEGORY_BANNER_KEY", mListCategory.get(position).getCategoryBannerInt());
                    mContext.startActivity(intent);
                }
            }
        });

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.performClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return /*movieObjects == null ? 0 : */mListCategory.size();
    }

    public void clear() {
        int size = mListCategory.size();
        mListCategory.clear();
        notifyItemRangeRemoved(0, size);
    }
}