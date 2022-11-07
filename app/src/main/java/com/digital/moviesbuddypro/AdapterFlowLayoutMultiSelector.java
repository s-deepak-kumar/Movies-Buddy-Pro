package com.digital.moviesbuddypro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

import java.util.List;

public class AdapterFlowLayoutMultiSelector extends RecyclerView.Adapter<AdapterFlowLayoutMultiSelector.MyViewHolder> {

    private Context mContext;
    public List<String> mListString;
    public List<String> mListStringSelected;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemTitle;

        public MyViewHolder(View view) {
            super(view);
            mItemTitle = (TextView) view.findViewById(R.id.itemTitle);

        }
    }


    public AdapterFlowLayoutMultiSelector(Context mContext, List<String> mListString, List<String> mListStringSelected) {
        this.mContext = mContext;
        this.mListString = mListString;
        this.mListStringSelected = mListStringSelected;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flowlayout_select, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mItemTitle.setText(mListString.get(position));

        if (mListStringSelected.contains(mListString.get(position))) {
            holder.mItemTitle.setBackground(mContext.getDrawable(R.drawable.flowlayout_select_bg));
            holder.mItemTitle.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.mItemTitle.setBackground(mContext.getDrawable(R.drawable.flowlayout_unselect_bg));
            holder.mItemTitle.setTextColor(mContext.getResources().getColor(R.color.secondary_icon_color));
        }


    }

    @Override
    public int getItemCount() {
        return mListString.size();
    }
}