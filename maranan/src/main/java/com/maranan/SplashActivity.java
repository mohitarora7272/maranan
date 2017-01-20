package com.maranan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.AnimateGifMode;
import com.maranan.gcm.Constant;
import com.maranan.gcm.GCMNotificationIntentService;
import com.maranan.notifications.MyNotification;
import com.maranan.services.GCMService;
import com.maranan.services.NewsletterService;
import com.maranan.utils.Config;
import com.maranan.utils.ConnectionDetector;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.MarshMallowPermission;
import com.maranan.utils.MediaPlayerNoti;
import com.maranan.utils.ServiceHandler;
import com.maranan.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity implements Constant {
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private final int GET = 1;
    private ArrayList<GetterSetter> list_youtubePlayListID;
    private ArrayList<GetterSetter> list_youtubeVideos;
    private ArrayList<HashMap<String, String>> list_youtubeVidID;
    private GetterSetter getset;
    private HashMap<String, String> hashMap;
    public ImageView imageView;
    private String msg = "";
    public SharedPreferences pref;
    public Editor edit;
    private Bundle extras = null;
    private MediaPlayerAction actionMediaPlayer;
    private Timer time;
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            // Get Message From Notification To Differentiate The Notification.
            extras = getIntent().getExtras();

            if (extras != null) {
                if (extras.containsKey("value")) {

                    if (extras.getString("value").equals("MediaPlayer")) {
                        msg = extras.getString("value");
                        pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
                        edit = pref.edit();
                        edit.putBoolean("Noti1", true);
                        edit.putBoolean("Noti2", false);
                        edit.putBoolean("Noti3", false);
                        edit.commit();

                        // Here To set false prefrence when the notification are not click...
                        Utilities.setSharedPrefForNoti(this, false, "", "");
                        if (MyNotification.getInstance() != null) {
                            MyNotification.getInstance().notificationCancel();
                        }

                    } else if (extras.getString("value").equals("AlertAlarm")) {
                        msg = extras.getString("value");
                        extras = null;
                        pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
                        edit = pref.edit();
                        edit.putBoolean("Noti1", false);
                        edit.putBoolean("Noti2", true);
                        edit.putBoolean("Noti3", false);
                        edit.commit();

                        Utilities.setSharedPrefForNoti(this, true, Utilities.getAlertId(this), Utilities.getAlertURL(this));

                        if (GCMNotificationIntentService.getInstance() != null) {
                            GCMNotificationIntentService.getInstance().notificationCancelOne();
                        }

                    } else if (extras.getString("value").equals("RadioAlarmAlert")) {
                        msg = extras.getString("value");
                        pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
                        edit = pref.edit();
                        edit.putBoolean("Noti1", false);
                        edit.putBoolean("Noti2", false);
                        edit.putBoolean("Noti3", true);
                        edit.commit();

                        if (GCMNotificationIntentService.getInstance() != null) {
                            GCMNotificationIntentService.getInstance().notificationCancelTwo();
                        }

                        // Here To set false prefrence when the notification are not click...
                        Utilities.setSharedPrefForNoti(this, false, "", "");

                    } else if (extras.getString("value").equals("NewsLetterAlert")) {
                        msg = extras.getString("value");
                        pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
                        edit = pref.edit();
                        edit.putBoolean("Noti1", false);
                        edit.putBoolean("Noti2", false);
                        edit.putBoolean("Noti3", false);
                        edit.commit();

                        if (GCMNotificationIntentService.getInstance() != null) {
                            GCMNotificationIntentService.getInstance().notificationCancelThree();
                        }

                        // Here To set false prefrence when the notification are not click...
                        Utilities.setSharedPrefForNoti(this, false, "", "");

                    } else if (extras.getString("value").equals("VideoAlert")) {
                        msg = extras.getString("value");
                        pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
                        edit = pref.edit();
                        edit.putBoolean("Noti1", false);
                        edit.putBoolean("Noti2", false);
                        edit.putBoolean("Noti3", false);
                        edit.commit();

                        if (GCMNotificationIntentService.getInstance() != null) {
                            GCMNotificationIntentService.getInstance().notificationCancelFour();
                        }

                        // Here To set false prefrence when the notification are not click...
                        Utilities.setSharedPrefForNoti(this, false, "", "");

                    } else if (extras.getString("value").equals("LiveBroadcast")) {
                        msg = extras.getString("value");
                        pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
                        edit = pref.edit();
                        edit.putBoolean("Noti1", false);
                        edit.putBoolean("Noti2", false);
                        edit.putBoolean("Noti3", false);
                        edit.commit();

                        if (GCMNotificationIntentService.getInstance() != null) {
                            GCMNotificationIntentService.getInstance().notificationCancelFive();
                        }

                        // Here To set false prefrence when the notification are not click...
                        Utilities.setSharedPrefForNoti(this, false, "", "");
                    }

                } else {

                    // Here To set false prefrence when the notification are not click...
                    setPrefrenceMode();
                    //Log.e("else", "elseUP");
                }
            } else {

                // Here To set false prefrence when the notification are not click...
                setPrefrenceMode();
                //Log.e("else", "elseD");
            }

            // Handle UnCaughtException Exception Handler
//			Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(
//					this));

            //set content view to load UI...
            setContentView(R.layout.activity_splash);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            imageView = (ImageView) findViewById(R.id.gif1);
            loadSplashFile();


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Splash", "Exception?? " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpResultsForServices();
    }

    /* Set Up Results For Services*/
    private void setUpResultsForServices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (marshMallowPermission.checkPermissionForAccessWifiState() == true
                    && marshMallowPermission.checkPermissionForInternet() == true
                    && marshMallowPermission.checkPermissionForReadExternalStorage() == true
                    && marshMallowPermission.checkPermissionForACCESS_NETWORK_STATE() == true
                    && marshMallowPermission.checkPermissionForGET_ACCOUNTS() == true
                    && marshMallowPermission.checkPermissionForWAKE_LOCK() == true
                    && marshMallowPermission.checkPermissionForRECORD_AUDIO() == true
                    && marshMallowPermission.checkPermissionForWriteExternalStorage() == true
                    && marshMallowPermission.checkPermissionForREAD_PHONE_STATE() == true
                    && marshMallowPermission.checkPermissionForCALL_PHONE() == true
                    && marshMallowPermission.checkPermissionForRECEIVE_BOOT_COMPLETED() == true
                    && marshMallowPermission.checkPermissionForVIBRATE() == true) {

                cd = new ConnectionDetector(getApplicationContext());
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    // Get Share Prefrence For First Time To Check email and signinwith values are exists or not for login...
                    getSharePrefrenceFromSdcard();

                    // Get Registration Id From GCM
                    Utilities.sharedPreferences = getSharedPreferences(Utilities.MyPREFERENCES, Context.MODE_PRIVATE);

                    if (Utilities.sharedPreferences != null) {

                        if (Utilities.getSharedPref(Utilities.GCM_REG_ID, null) == null) {

                            startService(new Intent(this, GCMService.class));

                        } else if (Utilities.getSharedPref(Utilities.GCM_REG_ID, null).equals("")) {

                            startService(new Intent(this, GCMService.class));

                        }
                    }

                    // Start Services to get Dates For checking and match's on the
                    // newsletter view...
                    startService(new Intent(this, NewsletterService.class));

                    list_youtubeVideos = new ArrayList<GetterSetter>();
                    list_youtubePlayListID = new ArrayList<GetterSetter>();
                    new GetYouTubeChannelPlaylist().execute();

                } else {
                    Utilities.showAlertDialog(SplashActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                }
            } else {
                marshMallowPermission.commonPermissionForApp();
            }
        } else {

            cd = new ConnectionDetector(getApplicationContext());
            isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {
                // Get Share Prefrence For First Time To Check email and signinwith values are exists or not for login...
                getSharePrefrenceFromSdcard();

                // Get Registration Id From GCM
                Utilities.sharedPreferences = getSharedPreferences(Utilities.MyPREFERENCES, Context.MODE_PRIVATE);

                if (Utilities.sharedPreferences != null) {

                    if (Utilities.getSharedPref(Utilities.GCM_REG_ID, null) == null) {

                        startService(new Intent(this, GCMService.class));

                    } else if (Utilities.getSharedPref(Utilities.GCM_REG_ID, null).equals("")) {

                        startService(new Intent(this, GCMService.class));

                    }
                }

                // Start Services to get Dates For checking and match's on the
                // newsletter view...
                startService(new Intent(this, NewsletterService.class));

                list_youtubeVideos = new ArrayList<GetterSetter>();
                list_youtubePlayListID = new ArrayList<GetterSetter>();
                new GetYouTubeChannelPlaylist().execute();

            } else {
                Utilities.showAlertDialog(SplashActivity.this,
                        "Internet Connection Error",
                        "Please connect to working Internet connection", false);
            }
        }
    }

    // Set Prefrence Mode To Check When Enter Through Icon Not From Notification Click...
    private void setPrefrenceMode() {
        Utilities.setSharedPrefForNoti(this, false, "", "");

        pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
        if (pref != null) {
            if (pref.contains("Noti1") && pref.contains("Noti2") && pref.contains("Noti3")) {
                if (pref.getBoolean("Noti1", false) == true) {

                    msg = "MediaPlayer";

                    if (MyNotification.getInstance() != null) {
                        MyNotification.getInstance().notificationCancel();
                    }

                }

                if (pref.getBoolean("Noti2", false) == true) {

                    msg = "AlertAlarm";

                    if (GCMNotificationIntentService.getInstance() != null) {
                        GCMNotificationIntentService.getInstance().notificationCancelOne();
                    }

                }

                if (pref.getBoolean("Noti3", false) == true) {

                    msg = "RadioAlarmAlert";

                    if (GCMNotificationIntentService.getInstance() != null) {
                        GCMNotificationIntentService.getInstance().notificationCancelTwo();
                    }

                }

            }
        }
    }

    // Play Audio Alert When Alert Notification Comes With Sound Alert...
    @SuppressWarnings("unused")
    private void playAudioAlert() {
        if (Utilities.isComingFromNoti(this)) {
            if (actionMediaPlayer != null) {
                actionMediaPlayer = null;
            }
            actionMediaPlayer = new MediaPlayerAction(this, Utilities.getAlertURL(this));
        }
    }

    // Load Gif Image Using ION Library And AndroidAsync jar
    private void loadSplashFile() {
        Ion.with(imageView).animateGif(AnimateGifMode.ANIMATE)
                .load("file:///android_asset/splash_gif_newone.gif")
                .setCallback(new FutureCallback<ImageView>() {

                    @SuppressLint("NewApi")
                    @Override
                    public void onCompleted(Exception arg0, ImageView arg1) {

                        imageView.getViewTreeObserver().addOnPreDrawListener(
                                new ViewTreeObserver.OnPreDrawListener() {
                                    public boolean onPreDraw() {

                                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                                        imageView.setScaleType(ScaleType.FIT_XY);

                                        return true;
                                    }
                                });
                    }
                });

    }

    // Get getSharePrefrenceFromSdcard
    private void getSharePrefrenceFromSdcard() {
        SharedPreferences pref = getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
        if (pref != null) {
            if (!pref.contains("email")) {
                if (Utilities.getFile(this).exists()) {
                    HashMap<String, String> map = Utilities.getXmlData(this, Utilities.getFile(this));
                    Utilities.setSharedPrefRegistrationValues(this, "email", map.get("email"), "signinwith", map.get("signinwith"));
                }
            }
        }

//		Log.e("path","path?? "+Utilities.getFile(this));
//		Log.e("delete","delete?? "+Utilities.deleteFile(Utilities.getFile(this)));
//		SharedPreferences pref2 = getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
//		Log.e("Email", "?? "+pref2.getString("email", null));
//		Utilities.setSharedPrefRegistrationValues(this, "email", null, "signinwith", null);
    }

    // Get You Tube Channel Playlists...
    class GetYouTubeChannelPlaylist extends AsyncTask<Void, Void, ArrayList<GetterSetter>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<GetterSetter> doInBackground(Void... arg0) {

            ServiceHandler handle = new ServiceHandler();
            String response = handle.makeServiceCall(Config.ROOT_SERVER_CLIENT + Config.GET_YOUTUBE_CHANNEL, GET);

            if (response != null) {
                try {
                    //JSONObject jsonObject = new JSONObject(response);
                    //JSONArray jsonArrayItem = jsonObject.getJSONArray("value");

                    JSONArray jsonArrayItem = new JSONArray(response);

                    if (jsonArrayItem.length() > 0) {
                        for (int j = 0; j < jsonArrayItem.length(); j++) {

                            list_youtubeVidID = new ArrayList<HashMap<String, String>>();
                            JSONObject jsonObj = jsonArrayItem.getJSONObject(j);
                            JSONArray jsonArrayItemInner = jsonObj.getJSONArray("items");

                            for (int i = 0; i < jsonArrayItemInner.length(); i++) {
                                hashMap = new HashMap<String, String>();
                                getset = new GetterSetter();

                                if (jsonObj.has("playlist_id"))
                                    hashMap.put("Playlist_ID", jsonObj.getString("playlist_id"));

                                if (jsonObj.has("itemcounts"))
                                    hashMap.put("TotalVideos", String.valueOf(jsonArrayItemInner.length()));

                                if (jsonObj.has("status"))
                                    hashMap.put("Status", jsonObj.getString("status"));

                                if (jsonObj.has("sequence_status"))
                                    hashMap.put("SequenceStatus", jsonObj.getString("sequence_status"));

                                // Get Inner JsonObject For getting Inner Items...
                                JSONObject jsonObjItems = jsonArrayItemInner.getJSONObject(i);

                                if (jsonObjItems.has("title"))
                                    hashMap.put("Title", jsonObjItems.getString("title"));

                                if (jsonObjItems.has("description"))
                                    hashMap.put("Description", jsonObjItems.getString("description"));

                                if (jsonObjItems.has("video_id"))
                                    hashMap.put("VideoId", jsonObjItems.getString("video_id"));

                                if (jsonObjItems.has("image"))
                                    hashMap.put("Url", jsonObjItems.getString("image"));

                                if (jsonObjItems.has("unique_video_id"))
                                    hashMap.put("Unique_Video_Id", jsonObjItems.getString("unique_video_id"));

                                list_youtubeVidID.add(hashMap);
                                getset.setList(list_youtubeVidID);
                            }

                            getset.setPlayListId(jsonObj.getString("playlist_id"));
                            list_youtubePlayListID.add(j, getset);
                            list_youtubeVideos.add(getset);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Utilities.showAlertDialog(SplashActivity.this,
                                "Internet Connection Error",
                                "Your internet connection is too slow...",
                                false);
                    }
                });
            }
            return list_youtubeVideos;
        }

        @Override
        protected void onPostExecute(ArrayList<GetterSetter> result) {
            super.onPostExecute(result);
            if (result != null) {
                if (result.size() > 0) {

                    System.gc();
                    pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
                    if (pref != null)
                        if (pref.contains("Noti1") && pref.contains("Noti2") && pref.contains("Noti3")) {
                            edit = pref.edit();
                            edit.putBoolean("Noti1", false);
                            edit.putBoolean("Noti2", false);
                            edit.putBoolean("Noti3", false);
                            edit.commit();
                        }

                    // Set Youtube PlayList Data Here...
                    ((ApplicationSingleton) getApplicationContext()).dataContainer.setYoutubeList(list_youtubeVideos);
                    ((ApplicationSingleton) getApplicationContext()).dataContainer.setPlaylist(list_youtubePlayListID);

                    checkPdfStackFull();


                } else {

                    checkPdfStackFull();
                }
            }
        }

        /* Check Is PDF Stack Full Or Null */
        private void checkPdfStackFull() {
            // Set Timer for check Newsletters from DateServices Class
            // when you tube playlist id are not available to publish by Admin
            final TimerTask timer = new TimerTask() {

                @Override
                public void run() {

                    if (NewsletterService.getInstance().getIsPDfStackFull() == true) {

                        // Pass Next Activity when playlist are not available and
                        // Newsletter's are available to publish by Admin.
                        time.cancel();
                        time.purge();
                        passNextActivity();

                    } else if (NewsletterService.getInstance().getIsPDfStackNull() == true) {
                        // Pass Next Activity when playlist and
                        // Newsletter's both are not available to publish by Admin.
                        time.cancel();
                        time.purge();
                        passNextActivity();
                    }
                }
            };

            time = new Timer();
            time.scheduleAtFixedRate(timer, 1000, 1000);

        }
    }

    // Pass Next Video Activity...
    private void passNextActivity() {
        Intent videoIntent = new Intent(SplashActivity.this, VideoActivity.class);
        videoIntent.putExtra("Message", msg);
        startActivity(videoIntent);
        finish();
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
    }

    // Class To Handle Media Player Action During Notification...
    class MediaPlayerAction extends MediaPlayerNoti {

        public MediaPlayerAction(Context context, String url) {
            super(context, url);
        }
    }

    // Request Call Back Method To check permission is granted by user or not for MarshMallow...
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case COMMON_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    setUpResultsForServices();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
