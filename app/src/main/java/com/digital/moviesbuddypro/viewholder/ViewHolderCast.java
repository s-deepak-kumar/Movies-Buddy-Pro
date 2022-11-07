package com.digital.moviesbuddypro.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.digital.moviesbuddypro.R;

public class ViewHolderCast extends RecyclerView.ViewHolder {

    public TextView castName;
    public ImageView castImage;
    public CardView cardView;

    public ViewHolderCast(View itemView) {
        super(itemView);
        assignViews();
    }

    private void assignViews() {
        castName = (TextView) findViewById(R.id.castName);
        castImage = (CircularImageView) findViewById(R.id.castImage);
        cardView = (CardView) findViewById(R.id.cardView);
    }

    private View findViewById(final int id) {
        return itemView.findViewById(id);
    }
}