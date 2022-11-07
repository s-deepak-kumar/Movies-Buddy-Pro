package com.digital.moviesbuddypro.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digital.moviesbuddypro.CONFIG;
import com.digital.moviesbuddypro.InternetConnection;
import com.digital.moviesbuddypro.adapter.AdapterEpisode;
import com.digital.moviesbuddypro.adapter.AdapterWebseries;
import com.digital.moviesbuddypro.adapter.ClickableAdapter;
import com.digital.moviesbuddypro.model.Episode;
import com.digital.moviesbuddypro.model.Movie;
import com.digital.moviesbuddypro.model.WebSeries;
import com.digital.moviesbuddypro.sqlitedatabase.SQLiteDB;
import com.digital.moviesbuddypro.utils.CallBack;
import com.digital.moviesbuddypro.utils.RequestAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.movies.flowlayoutmanager.Alignment;
import com.movies.flowlayoutmanager.FlowLayoutManager;
import com.digital.moviesbuddypro.HelperClass;
import com.digital.moviesbuddypro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityWebseriesDetails extends AppCompatActivity {

    private LinearLayout mContainer;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    private RecyclerView mRecyclerViewActor, mRecyclerViewGenre, mRecyclerViewCreator, mRecyclerViewOriginalNetwork;

    private ClickableAdapter mClickableAdapterActor, mClickableAdapterGenre, mClickableAdapterCreator, mClickableAdapterOriginalNetwork;
    private FlowLayoutManager flowLayoutManagerActor, flowLayoutManagerGenre, flowLayoutManagerCreator, flowLayoutManagerOriginalNetwork;

    private RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(10, 10, 10, 10);
        }
    };

    private TextView mWebseriesTitle, mWebseriesCertificate, mWebseriesReleaseDate, mWebseriesDuration, mWebseriesRating,
            mWebseriesDescription, mWebseriesLanguage, mWebseriesTotalEpisode, mTotalSeason;

    private ImageView mWebseriesPoster, mWebseriesBackPoster;
    private ImageButton mWishlistButton, mShareButton;
    private LinearLayout mPlayLayout;

    private List<WebSeries> mListMoreWebseries;
    private RecyclerView mRecyclerViewMoreWebseries;
    private AdapterWebseries mAdapterWebseriesMore;

    private Bundle bundle;

    private String mId, mTitleString, mPosterString, mIMDbRatingString,
            mCertificateString, mSeasonTag;
    private static boolean fromNotification = false;

    private WebSeries webSeries;

    private boolean mIsAddedMoreWebseries = false;
    private boolean mIsLoadingMoreWebseries = false;

    private LinearLayoutManager mLinearLayoutManagerMoreWebseries;

    private LinearLayout mTotalSeasonLayout;
    private LinearLayout mLayoutMoreWebseries, mLoadingLayoutMoreWebseries, mLayoutLoadingProgressBar;
    private NestedScrollView mNestedScrollView;

    private int pageMoreWebseries = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webseries_details);

        findViews();
        setUpViews();
    }

    private void findViews(){
        CONFIG.sharedPreferences = getSharedPreferences(CONFIG.pref_name, MODE_PRIVATE);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsinToolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        flowLayoutManagerActor = new FlowLayoutManager().setAlignment(Alignment.LEFT);
        flowLayoutManagerActor.setAutoMeasureEnabled(true);

        flowLayoutManagerGenre = new FlowLayoutManager().setAlignment(Alignment.LEFT);
        flowLayoutManagerGenre.setAutoMeasureEnabled(true);

        flowLayoutManagerCreator = new FlowLayoutManager().setAlignment(Alignment.LEFT);
        flowLayoutManagerCreator.setAutoMeasureEnabled(true);

        flowLayoutManagerOriginalNetwork = new FlowLayoutManager().setAlignment(Alignment.LEFT);
        flowLayoutManagerOriginalNetwork.setAutoMeasureEnabled(true);

        mWebseriesTitle = (TextView) findViewById(R.id.webseriesTitle);
        mWebseriesCertificate = (TextView) findViewById(R.id.webseriesCertificate);
        mWebseriesReleaseDate = (TextView) findViewById(R.id.webseriesReleaseDate);
        mWebseriesDuration = (TextView) findViewById(R.id.webseriesDuration);
        mWebseriesRating = (TextView) findViewById(R.id.webseriesRating);
        mWebseriesDescription = (TextView) findViewById(R.id.webseriesDescription);
        mWebseriesLanguage = (TextView) findViewById(R.id.webseriesLanguage);
        mWebseriesTotalEpisode = (TextView) findViewById(R.id.webseriesTotalEpisode);
        mTotalSeason = (TextView) findViewById(R.id.totalSeason);

        mWishlistButton = (ImageButton) findViewById(R.id.wishlistButton);
        mShareButton = (ImageButton) findViewById(R.id.shareButton);

        mWebseriesPoster = (ImageView) findViewById(R.id.webseriesPoster);
        mWebseriesBackPoster = (ImageView) findViewById(R.id.webseriesBackPoster);

        mRecyclerViewActor = (RecyclerView) findViewById(R.id.actorRecyclerView);
        mRecyclerViewGenre = (RecyclerView) findViewById(R.id.genreRecyclerView);
        mRecyclerViewCreator = (RecyclerView) findViewById(R.id.creatorRecyclerView);
        mRecyclerViewOriginalNetwork = (RecyclerView) findViewById(R.id.originalNetworkRecyclerView);
        mRecyclerViewMoreWebseries = (RecyclerView) findViewById(R.id.moreWebseriesRecyclerview);

        mPlayLayout = (LinearLayout) findViewById(R.id.playLayout);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        mLayoutLoadingProgressBar = (LinearLayout) findViewById(R.id.loadingLayoutProgressBar);

        mContainer = (LinearLayout) findViewById(R.id.container);

        mLayoutMoreWebseries = (LinearLayout) findViewById(R.id.moreWebseriesLayout);
        mLoadingLayoutMoreWebseries = (LinearLayout) findViewById(R.id.loadingLayoutMoreView);
        mLinearLayoutManagerMoreWebseries = new LinearLayoutManager(ActivityWebseriesDetails.this, LinearLayoutManager.HORIZONTAL, false);

        mListMoreWebseries = new ArrayList<>();

        bundle = getIntent().getExtras();
    }

    private void setUpViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        mLayoutLoadingProgressBar.setVisibility(View.VISIBLE);
        mNestedScrollView.setVisibility(View.GONE);

        if (bundle != null) {
            mId = bundle.getString("WEBSERIES_ID_KEY");
            mTitleString = bundle.getString("WEBSERIES_TITLE_KEY");
            mPosterString = bundle.getString("WEBSERIES_POSTER_KEY");
            mIMDbRatingString = bundle.getString("WEBSERIES_IMDB_RATING_KEY");
            mCertificateString = bundle.getString("WEBSERIES_CERTIFICATE_KEY");
            mSeasonTag = bundle.getString("WEBSERIES_SEASON_TAG_KEY");
            fromNotification = bundle.getBoolean("IS_FROM_NOTIFICATION_KEY");

            mWebseriesTitle.setText(capitalize(mTitleString));
            mWebseriesCertificate.setText(mCertificateString);

            Glide.with(ActivityWebseriesDetails.this)
                    .load(mPosterString)
                    /*.placeholder(R.drawable.ic_play)
                    .error(R.drawable.ic_share)
                    .priority( Priority.HIGH )*/
                    .into(mWebseriesPoster);

            Glide.with(ActivityWebseriesDetails.this)
                    .load(mPosterString)
                    /*.placeholder(R.drawable.ic_play)
                    .error(R.drawable.ic_share)
                    .priority( Priority.HIGH )*/
                    .into(mWebseriesBackPoster);

            mAdapterWebseriesMore = new AdapterWebseries(ActivityWebseriesDetails.this, mListMoreWebseries, R.layout.item_webseries_home);

            if (InternetConnection.isNetworkConnected(ActivityWebseriesDetails.this)) {

                JSONObject parameters = new JSONObject();

                try {
                    parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));
                    parameters.put("id", mId);

                    Log.e("Parameter:", parameters.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestAPI.Call_Api(ActivityWebseriesDetails.this, CONFIG.GET_WEBSERIES_BY_ID, parameters, new CallBack() {
                    @Override
                    public void Response(String resp) {
                        Log.e("stringData: ", resp);
                        try {
                            JSONObject jsonObject = new JSONObject(resp);
                            String code = jsonObject.optString("code");

                            if (code.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                JSONObject userdata = jsonArray.getJSONObject(0);

                                webSeries = new WebSeries();
                                webSeries.setActor((ArrayList<String>) convertArrayToList(userdata.optString("actors").split(",")));
                                webSeries.setGenre((ArrayList<String>) convertArrayToList(userdata.optString("genres").split(",")));
                                webSeries.setCreator((ArrayList<String>) convertArrayToList(userdata.optString("creators").split(",")));
                                webSeries.setOriginalNetwork((ArrayList<String>) convertArrayToList(userdata.optString("original_network").split(",")));
                                webSeries.setDuration(userdata.optString("duration"));
                                webSeries.setDescription(userdata.optString("description"));
                                webSeries.setTotalSeason(userdata.optString("total_season"));

                                mClickableAdapterActor = new ClickableAdapter(ActivityWebseriesDetails.this, webSeries.getActor());
                                mClickableAdapterGenre = new ClickableAdapter(ActivityWebseriesDetails.this, webSeries.getGenre());
                                mClickableAdapterCreator = new ClickableAdapter(ActivityWebseriesDetails.this, webSeries.getCreator());
                                mClickableAdapterOriginalNetwork = new ClickableAdapter(ActivityWebseriesDetails.this, webSeries.getOriginalNetwork());

                                mRecyclerViewActor.setHasFixedSize(true);
                                mRecyclerViewActor.setItemAnimator(new DefaultItemAnimator());
                                mRecyclerViewActor.setLayoutManager(flowLayoutManagerActor);
                                mRecyclerViewActor.setAdapter(mClickableAdapterActor);
                                mRecyclerViewActor.addItemDecoration(itemDecoration);
                                mRecyclerViewActor.setNestedScrollingEnabled(false);

                                mRecyclerViewGenre.setHasFixedSize(true);
                                mRecyclerViewGenre.setItemAnimator(new DefaultItemAnimator());
                                mRecyclerViewGenre.setLayoutManager(flowLayoutManagerGenre);
                                mRecyclerViewGenre.setAdapter(mClickableAdapterGenre);
                                mRecyclerViewGenre.addItemDecoration(itemDecoration);
                                mRecyclerViewGenre.setNestedScrollingEnabled(false);

                                mRecyclerViewCreator.setHasFixedSize(true);
                                mRecyclerViewCreator.setItemAnimator(new DefaultItemAnimator());
                                mRecyclerViewCreator.setLayoutManager(flowLayoutManagerCreator);
                                mRecyclerViewCreator.setAdapter(mClickableAdapterCreator);
                                mRecyclerViewCreator.addItemDecoration(itemDecoration);
                                mRecyclerViewCreator.setNestedScrollingEnabled(false);

                                mRecyclerViewOriginalNetwork.setHasFixedSize(true);
                                mRecyclerViewOriginalNetwork.setItemAnimator(new DefaultItemAnimator());
                                mRecyclerViewOriginalNetwork.setLayoutManager(flowLayoutManagerOriginalNetwork);
                                mRecyclerViewOriginalNetwork.setAdapter(mClickableAdapterOriginalNetwork);
                                mRecyclerViewOriginalNetwork.addItemDecoration(itemDecoration);
                                mRecyclerViewOriginalNetwork.setNestedScrollingEnabled(false);

                                mWebseriesReleaseDate.setText(userdata.optString("release_date"));
                                mWebseriesDuration.setText(webSeries.getDuration());
                                mWebseriesRating.setText(mIMDbRatingString);
                                mWebseriesDescription.setText(webSeries.getDescription());
                                mWebseriesLanguage.setText(userdata.optString("languages").replace(",", " • "));

                                mWebseriesTotalEpisode.setText(userdata.optString("total_episode").replace(",", " • "));
                                mTotalSeason.setText(userdata.optString("total_season"));

                                if (InternetConnection.isNetworkConnected(ActivityWebseriesDetails.this)) {
                                    JSONObject parameters = new JSONObject();

                                    try {
                                        parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                                        Log.e("Parameter:", parameters.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    RequestAPI.Call_Api(ActivityWebseriesDetails.this, CONFIG.GET_WEBSERIES + "&page="+pageMoreWebseries, parameters, new CallBack() {
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

                                                        if (!webSeries.getId().equals(mId)) {
                                                            mListMoreWebseries.add(webSeries);
                                                        }
                                                    }

                                                    if (mListMoreWebseries.size() > 0){
                                                        mLayoutMoreWebseries.setVisibility(View.VISIBLE);
                                                    }else {
                                                        mLayoutMoreWebseries.setVisibility(View.GONE);
                                                    }

                                                    mRecyclerViewMoreWebseries.setHasFixedSize(true);
                                                    mRecyclerViewMoreWebseries.setItemAnimator(new DefaultItemAnimator());
                                                    mRecyclerViewMoreWebseries.setLayoutManager(mLinearLayoutManagerMoreWebseries);
                                                    mRecyclerViewMoreWebseries.setAdapter(mAdapterWebseriesMore);
                                                    mRecyclerViewMoreWebseries.setNestedScrollingEnabled(false);

                                                    pageMoreWebseries++;

                                                    RecyclerView.OnScrollListener onScrollListener =
                                                            new RecyclerView.OnScrollListener(){
                                                                @Override
                                                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                                                    super.onScrollStateChanged(recyclerView, newState);

                                                                }

                                                                @Override
                                                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                                    super.onScrolled(recyclerView, dx, dy);

                                                                    if (!mIsLoadingMoreWebseries) {
                                                                        if (mLinearLayoutManagerMoreWebseries != null && mLinearLayoutManagerMoreWebseries.findLastCompletelyVisibleItemPosition() == mListMoreWebseries.size() - 1) {
                                                                            //bottom of list!
                                                                            mIsLoadingMoreWebseries = true;

                                                                            if (!mIsAddedMoreWebseries) {
                                                                                mLoadingLayoutMoreWebseries.setVisibility(View.VISIBLE);
                                                                            }

                                                                            if (InternetConnection.isNetworkConnected(ActivityWebseriesDetails.this)) {
                                                                                RequestAPI.Call_Api(ActivityWebseriesDetails.this, CONFIG.GET_WEBSERIES + "&page="+pageMoreWebseries, parameters, new CallBack() {
                                                                                    @Override
                                                                                    public void Response(String resp) {
                                                                                        mLoadingLayoutMoreWebseries.setVisibility(View.GONE);

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

                                                                                                    if (!webSeries.getId().equals(mId)) {
                                                                                                        mListMoreWebseries.add(webSeries);
                                                                                                    }
                                                                                                }

                                                                                                pageMoreWebseries++;

                                                                                                mIsAddedMoreWebseries = false;
                                                                                                mAdapterWebseriesMore.notifyDataSetChanged();
                                                                                                mIsLoadingMoreWebseries = false;
                                                                                            }
                                                                                        }catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }else {
                                                                                Toast.makeText(ActivityWebseriesDetails.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            };
                                                    mRecyclerViewMoreWebseries.addOnScrollListener(onScrollListener);
                                                }
                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            mLayoutLoadingProgressBar.setVisibility(View.GONE);
                                            mNestedScrollView.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }else {
                                    Toast.makeText(ActivityWebseriesDetails.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }else {
                Toast.makeText(ActivityWebseriesDetails.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
            }

            mPlayLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityWebseriesDetails.this, ActivityYouTubePlayer.class);
                    intent.putExtra("YT_VIDEO_KEY", "aqz-KE-bpKQ");
                    startActivity(intent);
                }
            });

            mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //checkWriteExternalStoragePermission();
                    HelperClass.shareApp(ActivityWebseriesDetails.this, mWebseriesPoster, mTitleString, ActivityWebseriesDetails.this);
                }
            });

            if (SQLiteDB.isWebseriesFav(ActivityWebseriesDetails.this, mId)) {
                mWishlistButton.setImageResource(R.drawable.ic_favourite_check);
            } else {
                mWishlistButton.setImageResource(R.drawable.ic_favourite_uncheck);
            }

            mWishlistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SQLiteDB.isWebseriesFav(ActivityWebseriesDetails.this, mId)) {
                        SQLiteDB.removeWebseriesFromFav(ActivityWebseriesDetails.this, mId);
                        mWishlistButton.setImageResource(R.drawable.ic_favourite_uncheck);
                        Toast.makeText(ActivityWebseriesDetails.this, mTitleString + " Removed From Favourite.", Toast.LENGTH_SHORT).show();
                    } else {
                        SQLiteDB.addWebseriesToFav(ActivityWebseriesDetails.this, mId, mTitleString,
                                mPosterString,
                                mIMDbRatingString, mSeasonTag,
                                mCertificateString);
                        mWishlistButton.setImageResource(R.drawable.ic_favourite_check);
                        Toast.makeText(ActivityWebseriesDetails.this, mTitleString + " Added To Favourite.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbarLayout.setTitle(mTitleString);
                    isShow = true;
                } else if(isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    //carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Generic function to convert an Array to List
    public static <T> List<T> convertArrayToList(T array[])
    {

        // Create an empty List
        List<T> list = new ArrayList<>();

        // Iterate through the array
        for (T t : array) {
            // Add each element into the list
            list.add(t);
        }

        // Return the converted List
        return list;
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

    private String returnReleaseDateString(ArrayList<String> list){
        String finalString = "";
        if (list.size() == 2){
            for (int i = 0; i <= list.size() - 1; i++){
                if (i != list.size() - 1){
                    finalString = finalString + list.get(i) + ", ";
                }else {
                    finalString = finalString + list.get(i).trim();
                }
            }
        }else {
            finalString = list.get(1) + " - " + list.get(list.size()-1).trim();
        }
        return finalString;
    }

    private String returnReleaseYearString(ArrayList<String> list){
        return list.get(1);
    }

    private String returnString(ArrayList<String> list){
        String finalString = "";
        for (int i = 0; i <= list.size() - 1; i++){
            if (i != list.size() - 1){
                finalString = finalString + capitalize(list.get(i)) + "・";
            }else {
                finalString = finalString + capitalize(list.get(i));
            }
        }
        return finalString;
    }

    private String returnSubtitle(ArrayList<String> list){
        String finalString = "";
        if (list.size() == 1){
            if (list.get(0).equals("")){
                finalString = "No";
            }else {
                for (int i = 0; i <= list.size() - 1; i++){
                    if (i != list.size() - 1){
                        finalString = finalString + capitalize(list.get(i)) + "・";
                    }else {
                        finalString = finalString + capitalize(list.get(i));
                    }
                }
            }
        }else {
            for (int i = 0; i <= list.size() - 1; i++){
                if (i != list.size() - 1){
                    finalString = finalString + capitalize(list.get(i)) + "・";
                }else {
                    finalString = finalString + capitalize(list.get(i));
                }
            }
        }
        return finalString;
    }

    private String removeColon(String str){
        return str.replace(":", "%3A");
    }
}
