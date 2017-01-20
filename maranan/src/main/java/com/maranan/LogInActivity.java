package com.maranan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.maranan.gcm.Constant;
import com.maranan.utils.Config;
import com.maranan.utils.ConnectionDetector;
import com.maranan.utils.Utilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LogInActivity extends Activity implements OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener {
	private static LogInActivity mContext;
	private DrawerLayout drawer_layout_login;
	private RelativeLayout realtive_login;
	private ActionBarDrawerToggle mDrawerToggle;
	private ConnectionDetector cd;
	private Boolean isInternetPresent = false;
	private Button btn_fb, btn_google, btn_login;
	private EditText edt_email, edt_password, edt_password_forgot;
	private TextView tv_logOut;
	private ImageView img_newuser, img_forgotPass, img_logout;
	private UiLifecycleHelper uiHelper;
	private SharedPreferences pref;
	private Editor editor;
	public String id, firstname, email, birthday, username, link, gender,
			location, personPhotoUrl, personGooglePlusProfile, verified,
			password;
	private String regId, lastname, signinwith;
	public URL imgUrl;
	// ProgressBar bar;
	private ProgressBar pDialog;
	public static int REQUEST_CODE_REGISTER = 100;
	private static final int RC_SIGN_IN = 0;

	// Google client to interact with Google API
	public static GoogleApiClient mGoogleApiClient;

	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;
	// Profile PIC image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;

	// GCM
	private GoogleCloudMessaging gcm;
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";

	// LogInActivity Instance
	public static LogInActivity getInstance() {
		return mContext;
	}

	// onCreate
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);
		// Handle UnCaughtException Exception Handler
