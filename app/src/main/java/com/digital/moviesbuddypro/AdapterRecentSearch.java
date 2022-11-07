package com.digital.moviesbuddypro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.viewholder.ViewHolderRecentSearch;
import com.digital.moviesbuddypro.R;

import java.lang.reflect.Field;
import java.util.List;

public class AdapterRecentSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mlistRecentSearches;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int positon);
    }

    public AdapterRecentSearch(Context mContext, List<String> mlistRecentSearches, OnItemClickListener mOnItemClickListener) {
        this.mlistRecentSearches = mlistRecentSearches;
        this.mContext = mContext;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_search, parent, false);
        return new ViewHolderRecentSearch(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder mainHolder, final int position) {
        final ViewHolderRecentSearch holder = (ViewHolderRecentSearch) mainHolder;

        /*String[] arrayColorsName = {"random_color_1", "random_color_2", "random_color_3", "random_color_4", "random_color_5",
                "random_color_6", "random_color_7", "random_color_8", "random_color_9", "random_color_10", "random_color_11",
                "random_color_12", "random_color_13", "random_color_14"};
        String mRandomColorName = arrayColorsName[new Random().nextInt(arrayColorsName.length)];*/
        //Toast.makeText(mContext, ""+mRandomColor, Toast.LENGTH_SHORT).show();

        holder.mRecentSearchTitle.setText(capitalize(mlistRecentSearches.get(position)));
        /*holder.mTrendingSearchTitle.setTextColor(ContextCompat.getColorStateList(mContext, getResId(mRandomColorName, R.color.class)));
        holder.mTrendingSearchTitle.setBackgroundTintList(ContextCompat.getColorStateList(mContext, getResId(mRandomColorName, R.color.class)));
        setTextViewDrawableColor(holder.mTrendingSearchTitle, getResId(mRandomColorName, R.color.class));*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlistRecentSearches.size();
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

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}