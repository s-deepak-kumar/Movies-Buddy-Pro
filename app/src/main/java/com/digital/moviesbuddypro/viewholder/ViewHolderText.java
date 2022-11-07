package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.R;

public class ViewHolderText extends RecyclerView.ViewHolder {

    public TextView mTitleTextView;
    public LinearLayout linearLayout;

    public ViewHolderText(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        mTitleTextView = (TextView) findViewById(R.id.title);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}