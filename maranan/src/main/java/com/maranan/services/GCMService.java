package com.maranan.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.maranan.LogInActivity;
import com.maranan.gcm.Constant;
import com.maranan.restclient.RestClientGCM;
import com.maranan.utils.Utilities;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class GCMService extends Service {
	private String regId;
	// GCM
	private GoogleCloudMessaging gcm;
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {

			getRegistrationIdGCM();

		} catch (Exception e) {
			Log.e("GCMService", "Exception?? " + e.getMessage());
			stopSelf();
		}
	}

	// getRegister ID GCM
	private void getRegistrationIdGCM() {

		if (TextUtils.isEmpty(regId)) {

			regId = registerGCM();
			if (!regId.equals("")) {

			}
			Utilities.setSharedPref(Utilities.GCM_REG_ID, regId,
					Utilities.GCM_REG_BOOL, false);
			Utilities.gcm_id = Utilities.getSharedPref(Utilities.GCM_REG_ID,
					null);

		} else {
			// Toast.makeText(this, "Already Registered with GCM Server!",
			// Toast.LENGTH_LONG).show();
		}

	}

	// register GCM...
	public String registerGCM() {
		// GCMRegistrar.checkManifest(this);
		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(GCMService.this);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();
			Utilities.setSharedPref(Utilities.GCM_REG_ID, regId,
					Utilities.GCM_REG_BOOL, false);
			Utilities.gcm_id = Utilities.getSharedPref(Utilities.GCM_REG_ID,
					null);

		} else {
			Utilities.setSharedPref(Utilities.GCM_REG_ID, regId,
					Utilities.GCM_REG_BOOL, false);
			Utilities.gcm_id = Utilities.getSharedPref(Utilities.GCM_REG_ID,
					null);
		}
		return regId;
	}

	// Register Gcm In BackGround...
	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(GCMService.this);
					}
					regId = gcm.register(Constant.GOOGLE_PROJECT_ID);
					Utilities.setSharedPref(Utilities.GCM_REG_ID, regId,
							Utilities.GCM_REG_BOOL, false);
					Utilities.gcm_id = Utilities.getSharedPref(
							Utilities.GCM_REG_ID, null);
					msg = "Device registered";
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.e("GCM Services", "Error: " + msg);
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {

				if (msg.equals("Device registered")) {

					putGCMRegIdToServer();

				} else {
					stopSelf();
				}
			}
		}.execute(null, null, null);
	}

	// Get GCM Registration ID here...
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				LogInActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return "";
		}
		return registrationId;
	}

	// Get Application Version Here...
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	// Put GCM Registartion Id To Server...
	private void putGCMRegIdToServer() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		RestClientGCM.get().downloadHomeData3(regId,
				Utilities.getTimeZone(GCMService.this),
				telephonyManager.getDeviceId(), new Callback<Response>() {

					@Override
					public void success(Response response, Response arg1) {

						TypedInput body = response.getBody();
						try {
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(body.in()));
							StringBuilder out = new StringBuilder();
							String newLine = System
									.getProperty("line.separator");
							String line;
							while ((line = reader.readLine()) != null) {
								out.append(line);
								out.append(newLine);
							}
							JSONObject mainObject = new JSONObject(out
									.toString());

							if (mainObject.get("success").equals("true")) {
								Utilities.setSharedPref(Utilities.GCM_REG_ID,
										regId, Utilities.GCM_REG_BOOL, true);
								stopSelf();
							} else {
								Utilities.setSharedPref(Utilities.GCM_REG_ID,
										regId, Utilities.GCM_REG_BOOL, false);
								stopSelf();
							}

						} catch (Exception e) {
							stopSelf();
						}
					}

					@Override
					public void failure(RetrofitError arg0) {
						Log.e("RetrofitError", "RetrofitError?? " + arg0);
						stopSelf();
					}
				});
	}
}
