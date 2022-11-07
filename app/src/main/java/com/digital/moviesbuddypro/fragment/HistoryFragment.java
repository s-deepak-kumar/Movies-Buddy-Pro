package com.digital.moviesbuddypro.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.model.Movie;
import com.digital.moviesbuddypro.model.WebSeries;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.adapter.AdapterMovie;
import com.digital.moviesbuddypro.adapter.AdapterWebseries;
import com.digital.moviesbuddypro.sqlitedatabase.SQLiteDB;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private String mCategory;

    private RecyclerView mRecyclerViewHistory;
    private List<Movie> mListHistoryMovie;
    private AdapterMovie mAdapterHistoryMovie;

    private List<WebSeries> mListHistoryWebSeries;
    private AdapterWebseries mAdapterHistoryWebSeries;

    private LinearLayout mNotFoundLayout;
    private NestedScrollView mNestedScrollView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public HistoryFragment(String mCategory) {
        this.mCategory = mCategory;
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
    public static FragmentWishlist newInstance(String param1, String param2) {
        FragmentWishlist fragment = new FragmentWishlist();
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
        View v = inflater.inflate(R.layout.history_fragment, container, false);

        findViews(v);
        setViews();

        return v;
    }

    private void findViews(View v){
        mNotFoundLayout = v.findViewById(R.id.notFoundLayout);
        mNestedScrollView = v.findViewById(R.id.nestedScrollView);
        mRecyclerViewHistory = v.findViewById(R.id.historyRecyclerView);

        mListHistoryMovie = new ArrayList<>();
        mAdapterHistoryMovie = new AdapterMovie(getActivity(), mListHistoryMovie, "All", R.layout.item_movie);

        mListHistoryWebSeries = new ArrayList<>();
        mAdapterHistoryWebSeries = new AdapterWebseries(getActivity(), mListHistoryWebSeries, R.layout.item_webseries);
    }

    private void setViews(){
        if (mCategory.equalsIgnoreCase("Web Series")){
            mRecyclerViewHistory.setAdapter(mAdapterHistoryWebSeries);
            mRecyclerViewHistory.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }else {
            mRecyclerViewHistory.setAdapter(mAdapterHistoryMovie);
            mRecyclerViewHistory.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCategory.equalsIgnoreCase("Web Series")) {
            mAdapterHistoryWebSeries.clear();
            loadHistoryWebSeries();
        }else {
            mAdapterHistoryMovie.clear();
            loadHistoryMovies();
        }
        //mFavMoviesAdapter.notifyDataSetChanged();
    }

    private void loadHistoryMovies() {
        List<Movie> historyMovieBriefs = SQLiteDB.getRecentMovieBriefs(getContext());
        if (historyMovieBriefs.isEmpty()) {
            mNotFoundLayout.setVisibility(View.VISIBLE);
            mNestedScrollView.setVisibility(View.GONE);
            //mFavMoviesAdapter.notifyItemRemoved(0);
            return;
        }else {
            mNotFoundLayout.setVisibility(View.GONE);
            mNestedScrollView.setVisibility(View.VISIBLE);
        }

        for (Movie movieBrief : historyMovieBriefs) {
            mListHistoryMovie.add(movieBrief);
        }
        mAdapterHistoryMovie.notifyDataSetChanged();
    }

    private void loadHistoryWebSeries() {
        List<WebSeries> historyWebSeriesBriefs = SQLiteDB.getRecentWebseriesBriefs(getActivity());
        if (historyWebSeriesBriefs.isEmpty()) {
            mNotFoundLayout.setVisibility(View.VISIBLE);
            mNestedScrollView.setVisibility(View.GONE);
            //mFavMoviesAdapter.notifyItemRemoved(0);
            return;
        }else {
            mNotFoundLayout.setVisibility(View.GONE);
            mNestedScrollView.setVisibility(View.VISIBLE);
        }

        for (WebSeries webSeriesBrief : historyWebSeriesBriefs) {
            mListHistoryWebSeries.add(webSeriesBrief);
        }
        mAdapterHistoryWebSeries.notifyDataSetChanged();
    }
}