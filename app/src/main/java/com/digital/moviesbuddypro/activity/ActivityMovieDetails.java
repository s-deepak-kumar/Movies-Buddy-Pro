package com.digital.moviesbuddypro.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
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

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.bumptech.glide.Glide;
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
import com.digital.moviesbuddypro.CONFIG;
import com.digital.moviesbuddypro.HelperClass;
import com.digital.moviesbuddypro.InternetConnection;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.adapter.AdapterMovie;
import com.digital.moviesbuddypro.adapter.ClickableAdapter;
import com.digital.moviesbuddypro.model.Movie;
import com.digital.moviesbuddypro.sqlitedatabase.SQLiteDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ActivityMovieDetails extends AppCompatActivity{

    private LinearLayout mContainer;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    private RecyclerView mRecyclerViewActor, mRecyclerViewGenre, mRecyclerViewDirector, mRecyclerViewWriter, mRecyclerViewOriginalNetwork;

    private ClickableAdapter mClickableAdapterActor, mClickableAdapterGenre, mClickableAdapterDirector, mClickableAdapterWriter, mClickableAdapterOriginalNetwork;
    private FlowLayoutManager flowLayoutManagerActor, flowLayoutManagerGenre, flowLayoutManagerDirector, flowLayoutManagerWriter, flowLayoutManagerOriginalNetwork;

    private RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(10, 10, 10, 10);
        }
    };

    private TextView mMovieTitle, mMovieCertificate, mMovieReleaseDate, mMovieDuration, mMovieRating,
            mMovieDescription, mMovieLanguage;

    private ImageView mMoviePoster, mMovieBackPoster;
    private ImageButton mWishlistButton, mShareButton;
    private LinearLayout mPlayLayout;

    private List<Movie> mListMoreMovie;
    private RecyclerView mRecyclerViewMoreMovie;
    private AdapterMovie mAdapterMovieMore;

    private Bundle bundle;

    private String mId, mTitleString, mPosterString, mIMDbRatingString, mCategoryString, mCertificateString;

    private Movie movie;

    private boolean mIsAddedMoreMovie = false;
    private boolean mIsLoadingMoreMovie = false;

    private LinearLayoutManager mLinearLayoutManagerMoreMovie;
    private LinearLayout mLayoutMoreMovie, mLoadingLayoutMoreMovie, mLayoutLoadingProgressBar;
    private NestedScrollView mNestedScrollView;

    private int pageMoreMovie = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

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

        flowLayoutManagerDirector = new FlowLayoutManager().setAlignment(Alignment.LEFT);
        flowLayoutManagerDirector.setAutoMeasureEnabled(true);

        flowLayoutManagerWriter = new FlowLayoutManager().setAlignment(Alignment.LEFT);
        flowLayoutManagerWriter.setAutoMeasureEnabled(true);

        flowLayoutManagerOriginalNetwork = new FlowLayoutManager().setAlignment(Alignment.LEFT);
        flowLayoutManagerOriginalNetwork.setAutoMeasureEnabled(true);

        mMovieTitle = (TextView) findViewById(R.id.movieTitle);
        mMovieCertificate = (TextView) findViewById(R.id.movieCertificate);
        mMovieReleaseDate = (TextView) findViewById(R.id.movieReleaseDate);
        mMovieDuration = (TextView) findViewById(R.id.movieDuration);
        mMovieRating = (TextView) findViewById(R.id.movieRating);
        mMovieDescription = (TextView) findViewById(R.id.movieDescription);
        mMovieLanguage = (TextView) findViewById(R.id.movieLanguage);

        mWishlistButton = (ImageButton) findViewById(R.id.wishlistButton);
        mShareButton = (ImageButton) findViewById(R.id.shareButton);

        mMoviePoster = (ImageView) findViewById(R.id.moviePoster);
        mMovieBackPoster = (ImageView) findViewById(R.id.movieBackPoster);

        mRecyclerViewActor = (RecyclerView) findViewById(R.id.actorRecyclerView);
        mRecyclerViewGenre = (RecyclerView) findViewById(R.id.genreRecyclerView);
        mRecyclerViewDirector = (RecyclerView) findViewById(R.id.directorRecyclerView);
        mRecyclerViewWriter = (RecyclerView) findViewById(R.id.writerRecyclerView);
        mRecyclerViewOriginalNetwork = (RecyclerView) findViewById(R.id.originalNetworkRecyclerView);
        mRecyclerViewMoreMovie = (RecyclerView) findViewById(R.id.moreMovieRecyclerview);

        mPlayLayout = (LinearLayout) findViewById(R.id.playLayout);

        mNestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        mLayoutLoadingProgressBar = (LinearLayout) findViewById(R.id.loadingLayoutProgressBar);

        mContainer = (LinearLayout) findViewById(R.id.container);

        mLayoutMoreMovie = (LinearLayout) findViewById(R.id.moreMovieLayout);
        mLoadingLayoutMoreMovie = (LinearLayout) findViewById(R.id.loadingLayoutMoreView);
        mLinearLayoutManagerMoreMovie = new LinearLayoutManager(ActivityMovieDetails.this, LinearLayoutManager.HORIZONTAL, false);

        mListMoreMovie = new ArrayList<>();

        bundle = getIntent().getExtras();
    }

    private void setUpViews() {
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        mLayoutLoadingProgressBar.setVisibility(View.VISIBLE);
        mNestedScrollView.setVisibility(View.GONE);

        if (bundle != null) {
            mId = bundle.getString("MOVIE_ID_KEY");
            mTitleString = bundle.getString("MOVIE_TITLE_KEY");
            mPosterString = bundle.getString("MOVIE_POSTER_KEY");
            mIMDbRatingString = bundle.getString("MOVIE_IMDB_RATING_KEY");
            mCategoryString = bundle.getString("MOVIE_CATEGORY_KEY");
            mCertificateString = bundle.getString("MOVIE_CERTIFICATE_KEY");
            boolean fromNotification = bundle.getBoolean("IS_FROM_NOTIFICATION_KEY");

            mMovieTitle.setText(mTitleString);
            mMovieCertificate.setText(mCertificateString);

            Glide.with(ActivityMovieDetails.this)
                    .load(mPosterString)
                    /*.placeholder(R.drawable.ic_play)
                    .error(R.drawable.ic_share)
                    .priority( Priority.HIGH )*/
                    .into(mMoviePoster);

            Glide.with(ActivityMovieDetails.this)
                .load(mPosterString)
                /*.placeholder(R.drawable.ic_play)
                .error(R.drawable.ic_share)
                .priority( Priority.HIGH )*/
                .into(mMovieBackPoster);

            mAdapterMovieMore = new AdapterMovie(ActivityMovieDetails.this, mListMoreMovie, mCategoryString, R.layout.item_movie_home);

            if (InternetConnection.isNetworkConnected(ActivityMovieDetails.this)) {

                JSONObject parameters = new JSONObject();

                try {
                    parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));
                    parameters.put("id", mId);

                    Log.e("Parameter:", parameters.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestAPI.Call_Api(ActivityMovieDetails.this, CONFIG.GET_MOVIE_BY_ID, parameters, new CallBack() {
                    @Override
                    public void Response(String resp) {
                        Log.e("stringData: ", resp);
                        try {
                            JSONObject jsonObject = new JSONObject(resp);
                            String code = jsonObject.optString("code");

                            if (code.equals("200")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                JSONObject userdata = jsonArray.getJSONObject(0);

                                movie = new Movie();
                                //movie.setLanguage((ArrayList<String>) Arrays.asList(userdata.optString("languages").replace(",", " • ")));
                                //movie.setRelease((ArrayList<String>)  document.getData().get("release_date"));

                                movie.setActor((ArrayList<String>) convertArrayToList(userdata.optString("actors").split(",")));
                                movie.setGenre((ArrayList<String>) convertArrayToList(userdata.optString("genres").split(",")));
                                movie.setDirector((ArrayList<String>) convertArrayToList(userdata.optString("directors").split(",")));
                                movie.setWriter((ArrayList<String>) convertArrayToList(userdata.optString("writers").split(",")));
                                movie.setDuration(userdata.optString("duration"));
                                movie.setDescription(userdata.optString("description"));

                                mClickableAdapterActor = new ClickableAdapter(ActivityMovieDetails.this, movie.getActor());
                                mClickableAdapterGenre = new ClickableAdapter(ActivityMovieDetails.this, movie.getGenre());
                                mClickableAdapterDirector = new ClickableAdapter(ActivityMovieDetails.this, movie.getDirector());
                                mClickableAdapterWriter = new ClickableAdapter(ActivityMovieDetails.this, movie.getWriter());

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

                                mRecyclerViewDirector.setHasFixedSize(true);
                                mRecyclerViewDirector.setItemAnimator(new DefaultItemAnimator());
                                mRecyclerViewDirector.setLayoutManager(flowLayoutManagerDirector);
                                mRecyclerViewDirector.setAdapter(mClickableAdapterDirector);
                                mRecyclerViewDirector.addItemDecoration(itemDecoration);
                                mRecyclerViewDirector.setNestedScrollingEnabled(false);

                                mRecyclerViewWriter.setHasFixedSize(true);
                                mRecyclerViewWriter.setItemAnimator(new DefaultItemAnimator());
                                mRecyclerViewWriter.setLayoutManager(flowLayoutManagerWriter);
                                mRecyclerViewWriter.setAdapter(mClickableAdapterWriter);
                                mRecyclerViewWriter.addItemDecoration(itemDecoration);
                                mRecyclerViewWriter.setNestedScrollingEnabled(false);

                                mMovieReleaseDate.setText(""+userdata.optString("release_date"));
                                mMovieDuration.setText(movie.getDuration());
                                mMovieRating.setText(mIMDbRatingString);
                                mMovieDescription.setText(movie.getDescription());
                                mMovieLanguage.setText(userdata.optString("languages").replace(",", " • "));

                                if (InternetConnection.isNetworkConnected(ActivityMovieDetails.this)) {
                                    JSONObject parameters = new JSONObject();

                                    try {
                                        parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                                        Log.e("Parameter:", parameters.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    RequestAPI.Call_Api(ActivityMovieDetails.this, CONFIG.GET_MOVIES + "&page="+pageMoreMovie + "&category="+mCategoryString, parameters, new CallBack() {
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

                                                        if (!movie.getId().equals(mId)) {
                                                            mListMoreMovie.add(movie);
                                                        }
                                                    }

                                                    if (mListMoreMovie.size() > 0){
                                                        mLayoutMoreMovie.setVisibility(View.VISIBLE);
                                                    }else {
                                                        mLayoutMoreMovie.setVisibility(View.GONE);
                                                    }

                                                    mRecyclerViewMoreMovie.setHasFixedSize(true);
                                                    mRecyclerViewMoreMovie.setItemAnimator(new DefaultItemAnimator());
                                                    mRecyclerViewMoreMovie.setLayoutManager(mLinearLayoutManagerMoreMovie);
                                                    mRecyclerViewMoreMovie.setAdapter(mAdapterMovieMore);
                                                    mRecyclerViewMoreMovie.setNestedScrollingEnabled(false);

                                                    pageMoreMovie++;

                                                    RecyclerView.OnScrollListener onScrollListener =
                                                            new RecyclerView.OnScrollListener(){
                                                                @Override
                                                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                                                    super.onScrollStateChanged(recyclerView, newState);

                                                                }

                                                                @Override
                                                                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                                    super.onScrolled(recyclerView, dx, dy);

                                                                    if (!mIsLoadingMoreMovie) {
                                                                        if (mLinearLayoutManagerMoreMovie != null && mLinearLayoutManagerMoreMovie.findLastCompletelyVisibleItemPosition() == mListMoreMovie.size() - 1) {
                                                                            //bottom of list!
                                                                            mIsLoadingMoreMovie = true;

                                                                            if (!mIsAddedMoreMovie) {
                                                                                mLoadingLayoutMoreMovie.setVisibility(View.VISIBLE);
                                                                            }

                                                                            if (InternetConnection.isNetworkConnected(ActivityMovieDetails.this)) {
                                                                                RequestAPI.Call_Api(ActivityMovieDetails.this, CONFIG.GET_MOVIES + "&page="+pageMoreMovie + "&category="+mCategoryString, parameters, new CallBack() {
                                                                                    @Override
                                                                                    public void Response(String resp) {
                                                                                        mLoadingLayoutMoreMovie.setVisibility(View.GONE);

                                                                                        Log.e("stringData: ", resp);
                                                                                        try {
                                                                                            JSONObject jsonObject = new JSONObject(resp);
                                                                                            String code = jsonObject.optString("code");

                                                                                            if (code.equals("200")) {
                                                                                                JSONArray jsonArray = jsonObject.getJSONArray("msg");
                                                                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                                                                    JSONObject userdata = jsonArray.getJSONObject(i);

                                                                                                    Movie movie = new Movie();
                                                                                                    movie.setId(Integer.toString(userdata.optInt("id")));
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

                                                                                                    if (!movie.getId().equals(mId)) {
                                                                                                        mListMoreMovie.add(movie);
                                                                                                    }
                                                                                                }

                                                                                                pageMoreMovie++;

                                                                                                mIsAddedMoreMovie = false;
                                                                                                mAdapterMovieMore.notifyDataSetChanged();
                                                                                                mIsLoadingMoreMovie = false;
                                                                                            }
                                                                                        }catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }else {
                                                                                Toast.makeText(ActivityMovieDetails.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            };
                                                    mRecyclerViewMoreMovie.addOnScrollListener(onScrollListener);
                                                }
                                            }catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            mLayoutLoadingProgressBar.setVisibility(View.GONE);
                                            mNestedScrollView.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }else {
                                    Toast.makeText(ActivityMovieDetails.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }else {
                Toast.makeText(ActivityMovieDetails.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
            }

            mPlayLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityMovieDetails.this, ActivityYouTubePlayer.class);
                    intent.putExtra("YT_VIDEO_KEY", "aqz-KE-bpKQ");
                    startActivity(intent);
                }
            });

            mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HelperClass.shareApp(ActivityMovieDetails.this, mMoviePoster, mTitleString, ActivityMovieDetails.this);
                }
            });

            if (SQLiteDB.isMovieFav(ActivityMovieDetails.this, mId)) {
                mWishlistButton.setImageResource(R.drawable.ic_favourite_check);
            } else {
                mWishlistButton.setImageResource(R.drawable.ic_favourite_uncheck);
            }

            mWishlistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SQLiteDB.isMovieFav(ActivityMovieDetails.this, mId)) {
                        SQLiteDB.removeMovieFromFav(ActivityMovieDetails.this, mId);
                        mWishlistButton.setImageResource(R.drawable.ic_favourite_uncheck);
                        Toast.makeText(ActivityMovieDetails.this, mTitleString + " Removed From Favourite.", Toast.LENGTH_SHORT).show();
                    } else {
                        SQLiteDB.addMovieToFav(ActivityMovieDetails.this, mId, mTitleString, mPosterString, mCategoryString,
                                mIMDbRatingString, mCertificateString);
                        mWishlistButton.setImageResource(R.drawable.ic_favourite_check);
                        Toast.makeText(ActivityMovieDetails.this, mTitleString + " Added To Favourite.", Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i <= list.size() - 1; i++){
            if (i != list.size() - 1){
                finalString = finalString + list.get(i) + ", ";
            }else {
                finalString = finalString + list.get(i).trim();
            }
        }
        return finalString;
    }

    private String returnReleaseYearString(ArrayList<String> list){
        return list.get(1).trim();
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

    private List<String> returnLowerCaseList(List<String> mGivenList){
        List<String> mList = new ArrayList<>();
        for (int i=0; i<mGivenList.size(); i++){
            mList.add(mGivenList.get(i).toLowerCase());
        }
        return mList;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
