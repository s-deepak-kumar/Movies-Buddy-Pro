package com.digital.moviesbuddypro.fragment;

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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.digital.moviesbuddypro.CONFIG;
import com.digital.moviesbuddypro.FootView;
import com.digital.moviesbuddypro.InternetConnection;
import com.digital.moviesbuddypro.activity.ActivityAllCategory;
import com.digital.moviesbuddypro.activity.ActivityMovieDetails;
import com.digital.moviesbuddypro.model.Movie;
import com.digital.moviesbuddypro.utils.CallBack;
import com.digital.moviesbuddypro.utils.RequestAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.lovejjfg.powerrecycle.PowerAdapter;
import com.lovejjfg.powerrecycle.holder.PowerHolder;
import com.digital.moviesbuddypro.CircleHeaderView;
import com.digital.moviesbuddypro.R;
import com.movies.powerrefresh.OnRefreshListener;
import com.movies.powerrefresh.PowerRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearchMovies extends Fragment implements View.OnClickListener {

    private PowerRefreshLayout mRefreshLayout;

    private LinearLayout mLoadingLayoutProgressBar, mNotFoundLayout;

    private RecyclerView mRecyclerViewMovie;
    private MyAdapter mAdapterMovie;

    private int pageSearchedMovie = 1;
    private boolean isFinished = true;

    private TextView mRetryButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SEARCHABLE_STRING = "searchable_string";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mSearchableString;
    private String mParam2;

    public FragmentSearchMovies() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mSearchableString Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearchMovies newInstance(String mSearchableString, String param2) {
        FragmentSearchMovies fragment = new FragmentSearchMovies();
        Bundle args = new Bundle();
        args.putString(SEARCHABLE_STRING, mSearchableString);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchableString = getArguments().getString(SEARCHABLE_STRING);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_movies, container, false);

        findViews(v);
        setUpViews();

        return v;
    }

    private void findViews(View v) {
        mRefreshLayout = (PowerRefreshLayout) v.findViewById(R.id.refresh_layout);

        mLoadingLayoutProgressBar = v.findViewById(R.id.loadingLayoutProgressBar);
        mNotFoundLayout = v.findViewById(R.id.notFoundLayout);

        mRecyclerViewMovie = v.findViewById(R.id.movieRecyleview);

        mRetryButton = (TextView) v.findViewById(R.id.retryButton);
    }

    private void setUpViews() {

        mRefreshLayout.setVisibility(View.GONE);
        mNotFoundLayout.setVisibility(View.GONE);
        mLoadingLayoutProgressBar.setVisibility(View.VISIBLE);

        mRetryButton.setOnClickListener(this);

        loadMovies(mSearchableString);
    }

    private void loadMovies(String mSearchedString){
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

                        if (InternetConnection.isNetworkConnected(getActivity())) {
                            JSONObject parameters = new JSONObject();

                            try {
                                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                                Log.e("Parameter:", parameters.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            RequestAPI.Call_Api(getActivity(), CONFIG.GET_SEARCHED_MOVIES + "&page="+pageSearchedMovie + "&searched_string="+mSearchedString, parameters, new CallBack() {
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

                                            pageSearchedMovie++;

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
                            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                        }

                        mRefreshLayout.setLoadEnable(isFinished);
                    }
                }, 1000);
            }
        });

        CircleHeaderView header = new CircleHeaderView(getActivity());
        FootView footView = new FootView(getActivity());
        mRefreshLayout.setRefreshEnable(false);
        mRefreshLayout.addHeader(header);
        mRefreshLayout.addFooter(footView);
        mRefreshLayout.setAutoLoadMore(true);
        mRefreshLayout.setAutoRefresh(false);
        mRecyclerViewMovie.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        mAdapterMovie = new MyAdapter();
        mRecyclerViewMovie.setAdapter(mAdapterMovie);
        List<Movie> mlist = new ArrayList<>();

        if (InternetConnection.isNetworkConnected(getActivity())) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(getActivity(), CONFIG.GET_SEARCHED_MOVIES + "&page="+pageSearchedMovie + "&searched_string="+mSearchedString, parameters, new CallBack() {
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

                            pageSearchedMovie++;

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
                        } else if (code.equals("400")){
                            mRefreshLayout.setVisibility(View.GONE);

                            mNotFoundLayout.setVisibility(View.VISIBLE);
                            mLoadingLayoutProgressBar.setVisibility(View.GONE);
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
        switch (mId){
            case R.id.retryButton:
                loadMovies(mSearchableString);
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

            Glide.with(getActivity())
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
                    String mId = movie.getId();
                    Intent intent = new Intent(getActivity(), ActivityMovieDetails.class);
                    intent.putExtra("MOVIE_ID_KEY", mId);
                    intent.putExtra("MOVIE_CATEGORY_KEY", movie.getCategory());
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
}