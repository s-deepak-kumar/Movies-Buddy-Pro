package com.digital.moviesbuddypro.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.moviesbuddypro.CONFIG;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.digital.moviesbuddypro.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivitySplash extends AppCompatActivity {

    public static String mToken;
    private CountDownTimer countDownTimer;

    private static final String TAG = ActivitySplash.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CONFIG.sharedPreferences = getSharedPreferences(CONFIG.pref_name, MODE_PRIVATE);
        mToken = CONFIG.sharedPreferences.getString(CONFIG.token,"null");

        boolean isPushNotificationReceive = CONFIG.sharedPreferences.getBoolean(CONFIG.pushNotification, false);

        if (isPushNotificationReceive) {
            FirebaseMessaging.getInstance().subscribeToTopic("alerts")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Subscribe Successfully!";
                            if (!task.isSuccessful()) {
                                msg = "Subscribe Failed!";
                            }
                            Log.d(TAG, msg);
                            //Toast.makeText(ActivitySplash.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("alerts")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Unsubscribe Successfully!";
                            if (!task.isSuccessful()) {
                                msg = "Unsubscribe Failed!";
                            }
                            Log.d(TAG, msg);
                            //Toast.makeText(ActivitySplash.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        if(mToken == null || (mToken.equals("") || mToken.equals("null"))){
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }

                            // Get new FCM registration token
                            String token = task.getResult();
                            addToken(token);

                            // Log and toast
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d(CONFIG.TAG, msg);
                            //Toast.makeText(ActivitySplash.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!CONFIG.sharedPreferences.getBoolean(CONFIG.showOnboardingScreen, false)) {
                    startActivity(new Intent(ActivitySplash.this, ActivityOnboarding.class));
                }else {
                    startActivity(new Intent(ActivitySplash.this, ActivityMain.class));
                    finish();
                }
            }
        }.start();

    }

    @SuppressLint("HardwareIds")
    private void addToken(String mToken){
        final String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        final String deviceName = android.os.Build.MODEL;
        final String deviceVersion = ""+android.os.Build.VERSION.SDK_INT;
        SharedPreferences.Editor editor =  CONFIG.sharedPreferences.edit();
        editor.putString(CONFIG.androidId, androidId);
        editor.putString(CONFIG.deviceName, deviceName);
        editor.putString(CONFIG.deviceVersion, deviceVersion);
        editor.putString(CONFIG.token, mToken);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (countDownTimer != null){
            countDownTimer.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (countDownTimer != null){
            countDownTimer.start();
        }
    }
}
