package com.digital.moviesbuddypro.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.digital.moviesbuddypro.CONFIG;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.digital.moviesbuddypro.R;
import com.digital.moviesbuddypro.adapter.AdapterOnboarding;
import com.digital.moviesbuddypro.model.Onboarding;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class ActivityOnboarding extends AppCompatActivity implements View.OnClickListener{

    private DotsIndicator mPagerDotIndicator;

    private ViewPager mViewPager;
    private AdapterOnboarding mAdapterOnboarding;
    private TextView mGetStartedButton;
    private ProgressBar mProgressBar;

    private List<Onboarding> mListOnboarding;

    private FirebaseAuth mAuth;
    private static final String TAG = ActivityOnboarding.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        findViews();
        setUpViews();
    }

    private void findViews() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mPagerDotIndicator = (DotsIndicator) findViewById(R.id.pagerDotsIndicator);
        mViewPager = (ViewPager) findViewById(R.id.onboardingViewpager);
        mGetStartedButton = (TextView) findViewById(R.id.getStartedButton);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mListOnboarding = new ArrayList<>();
        mAdapterOnboarding = new AdapterOnboarding(ActivityOnboarding.this, mListOnboarding);
    }

    private void setUpViews() {
        loadData();

        mViewPager.setAdapter(mAdapterOnboarding);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPagerDotIndicator.setViewPager(mViewPager);

        mGetStartedButton.setOnClickListener(this);
    }

    // Load data into the viewpager
    public void loadData() {
        int[] titleArray = {R.string.onboarding_title_1, R.string.onboarding_title_2, R.string.onboarding_title_3};
        int[] descriptionArray = {R.string.onboarding_description_1, R.string.onboarding_description_2, R.string.onboarding_description_3};
        int[] imageArray = {R.mipmap.onboarding_screen_poster_1, R.mipmap.onboarding_screen_poster_2, R.mipmap.onboarding_screen_poster_3};

        for(int i=0; i<imageArray.length; i++) {
            Onboarding itemOnboarding = new Onboarding();
            itemOnboarding.setImageid(imageArray[i]);
            itemOnboarding.setTitle(getResources().getString(titleArray[i]));
            itemOnboarding.setDescription(getResources().getString(descriptionArray[i]));

            mListOnboarding.add(itemOnboarding);
        }
    }

    public void setOnboardingScreenShow(boolean showOnboardingScreen) {
        SharedPreferences.Editor editor =  CONFIG.sharedPreferences.edit();
        editor.putBoolean(CONFIG.showOnboardingScreen, showOnboardingScreen);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int mId = v.getId();
        if (mId == R.id.getStartedButton) {
            mGetStartedButton.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");

                                startActivity(new Intent(ActivityOnboarding.this, ActivityMain.class));
                                finish();
                                setOnboardingScreenShow(true);
                                saveNotificationPref();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
                                Toast.makeText(ActivityOnboarding.this, "Authentication failed, please try again later!", Toast.LENGTH_SHORT).show();
                                mGetStartedButton.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
            } else {
                startActivity(new Intent(ActivityOnboarding.this, ActivityMain.class));
                finish();
            }
        }
    }

    private void saveNotificationPref(){
        SharedPreferences.Editor editor =  CONFIG.sharedPreferences.edit();
        editor.putBoolean(CONFIG.pushNotification, true);
        editor.apply();
    }
}
