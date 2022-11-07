package com.digital.moviesbuddypro.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.digital.moviesbuddypro.CONFIG;
import com.digital.moviesbuddypro.adapter.AdapterTrendingSearch;
import com.digital.moviesbuddypro.model.Category;
import com.digital.moviesbuddypro.utils.CallBack;
import com.digital.moviesbuddypro.utils.RequestAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.movies.flowlayoutmanager.Alignment;
import com.movies.flowlayoutmanager.FlowLayoutManager;
import com.digital.moviesbuddypro.AdapterRecentSearch;
import com.digital.moviesbuddypro.InternetConnection;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.adapter.AdapterCategory;
import com.digital.moviesbuddypro.fragment.FragmentSearchMovies;
import com.digital.moviesbuddypro.fragment.FragmentSearchWebSeries;
import com.digital.moviesbuddypro.model.LatestItemSlider;
import com.digital.moviesbuddypro.sqlitedatabase.SQLiteDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivitySearch extends AppCompatActivity implements View.OnClickListener{

    private SearchView searchView;
    private ImageButton mBackButton, mVoiceButton;
    private static final int REQUEST_CODE_VOICE_TO_TEXT = 1;

    private LinearLayout mRecentSearchesLayout, mTrendingSearchesLayout;
    private TextView mClearAllButton;
    private RecyclerView mCategoryRecyclerView, mRecentSearchesRecyclerView, mTrendingSearchRecyclerView;

    private List<Category> mListCategory;
    private List<String> mListRecentSearches;
    private List<LatestItemSlider> mListTrendingSearches;

    private AdapterCategory mAdapterCategory;
    private AdapterRecentSearch mAdapterRecentSearch;
    private AdapterTrendingSearch mAdapterTrendingSearch;

    private FlowLayoutManager mFlowLayoutManagerRecentSearches;
    private FlowLayoutManager mFlowLayoutManagerTrendingSearches;

    private final RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(10, 10, 10, 10);
        }
    };

    private NestedScrollView mNestedScrollView;
    private View mDivider;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private AdapterViewPager mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViews();
        setUpViews();
    }

    private void findViews(){
        mBackButton = (ImageButton) findViewById(R.id.backButton);
        searchView = (SearchView) findViewById(R.id.searchView);
        mVoiceButton = (ImageButton) findViewById(R.id.voiceButton);

        mRecentSearchesLayout = (LinearLayout) findViewById(R.id.recentSearchesLayout);
        mTrendingSearchesLayout = (LinearLayout) findViewById(R.id.trendingSearchesLayout);

        mClearAllButton = (TextView) findViewById(R.id.clearAllButton);

        mCategoryRecyclerView = findViewById(R.id.categoryRecyclerview);
        mRecentSearchesRecyclerView = findViewById(R.id.recentSearchesRecyclerView);
        mTrendingSearchRecyclerView = findViewById(R.id.trendingSearchesRecyclerView);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mNestedScrollView = findViewById(R.id.nestedScrollView);
        mDivider = findViewById(R.id.divider);

        mListCategory = new ArrayList<>();
        /*mListRecentSearches = new ArrayList<>();*/
        mListTrendingSearches = new ArrayList<>();

        mAdapterCategory = new AdapterCategory(ActivitySearch.this, mListCategory);
        mAdapterTrendingSearch = new AdapterTrendingSearch(ActivitySearch.this, mListTrendingSearches);

        mFlowLayoutManagerTrendingSearches = new FlowLayoutManager().setAlignment(Alignment.LEFT);
        mFlowLayoutManagerTrendingSearches.setAutoMeasureEnabled(true);

        mFlowLayoutManagerRecentSearches = new FlowLayoutManager().setAlignment(Alignment.LEFT);
        mFlowLayoutManagerRecentSearches.setAutoMeasureEnabled(true);
    }

    private void setUpViews() {

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) searchView.findViewById(id);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"righteous.ttf");
        searchText.setTypeface(myCustomFont);
        searchText.setHintTextColor(getResources().getColor(R.color.primary_icon_color));
        searchText.setTextColor(getResources().getColor(R.color.secondary_icon_color));
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            searchText.setTextCursorDrawable(ContextCompat.getDrawable(ActivitySearch.this, R.drawable.cursor));
        }*/

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.equals("")) {

                } else {
                    SQLiteDB.addSearchRecent(ActivitySearch.this, query);

                    searchItem(query);
                    updateRecentSearches(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        getTrendingSearchesData();

        mListCategory.add(new Category("Bollywood", R.drawable.bollywood_poster));
        mListCategory.add(new Category("Web Series", R.drawable.webseries_poster));
        mListCategory.add(new Category("Hollywood", R.drawable.hollywood_poster));
        mListCategory.add(new Category("Tollywood", R.drawable.tollywood_poster));
        mListCategory.add(new Category("Animated", R.drawable.animated_poster));

        mCategoryRecyclerView.setHasFixedSize(true);
        mCategoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(ActivitySearch.this, LinearLayoutManager.HORIZONTAL, false));
        mCategoryRecyclerView.setAdapter(mAdapterCategory);
        mCategoryRecyclerView.setNestedScrollingEnabled(false);

        getRecentSearches();

        mBackButton.setOnClickListener(this);
        mVoiceButton.setOnClickListener(this);
        mClearAllButton.setOnClickListener(this);
    }

    private void updateRecentSearches(String mSearchableString){
        if (mAdapterRecentSearch != null && mListRecentSearches != null){
            if (mListRecentSearches.contains(mSearchableString)){
                mListRecentSearches.remove(mSearchableString);
            }
            if (mListRecentSearches.size() == 11){
                mListRecentSearches.remove(10);
                mListRecentSearches.add(0, mSearchableString);
            }else {
                mListRecentSearches.add(0, mSearchableString);
            }
            mAdapterRecentSearch.notifyDataSetChanged();

            if (mRecentSearchesLayout.getVisibility() == View.GONE){
                mRecentSearchesLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getRecentSearches(){
        mListRecentSearches = SQLiteDB.getSearchRecentBriefs(ActivitySearch.this);
        mAdapterRecentSearch = new AdapterRecentSearch(ActivitySearch.this, mListRecentSearches, new AdapterRecentSearch.OnItemClickListener() {
            @Override
            public void onItemClick(int positon) {
                searchItem(mListRecentSearches.get(positon));
                updateRecentSearches(mListRecentSearches.get(positon));
            }
        });

        if (mListRecentSearches.size() > 0){
            mRecentSearchesLayout.setVisibility(View.VISIBLE);
        }else {
            mRecentSearchesLayout.setVisibility(View.GONE);
        }

        mRecentSearchesRecyclerView.setHasFixedSize(true);
        mRecentSearchesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecentSearchesRecyclerView.setLayoutManager(mFlowLayoutManagerRecentSearches);
        mRecentSearchesRecyclerView.setAdapter(mAdapterRecentSearch);
        mRecentSearchesRecyclerView.addItemDecoration(itemDecoration);
        mRecentSearchesRecyclerView.setNestedScrollingEnabled(false);
    }

    private void getTrendingSearchesData(){
        if (InternetConnection.isNetworkConnected(ActivitySearch.this)) {
            JSONObject parameters = new JSONObject();

            try {
                parameters.put("uid", CONFIG.sharedPreferences.getString(CONFIG.uid, "uid"));

                Log.e("Parameter:", parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestAPI.Call_Api(ActivitySearch.this, CONFIG.GET_LATEST_ITEMS, parameters, new CallBack() {
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

                                mListTrendingSearches.add(latestItemSlider);
                            }

                            if (mListTrendingSearches.size() > 0){
                                mTrendingSearchesLayout.setVisibility(View.VISIBLE);
                            }else {
                                mTrendingSearchesLayout.setVisibility(View.GONE);
                            }

                            mTrendingSearchRecyclerView.setHasFixedSize(true);
                            mTrendingSearchRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            mTrendingSearchRecyclerView.setLayoutManager(mFlowLayoutManagerTrendingSearches);
                            mTrendingSearchRecyclerView.setAdapter(mAdapterTrendingSearch);
                            mTrendingSearchRecyclerView.addItemDecoration(itemDecoration);
                            mTrendingSearchRecyclerView.setNestedScrollingEnabled(false);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(ActivitySearch.this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchItem(String mSearchableString) {

        if (mSearchableString.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Search Any Keyword!", Toast.LENGTH_SHORT).show();
        }else {
            /*String searchableString;
            if (getFirstWord(searchText.toLowerCase()).equals("the")) {
                searchableString = getSecondWord(searchText.toLowerCase());
            } else {
                searchableString = getFirstWord(searchText.toLowerCase());
            }*/

            mNestedScrollView.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            mDivider.setVisibility(View.VISIBLE);

            mAdapter = new AdapterViewPager(getSupportFragmentManager(), mSearchableString.toLowerCase());
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);

            Toast.makeText(getApplicationContext(), mSearchableString, Toast.LENGTH_SHORT).show();
            //mTabLayout.setVisibility(View.VISIBLE);
        }
    }

    /* When Mic activity close */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_VOICE_TO_TEXT: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    String yourResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                    //Toast.makeText(this, ""+yourResult, Toast.LENGTH_SHORT).show();
                    searchView.setQuery(yourResult, true);
                    //searchItem(yourResult.toLowerCase());
                }
                break;
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int mId = v.getId();
        switch (mId){
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.clearAllButton:
                SQLiteDB.clearAllSearchRecent(ActivitySearch.this);

                mListRecentSearches.clear();
                mRecentSearchesLayout.setVisibility(View.GONE);
                break;
            case R.id.voiceButton:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, REQUEST_CODE_VOICE_TO_TEXT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(ActivitySearch.this, "Oops! Your device doesn't support Speech to Text",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class AdapterViewPager extends FragmentStatePagerAdapter {

        private String mSearchableString;

        String[] titleArray = {"Movies", "Web Series"};

        AdapterViewPager(FragmentManager fragmentManager, String mSearchableString) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mSearchableString = mSearchableString;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return FragmentSearchMovies.newInstance(mSearchableString, "SearchByTitle");
            }else {
                return FragmentSearchWebSeries.newInstance(mSearchableString, "SearchByTitle");
            }
        }

        @Override
        public int getCount() {
            return titleArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleArray[position];
        }
    }

    @Override
    public void onBackPressed() {
        if (mNestedScrollView.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        }else {
            mNestedScrollView.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.GONE);
            mDivider.setVisibility(View.GONE);
        }
    }
}
