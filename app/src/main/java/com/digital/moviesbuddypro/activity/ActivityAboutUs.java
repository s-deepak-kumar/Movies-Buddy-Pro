package com.digital.moviesbuddypro.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.digital.moviesbuddypro.CONFIG;
import com.digital.moviesbuddypro.R;

public class ActivityAboutUs extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mVisitWebsiteButton, mDMCAButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        findViews();
        setViews();
    }

    private void findViews(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mVisitWebsiteButton = (TextView) findViewById(R.id.visitWebsiteButton);
        mDMCAButton = (TextView) findViewById(R.id.dmcaButton);
    }

    private void setViews(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        //Click Listener
        mVisitWebsiteButton.setOnClickListener(this);
        mDMCAButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int mId = v.getId();
        switch (mId){
            case R.id.visitWebsiteButton:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/sundardeepakkumar/")));
                break;
            case R.id.dmcaButton:
                Intent intent = new Intent(ActivityAboutUs.this, ActivityWebView.class);
                intent.putExtra("WEBVIEW_LINK_KEY", CONFIG.dmca_url);
                intent.putExtra("TYPE_KEY", "DMCA");
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
