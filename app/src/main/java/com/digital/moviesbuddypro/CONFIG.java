package com.digital.moviesbuddypro;

import android.content.SharedPreferences;

public class CONFIG {

    public static String TAG = "Movies Buddy Pro: ";

    public static final boolean is_secure_info = false;

    public static final String privacy_policy_url = "file:///android_asset/privacy_policy.html";
    public static final String terms_of_usage_url = "file:///android_asset/terms_of_usage.html";
    public static final String dmca_url = "file:///android_asset/dmca.html";

    public static SharedPreferences sharedPreferences;
    public static final String pref_name = "pref_name";
    public static final String uid ="uid";
    public static final String name = "name";
    public static final String email = "email";
    public static final String profile_pic_url_local = "profile_pic_url_local";
    public static final String token = "token";

    //Device
    public static final String androidId = "android_id";
    public static final String deviceName = "device_name";
    public static final String deviceVersion = "device_version";

    public static final String islogin = "is_login";
    public static final String showOnboardingScreen = "show_onboarding_screen";

    public static final String pushNotification = "push_notification";

    public static final String YOUTUBE_API_KEY = "AIzaSyBizl70COg1Tj00ErtwbIZ5qQcE2MTgMCc";

    public static final  String main_domain="http://192.168.1.9/MoviesBuddyProSingleStoreDB/";
    public static final String base_url=main_domain;
    public static final String api_domain = base_url+"API.php?p=";

    public static final String GET_LATEST_ITEMS = api_domain + "getLatestItems";
    public static final String GET_MOVIES = api_domain + "getMovies";
    public static final String GET_SEARCHED_MOVIES = api_domain + "getSearchedMovies";
    public static final String GET_MOVIE_BY_ID = api_domain + "getMovieByID";
    public static final String GET_CASTS = api_domain + "getCasts";
    public static final String GET_WEBSERIES = api_domain + "getWebseries";
    public static final String GET_SEARCHED_WEBSERIES = api_domain + "getSearchedWebseries";
    public static final String GET_WEBSERIES_BY_ID = api_domain + "getWebseriesByID";
    public static final String ADD_MOVIE = api_domain + "addWebseries";

    public static void resetSharedPreferencesData(){
        SharedPreferences.Editor editor = CONFIG.sharedPreferences.edit();
        editor.putString(CONFIG.profile_pic_url_local, "");
        editor.putString(CONFIG.uid, "");
        editor.putString(CONFIG.name, "");
        editor.putString(CONFIG.email, "");
        editor.putString(CONFIG.token, "");
        editor.putBoolean(CONFIG.islogin, false);
        editor.apply();
    }

    public static void setSharedPreferencesData(String mUid, String mName, String mEmail, String mProfilePicUrl){
        SharedPreferences.Editor editor = CONFIG.sharedPreferences.edit();
        editor.putString(CONFIG.profile_pic_url_local, mProfilePicUrl);
        editor.putString(CONFIG.uid, mUid);
        editor.putString(CONFIG.name, mName);
        editor.putString(CONFIG.email, mEmail);
        editor.putBoolean(CONFIG.islogin, true);
        editor.apply();
    }
}
