package com.digital.moviesbuddypro.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.digital.moviesbuddypro.R;

public class ActivityWebView extends AppCompatActivity {

    private Bundle mBundle;
    private Toolbar mToolBar;
    private WebView mWebView;

    private String mLink, mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        findViews();
        setViews();
    }

    private void findViews(){
        mBundle = getIntent().getExtras();
        if (mBundle != null){
            mLink = mBundle.getString("WEBVIEW_LINK_KEY");
            mType = mBundle.getString("TYPE_KEY");
        }

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mWebView = (WebView) findViewById(R.id.webView);
    }

    private void setViews(){
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolBar.setTitle(mType);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        mWebView.setWebViewClient(new MyBrowser());

        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.loadUrl(mLink);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
