package com.maranan.gcm;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.maranan.ApplicationSingleton;
import com.maranan.R;
import com.maranan.SplashActivity;
import com.maranan.database.NotificationDB;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.Utilities;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class GCMNotificationIntentService extends IntentService implements Constant{
	public static final String TAG = "GCMNotificationIntentService";
	private static Boolean isNotificationGenerated = false;
	private static GCMNotificationIntentService mContext;
	private Notification notification;
	private Bundle extras;
	private JSONObject jObj = null;
	private JSONObject jObj2; 
	private Bitmap bitmap = null; 
	private SharedPreferences pref;
	private Editor edit;
	private NotificationDB db;
	private SQLiteDatabase sqlite_dbase;
	private Boolean isComingFromNoti = false;
	private NotificationManager mNotificationManagerOne;
	
	public GCMNotificationIntentService() {
		super("GCMNotificationIntentService");
	}
	
	// GCMNotificationIntentService Instance
	public static GCMNotificationIntentService getInstance() {
		return mContext;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = GCMNotificationIntentService.this;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		if (extras != null) {
			if (!extras.isEmpty()) {
				if (extras.size() != 0) {
					if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

						sendNotification("Send error: " + extras.toString());

					} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

						sendNotification("Deleted messages on server: "+ extras.toString());

					} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

						if (extras.getString(MESSAGE_KEY) != null) {
							
							sendNotification(extras.getString(MESSAGE_KEY));
						
						} else if(extras.getString(MESSAGE_KEY2) != null){
							
							sendNotificationForRadioAlert(extras.getString(MESSAGE_KEY2));
						
						} else if(extras.getString(MESSAGE_KEY3) != null){
							
							sendNotificationForNewsLetterAlert(extras.getString(MESSAGE_KEY3));
						
						}else if(extras.getString(MESSAGE_KEY4) != null){
							
							sendNotificationForVideoAlert(extras.getString(MESSAGE_KEY4));
						
						}else if(extras.getString(MESSAGE_KEY5) != null){
							
							sendNotificationForLiveBroadcast(extras.getString(MESSAGE_KEY5));
						
						}
					}
				}

			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.CUPCAKE)
	private void sendNotification(String msg) {
		Log.e("AlarmAlert", "AlarmAlert");
		// Set SharePrefrence Value For Handling Notification...
		pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
		if (pref != null)
		if (pref.contains("Noti1")) {
		if (pref.getBoolean("Noti1", false) == true) {
			edit = pref.edit();
			edit.putBoolean("Noti1", true);
			edit.putBoolean("Noti2", false);
			edit.putBoolean("Noti3", false);
			edit.commit();
		}else{
			edit = pref.edit();
			edit.putBoolean("Noti1", false);
			edit.putBoolean("Noti2", true);
			edit.putBoolean("Noti3", false);
			edit.commit();
		  }
		}
		
		try {
			jObj2 = new JSONObject();
			JSONArray jarray = new JSONArray(msg);
			jObj2 = jarray.getJSONObject(0);
			
			mNotificationManagerOne = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
			Intent myIntent = new Intent(GCMNotificationIntentService.this, SplashActivity.class);
			myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			myIntent.putExtra("value", "AlertAlarm");
			
			// Creates the PendingIntent
			PendingIntent contentIntent = PendingIntent.getActivity(GCMNotificationIntentService.this, NOTIFICATION_ID1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


//			JSONArray jImage =  jObj2.getJSONArray("image");
//
//			if (jImage.length() > 0) {
//				JSONObject jsonObjImg = jImage.getJSONObject(0);
//				jsonObjImg.getString("image" + 1);
//			}

		    // Instantiate a Builder object.
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(GCMNotificationIntentService.this)
				.setSmallIcon(R.drawable.notification_logo)
				.setLargeIcon(loadBitmap(jObj2.getString("image")))
				.setContentTitle(Utilities.decodeImoString(jObj2.getString("messsage")))
				.setContentText(Utilities.decodeImoString(jObj2.getString("description")))
				.setStyle(new NotificationCompat.BigTextStyle().bigText(Utilities.decodeImoString(jObj2.getString("description"))))
				.setPriority(Notification.PRIORITY_HIGH)
				.setWhen(System.currentTimeMillis())
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
				.setAutoCancel(true);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			
			mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
			mBuilder.setContentIntent(contentIntent);
			mNotificationManagerOne.notify(NOTIFICATION_ID1, mBuilder.build());
		
		} else {
			
			notification = mBuilder.getNotification();
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			mNotificationManagerOne.notify(NOTIFICATION_ID1, notification);
		}
		
		// Insert Notification Alerts in the Database...
		db = new NotificationDB(mContext);
		
		if (jObj2.has("alert_id")) 
		if (!db.isAlertIdExists(jObj2.getString("alert_id"))) {
			
			db.insertRecords(sqlite_dbase, putvaluesfromNotification());
		
		}else{
			
			db.updateRecords(sqlite_dbase, putvaluesfromNotification());
		
		}
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			if (jObj2.getString("audio_alert") != null) {
				if (!jObj2.getString("audio_alert").equals("")) {
				    if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
						isComingFromNoti = true;
						Utilities.setSharedPrefForNoti(GCMNotificationIntentService.this, true, jObj2.getString("alert_id"), jObj2.getString("audio_alert"));

					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// Put Values From Notification To DataBase
	private GetterSetter putvaluesfromNotification() {
		
		GetterSetter getset = new GetterSetter();
		try {
			if (jObj2.getString("alert_id") != null) {
				if (!jObj2.getString("alert_id").equals("")) {
					
					getset.setAlert_id(jObj2.getString("alert_id"));
				
				}else{
					getset.setAlert_id("");
				}
			}else{
				getset.setAlert_id("");
			}
			
			if (jObj2.getString("messsage") != null) {
				if (!jObj2.getString("messsage").equals("")) {
					getset.setTitle(jObj2.getString("messsage"));
				
				}else{
					getset.setTitle("");
				}
			}else{
				getset.setTitle("");
			}
			
			if (jObj2.getString("description") != null) {
				if (!jObj2.getString("description").equals("")) {
					
					getset.setDescriptions(jObj2.getString("description"));
				
				}else{
					getset.setDescriptions("");
				}
			}else{
				getset.setDescriptions("");
			}
			
			if (jObj2.getString("image") != null) {
				if (!jObj2.getString("image").equals("")) {
					
					getset.setThumbnailHigh(jObj2.getString("image"));
				
				}else{
					getset.setThumbnailHigh("");
				}
			}else{
				getset.setThumbnailHigh("");
			}

//			JSONArray jImage =  jObj2.getJSONArray("image");
//
//			if (jImage.length() > 0) {
//				int l = 1;
//				ArrayList<String> listImage = new ArrayList<String>();
//				JSONObject jsonObjImg = jImage.getJSONObject(0);
//				for (int j = 0; j < jsonObjImg.length(); j++) {
//					if (!jsonObjImg.getString("image" + l).equals("")) {
//						listImage.add(jsonObjImg.getString("image" + l));
//						getset.setListImage(listImage);
//					} else {
//						getset.setListImage(null);
//					}
//					l++;
//				}
//
//			} else {
//				getset.setListImage(null);
//			}
			
			if (jObj2.getString("phone") != null) {
				if (!jObj2.getString("phone").equals("")) {
					
					getset.setPhone(jObj2.getString("phone"));
				
				}else{
					getset.setPhone("");
				}
			}else{
				getset.setPhone("");
			}
			
			if (jObj2.getString("audio_alert") != null) {
				if (!jObj2.getString("audio_alert").equals("")) {
					
					getset.setRadio_alert(jObj2.getString("audio_alert"));
					getset.setImageResource(R.drawable.play_icon_list);
				
				}else{
					getset.setRadio_alert("");
					getset.setImageResource(-1);
				}
			}else{
				getset.setRadio_alert("");
				getset.setImageResource(-1);
			}
			
			getset.setDate(Utilities.getCurrentDate());
			getset.setTime(Utilities.getCurrentTime());
			getset.setLocation("location");
			getset.setSeekBar("false");
			getset.setProgressBar("false");
			Utilities.setSharedPrefForNoti(mContext, isComingFromNoti, jObj2.getString("alert_id"), "");
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getset;
	}

	// Send Notification For Radio Alert To All the User Of the App For Update
	@SuppressWarnings("deprecation")
	private void sendNotificationForRadioAlert(String msg) {
		Log.e("RadioAlert", "RadioAlert");
		// Set SharePrefrence Value For Handling Notification...
		pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
		if (pref != null)
		if (pref.contains("Noti1")) {
		if (pref.getBoolean("Noti1", false) == true) {
			edit = pref.edit();
			edit.putBoolean("Noti1", true);
			edit.putBoolean("Noti2", false);
			edit.putBoolean("Noti3", false);
			edit.commit();
		} else {
			edit = pref.edit();
			edit.putBoolean("Noti1", false);
			edit.putBoolean("Noti2", false);
			edit.putBoolean("Noti3", true);
			edit.commit();
		   }
	    }
		isNotificationGenerated = true;
		
		try {
		
		jObj = new JSONObject();
		JSONArray jarray = new JSONArray(msg);
		jObj = jarray.getJSONObject(0);
		
		// Here Set The Radio Alert Id To Play in the Video Activity
		Utilities.setRadioAlermID(GCMNotificationIntentService.this, jObj.getString("id"));
		
			
		    mNotificationManagerOne = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Intent alert = new Intent(GCMNotificationIntentService.this, SplashActivity.class);
			alert.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			alert.putExtra("value", "RadioAlarmAlert");
			PendingIntent palert = PendingIntent.getActivity(GCMNotificationIntentService.this, NOTIFICATION_ID2, alert, PendingIntent.FLAG_UPDATE_CURRENT);
			
			 NotificationCompat.Builder builder = new NotificationCompat.Builder(GCMNotificationIntentService.this)
			.setSmallIcon(R.drawable.notification_logo)
			.setContentTitle(Utilities.decodeImoString(jObj.getString("title")))
			.setContentText(Utilities.decodeImoString(jObj.getString("description")))
			.setPriority(Notification.PRIORITY_HIGH)
			.setWhen(System.currentTimeMillis())
			.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
			.setStyle(new NotificationCompat.BigTextStyle().bigText(Utilities.decodeImoString(jObj.getString("description"))))
			.setLargeIcon(loadBitmap(jObj.getString("image")))
			.setAutoCancel(true);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
				builder.setContentIntent(palert);
				mNotificationManagerOne.notify(NOTIFICATION_ID2, builder.build());
			
			} else {
				notification = builder.getNotification();
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				mNotificationManagerOne.notify(NOTIFICATION_ID2, notification);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// Send Notification For News Letter Alert
	@SuppressWarnings("deprecation")
	private void sendNotificationForNewsLetterAlert(String msg) {
		Log.e("NewsLetterAlert", "NewsLetterAlert");
		// Set SharePrefrence Value For Handling Notification...
		pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
		if (pref != null)
		if (pref.contains("Noti1")) {
		if (pref.getBoolean("Noti1", false) == true) {
			edit = pref.edit();
			edit.putBoolean("Noti1", false);
			edit.putBoolean("Noti2", false);
			edit.putBoolean("Noti3", false);
			edit.commit();
		} else {
			edit = pref.edit();
			edit.putBoolean("Noti1", false);
			edit.putBoolean("Noti2", false);
			edit.putBoolean("Noti3", false);
			edit.commit();
		   }
	    }
		isNotificationGenerated = true;
		
		try {
		
		jObj = new JSONObject();
		JSONArray jarray = new JSONArray(msg);
		jObj = jarray.getJSONObject(0);
			
		    mNotificationManagerOne = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Intent alert = new Intent(GCMNotificationIntentService.this, SplashActivity.class);
			alert.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			alert.putExtra("value", "NewsLetterAlert");
			
			// Set Pdf Path And Name in Share Prefrence...
			Utilities.setPDF(GCMNotificationIntentService.this, jObj.getString("pdf_name"), jObj.getString("pdf_path"));
			
			PendingIntent palert = PendingIntent.getActivity(GCMNotificationIntentService.this, NOTIFICATION_ID3, alert, PendingIntent.FLAG_UPDATE_CURRENT);
			
			 NotificationCompat.Builder builder = new NotificationCompat.Builder(GCMNotificationIntentService.this)
			.setSmallIcon(R.drawable.notification_logo)
			.setContentTitle(Utilities.decodeImoString(jObj.getString("title")))
			.setContentText(Utilities.decodeImoString(jObj.getString("description")))
			.setStyle(new NotificationCompat.BigTextStyle().bigText(Utilities.decodeImoString(jObj.getString("description"))))
            .setPriority(Notification.PRIORITY_HIGH)
			.setWhen(System.currentTimeMillis())
			.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
			.setLargeIcon(loadBitmap(jObj.getString("image")))
			.setAutoCancel(true);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
				builder.setContentIntent(palert);
				mNotificationManagerOne.notify(NOTIFICATION_ID3, builder.build());
			
			} else {
				notification = builder.getNotification();
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				mNotificationManagerOne.notify(NOTIFICATION_ID3, notification);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// Send Notification For Video Alert
	@SuppressWarnings("deprecation")
	private void sendNotificationForVideoAlert(String msg) {
		Log.e("VideoAlert", "VideoAlert");
		// Set SharePrefrence Value For Handling Notification...
		pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
		if (pref != null)
		if (pref.contains("Noti1")) {
		if (pref.getBoolean("Noti1", false) == true) {
			edit = pref.edit();
			edit.putBoolean("Noti1", false);
			edit.putBoolean("Noti2", false);
			edit.putBoolean("Noti3", false);
			edit.commit();
		} else {
			edit = pref.edit();
			edit.putBoolean("Noti1", false);
			edit.putBoolean("Noti2", false);
			edit.putBoolean("Noti3", false);
			edit.commit();
		   }
	    }
		isNotificationGenerated = true;
		
		try {
		
		jObj = new JSONObject();
		JSONArray jarray = new JSONArray(msg);
		jObj = jarray.getJSONObject(0);
			
		    mNotificationManagerOne = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			Intent alert = new Intent(GCMNotificationIntentService.this, SplashActivity.class);
			alert.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			alert.putExtra("value", "VideoAlert");
			
			Utilities.setVideoId(GCMNotificationIntentService.this, jObj.getString("video_id"));
			
			PendingIntent palert = PendingIntent.getActivity(GCMNotificationIntentService.this, NOTIFICATION_ID4, alert, PendingIntent.FLAG_UPDATE_CURRENT);
			
			 NotificationCompat.Builder builder = new NotificationCompat.Builder(GCMNotificationIntentService.this)
			.setSmallIcon(R.drawable.notification_logo)
			.setContentTitle(Utilities.decodeImoString(jObj.getString("title")))
			.setContentText(Utilities.decodeImoString(jObj.getString("description")))
			.setPriority(Notification.PRIORITY_HIGH)
			.setWhen(System.currentTimeMillis())
			.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
			.setStyle(new NotificationCompat.BigTextStyle().bigText(Utilities.decodeImoString(jObj.getString("description"))))
			.setLargeIcon(loadBitmap(jObj.getString("image")))
			.setAutoCancel(true);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
				builder.setContentIntent(palert);
				mNotificationManagerOne.notify(NOTIFICATION_ID4, builder.build());
			
			} else {
				notification = builder.getNotification();
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				mNotificationManagerOne.notify(NOTIFICATION_ID4, notification);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// Send Notification For Live Broadcast
		@SuppressWarnings("deprecation")
		private void sendNotificationForLiveBroadcast(String msg) {
			Log.e("LiveBroadcast", "LiveBroadcast");
			// Set SharePrefrence Value For Handling Notification...
			pref = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
			if (pref != null)
			if (pref.contains("Noti1")) {
			if (pref.getBoolean("Noti1", false) == true) {
				edit = pref.edit();
				edit.putBoolean("Noti1", false);
				edit.putBoolean("Noti2", false);
				edit.putBoolean("Noti3", false);
				edit.commit();
			} else {
				edit = pref.edit();
				edit.putBoolean("Noti1", false);
				edit.putBoolean("Noti2", false);
				edit.putBoolean("Noti3", false);
				edit.commit();
			   }
		    }
			isNotificationGenerated = true;
			
			try {
			
			jObj = new JSONObject();
			JSONArray jarray = new JSONArray(msg);
			jObj = jarray.getJSONObject(0);
				
			    mNotificationManagerOne = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				Intent alert = new Intent(GCMNotificationIntentService.this, SplashActivity.class);
				alert.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				alert.putExtra("value", "LiveBroadcast");
				
				Utilities.setVideoId(GCMNotificationIntentService.this, jObj.getString("video_id"));
				
				PendingIntent palert = PendingIntent.getActivity(GCMNotificationIntentService.this, NOTIFICATION_ID5, alert, PendingIntent.FLAG_UPDATE_CURRENT);
				
				 NotificationCompat.Builder builder = new NotificationCompat.Builder(GCMNotificationIntentService.this)
				.setSmallIcon(R.drawable.notification_logo)
				.setContentTitle(Utilities.decodeImoString(jObj.getString("title")))
				.setContentText(Utilities.decodeImoString(jObj.getString("description")))
				.setPriority(Notification.PRIORITY_HIGH)
				.setWhen(System.currentTimeMillis())
				.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(Utilities.decodeImoString(jObj.getString("description"))))
				.setLargeIcon(loadBitmap(jObj.getString("image")))
				.setAutoCancel(true);
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
					builder.setContentIntent(palert);
					mNotificationManagerOne.notify(NOTIFICATION_ID5, builder.build());
				
				} else {
					notification = builder.getNotification();
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					mNotificationManagerOne.notify(NOTIFICATION_ID5, notification);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	
	// Here To Check Is Coming From Notification Or Not...
	public Boolean isComingFromNotification(){
		return isComingFromNoti;
	}
	
	// Check The Notification Is Genrated Or Not...
	public Boolean isNotificationGenerate() {
		return isNotificationGenerated;
	}
	
	// You Cancel Notification ForceFully With This...
	public void notificationCancelOne() {
		isNotificationGenerated = false;
		mNotificationManagerOne.cancel(NOTIFICATION_ID1);
		//Log.e("mNotificationManagerOne", "mNotificationManagerOne?? "+mNotificationManagerOne);
	}

	// You Cancel Notification ForceFully With This...
	public void notificationCancelTwo() {
		isNotificationGenerated = false;
		mNotificationManagerOne.cancel(NOTIFICATION_ID2);
		//Log.e("mNotificationManagerTwo", "mNotificationManagerTwo?? "+mNotificationManagerOne);
	}

	// You Cancel Notification ForceFully With This...
	public void notificationCancelThree() {
		isNotificationGenerated = false;
		mNotificationManagerOne.cancel(NOTIFICATION_ID3);
		//Log.e("mNotificationManagerThree", "mNotificationManagerThree?? "+mNotificationManagerOne);
	}
	
	// You Cancel Notification ForceFully With This...
	public void notificationCancelFour() {
		isNotificationGenerated = false;
		mNotificationManagerOne.cancel(NOTIFICATION_ID4);
		//Log.e("mNotificationManagerFour", "mNotificationManagerFour?? "+mNotificationManagerOne);
	}
	
	public void notificationCancelFive() {
		isNotificationGenerated = false;
		mNotificationManagerOne.cancel(NOTIFICATION_ID5);
	}
	
	public Notification getnotification() {
		return notification;
	}
	
	// Get Notification Manager...
	public NotificationManager getNotiManager(){
		return mNotificationManagerOne;
	}
	
	// Load Bitmap Using Picaso Library to Get Image From Server To Showing on
	// the Notification...
	private Bitmap loadBitmap(String url) {
		try {
			if (url != null) 
			if (!url.equals("")) {
				bitmap = Picasso.with(this).load(url).get();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	
}
