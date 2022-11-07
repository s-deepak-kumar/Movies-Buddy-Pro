package com.digital.moviesbuddypro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.digital.moviesbuddypro.model.Onboarding;
import com.digital.moviesbuddypro.R;

import java.util.List;

public class AdapterOnboarding extends PagerAdapter {

    private final Context context;
    private final List<Onboarding> mlistOnboarding;

    public AdapterOnboarding(Context context, List<Onboarding> mlistOnboarding) {
        this.context = context;
        this.mlistOnboarding = mlistOnboarding;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_onboarding, container, false);

        Onboarding itemOnboarding = mlistOnboarding.get(position);

        TextView mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mTitleTextView.setText(itemOnboarding.getTitle());

        TextView mDescriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        mDescriptionTextView.setText(itemOnboarding.getDescription());

        ImageView mPosterImageView = (ImageView) itemView.findViewById(R.id.posterImageView);

        ImageView mSecondPosterImageView = (ImageView) itemView.findViewById(R.id.secondPosterImageView);

        if (position == 0) {
            mPosterImageView.setImageResource(itemOnboarding.getImageid());
            mPosterImageView.setVisibility(View.VISIBLE);
            mSecondPosterImageView.setVisibility(View.GONE);
        }else {
            mPosterImageView.setVisibility(View.GONE);
            mSecondPosterImageView.setVisibility(View.VISIBLE);
            mSecondPosterImageView.setImageResource(itemOnboarding.getImageid());
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((FrameLayout) object);
    }

    @Override
    public int getCount() {
        return mlistOnboarding.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}