//		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(
//				this));
		setContentView(R.layout.activity_login);
		mContext = LogInActivity.this;
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();
		drawer_layout_login = (DrawerLayout) findViewById(R.id.drawer_layout_login);
		drawer_layout_login
				.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		img_logout = (ImageView) findViewById(R.id.img_logout);
		img_logout.setOnClickListener(this);
		tv_logOut = (TextView) findViewById(R.id.tv_logOut);
		tv_logOut.setOnClickListener(this);
		realtive_login = (RelativeLayout) findViewById(R.id.realtive_login);
		btn_fb = (Button) findViewById(R.id.btn_Facebook);
		btn_fb.setOnClickListener(this);
		btn_google = (Button) findViewById(R.id.btn_GooglePlus);
		btn_google.setOnClickListener(this);
		btn_login = (Button) findViewById(R.id.btn_Login);
		btn_login.setOnClickListener(this);
		edt_email = (EditText) findViewById(R.id.edt_EmailAddress);
		edt_email.addTextChangedListener(mTextEditorWatcher);
		edt_password = (EditText) findViewById(R.id.edt_Password);
		edt_password.addTextChangedListener(mTextEditorWatcher);
		img_newuser = (ImageView) findViewById(R.id.img_newuser);
		img_newuser.setOnClickListener(this);
		img_forgotPass = (ImageView) findViewById(R.id.img_forgotPass);
		img_forgotPass.setOnClickListener(this);
		pDialog = (ProgressBar) findViewById(R.id.progressBar);
		pDialog.setVisibility(View.GONE);

		mDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout_login,
				R.drawable.maranan_icon, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {

			}

			public void onDrawerOpened(View drawerView) {

			}
		};
		drawer_layout_login.setDrawerListener(mDrawerToggle);

		Utilities.sharedPreferences = getSharedPreferences(
				Utilities.MyPREFERENCES, Context.MODE_PRIVATE);

		if (isInternetPresent) {

			Utilities.sharedPreferences = getSharedPreferences(
					Utilities.MyPREFERENCES, Context.MODE_PRIVATE);

			if (Utilities.sharedPreferences != null) {

				if (Utilities.getSharedPref(Utilities.GCM_REG_ID, null) == null) {

					getRegistrationIdGCM();

				} else if (Utilities.getSharedPref(Utilities.GCM_REG_ID, null)
						.equals("")) {

					getRegistrationIdGCM();

				}
			}

		} else {
			Utilities.showAlertDialog(LogInActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
		}

		// Initializing google plus API client
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
	}

	// onClick
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_Facebook) {
			if (isInternetPresent) {

				loginTOFb();

			} else {
				Utilities.showAlertDialog(LogInActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
			}
		} else if (id == R.id.btn_GooglePlus) {
			if (isInternetPresent) {

				signInWithGplus();

			} else {
				Utilities.showAlertDialog(LogInActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
			}
		} else if (id == R.id.btn_Login) {
			if (isInternetPresent) {
				if (edt_email.getText().length() > 0) {
					if (Utilities.isEmailValid(edt_email.getText().toString())) {
						if (edt_password.getText().length() > 0) {

							email = edt_email.getText().toString();
							password = edt_password.getText().toString();
							signinwith = "App";
							// startActivity();
							new LogIn().execute(email, password, signinwith,
									Utilities.getSharedPref(
											Utilities.GCM_REG_ID, null));

						} else {

							Utilities.showSnackbar(mContext, "Enter Password");

							edt_password
									.setBackgroundResource(R.drawable.greybg_red);
						}
					} else {

						Utilities.showSnackbar(mContext, "Enter Valid Email");

						edt_email.setBackgroundResource(R.drawable.greybg_red);
					}

				} else {
					Utilities.showSnackbar(mContext, "Enter Email");

					edt_email.setBackgroundResource(R.drawable.greybg_red);
				}
			} else {
				Utilities.showAlertDialog(LogInActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
			}
		} else if (id == R.id.img_newuser) {
			if (isInternetPresent) {
				Intent register = new Intent(LogInActivity.this,
						RegistrationActivity.class);
				startActivityForResult(register, REQUEST_CODE_REGISTER);
			} else {
				Utilities.showAlertDialog(LogInActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
			}
		} else if (id == R.id.img_forgotPass) {
			if (isInternetPresent) {
				showCustomDialog();
			} else {
				Utilities.showAlertDialog(LogInActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
			}
		} else if (id == R.id.img_logout) {
			if (isInternetPresent) {
				Utilities.logout(LogInActivity.this);
				drawer_layout_login.closeDrawer(realtive_login);
			} else {
				Utilities.showAlertDialog(LogInActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
			}
		} else if (id == R.id.tv_logOut) {
			if (isInternetPresent) {
				Utilities.logout(LogInActivity.this);
				drawer_layout_login.closeDrawer(realtive_login);
			} else {
				Utilities.showAlertDialog(LogInActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
			}
		}

	}

	// Show dialog for forgot password
	private void showCustomDialog() {
		// Create custom dialog object
		final Dialog dialog = new Dialog(LogInActivity.this);
		// Include dialog.xml file
		dialog.setContentView(R.layout.custom_dialog);
		// Set dialog title
		dialog.setTitle("Enter Email...");
		dialog.setCanceledOnTouchOutside(false);

		// set values for custom dialog components - text, image and button
		edt_password_forgot = (EditText) dialog
				.findViewById(R.id.edt_forgotPass);
		dialog.show();

		Button declineButton = (Button) dialog.findViewById(R.id.btn_send);
		declineButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edt_password_forgot.getText().length() > 0) {
					email = edt_password_forgot.getText().toString();
					signinwith = "App";
					new ForgotPassword().execute(email, signinwith);
					dialog.dismiss();
				} else {
					Utilities.showSnackbar(mContext, "Enter Email");
				}
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// LogIn With FaceBook
	protected void loginTOFb() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.maranan", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

		// openActiveSession
		openActiveSession(this, true, new Session.StatusCallback() {
			@Override
			public void call(final Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					pDialog.setVisibility(View.VISIBLE);
					/*
					 * dialog = new ProgressDialog(RegistrationActivity.this);
					 * dialog.setMessage("Loading...");
					 * dialog.setCanceledOnTouchOutside(false); dialog.show();
					 */
					Request.newMeRequest(session,
							new Request.GraphUserCallback() {
								@Override
								public void onCompleted(final GraphUser user,
										Response response) {
									if (user != null) {
										// Log.e("Response>>>", ""+response);
										firstname = user.getFirstName();
										lastname = user.getLastName();
										id = user.getId();

										try {
											// Log.e("Response>>>",
											// ""+user.getInnerJSONObject());

											if (user.getInnerJSONObject().has(
													"email"))
												if (user.getInnerJSONObject()
														.getString("email") != null) {
													email = user
															.getInnerJSONObject()
															.getString("email");
													// Log.e("emailFb>>>", ""
													// + email);
												} else {
													email = "";
												}

											if (user.getInnerJSONObject().has(
													"birthday"))
												if (user.getInnerJSONObject()
														.getString("birthday") != null) {
													birthday = user
															.getInnerJSONObject()
															.getString(
																	"birthday");
													// Log.e("birthdayFb>>>", ""
													// + birthday);
												} else {
													birthday = "";
												}

											if (user.getInnerJSONObject().has(
													"link"))
												if (user.getInnerJSONObject()
														.getString("link") != null) {
													link = user
															.getInnerJSONObject()
															.getString("link");
													// Log.e("linkFb>>>", ""
													// + link);
												} else {
													link = "";
												}

											if (user.getInnerJSONObject().has(
													"gender"))
												if (user.getInnerJSONObject()
														.getString("gender") != null) {
													gender = user
															.getInnerJSONObject()
															.getString("gender");

												} else {
													gender = "";
												}

											if (user.getInnerJSONObject().has(
													"verified"))
												if (user.getInnerJSONObject()
														.getString("verified") != null) {
													verified = user
															.getInnerJSONObject()
															.getString(
																	"verified");

												} else {
													verified = "";
												}

											if (user.getInnerJSONObject().has(
													"location"))
												if (user.getInnerJSONObject()
														.getJSONObject(
																"location") != null) {
													JSONObject jsoLoc = user
															.getInnerJSONObject()
															.getJSONObject(
																	"location");
													location = jsoLoc
															.getString("name");

												} else {
													location = "";
												}

											try {
												imgUrl = new URL(
														"https://graph.facebook.com/"
																+ user.getId()
																+ "/picture?type=large");

											} catch (MalformedURLException e) {
												e.printStackTrace();
											}

										} catch (JSONException e) {
											e.printStackTrace();
										}
										// startActivity();
										signinwith = "Facebook";

										if(email != null && !email.equals("")){
											new Registration().execute(
													lastname,
													firstname,
													email,
													"Android",
													"Android",
													signinwith,
													Utilities.getSharedPref(
															Utilities.GCM_REG_ID,
															null),
													Utilities
															.getTimeZone(LogInActivity.this),
													Utilities
															.getDeviceID(LogInActivity.this));
										}else{
											pDialog.setVisibility(View.GONE);
											showFacebookEmailDialog();
										}

									} else {
										pDialog.setVisibility(View.GONE);
										Utilities.showSnackbar(mContext, "Information Not Recieved");
									}
								}
							}).executeAsync();
				}
			}
		});
	}

	// Show facebook dialog when email is make private by user
	public void showFacebookEmailDialog() {
		// Create custom dialog object
		final Dialog dialog = new Dialog(LogInActivity.this);
		// Include dialog.xml file
		dialog.setContentView(R.layout.custom_dialog_facebook);
		// Set dialog title
		dialog.setTitle("Your facebook email id is private so you have to make it public or you can put manually here");
		dialog.setCanceledOnTouchOutside(false);

		// set values for custom dialog components - text, image and button
		final EditText edt_email_facebook = (EditText) dialog
				.findViewById(R.id.edt_email_facebook);
		dialog.show();
		Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		Button btn_Ok = (Button) dialog.findViewById(R.id.btn_Ok);
		btn_Ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edt_email_facebook.getText().length() == 0) {

					Utilities.showSnackbar(mContext, "Enter Email");

				} else if(Utilities.isEmailValid(edt_email_facebook.getText().toString())){

					Utilities.showSnackbar(mContext, "Enter Valid Email");

				}else{
					email = edt_email_facebook.getText().toString();
					dialog.dismiss();
					pDialog.setVisibility(View.VISIBLE);
					new Registration().execute(
							lastname,
							firstname,
							email,
							"Android",
							"Android",
							signinwith,
							Utilities.getSharedPref(
									Utilities.GCM_REG_ID,
									null),
							Utilities.getTimeZone(LogInActivity.this),
							Utilities.getDeviceID(LogInActivity.this));
				}
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	// start Another Activity
	private void startActivity() {
		pref = getApplicationContext().getSharedPreferences("RegisterPref",
				MODE_PRIVATE);
		editor = pref.edit();
		editor.putString("email", email);
		editor.putString("signinwith", signinwith);
		editor.commit();

		// Here To Copy SharePrefrence Xml to Sdcard...
		Utilities.copyFile("/data/data/" + getPackageName()
				+ "/shared_prefs/RegisterPref.xml");

		Intent mintent = new Intent();
		setResult(RESULT_OK, mintent);
		finish();
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);
	}

	// onActivityResult
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		uiHelper.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}

		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_REGISTER) {
				Log.e("onActivityResult", "onActivityResultRegister");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	// onStart
	protected void onStart() {
		super.onStart();
		// mGoogleApiClient.connect();
	}

	// onResume
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	// onSaveInstanceState
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
	}

	// onStop
	@Override
	protected void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(callback);
		// if (mGoogleApiClient.isConnected()) {
		// mGoogleApiClient.disconnect();
		// }
	}

	// onPause
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	// onSessionStateChange
	private void onSessionStateChange(final Session session,
			SessionState state, Exception exception) {
		if (session != null && session.isOpened()) {
			// requestMyAppFacebookFriends(session);
		}
	}

	// StatusCallback
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	// openActiveSession
	private static Session openActiveSession(Activity activity,
			boolean allowLoginUI, Session.StatusCallback statusCallback) {
		OpenRequest openRequest = new OpenRequest(activity);
		openRequest.setPermissions(Arrays.asList("user_birthday", "email",
				"read_friendlists", "user_friends"));
		openRequest.setCallback(statusCallback);

		Session session = new Session.Builder(activity).build();

		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForRead(openRequest);
			return session;
		}

		return null;
	}

	// onDestroy
	@Override
	protected void onDestroy() {
		try {
			uiHelper.onDestroy();
		} catch (Exception e) {
			Log.e("Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		pDialog.setVisibility(View.VISIBLE);
		if (!mGoogleApiClient.isConnecting()) {
			mGoogleApiClient.connect();
			mSignInClicked = true;
			// resolveSignInError();
		}
	}

	/**
	 * Sign-out from google
	 * */
	public void signOutFromGplus() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			// mGoogleApiClient.connect();
		}
	}

	/**
	 * Revoking access from google
	 * */
	@SuppressWarnings("unused")
	private void revokeGplusAccess() {
		if (mGoogleApiClient.isConnected()) {
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
					.setResultCallback(new ResultCallback<Status>() {
						@Override
						public void onResult(Status arg0) {
							mGoogleApiClient.connect();
						}

					});
		}
	}

	/**
	 * Method to resolve any sign in errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	// onConnectionFailed
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	// onConnected
	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		getProfileInformation();
		// Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	}

	/**
	 * Fetching user's information name, email, profile PIC
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				id = currentPerson.getId();
				String name = currentPerson.getDisplayName();
				Log.e("NameGooGle>> ", "" + name);
				String[] separated = name.split(" ");
				firstname = separated[0];
				Log.e("FirstNAme>> ", "" + firstname);
				lastname = separated[1];
				Log.e("LastNAme>> ", "" + lastname);
				personPhotoUrl = currentPerson.getImage().getUrl();
				Log.e("personPhotoUrlGooGle>> ", "" + personPhotoUrl);
				personGooglePlusProfile = currentPerson.getUrl();
				Log.e("personGooglePlus>> ", ""+ personGooglePlusProfile);
				email = Plus.AccountApi.getAccountName(mGoogleApiClient);
				Log.e("emailGooGle>> ", "" + email);
				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;

				signinwith = "Google";
				new Registration().execute(lastname, firstname, email,
						"Android", "Android", signinwith,
						Utilities.getSharedPref(Utilities.GCM_REG_ID, null),
						Utilities.getTimeZone(LogInActivity.this),
						Utilities.getDeviceID(LogInActivity.this));

			} else {
				Toast.makeText(getApplicationContext(),
						"Person information is null", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// onConnectionSuspended
	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	// on edit text change watcher
	private final TextWatcher mTextEditorWatcher = new TextWatcher() {

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			edt_email.setBackgroundResource(R.drawable.greybg_heb);
			edt_password.setBackgroundResource(R.drawable.greybg_heb);
		}

		public void afterTextChanged(Editable s) {

		}
	};

	// Log In
	class LogIn extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jObj = null;
			HttpParams params2 = new BasicHttpParams();
			params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			DefaultHttpClient mHttpClient = new DefaultHttpClient(params2);
			String url = Config.ROOT_SERVER_CLIENT + Config.LOGIN_API;
			HttpPost httppost = new HttpPost(url);

			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			try {
				multipartEntity.addPart("email", new StringBody(params[0],
						Charset.forName("UTF-8")));
				multipartEntity.addPart("password", new StringBody(params[1],
						Charset.forName("UTF-8")));
				multipartEntity.addPart("signinwith", new StringBody(params[2],
						Charset.forName("UTF-8")));
				multipartEntity.addPart("device_token", new StringBody(
						params[3], Charset.forName("UTF-8")));
				httppost.setEntity(multipartEntity);
				HttpResponse response = mHttpClient.execute(httppost);
				HttpEntity r_entity = response.getEntity();
				String strResponse = EntityUtils.toString(r_entity);
				jObj = new JSONObject(strResponse);
				Log.e("LogInResponse >> ", ">> " + jObj);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.setVisibility(View.GONE);
			if (result != null) {
				try {
					if (result.get("success").equals(true)) {

						edt_email.setText("");
						edt_password.setText("");
						Utilities.showSnackbar(mContext, result.getString("message"));
						startActivity();

					} else if (result.get("success").equals(false)) {
						Utilities.showSnackbar(mContext, result.getString("message"));

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// Registration Task Execute Here For FaceBook And Google...
	public class Registration extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jObj = null;
			HttpParams params2 = new BasicHttpParams();
			params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			DefaultHttpClient mHttpClient = new DefaultHttpClient(params2);
			String url = Config.ROOT_SERVER_CLIENT + Config.REGISTER_API;

			HttpPost httppost = new HttpPost(url);

			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			try {
				if (params[0] != null) {
					if (!params[0].equals("")) {
						multipartEntity.addPart("lastname", new StringBody(
								params[0], Charset.forName("UTF-8")));
					} else {
						multipartEntity.addPart("lastname", new StringBody(""));
					}
				} else {
					multipartEntity.addPart("lastname", new StringBody(""));
				}
				if (params[1] != null) {
					if (!params[1].equals("")) {
						multipartEntity.addPart("firstname", new StringBody(
								params[1], Charset.forName("UTF-8")));
					} else {
						multipartEntity
								.addPart("firstname", new StringBody(""));
					}
				} else {
					multipartEntity.addPart("firstname", new StringBody(""));
				}
				if (params[2] != null) {
					if (!params[2].equals("")) {
						multipartEntity.addPart("email", new StringBody(
								params[2], Charset.forName("UTF-8")));
					} else {
						multipartEntity.addPart("email", new StringBody(""));
					}
				} else {
					multipartEntity.addPart("email", new StringBody(""));
				}

				multipartEntity.addPart("password", new StringBody(params[3],
						Charset.forName("UTF-8")));
				multipartEntity.addPart("passwordagain", new StringBody(
						params[4], Charset.forName("UTF-8")));
				multipartEntity.addPart("signinwith", new StringBody(params[5],
						Charset.forName("UTF-8")));
				multipartEntity.addPart("device_token", new StringBody(
						params[6], Charset.forName("UTF-8")));
				multipartEntity.addPart("time_zone", new StringBody(params[7],
						Charset.forName("UTF-8")));
				multipartEntity.addPart("device_key", new StringBody(params[8],
						Charset.forName("UTF-8")));
				httppost.setEntity(multipartEntity);
				HttpResponse response = mHttpClient.execute(httppost);
				HttpEntity r_entity = response.getEntity();
				String strResponse = EntityUtils.toString(r_entity);

				jObj = new JSONObject(strResponse);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.setVisibility(View.GONE);
			if (result != null) {
				try {
					if (result.get("success").equals(true)) {
						Utilities.showSnackbar(mContext, result.getString("value"));
						startActivity();

					} else if (result.get("success").equals(false)) {
						Utilities.showSnackbar(mContext, "Login Successfully!");
						startActivity();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// Forgot password execute ...
	class ForgotPassword extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jObj = null;
			HttpParams params2 = new BasicHttpParams();
			params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			DefaultHttpClient mHttpClient = new DefaultHttpClient(params2);
			String url = Config.ROOT_SERVER_CLIENT + Config.FORGOT_PASSWORD;
			HttpPost httppost = new HttpPost(url);

			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			try {
				multipartEntity.addPart("email", new StringBody(params[0],
						Charset.forName("UTF-8")));
				multipartEntity.addPart("signinwith", new StringBody(params[1],
						Charset.forName("UTF-8")));
				httppost.setEntity(multipartEntity);
				HttpResponse response = mHttpClient.execute(httppost);
				HttpEntity r_entity = response.getEntity();
				String strResponse = EntityUtils.toString(r_entity);
				jObj = new JSONObject(strResponse);
				Log.e("Response >> ", ">> " + jObj);

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.setVisibility(View.GONE);
			if (result != null) {
				try {
					if (result.get("success").equals(true)) {

						// edt_email.setText("");
						edt_password.setText("");
						edt_password_forgot.setText("");
						/*
						 * pref = getApplicationContext().getSharedPreferences(
						 * "RegisterPref", MODE_PRIVATE);
						 * 
						 * editor = pref.edit(); editor.putString("email",
						 * email); editor.putString("signinwith", signinwith);
						 * editor.commit();
						 */

						Utilities.showSnackbar(mContext, result.getString("message"));

						// startActivity();

					} else if (result.get("success").equals(false)) {
						Utilities.showSnackbar(mContext, result.getString("message"));

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// finish activity
	public void finishAcivity() {
		LogInActivity.this.finish();
	}

	/*************************************** Keyboard hide ***************************************************/
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View view = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (view instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if(getWindow().getCurrentFocus().getWindowToken()!=null){
					imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
				}
			}
		}
		return ret;
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
			Log.e(Utilities.TAG, "register id  in checkTextUtils gcm info   = "
					+ Utilities.gcm_id);
			Log.e("LogInActivity", "GCM RegId: " + regId);

		} else {
			// Toast.makeText(getApplicationContext(),
			// "Already Registered with GCM Server!",
			// Toast.LENGTH_LONG).show();
			// showAlertDialog();
		}

	}

	// register GCM...
	public String registerGCM() {
		// GCMRegistrar.checkManifest(this);
		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(LogInActivity.this);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.e("RegisterActivity",
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
			Utilities.setSharedPref(Utilities.GCM_REG_ID, regId,
					Utilities.GCM_REG_BOOL, false);
			Utilities.gcm_id = Utilities.getSharedPref(Utilities.GCM_REG_ID,
					null);
			// Utilities.selfProfile.setRegistrationId(regId);
			Log.e(Utilities.TAG, "register id  in register gcm in if  = "
					+ Utilities.gcm_id);

		} else {
			Utilities.setSharedPref(Utilities.GCM_REG_ID, regId,
					Utilities.GCM_REG_BOOL, false);
			Utilities.gcm_id = Utilities.getSharedPref(Utilities.GCM_REG_ID,
					null);
			// Toast.makeText(getApplicationContext(),
			// "RegId already available. RegId: " + regId,
			// Toast.LENGTH_LONG).show();

			Log.e(Utilities.TAG,
					"register id  in register gcm in shared else = "
							+ Utilities.gcm_id);
		}
		return regId;
	}

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging
								.getInstance(LogInActivity.this);
					}
					regId = gcm.register(Constant.GOOGLE_PROJECT_ID);
					Utilities.setSharedPref(Utilities.GCM_REG_ID, regId,
							Utilities.GCM_REG_BOOL, false);
					Utilities.gcm_id = Utilities.getSharedPref(
							Utilities.GCM_REG_ID, null);
					Log.e(Utilities.TAG,
							"RegisterActivity registerInBackground -in  regId: "
									+ regId);
					// Utilities.selfProfile.setRegistrationId(regId);
					Log.e(Utilities.TAG,
							"register id  in register background in shared = "
									+ Utilities.gcm_id);
					msg = "Device registered, registration ID=" + regId;
					// new RegisterDeviceTask().execute();
					// registerDeviceOnServer();

				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.e("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				// Toast.makeText(getApplicationContext(),
				// "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
				// .show();
			}
		}.execute(null, null, null);
	}

	// Get GCM Registration ID here...
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				LogInActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.e(Utilities.TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.e(Utilities.TAG, "App version changed.");
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
			Log.e(Utilities.TAG,
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}

	// onBackPressed
	@Override
	public void onBackPressed() {
		Intent mintent = new Intent();
		setResult(RESULT_OK, mintent);
		finish();
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);
		super.onBackPressed();
	}
}
