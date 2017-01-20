package com.maranan.gcm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver implements Constant{

	private Bundle extras;
	private String message, message2, message3, message4, message5;

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, final Intent intent) {
		ComponentName comp = new ComponentName(context.getPackageName(),
				GCMNotificationIntentService.class.getName());
		extras = intent.getExtras();
		startWakefulService(context, (intent.setComponent(comp)));

		message = extras.getString(MESSAGE_KEY);
		Log.e("message", "?? "+message);
		
		message2 = extras.getString(MESSAGE_KEY2);
		Log.e("message2", "?? "+message2);
		
		message3 = extras.getString(MESSAGE_KEY3);
		Log.e("message3", "?? "+message3);
		
		message4 = extras.getString(MESSAGE_KEY4);
		Log.e("message4", "?? "+message4);
		
		message5 = extras.getString(MESSAGE_KEY5);
		Log.e("message5", "?? "+message5);
		
		if (message != null) {
			setResultCode(Activity.RESULT_OK);
			message = null;
		}

		if (message2 != null) {
			setResultCode(Activity.RESULT_OK);
			message2 = null;
		}
		
		if (message3 != null) {
			setResultCode(Activity.RESULT_OK);
			message3 = null;
		}
		
		if (message4 != null) {
			setResultCode(Activity.RESULT_OK);
			message4 = null;
		}
		
		if (message5 != null) {
			setResultCode(Activity.RESULT_OK);
			message5 = null;
		}
	}

	public static String bundle2string(Bundle bundle) {
		String string = "Bundle{";
		for (String key : bundle.keySet()) {
			string += " " + key + " => " + bundle.get(key) + ";";
		}
		string += " }Bundle";
		return string;
	}

}
