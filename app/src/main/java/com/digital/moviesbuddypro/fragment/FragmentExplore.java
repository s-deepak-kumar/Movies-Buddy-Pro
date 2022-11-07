package com.digital.moviesbuddypro.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.moviesbuddypro.activity.ActivityAllCasts;
import com.digital.moviesbuddypro.activity.ActivityAllCategory;
import com.digital.moviesbuddypro.activity.ActivityCategoryWebseries;
import com.digital.moviesbuddypro.activity.ActivitySearch;
import com.digital.moviesbuddypro.adapter.AdapterText;
import com.digital.moviesbuddypro.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentExplore extends Fragment implements View.OnClickListener {

    private NestedScrollView mNestedScrollView;

    private LinearLayout mSearchBarButton;
    private TextView mFilterByButton;

    private CardView mBollywoodCardButton, mHollywoodCardButton, mTollywoodCardButton, mAnimatedCardButton, mWebSeriesCardButton,
            mPersonCardButton;

    private RecyclerView mRecyclerViewOriginalNetwork, mRecyclerViewYear, mRecyclerViewGenre;
    private AdapterText mAdapterTextOriginalNetwork, mAdapterTextYear, mAdapterTextGenre;
    private List<String> mListOrigonalNetworkString, mListYearString, mListGenreString;

    private LinearLayout mLayoutOriginalNetwork, mLayoutYear, mLayoutGenre;

    private final String[] mOriginalNetworkArray= {"Netflix", "Prime Video", "HBO", "AMC", "Apple TV+", "Disney+", "FOX", "FX",
            "NBC", "CBS", "History", "The CW", "ESPN", "Lifetime", "Showtime", "VH1", "Bravo", "Comedy Central",
            "National Geographic Channel", "BBC America", "ABC", "Nickelodeon", "PBS", "A & E", "Animal Planet",
            "Adult Swim", "Hulu"};

    private final String[] mYearStringArray = {"2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012",
            "2011", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998",
            "1997", "1996", "1995", "1994", "1993", "1992", "1991", "1990", "1989"};

    private final String[] mGenreStringArray = {"Action", "Adventure", "Animation", "Comedy", "Crime", "Documentry", "Drama",
            "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western",
            "Kids", "News", "Reality"};

    private GridLayoutManager mLinearLayoutManagerOriginalNetwork, mLinearLayoutManagerYear, mLinearLayoutManagerGenre;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentExplore() {
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
    public static FragmentExplore newInstance(String param1, String param2) {
        FragmentExplore fragment = new FragmentExplore();
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
        View v = inflater.inflate(R.layout.fragment_explore, container, false);

        findViews(v);
        setUpViews();

        return v;
    }

    private void findViews(View v) {
        mNestedScrollView = v.findViewById(R.id.nestedScrollView);
        mSearchBarButton = v.findViewById(R.id.searchBarButton);
        mFilterByButton = v.findViewById(R.id.filterByButton);

        mBollywoodCardButton = v.findViewById(R.id.bollywoodCardView);
        mHollywoodCardButton = v.findViewById(R.id.hollywoodCardView);
        mTollywoodCardButton = v.findViewById(R.id.tollywoodCardView);
        mAnimatedCardButton = v.findViewById(R.id.animatedCardView);
        mWebSeriesCardButton = v.findViewById(R.id.webseriesCardView);
        mPersonCardButton = v.findViewById(R.id.personCardView);

        mLayoutOriginalNetwork = v.findViewById(R.id.originalNetworkLayout);
        mLayoutYear = v.findViewById(R.id.yearLayout);
        mLayoutGenre = v.findViewById(R.id.genreLayout);

        mRecyclerViewOriginalNetwork = v.findViewById(R.id.originalNetworkRecyclerView);
        mRecyclerViewYear = v.findViewById(R.id.yearRecyclerView);
        mRecyclerViewGenre = v.findViewById(R.id.genreRecyclerView);

        mListOrigonalNetworkString = new ArrayList<>();
        mListYearString = new ArrayList<>();
        mListGenreString = new ArrayList<>();

        mAdapterTextOriginalNetwork = new AdapterText(getActivity(), mListOrigonalNetworkString, "Original Network", R.layout.item_original_network);
        mAdapterTextYear = new AdapterText(getActivity(), mListYearString, "Year", R.layout.item_year);
        mAdapterTextGenre = new AdapterText(getActivity(), mListGenreString, "Genre", R.layout.item_genre);

        mLinearLayoutManagerOriginalNetwork = new GridLayoutManager(getActivity(), 3);
        mLinearLayoutManagerYear = new GridLayoutManager(getActivity(), 3);
        mLinearLayoutManagerGenre = new GridLayoutManager(getActivity(), 3);
    }

    private void setUpViews() {

        loadOriginalNetwork();
        loadYear();
        loadGenre();

        mSearchBarButton.setOnClickListener(this);
        mFilterByButton.setOnClickListener(this);

        mBollywoodCardButton.setOnClickListener(this);
        mHollywoodCardButton.setOnClickListener(this);
        mTollywoodCardButton.setOnClickListener(this);
        mAnimatedCardButton.setOnClickListener(this);
        mWebSeriesCardButton.setOnClickListener(this);
        mPersonCardButton.setOnClickListener(this);
    }

    private void loadOriginalNetwork(){
        mListOrigonalNetworkString.addAll(Arrays.asList(mOriginalNetworkArray));

        if (mListOrigonalNetworkString.size() > 0){
            mLayoutOriginalNetwork.setVisibility(View.VISIBLE);
        }else {
            mLayoutOriginalNetwork.setVisibility(View.GONE);
        }

        mRecyclerViewOriginalNetwork.setHasFixedSize(true);
        mRecyclerViewOriginalNetwork.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewOriginalNetwork.setLayoutManager(mLinearLayoutManagerOriginalNetwork);
        mRecyclerViewOriginalNetwork.setAdapter(mAdapterTextOriginalNetwork);
        mRecyclerViewOriginalNetwork.setNestedScrollingEnabled(false);
    }

    private void loadYear(){
        mListYearString.addAll(Arrays.asList(mYearStringArray));

        if (mListYearString.size() > 0){
            mLayoutYear.setVisibility(View.VISIBLE);
        }else {
            mLayoutYear.setVisibility(View.GONE);
        }

        mRecyclerViewYear.setHasFixedSize(true);
        mRecyclerViewYear.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewYear.setLayoutManager(mLinearLayoutManagerYear);
        mRecyclerViewYear.setAdapter(mAdapterTextYear);
        mRecyclerViewYear.setNestedScrollingEnabled(false);
    }

    private void loadGenre(){
        mListGenreString.addAll(Arrays.asList(mGenreStringArray));

        if (mListGenreString.size() > 0){
            mLayoutGenre.setVisibility(View.VISIBLE);
        }else {
            mLayoutGenre.setVisibility(View.GONE);
        }

        mRecyclerViewGenre.setHasFixedSize(true);
        mRecyclerViewGenre.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewGenre.setLayoutManager(mLinearLayoutManagerGenre);
        mRecyclerViewGenre.setAdapter(mAdapterTextGenre);
        mRecyclerViewGenre.setNestedScrollingEnabled(false);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int mId = v.getId();
        Intent intent = new Intent(getActivity(), ActivityAllCategory.class);
        switch (mId){
            case R.id.searchBarButton:
                startActivity(new Intent(getActivity(), ActivitySearch.class));
                break;
            case R.id.filterByButton:
                Toast.makeText(getActivity(), "Try After Sometime!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bollywoodCardView:
                intent.putExtra("CATEGORY_KEY", "Bollywood");
                intent.putExtra("CATEGORY_BANNER_KEY", R.mipmap.bollywood_poster_landscape);
                startActivity(intent);
                break;
            case R.id.hollywoodCardView:
                intent.putExtra("CATEGORY_KEY", "Hollywood");
                intent.putExtra("CATEGORY_BANNER_KEY", R.mipmap.hollywood_poster_landscape);
                startActivity(intent);
                break;
            case R.id.tollywoodCardView:
                intent.putExtra("CATEGORY_KEY", "Tollywood");
                intent.putExtra("CATEGORY_BANNER_KEY", R.mipmap.tollywood_poster_landscape);
                startActivity(intent);
                break;
            case R.id.animatedCardView:
                intent.putExtra("CATEGORY_KEY", "Animated");
                intent.putExtra("CATEGORY_BANNER_KEY", R.mipmap.animated_poster_landscape);
                startActivity(intent);
                break;
            case R.id.webseriesCardView:
                startActivity(new Intent(getActivity(), ActivityCategoryWebseries.class));
                break;
            case R.id.personCardView:
                intent = new Intent(getActivity(), ActivityAllCasts.class);
                intent.putExtra("CATEGORY_KEY", "Cast");
                intent.putExtra("CATEGORY_BANNER_KEY", R.drawable.person_poster);
                startActivity(intent);
                break;
        }
    }
}
