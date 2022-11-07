package com.digital.moviesbuddypro.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digital.moviesbuddypro.CONFIG;
import com.digital.moviesbuddypro.FootView;
import com.digital.moviesbuddypro.utils.CallBack;
import com.digital.moviesbuddypro.utils.RequestAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.lovejjfg.powerrecycle.PowerAdapter;
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.digital.moviesbuddypro.CircleHeaderView;
import com.digital.moviesbuddypro.InternetConnection;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.model.Cast;
import com.movies.powerrefresh.OnRefreshListener;
import com.movies.powerrefresh.PowerRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityAllCasts extends AppCompatActivity implements View.OnClickListener {

    private Bundle bundle;

    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    private PowerRefreshLayout mRefreshLayout;

    private LinearLayout mLoadingLayoutProgressBar, mNotFoundLayout;

    private RecyclerView mRecyclerViewCast;
    private MyAdapter mAdapterCast;

    private int pageCast = 1;
    private boolean isFinished = true;

    private TextView mRetryButton;
    private ImageView mCategoryBanner;

    private String mCategory;
    private int mCategoryBannerInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_casts);

        findViews();
        setUpViews();
    }

    private void findViews() {
        mAppBarLayout = findViewById(R.id.appbar);
        mToolbar = findViewById(R.id.toolbar);

        mRefreshLayout = (PowerRefreshLayout) findViewById(R.id.refresh_layout);

        bundle = getIntent().getExtras();

        mLoadingLayoutProgressBar = findViewById(R.id.loadingLayoutProgressBar);
        mNotFoundLayout = findViewById(R.id.notFoundLayout);

        mRecyclerViewCast = findViewById(R.id.castRecyleview);
        mCategoryBanner = findViewById(R.id.categoryBanner);

        mRetryButton = (TextView) findViewById(R.id.retryButton);
    }

    private void setUpViews() {

        mRefreshLayout.setVisibility(View.GONE);
        mNotFoundLayout.setVisibility(View.GONE);
        mLoadingLayoutProgressBar.setVisibility(View.VISIBLE);

        if (bundle != null){
            mCategory = bundle.getString("CATEGORY_KEY");
            mCategoryBannerInt = bundle.getInt("CATEGORY_BANNER_KEY");
        }

        mCategoryBanner.setImageResource(mCategoryBannerInt);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle(mCategory);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        mRetryButton.setOnClickListener(this);

        loadCast();
    }

    private void loadCast(){
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.stopRefresh(true, 300);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Cast> mlist = new ArrayList<>();

                        if (InternetConnection.isNetworkConnected(ActivityAllCasts.this)) {
                            JSONObject parameters = new JSONObject();

                            try {
                                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                                Log.e("Parameter:", parameters.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            RequestAPI.Call_Api(ActivityAllCasts.this, CONFIG.GET_CASTS + "&page="+pageCast, parameters, new CallBack() {
                                @Override
                                public void Response(String resp) {
                                    Log.e("stringData: ", resp);
                                    try {
                                        JSONObject jsonObject = new JSONObject(resp);
                                        String code = jsonObject.optString("code");
                                        mRefreshLayout.stopLoadMore(true);
                                        if (code.equals("200")) {
                                            JSONArray jsonArray = jsonObject.getJSONArray("msg");

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject userdata = jsonArray.getJSONObject(i);

                                                Cast cast = new Cast();
                                                cast.setName(userdata.optString("name"));
                                                cast.setImage(userdata.optString("image"));

                                                mlist.add(cast);
                                            }

                                            mAdapterCast.appendList(mlist);

                                            isFinished = jsonArray.length() != 0;

                                            pageCast++;

                                        }else if (code.equals("400")) {
                                            isFinished = true;
                                        }
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(ActivityAllCasts.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                        }
                        mRefreshLayout.setLoadEnable(isFinished);
                    }
                }, 1000);
            }
        });

        CircleHeaderView header = new CircleHeaderView(ActivityAllCasts.this);
        FootView footView = new FootView(ActivityAllCasts.this);
        mRefreshLayout.setRefreshEnable(false);
        mRefreshLayout.addHeader(header);
        mRefreshLayout.addFooter(footView);
        mRefreshLayout.setAutoLoadMore(true);
        mRefreshLayout.setAutoRefresh(false);
        mRecyclerViewCast.setLayoutManager(new GridLayoutManager(ActivityAllCasts.this, 3));

        mAdapterCast = new MyAdapter();
        mRecyclerViewCast.setAdapter(mAdapterCast);
        List<Cast> mlist = new ArrayList<>();

        if (InternetConnection.isNetworkConnected(ActivityAllCasts.this)) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(ActivityAllCasts.this, CONFIG.GET_CASTS + "&page="+pageCast, parameters, new CallBack() {
                @Override
                public void Response(String resp) {
                    Log.e("stringData: ", resp);
                    try {
                        JSONObject jsonObject = new JSONObject(resp);
                        String code = jsonObject.optString("code");

                        if (code.equals("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("msg");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject userdata = jsonArray.getJSONObject(i);

                                Cast cast = new Cast();
                                cast.setName(userdata.optString("name"));
                                cast.setImage(userdata.optString("image"));

                                mlist.add(cast);
                            }

                            mAdapterCast.setList(mlist);

                            if (mlist.size() > 0){
                                mRefreshLayout.setVisibility(View.VISIBLE);

                                mNotFoundLayout.setVisibility(View.GONE);
                                mLoadingLayoutProgressBar.setVisibility(View.GONE);
                            }else {
                                mRefreshLayout.setVisibility(View.GONE);

                                mNotFoundLayout.setVisibility(View.VISIBLE);
                                mLoadingLayoutProgressBar.setVisibility(View.GONE);
                            }

                            pageCast++;

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(ActivityAllCasts.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int mId = v.getId();
        switch (mId){
            case R.id.retryButton:
                loadCast();
                break;
        }
    }

    class MyHolder extends PowerHolder<Cast> {

        private final TextView castName;
        private final CircularImageView castImage;
        private final CardView cardView;

        public MyHolder(View itemView) {
            super(itemView);
            castName = (TextView) findViewById(R.id.castName);
            castImage = (CircularImageView) findViewById(R.id.castImage);
            cardView = (CardView) findViewById(R.id.cardView);
        }

        @Override
        public void onBind(Cast cast) {

            Glide.with(ActivityAllCasts.this)
                    .load(cast.getImage())
                    /*.placeholder(R.drawable.ic_play)
                    .error(R.drawable.ic_share)
                    .priority( Priority.HIGH )*/
                    .into(castImage);

            castName.setText(cast.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(ActivityAllCasts.this, ActivityExploredCinema.class);
                    intent.putExtra("SEARCHABLE_STRING_KEY", cast.getName());
                    intent.putExtra("TYPE_KEY", "Cast");
                    startActivity(intent);
                }
            });

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.performClick();
                }
            });
        }

        private View findViewById(final int id) {
            return itemView.findViewById(id);
        }
    }

    class MyAdapter extends PowerAdapter<Cast> {
        @Override
        public PowerHolder<Cast> onViewHolderCreate(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false);
            return new MyHolder(itemView);
        }

        @Override
        public void onViewHolderBind(PowerHolder<Cast> holder, int position) {
            Cast cast = list.get(position);
            holder.onBind(cast);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}