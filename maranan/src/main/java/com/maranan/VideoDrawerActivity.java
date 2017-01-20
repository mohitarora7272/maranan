package com.maranan;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.maranan.adapters.NavDrawerListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class VideoDrawerActivity extends YouTubeBaseActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<HashMap<String, String>> arrayList;
	private ArrayList<HashMap<String, String>> arrayListFull;
	private NavDrawerListAdapter adapter;
	private static VideoDrawerActivity mContext;
	private Bundle savedInstanceStates;
	private RelativeLayout realtive_naviagate;
	public Handler h;
	public int width, widthDrawer;
	public int height;
	public DrawerLayout.LayoutParams lay;
	public FrameLayout frame_container;
	private ImageView img_sideBtn_Up;
	int mLastSystemUiVis;

	// VideoDrawerActivity Instance
	public static VideoDrawerActivity getInstance() {
		return mContext;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
		// Handle UnCaughtException Exception Handler
		//Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(this));
		setContentView(R.layout.nav_drawer_layout);
		mContext = VideoDrawerActivity.this;
		savedInstanceStates = savedInstanceState;
		// getDeviceHeightWidht();
		
		try {
			initializeView();
		} catch (Exception e) {
			Log.e("VideoDrawerActivity", "Exception?? " + e.getMessage());
		}
		
	}

	// initializeView
	@SuppressWarnings("unchecked")
	private void initializeView() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		frame_container = (FrameLayout) findViewById(R.id.frame_container);
		img_sideBtn_Up = (ImageView) findViewById(R.id.img_sideBtn_Up);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		realtive_naviagate = (RelativeLayout) findViewById(R.id.realtive_naviagate);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			arrayList = (ArrayList<HashMap<String, String>>) b.getSerializable("ArrayList");
			arrayListFull = (ArrayList<HashMap<String, String>>) b.getSerializable("ArrayListFull");
		}

		// setting the nav drawer list adapter
		if (arrayListFull.size() > 0) {
			adapter = new NavDrawerListAdapter(VideoDrawerActivity.this, arrayListFull);
			mDrawerList.setAdapter(adapter);
		}

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.logo, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				img_sideBtn_Up.setBackgroundResource(R.drawable.side_right_arrow);
				hideButton();
			}

			public void onDrawerOpened(View drawerView) {
				img_sideBtn_Up.setBackgroundResource(R.drawable.side_left_arrow);
				VideoPlayActivity.getInstance().getImageView().setVisibility(View.VISIBLE);
				VideoPlayActivity.getInstance().getView().setVisibility(View.VISIBLE);
				showDrawer();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

		// Navigation List Item Click listener
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				displayView(0, arrayListFull.get(position).get("VideoId"), false, arrayList, arrayListFull);
			}
		});

		if (savedInstanceStates == null) {
			displayView(0, getIntent().getStringExtra("video_Id"), getIntent().getBooleanExtra("Check", false), arrayList, arrayListFull);
		}

		// Show Drawer For 2 Second Here...
		//mDrawerLayout.openDrawer(realtive_naviagate);
		//showDrawer();
	}
	
	private void displayView(int position, String videoID, boolean check,
			ArrayList<HashMap<String, String>> arrayList2,
			ArrayList<HashMap<String, String>> arrayListFull) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new VideoPlayActivity(videoID, check, arrayList2, arrayListFull);
			break;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			mDrawerLayout.closeDrawer(realtive_naviagate);
		} 
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

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

	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
	}

	// Get Drawer Layout From Instance
	public DrawerLayout getDrawer() {
		return mDrawerLayout;
	}

	// Get Drawer Layout From Instance
	public RelativeLayout getRelativeLayout() {
		return realtive_naviagate;
	}

	// Show Navigation Drawer For 2 second for first time...
	private void showDrawer() {
		h = new Handler();
		h.postDelayed(new Runnable() {

			public void run() {
				mDrawerLayout.closeDrawer(realtive_naviagate);
			}
		}, 2000);
	}

	// hide side button after 2 second for first time...
	private void hideButton() {
		h = new Handler();
		h.postDelayed(new Runnable() {

			public void run() {
				VideoPlayActivity.getInstance().getImageView().setVisibility(View.INVISIBLE);
				VideoPlayActivity.getInstance().getView().setVisibility(View.VISIBLE);
			}
		}, 2000);
	}

	// Get Device Height Width Here...
	public void getDeviceHeightWidht() {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		width = displayMetrics.widthPixels;
		widthDrawer = (width - 30);
		height = displayMetrics.heightPixels;
	}
	
	// Get Intent Values To Stop An Start Player in the other activity...
	public Serializable getIntentValue(){
		return getIntent().getSerializableExtra("isPlayerPlaying");
	}
	
	// Get Intent Values To Stop An Start Player in the other activity...
	public Serializable getIntentValue2() {
		return getIntent().getSerializableExtra("isPlayerPlaying2");
	}

	// onBackPressed()
	@Override
	public void onBackPressed() {
		//h.removeCallbacks(null);
		Intent mintent = new Intent();
		mintent.putExtra("isPlayerPlaying", getIntent().getSerializableExtra("isPlayerPlaying"));
		mintent.putExtra("isPlayerPlaying2", getIntent().getSerializableExtra("isPlayerPlaying2"));
		setResult(RESULT_OK, mintent);
		finish();
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);
		super.onBackPressed();
	}
}
