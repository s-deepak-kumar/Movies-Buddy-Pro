package com.digital.moviesbuddypro;

import android.widget.Toast;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

public class Application extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(Application.this);
    }
}
