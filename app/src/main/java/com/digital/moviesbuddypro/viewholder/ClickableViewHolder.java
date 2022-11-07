package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ClickableViewHolder extends RecyclerView.ViewHolder {

    public TextView mClickableTitle;

    public ClickableViewHolder(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        mClickableTitle = (TextView) findViewById(R.id.clickableTitle);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}
