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
import com.digital.moviesbuddypro.CircleHeaderView;
import com.digital.moviesbuddypro.FootView;
import com.digital.moviesbuddypro.InternetConnection;
import com.digital.moviesbuddypro.model.WebSeries;
import com.digital.moviesbuddypro.sqlitedatabase.SQLiteDB;
import com.digital.moviesbuddypro.utils.CallBack;
import com.digital.moviesbuddypro.utils.RequestAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.lovejjfg.powerrecycle.PowerAdapter;
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import com.digital.moviesbuddypro.R;
import com.movies.powerrefresh.OnRefreshListener;
import com.movies.powerrefresh.PowerRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityCategoryWebseries extends AppCompatActivity implements View.OnClickListener{

    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    private LinearLayout mLoadingLayoutProgressBar, mNotFoundLayout;
    private ImageView mCategoryBanner;

    private RecyclerView mRecyclerViewCategoryWebseries;
    private MyAdapter mAdapterWebseries;

    private int pageWebseries = 1;
    private PowerRefreshLayout mRefreshLayout;

    private boolean isFinished = true;

    private TextView mRetryButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_webseries);

        findViews();
        setUpViews();
    }

    private void findViews() {
        mAppBarLayout = findViewById(R.id.appbar);
        mToolbar = findViewById(R.id.toolbar);

        mRefreshLayout = (PowerRefreshLayout) findViewById(R.id.refresh_layout);

        mLoadingLayoutProgressBar = findViewById(R.id.loadingLayoutProgressBar);
        mNotFoundLayout = findViewById(R.id.notFoundLayout);

        mRecyclerViewCategoryWebseries = findViewById(R.id.categoryWebseriesRecyleview);
        mCategoryBanner = findViewById(R.id.categoryBanner);

        mRetryButton = (TextView) findViewById(R.id.retryButton);
    }

    private void setUpViews() {

        mRefreshLayout.setVisibility(View.GONE);
        mNotFoundLayout.setVisibility(View.GONE);
        mLoadingLayoutProgressBar.setVisibility(View.VISIBLE);

        mCategoryBanner.setImageResource(R.mipmap.webseries_poster_landscape);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle("Web Series");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        mRetryButton.setOnClickListener(this);

        loadWebseries();
    }

    private void loadWebseries(){
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
                        List<WebSeries> mlist = new ArrayList<>();

                        if (InternetConnection.isNetworkConnected(ActivityCategoryWebseries.this)) {
                            JSONObject parameters = new JSONObject();

                            try {
                                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                                Log.e("Parameter:", parameters.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            RequestAPI.Call_Api(ActivityCategoryWebseries.this, CONFIG.GET_WEBSERIES + "&page="+pageWebseries, parameters, new CallBack() {
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

                                                WebSeries webSeries = new WebSeries();
                                                webSeries.setId(Long.toString(userdata.optLong("id")));
                                                webSeries.setTitle(userdata.optString("title"));
                                                webSeries.setMainPoster(userdata.optString("main_thumbnail"));
                                                webSeries.setSeasonTag(userdata.optString("season_tag"));

                                                if (userdata.optString("imdb_rating").equals("null") || userdata.optString("imdb_rating").equals("")) {
                                                    webSeries.setIMDBRating("∞");
                                                } else {
                                                    webSeries.setIMDBRating(userdata.optString("imdb_rating"));
                                                }

                                                if (userdata.optString("certificate").equals("Not Rated")) {
                                                    webSeries.setCertificate("N/A");
                                                } else {
                                                    webSeries.setCertificate(userdata.optString("certificate"));
                                                }

                                                mlist.add(webSeries);
                                            }

                                            pageWebseries++;

                                            mAdapterWebseries.appendList(mlist);

                                            isFinished = jsonArray.length() != 0;
                                        }else if (code.equals("400")) {
                                            isFinished = true;
                                        }
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(ActivityCategoryWebseries.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                        }

                        mRefreshLayout.setLoadEnable(isFinished);
                    }
                }, 1000);
            }
        });

        CircleHeaderView header = new CircleHeaderView(ActivityCategoryWebseries.this);
        FootView footView = new FootView(ActivityCategoryWebseries.this);
        mRefreshLayout.setRefreshEnable(false);
        mRefreshLayout.addHeader(header);
        mRefreshLayout.addFooter(footView);
        mRefreshLayout.setAutoLoadMore(true);
        mRefreshLayout.setAutoRefresh(false);
        mRecyclerViewCategoryWebseries.setLayoutManager(new GridLayoutManager(ActivityCategoryWebseries.this, 3));

        mAdapterWebseries = new MyAdapter();
        mRecyclerViewCategoryWebseries.setAdapter(mAdapterWebseries);
        List<WebSeries> mlist = new ArrayList<>();

        if (InternetConnection.isNetworkConnected(ActivityCategoryWebseries.this)) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(ActivityCategoryWebseries.this, CONFIG.GET_WEBSERIES + "&page="+pageWebseries, parameters, new CallBack() {
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

                                WebSeries webSeries = new WebSeries();
                                webSeries.setId(Long.toString(userdata.optLong("id")));
                                webSeries.setTitle(userdata.optString("title"));
                                webSeries.setMainPoster(userdata.optString("main_thumbnail"));
                                webSeries.setSeasonTag(userdata.optString("season_tag"));

                                if (userdata.optString("imdb_rating").equals("null") || userdata.optString("imdb_rating").equals("")) {
                                    webSeries.setIMDBRating("∞");
                                } else {
                                    webSeries.setIMDBRating(userdata.optString("imdb_rating"));
                                }

                                if (userdata.optString("certificate").equals("Not Rated")) {
                                    webSeries.setCertificate("N/A");
                                } else {
                                    webSeries.setCertificate(userdata.optString("certificate"));
                                }

                                mlist.add(webSeries);
                            }

                            pageWebseries++;

                            mAdapterWebseries.setList(mlist);

                            if (mlist.size() > 0){
                                mRefreshLayout.setVisibility(View.VISIBLE);

                                mNotFoundLayout.setVisibility(View.GONE);
                                mLoadingLayoutProgressBar.setVisibility(View.GONE);
                            }else {
                                mRefreshLayout.setVisibility(View.GONE);

                                mNotFoundLayout.setVisibility(View.VISIBLE);
                                mLoadingLayoutProgressBar.setVisibility(View.GONE);
                            }
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(ActivityCategoryWebseries.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int mId = v.getId();
        switch (mId){
            case R.id.retryButton:
                loadWebseries();
                break;
        }
    }

    class MyHolder extends PowerHolder<WebSeries> {

        private final TextView webseriesRating;
        private final TextView webseriesTitle;
        private final ImageView webseriesPoster;
        private final TextView webseriesSeasonTag;
        private final CardView cardView;

        public MyHolder(View itemView) {
            super(itemView);
            webseriesRating = (TextView) findViewById(R.id.webseriesRating);
            webseriesTitle = (TextView) findViewById(R.id.webseriesTitle);
            webseriesPoster = (ImageView) findViewById(R.id.webseriesPoster);
            webseriesSeasonTag = (TextView) findViewById(R.id.webseriesSeasonTag);
            cardView = (CardView) findViewById(R.id.cardView);
        }

        @Override
        public void onBind(WebSeries webSeries) {

            Glide.with(ActivityCategoryWebseries.this)
                    .load(webSeries.getMainPoster())
                    /*.placeholder(R.drawable.ic_play)
                    .error(R.drawable.ic_share)
                    .priority( Priority.HIGH )*/
                    .into(webseriesPoster);

            webseriesTitle.setText(webSeries.getTitle());
            webseriesSeasonTag.setText(webSeries.getSeasonTag());
            webseriesRating.setText(webSeries.getIMDBRating());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQLiteDB.addWebseriesToRecent(ActivityCategoryWebseries.this, webSeries.getId(), webSeries.getTitle(),
                            webSeries.getMainPoster(), webSeries.getIMDBRating(), webSeries.getSeasonTag(),
                            webSeries.getCertificate());

                    String mId = webSeries.getId();
                    Intent intent = new Intent(ActivityCategoryWebseries.this, ActivityWebseriesDetails.class);
                    intent.putExtra("WEBSERIES_ID_KEY", mId);
                    intent.putExtra("WEBSERIES_SEASON_TAG_KEY", webSeries.getSeasonTag());
                    intent.putExtra("WEBSERIES_TITLE_KEY", webSeries.getTitle());
                    intent.putExtra("WEBSERIES_CERTIFICATE_KEY", webSeries.getCertificate());
                    intent.putExtra("WEBSERIES_POSTER_KEY", webSeries.getMainPoster());
                    intent.putExtra("WEBSERIES_IMDB_RATING_KEY", webSeries.getIMDBRating());
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

    class MyAdapter extends PowerAdapter<WebSeries> {
        @Override
        public PowerHolder<WebSeries> onViewHolderCreate(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_webseries, parent, false);
            return new MyHolder(itemView);
        }

        @Override
        public void onViewHolderBind(PowerHolder<WebSeries> holder, int position) {
            WebSeries webSeries = list.get(position);
            holder.onBind(webSeries);
        }
    }
}