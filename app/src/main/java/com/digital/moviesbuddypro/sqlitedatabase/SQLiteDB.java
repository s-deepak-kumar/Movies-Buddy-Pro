package com.digital.moviesbuddypro.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.digital.moviesbuddypro.model.Item;
import com.digital.moviesbuddypro.model.Movie;
import com.digital.moviesbuddypro.model.WebSeries;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDB {

    //ADD MOVIE TO FAVOURIRTE
    public static void addMovieToFav(Context context, String movieId, String title, String poster, String category,
                                     String imdbRating, String certificate) {
        if (movieId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if (!isMovieFav(context, movieId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.MOVIE_ID, movieId);
            contentValues.put(DatabaseHelper.TITLE, title);
            contentValues.put(DatabaseHelper.POSTER, poster);
            contentValues.put(DatabaseHelper.CATEGORY, category);
            contentValues.put(DatabaseHelper.IMDB_RATING, imdbRating);
            contentValues.put(DatabaseHelper.CERTIFICATE, certificate);
            database.insert(DatabaseHelper.FAV_MOVIE_TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public static void removeMovieFromFav(Context context, String movieId) {
        if (movieId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isMovieFav(context, movieId)) {
            database.delete(DatabaseHelper.FAV_MOVIE_TABLE_NAME, DatabaseHelper.MOVIE_ID + " = ?", new String[] { movieId});
        }
        database.close();
    }

    public static boolean isMovieFav(Context context, String movieId) {
        if (movieId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isMovieFav;
        String isMovieFavQuery = "SELECT * FROM " + DatabaseHelper.FAV_MOVIE_TABLE_NAME + " WHERE " + DatabaseHelper.MOVIE_ID + " = ?";
        Cursor cursor = database.rawQuery(isMovieFavQuery, new String[] { movieId});
        if (cursor.getCount() == 1)
            isMovieFav = true;
        else
            isMovieFav = false;

        cursor.close();
        database.close();
        return isMovieFav;
    }

    public static List<Movie> getFavMovieBriefs(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<Movie> favMovie = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.FAV_MOVIE_TABLE_NAME, null, null, null, null, null, DatabaseHelper.ID + " DESC");
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            String movieId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_ID));
            String movieTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
            String moviePoster = cursor.getString(cursor.getColumnIndex(DatabaseHelper.POSTER));
            String movieCategory = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORY));
            String movieIMDBRating = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMDB_RATING));
            String movieCertificate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CERTIFICATE));

            movie.setId(movieId);
            movie.setTitle(movieTitle);
            movie.setPoster(moviePoster);
            movie.setCategory(movieCategory);
            movie.setIMDBRating(movieIMDBRating);
            movie.setCertificate(movieCertificate);

            favMovie.add(movie);
        }
        cursor.close();
        database.close();
        return favMovie;
    }

    //ADD MOVIE TO RECENT
    public static void addMovieToRecent(Context context, String movieId, String title, String poster, String category,
                                        String imdbRating, String certificate) {
        if (movieId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if (isMovieRecent(context, movieId)) {
            removeMovieFromRecent(context, movieId);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.MOVIE_ID, movieId);
            contentValues.put(DatabaseHelper.TITLE, title);
            contentValues.put(DatabaseHelper.POSTER, poster);
            contentValues.put(DatabaseHelper.CATEGORY, category);
            contentValues.put(DatabaseHelper.IMDB_RATING, imdbRating);
            contentValues.put(DatabaseHelper.CERTIFICATE, certificate);
            database.insert(DatabaseHelper.RECENT_MOVIE_TABLE_NAME, null, contentValues);
        }else {
            removeFirstRowFromMovieRecent(context);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.MOVIE_ID, movieId);
            contentValues.put(DatabaseHelper.TITLE, title);
            contentValues.put(DatabaseHelper.POSTER, poster);
            contentValues.put(DatabaseHelper.CATEGORY, category);
            contentValues.put(DatabaseHelper.IMDB_RATING, imdbRating);
            contentValues.put(DatabaseHelper.CERTIFICATE, certificate);
            database.insert(DatabaseHelper.RECENT_MOVIE_TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public static void removeMovieFromRecent(Context context, String movieId) {
        if (movieId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isMovieRecent(context, movieId)) {
            database.delete(DatabaseHelper.RECENT_MOVIE_TABLE_NAME, DatabaseHelper.MOVIE_ID + " = ?", new String[] { movieId});
        }
        database.close();
    }

    public static void removeFirstRowFromMovieRecent(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isMovieRecentRowMoreThan150(context)) {
            String deleteRecentMovieFirstRowQuery = "DELETE FROM " + DatabaseHelper.RECENT_MOVIE_TABLE_NAME + " WHERE " + DatabaseHelper.ID + " IN " +
                    "(SELECT " + DatabaseHelper.ID + " FROM " + DatabaseHelper.RECENT_MOVIE_TABLE_NAME +
                    " ORDER BY " + DatabaseHelper.ID +" ASC LIMIT 1)";
            database.execSQL(deleteRecentMovieFirstRowQuery);
        }

        database.close();
    }

    public static boolean isMovieRecentRowMoreThan150(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isMeaningRecentRowMoreThan150;
        long count = DatabaseUtils.queryNumEntries(database, DatabaseHelper.RECENT_MOVIE_TABLE_NAME);

        if (count > 150)
            isMeaningRecentRowMoreThan150 = true;
        else
            isMeaningRecentRowMoreThan150 = false;

        database.close();
        return isMeaningRecentRowMoreThan150;
    }

    public static boolean isMovieRecent(Context context, String movieId) {
        if (movieId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isMovieRecent;
        String isMovieFavQuery = "SELECT * FROM " + DatabaseHelper.RECENT_MOVIE_TABLE_NAME + " WHERE " + DatabaseHelper.MOVIE_ID + " = ?";
        Cursor cursor = database.rawQuery(isMovieFavQuery, new String[] { movieId});
        if (cursor.getCount() == 1)
            isMovieRecent = true;
        else
            isMovieRecent = false;

        cursor.close();
        database.close();
        return isMovieRecent;
    }

    public static List<Movie> getRecentMovieBriefs(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<Movie> recentMovie = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.RECENT_MOVIE_TABLE_NAME, null, null, null, null, null, DatabaseHelper.ID + " DESC");
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            String movieId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_ID));
            String movieTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
            String moviePoster = cursor.getString(cursor.getColumnIndex(DatabaseHelper.POSTER));
            String movieCategory = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORY));
            String movieIMDBRating = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMDB_RATING));
            String movieCertificate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CERTIFICATE));

            movie.setId(movieId);
            movie.setTitle(movieTitle);
            movie.setPoster(moviePoster);
            movie.setCategory(movieCategory);
            movie.setIMDBRating(movieIMDBRating);
            movie.setCertificate(movieCertificate);

            recentMovie.add(movie);
        }
        cursor.close();
        database.close();
        return recentMovie;
    }

    //ADD WEBSERIES TO FAVOURITE
    public static void addWebseriesToFav(Context context, String webseriesId, String title, String poster,
                                         String imdbRating, String seasonTag, String certificate) {
        if (webseriesId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if (!isWebseriesFav(context, webseriesId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.WEBSERIES_ID, webseriesId);
            contentValues.put(DatabaseHelper.TITLE, title);
            contentValues.put(DatabaseHelper.POSTER, poster);
            contentValues.put(DatabaseHelper.IMDB_RATING, imdbRating);
            contentValues.put(DatabaseHelper.SEASON_TAG, seasonTag);
            contentValues.put(DatabaseHelper.CERTIFICATE, certificate);
            database.insert(DatabaseHelper.FAV_WEBSERIES_TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public static void removeWebseriesFromFav(Context context, String webseriesId) {
        if (webseriesId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isWebseriesFav(context, webseriesId)) {
            database.delete(DatabaseHelper.FAV_WEBSERIES_TABLE_NAME, DatabaseHelper.WEBSERIES_ID + " = ?", new String[] { webseriesId});
        }
        database.close();
    }

    public static boolean isWebseriesFav(Context context, String webseriesId) {
        if (webseriesId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isWebseriesFav;
        String isWebseriesFavQuery = "SELECT * FROM " + DatabaseHelper.FAV_WEBSERIES_TABLE_NAME + " WHERE " + DatabaseHelper.WEBSERIES_ID + " = ?";
        Cursor cursor = database.rawQuery(isWebseriesFavQuery, new String[] { webseriesId});
        if (cursor.getCount() == 1)
            isWebseriesFav = true;
        else
            isWebseriesFav = false;

        cursor.close();
        database.close();
        return isWebseriesFav;
    }

    public static List<WebSeries> getFavWebseriesBriefs(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<WebSeries> favWebseries = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.FAV_WEBSERIES_TABLE_NAME, null, null, null, null, null, DatabaseHelper.ID + " DESC");
        while (cursor.moveToNext()) {
            WebSeries webSeries = new WebSeries();
            String webseriesId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.WEBSERIES_ID));
            String webseriesTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
            String webseriesPoster = cursor.getString(cursor.getColumnIndex(DatabaseHelper.POSTER));
            String webseriesIMDBRating = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMDB_RATING));
            String webseriesSeasonTag = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SEASON_TAG));
            String webseriesCertificate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CERTIFICATE));

            webSeries.setId(webseriesId);
            webSeries.setTitle(webseriesTitle);
            webSeries.setMainPoster(webseriesPoster);
            webSeries.setIMDBRating(webseriesIMDBRating);
            webSeries.setSeasonTag(webseriesSeasonTag);
            webSeries.setCertificate(webseriesCertificate);

            favWebseries.add(webSeries);
        }
        cursor.close();
        database.close();
        return favWebseries;
    }

    //ADD WEBSERIES TO RECENT
    public static void addWebseriesToRecent(Context context, String webseriesId, String title, String poster,
                                            String imdbRating, String seasonTag, String certificate) {
        if (webseriesId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if (isWebseriesRecent(context, webseriesId)) {
            removeWebseriesFromRecent(context, webseriesId);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.WEBSERIES_ID, webseriesId);
            contentValues.put(DatabaseHelper.TITLE, title);
            contentValues.put(DatabaseHelper.POSTER, poster);
            contentValues.put(DatabaseHelper.IMDB_RATING, imdbRating);
            contentValues.put(DatabaseHelper.SEASON_TAG, seasonTag);
            contentValues.put(DatabaseHelper.CERTIFICATE, certificate);
            database.insert(DatabaseHelper.RECENT_WEBSERIES_TABLE_NAME, null, contentValues);
        }else {
            removeFirstRowFromWebseriesRecent(context);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.WEBSERIES_ID, webseriesId);
            contentValues.put(DatabaseHelper.TITLE, title);
            contentValues.put(DatabaseHelper.POSTER, poster);
            contentValues.put(DatabaseHelper.IMDB_RATING, imdbRating);
            contentValues.put(DatabaseHelper.SEASON_TAG, seasonTag);
            contentValues.put(DatabaseHelper.CERTIFICATE, certificate);
            database.insert(DatabaseHelper.RECENT_WEBSERIES_TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public static void removeWebseriesFromRecent(Context context, String webseriesId) {
        if (webseriesId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isWebseriesRecent(context, webseriesId)) {
            database.delete(DatabaseHelper.RECENT_WEBSERIES_TABLE_NAME, DatabaseHelper.WEBSERIES_ID+ " = ?", new String[] { webseriesId});
        }
        database.close();
    }

    public static void removeFirstRowFromWebseriesRecent(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isWebseriesRecentRowMoreThan150(context)) {
            String deleteRecentWebseriesFirstRowQuery = "DELETE FROM " + DatabaseHelper.RECENT_WEBSERIES_TABLE_NAME + " WHERE " + DatabaseHelper.ID + " IN " +
                    "(SELECT " + DatabaseHelper.ID + " FROM " + DatabaseHelper.RECENT_WEBSERIES_TABLE_NAME +
                    " ORDER BY " + DatabaseHelper.ID +" ASC LIMIT 1)";
            database.execSQL(deleteRecentWebseriesFirstRowQuery);
        }
        database.close();
    }

    public static boolean isWebseriesRecentRowMoreThan150(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isWebseriesRecentRowMoreThan150;
        long count = DatabaseUtils.queryNumEntries(database, DatabaseHelper.RECENT_WEBSERIES_TABLE_NAME);

        if (count > 150)
            isWebseriesRecentRowMoreThan150 = true;
        else
            isWebseriesRecentRowMoreThan150 = false;

        database.close();
        return isWebseriesRecentRowMoreThan150;
    }

    public static boolean isWebseriesRecent(Context context, String webseriesId) {
        if (webseriesId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isWebseriesRecent;
        String isWebseriesRecentQuery = "SELECT * FROM " + DatabaseHelper.RECENT_WEBSERIES_TABLE_NAME + " WHERE " + DatabaseHelper.WEBSERIES_ID + " = ?";
        Cursor cursor = database.rawQuery(isWebseriesRecentQuery, new String[] { webseriesId});
        if (cursor.getCount() == 1)
            isWebseriesRecent = true;
        else
            isWebseriesRecent = false;

        cursor.close();
        database.close();
        return isWebseriesRecent;
    }

    public static List<WebSeries> getRecentWebseriesBriefs(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<WebSeries> recentWebseries = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.RECENT_WEBSERIES_TABLE_NAME, null, null, null, null, null, DatabaseHelper.ID + " DESC");
        while (cursor.moveToNext()) {
            WebSeries webSeries = new WebSeries();
            String webseriesId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.WEBSERIES_ID));
            String webseriesTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
            String webseriesPoster = cursor.getString(cursor.getColumnIndex(DatabaseHelper.POSTER));
            String webseriesIMDBRating = cursor.getString(cursor.getColumnIndex(DatabaseHelper.IMDB_RATING));
            String webseriesSeasonTag = cursor.getString(cursor.getColumnIndex(DatabaseHelper.SEASON_TAG));
            String webseriesCertificate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CERTIFICATE));

            webSeries.setId(webseriesId);
            webSeries.setTitle(webseriesTitle);
            webSeries.setMainPoster(webseriesPoster);
            webSeries.setIMDBRating(webseriesIMDBRating);
            webSeries.setSeasonTag(webseriesSeasonTag);
            webSeries.setCertificate(webseriesCertificate);

            recentWebseries.add(webSeries);
        }
        cursor.close();
        database.close();
        return recentWebseries;
    }

    //ADD RECENT SEARCH
    public static void addSearchRecent(Context context, String word) {
        if (word == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if (isSearchRecent(context, word)) {
            removeSearchFromRecent(context, word);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.WORD, word);
            database.insert(DatabaseHelper.RECENT_SEARCH, null, contentValues);
        }else {
            removeFirstRowFromSearchRecent(context);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.WORD, word);
            database.insert(DatabaseHelper.RECENT_SEARCH, null, contentValues);
        }
        database.close();
    }

    public static boolean isSearchRecent(Context context, String word) {
        if (word == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isSearchReecent;
        String isSearchRecentQuery = "SELECT * FROM " + DatabaseHelper.RECENT_SEARCH+ " WHERE " + DatabaseHelper.WORD + " = ?";
        Cursor cursor = database.rawQuery(isSearchRecentQuery, new String[] { word});
        if (cursor.getCount() == 1)
            isSearchReecent = true;
        else
            isSearchReecent = false;

        cursor.close();
        database.close();
        return isSearchReecent;
    }

    public static void removeSearchFromRecent(Context context, String word) {
        if (word == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isSearchRecent(context, word)) {
            database.delete(DatabaseHelper.RECENT_SEARCH, DatabaseHelper.WORD+ " = ?", new String[] { word});
        }
        database.close();
    }

    public static void removeFirstRowFromSearchRecent(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isRecentSearchRowMoreThan10(context)) {
            String deleteRecentSearchFirstRowQuery = "DELETE FROM " + DatabaseHelper.RECENT_SEARCH + " WHERE " + DatabaseHelper.ID + " IN " +
                    "(SELECT " + DatabaseHelper.ID + " FROM " + DatabaseHelper.RECENT_SEARCH +
                    " ORDER BY " + DatabaseHelper.ID +" ASC LIMIT 1)";
            database.execSQL(deleteRecentSearchFirstRowQuery);
        }
        database.close();
    }

    public static boolean isRecentSearchRowMoreThan10(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isSearchRecentRowMoreThan10;
        long count = DatabaseUtils.queryNumEntries(database, DatabaseHelper.RECENT_SEARCH);

        if (count > 10)
            isSearchRecentRowMoreThan10 = true;
        else
            isSearchRecentRowMoreThan10 = false;

        database.close();
        return isSearchRecentRowMoreThan10;
    }

    public static void clearAllSearchRecent(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        String deleteAllRecentSearchQuery = "DELETE FROM " + DatabaseHelper.RECENT_SEARCH;
        database.execSQL(deleteAllRecentSearchQuery);

        database.close();
    }

    public static List<String> getSearchRecentBriefs(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<String> searchRecent = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.RECENT_SEARCH, null, null, null, null, null, DatabaseHelper.ID + " DESC");
        while (cursor.moveToNext()) {
            String word = cursor.getString(cursor.getColumnIndex(DatabaseHelper.WORD));

            searchRecent.add(word);
        }
        cursor.close();
        database.close();
        return searchRecent;
    }

    //ADD ITEM TRENDING SEARCH
    public static void addItemToTrendingSearch(Context context, String itemId, String title, String category) {
        if (itemId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        if (!isTrendingSearch(context, itemId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.MOVIE_ID, itemId);
            contentValues.put(DatabaseHelper.TITLE, title);
            contentValues.put(DatabaseHelper.CATEGORY, category);
            database.insert(DatabaseHelper.TRENDING_SEARCH, null, contentValues);
        }
        database.close();
    }

    public static void removeItemFromTrendingSearch(Context context, String itemId) {
        if (itemId == null) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        if (isTrendingSearch(context, itemId)) {
            database.delete(DatabaseHelper.TRENDING_SEARCH, DatabaseHelper.ITEM_ID + " = ?", new String[] { itemId});
        }
        database.close();
    }

    public static boolean isTrendingSearch(Context context, String itemId) {
        if (itemId == null) return false;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        boolean isItemTrendingSearch;
        String isItemTrendingSearchQuery = "SELECT * FROM " + DatabaseHelper.TRENDING_SEARCH + " WHERE " + DatabaseHelper.ITEM_ID + " = ?";
        Cursor cursor = database.rawQuery(isItemTrendingSearchQuery, new String[] { itemId});
        if (cursor.getCount() == 1)
            isItemTrendingSearch = true;
        else
            isItemTrendingSearch = false;

        cursor.close();
        database.close();
        return isItemTrendingSearch;
    }

    public static List<Item> getTrendingSearchBriefs(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        List<Item> trendingSearch = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TRENDING_SEARCH, null, null, null, null, null, DatabaseHelper.ID + " DESC");
        while (cursor.moveToNext()) {
            Item item = new Item();
            String itemId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOVIE_ID));
            String itemTitle = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
            String itemCategory = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CATEGORY));

            item.setId(itemId);
            item.setTitle(itemTitle);
            item.setCategory(itemCategory);

            trendingSearch.add(item);
        }
        cursor.close();
        database.close();
        return trendingSearch;
    }
}
