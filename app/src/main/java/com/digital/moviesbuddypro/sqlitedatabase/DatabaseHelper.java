package com.digital.moviesbuddypro.sqlitedatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hitanshu on 9/8/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesbuddypro_again.db";
    public static final String FAV_MOVIE_TABLE_NAME = "movie_favourite";
    public static final String FAV_WEBSERIES_TABLE_NAME = "webseries_favourite";
    public static final String RECENT_MOVIE_TABLE_NAME = "movie_recent";
    public static final String RECENT_WEBSERIES_TABLE_NAME = "webseries_recent";
    public static final String RECENT_SEARCH = "search_recent";
    public static final String TRENDING_SEARCH = "search_trending";

    public static final String ID = "id";
    public static final String MOVIE_ID = "movie_id";
    public static final String POSTER = "poster";
    public static final String TITLE = "title";
    public static final String CATEGORY = "category";
    public static final String IMDB_RATING = "rating";
    public static final String CERTIFICATE = "certificate";

    public static final String WEBSERIES_ID = "webseries_id";
    public static final String SEASON_TAG = "season_tag";

    public static final String WORD = "word";
    public static final String ITEM_ID = "item_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryCreateMovieFavTable = "CREATE TABLE " + FAV_MOVIE_TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_ID + " TEXT, "
                + TITLE + " TEXT, "
                + POSTER + " TEXT, "
                + CATEGORY + " TEXT, "
                + IMDB_RATING + " TEXT, "
                + CERTIFICATE + " TEXT)";

        String queryCreateWebseriesFavTable = "CREATE TABLE " + FAV_WEBSERIES_TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WEBSERIES_ID + " TEXT, "
                + TITLE + " TEXT, "
                + POSTER + " TEXT, "
                + IMDB_RATING + " TEXT, "
                + SEASON_TAG + " TEXT, "
                + CERTIFICATE + " TEXT)";

        String queryCreateMovieRecentTable = "CREATE TABLE " + RECENT_MOVIE_TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_ID + " TEXT, "
                + TITLE + " TEXT, "
                + POSTER + " TEXT, "
                + CATEGORY + " TEXT, "
                + IMDB_RATING + " TEXT, "
                + CERTIFICATE + " TEXT)";

        String queryCreateWebseriesRecentTable = "CREATE TABLE " + RECENT_WEBSERIES_TABLE_NAME + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WEBSERIES_ID + " TEXT, "
                + TITLE + " TEXT, "
                + POSTER + " TEXT, "
                + IMDB_RATING + " TEXT, "
                + SEASON_TAG + " TEXT, "
                + CERTIFICATE + " TEXT)";

        String queryCreateSearchRecentTable = "CREATE TABLE " + RECENT_SEARCH + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WORD + " TEXT)";

        String queryCreateSearchTrendingTable = "CREATE TABLE " + TRENDING_SEARCH + " ( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEM_ID + " TEXT, "
                + TITLE + " TEXT, "
                + CATEGORY + " TEXT)";

        sqLiteDatabase.execSQL(queryCreateMovieFavTable);
        sqLiteDatabase.execSQL(queryCreateWebseriesFavTable);
        sqLiteDatabase.execSQL(queryCreateMovieRecentTable);
        sqLiteDatabase.execSQL(queryCreateWebseriesRecentTable);
        sqLiteDatabase.execSQL(queryCreateSearchRecentTable);
        sqLiteDatabase.execSQL(queryCreateSearchTrendingTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
