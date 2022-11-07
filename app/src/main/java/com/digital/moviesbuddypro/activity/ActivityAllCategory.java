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
import com.digital.moviesbuddypro.model.Movie;
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

public class ActivityAllCategory extends AppCompatActivity implements View.OnClickListener {

    private Bundle bundle;

    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    private PowerRefreshLayout mRefreshLayout;

    private LinearLayout mLoadingLayoutProgressBar, mNotFoundLayout;

    private RecyclerView mRecyclerViewMovie;
    private MyAdapter mAdapterMovie;

    private boolean isFinished = true;

    private TextView mRetryButton;
    private ImageView mCategoryBanner;

    private String mCategory;
    private int mCategoryBannerInt;

    private int pageBollywood = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

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

        mRecyclerViewMovie = findViewById(R.id.movieRecyleview);
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

        loadMovies(mCategory);
    }

    private void loadMovies(String mCategory){
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
                        List<Movie> mlist = new ArrayList<>();

                        if (InternetConnection.isNetworkConnected(ActivityAllCategory.this)) {
                            JSONObject parameters = new JSONObject();

                            try {
                                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                                Log.e("Parameter:", parameters.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            RequestAPI.Call_Api(ActivityAllCategory.this, CONFIG.GET_MOVIES + "&page="+pageBollywood + "&category="+mCategory, parameters, new CallBack() {
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

                                                Movie movie = new Movie();
                                                movie.setId(Long.toString(userdata.optLong("id")));
                                                movie.setTitle(userdata.optString("title"));
                                                movie.setPoster(userdata.optString("poster"));

                                                if (userdata.optString("imdb_rating").equals("null") || userdata.optString("imdb_rating").equals("")) {
                                                    movie.setIMDBRating("∞");
                                                } else {
                                                    movie.setIMDBRating(userdata.optString("imdb_rating"));
                                                }

                                                if (userdata.optString("certificate").equals("Not Rated")) {
                                                    movie.setCertificate("N/A");
                                                } else {
                                                    movie.setCertificate(userdata.optString("certificate"));
                                                }

                                                mlist.add(movie);
                                            }

                                            pageBollywood++;

                                            mAdapterMovie.appendList(mlist);

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
                            Toast.makeText(ActivityAllCategory.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                        }


                        mRefreshLayout.setLoadEnable(isFinished);
                    }
                }, 1000);
            }
        });

        CircleHeaderView header = new CircleHeaderView(ActivityAllCategory.this);
        FootView footView = new FootView(ActivityAllCategory.this);
        mRefreshLayout.setRefreshEnable(false);
        mRefreshLayout.addHeader(header);
        mRefreshLayout.addFooter(footView);
        mRefreshLayout.setAutoLoadMore(true);
        mRefreshLayout.setAutoRefresh(false);
        mRecyclerViewMovie.setLayoutManager(new GridLayoutManager(ActivityAllCategory.this, 3));

        mAdapterMovie = new MyAdapter();
        mRecyclerViewMovie.setAdapter(mAdapterMovie);

        List<Movie> mlist = new ArrayList<>();

        if (InternetConnection.isNetworkConnected(ActivityAllCategory.this)) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(ActivityAllCategory.this, CONFIG.GET_MOVIES + "&page="+pageBollywood + "&category="+mCategory, parameters, new CallBack() {
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

                                Movie movie = new Movie();
                                movie.setId(Long.toString(userdata.optLong("id")));
                                movie.setTitle(userdata.optString("title"));
                                movie.setPoster(userdata.optString("poster"));

                                if (userdata.optString("imdb_rating").equals("null") || userdata.optString("imdb_rating").equals("")) {
                                    movie.setIMDBRating("∞");
                                } else {
                                    movie.setIMDBRating(userdata.optString("imdb_rating"));
                                }

                                if (userdata.optString("certificate").equals("Not Rated")) {
                                    movie.setCertificate("N/A");
                                } else {
                                    movie.setCertificate(userdata.optString("certificate"));
                                }

                                mlist.add(movie);
                            }

                            pageBollywood++;

                            mAdapterMovie.setList(mlist);

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
            Toast.makeText(ActivityAllCategory.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int mId = v.getId();
        switch (mId){
            case R.id.retryButton:
                loadMovies(mCategory);
                break;
        }
    }

    class MyHolder extends PowerHolder<Movie> {

        private final TextView movieRating;
        private final TextView movieTitle;
        private final ImageView moviePoster;
        private final CardView cardView;

        public MyHolder(View itemView) {
            super(itemView);
            movieRating = (TextView) findViewById(R.id.movieRating);
            movieTitle = (TextView) findViewById(R.id.movieTitle);
            moviePoster = (ImageView) findViewById(R.id.moviePoster);
            cardView = (CardView) findViewById(R.id.cardView);
        }

        @Override
        public void onBind(Movie movie) {

            Glide.with(ActivityAllCategory.this)
                    .load(movie.getPoster())
                    /*.placeholder(R.drawable.ic_play)
                    .error(R.drawable.ic_share)
                    .priority( Priority.HIGH )*/
                    .into(moviePoster);

            movieTitle.setText(movie.getTitle());
            movieRating.setText(movie.getIMDBRating());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQLiteDB.addMovieToRecent(ActivityAllCategory.this, movie.getId(), movie.getTitle(),
                            movie.getPoster(), mCategory, movie.getIMDBRating(), movie.getCertificate());

                    String mId = movie.getId();
                    Intent intent = new Intent(ActivityAllCategory.this, ActivityMovieDetails.class);
                    intent.putExtra("MOVIE_ID_KEY", mId);
                    intent.putExtra("MOVIE_CATEGORY_KEY", mCategory);
                    intent.putExtra("MOVIE_TITLE_KEY", movie.getTitle());
                    intent.putExtra("MOVIE_CERTIFICATE_KEY", movie.getCertificate());
                    intent.putExtra("MOVIE_POSTER_KEY", movie.getPoster());
                    intent.putExtra("MOVIE_IMDB_RATING_KEY", movie.getIMDBRating());
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

    class MyAdapter extends PowerAdapter<Movie> {
        @Override
        public PowerHolder<Movie> onViewHolderCreate(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
            return new MyHolder(itemView);
        }

        @Override
        public void onViewHolderBind(PowerHolder<Movie> holder, int position) {
            Movie movie = list.get(position);
            holder.onBind(movie);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
