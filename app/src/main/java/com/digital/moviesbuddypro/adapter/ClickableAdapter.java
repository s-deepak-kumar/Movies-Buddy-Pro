package com.digital.moviesbuddypro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.activity.ActivityExploredCinema;
import com.digital.moviesbuddypro.viewholder.ClickableViewHolder;
import com.digital.moviesbuddypro.R;

import java.util.ArrayList;

public class ClickableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mlistString;

    public ClickableAdapter(Context mContext, ArrayList<String> mlistString) {
        this.mlistString = mlistString;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clickable, parent, false);
        return new ClickableViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder mainHolder, final int position) {
        final ClickableViewHolder holder = (ClickableViewHolder) mainHolder;

        holder.mClickableTitle.setText(capitalize(mlistString.get(position)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityExploredCinema.class);
                intent.putExtra("SEARCHABLE_STRING_KEY", mlistString.get(position));
                intent.putExtra("TYPE_KEY", "mType");
                mContext.startActivity(intent);
            }
        });

        holder.mClickableTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityExploredCinema.class);
                intent.putExtra("SEARCHABLE_STRING_KEY", mlistString.get(position));
                intent.putExtra("TYPE_KEY", "mType");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlistString.size();
    }

    public static String capitalize (String givenString) {
        String Separateur = " ,.-;";
        StringBuilder sb = new StringBuilder();
        boolean ToCap = true;
        for (int i = 0; i < givenString.length(); i++) {
            if (ToCap)
                sb.append(Character.toUpperCase(givenString.charAt(i)));
            else
                sb.append(Character.toLowerCase(givenString.charAt(i)));

            ToCap = Separateur.indexOf(givenString.charAt(i)) >= 0;
        }
        return sb.toString().trim();
    }
}

