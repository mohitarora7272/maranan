package com.maranan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maranan.adapters.AutoScrollViewPagerAadpter;
import com.maranan.adapters.PrayerAdapter;
import com.maranan.adapters.VideoListAdapter;
import com.maranan.infiniteviewpager.InfinitePagerAdapter;
import com.maranan.infiniteviewpager.InfiniteViewPager;
import com.maranan.infiniteviewpager.OnPageChangeListenerForInfiniteIndicator;
import com.maranan.restclient.RestClientRadioPrograms;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class PrayerScreen extends AppCompatActivity implements OnClickListener {
	private static PrayerScreen mContext;
	private TextView tv_title;
	private TextView tv_count_top;
	private ListView lv_prayers;
	private PrayerAdapter adapter;
	private ArrayList<String> list_email;
	private ArrayList<GetterSetter> newCompareList;
	private int count_White = 0;
	private int count_Red = 0;
	private int count_Green = 0;
	private int count_Blue = 0;
	private boolean isFirstTime = false;
	private boolean isFavourite = false;
	private boolean isTabBarClick = false;
	private ArrayList<String> list_favEmail;
	private ArrayList<GetterSetter> listSer;
	private TextView tv_those_who_prayer_count, tv_those_who_prayer;
	private TextView tv_prayer_success_count, tv_prayer_success;
	private TextView tv_prayer_medicine_count, tv_prayer_medicine;
	private TextView tv_elevation_of_soul_count, tv_elevation_of_soul;
	private LinearLayout linear_fourth;
	private LinearLayout linear_third;
	private LinearLayout linear_second;
	private LinearLayout linear_first;
	private SharedPreferences pref;
	private AutoScrollViewPagerAadpter adapter_auto;
	private InfiniteViewPager pager_infinite;
	private Button btn_pencil_live;
	private Button btn_play_live;
	private Button btn_play_text_live;
	private int REQUEST_CODE_DEVOTE = 300;
	private int position;
	private String tag = "";
	private ProgressBar pDialog;
	private CollapsingToolbarLayout collapsingToolbarLayout = null;

	// PrayerScreen Instance
	public static PrayerScreen getInstance() {
		return mContext;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);
		// Handle UnCaughtException Exception Handler
//		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(
//				this));
		setContentView(R.layout.prayer_screen);
		mContext = PrayerScreen.this;
		pref = getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
		try {
            if(pref.getString("email", null)!=null && !pref.getString("email", null).equals("")){
				GetFavourites(pref.getString("email", null));
			}else{
				setListValues();
			}
		} catch (Exception e) {
			Log.e("PrayerScreen", "Exception?? " + e.getMessage());
		}

		initializeView();

	}

	// initialize View Here...
	private void initializeView() {
		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);
		//ActionBar actionBar = getSupportActionBar();
		//actionBar.setDisplayHomeAsUpEnabled(false);

		collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
		collapsingToolbarLayout.setTitle("");

		//dynamicToolbarColor();
		//toolbarTextAppernce();

		pDialog = (ProgressBar) findViewById(R.id.progressBar);
		pDialog.setVisibility(View.INVISIBLE);
		setProgressBar(pDialog);
		pager_infinite = (InfiniteViewPager) findViewById(R.id.pager_auto);
		pager_infinite.startAutoScroll();
		adapter_auto = new AutoScrollViewPagerAadpter(this);
		PagerAdapter wrapperAdapter = new InfinitePagerAdapter(adapter_auto);
		pager_infinite.setAdapter(wrapperAdapter);

		pager_infinite.setOnPageChangeListener(new OnPageChangeListenerForInfiniteIndicator(
						this, pager_infinite.getCurrentItem()));

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getResources().getString(
				R.string.seeks_mercy_his_friend)
				+ "\n" + getResources().getString(R.string.answered_first));

		tv_count_top = (TextView) findViewById(R.id.tv_count_top);
		lv_prayers = (ListView) findViewById(R.id.lv_prayers);

		tv_those_who_prayer_count = (TextView) findViewById(R.id.tv_those_who_prayer_count);
		tv_those_who_prayer_count.setOnClickListener(this);
		tv_those_who_prayer_count.setTextColor(Color.parseColor("#000000"));
       
		tv_prayer_success_count = (TextView) findViewById(R.id.tv_prayer_success_count);
		tv_prayer_success_count.setOnClickListener(this);
		tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));

		tv_prayer_medicine_count = (TextView) findViewById(R.id.tv_prayer_medicine_count);
		tv_prayer_medicine_count.setOnClickListener(this);
		tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));

		tv_elevation_of_soul_count = (TextView) findViewById(R.id.tv_elevation_of_soul_count);
		tv_elevation_of_soul_count.setOnClickListener(this);
		tv_elevation_of_soul_count.setTextColor(Color.parseColor("#FFFFFF"));

		tv_those_who_prayer = (TextView) findViewById(R.id.tv_those_who_prayer);
		tv_those_who_prayer.setOnClickListener(this);
		tv_those_who_prayer.setTextColor(Color.parseColor("#000000"));

		tv_prayer_success = (TextView) findViewById(R.id.tv_prayer_success);
		tv_prayer_success.setOnClickListener(this);
		tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));

		tv_prayer_medicine = (TextView) findViewById(R.id.tv_prayer_medicine);
		tv_prayer_medicine.setOnClickListener(this);
		tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));

		tv_elevation_of_soul = (TextView) findViewById(R.id.tv_elevation_of_soul);
		tv_elevation_of_soul.setOnClickListener(this);
		tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));

		linear_first = (LinearLayout) findViewById(R.id.linear_first);
		linear_first.setOnClickListener(this);
		linear_first.setBackgroundColor(Color.parseColor("#FFFFFF"));

		linear_second = (LinearLayout) findViewById(R.id.linear_second);
		linear_second.setOnClickListener(this);
		linear_second.setBackgroundColor(Color.parseColor("#0380B7"));

		linear_third = (LinearLayout) findViewById(R.id.linear_third);
		linear_third.setOnClickListener(this);
		linear_third.setBackgroundColor(Color.parseColor("#64B100"));

		linear_fourth = (LinearLayout) findViewById(R.id.linear_fourth);
		linear_fourth.setOnClickListener(this);
		linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));

		btn_pencil_live = (Button) findViewById(R.id.btn_pencil_live);
		btn_pencil_live.setOnClickListener(this);
		btn_play_live = (Button) findViewById(R.id.btn_play_live);
		btn_play_live.setOnClickListener(this);
		btn_play_text_live = (Button) findViewById(R.id.btn_play_text_live);
		btn_play_text_live.setOnClickListener(this);

		//setListValues();

		lv_prayers.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		lv_prayers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				checkForEveryUserDedication(position);
			}
		});

	}

	private void dynamicToolbarColor() {

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo);

		Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

			@Override
			public void onGenerated(Palette palette) {
				collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.color.blue_text_color));
				collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.color.blue_text_color));
			}
		});
	}

	private void toolbarTextAppernce() {
		collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
		collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);
