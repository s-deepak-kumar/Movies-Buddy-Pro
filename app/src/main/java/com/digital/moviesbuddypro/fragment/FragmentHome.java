package com.digital.moviesbuddypro.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.CONFIG;
import com.digital.moviesbuddypro.InternetConnection;
import com.digital.moviesbuddypro.activity.ActivityAllCasts;
import com.digital.moviesbuddypro.activity.ActivityAllCategory;
import com.digital.moviesbuddypro.activity.ActivityCategoryWebseries;
import com.digital.moviesbuddypro.activity.ActivityMovieDetails;
import com.digital.moviesbuddypro.activity.ActivitySearch;
import com.digital.moviesbuddypro.adapter.AdapterCast;
import com.digital.moviesbuddypro.adapter.AdapterLatestItemSlider;
import com.digital.moviesbuddypro.adapter.AdapterMovie;
import com.digital.moviesbuddypro.adapter.AdapterWebseries;
import com.digital.moviesbuddypro.adapter.ClickableAdapter;
import com.digital.moviesbuddypro.model.Cast;
import com.digital.moviesbuddypro.model.LatestItemSlider;
import com.digital.moviesbuddypro.model.Movie;
import com.digital.moviesbuddypro.model.WebSeries;
import com.digital.moviesbuddypro.utils.CallBack;
import com.digital.moviesbuddypro.utils.RequestAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.digital.moviesbuddypro.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment implements View.OnClickListener, SliderView.OnSliderPageListener{

    private SliderView mItemSliderView;
    private AdapterLatestItemSlider mAdapterLatestItemSlider;

    private NestedScrollView mNestedScrollView;
    private LinearLayout mLoadingLayoutProgressBar;
    private LinearLayout mNotFoundLayout;

    private RecyclerView mRecyclerViewBollywood, mRecyclerViewHollywood, mRecyclerViewTollywood,mRecyclerViewAnimated;
    private AdapterMovie mAdapterMovieBollywood, mAdapterMovieHollywood, mAdapterMovieTollywood, mAdapterMovieAnimated;
    private List<Movie> mListMovieBollywood, mListMovieHollywood, mListMovieTollywood, mListMovieAnimated;

    private RecyclerView mRecyclerViewCast;
    private AdapterCast mAdapterCast;
    private List<Cast> mListCast;

    private RecyclerView mRecyclerViewWebseries;
    private AdapterWebseries mAdapterWebseries;
    private List<WebSeries> mListWebseries;

    private LinearLayout mLoadingLayoutBollywood, mLoadingLayoutCast, mLoadingLayoutWebseries, mLoadingLayoutHollywood, mLoadingLayoutTollywood,
            mLoadingLayoutAnimated;
    private LinearLayout mLayoutBollywood, mLayoutCast, mLayoutWebseries, mLayoutHollywood, mLayoutTollywood, mLayoutAnimated;
    private LinearLayoutManager mLinearLayoutManagerBollywood, mLinearLayoutManagerCast, mLinearLayoutManagerWebseries, mLinearLayoutManagerHollywood,
            mLinearLayoutManagerTollywood, mLinearLayoutManagerAnimated;

    private boolean mIsAddedBollywood = false, mIsAddedCast = false, mIsAddedWebseries = false, mIsAddedHollywood =  false, mIsAddedTollywood = false,
            mIsAddedAnimated = false;
    private boolean mIsLoadingBollywood = false, mIsLoadingCast = false, mIsLoadingWebseries = false, mIsLoadingHollywood = false, mIsLoadingTollywood = false,
            mIsLoadingAnimated = false;

    //private boolean isFirstPageBollywood = true;
    private int pageBollywood = 1, pageCast = 1, pageWebseries = 1, pageHollywood = 1, pageTollywood = 1, pageAnimated = 1;

    private TextView mRetryButton;
    private ImageButton mSearchButton;

    private LinearLayout mBollywoodTabButton, mCastTabButton, mHollywoodTabButton, mTollywoodTabButton,
            mAnimatedTabButton, mWebseriesTabButton;
    private ImageButton mLoadMoreButtonBollywood, mLoadMoreButtonCast, mLoadMoreButtonHollywood, mLoadMoreButtonTollywood,
            mLoadMoreButtonAnimated, mLoadMoreButtonWebseries;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHome() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        findViews(v);
        setUpViews();

        return v;
    }

    private void findViews(View v) {
        mNestedScrollView = v.findViewById(R.id.nestedScrollView);
        mLoadingLayoutProgressBar = v.findViewById(R.id.loadingLayoutProgressBar);
        mNotFoundLayout = v.findViewById(R.id.notFoundLayout);

        mItemSliderView = v.findViewById(R.id.latestItemSlider);
        mRecyclerViewBollywood = v.findViewById(R.id.bollywoodRecyleview);
        mRecyclerViewCast = v.findViewById(R.id.castRecyleview);
        mRecyclerViewWebseries = v.findViewById(R.id.webseriesRecyleview);
        mRecyclerViewHollywood = v.findViewById(R.id.hollywoodRecyleview);
        mRecyclerViewTollywood = v.findViewById(R.id.tollywoodRecyleview);
        mRecyclerViewAnimated = v.findViewById(R.id.animatedRecyleview);

        mLoadingLayoutBollywood = v.findViewById(R.id.bollywoodLoadingLayout);
        mLoadingLayoutCast = v.findViewById(R.id.castLoadingLayout);
        mLoadingLayoutWebseries = v.findViewById(R.id.webseriesLoadingLayout);
        mLoadingLayoutHollywood = v.findViewById(R.id.hollywoodLoadingLayout);
        mLoadingLayoutTollywood = v.findViewById(R.id.tollywoodLoadingLayout);
        mLoadingLayoutAnimated = v.findViewById(R.id.animatedLoadingLayout);

        mLayoutBollywood = v.findViewById(R.id.bollywoodLayout);
        mLayoutCast = v.findViewById(R.id.castLayout);
        mLayoutWebseries = v.findViewById(R.id.webseriesLayout);
        mLayoutHollywood = v.findViewById(R.id.hollywoodLayout);
        mLayoutTollywood = v.findViewById(R.id.tollywoodLayout);
        mLayoutAnimated = v.findViewById(R.id.animatedLayout);

        mBollywoodTabButton = v.findViewById(R.id.bollywoodTabButton);
        mCastTabButton = v.findViewById(R.id.castTabButton);
        mHollywoodTabButton = v.findViewById(R.id.hollywoodTabButton);
        mTollywoodTabButton = v.findViewById(R.id.tollywoodTabButton);
        mAnimatedTabButton = v.findViewById(R.id.animatedTabButton);
        mWebseriesTabButton = v.findViewById(R.id.webseriesTabButton);

        mLoadMoreButtonBollywood = v.findViewById(R.id.loadMoreBollywoodButton);
        mLoadMoreButtonCast = v.findViewById(R.id.loadMoreCastButton);
        mLoadMoreButtonHollywood = v.findViewById(R.id.loadMoreHollywoodButton);
        mLoadMoreButtonTollywood = v.findViewById(R.id.loadMoreTollywoodButton);
        mLoadMoreButtonAnimated = v.findViewById(R.id.loadMoreAnimatedButton);
        mLoadMoreButtonWebseries = v.findViewById(R.id.loadMoreWebseriesButton);

        mRetryButton = v.findViewById(R.id.retryButton);

        mSearchButton = v.findViewById(R.id.searchButton);

        mAdapterLatestItemSlider = new AdapterLatestItemSlider(getActivity());

        mListMovieBollywood = new ArrayList<>();
        mListCast = new ArrayList<>();
        mListWebseries = new ArrayList<>();
        mListMovieHollywood = new ArrayList<>();
        mListMovieTollywood = new ArrayList<>();
        mListMovieAnimated = new ArrayList<>();

        mAdapterMovieBollywood = new AdapterMovie(getActivity(), mListMovieBollywood, "Bollywood", R.layout.item_movie_home);
        mAdapterCast = new AdapterCast(getActivity(), mListCast, R.layout.item_cast_home);
        mAdapterWebseries = new AdapterWebseries(getActivity(), mListWebseries, R.layout.item_webseries_home);
        mAdapterMovieHollywood = new AdapterMovie(getActivity(), mListMovieHollywood, "Hollywood", R.layout.item_movie_home);
        mAdapterMovieTollywood = new AdapterMovie(getActivity(), mListMovieTollywood, "Tollywood", R.layout.item_movie_home);
        mAdapterMovieAnimated = new AdapterMovie(getActivity(), mListMovieAnimated, "Animated", R.layout.item_movie_home);

        mLinearLayoutManagerBollywood = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mLinearLayoutManagerCast = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mLinearLayoutManagerWebseries = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mLinearLayoutManagerHollywood = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mLinearLayoutManagerTollywood = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mLinearLayoutManagerAnimated = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
    }

    private void setUpViews() {
        mItemSliderView.setSliderAdapter(mAdapterLatestItemSlider);

        mItemSliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        mItemSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mItemSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        mItemSliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        mItemSliderView.startAutoCycle();
        mItemSliderView.setCurrentPageListener(this);

        mLoadingLayoutProgressBar.setVisibility(View.VISIBLE);

        getLatestItemData();
        loadMovieBollywood();
        loadCast();
        loadWebseries();
        loadMovieHollywood();
        loadMovieTollywood();
        loadMovieAnimated();
        //addWebSeries();

        mBollywoodTabButton.setOnClickListener(this);
        mCastTabButton.setOnClickListener(this);
        mHollywoodTabButton.setOnClickListener(this);
        mTollywoodTabButton.setOnClickListener(this);
        mAnimatedTabButton.setOnClickListener(this);
        mWebseriesTabButton.setOnClickListener(this);

        mLoadMoreButtonBollywood.setOnClickListener(this);
        mLoadMoreButtonCast.setOnClickListener(this);
        mLoadMoreButtonHollywood.setOnClickListener(this);
        mLoadMoreButtonTollywood.setOnClickListener(this);
        mLoadMoreButtonAnimated.setOnClickListener(this);
        mLoadMoreButtonWebseries.setOnClickListener(this);

        mRetryButton.setOnClickListener(this);

        mSearchButton.setOnClickListener(this);
    }

    private void getLatestItemData(){
        if (InternetConnection.isNetworkConnected(getActivity())) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(getActivity(), CONFIG.GET_LATEST_ITEMS, parameters, new CallBack() {
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

                                LatestItemSlider latestItemSlider = new LatestItemSlider();
                                latestItemSlider.setId(Long.toString(userdata.optLong("id")));
                                latestItemSlider.setItemCategory(userdata.optString("category"));
                                latestItemSlider.setItemTitle(userdata.optString("title"));
                                latestItemSlider.setItemPoster(userdata.optString("poster"));
                                latestItemSlider.setItemSeasonTag("");

                                if (userdata.optString("imdb_rating").equals("null")) {
                                    latestItemSlider.setItemIMDBRating("∞");
                                } else {
                                    latestItemSlider.setItemIMDBRating(userdata.optString("imdb_rating"));
                                }

                                if (userdata.optString("certificate").equals("Not Rated")) {
                                    latestItemSlider.setItemCertificate("N/A");
                                } else {
                                    latestItemSlider.setItemCertificate(userdata.optString("certificate").toString());
                                }

                                mAdapterLatestItemSlider.addItem(latestItemSlider);
                            }
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMovieBollywood(){
        if (InternetConnection.isNetworkConnected(getActivity())) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(getActivity(), CONFIG.GET_MOVIES + "&page="+pageBollywood + "&category=Bollywood", parameters, new CallBack() {
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

                                mListMovieBollywood.add(movie);
                            }

                            pageBollywood++;

                            if (mListMovieBollywood.size() > 0){
                                mLayoutBollywood.setVisibility(View.VISIBLE);
                            }else {
                                mLayoutBollywood.setVisibility(View.GONE);
                            }

                            mRecyclerViewBollywood.setHasFixedSize(true);
                            mRecyclerViewBollywood.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerViewBollywood.setLayoutManager(mLinearLayoutManagerBollywood);
                            mRecyclerViewBollywood.setAdapter(mAdapterMovieBollywood);
                            mRecyclerViewBollywood.setNestedScrollingEnabled(false);

                            RecyclerView.OnScrollListener onScrollListener =
                                    new RecyclerView.OnScrollListener(){
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);

                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);

                                            if (!mIsLoadingBollywood) {
                                                if (mLinearLayoutManagerBollywood != null && mLinearLayoutManagerBollywood.findLastCompletelyVisibleItemPosition() == mListMovieBollywood.size() - 1) {
                                                    //bottom of list!
                                                    mIsLoadingBollywood = true;

                                                    if (!mIsAddedBollywood) {
                                                        mLoadingLayoutBollywood.setVisibility(View.VISIBLE);
                                                    }

                                                    if (InternetConnection.isNetworkConnected(getActivity())) {
                                                        RequestAPI.Call_Api(getActivity(), CONFIG.GET_MOVIES + "&page="+pageBollywood + "&category=Bollywood", parameters, new CallBack() {
                                                            @Override
                                                            public void Response(String resp) {
                                                                mLoadingLayoutBollywood.setVisibility(View.GONE);

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

                                                                            mListMovieBollywood.add(movie);
                                                                        }

                                                                        pageBollywood++;

                                                                        mIsAddedBollywood = false;
                                                                        mAdapterMovieBollywood.notifyDataSetChanged();
                                                                        mIsLoadingBollywood = false;
                                                                    }
                                                                }catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }else {
                                                        Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    };
                            mRecyclerViewBollywood.addOnScrollListener(onScrollListener);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    /*public static String capitalize (String givenString) {
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

    private String returnCommaSeperatedString(ArrayList<String> list){
        String finalString = "";
        for (int i = 0; i <= list.size() - 1; i++){
            if (i != list.size() - 1){
                finalString = finalString + capitalize(list.get(i)) + ",";
            }else {
                finalString = finalString + capitalize(list.get(i));
            }
        }
        return finalString;
    }

    private String returnSeperatedPosterString(ArrayList<String> list){
        String finalString = "";
        for (int i = 0; i <= list.size() - 1; i++){
            if (i != list.size() - 1){
                finalString = finalString + capitalize(list.get(i)) + "##!@##";
            }else {
                finalString = finalString + capitalize(list.get(i));
            }
        }
        return finalString;
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

    private void addWebSeries(){
        if (InternetConnection.isNetworkConnected(getActivity())) {
            db.collection("WebSeries")
                    .orderBy("last_modified", Query.Direction.DESCENDING)
                    //.limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    JSONObject parameters = new JSONObject();

                                    db.collection("WebSeries_Details")
                                            .whereEqualTo("id", document.getData().get("id").toString())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document1 : task.getResult()) {

                                                            try {
                                                                //parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));
                                                                parameters.put("total_season", document.getData().get("total_season").toString());
                                                                parameters.put("main_thumbnail", document.getData().get("main_thumbnail").toString());
                                                                parameters.put("certificate", document.getData().get("certificate").toString());
                                                                parameters.put("imdb_rating", document.getData().get("imdb_rating").toString());
                                                                parameters.put("languages", returnCommaSeperatedString((ArrayList<String>) document.getData().get("languages")));
                                                                parameters.put("season_tag", document.getData().get("season_tag").toString());
                                                                String releaseDate =  returnReleaseDateString((ArrayList<String>) document.getData().get("release_date"));
                                                                SimpleDateFormat format1 = new SimpleDateFormat("dd MMM, yyyy");
                                                                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                                                                Date date = null;
                                                                try {
                                                                    date = format1.parse(releaseDate);
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                parameters.put("release_date", format2.format(date));
                                                                parameters.put("title", document.getData().get("title").toString());
                                                                parameters.put("actors", returnCommaSeperatedString((ArrayList<String>) document1.getData().get("actors")));
                                                                parameters.put("total_episode", returnCommaSeperatedString((ArrayList<String>) document1.getData().get("total_episode")));
                                                                parameters.put("description", document1.getData().get("description").toString());
                                                                if (((ArrayList<String>) document1.getData().get("creators")).size() > 0) {
                                                                    parameters.put("creators", returnCommaSeperatedString((ArrayList<String>) document1.getData().get("creators")));
                                                                }else {
                                                                    parameters.put("creators", "");
                                                                }
                                                                parameters.put("duration", document1.getData().get("duration").toString());
                                                                parameters.put("genres", returnCommaSeperatedString((ArrayList<String>) document1.getData().get("genres")));
                                                                parameters.put("original_network", returnCommaSeperatedString((ArrayList<String>) document1.getData().get("original_network")));
                                                                parameters.put("season_poster", returnSeperatedPosterString((ArrayList<String>) document1.getData().get("season_poster")));

                                                                Log.e("Parameter:", parameters.toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                            Log.e("MovieDetails: ", parameters.toString());
                                                            //Functions.Show_loader(this, false, false);
                                                            //loadingLayout.setVisibility(View.VISIBLE);
                                                            //Bottom Loading
                                                            RequestAPI.Call_Api(getActivity(), CONFIG.ADD_MOVIE, parameters, new CallBack() {
                                                                @Override
                                                                public void Response(String resp) {
                                                                    //Functions.cancel_loader();
                                                                    //progressLayout.setVisibility(View.GONE);
                                                                    //loadingLayout.setVisibility(View.GONE);
                                                                    //Bottom Loading
                                                                    //parseData(resp, isFirstPage);
                                                                    Toast.makeText(getActivity(), resp, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        Log.d("DetailedMovie", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("On Failure:", e.toString());
                        }
                    });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void addMovie(){
        if (InternetConnection.isNetworkConnected(getActivity())) {
            db.collection("Movies")
                    .orderBy("last_modified", Query.Direction.DESCENDING)
                    //.limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    JSONObject parameters = new JSONObject();

                                    db.collection("Movies_Details")
                                            .whereEqualTo("id", document.getData().get("id").toString())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document1 : task.getResult()) {

                                                            try {
                                                                //parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));
                                                                parameters.put("category", document.getData().get("category").toString());
                                                                parameters.put("certificate", document.getData().get("certificate").toString());
                                                                parameters.put("imdb_rating", document.getData().get("imdb_rating").toString());
                                                                parameters.put("languages", returnCommaSeperatedString((ArrayList<String>) document.getData().get("languages")));
                                                                parameters.put("poster", document.getData().get("poster").toString());
                                                                String releaseDate =  returnReleaseDateString((ArrayList<String>) document.getData().get("release_date"));
                                                                SimpleDateFormat format1 = new SimpleDateFormat("dd MMM, yyyy");
                                                                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
                                                                Date date = null;
                                                                try {
                                                                    date = format1.parse(releaseDate);
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                parameters.put("release_date", format2.format(date));
                                                                parameters.put("title", document.getData().get("title").toString());
                                                                parameters.put("actors", returnCommaSeperatedString((ArrayList<String>) document1.getData().get("actors")));
                                                                parameters.put("description", document1.getData().get("description").toString());
                                                                if (((ArrayList<String>) document1.getData().get("directors")).size() > 0) {
                                                                    parameters.put("directors", returnCommaSeperatedString((ArrayList<String>) document1.getData().get("directors")));
                                                                }else {
                                                                    parameters.put("directors", "");
                                                                }
                                                                parameters.put("duration", document1.getData().get("duration").toString());
                                                                parameters.put("genres", returnCommaSeperatedString((ArrayList<String>) document1.getData().get("genres")));
                                                                parameters.put("writers", returnCommaSeperatedString((ArrayList<String>) document1.getData().get("writers")));

                                                                Log.e("Parameter:", parameters.toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                            Log.e("MovieDetails: ", parameters.toString());
                                                            //Functions.Show_loader(this, false, false);
                                                            //loadingLayout.setVisibility(View.VISIBLE);
                                                            //Bottom Loading
                                                            RequestAPI.Call_Api(getActivity(), CONFIG.ADD_MOVIE, parameters, new CallBack() {
                                                                @Override
                                                                public void Response(String resp) {
                                                                    //Functions.cancel_loader();
                                                                    //progressLayout.setVisibility(View.GONE);
                                                                    //loadingLayout.setVisibility(View.GONE);
                                                                    //Bottom Loading
                                                                    //parseData(resp, isFirstPage);
                                                                    Toast.makeText(getActivity(), resp, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        Log.d("DetailedMovie", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("On Failure:", e.toString());
                        }
                    });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void loadCast(){
        if (InternetConnection.isNetworkConnected(getActivity())) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(getActivity(), CONFIG.GET_CASTS + "&page="+pageCast, parameters, new CallBack() {
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

                                mListCast.add(cast);
                            }

                            if (mListCast.size() > 0){
                                mLayoutCast.setVisibility(View.VISIBLE);
                            }else {
                                mLayoutCast.setVisibility(View.GONE);
                            }

                            pageCast++;

                            mRecyclerViewCast.setHasFixedSize(true);
                            mRecyclerViewCast.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerViewCast.setLayoutManager(mLinearLayoutManagerCast);
                            mRecyclerViewCast.setAdapter(mAdapterCast);
                            mRecyclerViewCast.setNestedScrollingEnabled(false);

                            RecyclerView.OnScrollListener onScrollListener =
                                    new RecyclerView.OnScrollListener(){
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);

                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);

                                            if (!mIsLoadingCast) {
                                                if (mLinearLayoutManagerCast != null && mLinearLayoutManagerCast.findLastCompletelyVisibleItemPosition() == mListCast.size() - 1) {
                                                    //bottom of list!
                                                    mIsLoadingCast = true;

                                                    if (!mIsAddedCast) {
                                                        mLoadingLayoutCast.setVisibility(View.VISIBLE);
                                                    }

                                                    if (InternetConnection.isNetworkConnected(getActivity())) {
                                                        RequestAPI.Call_Api(getActivity(), CONFIG.GET_CASTS + "&page="+pageCast, parameters, new CallBack() {
                                                            @Override
                                                            public void Response(String resp) {
                                                                mLoadingLayoutCast.setVisibility(View.GONE);

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

                                                                            mListCast.add(cast);
                                                                        }

                                                                        pageCast++;

                                                                        mIsAddedCast = false;
                                                                        mAdapterCast.notifyDataSetChanged();
                                                                        mIsLoadingCast = false;
                                                                    }
                                                                }catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }else {
                                                        Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    };
                            mRecyclerViewCast.addOnScrollListener(onScrollListener);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadWebseries(){
        if (InternetConnection.isNetworkConnected(getActivity())) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(getActivity(), CONFIG.GET_WEBSERIES + "&page="+pageWebseries, parameters, new CallBack() {
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

                                mListWebseries.add(webSeries);
                            }

                            pageWebseries++;

                            if (mListWebseries.size() > 0){
                                mLayoutWebseries.setVisibility(View.VISIBLE);
                            }else {
                                mLayoutWebseries.setVisibility(View.GONE);
                            }

                            mRecyclerViewWebseries.setHasFixedSize(true);
                            mRecyclerViewWebseries.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerViewWebseries.setLayoutManager(mLinearLayoutManagerWebseries);
                            mRecyclerViewWebseries.setAdapter(mAdapterWebseries);
                            mRecyclerViewWebseries.setNestedScrollingEnabled(false);

                            RecyclerView.OnScrollListener onScrollListener =
                                    new RecyclerView.OnScrollListener(){
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);

                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);

                                            if (!mIsLoadingWebseries) {
                                                if (mLinearLayoutManagerWebseries != null && mLinearLayoutManagerWebseries.findLastCompletelyVisibleItemPosition() == mListWebseries.size() - 1) {
                                                    //bottom of list!
                                                    mIsLoadingWebseries = true;

                                                    if (!mIsAddedWebseries) {
                                                        mLoadingLayoutWebseries.setVisibility(View.VISIBLE);
                                                    }

                                                    if (InternetConnection.isNetworkConnected(getActivity())) {
                                                        RequestAPI.Call_Api(getActivity(), CONFIG.GET_WEBSERIES + "&page="+pageWebseries, parameters, new CallBack() {
                                                            @Override
                                                            public void Response(String resp) {
                                                                mLoadingLayoutWebseries.setVisibility(View.GONE);

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

                                                                            mListWebseries.add(webSeries);
                                                                        }

                                                                        pageWebseries++;

                                                                        mIsAddedWebseries = false;
                                                                        mAdapterWebseries.notifyDataSetChanged();
                                                                        mIsLoadingWebseries = false;
                                                                    }
                                                                }catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }else {
                                                        Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    };
                            mRecyclerViewWebseries.addOnScrollListener(onScrollListener);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMovieHollywood(){
        if (InternetConnection.isNetworkConnected(getActivity())) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(getActivity(), CONFIG.GET_MOVIES + "&page="+pageHollywood + "&category=Hollywood", parameters, new CallBack() {
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

                                mListMovieHollywood.add(movie);
                            }

                            pageHollywood++;

                            if (mListMovieHollywood.size() > 0){
                                mLayoutHollywood.setVisibility(View.VISIBLE);
                            }else {
                                mLayoutHollywood.setVisibility(View.GONE);
                            }

                            mRecyclerViewHollywood.setHasFixedSize(true);
                            mRecyclerViewHollywood.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerViewHollywood.setLayoutManager(mLinearLayoutManagerHollywood);
                            mRecyclerViewHollywood.setAdapter(mAdapterMovieHollywood);
                            mRecyclerViewHollywood.setNestedScrollingEnabled(false);

                            RecyclerView.OnScrollListener onScrollListener =
                                    new RecyclerView.OnScrollListener(){
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);

                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);

                                            if (!mIsLoadingHollywood) {
                                                if (mLinearLayoutManagerHollywood != null && mLinearLayoutManagerHollywood.findLastCompletelyVisibleItemPosition() == mListMovieHollywood.size() - 1) {
                                                    //bottom of list!
                                                    mIsLoadingHollywood = true;

                                                    if (!mIsAddedHollywood) {
                                                        mLoadingLayoutHollywood.setVisibility(View.VISIBLE);
                                                    }

                                                    if (InternetConnection.isNetworkConnected(getActivity())) {
                                                        RequestAPI.Call_Api(getActivity(), CONFIG.GET_MOVIES + "&page="+pageHollywood + "&category=Hollywood", parameters, new CallBack() {
                                                            @Override
                                                            public void Response(String resp) {
                                                                mLoadingLayoutHollywood.setVisibility(View.GONE);

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

                                                                            mListMovieHollywood.add(movie);
                                                                        }

                                                                        pageHollywood++;

                                                                        mIsAddedHollywood = false;
                                                                        mAdapterMovieHollywood.notifyDataSetChanged();
                                                                        mIsLoadingHollywood = false;
                                                                    }
                                                                }catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }else {
                                                        Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    };
                            mRecyclerViewHollywood.addOnScrollListener(onScrollListener);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMovieTollywood(){
        if (InternetConnection.isNetworkConnected(getActivity())) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(getActivity(), CONFIG.GET_MOVIES + "&page="+pageTollywood + "&category=Tollywood", parameters, new CallBack() {
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

                                mListMovieTollywood.add(movie);
                            }

                            pageTollywood++;

                            if (mListMovieTollywood.size() > 0){
                                mLayoutTollywood.setVisibility(View.VISIBLE);
                            }else {
                                mLayoutTollywood.setVisibility(View.GONE);
                            }

                            mRecyclerViewTollywood.setHasFixedSize(true);
                            mRecyclerViewTollywood.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerViewTollywood.setLayoutManager(mLinearLayoutManagerTollywood);
                            mRecyclerViewTollywood.setAdapter(mAdapterMovieTollywood);
                            mRecyclerViewTollywood.setNestedScrollingEnabled(false);

                            RecyclerView.OnScrollListener onScrollListener =
                                    new RecyclerView.OnScrollListener(){
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);

                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);

                                            if (!mIsLoadingTollywood) {
                                                if (mLinearLayoutManagerTollywood != null && mLinearLayoutManagerTollywood.findLastCompletelyVisibleItemPosition() == mListMovieTollywood.size() - 1) {
                                                    //bottom of list!
                                                    mIsLoadingTollywood = true;

                                                    if (!mIsAddedTollywood) {
                                                        mLoadingLayoutTollywood.setVisibility(View.VISIBLE);
                                                    }

                                                    if (InternetConnection.isNetworkConnected(getActivity())) {
                                                        RequestAPI.Call_Api(getActivity(), CONFIG.GET_MOVIES + "&page="+pageTollywood + "&category=Tollywood", parameters, new CallBack() {
                                                            @Override
                                                            public void Response(String resp) {
                                                                mLoadingLayoutTollywood.setVisibility(View.GONE);

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

                                                                            mListMovieTollywood.add(movie);
                                                                        }

                                                                        pageTollywood++;

                                                                        mIsAddedTollywood = false;
                                                                        mAdapterMovieTollywood.notifyDataSetChanged();
                                                                        mIsLoadingTollywood = false;
                                                                    }
                                                                }catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }else {
                                                        Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    };
                            mRecyclerViewTollywood.addOnScrollListener(onScrollListener);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMovieAnimated(){
        if (InternetConnection.isNetworkConnected(getActivity())) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(getActivity(), CONFIG.GET_MOVIES + "&page="+pageBollywood + "&category=Animated", parameters, new CallBack() {
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

                                mListMovieAnimated.add(movie);
                            }

                            pageAnimated++;

                            if (mListMovieBollywood.size() == 0 && mListMovieHollywood.size() == 0 &&
                                    mListMovieTollywood.size() == 0 && mListMovieAnimated.size() == 0){
                                mNotFoundLayout.setVisibility(View.VISIBLE);
                                mNestedScrollView.setVisibility(View.GONE);
                                mLoadingLayoutProgressBar.setVisibility(View.GONE);
                            }else {
                                mNotFoundLayout.setVisibility(View.GONE);
                                mNestedScrollView.setVisibility(View.VISIBLE);
                                mLoadingLayoutProgressBar.setVisibility(View.GONE);
                            }

                            if (mListMovieAnimated.size() > 0){
                                mLayoutAnimated.setVisibility(View.VISIBLE);
                            }else {
                                mLayoutAnimated.setVisibility(View.GONE);
                            }

                            mRecyclerViewAnimated.setHasFixedSize(true);
                            mRecyclerViewAnimated.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerViewAnimated.setLayoutManager(mLinearLayoutManagerAnimated);
                            mRecyclerViewAnimated.setAdapter(mAdapterMovieAnimated);
                            mRecyclerViewAnimated.setNestedScrollingEnabled(false);

                            RecyclerView.OnScrollListener onScrollListener =
                                    new RecyclerView.OnScrollListener(){
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);

                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);

                                            if (!mIsLoadingAnimated) {
                                                if (mLinearLayoutManagerAnimated != null && mLinearLayoutManagerAnimated.findLastCompletelyVisibleItemPosition() == mListMovieAnimated.size() - 1) {
                                                    //bottom of list!
                                                    mIsLoadingAnimated = true;

                                                    if (!mIsAddedAnimated) {
                                                        mLoadingLayoutAnimated.setVisibility(View.VISIBLE);
                                                    }

                                                    if (InternetConnection.isNetworkConnected(getActivity())) {
                                                        RequestAPI.Call_Api(getActivity(), CONFIG.GET_MOVIES + "&page="+pageAnimated + "&category=Animated", parameters, new CallBack() {
                                                            @Override
                                                            public void Response(String resp) {
                                                                mLoadingLayoutAnimated.setVisibility(View.GONE);

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

                                                                            mListMovieAnimated.add(movie);
                                                                        }

                                                                        pageAnimated++;

                                                                        mIsAddedAnimated = false;
                                                                        mAdapterMovieAnimated.notifyDataSetChanged();
                                                                        mIsLoadingAnimated = false;
                                                                    }
                                                                }catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }else {
                                                        Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    };
                            mRecyclerViewAnimated.addOnScrollListener(onScrollListener);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int mId = v.getId();
        Intent intent;
        switch (mId){
            case R.id.retryButton:
                mLoadingLayoutProgressBar.setVisibility(View.VISIBLE);
                mNotFoundLayout.setVisibility(View.GONE);
                mNestedScrollView.setVisibility(View.GONE);

                getLatestItemData();
                loadMovieBollywood();
                loadCast();
                loadWebseries();
                loadMovieHollywood();
                loadMovieTollywood();
                loadMovieAnimated();
                break;
            case R.id.bollywoodTabButton:
                mLoadMoreButtonBollywood.performClick();
                break;
            case R.id.castTabButton:
                //mLoadMoreButtonBollywood.performClick();
                break;
            case R.id.hollywoodTabButton:
                mLoadMoreButtonHollywood.performClick();
                break;
            case R.id.tollywoodTabButton:
                mLoadMoreButtonTollywood.performClick();
                break;
            case R.id.animatedTabButton:
                mLoadMoreButtonAnimated.performClick();
                break;
            case R.id.webseriesTabButton:
                mLoadMoreButtonWebseries.performClick();
                break;
            case R.id.loadMoreBollywoodButton:
                intent = new Intent(getActivity(), ActivityAllCategory.class);
                intent.putExtra("CATEGORY_KEY", "Bollywood");
                intent.putExtra("CATEGORY_BANNER_KEY", R.mipmap.bollywood_poster_landscape);
                startActivity(intent);
                break;
            case R.id.loadMoreCastButton:
                intent = new Intent(getActivity(), ActivityAllCasts.class);
                intent.putExtra("CATEGORY_KEY", "Cast");
                intent.putExtra("CATEGORY_BANNER_KEY", R.drawable.person_poster);
                startActivity(intent);
                break;
            case R.id.loadMoreHollywoodButton:
                intent = new Intent(getActivity(), ActivityAllCategory.class);
                intent.putExtra("CATEGORY_KEY", "Hollywood");
                intent.putExtra("CATEGORY_BANNER_KEY", R.mipmap.hollywood_poster_landscape);
                startActivity(intent);
                break;
            case R.id.loadMoreTollywoodButton:
                intent = new Intent(getActivity(), ActivityAllCategory.class);
                intent.putExtra("CATEGORY_KEY", "Tollywood");
                intent.putExtra("CATEGORY_BANNER_KEY", R.mipmap.tollywood_poster_landscape);
                startActivity(intent);
                break;
            case R.id.loadMoreAnimatedButton:
                intent = new Intent(getActivity(), ActivityAllCategory.class);
                intent.putExtra("CATEGORY_KEY", "Animated");
                intent.putExtra("CATEGORY_BANNER_KEY", R.mipmap.animated_poster_landscape);
                startActivity(intent);
                break;
            case R.id.loadMoreWebseriesButton:
                intent = new Intent(getActivity(), ActivityCategoryWebseries.class);
                startActivity(intent);
                break;
            case R.id.searchButton:
                startActivity(new Intent(getActivity(), ActivitySearch.class));
                break;
        }
    }

    @Override
    public void onSliderPageChanged(int position) {

    }
}
