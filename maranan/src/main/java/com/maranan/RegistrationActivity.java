package com.maranan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.nio.charset.Charset;

public class RegistrationActivity extends Activity implements OnClickListener {

	private EditText edt_LastName_Reg, edt_FirstName_Reg, edt_EmailAddress_Reg,
			edt_Password_Reg, edt_PasswordRE_Reg;
	private Button btn_Register;
	private DrawerLayout drawer_layout_reg;
	private RelativeLayout realtive_reg;
	private ActionBarDrawerToggle mDrawerToggle;
	private ConnectionDetector cd;
	private Boolean isInternetPresent = false;
	private TextView tv_reg;
	private ImageView img_reg;
	private String lastname, firstname, email, password, passwordmatch;
	private SharedPreferences pref;
	private Editor editor;
	//private ProgressDialog pDialog;
	private ProgressBar pDialog;
	private CheckBox chk_signature;
	boolean isCheck = true;

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
		setContentView(R.layout.activity_registration);
		initializeView();
	}

	// Initialize View
	private void initializeView() {
		pDialog = (ProgressBar) findViewById(R.id.progressBar);
		pDialog.setVisibility(View.GONE);
		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();
		drawer_layout_reg = (DrawerLayout) findViewById(R.id.drawer_layout_reg);
		drawer_layout_reg
				.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		img_reg = (ImageView) findViewById(R.id.img_reg);
		img_reg.setOnClickListener(this);
		tv_reg = (TextView) findViewById(R.id.tv_reg);
		tv_reg.setOnClickListener(this);
		realtive_reg = (RelativeLayout) findViewById(R.id.realtive_reg);
		edt_LastName_Reg = (EditText) findViewById(R.id.edt_LastName_Reg);
		edt_LastName_Reg.addTextChangedListener(mTextEditorWatcher);
		edt_FirstName_Reg = (EditText) findViewById(R.id.edt_FirstName_Reg);
		edt_FirstName_Reg.addTextChangedListener(mTextEditorWatcher);
		edt_EmailAddress_Reg = (EditText) findViewById(R.id.edt_EmailAddress_Reg);
		edt_EmailAddress_Reg.addTextChangedListener(mTextEditorWatcher);
		edt_Password_Reg = (EditText) findViewById(R.id.edt_Password_Reg);
		edt_Password_Reg.addTextChangedListener(mTextEditorWatcher);
		edt_PasswordRE_Reg = (EditText) findViewById(R.id.edt_PasswordRE_Reg);
		edt_PasswordRE_Reg.addTextChangedListener(mTextEditorWatcher);
		btn_Register = (Button) findViewById(R.id.btn_Register);
		btn_Register.setOnClickListener(this);
		chk_signature = (CheckBox) findViewById(R.id.chk_signature);
		chk_signature.setChecked(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout_reg,
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
		drawer_layout_reg.setDrawerListener(mDrawerToggle);

		// check listener on check Box
		chk_signature.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					isCheck = isChecked;
					// chk_box.setButtonDrawable(R.drawable.chk_black);

				} else {
					// chk_box.setButtonDrawable(R.drawable.chk_white);
					isCheck = isChecked;

				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_Register) {
			if (edt_LastName_Reg.getText().length() > 0) {
				if (edt_FirstName_Reg.getText().length() > 0) {
					if (edt_EmailAddress_Reg.getText().length() > 0) {
						if (Utilities.isEmailValid(edt_EmailAddress_Reg
								.getText().toString())) {
							if (edt_Password_Reg.getText().length() > 0) {
								if (edt_PasswordRE_Reg.getText().length() > 0) {
									if (edt_Password_Reg
											.getText()
											.toString()
											.equals(edt_PasswordRE_Reg
													.getText().toString())) {

										if (isCheck == true) {
											isInternetPresent = cd
													.isConnectingToInternet();
											if (isInternetPresent) {

												lastname = edt_LastName_Reg
														.getText().toString();
												firstname = edt_FirstName_Reg
														.getText().toString();
												email = edt_EmailAddress_Reg
														.getText().toString();
												password = edt_Password_Reg
														.getText().toString();
												passwordmatch = edt_PasswordRE_Reg
														.getText().toString();
												
												new Registration()
														.execute(
																lastname,
																firstname,
																email,
																password,
																passwordmatch,
																"App",
																Utilities.getSharedPref(
																				Utilities.GCM_REG_ID,
																				null),
																Utilities.getTimeZone(RegistrationActivity.this), 
																		Utilities.getDeviceID(RegistrationActivity.this));

											} else {
												Utilities
														.showAlertDialog(
																RegistrationActivity.this,
																"Internet Connection Error",
																"Please connect to working Internet connection",
																false);
											}
										} else {

											Utilities.showSnackbar(this, "This field is mandatory");

										}

									} else {
										Utilities.showSnackbar(this, "Password Not Match");
										edt_PasswordRE_Reg
												.setBackgroundResource(R.drawable.greybg_red);
									}

								} else {
									Utilities.showSnackbar(this, "Enter Password Again");
									edt_PasswordRE_Reg
											.setBackgroundResource(R.drawable.greybg_red);
								}

							} else {

								Utilities.showSnackbar(this, "Enter Password");

								edt_Password_Reg
										.setBackgroundResource(R.drawable.greybg_red);

							}
						} else {

							Utilities.showSnackbar(this, "Enter Valid Email");

							edt_EmailAddress_Reg
									.setBackgroundResource(R.drawable.greybg_red);

						}

					} else {

						Utilities.showSnackbar(this, "Enter Email");

						edt_EmailAddress_Reg
								.setBackgroundResource(R.drawable.greybg_red);

					}
				} else {

					Utilities.showSnackbar(this, "Enter First Name");

					edt_FirstName_Reg
							.setBackgroundResource(R.drawable.greybg_red);

				}
			} else {

				Utilities.showSnackbar(this, "Enter Last Name");

				edt_LastName_Reg.setBackgroundResource(R.drawable.greybg_red);

			}

		} else if (id == R.id.img_reg) {
			if (isInternetPresent) {
				Utilities.logout(RegistrationActivity.this);
				drawer_layout_reg.closeDrawer(realtive_reg);
			} else {
				Utilities.showAlertDialog(RegistrationActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
			}
		} else if (id == R.id.tv_reg) {
			if (isInternetPresent) {
				Utilities.logout(RegistrationActivity.this);
				drawer_layout_reg.closeDrawer(realtive_reg);
			} else {
				Utilities.showAlertDialog(RegistrationActivity.this,
						"Internet Connection Error",
						"Please connect to working Internet connection", false);
			}
		}

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

	// Registration Task Execute Here..
	public class Registration extends AsyncTask<String, Void, JSONObject> {

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
			String url = Config.ROOT_SERVER_CLIENT + Config.REGISTER_API;

			HttpPost httppost = new HttpPost(url);

			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			try {

				multipartEntity.addPart("lastname", new StringBody(params[0],
						Charset.forName("UTF-8")));
				multipartEntity.addPart("firstname", new StringBody(params[1],
						Charset.forName("UTF-8")));
				multipartEntity.addPart("email", new StringBody(params[2],
						Charset.forName("UTF-8")));
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
						pref = getApplicationContext().getSharedPreferences("RegisterPref", MODE_PRIVATE);
						editor = pref.edit();
						editor.putString("email", email);
						editor.putString("signinwith", "App");
						editor.commit();

						// Here To Copy SharePrefrence Xml to Sdcard...
						Utilities.copyFile("/data/data/" + getPackageName()
								+ "/shared_prefs/RegisterPref.xml");

						edt_LastName_Reg.setText("");
						edt_FirstName_Reg.setText("");
						edt_EmailAddress_Reg.setText("");
						edt_Password_Reg.setText("");
						edt_PasswordRE_Reg.setText("");

						Utilities.showSnackbar(RegistrationActivity.this, result.getString("value"));

						Intent mintent = new Intent();
						setResult(RESULT_OK, mintent);
						finish();
						overridePendingTransition(R.anim.activity_open_scale,
								R.anim.activity_close_translate);
						LogInActivity.getInstance().finishAcivity();

					} else if (result.get("success").equals(false)) {

						Utilities.showSnackbar(RegistrationActivity.this, result.getString("value"));

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// on edit text change watcher
	private final TextWatcher mTextEditorWatcher = new TextWatcher() {

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			edt_LastName_Reg.setBackgroundResource(R.drawable.greybg_heb);
			edt_FirstName_Reg.setBackgroundResource(R.drawable.greybg_heb);
			edt_EmailAddress_Reg.setBackgroundResource(R.drawable.greybg_heb);
			edt_Password_Reg.setBackgroundResource(R.drawable.greybg_heb);
			edt_PasswordRE_Reg.setBackgroundResource(R.drawable.greybg_heb);
		}

		public void afterTextChanged(Editable s) {

		}
	};

	// Decode Password text
	public String decode(String text) {
		byte[] data;
		String decode = null;
		try {
			data = text.getBytes("UTF-8");
			decode = Base64.encodeToString(data, Base64.DEFAULT);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decode;
	}

	// Decode Password text
	public String encode(String text) {
		byte[] data;
		String encode = null;
		try {
			data = Base64.decode(encode, Base64.DEFAULT);
			encode = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encode;
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
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}

	// onBackPressed
	@Override
	public void onBackPressed() {
		/*
		 * Intent mintent = new Intent(); setResult(RESULT_OK, mintent);
		 */
		finish();
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);
		super.onBackPressed();
	}

}
