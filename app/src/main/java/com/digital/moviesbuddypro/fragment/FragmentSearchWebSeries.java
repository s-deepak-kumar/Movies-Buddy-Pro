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
import com.digital.moviesbuddypro.CircleHeaderView;
import com.digital.moviesbuddypro.FootView;
import com.digital.moviesbuddypro.InternetConnection;
import com.digital.moviesbuddypro.activity.ActivityCategoryWebseries;
import com.digital.moviesbuddypro.activity.ActivityWebseriesDetails;
import com.digital.moviesbuddypro.model.WebSeries;
import com.digital.moviesbuddypro.utils.CallBack;
import com.digital.moviesbuddypro.utils.RequestAPI;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class FragmentSearchWebSeries extends Fragment implements View.OnClickListener {

    private PowerRefreshLayout mRefreshLayout;

    private LinearLayout mLoadingLayoutProgressBar, mNotFoundLayout;

    private RecyclerView mRecyclerViewWebSeries;
    private MyAdapter mAdapterWebSeries;

    private int pageSearchedWebseries = 1;
    private boolean isFinished = true;

    private TextView mRetryButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SEARCHABLE_STRING = "searchable_string";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mSearchableString;
    private String mParam2;

    public FragmentSearchWebSeries() {
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
    public static FragmentSearchWebSeries newInstance(String mSearchableString, String param2) {
        FragmentSearchWebSeries fragment = new FragmentSearchWebSeries();
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
        View v = inflater.inflate(R.layout.fragment_search_webseries, container, false);

        findViews(v);
        setUpViews();

        return v;
    }

    private void findViews(View v) {

        mRefreshLayout = (PowerRefreshLayout) v.findViewById(R.id.refresh_layout);

        mLoadingLayoutProgressBar = v.findViewById(R.id.loadingLayoutProgressBar);
        mNotFoundLayout = v.findViewById(R.id.notFoundLayout);

        mRecyclerViewWebSeries = v.findViewById(R.id.webseriesRecyleview);

        mRetryButton = (TextView) v.findViewById(R.id.retryButton);
    }

    private void setUpViews() {

        mRefreshLayout.setVisibility(View.GONE);
        mNotFoundLayout.setVisibility(View.GONE);
        mLoadingLayoutProgressBar.setVisibility(View.VISIBLE);

        mRetryButton.setOnClickListener(this);

        loadWebSeries(mSearchableString);
    }

    private void loadWebSeries(String mSearchedString){
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

                        if (InternetConnection.isNetworkConnected(getActivity())) {
                            JSONObject parameters = new JSONObject();

                            try {
                                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                                Log.e("Parameter:", parameters.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            RequestAPI.Call_Api(getActivity(), CONFIG.GET_SEARCHED_WEBSERIES + "&page="+pageSearchedWebseries + "&searched_string="+mSearchedString, parameters, new CallBack() {
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

                                            pageSearchedWebseries++;

                                            mAdapterWebSeries.appendList(mlist);

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
        mRecyclerViewWebSeries.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        mAdapterWebSeries = new MyAdapter();
        mRecyclerViewWebSeries.setAdapter(mAdapterWebSeries);

        List<WebSeries> mlist = new ArrayList<>();

        if (InternetConnection.isNetworkConnected(getActivity())) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(getActivity(), CONFIG.GET_SEARCHED_WEBSERIES + "&page="+pageSearchedWebseries + "&searched_string="+mSearchedString, parameters, new CallBack() {
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

                            pageSearchedWebseries++;

                            mAdapterWebSeries.setList(mlist);

                            if (mlist.size() > 0){
                                mRefreshLayout.setVisibility(View.VISIBLE);

                                mNotFoundLayout.setVisibility(View.GONE);
                                mLoadingLayoutProgressBar.setVisibility(View.GONE);
                            }else {
                                mRefreshLayout.setVisibility(View.GONE);

                                mNotFoundLayout.setVisibility(View.VISIBLE);
                                mLoadingLayoutProgressBar.setVisibility(View.GONE);
                            }
                        }else if (code.equals("400")){
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
                loadWebSeries(mSearchableString);
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

            Glide.with(getActivity())
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
                    String mId = webSeries.getId();
                    Intent intent = new Intent(getActivity(), ActivityWebseriesDetails.class);
                    intent.putExtra("WEBSERIES_ID_KEY", mId);
                    //intent.putExtra("MOVIE_CATEGORY_KEY", mCategory);
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