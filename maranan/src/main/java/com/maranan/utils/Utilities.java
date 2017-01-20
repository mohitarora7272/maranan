package com.maranan.utils;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.maranan.DevoteActivity;
import com.maranan.R;
import com.nispok.snackbar.Snackbar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Utilities {

    public static String MyPREFERENCES = "GCMRegidPref";
    public static final String TAG = "LogInActivity";
    public static final String GCM_REG_ID = "gcm_reg_id";
    public static final String GCM_REG_BOOL = "gcm_reg_bool";
    public static SharedPreferences sharedPreferences;
    public static String gcm_id = "";
    public static boolean mSwitchTextDirection = false;

    // Key's For Media Player...
    public static String PLAYER_PREF = "Player_Pref";
    public static final String KEY_PLAYER_START = "isPlayerStart";
    public static final String KEY_ID = "id";
    public static final String KEY_POSITION = "position";

    // Key's For Media Player...
    public static String CANCEL_PREF = "Cancel_Pref";
    public static final String KEY_PLAYER_CANCEL = "isPlayerCancel";
    public static final String KEY_ID_CANCEL = "id_cancel";

    // Key's For Notification Alertss...
    public static String NOTI_PREF = "Noti_Pref";
    public static final String KEY_NOTI_BOOL = "isComingFromNoti";
    public static final String KEY_ID_NOTI = "id_noti";
    public static final String KEY_URL_NOTI = "url";

    // Key's For Notification Alertss...
    public static String FAV_PREF = "Fav_Pref";
    public static final String KEY_FAV_EMAIL = "fav_email";
    public static final String KEY_FAV_ID = "id";

    // Key's For Notification Radio Alarm Alert ID...
    public static String RADIO_ALARM = "Radio_Alarm_Pref";
    public static final String KEY_RADIO_ALARM_ID = "id";

    // Key's For PDF
    public static String PDF = "PDF_Pref";
    public static final String KEY_PDF_NAME = "pdf_name";
    public static final String KEY_PDF_PATH = "pdf_path";

    // Key's For Video Alert
    public static String VIDEO = "Video_Pref";
    public static final String KEY_VIDEO_ID = "video_id";

    // Key's For Email Dedicated Users
    public static String USER_ID_PREF = "user_id_pref";
    public static final String KEY_USER_ID_DEDICATED = "user_id_dedicated";

    // Key's For Coming From Dedications
    public static String COMING_FROM_PREF = "Coming_From_Pref";
    public static String KEY_USER_IS_COMING_FROM = "isComingFromDedication";

    // Key's For Coming From Dedications
    public static String COMING_FROM_PREF_HOME = "Coming_From_Pref_Home";
    public static String KEY_USER_IS_COMING_FROM_HOME = "isComingFromDedicationHome";


    public static String dest_file_path = "Maranan/PDF";
    static int downloadedSize = 0;
    static int totalsize;
    static float per = 0;
    public static ProgressBar seek;
    public static Dialog dialog;
    public static String pdfName;

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(final Context context, String title,
                                       String message, Boolean status) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.sucess : R.drawable.fail);
        alertDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setButton2("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Setting(context);
                    }
                });
        alertDialog.show();
    }

    public static void showAlertDialogDownload(final Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_pdf_dialog);
        seek = (ProgressBar) dialog.findViewById(R.id.seek_bar_pdf);
        seek.setProgress(0);
        seek.setMax(10000000);
        dialog.show();
    }

    /**
     * check email validation
     **/
    public static boolean isEmailValid(String email) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /****
     * Method for Setting the Height of the ListView dynamically. Hack to fix
     * the issue of not showing all the items of the ListView when placed inside
     * a ScrollView
     ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
                MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * hide Keyboard
     *
     * @param ctx
     **/
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = ((Activity) ctx).getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // Find DataBase Exists or Not
    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    // Get Israel Time
    public static String getIsraelTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        // sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        return sdf.format(new Date());
    }

    // Get Israel Time
    public static String getIsraelDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy",
                Locale.ENGLISH);
        // sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        return sdf.format(new Date());
    }

    // Get Current Date...
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy",
                Locale.ENGLISH);
        return sdf.format(new Date());
    }

    // Get Current Date With Diffrent Format...
    public static String getCurrentDateDiffFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy",
                Locale.ENGLISH);
        return sdf.format(new Date());
    }

    // Get Current Time...
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());
    }

    // parse date to change date patterns...
    public static String parseDateToddMMMMyyyy(String datestr) {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "dd-yyyy-MMMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,
                Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,
                Locale.ENGLISH);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(datestr);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    // Log out
    public static void logout(Context context) {
        SharedPreferences pref = context.getSharedPreferences("RegisterPref",
                Context.MODE_PRIVATE);
        if (pref.getString("signinwith", null) != null) {
            if (pref.getString("signinwith", null).equals("App")) {
                // showToast(context, "Logout");
                showSnackbar(context, "Logout");
                pref = context.getSharedPreferences("RegisterPref",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

            } else if (pref.getString("signinwith", null).equals("Facebook")) {
                Session session = Session.getActiveSession();
                if (session != null && session.isOpened()) {
                    // showToast(context, "Logout");
                    showSnackbar(context, "Logout");
                    session.closeAndClearTokenInformation();
                    SharedPreferences pref1 = context
                            .getSharedPreferences(
                                    "com.facebook.AuthorizationClient.WebViewAuthHandler.TOKEN_STORE_KEY",
                                    Context.MODE_PRIVATE);
                    SharedPreferences pref2 = context
                            .getSharedPreferences(
                                    "com.facebook.SharedPreferencesTokenCachingStrategy.DEFAULT_KEY",
                                    Context.MODE_PRIVATE);
                    SharedPreferences pref3 = context.getSharedPreferences(
                            "com.facebook.internal.preferences.APP_SETTINGS",
                            Context.MODE_PRIVATE);
                    pref = context.getSharedPreferences("RegisterPref",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    SharedPreferences.Editor editor1 = pref1.edit();
                    SharedPreferences.Editor editor2 = pref2.edit();
                    SharedPreferences.Editor editor3 = pref3.edit();
                    editor.clear();
                    editor.commit();
                    editor1.clear();
                    editor1.commit();
                    editor2.clear();
                    editor2.commit();
                    editor3.clear();
                    editor3.commit();
                }

            } else {
                // showToast(context, "Logout");
                showSnackbar(context, "Logout");
                pref = context.getSharedPreferences("RegisterPref",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                // LogInActivity.getInstance().signOutFromGplus();
                /*
                 * if (LogInActivity.mGoogleApiClient.isConnected()) {
				 * Log.e("if", "if"+
				 * LogInActivity.mGoogleApiClient.isConnected());
				 * Plus.AccountApi
				 * .clearDefaultAccount(LogInActivity.mGoogleApiClient);
				 * LogInActivity.mGoogleApiClient.disconnect(); //
				 * mGoogleApiClient.connect(); }else{ Log.e("else", "else"+
				 * LogInActivity.mGoogleApiClient.isConnected());
				 * Plus.AccountApi
				 * .clearDefaultAccount(LogInActivity.mGoogleApiClient); }
				 */
            }
        } else {
            showSnackbar(context, "Please Login");
            // showToast(context, "Please Login");
        }

        // When Log Out then Delete All Records From database
        DevoteActivity.getInstance().getLocalDataBase().deleteAllRecord();
    }

    // Open Setting Window When Internet Connection are not available
    public static void Setting(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    // Check is Appllication Shown At front Or Not
    @SuppressWarnings("unused")
    private boolean isAppShown(Context context) {

        KeyguardManager km = (KeyguardManager) context
                .getSystemService(Context.KEYGUARD_SERVICE);
        boolean locked = km.inKeyguardRestrictedInputMode();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> l = mActivityManager
                .getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = i.next();

            if (info.uid == context.getApplicationInfo().uid
                    && info.importance ==
                    ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && !locked) {
                return true;
            }
        }

        return false;
    }

    // Slide to above animation
    public static Animation SlideToAbove(final ListView rl_footer, Context ctx) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -2.8f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        rl_footer.startAnimation(slide);

        slide.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                rl_footer.clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        rl_footer.getWidth(), rl_footer.getHeight());
                lp.setMargins(0, 150, 0, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                rl_footer.setLayoutParams(lp);

            }

        });
        return slide;

    }

    public static Animation SlideToDown(final ListView rl_footer, Context ctx) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 3.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        rl_footer.startAnimation(slide);

        slide.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                rl_footer.clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        rl_footer.getWidth(), rl_footer.getHeight());
                lp.setMargins(0, 0, 0, 0);

                lp.addRule(RelativeLayout.BELOW, R.id.image);
                rl_footer.setLayoutParams(lp);

            }

        });
        return slide;

    }

    // set the shared preferences
    public static void setSharedPref(String key, String value, String key2,
                                     Boolean bool) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.putBoolean(key2, bool);
        editor.commit();
    }

    // set the shared preferences
    public static void setSharedPrefRegistrationValues(Context ctx, String key,
                                                       String value, String key2, String value2) {
        SharedPreferences pref = ctx.getSharedPreferences("RegisterPref",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.putString(key2, value2);
        editor.commit();
    }

    // set the shared preferences for player
    public static void setSharedPrefPlayer(Context ctx, Boolean isPlayerStart,
                                           String id, int position) {
        SharedPreferences pref = ctx.getSharedPreferences(PLAYER_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_PLAYER_START, isPlayerStart);
        editor.putString(KEY_ID, id);
        editor.putInt(KEY_POSITION, position);
        editor.commit();
    }

    // set the shared preferences for player cancel
    public static void setSharedPrefPlayerCancel(Context ctx,
                                                 Boolean isPlayerStart, String id) {
        SharedPreferences pref = ctx.getSharedPreferences(CANCEL_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_PLAYER_CANCEL, isPlayerStart);
        editor.putString(KEY_ID_CANCEL, id);
        editor.commit();
    }

    // set the shared preferences for player cancel
    public static void setSharedPrefForNoti(Context ctx,
                                            Boolean isComingFromNoti, String id, String url) {
        SharedPreferences pref = ctx.getSharedPreferences(NOTI_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_NOTI_BOOL, isComingFromNoti);
        editor.putString(KEY_ID_NOTI, id);
        editor.putString(KEY_URL_NOTI, url);
        editor.commit();
    }

    // set the shared preferences for favourite
    public static void setSharedPrefForFav(Context ctx, String id, String email) {
        SharedPreferences pref = ctx.getSharedPreferences(FAV_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_FAV_ID, id);
        editor.putString(KEY_FAV_EMAIL + id, email);
        editor.commit();
    }

    // set the shared preferences for favourite
    public static void setSharedPrefForFavId(Context ctx, String id) {
        SharedPreferences pref = ctx.getSharedPreferences(FAV_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_FAV_ID, id);
        editor.commit();
    }

    // set the shared preferences for radio Alarm Alert Id
    public static void setRadioAlermID(Context ctx, String id) {
        SharedPreferences pref = ctx.getSharedPreferences(RADIO_ALARM,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_RADIO_ALARM_ID, id);
        editor.commit();
    }

    // set the shared preferences for pdf
    public static void setPDF(Context ctx, String pdfName, String pdfPath) {
        SharedPreferences pref = ctx.getSharedPreferences(PDF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_PDF_NAME, pdfName);
        editor.putString(KEY_PDF_PATH, pdfPath);
        editor.commit();
    }

    // set the shared preferences for video id
    public static void setVideoId(Context ctx, String id) {
        SharedPreferences pref = ctx.getSharedPreferences(VIDEO,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_VIDEO_ID, id);
        editor.commit();
    }

    // set the shared preferences for user dedicated id
    public static void setUserIdDedicated(Context ctx, String id) {
        SharedPreferences pref = ctx.getSharedPreferences(USER_ID_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_USER_ID_DEDICATED, id);
        editor.commit();
    }

    // Get user dedicated id...
    public static String getUserIdDedicated(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(USER_ID_PREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_USER_ID_DEDICATED, null);
    }

    // delete sharedPrefForPref for email....
    public static void deleteSharedPrefForEmail(Context ctx, String id) {
        SharedPreferences pref = ctx.getSharedPreferences(FAV_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(KEY_FAV_EMAIL + id);
        editor.commit();
    }

    // delete sharedPrefForPref for id....
    public static void deleteSharedPrefForId(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(FAV_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(KEY_FAV_ID);
        editor.commit();
    }

    // Get Favourite Id...
    public static String getFavId(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(FAV_PREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_FAV_ID, null);
    }

    // Get Favourite Email...
    public static String getFavEmail(Context ctx, String id) {
        SharedPreferences pref = ctx.getSharedPreferences(FAV_PREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_FAV_EMAIL + id, null);
    }

    // Get Radio Alarm ID
    public static String getRadioAlarmID(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(RADIO_ALARM,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_RADIO_ALARM_ID, null);
    }

    // Get PDf Name
    public static String getPDfName(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PDF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_PDF_NAME, null);
    }

    // Get Pdf Path
    public static String getPDfPath(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PDF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_PDF_PATH, null);
    }

    // Get Video ID
    public static String getVideoId(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(VIDEO,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_VIDEO_ID, null);
    }

    // insert favourite arraylist in SharePrefrence...
    public static void insertArrayListPref(Context ctx, ArrayList<String> list) {
        SharedPreferences prefs = ctx.getSharedPreferences("ListFavourite",
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString("FavList", ObjectSerializer.serialize(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<String> getfavArrayListPref(Context ctx) {
        ArrayList<String> favlist = null;
        SharedPreferences prefs = ctx.getSharedPreferences("ListFavourite",
                Context.MODE_PRIVATE);
        try {
            favlist = (ArrayList<String>) ObjectSerializer
                    .deserialize(prefs.getString("FavList",
                            ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return favlist;
    }

    // insert favourite arraylist tag in SharePrefrence...
    public static void insertArrayListPrefForTag(Context ctx,
                                                 ArrayList<GetterSetter> list) {
        SharedPreferences prefs = ctx.getSharedPreferences("ListFavouriteTag",
                Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        try {
            editor.putString("FavListTag", ObjectSerializer.serialize(list));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<GetterSetter> getfavArrayListPrefForTag(Context ctx) {
        ArrayList<GetterSetter> favlist = null;
        SharedPreferences prefs = ctx.getSharedPreferences("ListFavouriteTag",
                Context.MODE_PRIVATE);
        try {
            favlist = (ArrayList<GetterSetter>) ObjectSerializer
                    .deserialize(prefs.getString("FavListTag", ObjectSerializer
                            .serialize(new ArrayList<GetterSetter>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return favlist;
    }

    // Get Boolean Value for check player is started or not...
    public static Boolean isComingFromNoti(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(NOTI_PREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_NOTI_BOOL, false);
    }

    // Get Boolean Value for check user is Coming From dedication Screen or not
    public static Boolean isComingFromDedication(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(COMING_FROM_PREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_USER_IS_COMING_FROM, false);
    }

    // Set Boolean Value for check user is Coming From dedication Screen or not
    public static void setComingFromDedication(Context ctx, Boolean isComingFrom) {
        SharedPreferences pref = ctx.getSharedPreferences(COMING_FROM_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_USER_IS_COMING_FROM, isComingFrom);
        editor.commit();
    }

    // Get Boolean Value for check user is Coming From dedication Screen or not
    public static Boolean isComingFromDedicationHome(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(
                COMING_FROM_PREF_HOME, Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_USER_IS_COMING_FROM_HOME, false);
    }

    // Set Boolean Value for check user is Coming From dedication Screen or not
    public static void setComingFromDedicationHome(Context ctx,
                                                   Boolean isComingFrom) {
        SharedPreferences pref = ctx.getSharedPreferences(
                COMING_FROM_PREF_HOME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_USER_IS_COMING_FROM_HOME, isComingFrom);
        editor.commit();
    }

    // Get Boolean Value for check player is started or not...
    public static Boolean isPlayerStart(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PLAYER_PREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_PLAYER_START, false);
    }

    // Get Boolean Value for check player cancel or not...
    public static Boolean isPlayerCancel(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(CANCEL_PREF,
                Context.MODE_PRIVATE);
        return pref.getBoolean(KEY_PLAYER_CANCEL, false);
    }

    // Get Player List Id...
    public static String getPlayerId(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PLAYER_PREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_ID, null);
    }

    // Get Alert Id...
    public static String getAlertId(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(NOTI_PREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_ID_NOTI, null);
    }

    // Get Alert URL...
    public static String getAlertURL(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(NOTI_PREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_URL_NOTI, null);
    }

    // Get Player List Id...
    public static String getPlayerIdForCancel(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(CANCEL_PREF,
                Context.MODE_PRIVATE);
        return pref.getString(KEY_ID_CANCEL, null);
    }

    // Get Player Position...
    public static int getPlayerPosition(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(PLAYER_PREF,
                Context.MODE_PRIVATE);
        return pref.getInt(KEY_POSITION, 0);
    }

    // get the shared preferences...
    public static String getSharedPref(String key, String value) {
        return sharedPreferences.getString(key, value);
    }

    // get the shared preferences...
    public static boolean getSharedPrefForGCM(String key, Boolean value) {
        return sharedPreferences.getBoolean(key, value);
    }

    // Get Country Code Here...
    public static String getCountryCode(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        return countryCodeValue;
    }

    // Get Time Zone Here...
    public static String getTimeZone(Context ctx) {
        Calendar cal = Calendar.getInstance();
        return cal.getTimeZone().getID();
    }

    // Get Unix Time Stamp Here...
    public static String getUnixTimeStamp(Context ctx) {
        long unixTime = System.currentTimeMillis() / 1000L;
        return String.valueOf(unixTime);
    }

    // Get Bitmap From URL..
    public static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
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

    // Converting Bitmap To String...
    public static String BitMapToString(Bitmap bitmap) {
        String temp = "";
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        }
        return temp;
    }

    // Converting String To Bitmap...
    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    // Change Drawable To BitMap...
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0
                || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single
            // color
            // bitmap
            // will
            // be
            // created
            // of
            // 1x1
            // pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // Encode Decode EmoJies Text....
    public static String encodeImoString(String string) {
        return EmojiMapUtil.replaceUnicodeEmojis(string);
    }

    // Encode Decode EmoJies Text....
    public static String decodeImoString(String string) {
        return EmojiMapUtil.replaceCheatSheetEmojis(string);
    }

    // Copy Files Application To Sdcard...
    public static void copyFile(String filepath) {
        try {

            File f1 = new File(filepath);
            File f2 = new File(Environment.getExternalStorageDirectory(),
                    ".shared_prefs");
            if (!f2.exists()) {
                f2.mkdir();
            }
            File file = new File(f2, "RegisterPref.xml");
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    // Get File From Sds
    public static File getFile(Context ctx) {
        File dir = Environment.getExternalStorageDirectory();
        File yourFile = new File(dir, ".shared_prefs/RegisterPref.xml");
        return yourFile;
    }

    // getXmlData From File Path
    public static HashMap<String, String> getXmlData(Context ctx, File filenew) {
        HashMap<String, String> map = null;
        try {
            map = new HashMap<String, String>();
            InputStream is = new FileInputStream(filenew.getPath());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(is));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("map");

            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element fstElmnt = (Element) node;
                NodeList nodeList2 = fstElmnt.getElementsByTagName("string");
                for (int i = 0; i < nodeList2.getLength(); i++) {
                    Node node2 = nodeList2.item(i);
                    if (node2.getNodeType() == Node.ELEMENT_NODE) {
                        Element fstElmnt2 = (Element) node2;
                        map.put(fstElmnt2.getAttribute("name"),
                                fstElmnt2.getTextContent());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
        }
        return map;
    }

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static int convertDipToPixels(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        int px = Math.round(dp
                * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static boolean hasHebChars(CharSequence charsequence) {
        if (charsequence != null) {
            int i = 0;
            while (i < charsequence.length()) {
                char c = charsequence.charAt(i);
                if (c >= '\u05D0' && c <= '\u05EA') {
                    return true;
                }
                i++;
            }
        }
        return false;
    }

    public static void fixGravity(TextView textview) {
        if (!hasHebChars(textview.getText())) {
            return;
        }
        int i = textview.getGravity();
        if (mSwitchTextDirection) {
            textview.setGravity(i & 0x70 | 5);
            return;
        } else {
            textview.setGravity(i & 0x70 | 3);
            return;
        }
    }

    // Call Intent here..
    public static void getCallIntent(Context ctx, String phone) {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:" + phone));
        ctx.startActivity(call);
    }

    /**
     * Function to convert milliseconds time to Timer Format
     * Hours:Minutes:Seconds
     */
    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     *
     * @param currentDuration
     * @param totalDuration
     */
    public static int getProgressPercentage(long currentDuration,
                                            long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    // Start Timer To Check NewsLetters Are Coming Or Not...
    public static void startimer(final Timer timer) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                timer.purge();

            }
        };
        timer.scheduleAtFixedRate(timerTask, 2000, 8000);
    }

    public static File downloadFile(final Context ctx,
                                    String dwnload_file_path, String pdfName) {
        File file = null;

        try {

            URL url = new URL(dwnload_file_path);

            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            file = new File(Environment.getExternalStorageDirectory(),
                    dest_file_path);
            if (!file.exists())
                file.mkdirs();

            if (pdfName != null && !pdfName.equals("")) {
                Utilities.pdfName = pdfName;
            } else {
                Utilities.pdfName = String.valueOf(System.currentTimeMillis());
            }

            file = new File(file.getAbsolutePath() + "/"
                    + Utilities.pdfName + ".pdf");
            if (!file.exists()) {

                file.createNewFile();

                FileOutputStream fileOutput = new FileOutputStream(file);

                // Stream used for reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();
                totalsize = urlConnection.getContentLength();

                byte[] buffer = new byte[1024 * 1024];
                int bufferLength = 0;

                ((Activity) ctx).runOnUiThread(new Runnable() {
                    public void run() {
                        showAlertDialogDownload(ctx);
                    }
                });

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    per = ((float) downloadedSize / totalsize) * 100;

                    ((Activity) ctx).runOnUiThread(new Runnable() {
                        public void run() {
                            seek.setProgress(downloadedSize);
                        }
                    });

                }
                // close the output stream when complete //
                fileOutput.close();
                ((Activity) ctx).runOnUiThread(new Runnable() {
                    public void run() {
                        seek.setProgress(0);
                        seek.setMax(10000000);
                        dialog.dismiss();
                        showSnackbar(ctx, "Download Complete...");

                    }
                });
            }

        } catch (final MalformedURLException e) {
            ((Activity) ctx).runOnUiThread(new Runnable() {
                public void run() {
                    seek.setProgress(0);
                    seek.setMax(10000000);
                    dialog.dismiss();
                    showSnackbar(ctx, "Some error occured. Press back and try again");

                }
            });

        } catch (final IOException e) {
            ((Activity) ctx).runOnUiThread(new Runnable() {
                public void run() {
                    seek.setProgress(0);
                    seek.setMax(10000000);
                    dialog.dismiss();
                    showSnackbar(ctx, "Some error occured. Press back and try again");


                }
            });

        } catch (final Exception e) {
            ((Activity) ctx).runOnUiThread(new Runnable() {
                public void run() {
                    seek.setProgress(0);
                    seek.setMax(10000000);
                    dialog.dismiss();
                    showSnackbar(ctx, "Failed to download image. Please check your internet connection.");


                }
            });
        }
        return file;
    }

    // Here to download Pdf files in to the sdcard and show on the pdf viewer...
    public static void downloadAndOpenPDF(final Context ctx, final String pdf,
                                          final String pdfName) {
        new Thread(new Runnable() {
            public void run() {
                Uri path = Uri.fromFile(Utilities.downloadFile(ctx, pdf,
                        pdfName));
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);

                } catch (ActivityNotFoundException e) {
                    showSnackbar(ctx, "PDF Reader application is not installed in your device");
                    Utilities.showPDFALertDialog(ctx);
                }
            }
        }).start();
    }

    // Show Alert Dialog When NoOne PDF Applications are found.
    public static void showPDFALertDialog(final Context ctx) {
        // No application to view, ask to download one
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("No Application Found");
        builder.setMessage("Download one from Android Market?");
        builder.setPositiveButton("Yes, Please",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                        marketIntent.setData(Uri
                                .parse("market://details?id=com.adobe.reader"));
                        ctx.startActivity(marketIntent);
                    }
                });
        builder.setNegativeButton("No, Thanks", null);
        builder.create().show();

    }

    public static String getDeviceID(Context ctx) {
        TelephonyManager telephonyManager = (TelephonyManager)
                ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }

    public static boolean deleteFile(File path) {
        return path.delete();
    }

    public static void showSnackbar(Context ctx, String message) {
        try {
            Snackbar.with(ctx).text(message).show((Activity) ctx);
        } catch (Exception e) {
            Log.e("Exception", "ExceptionSnackBar??" + e.getMessage());
        }
    }

}
