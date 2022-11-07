package com.digital.moviesbuddypro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.movies.flowlayoutmanager.Alignment;
import com.movies.flowlayoutmanager.FlowLayoutManager;
import com.digital.moviesbuddypro.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelperClass {

    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1001;

    public void showContactUsDialog(final Context context){
        final Dialog dDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        dDialog.setContentView(R.layout.dialog_contactus);

        View whatsappContainer = dDialog.findViewById(R.id.whatsappContainer);
        View facebookContainer = dDialog.findViewById(R.id.facebookContainer);
        View instagramContainer = dDialog.findViewById(R.id.instagramContainer);
        View twitterContainer = dDialog.findViewById(R.id.twitterContainer);
        View telegramContainer = dDialog.findViewById(R.id.telegramContainer);

        whatsappContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWhatsapp(context);
            }
        });

        facebookContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFacebook(context);
            }
        });

        instagramContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInstagram(context);
            }
        });

        twitterContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goToTwitter(context);
            }
        });

        telegramContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinTelegram(context);
            }
        });

        dDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.cancel();
            }
        });

        dDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dDialog.show();
    }

    /*//////////////////////////////*/

    public static void goToInstagram(Context context) {
        final String appName = "com.instagram.android";
        final boolean isAppInstalled = isAppAvailable(context, appName);
        if (isAppInstalled)
        {
            try {
                Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                telegramIntent.setPackage(appName);
                telegramIntent.setData(Uri.parse("https://www.instagram.com/moviesbuddypro/"));
                context.startActivity(telegramIntent);
            } catch (Exception e) {
                // show error message
            }
        }
        else
        {
            Toast.makeText(context, "Instagram not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    public static void goToFacebook(Context context) {
        final String appName = "com.facebook.katana";
        final String appName_2 = "com.facebook.lite";
        final boolean isAppInstalled = isAppAvailable(context, appName);
        final boolean isAppInstalled_2 = isAppAvailable(context, appName_2);
        if (isAppInstalled || isAppInstalled_2) {
            String finalAppName;
            if (isAppInstalled){
                finalAppName = appName;
            }else {
                finalAppName = appName_2;
            }

            try {
                Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                telegramIntent.setPackage(finalAppName);
                telegramIntent.setData(Uri.parse("fb://page/102182295575015"));
                context.startActivity(telegramIntent);
            } catch (Exception e) {
                // show error message
            }

        }else {
            Toast.makeText(context, "Install Facebook First", Toast.LENGTH_SHORT).show();
        }
    }

    void goToWhatsapp(Context context) {
        final String appName = "com.whatsapp";
        final String appName_2 = "com.whatsapp.w4b";
        final boolean isAppInstalled = isAppAvailable(context, appName);
        final boolean isAppInstalled_2 = isAppAvailable(context, appName_2);
        if (isAppInstalled || isAppInstalled_2) {
            String finalAppName;
            if (isAppInstalled){
                finalAppName = appName;
            }else {
                finalAppName = appName_2;
            }

            try {
                Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                telegramIntent.setPackage(finalAppName);
                telegramIntent.setData(Uri.parse("https://wa.me/919065164230"));
                context.startActivity(telegramIntent);
            } catch (Exception e) {
                // show error message
            }

        }else {
            Toast.makeText(context, "Install Facebook First", Toast.LENGTH_SHORT).show();
        }
    }

    public static void joinTelegram(Context context) {
        final String appName = "org.telegram.messenger";
        final boolean isAppInstalled = isAppAvailable(context, appName);
        if (isAppInstalled)
        {
            try {
                Intent telegramIntent = new Intent(Intent.ACTION_VIEW);
                telegramIntent.setPackage(appName);
                telegramIntent.setData(Uri.parse("http://telegram.me/MoviesBuddy_Pro"));
                context.startActivity(telegramIntent);
            } catch (Exception e) {
                // show error message
            }
        } else {
            Toast.makeText(context, "Telegram not Installed", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try
        {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    public void watchFromUrl(Context context, String url){
        final String appName = "com.mxtech.videoplayer.pro";
        final String appName_2 = "com.mxtech.videoplayer.ad";
        final boolean isAppInstalled = isAppAvailable(context, appName);
        final boolean isAppInstalled_2 = isAppAvailable(context, appName_2);
        if (isAppInstalled || isAppInstalled_2) {
            String finalAppName;
            if (isAppInstalled){
                finalAppName = appName;
            }else {
                finalAppName = appName_2;
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "video/*");
                intent.setPackage(finalAppName);
                context.startActivity(Intent.createChooser(intent, "Play Movie With"));
            }catch (Exception e){
                // show error message
            }
        }else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "video/*");
            context.startActivity(Intent.createChooser(intent, "Play Movie With"));
        }
    }
    /*/////////////////////////////*/

    public static void shareApp(Context mContext, ImageView mPoster, String mTitleText, Activity mActivity){
        final String appName = "com.whatsapp";
        final boolean isAppInstalled = isAppAvailable(mContext, appName);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String s = "https://play.google.com/store/apps/details?id="+mContext.getApplicationContext().getPackageName();
            if (isAppInstalled) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                Drawable mDrawable = mPoster.getDrawable();
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), mBitmap, "Image I want to share", null);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                if (mTitleText.equalsIgnoreCase("Movies Buddy Pro")){
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "*Hey, I'm Using Movies Buddy Pro App To Watch Or Download Latest Movies & Web Series.* If You Want The Same Try Using The App Then,\n\n\n" +
                            "*Download Movies Buddy Pro App From Below Link*\n\n" +
                            "*"+s+"*");
                }else {
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "*Hey I'm Watching: " + "" + mTitleText + " Now*" +
                            "\n\n*Download Movies Buddy Pro App*\n\n" +
                            "*"+s+"*");
                }
                shareIntent.setPackage(appName);
                mContext.startActivity(Intent.createChooser(shareIntent, "Share Movies Buddy With"));
            }else {
                Toast.makeText(mContext, "WhatsApp Not Installed", Toast.LENGTH_LONG).show();
            }
        }else {
            requestWriteExternalStoragePermission(mActivity);
        }
    }

    private static void requestWriteExternalStoragePermission(Activity mActivity) {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,  new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else{
            ActivityCompat.requestPermissions(mActivity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    public static void openYouTube(Context mContext, String mLink){
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(mLink));
        try {
            mContext.startActivity(webIntent);
        } catch (ActivityNotFoundException ex) { }
    }

    @SuppressLint("SetTextI18n")
    public static void openRateUsDialog(Context mContext){
        Dialog dDialog = new Dialog(mContext, R.style.FullScreenDialogStyle);
        dDialog.setContentView(R.layout.dialog_rate_us);

        RatingBar mRatingBar = dDialog.findViewById(R.id.rateStars);
        TextView mRateButton = dDialog.findViewById(R.id.rateUsButton);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //Toast.makeText(mContext, ""+rating, Toast.LENGTH_SHORT).show();
                final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                dDialog.cancel();
            }
        });

        mRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                dDialog.cancel();
            }
        });

        dDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.cancel();
            }
        });

        dDialog.findViewById(R.id.cancelDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.cancel();
            }
        });

        dDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dDialog.show();
    }

    private static void recyclerViewClickListener(Context mContext, RecyclerView mRecyclerView,
                                           AdapterFlowLayoutMultiSelector mAdapterFlowLayoutMultiSelector, List<String> mList,
                                           List<String> mListSelected, int mLimit){
        mRecyclerView.addOnItemTouchListener(new ClickListenerRecyclerViewItem(mContext, mRecyclerView, new ClickListenerRecyclerViewItem.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                multiSelect(mAdapterFlowLayoutMultiSelector, mList, mListSelected, position, mLimit);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(mContext, mList.get(position), Toast.LENGTH_SHORT).show();

            }
        }));
    }

    public static void multiSelect(AdapterFlowLayoutMultiSelector mAdapterFlowLayoutMultiSelector, List<String> mList, List<String> mListSelected,
                            int position, int mLimit) {
        if (mListSelected.contains(mList.get(position))) {
            mListSelected.remove(mList.get(position));
        } else if (mListSelected.size() == mLimit) {
            mListSelected.add(mList.get(position));
            mListSelected.remove(mListSelected.get(0));
        }else {
            mListSelected.add(mList.get(position));
        }

        refreshAdapter(mAdapterFlowLayoutMultiSelector, mList, mListSelected);
    }


    public static void refreshAdapter(AdapterFlowLayoutMultiSelector mAdapterFlowLayoutMultiSelector, List<String> mList, List<String> mListSelected) {
        mAdapterFlowLayoutMultiSelector.mListStringSelected = mListSelected;
        mAdapterFlowLayoutMultiSelector.mListString = mList;
        mAdapterFlowLayoutMultiSelector.notifyDataSetChanged();
    }


    @SuppressLint("SetTextI18n")
    public static void openFeedbackDialog(Context mContext){
        Dialog dDialog = new Dialog(mContext, R.style.FullScreenDialogStyle);
        dDialog.setContentView(R.layout.dialog_feedback);

        EditText mFeedbackEditText = dDialog.findViewById(R.id.feedbackEditText);
        TextView mFeedbackButton = dDialog.findViewById(R.id.feedbackButton);

        mFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mFeedback = mFeedbackEditText.getText().toString();

                if (mFeedback.equalsIgnoreCase("")){
                    Toast.makeText(mContext, "Please Write Feedback!", Toast.LENGTH_SHORT).show();
                }else {
                    sendMail(mContext, "Feedback!", mFeedback);
                    dDialog.cancel();
                }
            }
        });

        dDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.cancel();
            }
        });

        dDialog.findViewById(R.id.cancelDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.cancel();
            }
        });

        dDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dDialog.show();
    }

    @SuppressLint("SetTextI18n")
    public static void openBusinessInquiryDialog(Context mContext){
        Dialog dDialog = new Dialog(mContext, R.style.FullScreenDialogStyle);
        dDialog.setContentView(R.layout.dialog_business_inquiry);

        EditText mBusinessInquiryEditText = dDialog.findViewById(R.id.businessInquiryEditText);
        TextView mBusinessInquryButton = dDialog.findViewById(R.id.businessInquiryButton);

        mBusinessInquryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mBusinessInquiry = mBusinessInquiryEditText.getText().toString();

                if (mBusinessInquiry.equalsIgnoreCase("")){
                    Toast.makeText(mContext, "Please Write Some Text!", Toast.LENGTH_SHORT).show();
                }else {
                    sendMail(mContext, "Business Inquiry!", mBusinessInquiry);
                    dDialog.cancel();
                }
            }
        });

        dDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.cancel();
            }
        });

        dDialog.findViewById(R.id.cancelDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDialog.cancel();
            }
        });

        dDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dDialog.show();
    }

    public static void sendMail(Context mContext, String mSubject, String mBody){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + mSubject + "&body=" + mBody + "&to=" + "moviesbuddypro@gmail.com");
        intent.setData(data);
        mContext.startActivity(intent);
    }
}
