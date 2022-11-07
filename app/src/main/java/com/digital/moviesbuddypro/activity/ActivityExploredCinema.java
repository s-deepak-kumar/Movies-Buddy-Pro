package com.digital.moviesbuddypro.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.fragment.FragmentSearchMovies;
import com.digital.moviesbuddypro.fragment.FragmentSearchWebSeries;

public class ActivityExploredCinema extends AppCompatActivity{

    private Bundle mBundle;
    private Toolbar mToolbar;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private AdapterViewPager mAdapterViewPager;
    private String mSearchableString, mTypeString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explored_cinema);

        findViews();
        setUpViews();
    }

    private void findViews(){
        mToolbar = findViewById(R.id.toolbar);

        mBundle = getIntent().getExtras();
        if (mBundle != null){
            mSearchableString = mBundle.getString("SEARCHABLE_STRING_KEY");
            mTypeString = mBundle.getString("TYPE_KEY");
        }
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
    }

    private void setUpViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitle(mSearchableString);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        mAdapterViewPager = new AdapterViewPager(getSupportFragmentManager(), mSearchableString.toLowerCase());
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mAdapterViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}