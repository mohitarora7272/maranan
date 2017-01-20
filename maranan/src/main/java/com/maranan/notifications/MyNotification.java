package com.maranan.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.maranan.R;
import com.maranan.SplashActivity;
import com.maranan.utils.Utilities;

public class MyNotification extends Notification {
	
	private Context ctx;
	private static MyNotification mContext;
	private static NotificationManager mNotificationManager;
	private static Notification notification;
	private static NotificationCompat.Builder builder2;
	private static PendingIntent pendingSwitchIntent;
	private PendingIntent pendingdeleteIntent;
	private PendingIntent pendingCallIntent;
	private String[] str;
	private static Boolean isPlaying = true;
	private static Boolean isNotificationGenerated = false;
	private Bitmap bitmap;
	private SharedPreferences pref;
	private Editor edit;

	// MyNotification Instance
	public static MyNotification getInstance() {
		return mContext;
	}

	@SuppressLint("NewApi")
	public MyNotification(Context ctx, final String[] str) {
		super();
		this.ctx = ctx;
		this.str = str;
		mContext = MyNotification.this;
		generateNotification(ctx, isPlaying, str);
	}
	
	@SuppressWarnings("deprecation")
	private void generateNotification(Context ctx, Boolean isPlaying, String[] str) {
		// Set SharePrefrence Value For Handling Notification...
		pref = ctx.getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
		edit = pref.edit();
		edit.putBoolean("Noti1", true);
		edit.putBoolean("Noti2", false);
		edit.putBoolean("Noti3", false);
		edit.commit();
		
		isNotificationGenerated = true;
		mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent radio2 = new Intent(ctx, SplashActivity.class);
		radio2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		radio2.putExtra("value", "MediaPlayer");
		PendingIntent pRadio2 = PendingIntent.getActivity(ctx, 0, radio2, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Intent switchIntent = new Intent("com.example.app.ACTION_PLAY");
		pendingSwitchIntent = PendingIntent.getBroadcast(ctx, 100, switchIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent deleteIntent = new Intent("com.example.app.ACTION_DELETE");
		pendingdeleteIntent = PendingIntent.getBroadcast(ctx, 100, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Intent callintent = new Intent("com.example.app.CALL");
		pendingCallIntent = PendingIntent.getBroadcast(ctx, 100, callintent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		bitmap = Utilities.StringToBitMap(str[3]);
		builder2 = new NotificationCompat.Builder(ctx);
		builder2.setSmallIcon(R.drawable.notification_logo);
		builder2.setLargeIcon(bitmap);
		builder2.setContentTitle(Utilities.decodeImoString(str[0]));
		builder2.setContentText(Utilities.decodeImoString(str[1]));
		builder2.setContentIntent(pRadio2);
		builder2.setPriority(Notification.PRIORITY_MAX); 
		builder2.setWhen(System.currentTimeMillis());
		builder2.setStyle(new NotificationCompat.BigTextStyle().bigText(Utilities.decodeImoString(str[1])));
		builder2.addAction(R.drawable.call_icon, "", pendingCallIntent);
		
		if (isPlaying) {
			
			builder2.addAction(R.drawable.icon_pause_noti, "Pause", pendingSwitchIntent);
		
		}else{
			
			builder2.addAction(R.drawable.play_arrow_white_noti, "Play", pendingSwitchIntent);
		}
		
		builder2.addAction(R.drawable.icon_cross, "Cancel", pendingdeleteIntent);
		builder2.setAutoCancel(true);
		builder2.setOngoing(true);
		builder2.build();
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mNotificationManager.notify(548853, builder2.build());
		
		} else {
			notification = builder2.getNotification();
			notification.flags |= Notification.FLAG_NO_CLEAR;
			mNotificationManager.notify(548853, notification);
		}
	}
	
	// Get Notification For Change Action On Notification For Play And Pause...
	public void getNotificationForChange(Boolean isPlaying){
		generateNotification(ctx, isPlaying, str);
	}

	public RemoteViews getRemoteView() {
		return contentView;
	}

	public Notification getnotification() {
		return notification;
	}

	public NotificationManager getnotificationmanager() {
		return mNotificationManager;
	}

	public NotificationCompat.Builder getnotificationbuilder() {
		return builder2;
	}

	public void notificationCancel() {
		isNotificationGenerated = false;
		mNotificationManager.cancel(548853);
	}

	public Boolean isNotificationGenerate() {
		return isNotificationGenerated;
	}
	
	public String getPlayId(){
		return str[5];
	}
}
