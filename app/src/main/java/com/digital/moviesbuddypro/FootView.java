/*
 * Copyright (c) 2017.  Joe
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.digital.moviesbuddypro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.digital.moviesbuddypro.R;
import com.movies.powerrefresh.FooterListener;


/**
 * Created by Joe on 2017/4/16.
 * Email lovejjfg@gmail.com
 */

public class FootView extends FrameLayout implements FooterListener {

//    private final ImageView mHeaderImageView;
//    private final AnimationDrawable mFrameAnimation;
//    private final int mNumberOfFrames;

    /*private final TextView mFoot;
    private final ProgressBar mProgressBar;*/

    public FootView(Context context) {
        super(context);
        float density = context.getResources().getDisplayMetrics().density;
        /*mFoot = new TextView(context);
        mProgressBar = new ProgressBar(context);
        //DrawableCompat.setTint(mProgressBar.getProgressDrawable(), getResources().getColor(R.color.secondary_icon_color));
        //mProgressBar.getIndeterminateDrawable().setColorFilter(0xD74315, PorterDuff.Mode.MULTIPLY);
        mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        mProgressBar.setScaleX(0.7f);
        mProgressBar.setScaleY(0.7f);
        mProgressBar.setPadding(20, 20, 20, 20);
        mFoot.setBackgroundColor(Color.TRANSPARENT);
        mFoot.setText("Loading...");
        mFoot.setGravity(Gravity.CENTER);*/
        View itemView = LayoutInflater.from(context).inflate(R.layout.loading_layout, null, false);
        addView(itemView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (density * 60)));

//        LayoutInflater.from(context).inflate(R.layout.layout_header_loading, this, true);
//        mHeaderImageView = (ImageView) findViewById(R.id.pull_to_refresh_image);
//
//        mFrameAnimation = (AnimationDrawable) mHeaderImageView.getBackground();
//        mNumberOfFrames = mFrameAnimation.getNumberOfFrames();
    }

    @Override
    public void onLoadBefore(int scrollY) {

    }

    @Override
    public void onLoadAfter(int scrollY) {

    }

    @Override
    public void onLoadReady(int scrollY) {

    }

    @Override
    public void onLoading(int scrollY) {
    }

    @Override
    public void onLoadComplete(int scrollY, boolean isLoadSuccess) {

    }

    @Override
    public void onLoadCancel(int scrollY) {

    }
}
