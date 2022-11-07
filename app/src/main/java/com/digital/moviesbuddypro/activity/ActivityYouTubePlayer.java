package com.digital.moviesbuddypro.activity;

import android.os.Bundle;
import android.view.View;

import com.digital.moviesbuddypro.CONFIG;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.digital.moviesbuddypro.R;

public class ActivityYouTubePlayer extends YouTubeBaseActivity {

    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    private String mYTVideoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtubeplayer);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            mYTVideoID = bundle.getString("YT_VIDEO_KEY");

            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            mYouTubePlayerView = (YouTubePlayerView)  findViewById(R.id.youtubePlayer);

            mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.loadVideo(mYTVideoID);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            };

            mYouTubePlayerView.initialize(CONFIG.YOUTUBE_API_KEY, mOnInitializedListener);
        }
    }
}