package com.digital.moviesbuddypro.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.digital.moviesbuddypro.activity.ActivityMovieDetails;
import com.digital.moviesbuddypro.model.LatestItemSlider;
import com.digital.moviesbuddypro.sqlitedatabase.SQLiteDB;
import com.digital.moviesbuddypro.HelperClass;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.activity.ActivityWebseriesDetails;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterLatestItemSlider extends SliderViewAdapter<AdapterLatestItemSlider.SliderAdapterVH> {

    private Context context;
    private List<LatestItemSlider> mSliderItems = new ArrayList<>();

    public AdapterLatestItemSlider(Context context) {
        this.context = context;
    }

    public void renewItems(List<LatestItemSlider> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(LatestItemSlider sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        //final LatestItemSlider sliderItem = mSliderItems.get(position);

        /*viewHolder.textViewDescription.setText(sliderItem.getDescription());
        viewHolder.textViewDescription.setTextSize(16);
        viewHolder.textViewDescription.setTextColor(Color.WHITE);*/

        Glide.with(context)
                .load(mSliderItems.get(position).getItemPoster())
                .fitCenter()
                .into(viewHolder.sliderBackImage);

        Glide.with(context)
                .load(mSliderItems.get(position).getItemPoster())
                .fitCenter()
                .into(viewHolder.sliderMainImage);

        viewHolder.sliderTitle.setText(mSliderItems.get(position).getItemTitle());

        if (mSliderItems.get(position).getItemCategory().equalsIgnoreCase("Web Series")){
            if (SQLiteDB.isWebseriesFav(context, mSliderItems.get(position).getId())) {
                viewHolder.mAddOutFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.ic_baseline_horizontal_rule_24, 0, 0);
                viewHolder.mAddOutFavouriteButton.setText("Out From\nFavourite");
            } else {
                viewHolder.mAddOutFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.ic_baseline_add_24, 0, 0);
                viewHolder.mAddOutFavouriteButton.setText("Add To\nFavourite");
            }
        }else {
            if (SQLiteDB.isMovieFav(context, mSliderItems.get(position).getId())) {
                viewHolder.mAddOutFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.ic_baseline_horizontal_rule_24, 0, 0);
                viewHolder.mAddOutFavouriteButton.setText("Out From\nFavourite");
            } else {
                viewHolder.mAddOutFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.ic_baseline_add_24, 0, 0);
                viewHolder.mAddOutFavouriteButton.setText("Add To\nFavourite");
            }
        }

        viewHolder.mAddOutFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSliderItems.get(position).getItemCategory().equalsIgnoreCase("Web Series")){
                    if (SQLiteDB.isWebseriesFav(context, mSliderItems.get(position).getId())) {
                        SQLiteDB.removeWebseriesFromFav(context, mSliderItems.get(position).getId());
                        viewHolder.mAddOutFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,
                                R.drawable.ic_baseline_add_24, 0, 0);
                        viewHolder.mAddOutFavouriteButton.setText("Add To\nFavourite");
                        Toast.makeText(context, mSliderItems.get(position).getItemTitle() + " Removed From Favourite.", Toast.LENGTH_SHORT).show();
                    } else {
                        SQLiteDB.addWebseriesToFav(context, mSliderItems.get(position).getId(), mSliderItems.get(position).getItemTitle(),
                                mSliderItems.get(position).getItemPoster(),
                                mSliderItems.get(position).getItemIMDBRating(), mSliderItems.get(position).getItemSeasonTag(),
                                mSliderItems.get(position).getItemCertificate());
                        viewHolder.mAddOutFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,
                                R.drawable.ic_baseline_horizontal_rule_24, 0, 0);
                        viewHolder.mAddOutFavouriteButton.setText("Out From\nFavourite");
                        Toast.makeText(context, mSliderItems.get(position).getItemTitle() + " Added To Favourite.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (SQLiteDB.isMovieFav(context, mSliderItems.get(position).getId())) {
                        SQLiteDB.removeMovieFromFav(context, mSliderItems.get(position).getId());
                        viewHolder.mAddOutFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,
                                R.drawable.ic_baseline_add_24, 0, 0);
                        viewHolder.mAddOutFavouriteButton.setText("Add To\nFavourite");
                        Toast.makeText(context, mSliderItems.get(position).getItemTitle() + " Removed From Favourite.", Toast.LENGTH_SHORT).show();
                    } else {
                        SQLiteDB.addMovieToFav(context, mSliderItems.get(position).getId(), mSliderItems.get(position).getItemTitle(),
                                mSliderItems.get(position).getItemPoster(), mSliderItems.get(position).getItemCategory(),
                                mSliderItems.get(position).getItemIMDBRating(), mSliderItems.get(position).getItemCertificate());
                        viewHolder.mAddOutFavouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,
                                R.drawable.ic_baseline_horizontal_rule_24, 0, 0);
                        viewHolder.mAddOutFavouriteButton.setText("Out From\nFavourite");
                        Toast.makeText(context, mSliderItems.get(position).getItemTitle() + " Added To Favourite.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        viewHolder.mShareToFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.shareApp(context, viewHolder.sliderMainImage, mSliderItems.get(position).getItemTitle(), (Activity) context);
            }
        });

        viewHolder.mPlayNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailsActivity(position);
            }
        });

        viewHolder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailsActivity(position);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailsActivity(position);
            }
        });
    }

    public String getIdOfSlider(int position){
        if (getCount() != 0){
            return mSliderItems.get(position).getId();
        }else {
            return "0";
        }
    }

    private void goToDetailsActivity(int position){
        if (mSliderItems.get(position).getItemCategory().equalsIgnoreCase("Web Series")){
            String mId = mSliderItems.get(position).getId();
            Intent intent = new Intent(context, ActivityWebseriesDetails.class);
            intent.putExtra("WEBSERIES_ID_KEY", mId);
            intent.putExtra("WEBSERIES_TITLE_KEY", mSliderItems.get(position).getItemTitle());
            intent.putExtra("WEBSERIES_CERTIFICATE_KEY", mSliderItems.get(position).getItemCertificate());
            intent.putExtra("WEBSERIES_POSTER_KEY", mSliderItems.get(position).getItemPoster());
            intent.putExtra("WEBSERIES_QUALITY_TYPE_KEY", mSliderItems.get(position).getItemQualityType());
            intent.putExtra("WEBSERIES_IMDB_RATING_KEY", mSliderItems.get(position).getItemIMDBRating());
            intent.putExtra("WEBSERIES_SEASON_TAG_KEY", mSliderItems.get(position).getItemSeasonTag());
            context.startActivity(intent);
        }else {
            String mId = mSliderItems.get(position).getId();
            Intent intent = new Intent(context, ActivityMovieDetails.class);
            intent.putExtra("MOVIE_ID_KEY", mId);
            intent.putExtra("MOVIE_CATEGORY_KEY", mSliderItems.get(position).getItemCategory());
            intent.putExtra("MOVIE_TITLE_KEY", mSliderItems.get(position).getItemTitle());
            intent.putExtra("MOVIE_CERTIFICATE_KEY", mSliderItems.get(position).getItemCertificate());
            intent.putExtra("MOVIE_POSTER_KEY", mSliderItems.get(position).getItemPoster());
            intent.putExtra("MOVIE_QUALITY_TYPE_KEY", mSliderItems.get(position).getItemQualityType());
            intent.putExtra("MOVIE_IMDB_RATING_KEY", mSliderItems.get(position).getItemIMDBRating());
            context.startActivity(intent);
        }
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends ViewHolder {

        View itemView;
        ImageView sliderBackImage;
        ImageView sliderMainImage;
        CardView itemCard;
        TextView sliderTitle;
        TextView mPlayNowButton;
        TextView mAddOutFavouriteButton;
        TextView mShareToFriendsButton;
        /*ImageView imageGifContainer;
        TextView textViewDescription;*/

        public SliderAdapterVH(View itemView) {
            super(itemView);
            sliderBackImage = itemView.findViewById(R.id.sliderBackImage);
            sliderMainImage = itemView.findViewById(R.id.sliderMainImage);
            itemCard = itemView.findViewById(R.id.itemCard);
            sliderTitle = itemView.findViewById(R.id.sliderTitle);
            mPlayNowButton = itemView.findViewById(R.id.playNowButton);
            mAddOutFavouriteButton = itemView.findViewById(R.id.addOutFavouriteButton);
            mShareToFriendsButton = itemView.findViewById(R.id.shareToFriendsButton);
            this.itemView = itemView;
        }
    }

}
