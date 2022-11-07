package com.digital.moviesbuddypro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.digital.moviesbuddypro.activity.ActivityMovieDetails;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.activity.ActivityWebseriesDetails;
import com.digital.moviesbuddypro.model.LatestItemSlider;
import com.digital.moviesbuddypro.viewholder.ViewHolderTrendingSearch;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class AdapterTrendingSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<LatestItemSlider> mlistMovieTrending;

    public AdapterTrendingSearch(Context mContext, List<LatestItemSlider> mlistMovieTrending) {
        this.mlistMovieTrending = mlistMovieTrending;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending_search, parent, false);
        return new ViewHolderTrendingSearch(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder mainHolder, final int position) {
        final ViewHolderTrendingSearch holder = (ViewHolderTrendingSearch) mainHolder;

        String[] arrayColorsName = {"random_color_1", "random_color_2", "random_color_3", "random_color_4", "random_color_5",
                "random_color_6", "random_color_7", "random_color_8", "random_color_9", "random_color_10", "random_color_11",
                "random_color_12", "random_color_13", "random_color_14"};
        String mRandomColorName = arrayColorsName[new Random().nextInt(arrayColorsName.length)];
        //Toast.makeText(mContext, ""+mRandomColor, Toast.LENGTH_SHORT).show();

        holder.mTrendingSearchTitle.setText(capitalize(mlistMovieTrending.get(position).getItemTitle()));
        holder.mTrendingSearchTitle.setTextColor(ContextCompat.getColorStateList(mContext, getResId(mRandomColorName, R.color.class)));
        holder.mTrendingSearchTitle.setBackgroundTintList(ContextCompat.getColorStateList(mContext, getResId(mRandomColorName, R.color.class)));
        setTextViewDrawableColor(holder.mTrendingSearchTitle, getResId(mRandomColorName, R.color.class));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlistMovieTrending.get(position).getItemCategory().equalsIgnoreCase("Web Series")){
                    String mId = mlistMovieTrending.get(position).getId();
                    Intent intent = new Intent(mContext, ActivityWebseriesDetails.class);
                    intent.putExtra("WEBSERIES_ID_KEY", mId);
                    intent.putExtra("WEBSERIES_TITLE_KEY", mlistMovieTrending.get(position).getItemTitle());
                    intent.putExtra("WEBSERIES_CERTIFICATE_KEY", mlistMovieTrending.get(position).getItemCertificate());
                    intent.putExtra("WEBSERIES_POSTER_KEY", mlistMovieTrending.get(position).getItemPoster());
                    intent.putExtra("WEBSERIES_QUALITY_TYPE_KEY", mlistMovieTrending.get(position).getItemQualityType());
                    intent.putExtra("WEBSERIES_IMDB_RATING_KEY", mlistMovieTrending.get(position).getItemIMDBRating());
                    intent.putExtra("WEBSERIES_SEASON_TAG_KEY", mlistMovieTrending.get(position).getItemSeasonTag());
                    mContext.startActivity(intent);
                }else {
                    String mId = mlistMovieTrending.get(position).getId();
                    Intent intent = new Intent(mContext, ActivityMovieDetails.class);
                    intent.putExtra("MOVIE_ID_KEY", mId);
                    intent.putExtra("MOVIE_CATEGORY_KEY", mlistMovieTrending.get(position).getItemCategory());
                    intent.putExtra("MOVIE_TITLE_KEY", mlistMovieTrending.get(position).getItemTitle());
                    intent.putExtra("MOVIE_CERTIFICATE_KEY", mlistMovieTrending.get(position).getItemCertificate());
                    intent.putExtra("MOVIE_POSTER_KEY", mlistMovieTrending.get(position).getItemPoster());
                    intent.putExtra("MOVIE_QUALITY_TYPE_KEY", mlistMovieTrending.get(position).getItemQualityType());
                    intent.putExtra("MOVIE_IMDB_RATING_KEY", mlistMovieTrending.get(position).getItemIMDBRating());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlistMovieTrending.size();
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