//	}
	

	// Set List Values From Array List...
	private void setListValues() {
		isTabBarClick = false;
		isFirstTime = false;
		isFavourite = false;
		int count_White = 0;
		this.count_White = count_White;
		int count_Red = 0;
		this.count_Red = count_Red;
		int count_Green = 0;
		this.count_Green = count_Green;
		int count_Blue = 0;
		this.count_Blue = count_Blue;
		tv_count_top.setText("0");
		tv_those_who_prayer_count.setText("0");
		tv_prayer_success_count.setText("0");
		tv_prayer_medicine_count.setText("0");
		tv_elevation_of_soul_count.setText("0");

		lv_prayers = (ListView) findViewById(R.id.lv_prayers);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.setMargins(0, 0, 0, 0); // left, top, right, bottom
		lv_prayers.setLayoutParams(params);

		// listFavourite Email List Declare here...
		list_favEmail = new ArrayList<String>();
		list_favEmail = Utilities.getfavArrayListPref(this);
		listSer = new ArrayList<GetterSetter>();
		listSer = Utilities.getfavArrayListPrefForTag(this);

		// Email Array List Declare...
		list_email = new ArrayList<String>();

		// Get Email From Accepted dedications array list...
		for (int i = 0; i < ((ApplicationSingleton) getApplicationContext()).dataContainer
				.getList_AcceptedDedication().size(); i++) {
			if (getIntent()
					.getStringExtra("email")
					.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i).getEmail())) {

				list_email
						.add(((ApplicationSingleton) getApplicationContext()).dataContainer
								.getList_AcceptedDedication().get(i).getEmail());
			}
		}

		for (int i = 0; i < ((ApplicationSingleton) getApplicationContext()).dataContainer
				.getList_AcceptedDedication().size(); i++) {

			if (!getIntent()
					.getStringExtra("email")
					.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i).getEmail())) {

				list_email
						.add(((ApplicationSingleton) getApplicationContext()).dataContainer
								.getList_AcceptedDedication().get(i).getEmail());
			}
		}

		newCompareList = new ArrayList<GetterSetter>();

		// Compare email to get common email ids from the list...
		if (list_email.size() == ((ApplicationSingleton) getApplicationContext()).dataContainer
				.getList_AcceptedDedication().size()) {

			Map<String, String> dupMap = new TreeMap<String, String>();
			String key = null;

			for (String some : list_email) {
				key = some;
				if (key != null && key.trim().length() > 0
						&& !dupMap.containsKey(key)) {
					dupMap.put(key, some);
					isFavourite = false;
					GetterSetter geset = new GetterSetter();

					// Get favourite checks from user...
					if (list_favEmail.size() > 0) {

						for (int i = 0; i < list_favEmail.size(); i++) {

							geset = new GetterSetter();

							if (list_favEmail.get(i).equals(dupMap.get(key))) {
                                if(listSer.size() > 0){
									if (listSer.get(i).getTag().equals("single")) {
										geset.setEmail(dupMap.get(key));
										geset.setImageResource(R.drawable.checked_green);
										geset.setColorResource(R.drawable.blue_back_new);
										geset.setImagefavResource(R.drawable.fav_green);
										geset.setYourfav(getResources().getString(R.string.your_favorites_list));
										geset.setTag("single");
										setCountsWithTag(dupMap.get(key));

									} else if (listSer.get(i).getTag()
											.equals("other")) {
										geset.setEmail(dupMap.get(key));
										geset.setImagefavResource(R.drawable.fav_blue);
										geset.setYourfav(getResources().getString(
												R.string.added_you_to_fav));
										geset.setImageResource(R.drawable.unchecked_green);
										geset.setColorResource(R.drawable.red_back_new);
										geset.setTag("other");

									} else if (listSer.get(i).getTag()
											.equals("both")) {
										geset.setEmail(dupMap.get(key));
										geset.setImageResource(R.drawable.checked_green);
										geset.setColorResource(R.drawable.blue_back_new);
										geset.setImagefavResource(R.drawable.fav_both);
										geset.setYourfav(getResources()
												.getString(
														R.string.you_are_each_other_favourite));
										geset.setTag("both");
										setCountsWithTag(dupMap.get(key));
									}

									isFavourite = true;
								}


								break;
							}
						}

						if (!isFavourite) {
							geset.setEmail(dupMap.get(key));
							geset.setImageResource(R.drawable.unchecked_green);
							geset.setColorResource(R.drawable.red_back_new);
							geset.setImagefavResource(0);
							geset.setYourfav("");
							geset.setTag("");
						}

					} else {
						geset.setEmail(dupMap.get(key));
						geset.setImageResource(R.drawable.unchecked_green);
						geset.setColorResource(R.drawable.red_back_new);
						geset.setImagefavResource(0);
						geset.setYourfav("");
						geset.setTag("");

					}

					newCompareList.add(geset);
				}
			}

			for (int i = 0; i < newCompareList.size(); i++) {
				int count2 = 0;
				isFirstTime = false;
				for (int j = 0; j < ((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().size(); j++) {

					if (newCompareList
							.get(i)
							.getEmail()
							.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(j)
									.getEmail())) {

						count2 = count2 + 1;
						newCompareList.get(i).setCount(count2);

						if (!isFirstTime) {
							newCompareList
									.get(i)
									.setDate(
											((ApplicationSingleton) getApplicationContext()).dataContainer
													.getList_AcceptedDedication()
													.get(j).getDate());
							newCompareList
									.get(i)
									.setTime(
											((ApplicationSingleton) getApplicationContext()).dataContainer
													.getList_AcceptedDedication()
													.get(j).getTime());
							isFirstTime = true;
						}
					}
				}

				// Get Total Count To Show Favourite On The Top Right Side Text
				// View...
				if (newCompareList.get(i).getImageResource() == R.drawable.checked_green) {
					tv_count_top.setText(String.valueOf(Integer
							.parseInt(tv_count_top.getText().toString())
							+ newCompareList.get(i).getCount()));
				}
			}

			setAdapter(newCompareList);
		}
	}

	private void setCountsWithTag(String email) {
		count_White = count_White + 1;
		setWhiteCount(count_White);
		tv_those_who_prayer_count.setText(String.valueOf(count_White));

		for (int j = 0; j < ((ApplicationSingleton) getApplicationContext()).dataContainer
				.getList_AcceptedDedication().size(); j++) {
			if (email
					.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(j).getEmail())) {

				if (((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication()
						.get(j)
						.getNature()
						.equals(getResources().getString(
								R.string.success_hebrew))) {

					count_Blue = count_Blue + 1;
					setBlueCount(count_Blue);
					tv_prayer_success_count.setText(String.valueOf(count_Blue));

				} else if (((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication()
						.get(j)
						.getNature()
						.equals(getResources().getString(
								R.string.medicine_hebrew))) {

					count_Green = count_Green + 1;
					setGreenCount(count_Green);
					tv_prayer_medicine_count.setText(String
							.valueOf(count_Green));

				} else if (((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication()
						.get(j)
						.getNature()
						.equals(getResources().getString(
								R.string.InMemoryof_hebrew))) {

					count_Red = count_Red + 1;
					setRedCount(count_Red);
					tv_elevation_of_soul_count.setText(String
							.valueOf(count_Red));

				}
			}
		}

	}

	// Set Adapter For Prayers...
	private void setAdapter(ArrayList<GetterSetter> list) {
		adapter = new PrayerAdapter(this, list);
		lv_prayers.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		
		/*
		 * Check if user coming from dedication screen that time the alert
		 * dialog will open
		 */
		if (Utilities.isComingFromDedication(mContext) == true) {
			Utilities.setComingFromDedication(this, false);
			checkClickForPencil();
		}

		/*
		 * Check if user coming from home screen that time the alert dialog will
		 * open
		 */
		if (Utilities.isComingFromDedicationHome(mContext) == true) {
			Utilities.setComingFromDedicationHome(this, false);
			for (int i = 0; i < newCompareList.size(); i++) {
				if (newCompareList.get(i).getEmail()
						.equals(getIntent().getStringExtra("email"))) {
					checkForEveryUserDedication(i);
				}
			}

		}
	}

	// onClick event
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tv_those_who_prayer:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#000000"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#000000"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));
			linear_first.setBackgroundColor(Color.parseColor("#FFFFFF"));
			linear_second.setBackgroundColor(Color.parseColor("#0380B7"));
			linear_third.setBackgroundColor(Color.parseColor("#64B100"));
			linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));
			try {
				setListValues();
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}

			break;

		case R.id.tv_those_who_prayer_count:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#000000"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#000000"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));
			linear_first.setBackgroundColor(Color.parseColor("#FFFFFF"));
			linear_second.setBackgroundColor(Color.parseColor("#0380B7"));
			linear_third.setBackgroundColor(Color.parseColor("#64B100"));
			linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));
			try {
				setListValues();
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}
			break;

		case R.id.linear_first:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#000000"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#000000"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));
			linear_first.setBackgroundColor(Color.parseColor("#FFFFFF"));
			linear_second.setBackgroundColor(Color.parseColor("#0380B7"));
			linear_third.setBackgroundColor(Color.parseColor("#64B100"));
			linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));
			try {
				setListValues();
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}
			break;

		case R.id.tv_prayer_success:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#000000"));
			tv_prayer_success.setTextColor(Color.parseColor("#000000"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));
			linear_first.setBackgroundColor(Color.parseColor("#000000"));
			linear_second.setBackgroundColor(Color.parseColor("#FFFFFF"));
			linear_third.setBackgroundColor(Color.parseColor("#64B100"));
			linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));
			isTabBarClick = true;
			try {
				setDedicationList("Success");
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}
			break;

		case R.id.tv_prayer_success_count:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#000000"));
			tv_prayer_success.setTextColor(Color.parseColor("#000000"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));
			linear_first.setBackgroundColor(Color.parseColor("#000000"));
			linear_second.setBackgroundColor(Color.parseColor("#FFFFFF"));
			linear_third.setBackgroundColor(Color.parseColor("#64B100"));
			linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));
			isTabBarClick = true;
			try {
				setDedicationList("Success");
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}
			break;

		case R.id.linear_second:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#000000"));
			tv_prayer_success.setTextColor(Color.parseColor("#000000"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));
			linear_first.setBackgroundColor(Color.parseColor("#000000"));
			linear_second.setBackgroundColor(Color.parseColor("#FFFFFF"));
			linear_third.setBackgroundColor(Color.parseColor("#64B100"));
			linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));
			isTabBarClick = true;
			try {
				setDedicationList("Success");
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}
			break;

		case R.id.tv_prayer_medicine:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#000000"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#000000"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));
			linear_first.setBackgroundColor(Color.parseColor("#000000"));
			linear_second.setBackgroundColor(Color.parseColor("#0380B7"));
			linear_third.setBackgroundColor(Color.parseColor("#FFFFFF"));
			linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));
			isTabBarClick = true;
			try {
				setDedicationList("Medicine");
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}

			break;

		case R.id.tv_prayer_medicine_count:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#000000"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#000000"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));
			linear_first.setBackgroundColor(Color.parseColor("#000000"));
			linear_second.setBackgroundColor(Color.parseColor("#0380B7"));
			linear_third.setBackgroundColor(Color.parseColor("#FFFFFF"));
			linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));
			isTabBarClick = true;
			try {
				setDedicationList("Medicine");
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}
			break;

		case R.id.linear_third:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#000000"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#000000"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#FFFFFF"));
			linear_first.setBackgroundColor(Color.parseColor("#000000"));
			linear_second.setBackgroundColor(Color.parseColor("#0380B7"));
			linear_third.setBackgroundColor(Color.parseColor("#FFFFFF"));
			linear_fourth.setBackgroundColor(Color.parseColor("#AB0D1F"));
			isTabBarClick = true;
			try {
				setDedicationList("Medicine");
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}
			break;

		case R.id.tv_elevation_of_soul:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#000000"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#000000"));
			linear_first.setBackgroundColor(Color.parseColor("#000000"));
			linear_second.setBackgroundColor(Color.parseColor("#0380B7"));
			linear_third.setBackgroundColor(Color.parseColor("#64B100"));
			linear_fourth.setBackgroundColor(Color.parseColor("#FFFFFF"));
			isTabBarClick = true;
			try {
				setDedicationList("Elevation");
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}

			break;

		case R.id.tv_elevation_of_soul_count:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#000000"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#000000"));
			linear_first.setBackgroundColor(Color.parseColor("#000000"));
			linear_second.setBackgroundColor(Color.parseColor("#0380B7"));
			linear_third.setBackgroundColor(Color.parseColor("#64B100"));
			linear_fourth.setBackgroundColor(Color.parseColor("#FFFFFF"));
			isTabBarClick = true;
			try {
				setDedicationList("Elevation");
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}
			break;

		case R.id.linear_fourth:
			tv_those_who_prayer_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_those_who_prayer.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_success.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine_count.setTextColor(Color.parseColor("#FFFFFF"));
			tv_prayer_medicine.setTextColor(Color.parseColor("#FFFFFF"));
			tv_elevation_of_soul_count
					.setTextColor(Color.parseColor("#000000"));
			tv_elevation_of_soul.setTextColor(Color.parseColor("#000000"));
			linear_first.setBackgroundColor(Color.parseColor("#000000"));
			linear_second.setBackgroundColor(Color.parseColor("#0380B7"));
			linear_third.setBackgroundColor(Color.parseColor("#64B100"));
			linear_fourth.setBackgroundColor(Color.parseColor("#FFFFFF"));
			isTabBarClick = true;
			try {
				setDedicationList("Elevation");
			} catch (Exception e) {
				Log.e("Exception", "ExceptionList?? " + e.getMessage());
			}
			break;

		// case R.id.img_filmed_live:
		// if(getIntent().getStringExtra("video_Id") != null &&
		// !getIntent().getStringExtra("video_Id").equals("")){
		//
		// Intent videoIntent = new Intent(this,
		// VideoPlayerForNotification.class);
		// videoIntent.putExtra("video_Id",
		// getIntent().getStringExtra("video_Id"));
		// startActivity(videoIntent);
		// }
		// break;

		case R.id.btn_pencil_live:
			checkClickForPencil();
			break;

		case R.id.btn_play_live:

			break;

		case R.id.btn_play_text_live:

			break;

		default:
			break;
		}
	}
    
	

	// Set SuccessList
	private void setDedicationList(String str_Dedication) {
		lv_prayers = (ListView) findViewById(R.id.lv_prayers);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.setMargins(150, 0, 50, 0); // left,top,right,bottom
		lv_prayers.setLayoutParams(params);

		ArrayList<GetterSetter> listPrayerItem = new ArrayList<GetterSetter>();
		ArrayList<String> list_count = new ArrayList<String>();
		int countItem = 0;

		if (listSer.size() > 0) {
			for (int i = 0; i < listSer.size(); i++) {

				if (listSer.get(i).getTag().equals("single")) {
					for (int j = 0; j < ((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().size(); j++) {
						if (list_favEmail
								.get(i)
								.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
										.getList_AcceptedDedication().get(j)
										.getEmail())) {

							countItem = countItem + 1;
							setChangeListAccToTag(str_Dedication, countItem,
									listPrayerItem, list_count, j);

						}
					}

				} else if (listSer.get(i).getTag().equals("both")) {
					for (int j = 0; j < ((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().size(); j++) {
						if (list_favEmail
								.get(i)
								.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
										.getList_AcceptedDedication().get(j)
										.getEmail())) {
							countItem = countItem + 1;
							setChangeListAccToTag(str_Dedication, countItem,
									listPrayerItem, list_count, j);

						}
					}

				}
			}
		}

		// Set Dedication Item Adapter...
		Collections.reverse(list_count);
		VideoListAdapter madapter = new VideoListAdapter(mContext,
				listPrayerItem, list_count, "PrayerScreen");
		madapter.notifyDataSetChanged();
		lv_prayers.setAdapter(madapter);
	}

	// Set Change List...
	private void setChangeListAccToTag(String str_Dedication, int countItem,
			ArrayList<GetterSetter> listPrayerItem,
			ArrayList<String> list_count, int j) {
		if (str_Dedication.equals("Success")) {
			if (((ApplicationSingleton) getApplicationContext()).dataContainer
					.getList_AcceptedDedication().get(j).getNature()
					.equals(getResources().getString(R.string.success_hebrew))) {

				GetterSetter getset = new GetterSetter();
				getset.setId(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getId());
				getset.setNature(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getNature());
				getset.setName(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getName());
				getset.setSex(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getSex());
				getset.setName_Optional("");
				getset.setThere_Is(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getThere_Is());
				getset.setBlessing("");
				getset.setEmail(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getEmail());
				getset.setSigninwith(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getSigninwith());
				getset.setTime(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getTime());
				getset.setDate(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getDate());
				getset.setPublish(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getPublish());
				getset.setCount(countItem);
				list_count.add(String.valueOf(getset.getCount()));
				listPrayerItem.add(getset);
			}
		} else if (str_Dedication.equals("Medicine")) {
			if (((ApplicationSingleton) getApplicationContext()).dataContainer
					.getList_AcceptedDedication().get(j).getNature()
					.equals(getResources().getString(R.string.medicine_hebrew))) {

				GetterSetter getset = new GetterSetter();
				getset.setId(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getId());
				getset.setNature(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getNature());
				getset.setName(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getName());
				getset.setSex(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getSex());
				getset.setName_Optional("");
				getset.setThere_Is(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getThere_Is());
				getset.setBlessing("");
				getset.setEmail(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getEmail());
				getset.setSigninwith(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getSigninwith());
				getset.setTime(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getTime());
				getset.setDate(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getDate());
				getset.setPublish(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getPublish());
				getset.setCount(countItem);
				list_count.add(String.valueOf(getset.getCount()));
				listPrayerItem.add(getset);
			}
		} else if (str_Dedication.equals("Elevation")) {
			if (((ApplicationSingleton) getApplicationContext()).dataContainer
					.getList_AcceptedDedication()
					.get(j)
					.getNature()
					.equals(getResources()
							.getString(R.string.InMemoryof_hebrew))) {

				GetterSetter getset = new GetterSetter();
				getset.setId(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getId());
				getset.setNature(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getNature());
				getset.setName(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getName());
				getset.setSex(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getSex());
				getset.setName_Optional("");
				getset.setThere_Is(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getThere_Is());
				getset.setBlessing("");
				getset.setEmail(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getEmail());
				getset.setSigninwith(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getSigninwith());
				getset.setTime(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getTime());
				getset.setDate(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getDate());
				getset.setPublish(((ApplicationSingleton) getApplicationContext()).dataContainer
						.getList_AcceptedDedication().get(j).getPublish());
				getset.setCount(countItem);
				list_count.add(String.valueOf(getset.getCount()));
				listPrayerItem.add(getset);
			}
		}

	}

	// Get Blue TextView...
	public TextView getBlueTextView() {
		return tv_prayer_success_count;
	}

	// Get Green TextView...
	public TextView getGreenTextView() {
		return tv_prayer_medicine_count;
	}

	// Get Red TextView...
	public TextView getRedTextView() {
		return tv_elevation_of_soul_count;
	}

	// Get White TextView...
	public TextView getWhiteTextView() {
		return tv_those_who_prayer_count;
	}

	// Get TextView Top...
	public TextView getTextViewTop() {
		return tv_count_top;
	}

	// Get Red Count...
	public int getRedCount() {
		return count_Red;
	}

	// Set Red Count...
	public void setRedCount(int count_Red) {
		this.count_Red = count_Red;
	}

	// Get White Count...
	public int getWhiteCount() {
		return count_White;
	}

	// Set White Count...
	public void setWhiteCount(int count_White) {
		this.count_White = count_White;
	}

	// Get Blue Count...
	public int getBlueCount() {
		return count_Blue;
	}

	// Set Blue Count...
	public void setBlueCount(int count_Blue) {
		this.count_Blue = count_Blue;
	}

	// Get Green Count...
	public int getGreenCount() {
		return count_Green;
	}

	// Set Green Count...
	public void setGreenCount(int count_Green) {
		this.count_Green = count_Green;
	}

	// Get Array List Favourite Email...
	public ArrayList<String> getfavArrayList() {
		return list_favEmail;
	}

	// Get Array List Favourite Tags...
	public ArrayList<GetterSetter> getfavArrayListTag() {
		return listSer;
	}

	// Set Array List Favourite Email...
	public void setfavArrayList() {
		list_favEmail = new ArrayList<String>();
		list_favEmail = Utilities.getfavArrayListPref(this);
	}

	// Set Array List Favourite Tags...
	public void setfavArrayListTag() {
		listSer = new ArrayList<GetterSetter>();
		listSer = Utilities.getfavArrayListPrefForTag(this);
	}

	// Showing List Prayer Items Dedications in the alert box...
	private void showAlertBox(final ArrayList<GetterSetter> listPrayerItem,
			ArrayList<String> list_count, final int position) {
		Dialog dialog = new Dialog(this);
		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.prayer_list_items);

		View top_View = (View) dialog.findViewById(R.id.top_View);
		TextView tv_profile_text_left = (TextView) top_View
				.findViewById(R.id.tv_profile_text_left);
		String[] separated = listPrayerItem.get(0).getEmail().split("@");
		tv_profile_text_left.setText(separated[0]);

		final TextView tv_count_right = (TextView) top_View
				.findViewById(R.id.tv_count_right);
		tv_count_right.setText(String.valueOf(list_count.size()));
		tv_count_right.setBackgroundResource(newCompareList.get(position)
				.getColorResource());

		TextView tv_text_name_bottom = (TextView) top_View
				.findViewById(R.id.tv_text_name_bottom);
		tv_text_name_bottom.setText(PrayerAdapter.getInstance().getArrayList()
				.get(position).getDateTimeText());
		final Button btn_check_uncheck_prayer = (Button) top_View
				.findViewById(R.id.btn_check_uncheck_prayer);
		btn_check_uncheck_prayer.setBackgroundResource(newCompareList.get(
				position).getImageResource());
		final ImageView img_favourite = (ImageView) top_View
				.findViewById(R.id.img_favourite);
		final TextView tv_devotion = (TextView) top_View
				.findViewById(R.id.tv_devotion);

		// Set Favourite Tag According to the selection on the list...
		if (newCompareList.get(position).getTag().equals("")) {

			tv_devotion.setVisibility(View.GONE);
			tv_devotion.setText("");
			img_favourite.setVisibility(View.GONE);
			if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
				if (pref.getString("email", null).equals(
						listPrayerItem.get(0).getEmail())) {
					tv_devotion.setVisibility(View.VISIBLE);
					tv_devotion.setText(getResources().getString(
							R.string.my_devotion));
				}
			}


		} else if (newCompareList.get(position).getTag().equals("single")) {

			if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
				if (pref.getString("email", null).equals(
						listPrayerItem.get(0).getEmail())) {
					img_favourite.setVisibility(View.GONE);
					tv_devotion.setVisibility(View.VISIBLE);
					tv_devotion.setText(getResources().getString(
							R.string.my_devotion));
				}else{
					tv_devotion.setVisibility(View.VISIBLE);
					tv_devotion.setText(getResources().getString(
							R.string.your_favorites_list));
					img_favourite.setVisibility(View.VISIBLE);
					img_favourite.setBackgroundResource(R.drawable.fav_green);
				}
			}


		} else if (newCompareList.get(position).getTag().equals("other")) {
			if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
				if (pref.getString("email", null).equals(
						listPrayerItem.get(0).getEmail())) {
					img_favourite.setVisibility(View.GONE);
					tv_devotion.setVisibility(View.VISIBLE);
					tv_devotion.setText(getResources().getString(
							R.string.my_devotion));
				}else{
					tv_devotion.setVisibility(View.VISIBLE);
					tv_devotion.setText(getResources().getString(
							R.string.added_you_to_fav));
					img_favourite.setVisibility(View.VISIBLE);
					img_favourite.setBackgroundResource(R.drawable.fav_blue);
				}
			}

			

		} else if (newCompareList.get(position).getTag().equals("both")) {
			if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
				if (pref.getString("email", null).equals(
						listPrayerItem.get(0).getEmail())) {
					img_favourite.setVisibility(View.GONE);
					tv_devotion.setVisibility(View.VISIBLE);
					tv_devotion.setText(getResources().getString(
							R.string.my_devotion));
				}else{
					tv_devotion.setVisibility(View.VISIBLE);
					tv_devotion.setText(getResources().getString(
							R.string.you_are_each_other_favourite));
					img_favourite.setVisibility(View.VISIBLE);
					img_favourite.setBackgroundResource(R.drawable.fav_both);
				}
			}
			
		}

		ListView list_prayer_item = (ListView) dialog
				.findViewById(R.id.list_prayer_item);

		// Set Dedication Item Adapter...
		Collections.reverse(list_count);
		VideoListAdapter madapter = new VideoListAdapter(mContext,
				listPrayerItem, list_count, "PrayerScreenPopUp");
		madapter.notifyDataSetChanged();
		list_prayer_item.setAdapter(madapter);

		btn_check_uncheck_prayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (newCompareList.get(position).getImageResource() == R.drawable.checked_green) {

					btn_check_uncheck_prayer.setBackgroundResource(R.drawable.unchecked_green);
					newCompareList.get(position).setColorResource(R.drawable.red_back_new);
					newCompareList.get(position).setImageResource(R.drawable.unchecked_green);
					tv_count_right.setBackgroundResource(R.drawable.red_back_new);

					PrayerScreen.getInstance().getWhiteTextView()
							.setText(String.valueOf(PrayerScreen.getInstance().getWhiteCount() - 1));
					PrayerScreen.getInstance().setWhiteCount(PrayerScreen.getInstance().getWhiteCount() - 1);
					PrayerScreen.getInstance().getTextViewTop()
							.setText(String.valueOf(Integer.parseInt(PrayerScreen.getInstance().getTextViewTop().getText()
													.toString()) - newCompareList.get(position).getCount()));

					try {
						// Remove email for uncheck favourite list...
						if (PrayerScreen.getInstance().getfavArrayList().size() > 0) {
							for (int i = 0; i < PrayerScreen.getInstance()
									.getfavArrayList().size(); i++) {
								if (newCompareList
										.get(position)
										.getEmail()
										.equals(PrayerScreen.getInstance()
												.getfavArrayList().get(i))) {
									PrayerScreen.getInstance()
											.getfavArrayList().remove(i);
								}
							}

							Utilities.insertArrayListPref(PrayerScreen.this,
									PrayerScreen.getInstance()
											.getfavArrayList());
							PrayerScreen.getInstance().setfavArrayList();
						}
						// Set Favourite Tag According to the selection on the
						// list...
						if (newCompareList.get(position).getTag().equals("")) {
							if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
								if (pref.getString("email", null).equals(
										listPrayerItem.get(0).getEmail())) {
									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									img_favourite.setVisibility(View.GONE);
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(
											R.string.my_devotion));
								}else{
									newCompareList.get(position).setImagefavResource(R.drawable.fav_green);
									newCompareList.get(position).setYourfav(getString(R.string.your_favorites_list));
									newCompareList.get(position).setTag("single");
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(R.string.your_favorites_list));
									img_favourite.setVisibility(View.VISIBLE);
									img_favourite.setBackgroundResource(R.drawable.fav_green);
								}
							}


						} else if (newCompareList.get(position).getTag().equals("single")) {
							if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
								if (pref.getString("email", null).equals(
										listPrayerItem.get(0).getEmail())) {

									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									img_favourite.setVisibility(View.GONE);
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(
											R.string.my_devotion));

								}else{
									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									tv_devotion.setVisibility(View.GONE);
									img_favourite.setVisibility(View.GONE);
								}

							}


						} else if (newCompareList.get(position).getTag().equals("other")) {
							if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
								if (pref.getString("email", null).equals(
										listPrayerItem.get(0).getEmail())) {

									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									img_favourite.setVisibility(View.GONE);
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(
											R.string.my_devotion));

								}else{
									newCompareList.get(position).setImagefavResource(R.drawable.fav_both);
									newCompareList.get(position).setYourfav(getString(R.string.you_are_each_other_favourite));
									newCompareList.get(position).setTag("both");
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(R.string.you_are_each_other_favourite));
									img_favourite.setVisibility(View.VISIBLE);
									img_favourite.setBackgroundResource(R.drawable.fav_both);
								}
							}


						} else if (newCompareList.get(position).getTag().equals("both")) {
							if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
								if (pref.getString("email", null).equals(
										listPrayerItem.get(0).getEmail())) {

									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									img_favourite.setVisibility(View.GONE);
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(
											R.string.my_devotion));

								}else{
									newCompareList.get(position).setImagefavResource(R.drawable.fav_blue);
									newCompareList.get(position).setYourfav(getString(R.string.added_you_to_fav));
									newCompareList.get(position).setTag("other");
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(R.string.added_you_to_fav));
									img_favourite.setVisibility(View.VISIBLE);
									img_favourite.setBackgroundResource(R.drawable.fav_blue);
								}
							}

						}

						// Set Blue, Red, Green Count At the Top Of the Tabs...
						for (int j = 0; j < ((ApplicationSingleton) getApplicationContext()).dataContainer
								.getList_AcceptedDedication().size(); j++) {
							if (newCompareList
									.get(position)
									.getEmail()
									.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
											.getList_AcceptedDedication()
											.get(j).getEmail())) {

								if (((ApplicationSingleton) getApplicationContext()).dataContainer
										.getList_AcceptedDedication()
										.get(j)
										.getNature()
										.equals(getResources().getString(
												R.string.success_hebrew))) {

									PrayerScreen
											.getInstance()
											.getBlueTextView()
											.setText(
													String.valueOf(PrayerScreen
															.getInstance()
															.getBlueCount() - 1));
									PrayerScreen.getInstance().setBlueCount(
											PrayerScreen.getInstance()
													.getBlueCount() - 1);

								} else if (((ApplicationSingleton) getApplicationContext()).dataContainer
										.getList_AcceptedDedication()
										.get(j)
										.getNature()
										.equals(getResources().getString(
												R.string.medicine_hebrew))) {

									PrayerScreen
											.getInstance()
											.getGreenTextView()
											.setText(
													String.valueOf(PrayerScreen
															.getInstance()
															.getGreenCount() - 1));
									PrayerScreen.getInstance().setGreenCount(
											PrayerScreen.getInstance()
													.getGreenCount() - 1);

								} else if (((ApplicationSingleton) getApplicationContext()).dataContainer
										.getList_AcceptedDedication()
										.get(j)
										.getNature()
										.equals(getResources().getString(
												R.string.InMemoryof_hebrew))) {

									PrayerScreen
											.getInstance()
											.getRedTextView()
											.setText(
													String.valueOf(PrayerScreen
															.getInstance()
															.getRedCount() - 1));
									PrayerScreen.getInstance().setRedCount(
											PrayerScreen.getInstance()
													.getRedCount() - 1);

								}
							}
						}

					} catch (Exception e) {
						Log.e("Exception??",
								"PrayerScreenUp?? " + e.getMessage());
					}

					adapter.notifyDataSetChanged();
					PrayerAdapter.getInstance().GetFavourites(
							pref.getString("email", null),
							newCompareList.get(position).getEmail(), "false");

				} else {

					btn_check_uncheck_prayer.setBackgroundResource(R.drawable.checked_green);
					newCompareList.get(position).setColorResource(R.drawable.blue_back_new);
					newCompareList.get(position).setImageResource(R.drawable.checked_green);
					tv_count_right.setBackgroundResource(R.drawable.blue_back_new);

					PrayerScreen.getInstance().getWhiteTextView()
							.setText(String.valueOf(PrayerScreen.getInstance()
											.getWhiteCount() + 1));
					PrayerScreen.getInstance().setWhiteCount(
							PrayerScreen.getInstance().getWhiteCount() + 1);
					PrayerScreen.getInstance().getTextViewTop()
							.setText(String.valueOf(Integer
											.parseInt(PrayerScreen
													.getInstance()
													.getTextViewTop().getText()
													.toString())
											+ newCompareList.get(position)
													.getCount()));

					try {
						// Set email for check user select as a favourite
						// list...
						if (PrayerScreen.getInstance().getfavArrayList().size() > 0) {

							PrayerScreen.getInstance()
									.getfavArrayList()
									.add(PrayerScreen.getInstance()
											.getfavArrayList().size() - 1,
											newCompareList.get(position)
													.getEmail());
							Utilities.insertArrayListPref(PrayerScreen.this,
									PrayerScreen.getInstance()
											.getfavArrayList());

						} else {

							PrayerScreen
									.getInstance()
									.getfavArrayList()
									.add(PrayerScreen.getInstance()
											.getfavArrayList().size(),
											newCompareList.get(position)
													.getEmail());
							Utilities.insertArrayListPref(PrayerScreen.this,
									PrayerScreen.getInstance()
											.getfavArrayList());
							PrayerScreen.getInstance().setfavArrayList();
						}

						// Set Favourite Tag According to the selection on the
						// list...
						if (newCompareList.get(position).getTag().equals("")) {
							if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
								if (pref.getString("email", null).equals(
										listPrayerItem.get(0).getEmail())) {

									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									img_favourite.setVisibility(View.GONE);
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(
											R.string.my_devotion));

								}else{
									newCompareList.get(position).setImagefavResource(R.drawable.fav_green);
									newCompareList.get(position).setYourfav(getString(R.string.your_favorites_list));
									newCompareList.get(position).setTag("single");
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(R.string.your_favorites_list));
									img_favourite.setVisibility(View.VISIBLE);
									img_favourite.setBackgroundResource(R.drawable.fav_green);
								}
							}

							

						} else if (newCompareList.get(position).getTag().equals("single")) {
							if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
								if (pref.getString("email", null).equals(
										listPrayerItem.get(0).getEmail())) {

									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									img_favourite.setVisibility(View.GONE);
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(
											R.string.my_devotion));

								}else{
									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									tv_devotion.setVisibility(View.GONE);
									img_favourite.setVisibility(View.GONE);
								}
							}


						} else if (newCompareList.get(position).getTag().equals("other")) {
							if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
								if (pref.getString("email", null).equals(
										listPrayerItem.get(0).getEmail())) {

									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									img_favourite.setVisibility(View.GONE);
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(
											R.string.my_devotion));

								}else{
									newCompareList.get(position).setImagefavResource(R.drawable.fav_both);
									newCompareList.get(position).setYourfav(getString(R.string.you_are_each_other_favourite));
									newCompareList.get(position).setTag("both");
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(R.string.you_are_each_other_favourite));
									img_favourite.setVisibility(View.VISIBLE);
									img_favourite.setBackgroundResource(R.drawable.fav_both);
								}
							}


						} else if (newCompareList.get(position).getTag().equals("both")) {
							if(pref.getString("email", null)!= null && listPrayerItem.get(0).getEmail() != null){
								if (pref.getString("email", null).equals(
										listPrayerItem.get(0).getEmail())) {

									newCompareList.get(position).setImagefavResource(0);
									newCompareList.get(position).setYourfav("");
									newCompareList.get(position).setTag("");
									img_favourite.setVisibility(View.GONE);
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(
											R.string.my_devotion));

								}else{
									newCompareList.get(position).setImagefavResource(R.drawable.fav_blue);
									newCompareList.get(position).setYourfav(getString(R.string.added_you_to_fav));
									newCompareList.get(position).setTag("other");
									tv_devotion.setVisibility(View.VISIBLE);
									tv_devotion.setText(getResources().getString(R.string.added_you_to_fav));
									img_favourite.setVisibility(View.VISIBLE);
									img_favourite.setBackgroundResource(R.drawable.fav_blue);
								}
							}


						}

						// Set Blue, Red, Green Count At the Top Of the Tabs...
						for (int j = 0; j < ((ApplicationSingleton) getApplicationContext()).dataContainer
								.getList_AcceptedDedication().size(); j++) {
							if (newCompareList
									.get(position)
									.getEmail()
									.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
											.getList_AcceptedDedication()
											.get(j).getEmail())) {

								if (((ApplicationSingleton) getApplicationContext()).dataContainer
										.getList_AcceptedDedication()
										.get(j)
										.getNature()
										.equals(getResources().getString(
												R.string.success_hebrew))) {

									PrayerScreen
											.getInstance()
											.getBlueTextView()
											.setText(
													String.valueOf(PrayerScreen
															.getInstance()
															.getBlueCount() + 1));
									PrayerScreen.getInstance().setBlueCount(
											PrayerScreen.getInstance()
													.getBlueCount() + 1);

								} else if (((ApplicationSingleton) getApplicationContext()).dataContainer
										.getList_AcceptedDedication()
										.get(j)
										.getNature()
										.equals(getResources().getString(
												R.string.medicine_hebrew))) {

									PrayerScreen
											.getInstance()
											.getGreenTextView()
											.setText(
													String.valueOf(PrayerScreen
															.getInstance()
															.getGreenCount() + 1));
									PrayerScreen.getInstance().setGreenCount(
											PrayerScreen.getInstance()
													.getGreenCount() + 1);

								} else if (((ApplicationSingleton) getApplicationContext()).dataContainer
										.getList_AcceptedDedication()
										.get(j)
										.getNature()
										.equals(getResources().getString(
												R.string.InMemoryof_hebrew))) {

									PrayerScreen
											.getInstance()
											.getRedTextView()
											.setText(
													String.valueOf(PrayerScreen
															.getInstance()
															.getRedCount() + 1));
									PrayerScreen.getInstance().setRedCount(
											PrayerScreen.getInstance()
													.getRedCount() + 1);

								}
							}
						}

					} catch (Exception e) {
						Log.e("Exception??", "PrayerScreen?? " + e.getMessage());
					}

					adapter.notifyDataSetChanged();
					PrayerAdapter.getInstance().GetFavourites(
							pref.getString("email", null),
							newCompareList.get(position).getEmail(), "true");
				}

			}
		});

		dialog.show();

	}

	// Get Radio Programs For Users to show using Retrofit library...
	public void GetFavourites(String email) {
		RestClientRadioPrograms.get().getFavourite(email,
				new Callback<Response>() {

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
							// listFavourite Email List Declare here...
							list_favEmail = new ArrayList<String>();
							listSer = new ArrayList<GetterSetter>();
							JSONObject jsonObjs = new JSONObject(out.toString());
							JSONArray jArray = jsonObjs.getJSONArray("value");
							for (int i = 0; i < jArray.length(); i++) {
								JSONObject jsonObj = jArray.getJSONObject(i);
								GetterSetter getset = new GetterSetter();
								getset.setEmail(jsonObj.getString("email"));
								getset.setFavEmail(jsonObj.getString("fav_email"));
								getset.setTag(jsonObj.getString("tag"));
								list_favEmail.add(jsonObj.getString("fav_email"));
								listSer.add(getset);
							}

							Utilities.insertArrayListPref(mContext, list_favEmail);
							Utilities.insertArrayListPrefForTag(mContext, listSer);

							setListValues();

						} catch (Exception e) {

							Log.e("Exception",
									"GetFavourites?? " + e.getMessage());
						}
					}

					@Override
					public void failure(RetrofitError arg0) {

					}

				});

	}

	// Set ZoomOut Page Transformation on View Pager
	public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.85f;
		private static final float MIN_ALPHA = 0.5f;

		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			int pageHeight = view.getHeight();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);

			} else if (position <= 1) { // [-1,1]
				// Modify the default slide transition to shrink the page as
				// well
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				float vertMargin = pageHeight * (1 - scaleFactor) / 2;
				float horzMargin = pageWidth * (1 - scaleFactor) / 2;
				if (position < 0) {
					view.setTranslationX(horzMargin - vertMargin / 2);
				} else {
					view.setTranslationX(-horzMargin + vertMargin / 2);
				}

				// Scale the page down (between MIN_SCALE and 1)
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

				// Fade the page relative to its size.
				view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
						/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}
	}

	/* onActivity Result */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_DEVOTE) {
				if(pref.getString("email", null)!=null && !pref.getString("email", null).equals("")){
					GetFavourites(pref.getString("email", null));
				}
			}
		}
	}
	
	// Pencil Click...
	private void checkClickForPencil() {
		isTabBarClick = false;

			if (Utilities.getUserIdDedicated(mContext) != null
					&& !Utilities.getUserIdDedicated(mContext).equals("")) {

				for (int i = 0; i < newCompareList.size(); i++) {
					if (Utilities.getUserIdDedicated(mContext).equals(
							newCompareList.get(i).getEmail())) {
						tag = newCompareList.get(i).getTag();
						position = i;
					}
				}

				if (!isTabBarClick) {
					ArrayList<GetterSetter> listPrayerItem = new ArrayList<GetterSetter>();
					ArrayList<String> list_count = new ArrayList<String>();
					int countItem = 0;

					for (int i = 0; i < ((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().size(); i++) {
						if (Utilities
								.getUserIdDedicated(mContext)
								.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
										.getList_AcceptedDedication().get(i)
										.getEmail())) {
							countItem = countItem + 1;
							GetterSetter getset = new GetterSetter();
							getset.setId(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getId());
							getset.setNature(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getNature());
							getset.setName(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getName());
							getset.setSex(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getSex());
							getset.setName_Optional(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getName_Optional());
							getset.setThere_Is(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getThere_Is());
							getset.setBlessing(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getBlessing());
							getset.setEmail(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getEmail());
							getset.setSigninwith(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getSigninwith());
							getset.setTime(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getTime());
							getset.setDate(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getDate());
							getset.setPublish(((ApplicationSingleton) getApplicationContext()).dataContainer
									.getList_AcceptedDedication().get(i)
									.getPublish());
							getset.setCount(countItem);
							getset.setTag(tag);
							getset.setImageResourceMan(R.drawable.gray_man);
							getset.setImageResourceMap(R.drawable.gray_map);
							list_count.add(String.valueOf(getset.getCount()));
							listPrayerItem.add(getset);

						}
					}

					if (listPrayerItem.size() > 0 && list_count.size() > 0) {
						showAlertBox(listPrayerItem, list_count, position);
					}

				}

			} else {

				Intent regIntent = new Intent(this, DevoteActivity.class);
				startActivityForResult(regIntent, REQUEST_CODE_DEVOTE);
				finish();

			}
		}
	
	public void checkForEveryUserDedication(int position){
		if (!isTabBarClick) {
			ArrayList<GetterSetter> listPrayerItem = new ArrayList<GetterSetter>();
			ArrayList<String> list_count = new ArrayList<String>();
			int countItem = 0;

			for (int i = 0; i < ((ApplicationSingleton) getApplicationContext()).dataContainer
					.getList_AcceptedDedication().size(); i++) {
				if (newCompareList
						.get(position)
						.getEmail()
						.equals(((ApplicationSingleton) getApplicationContext()).dataContainer
								.getList_AcceptedDedication().get(i)
								.getEmail())) {
					countItem = countItem + 1;
					GetterSetter getset = new GetterSetter();
					getset.setId(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getId());
					getset.setNature(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getNature());
					getset.setName(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getName());
					getset.setSex(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getSex());
					getset.setName_Optional(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getName_Optional());
					getset.setThere_Is(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getThere_Is());
					getset.setBlessing(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getBlessing());
					getset.setEmail(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getEmail());
					getset.setSigninwith(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getSigninwith());
					getset.setTime(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getTime());
					getset.setDate(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getDate());
					getset.setPublish(((ApplicationSingleton) getApplicationContext()).dataContainer
							.getList_AcceptedDedication().get(i)
							.getPublish());
					getset.setCount(countItem);
					getset.setTag(newCompareList.get(position).getTag());
					getset.setImageResourceMan(R.drawable.gray_man);
					getset.setImageResourceMap(R.drawable.gray_map);
					list_count.add(String.valueOf(getset.getCount()));
					listPrayerItem.add(getset);
				}
			}

			if (listPrayerItem.size() > 0 && list_count.size() > 0) {
				showAlertBox(listPrayerItem, list_count, position);
			}
		}
	}

	// onBackPressed()
	@Override
	public void onBackPressed() {
		Intent mintent = new Intent();
		setResult(RESULT_OK, mintent);
		finish();
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);
		super.onBackPressed();
	}

	public void GetFavouriteEmails() {
		GetFavourites(pref.getString("email", null));
	}

	public ProgressBar getProgressBar() {
		return pDialog;
	}

	public void setProgressBar(ProgressBar pDialog) {
		this.pDialog = pDialog;
	}

}
