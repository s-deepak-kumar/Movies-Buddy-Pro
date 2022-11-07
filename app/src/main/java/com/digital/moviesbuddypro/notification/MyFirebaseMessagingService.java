package com.digital.moviesbuddypro.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.digital.moviesbuddypro.activity.ActivityMovieDetails;
import com.digital.moviesbuddypro.activity.ActivityWebseriesDetails;
import com.digital.moviesbuddypro.model.Movie;
import com.digital.moviesbuddypro.model.WebSeries;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.digital.moviesbuddypro.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    NotificationCompat.Builder notificationBuilder;

    Movie movie;
    WebSeries webSeries;
    Bitmap image;
    Intent intent;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        if (remoteMessage.getData().get("category").equalsIgnoreCase("Web Series")){
            webSeries = new WebSeries();
            webSeries.setId(remoteMessage.getData().get("id"));
            webSeries.setTitle(remoteMessage.getData().get("title"));
            webSeries.setMainPoster(remoteMessage.getData().get("poster"));
            webSeries.setSeasonTag(remoteMessage.getData().get("season_tag"));

            if (remoteMessage.getData().get("imdb_rating").equals("null")) {
                webSeries.setIMDBRating("∞");
            } else {
                webSeries.setIMDBRating(remoteMessage.getData().get("imdb_rating"));
            }

            if (remoteMessage.getData().get("certificate").equals("Not Rated")) {
                webSeries.setCertificate("N/A");
            } else {
                webSeries.setCertificate(remoteMessage.getData().get("certificate"));
            }

            image = getBitmapFromURL(webSeries.getMainPoster());

            sendWebSeriesNotification(webSeries.getId(), "Check Out This Web Series", webSeries.getTitle(), webSeries.getMainPoster(),
                    webSeries.getIMDBRating(), webSeries.getCertificate(), webSeries.getSeasonTag(), image);
        }else {
            movie = new Movie();
            movie.setId(remoteMessage.getData().get("id"));
            movie.setTitle(remoteMessage.getData().get("title"));
            movie.setPoster(remoteMessage.getData().get("poster"));
            movie.setCategory(remoteMessage.getData().get("category"));

            if (remoteMessage.getData().get("imdb_rating").equals("null")) {
                movie.setIMDBRating("∞");
            } else {
                movie.setIMDBRating(remoteMessage.getData().get("imdb_rating"));
            }

            if (remoteMessage.getData().get("certificate").equals("Not Rated")) {
                movie.setCertificate("N/A");
            } else {
                movie.setCertificate(remoteMessage.getData().get("certificate"));
            }

            image = getBitmapFromURL(movie.getPoster());

            sendMovieNotification(movie.getId(), "Check Out This Movie", movie.getTitle(), movie.getPoster(),
                    movie.getIMDBRating(), movie.getCertificate(), movie.getCategory(), image);
        }
    }
    // [END receive_message]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param description FCM message body received.
     */
    private void sendMovieNotification(String mId, String description, String mTitle, String mPoster,
                                  String mIMDBRating, String mCertificate, String mCategory, Bitmap img) {

        Intent intent = new Intent(this, ActivityMovieDetails.class);
        intent.putExtra("MOVIE_ID_KEY", mId);
        intent.putExtra("MOVIE_CATEGORY_KEY", mCategory);
        intent.putExtra("MOVIE_TITLE_KEY", mTitle);
        intent.putExtra("MOVIE_CERTIFICATE_KEY", mCertificate);
        intent.putExtra("MOVIE_POSTER_KEY", mPoster);
        intent.putExtra("MOVIE_IMDB_RATING_KEY", mIMDBRating);
        intent.putExtra("IS_FROM_NOTIFICATION_KEY", true);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.S){
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_MUTABLE);

        }else {
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String channelID = getString(R.string.channel_id);

        notificationBuilder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(mTitle)
                .setContentText(description)
                .setLargeIcon(img)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(img).bigLargeIcon(null))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID,
                    "Movies Buddy Pro Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendWebSeriesNotification(String mId, String description, String mTitle, String mPoster,
                                       String mIMDBRating, String mCertificate, String mSeasonTag, Bitmap img) {

        Intent intent = new Intent(this, ActivityWebseriesDetails.class);
        intent.putExtra("WEBSERIES_ID_KEY", mId);
        intent.putExtra("WEBSERIES_SEASON_TAG_KEY", mSeasonTag);
        intent.putExtra("WEBSERIES_TITLE_KEY", mTitle);
        intent.putExtra("WEBSERIES_CERTIFICATE_KEY", mCertificate);
        intent.putExtra("WEBSERIES_POSTER_KEY", mPoster);
        intent.putExtra("WEBSERIES_IMDB_RATING_KEY", mIMDBRating);
        intent.putExtra("IS_FROM_NOTIFICATION_KEY", true);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.S){
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_MUTABLE);

        }else {
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String channelID = getString(R.string.channel_id);

        notificationBuilder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(mTitle)
                .setContentText(description)
                .setLargeIcon(img)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(img).bigLargeIcon(null))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID,
                    "Movies Buddy Pro Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String capitalize (String givenString) {
        String Separateur = " ,.-;";
        StringBuilder sb = new StringBuilder();
        boolean ToCap = true;
        for (int i = 0; i < givenString.length(); i++) {
            if (ToCap)
                sb.append(Character.toUpperCase(givenString.charAt(i)));
            else
                sb.append(Character.toLowerCase(givenString.charAt(i)));

            ToCap = Separateur.indexOf(givenString.charAt(i)) >= 0;
        }
        return sb.toString().trim();
    }
}
