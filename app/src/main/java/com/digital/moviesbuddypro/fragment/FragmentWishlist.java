package com.digital.moviesbuddypro.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.digital.moviesbuddypro.R;

public class FragmentWishlist extends Fragment implements View.OnClickListener{

    private AppBarLayout mAppBarLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private AdapterViewPager mAdapter;

    private TextView mWishlistTextTabButton, mHistoryTextTabButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mType;
    private String mParam2;

    public FragmentWishlist() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mType Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentWishlist newInstance(String mType, String param2) {
        FragmentWishlist fragment = new FragmentWishlist();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, mType);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wishlist, container, false);

        findViews(v);
        setUpViews();

        return v;
    }

    private void findViews(View v) {
        mAppBarLayout = v.findViewById(R.id.appbar);
        mTabLayout = v.findViewById(R.id.tabLayout);
        mViewPager = v.findViewById(R.id.viewPager);

        mWishlistTextTabButton = v.findViewById(R.id.wishlistTabButton);
        mHistoryTextTabButton = v.findViewById(R.id.historyTabButton);
    }

    private void setUpViews() {

        mWishlistTextTabButton.setOnClickListener(this);
        mHistoryTextTabButton.setOnClickListener(this);

        loadPageView();
    }

    private void loadPageView(){
        mAdapter = new AdapterViewPager(getActivity().getSupportFragmentManager(), mType);
        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int mId = v.getId();
        switch (mId){
            case R.id.wishlistTabButton:
                mWishlistTextTabButton.setTextColor(getResources().getColor(R.color.white));
                mHistoryTextTabButton.setTextColor(getResources().getColor(R.color.secondary_text_color));
                mType = "Wishlist";
                loadPageView();
                break;
            case R.id.historyTabButton:
                mHistoryTextTabButton.setTextColor(getResources().getColor(R.color.white));
                mWishlistTextTabButton.setTextColor(getResources().getColor(R.color.secondary_text_color));
                mType = "History";
                loadPageView();
                break;
        }
    }

    private class AdapterViewPager extends FragmentStatePagerAdapter {

        String[] tabArray = {"Movie", "Web Series"};
        String mType;

        AdapterViewPager(FragmentManager fragmentManager, String mType) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mType = mType;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (mType.equals("Wishlist")){
                if (position == 0) {
                    return new WishlistFragment("Movie");
                }else {
                    return new WishlistFragment("Web Series");
                }
            }else {
                if (position == 0) {
                    return new HistoryFragment("Movie");
                }else {
                    return new HistoryFragment("Web Series");
                }
            }
        }

        @Override
        public int getCount() {
            return tabArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabArray[position];
        }
    }

}
