package com.maranan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.maranan.adapters.NewsLetterAdapter;
import com.maranan.adapters.RadioLectureAdapter;
import com.maranan.adapters.ScreenSlidePagerAdapter;
import com.maranan.adapters.VideoLectureAdapter;
import com.maranan.adapters.VideoListAdapter;
import com.maranan.gcm.GCMNotificationIntentService;
import com.maranan.notifications.MyNotification;
import com.maranan.pojo.YoutubePojoVideoId;
import com.maranan.restclient.RestClient;
import com.maranan.restclient.RestClientRadioPrograms;
import com.maranan.services.MyRadioService;
import com.maranan.utils.Config;
import com.maranan.utils.ConnectionDetector;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.HorizontalTicker;
import com.maranan.utils.HorizontalTicker.TickerDirection;
import com.maranan.utils.ServiceHandler;
import com.maranan.utils.Utilities;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class VideoActivity extends SlidingFragmentActivity implements OnClickListener, OnTouchListener {
    private static VideoActivity mContext;
    private ViewPager mPager;
    private PageIndicator mIndicator;
    private PagerAdapter mPagerAdapter;
    private ConnectionDetector cd;

    private GetterSetter getterSetter;

    private ImageView img_top_vid_back, img_advertisement;
    private ImageView img_radio_bottom_view1;
    private TextView img_broadcast_sidebar;

    private Button btn_menu_new;
    private static Button btn_down;
    private static Button btn_up;
    private Button btn_share;
    private Button btn_play_vid_radio, btn_pause_vid_radio, btn_cross_view1;
    private Button btn_devote_new, btn_cancel;
    private static Button btn_red_dot;
    private static Button btn_play_vid, btn_play_text;

    private static ListView lv_videos_shows;
    private ListView lv_devotes_new;
    private ListView lv_bottom_pager;
    private static ListView lv_newsletter;

    private VideoListAdapter madapter;
    private static VideoLectureAdapter mVidAdapter;
    private RadioLectureAdapter radioAdapter;
    private NewsLetterAdapter newsAdapter;

    // private static ArrayList<GetterSetter> arrayListPlayListId;
    public static ArrayList<GetterSetter> arrayListVideo;
    private ArrayList<GetterSetter> listAcceptDedications;
    private ArrayList<GetterSetter> radioLinkList;
    private ArrayList<GetterSetter> radioLinkList2;
    private ArrayList<String> list;
    private ArrayList<GetterSetter> newArrayList;

    public String nexpageToken;
    public String playListID;

    private Boolean isOpen = false;
    private Boolean isLoading = false;
    private Boolean isInternetPresent = false;
    private Boolean inTheActivity = true;
    private Boolean isPlayerPlaying = false;
    private Boolean isPlayerPlaying2 = false;
    private static boolean check = false;
    private boolean isProgressShow = true;
    private Boolean isFirstTime = true;
    private Boolean isPlayerStart = false;
    private Boolean isBackPressed = false;
    private Boolean isPlayerCancel = false;
    private Boolean isFromAlert = false;
    public static Boolean isbtnDownPress = false;
    public static Boolean isPDFPageOpen = false;

    private static View footerView;
    private static View layout_uplist;
    private RelativeLayout dragView;
    private android.view.ViewGroup.LayoutParams params;

    private RelativeLayout relate_black_bar;
    public RelativeLayout relate_bottom_radio;
    private RelativeLayout handle;
    private RelativeLayout relative_view_up, relate_advertisement;
    public RelativeLayout relative_view;
    private RelativeLayout relative_up_text;
    private RelativeLayout relative_down_text;

    private static TextView tv_Video_Count, tv_total_dedications;
    private static TextView tv_vid_title_new;
    private TextView tv_lecture_title1, tv_lecture_description1;
    private TextView tv_lecture_title2, tv_lecture_description2;
    public TextView loading_media;
    public TextView tv_this_praying_about_it;
    private static TextView footer_tv;
    private static TextView tv_value_project_uplist;
    private static TextView tv_vid_des_uplist, tv_vid_des_uplist2;
    private TextView tv_time_start, tv_time_end;
    private static TextView tv_vid_title_uplist;
    private HorizontalScrollView mHorizontalScrollView;
    private HorizontalScrollView mHorizontalScrollView2;
    private LinearLayout mHorizontalScrollViewLayout;
    private LinearLayout mHorizontalScrollViewLayout2;

    private static RelativeLayout scroll_down;
    public static int REQUEST_CODE = 100;
    private int REQUEST_CODE_RADIO = 200;
    private int REQUEST_CODE_DEVOTE = 300;
    private int REQUEST_CODE_VIDEO_LIST = 400;
    private int REQUEST_CODE_LIST_OF_ALERTS = 500;
    private int REQUEST_CODE_LIST_OF_ALERTS_ITEM = 600;
    private final int GET = 1;
    public static int currentPosition = 0;
    private int heightListView;
    private static int pos = 0;
    public int width_device;
    private static int height_device;
    public static int height_relativeDown;
    private static int currentPage = 0;
    private static int count = 0;
    public static int position_list;
    private int count_dedications = 0;
    private int listLenght = 0;
    private int index = 0;
    public int total_size = 0;

    private SharedPreferences prefSize, pref;
    private Editor editor;

    private String radioAlertStr = "";

    private Handler h;
    private Handler handler, handler2, handler3, handler4;

    private Runnable myRunnable, myRun, myRun2, myRunnable2;
    private static ToggleButton btn_sequence;
    private SlidingMenu slidingMenu;

    private static ProgressBar progress_loading_more;

    private HashMap<String, String> hashMap;

    private static ToggleButton btn_sequence_uplist;
    private SeekBar seek_bar_radio1;
    private static SlidingUpPanelLayout mLayout;
    private String[] str;

    private NewsFlashManagerTITLE mNewsFlashManagerTitle;
    private NewsFlashManagerDES mNewsFlashManagerDes;

    private static View view_line_list;
    private static String live_video_id = "";
    private ProgressBar pDialog, pb_playpause;

    // VideoActivity Instance
    public static VideoActivity getInstance() {
        return mContext;
    }

    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        System.gc();

        try {
            // Here to check when alert alarm Notification are come and direct
            // open List of Alerts Activity....
            if (getIntent().getStringExtra("Message") != null)
                if (getIntent().getStringExtra("Message").equals("AlertAlarm")) {
                    inTheActivity = false;
                    isFromAlert = true;
                    if (((ApplicationSingleton) getApplicationContext()).player != null) {
                        if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
                            isPlayerPlaying = true;
                            disableTextMove();
                        }
                    }

                    Intent listAlerts = new Intent(VideoActivity.this, ListOfAlerts.class);
                    listAlerts.putExtra("isPlayerPlaying", isPlayerPlaying);
                    listAlerts.putExtra("AlertAlarm", "AlertAlarm");
                    startActivityForResult(listAlerts, REQUEST_CODE_LIST_OF_ALERTS);

                } else if (getIntent().getStringExtra("Message").equals("NewsLetterAlert")) {
                    // Show PDF Download Dialog According To The PDF Alert
                    // Comes...
                    Utilities.downloadAndOpenPDF(this, Utilities.getPDfPath(VideoActivity.this), Utilities.getPDfName(VideoActivity.this));
                } else if (getIntent().getStringExtra("Message").equals("VideoAlert")) {

                    // Showing video when video alert are publish by admin...
                    Intent videoIntent = new Intent(VideoActivity.this, VideoPlayerForNotification.class);
                    videoIntent.putExtra("video_Id", Utilities.getVideoId(VideoActivity.this));
                    startActivityForResult(videoIntent, REQUEST_CODE);
                } else if (getIntent().getStringExtra("Message").equals("LiveBroadcast")) {

                    // Showing live broadcast video publish by admin...
                    Intent videoIntent = new Intent(VideoActivity.this, VideoPlayerForNotification.class);
                    videoIntent.putExtra("video_Id", Utilities.getVideoId(VideoActivity.this));
                    startActivityForResult(videoIntent, REQUEST_CODE);
                }

            // Handle UnCaughtException Exception Handler
//            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(
//                    this));

            setContentView(R.layout.video_activity_new_second);
            cd = new ConnectionDetector(this);
            isInternetPresent = cd.isConnectingToInternet();
            mContext = VideoActivity.this;

            // get Device Height And Width For Set Bottom Sliding Drawer
            // Height...
            getDeviceHeightWidth();

            // initialize share preference...
            pref = getSharedPreferences("RegisterPref", MODE_PRIVATE);
            prefSize = getSharedPreferences("SizePref", MODE_PRIVATE);

            // Check Internet Connectivity...
            if (isInternetPresent) {

                arrayListVideo = (ArrayList<GetterSetter>) ((ApplicationSingleton) getApplicationContext()).dataContainer.getYoutubeList();
                Log.e("arrayListVideo", "arrayListVideo?? " + arrayListVideo.size());

                // Initialize View Here...
                initializeView();

                // Get Accepted Dedications Method Execute Here...
                new GetAcceptDedications().execute();

                // Get All Radio Programs Method Execute Here...
                GetAllRadioPrograms();

            } else {
                Utilities.showAlertDialog(VideoActivity.this,
                        "Internet Connection Error",
                        "Please connect to working Internet connection", false);
            }

        } catch (Exception e) {
            Log.e("VideoActivity", "Exception22?? " + e.getMessage());
        }

    }

    // Get Device Height And Width Here
    private void getDeviceHeightWidth() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        width_device = displayMetrics.widthPixels;
        height_device = displayMetrics.heightPixels;
    }

    // onWindowFocusChanged
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        updateListSizeInfo();
    }

    // updateSizeInfo
    private void updateListSizeInfo() {
        relate_black_bar = (RelativeLayout) findViewById(R.id.relate_black_bar);
        relative_view_up = (RelativeLayout) findViewById(R.id.relative_view_up);
        relate_advertisement = (RelativeLayout) findViewById(R.id.relate_advertisement);
        relative_view = (RelativeLayout) findViewById(R.id.relative_view);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
        height_relativeDown = relate_black_bar.getHeight();
        heightListView = (height_device - (relate_black_bar.getHeight() + relative_view_up.getHeight()));
        params.height = heightListView;

        // Set Bottom Drawer Height At RunTime...
        editor = prefSize.edit();
        editor.putInt("Size", heightListView);
        editor.commit();
    }

    // initialize View Here
    private void initializeView() {
        // SlideMenu To Open From Right Side...
        setBehindContentView(R.layout.right_menu);
        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.shadowright);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setBehindOffset(130);
        slidingMenu.setFadeDegree(0.35f);
        setSideBarWidthAccToScreenWidth();

        pDialog = (ProgressBar) findViewById(R.id.progressBar);
        pDialog.setVisibility(View.GONE);

        img_broadcast_sidebar = (TextView) findViewById(R.id.img_broadcast_sidebar);
        img_broadcast_sidebar.setOnClickListener(this);

        tv_this_praying_about_it = (TextView) findViewById(R.id.tv_this_praying_about_it);
        tv_this_praying_about_it.setOnClickListener(this);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setVisibility(View.VISIBLE);
        img_top_vid_back = (ImageView) findViewById(R.id.img_top_vid_back);
        img_top_vid_back.setVisibility(View.GONE);
        btn_menu_new = (Button) findViewById(R.id.btn_menu_new);
        btn_menu_new.setOnClickListener(this);
        btn_menu_new.setVisibility(View.INVISIBLE);
        btn_play_vid = (Button) findViewById(R.id.btn_play_vid);
        btn_play_vid.setOnClickListener(this);
        btn_play_text = (Button) findViewById(R.id.btn_play_text);
        btn_play_text.setOnClickListener(this);
        img_advertisement = (ImageView) findViewById(R.id.img_advertisement);
        layout_uplist = (View) findViewById(R.id.layout_uplist);
        layout_uplist.setVisibility(View.GONE);

        tv_value_project_uplist = (TextView) layout_uplist.findViewById(R.id.tv_value_project_uplist);
        tv_vid_des_uplist = (TextView) layout_uplist.findViewById(R.id.tv_vid_des_uplist);
        tv_vid_des_uplist.setText("");
        tv_vid_des_uplist2 = (TextView) layout_uplist.findViewById(R.id.tv_vid_des_uplist2);
        tv_vid_des_uplist2.setText("");
        btn_sequence_uplist = (ToggleButton) layout_uplist.findViewById(R.id.btn_sequence_uplist);
        tv_vid_title_uplist = (TextView) layout_uplist.findViewById(R.id.tv_vid_title_uplist);
        view_line_list = (View) findViewById(R.id.view_line_list);
        lv_videos_shows = (ListView) findViewById(R.id.lv_videos_shows);
        lv_videos_shows.setVisibility(View.GONE);

        handle = (RelativeLayout) findViewById(R.id.handle);

        lv_devotes_new = (ListView) findViewById(R.id.lv_devotes_new);
        lv_newsletter = (ListView) findViewById(R.id.lv_newsletter);
        lv_newsletter.setVisibility(View.GONE);

        relate_black_bar = (RelativeLayout) findViewById(R.id.relate_black_bar);
        relative_view_up = (RelativeLayout) findViewById(R.id.relative_view_up);

        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view_load_more, null, false);
        progress_loading_more = (ProgressBar) footerView.findViewById(R.id.progress_loading_more);
        progress_loading_more.setVisibility(View.GONE);
        footer_tv = (TextView) footerView.findViewById(R.id.footer_tv);
        footer_tv.setOnClickListener(this);
        footer_tv.setVisibility(View.GONE);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn_down.setOnClickListener(this);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_up.setOnClickListener(this);
        btn_share = (Button) findViewById(R.id.btn_share);
        btn_share.setOnClickListener(this);
        scroll_down = (RelativeLayout) findViewById(R.id.scroll_down);
        scroll_down.setVisibility(View.VISIBLE);

        btn_sequence = (ToggleButton) findViewById(R.id.btn_sequence);
        btn_devote_new = (Button) findViewById(R.id.btn_devote_new);
        btn_devote_new.setOnClickListener(this);
        tv_vid_title_new = (TextView) findViewById(R.id.tv_vid_title_new);
        tv_vid_title_new.setText("");
        tv_Video_Count = (TextView) findViewById(R.id.tv_Video_Count);
        tv_Video_Count.setText("");
        btn_red_dot = (Button) findViewById(R.id.btn_red_dot);
        btn_red_dot.setVisibility(View.GONE);

        tv_total_dedications = (TextView) findViewById(R.id.tv_total_dedications);

        // Set Total Size Count Of NewsLetter...
        if (((ApplicationSingleton) getApplicationContext()).dataContainer.getList_dates().size() > 0) {
            setTotalSize(0);

            if (((ApplicationSingleton) getApplicationContext()).dataContainer.getList_NewsletterUp().size() > 0) {

                setTotalSize(getTotalSize() + ((ApplicationSingleton) getApplicationContext()).dataContainer.getList_NewsletterUp().size());

                for (int i = 0; i < ((ApplicationSingleton) getApplicationContext()).dataContainer.getList_dates().size(); i++) {

                    if (((ApplicationSingleton) getApplicationContext()).dataContainer.getHashListNewsLetter() != null) {

                        if (!((ApplicationSingleton) getApplicationContext()).dataContainer.getList_dates().get(i).equals("")
                                && ((ApplicationSingleton) getApplicationContext()).dataContainer.getHashListNewsLetter().size() > 0) {
                            setTotalSize(getTotalSize() + ((ApplicationSingleton) getApplicationContext()).dataContainer.getHashListNewsLetter()
                                    .get(((ApplicationSingleton) getApplicationContext()).dataContainer.getList_dates().get(i)).size());
                        }
                    }
                }
            }
        }

        // Get Array List Values Here...
        if (arrayListVideo != null) {
            if (arrayListVideo.size() > 0) {

                // Set Title, Description And Total Video's Shown On Video
                // Thumbnails Screen At Top
                if (!arrayListVideo.get(currentPage).getList().get(0).get("Title").equals(""))

                    tv_vid_title_new.setText(Utilities.decodeImoString(arrayListVideo.get(currentPage).getList().get(0).get("Title")));

                if (!arrayListVideo.get(currentPage).getList().get(0)
                        .get("Description").equals(""))

                    if (!arrayListVideo.get(currentPage).getList().get(0)
                            .get("Unique_Video_Id").equals("")) {

                        tv_vid_des_uplist2.setVisibility(View.GONE);
                        tv_vid_des_uplist.setVisibility(View.VISIBLE);
                        tv_vid_des_uplist.setText(Utilities.decodeImoString(arrayListVideo.get(currentPage).getList().get(0).get("Description")));
                    } else {
                        tv_vid_des_uplist2.setVisibility(View.VISIBLE);
                        tv_vid_des_uplist.setVisibility(View.GONE);
                        tv_vid_des_uplist2.setText(Utilities.decodeImoString(arrayListVideo.get(currentPage).getList().get(0).get("Description")));
                    }

                if (!arrayListVideo.get(currentPage).getList().get(0).get("TotalVideos").equals(""))

                    if (!arrayListVideo.get(currentPage).getList().get(0).get("Unique_Video_Id").equals("")) {
                        tv_vid_title_uplist.setVisibility(View.VISIBLE);
                        btn_sequence_uplist.setVisibility(View.VISIBLE);
                        view_line_list.setVisibility(View.VISIBLE);
                        lv_videos_shows.setVisibility(View.GONE);
                        btn_red_dot.setVisibility(View.GONE);
                        tv_Video_Count.setText(arrayListVideo.get(currentPage).getList().get(0).get("TotalVideos"));
                        tv_Video_Count.setTextColor(Color.parseColor("#4DB9C9"));

                        if (arrayListVideo.get(currentPage).getList().get(0).get("SequenceStatus").equals("true")) {
                            check = true;
                            btn_sequence_uplist.setChecked(check);
                        } else {
                            check = false;
                            btn_sequence_uplist.setChecked(check);
                        }

                    } else {
                        tv_vid_title_uplist.setVisibility(View.GONE);
                        btn_sequence_uplist.setVisibility(View.GONE);
                        view_line_list.setVisibility(View.GONE);
                        lv_videos_shows.setVisibility(View.GONE);
                        btn_red_dot.setVisibility(View.VISIBLE);
                        tv_Video_Count.setText(getString(R.string.live));
                        tv_Video_Count.setTextColor(Color.parseColor("#ffffff"));
                        check = false;
                        live_video_id = arrayListVideo.get(currentPage).getList().get(0).get("VideoId");
                    }

                // To check total videos count is greater than 50
                // if
                // (Integer.parseInt(arrayListVideo.get(currentPage).getList().get(0).get("TotalVideos"))
                // > 50) {
                // lv_videos_shows.addFooterView(footerView);
                // playListID =
                // arrayListPlayListId.get(currentPage).getPlayListId();
                // position_list = currentPage;
                // nexpageToken =
                // arrayListVideo.get(currentPage).getList().get(0).get("NextPageToken");
                // }

                for (int i = 0; i < arrayListVideo.get(currentPage).getList().size(); i++) {
                    // Get View Count Here...
                    getViewCounts(arrayListVideo.get(currentPage).getList().get(i).get("VideoId"));
                }

            } else {
                setPdfValues();
            }
        }

        setAdapters();
        setOnCheckChangeListenerOnToggle();

        // Initialize Radio Buttons Ui Here...
        btn_play_vid_radio = (Button) findViewById(R.id.btn_play_vid_radio);
        btn_play_vid_radio.setOnClickListener(this);
        btn_play_vid_radio.setVisibility(View.INVISIBLE);
        btn_pause_vid_radio = (Button) findViewById(R.id.btn_pause_vid_radio);
        btn_pause_vid_radio.setOnClickListener(this);

        pb_playpause = (ProgressBar) findViewById(R.id.pb_playpause);
        pb_playpause.setVisibility(View.GONE);

        // New SlidingUpPanelLayout Declaration Here...
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setCoveredFadeColor(android.R.color.transparent);
        dragView = (RelativeLayout) findViewById(R.id.dragView);
        loading_media = (TextView) findViewById(R.id.loading_media);
        loading_media.setVisibility(View.VISIBLE);
        handle = (RelativeLayout) findViewById(R.id.handle);
        handle.setVisibility(View.GONE);
        handle.setOnTouchListener(this);

        lv_bottom_pager = (ListView) findViewById(R.id.lv_bottom_pager);
        lv_bottom_pager.setVisibility(View.GONE);
        btn_cross_view1 = (Button) findViewById(R.id.btn_cross_view1);
        btn_cross_view1.setOnClickListener(this);
        btn_cross_view1.setVisibility(View.INVISIBLE);
        seek_bar_radio1 = (SeekBar) findViewById(R.id.seek_bar_radio1);
        seek_bar_radio1.setProgress(0);
        seek_bar_radio1.setThumb(null);
        seek_bar_radio1.setVisibility(View.VISIBLE);
        seek_bar_radio1.setOnSeekBarChangeListener(seekBarChangeListener);
        // seek_bar_radio1.setPadding(-1, 0, -1, 0);
        tv_time_start = (TextView) findViewById(R.id.tv_time_start);
        tv_time_end = (TextView) findViewById(R.id.tv_time_end);
        tv_time_start.setVisibility(View.GONE);
        tv_time_end.setVisibility(View.GONE);

        tv_lecture_title2 = (TextView) findViewById(R.id.tv_lecture_title2);
        tv_lecture_description2 = (TextView) findViewById(R.id.tv_lecture_description2);
        tv_lecture_title2.setOnTouchListener(this);
        tv_lecture_description2.setOnTouchListener(this);

        relative_up_text = (RelativeLayout) findViewById(R.id.relative_up_text);
        relative_up_text.setVisibility(View.VISIBLE);
        relative_down_text = (RelativeLayout) findViewById(R.id.relative_down_text);
        relative_down_text.setVisibility(View.GONE);

        tv_lecture_title1 = new TextView(this);
        tv_lecture_title1.setTextSize(15);
        tv_lecture_title1.setTextColor(getResources().getColor(
                R.color.text_color_blue));

        tv_lecture_description1 = new TextView(this);
        tv_lecture_description1.setTextSize(15);
        tv_lecture_description1.setTextColor(getResources().getColor(
                R.color.white));

        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.mHorizontalScrollView);
        mHorizontalScrollView.setOnTouchListener(this);
        mHorizontalScrollViewLayout = (LinearLayout) findViewById(R.id.mHorizontalScrollViewLayout);
        mHorizontalScrollViewLayout.setOnTouchListener(this);
        mHorizontalScrollView2 = (HorizontalScrollView) findViewById(R.id.mHorizontalScrollView2);
        mHorizontalScrollView2.setOnTouchListener(this);
        mHorizontalScrollViewLayout2 = (LinearLayout) findViewById(R.id.mHorizontalScrollViewLayout2);
        mHorizontalScrollViewLayout2.setOnTouchListener(this);
        mHorizontalScrollViewLayout.addView(tv_lecture_title1);
        mHorizontalScrollViewLayout2.addView(tv_lecture_description1);

        initializeRadioFlashesTitle();
        initializeRadioFlashesDes();

        img_radio_bottom_view1 = (ImageView) findViewById(R.id.img_radio_bottom_view1);
        isOpen = true;
        params = dragView.getLayoutParams();

        bottomViewPanelSlideListener();
        onListViewTouchListener();
        onListViewItemClickListener();
    }

    // Handle Side Bar Width According To Device Screen Width...
    private void setSideBarWidthAccToScreenWidth() {
        if (width_device > 0 && width_device < 320) {

            slidingMenu.setBehindWidth(100);

        } else if (width_device > 320 && width_device < 480) {

            slidingMenu.setBehindWidth(150);

        } else if (width_device > 480 && width_device < 540) {

            slidingMenu.setBehindWidth(200);

        } else if (width_device > 540 && width_device < 1280) {

            slidingMenu.setBehindWidth(300);

        } else if (width_device > 1280 && width_device < 1920) {

            slidingMenu.setBehindWidth(400);

        } else {

            slidingMenu.setBehindWidth(400);
        }

    }

    // Set Bootom View Panel Slider Listener...
    private void bottomViewPanelSlideListener() {
        mLayout.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelHidden(View panel) {
            }

            @Override
            public void onPanelExpanded(View panel) {
                if (radioLinkList != null) {
                    seek_bar_radio1.setThumb(getResources().getDrawable(R.drawable.circle));
                    seek_bar_radio1.setVisibility(View.VISIBLE);
                    // seek_bar_radio1.setPadding(15, 0, 15, 0);
                    tv_time_start.setVisibility(View.VISIBLE);
                    tv_time_end.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelCollapsed(View panel) {
                if (radioLinkList != null) {
                    seek_bar_radio1.setThumb(null);
                    // seek_bar_radio1.setPadding(-1, 0, -1, 0);
                    seek_bar_radio1.setVisibility(View.VISIBLE);
                    tv_time_start.setVisibility(View.GONE);
                    tv_time_end.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPanelAnchored(View panel) {

            }
        });
    }

    // Set Pager Adapter And List Adapter....
    private void setAdapters() {
        // Set Pager Adapter Here...
        mPagerAdapter = new ScreenSlidePagerAdapter(VideoActivity.this, arrayListVideo);
        mPager.setAdapter(mPagerAdapter);

        // Set View Pager Adapter On View Pager
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        PageListener pageListener = new PageListener();
        mPager.setOnPageChangeListener(pageListener);
        mIndicator.setViewPager(mPager);
        mIndicator.setOnPageChangeListener(pageListener);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        if (arrayListVideo.size() > 0) {
            // Set List View For Show Videos Adapter Here...
            mVidAdapter = new VideoLectureAdapter(VideoActivity.this,
                    arrayListVideo.get(currentPage).getList());
            lv_videos_shows.setAdapter(mVidAdapter);
        }

        // Set NewsLetter Adapter...
        newsAdapter = new NewsLetterAdapter(VideoActivity.this);
        lv_newsletter.setAdapter(newsAdapter);

    }

    // Set List View Touch Listener...
    private void onListViewTouchListener() {
        lv_devotes_new.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        lv_videos_shows.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        lv_bottom_pager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        lv_newsletter.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    // Set List View Items Click Listener...
    private void onListViewItemClickListener() {
        // Video's List View Itme Click Listener...
        lv_videos_shows.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                handler.removeCallbacks(myRunnable);
                handler.removeCallbacks(null);
                inTheActivity = false;

                if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
                    if (((ApplicationSingleton) getApplicationContext()).player2.isPlaying()) {
                        ((ApplicationSingleton) getApplicationContext()).player2.pause();
                        isPlayerPlaying = false;
                        isPlayerPlaying2 = true;
                    }
                }

                if (((ApplicationSingleton) getApplicationContext()).player != null) {
                    if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
                        ((ApplicationSingleton) getApplicationContext()).player.pause();

                        disableTextMove();
                        btn_cross_view1.setVisibility(View.VISIBLE);
                        btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                        btn_play_vid_radio.setVisibility(View.VISIBLE);
                        pb_playpause.setVisibility(View.GONE);
                        handle.setVisibility(View.VISIBLE);
                        lv_bottom_pager.setVisibility(View.VISIBLE);
                        buttonPlayHandleAnimation();
                        isPlayerPlaying = true;
                        isPlayerPlaying2 = false;
                    }
                }

                Intent videoIntent = new Intent(VideoActivity.this, VideoDrawerActivity.class);
                videoIntent.putExtra("Check", check);
                videoIntent.putExtra("isPlayerPlaying", isPlayerPlaying);
                videoIntent.putExtra("isPlayerPlaying2", isPlayerPlaying2);
                videoIntent.putExtra("ArrayListFull", arrayListVideo.get(currentPage).getList());
                videoIntent.putExtra("ArrayList", VideoLectureAdapter.getInstance().getNewArrayList());
                videoIntent.putExtra("video_Id", VideoLectureAdapter.getInstance().getNewArrayList().get(position).get("VideoId"));
                startActivityForResult(videoIntent, VideoActivity.REQUEST_CODE);
            }
        });

        // Dedivation Alerts Click Listener...
        lv_devotes_new.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                inTheActivity = false;
                handler.removeCallbacks(myRunnable);
                handler.removeCallbacks(null);

                if (pref.getString("email", null) == null) {

                    Intent registerInt = new Intent(VideoActivity.this, LogInActivity.class);
                    startActivityForResult(registerInt, REQUEST_CODE_LIST_OF_ALERTS_ITEM);

                } else {
                    Utilities.setComingFromDedicationHome(mContext, true);

                    Intent videoIntent = new Intent(VideoActivity.this, PrayerScreen.class);
                    videoIntent.putExtra("email", ((ApplicationSingleton) getApplicationContext()).dataContainer
                            .getList_AcceptedDedication().get(position).getEmail());
                    videoIntent.putExtra("video_Id", live_video_id);
                    startActivityForResult(videoIntent, REQUEST_CODE_LIST_OF_ALERTS_ITEM);
                }
            }
        });

        // Bottom List View Item Click Listener...
        lv_bottom_pager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (isInternetPresent) {
                    isPlayerStart = false;
                    isPlayerCancel = false;
                    relative_up_text.setVisibility(View.VISIBLE);
                    relative_down_text.setVisibility(View.GONE);
                    mLayout.setPanelState(PanelState.COLLAPSED);
                    stopService(new Intent(VideoActivity.this, MyRadioService.class));
                    // Set SharePrefrence For Player Is Stop To Play...
                    Utilities.setSharedPrefPlayer(VideoActivity.this, isPlayerStart, "", -1);
                    Utilities.setSharedPrefPlayerCancel(VideoActivity.this, isPlayerCancel, "");

                    if (((ApplicationSingleton) getApplicationContext()).player != null) {

                        if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {

                            MyRadioService.getInstance().getHandler().removeCallbacks(MyRadioService.getInstance().getRunnable());
                            ((ApplicationSingleton) getApplicationContext()).player.stop();
                            ((ApplicationSingleton) getApplicationContext()).player.release();
                            handleUiWhenClickOnListItem(arg2);
                        } else {
                            handleUiWhenClickOnListItem(arg2);
                        }
                    } else {
                        handleUiWhenClickOnListItem(arg2);
                    }
                } else {
                    Utilities.showAlertDialog(VideoActivity.this, "Internet Connection Error", "Please connect to working Internet connection", false);
                }
            }
        });

        lv_newsletter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    // Handle UI When Click On The List Item...
    private void handleUiWhenClickOnListItem(int arg2) {
        ((ApplicationSingleton) getApplicationContext()).player = new MediaPlayer();
        btn_cross_view1.setVisibility(View.INVISIBLE);
        tv_time_start.setText("");
        tv_time_start.setText("00:00:00");
        seek_bar_radio1.setProgress(0);
        btn_pause_vid_radio.setVisibility(View.INVISIBLE);
        btn_play_vid_radio.setVisibility(View.INVISIBLE);
        pb_playpause.setVisibility(View.VISIBLE);
        disableTextMove();
        setRadioItemClickListener(arg2);
    }

    // Set On Toggle Button Check Change Listener...
    private void setOnCheckChangeListenerOnToggle() {

        btn_sequence.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    check = isChecked;
                } else {
                    check = isChecked;
                }
            }
        });

        btn_sequence_uplist.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    check = isChecked;
                } else {
                    check = isChecked;
                }
            }
        });

    }

    // Set Radio Lecture Item click Listener...
    private void setRadioItemClickListener(int arg2) {
        changeIndex(arg2, true);
        seek_bar_radio1.setProgress(0);
        startService(new Intent(VideoActivity.this, MyRadioService.class).putExtra("RadioUrl", getterSetter.getRadio_programs()));

    }

    // Page Change Listener Declare Here...
    public static class PageListener extends SimpleOnPageChangeListener {
        public void onPageSelected(int position) {
            currentPage = position;
            if (arrayListVideo.size() > 0) {
                if (currentPage != arrayListVideo.size()) {

                    if (!arrayListVideo.get(currentPage).getList().get(0).get("Title").equals(""))

                        tv_vid_title_new.setText("");
                    tv_vid_title_new.setText(Utilities.decodeImoString(arrayListVideo.get(currentPage).getList().get(0).get("Title")));

                    if (!arrayListVideo.get(currentPage).getList().get(0).get("Description").equals(""))

                        if (!arrayListVideo.get(currentPage).getList().get(0).get("Unique_Video_Id").equals("")) {
                            tv_vid_des_uplist2.setVisibility(View.GONE);
                            tv_vid_des_uplist.setVisibility(View.VISIBLE);
                            tv_vid_des_uplist.setText("");
                            tv_vid_des_uplist.setText(Utilities.decodeImoString(arrayListVideo.get(currentPage).getList().get(0).get("Description")));
                        } else {
                            tv_vid_des_uplist.setVisibility(View.GONE);
                            tv_vid_des_uplist2.setVisibility(View.VISIBLE);
                            tv_vid_des_uplist2.setText("");
                            tv_vid_des_uplist2.setText(Utilities.decodeImoString(arrayListVideo.get(currentPage).getList().get(0).get("Description")));
                        }

                    if (!arrayListVideo.get(currentPage).getList().get(0).get("TotalVideos").equals(""))

                        tv_Video_Count.setText("");

                    if (!arrayListVideo.get(currentPage).getList().get(0).get("Unique_Video_Id").equals("")) {
                        tv_vid_title_uplist.setVisibility(View.VISIBLE);
                        btn_sequence_uplist.setVisibility(View.VISIBLE);
                        view_line_list.setVisibility(View.VISIBLE);
                        lv_videos_shows.setVisibility(View.GONE);
                        btn_red_dot.setVisibility(View.GONE);
                        tv_Video_Count.setText(arrayListVideo.get(currentPage).getList().get(0).get("TotalVideos"));
                        tv_Video_Count.setTextColor(Color.parseColor("#4DB9C9"));
                        if (arrayListVideo.get(currentPage).getList().get(0).get("SequenceStatus").equals("true")) {
                            check = true;
                            btn_sequence_uplist.setChecked(check);
                        } else {
                            check = false;
                            btn_sequence_uplist.setChecked(check);
                        }
                    } else {
                        tv_vid_title_uplist.setVisibility(View.GONE);
                        btn_sequence_uplist.setVisibility(View.GONE);
                        view_line_list.setVisibility(View.GONE);
                        lv_videos_shows.setVisibility(View.GONE);
                        btn_red_dot.setVisibility(View.VISIBLE);
                        tv_Video_Count.setText(mContext
                                .getString(R.string.live));
                        tv_Video_Count.setTextColor(Color.parseColor("#ffffff"));
                        check = false;
                    }

                    // To check total videos count is greater than 50
                    // if
                    // (Integer.parseInt(arrayListVideo.get(currentPage).getList().get(0).get("TotalVideos"))
                    // > 50) {
                    // lv_videos_shows.addFooterView(footerView);
                    // progress_loading_more.setVisibility(View.GONE);
                    // footer_tv.setVisibility(View.VISIBLE);
                    // playListID =
                    // arrayListPlayListId.get(currentPage).getPlayListId();
                    // position_list = currentPage;
                    // nexpageToken =
                    // arrayListVideo.get(currentPage).getList().get(0).get("NextPageToken");
                    //
                    // }

                    progress_loading_more.setVisibility(View.GONE);
                    footer_tv.setVisibility(View.GONE);

                    // Set List View For Show Videos Adapter Here
                    mVidAdapter = new VideoLectureAdapter(mContext, arrayListVideo.get(currentPage).getList());
                    mVidAdapter.notifyDataSetChanged();
                    lv_videos_shows.setAdapter(mVidAdapter);

                    tv_value_project_uplist.setText("0");
                    pos = 0;
                    count = 0;

                    try {
                        if (currentPage != arrayListVideo.size()) {
                            for (int i = 0; i < arrayListVideo.get(currentPage).getList().size(); i++) {
                                // Get View Count Here...
                                mContext.getViewCounts(arrayListVideo.get(currentPage).getList().get(i).get("VideoId"));
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Exception", "VideoActivityCount?? " + e.getMessage());
                    }

                    isPDFPageOpen = false;

                    if (isbtnDownPress) {
                        btn_sequence.setVisibility(View.GONE);
                        btn_up.setVisibility(View.VISIBLE);
                        btn_down.setVisibility(View.INVISIBLE);
                        lv_videos_shows.setVisibility(View.VISIBLE);
                        layout_uplist.setVisibility(View.VISIBLE);
                        scroll_down.setVisibility(View.GONE);
                        lv_newsletter.setVisibility(View.GONE);
                        btn_play_vid.setBackgroundResource(R.drawable.play_video_new);
                        btn_play_text.setBackgroundResource(R.drawable.video_text);
                        btn_play_vid.setClickable(true);
                        btn_play_text.setClickable(true);
                    } else {
                        btn_sequence.setVisibility(View.GONE);
                        btn_up.setVisibility(View.INVISIBLE);
                        btn_down.setVisibility(View.VISIBLE);
                        lv_videos_shows.setVisibility(View.GONE);
                        layout_uplist.setVisibility(View.GONE);
                        scroll_down.setVisibility(View.VISIBLE);
                        lv_newsletter.setVisibility(View.GONE);
                        btn_play_vid.setBackgroundResource(R.drawable.play_video_new);
                        btn_play_text.setBackgroundResource(R.drawable.video_text);
                        btn_play_vid.setClickable(true);
                        btn_play_text.setClickable(true);
                    }
                } else {
                    setPdfValues();
                }
            } else {
                setPdfValues();
            }
        }

    }

    // Set Newsletter Values
    public static void setPdfValues() {
        if (((ApplicationSingleton) getInstance().getApplicationContext()).dataContainer.getList_NewsletterUp().size() > 0) {
            isPDFPageOpen = true;
            tv_vid_title_new.setText("");
            tv_vid_title_new.setText(Utilities.decodeImoString(((ApplicationSingleton) getInstance()
                    .getApplicationContext()).dataContainer.getList_NewsletterUp().get(0).getTitle()));
            btn_red_dot.setVisibility(View.GONE);
            tv_Video_Count.setText("");
            tv_Video_Count.setText(String.valueOf(VideoActivity.getInstance().getTotalSize()));
            tv_Video_Count.setTextColor(Color.parseColor("#4DB9C9"));

            if (isbtnDownPress) {
                btn_down.setVisibility(View.INVISIBLE);
                btn_up.setVisibility(View.VISIBLE);
            } else {
                btn_down.setVisibility(View.INVISIBLE);
                btn_up.setVisibility(View.VISIBLE);
            }

            btn_sequence.setVisibility(View.GONE);
            lv_videos_shows.setVisibility(View.GONE);
            layout_uplist.setVisibility(View.GONE);
            scroll_down.setVisibility(View.GONE);
            btn_sequence.setVisibility(View.GONE);
            lv_newsletter.setVisibility(View.VISIBLE);
            btn_play_vid.setBackgroundResource(R.drawable.icon_play_news);
            btn_play_text.setBackgroundResource(R.drawable.icon_text_news);
            btn_play_vid.setClickable(true);
            btn_play_text.setClickable(true);

        } else {
            btn_down.setVisibility(View.INVISIBLE);
            btn_up.setVisibility(View.INVISIBLE);
            btn_sequence.setVisibility(View.GONE);
            lv_newsletter.setVisibility(View.GONE);
            btn_play_vid.setVisibility(View.INVISIBLE);
            btn_play_text.setVisibility(View.INVISIBLE);
        }
    }

    // Here to slide close automatically after 5 sec...
    private void sliderCloseAutomatically(final SlidingUpPanelLayout mLayout2) {
        h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                mLayout2.setPanelState(PanelState.COLLAPSED);
                isOpen = false;
            }
        }, 5000);

    }

    // onClick Listener
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_up:
                isbtnDownPress = false;

                if (!isPDFPageOpen) {
                    btn_sequence.setVisibility(View.GONE);
                    btn_up.setVisibility(View.INVISIBLE);
                    btn_down.setVisibility(View.VISIBLE);
                    lv_videos_shows.setVisibility(View.GONE);
                    layout_uplist.setVisibility(View.GONE);
                    scroll_down.setVisibility(View.VISIBLE);
                    lv_newsletter.setVisibility(View.GONE);
                    btn_play_vid.setBackgroundResource(R.drawable.play_video_new);
                    btn_play_text.setBackgroundResource(R.drawable.video_text);
                    btn_play_vid.setClickable(true);
                    btn_play_text.setClickable(true);

                } else {
                    btn_sequence.setVisibility(View.GONE);
                    btn_up.setVisibility(View.INVISIBLE);
                    btn_down.setVisibility(View.VISIBLE);
                    lv_videos_shows.setVisibility(View.GONE);
                    layout_uplist.setVisibility(View.GONE);
                    scroll_down.setVisibility(View.VISIBLE);
                    btn_sequence.setVisibility(View.GONE);
                    lv_newsletter.setVisibility(View.GONE);
                    btn_play_vid.setBackgroundResource(R.drawable.icon_play_news);
                    btn_play_text.setBackgroundResource(R.drawable.icon_text_news);
                    btn_play_vid.setClickable(true);
                    btn_play_text.setClickable(true);
                }

                if (mLayout != null && (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
                    mLayout.setPanelState(PanelState.COLLAPSED);
                }
                break;

            case R.id.btn_down:

                isbtnDownPress = true;

                if (!isPDFPageOpen) {
                    btn_sequence.setVisibility(View.GONE);
                    btn_up.setVisibility(View.VISIBLE);
                    btn_down.setVisibility(View.INVISIBLE);
                    lv_videos_shows.setVisibility(View.VISIBLE);
                    layout_uplist.setVisibility(View.VISIBLE);
                    scroll_down.setVisibility(View.GONE);
                    lv_newsletter.setVisibility(View.GONE);
                    btn_play_vid.setBackgroundResource(R.drawable.play_video_new);
                    btn_play_text.setBackgroundResource(R.drawable.video_text);
                    btn_play_vid.setClickable(true);
                    btn_play_text.setClickable(true);

                } else {
                    btn_sequence.setVisibility(View.GONE);
                    btn_up.setVisibility(View.VISIBLE);
                    btn_down.setVisibility(View.INVISIBLE);
                    lv_videos_shows.setVisibility(View.GONE);
                    layout_uplist.setVisibility(View.GONE);
                    scroll_down.setVisibility(View.GONE);
                    btn_sequence.setVisibility(View.GONE);
                    lv_newsletter.setVisibility(View.VISIBLE);
                    btn_play_vid.setBackgroundResource(R.drawable.icon_play_news);
                    btn_play_text.setBackgroundResource(R.drawable.icon_text_news);
                    btn_play_vid.setClickable(true);
                    btn_play_text.setClickable(true);
                }

                if (mLayout != null && (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
                    mLayout.setPanelState(PanelState.COLLAPSED);
                }
                break;

            case R.id.btn_devote_new:
                inTheActivity = false;
                handler.removeCallbacks(myRunnable);
                handler.removeCallbacks(null);

                Intent regIntent = new Intent(VideoActivity.this, DevoteActivity.class);
                startActivityForResult(regIntent, REQUEST_CODE_DEVOTE);
                break;

            case R.id.btn_menu_new:

                break;

            case R.id.btn_share:
                inTheActivity = false;
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>Maranan</p>"));
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;

            case R.id.btn_cancel:
                img_advertisement.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                relate_advertisement.setVisibility(View.GONE);
                h.removeCallbacks(null);
                break;

            case R.id.footer_tv:
                progress_loading_more.setVisibility(View.VISIBLE);
                footer_tv.setVisibility(View.GONE);
                new GetYouTubeVideosID().execute(playListID);
                break;

            case R.id.btn_play_vid_radio:
                params.height = heightListView;
                if (isInternetPresent) {
                    if (radioLinkList != null) {
                        if (isLoading == false) {

                            if (MyNotification.getInstance() != null) {

                                if (((ApplicationSingleton) getApplicationContext()).player != null) {
                                    if (!((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {

                                        if (prefSize != null) {
                                            params.height = prefSize.getInt("Size", 0);
                                        }

                                        ((ApplicationSingleton) getApplicationContext()).player.start();

                                        if (MyNotification.getInstance().isNotificationGenerate()) {
                                            MyNotification.getInstance().getNotificationForChange(true);
                                        }
                                        btn_play_vid_radio.setVisibility(View.INVISIBLE);
                                        btn_pause_vid_radio.setVisibility(View.INVISIBLE);

                                        lv_bottom_pager.setVisibility(View.VISIBLE);
                                        handle.setVisibility(View.VISIBLE);
                                        pb_playpause.setVisibility(View.GONE);
                                        btn_cross_view1.setVisibility(View.VISIBLE);
                                        relative_up_text.setVisibility(View.GONE);
                                        relative_down_text.setVisibility(View.VISIBLE);
                                        updateSeekBar(((ApplicationSingleton) getApplicationContext()).player, seek_bar_radio1);
                                        // Start Thread For Pause Button Visible
                                        // Invisible After 2 Sec...
                                        buttonPauseHandleAnimation();
                                        enableTextMove();
                                        isLoading = false;
                                        isPlayerCancel = false;
                                        Utilities.setSharedPrefPlayerCancel(this, isPlayerCancel, "");
                                    }
                                } else {
                                    startPlayRadio();
                                }

                            } else {
                                startPlayRadio();
                            }
                        } else {
                            Utilities.showSnackbar(mContext, "Loading...");
                        }

                    } else {
                        Utilities.showSnackbar(mContext, "Radio Programs Not Available");
                    }
                } else {
                    Utilities.showAlertDialog(VideoActivity.this, "Internet Connection Error", "Please connect to working Internet connection", false);
                }
                break;

            case R.id.btn_pause_vid_radio:
                params.height = heightListView;
                if (isInternetPresent) {

                    isPlayerStart = true;
                    isPlayerCancel = false;
                    disableTextMove();
                    btn_cross_view1.setVisibility(View.VISIBLE);
                    btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                    pb_playpause.setVisibility(View.GONE);
                    btn_play_vid_radio.setVisibility(View.VISIBLE);
                    handle.setVisibility(View.VISIBLE);
                    lv_bottom_pager.setVisibility(View.VISIBLE);
                    buttonPlayHandleAnimation();
                    // Set Player Prefrence Values For handle player is playing or
                    // not
                    Utilities.setSharedPrefPlayer(this, isPlayerStart, getterSetter.getId(), ((ApplicationSingleton) getApplicationContext()).player.getCurrentPosition());
                    Utilities.setSharedPrefPlayerCancel(this, isPlayerCancel, "");

                    if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
                        ((ApplicationSingleton) getApplicationContext()).player.pause();
                    }

                    if (MyNotification.getInstance() != null) {
                        if (MyNotification.getInstance().isNotificationGenerate()) {
                            MyNotification.getInstance().getNotificationForChange(false);
                        }
                    }

                } else {
                    Utilities.showAlertDialog(VideoActivity.this, "Internet Connection Error", "Please connect to working Internet connection", false);
                }
                break;

            case R.id.btn_cross_view1:

                params.height = heightListView;
                if (isInternetPresent) {
                    disableTextMove();
                    isPlayerStart = false;
                    isPlayerCancel = false;
                    btn_cross_view1.setVisibility(View.INVISIBLE);
                    pb_playpause.setVisibility(View.GONE);
                    tv_time_start.setText("");
                    tv_time_start.setText("00:00:00");
                    seek_bar_radio1.setProgress(0);
                    btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                    btn_play_vid_radio.setVisibility(View.VISIBLE);
                    handle.setVisibility(View.VISIBLE);
                    lv_bottom_pager.setVisibility(View.VISIBLE);
                    relative_up_text.setVisibility(View.VISIBLE);
                    relative_down_text.setVisibility(View.GONE);

                    if (mLayout != null && (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
                        mLayout.setPanelState(PanelState.COLLAPSED);
                    }

                    stopService(new Intent(this, MyRadioService.class));
                    buttonPlayHandleAnimation();
                    relative_up_text.setVisibility(View.VISIBLE);
                    relative_down_text.setVisibility(View.GONE);
                    setGravityTextViews();

                    // Set SharePrefrence For Player Is Stop To Play...
                    Utilities.setSharedPrefPlayer(this, isPlayerStart, "", -1);
                    Utilities.setSharedPrefPlayerCancel(this, isPlayerCancel, getterSetter.getId());

                    // When Cancel Button Press It Will Load Againg Old ArrayList To
                    // Show On The List View...
                    radioLinkList.clear();
                    radioLinkList = new ArrayList<GetterSetter>();
                    radioLinkList.addAll(radioLinkList2);
                    setAdapterForListBottom(radioLinkList, true);

                } else {
                    Utilities.showAlertDialog(VideoActivity.this, "Internet Connection Error", "Please connect to working Internet connection", false);
                }
                break;

            case R.id.btn_play_vid:
                handler.removeCallbacks(myRunnable);
                handler.removeCallbacks(null);
                inTheActivity = false;

                if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
                    if (((ApplicationSingleton) getApplicationContext()).player2.isPlaying()) {
                        ((ApplicationSingleton) getApplicationContext()).player2.pause();
                        isPlayerPlaying = false;
                        isPlayerPlaying2 = true;
                    }
                }

                if (((ApplicationSingleton) getApplicationContext()).player != null) {
                    if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
                        ((ApplicationSingleton) getApplicationContext()).player.pause();
                        disableTextMove();
                        btn_cross_view1.setVisibility(View.VISIBLE);
                        btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                        btn_play_vid_radio.setVisibility(View.VISIBLE);
                        handle.setVisibility(View.VISIBLE);
                        lv_bottom_pager.setVisibility(View.VISIBLE);
                        buttonPlayHandleAnimation();
                        isPlayerPlaying = true;
                        isPlayerPlaying2 = false;
                    }
                }
                if (!isPDFPageOpen) {
                    Intent videoIntent = new Intent(VideoActivity.this, VideoDrawerActivity.class);
                    videoIntent.putExtra("Check", check);
                    videoIntent.putExtra("isPlayerPlaying", isPlayerPlaying);
                    videoIntent.putExtra("isPlayerPlaying2", isPlayerPlaying2);
                    videoIntent.putExtra("ArrayList", VideoLectureAdapter.getInstance().getNewArrayList());
                    videoIntent.putExtra("ArrayListFull", arrayListVideo.get(currentPage).getList());
                    videoIntent.putExtra("video_Id", arrayListVideo.get(currentPage).getList().get(0).get("VideoId"));
                    startActivityForResult(videoIntent, VideoActivity.REQUEST_CODE);

                } else {
                    Utilities.downloadAndOpenPDF(this, ((ApplicationSingleton) getApplicationContext()).dataContainer
                            .getList_NewsletterUp().get(0).getPdf(), ((ApplicationSingleton) getApplicationContext()).dataContainer
                            .getList_NewsletterUp().get(0).getTitle());
                }
                break;

            case R.id.btn_play_text:
                handler.removeCallbacks(myRunnable);
                handler.removeCallbacks(null);
                inTheActivity = false;

                if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
                    if (((ApplicationSingleton) getApplicationContext()).player2.isPlaying()) {
                        ((ApplicationSingleton) getApplicationContext()).player2.pause();
                        isPlayerPlaying = false;
                        isPlayerPlaying2 = true;
                    }
                }

                if (((ApplicationSingleton) getApplicationContext()).player != null) {
                    if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
                        ((ApplicationSingleton) getApplicationContext()).player.pause();
                        disableTextMove();
                        btn_cross_view1.setVisibility(View.VISIBLE);
                        btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                        btn_play_vid_radio.setVisibility(View.VISIBLE);
                        handle.setVisibility(View.VISIBLE);
                        lv_bottom_pager.setVisibility(View.VISIBLE);
                        buttonPlayHandleAnimation();
                        isPlayerPlaying = true;
                        isPlayerPlaying2 = false;
                    }
                }

                if (!isPDFPageOpen) {
                    Intent videoIntent = new Intent(VideoActivity.this, VideoDrawerActivity.class);
                    videoIntent.putExtra("Check", check);
                    videoIntent.putExtra("isPlayerPlaying", isPlayerPlaying);
                    videoIntent.putExtra("isPlayerPlaying2", isPlayerPlaying2);
                    videoIntent.putExtra("ArrayList", VideoLectureAdapter.getInstance().getNewArrayList());
                    videoIntent.putExtra("ArrayListFull", arrayListVideo.get(currentPage).getList());
                    videoIntent.putExtra("video_Id", arrayListVideo.get(currentPage).getList().get(0).get("VideoId"));
                    startActivityForResult(videoIntent, VideoActivity.REQUEST_CODE);
                } else {
                    Utilities
                            .downloadAndOpenPDF(
                                    this,
                                    ((ApplicationSingleton) getApplicationContext()).dataContainer
                                            .getList_NewsletterUp().get(0).getPdf(),
                                    ((ApplicationSingleton) getApplicationContext()).dataContainer
                                            .getList_NewsletterUp().get(0)
                                            .getTitle());
                }
                break;

            case R.id.img_broadcast_sidebar:
                if (isInternetPresent) {

                    slidingMenu.toggle();
                    inTheActivity = false;
                    if (((ApplicationSingleton) getApplicationContext()).player != null) {
                        if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
                            isPlayerPlaying = true;
                            disableTextMove();
                        }
                    }

                    Utilities.setSharedPrefForNoti(this, false, Utilities.getAlertId(this), Utilities.getAlertURL(this));

                    Intent listAlerts = new Intent(VideoActivity.this, ListOfAlerts.class);
                    listAlerts.putExtra("isPlayerPlaying", isPlayerPlaying);
                    startActivityForResult(listAlerts, REQUEST_CODE_LIST_OF_ALERTS);

                } else {
                    Utilities.showAlertDialog(VideoActivity.this, "Internet Connection Error", "Please connect to working Internet connection", false);
                }
                break;

            case R.id.tv_this_praying_about_it:

                if (isInternetPresent) {

                    slidingMenu.toggle();
                    inTheActivity = false;
                    handler.removeCallbacks(myRunnable);
                    handler.removeCallbacks(null);

                    if (pref.getString("email", null) == null) {

                        Intent registerInt = new Intent(VideoActivity.this, LogInActivity.class);
                        startActivityForResult(registerInt, REQUEST_CODE_LIST_OF_ALERTS_ITEM);

                    } else {
                        Utilities.setComingFromDedicationHome(mContext, false);
                        Intent prayerIntent = new Intent(this, PrayerScreen.class);
                        prayerIntent.putExtra("email", pref.getString("email", null));
                        prayerIntent.putExtra("video_Id", live_video_id);
                        startActivityForResult(prayerIntent, REQUEST_CODE_LIST_OF_ALERTS_ITEM);
                    }

                } else {
                    Utilities.showAlertDialog(VideoActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                }
                break;

        }
    }

    // Start Play Radio...
    private void startPlayRadio() {
        isPlayerCancel = false;
        handle.setVisibility(View.VISIBLE);
        lv_bottom_pager.setVisibility(View.VISIBLE);

        if (!getIntent().getStringExtra("Message").equals("RadioAlarmAlert")) {
            if (isOpen == true) {
                if (mLayout != null
                        && (mLayout.getPanelState() == PanelState.COLLAPSED || mLayout
                        .getPanelState() == PanelState.ANCHORED)) {
                    mLayout.setPanelState(PanelState.EXPANDED);
                    sliderCloseAutomatically(mLayout);
                }
            }
        }

        // Start Thread For Pause Button Visible Invisible After 2 Sec...
        // buttonPauseHandleAnimation();
        startService(new Intent(this, MyRadioService.class).putExtra("RadioUrl", getterSetter.getRadio_programs()));
        Utilities.setSharedPrefPlayerCancel(this, isPlayerCancel, "");

    }

    // Enable text Movement...
    private void enableTextMove() {
        if (Utilities.hasHebChars(getterSetter.getTitle())) {

            mNewsFlashManagerTitle.dd(Utilities.decodeImoString(getterSetter.getTitle()), TickerDirection.RIGHT, mHorizontalScrollViewLayout);

        } else {

            mNewsFlashManagerTitle.dd(Utilities.decodeImoString(getterSetter.getTitle()), TickerDirection.LEFT, mHorizontalScrollViewLayout);

        }

        if (Utilities.hasHebChars(getterSetter.getDescriptions())) {

            mNewsFlashManagerDes.dd2(Utilities.decodeImoString(getterSetter.getDescriptions()), TickerDirection.RIGHT, mHorizontalScrollViewLayout2);

        } else {

            mNewsFlashManagerDes.dd2(Utilities.decodeImoString(getterSetter.getDescriptions()), TickerDirection.LEFT, mHorizontalScrollViewLayout2);

        }

    }

    // Disable text Movement...
    private void disableTextMove() {
        mNewsFlashManagerTitle.stop();
        mNewsFlashManagerDes.stop();
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

    // onActivity Result to show data from another activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inTheActivity = true;
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CODE) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    if (handler != null) {
                        handler.removeCallbacks(myRunnable);
                        handler.removeCallbacks(null);
                    }

                    isProgressShow = false;
                    isFirstTime = true;
                    listLenght = 0;

                    if (data.getBooleanExtra("isPlayerPlaying", false) == true) {
                        enableTextMove();

                        if (((ApplicationSingleton) getApplicationContext()).player != null) {
                            if (!((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
                                ((ApplicationSingleton) getApplicationContext()).player.start();
                                btn_cross_view1.setVisibility(View.VISIBLE);
                                btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                                btn_play_vid_radio.setVisibility(View.INVISIBLE);
                                pb_playpause.setVisibility(View.GONE);
                                handle.setVisibility(View.VISIBLE);
                                lv_bottom_pager.setVisibility(View.VISIBLE);
                                relative_up_text.setVisibility(View.GONE);
                                relative_down_text.setVisibility(View.VISIBLE);
                                buttonPauseHandleAnimation();
                            }
                        }
                    }

                    if (data.getBooleanExtra("isPlayerPlaying2", false) == true) {
                        if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
                            if (!((ApplicationSingleton) getApplicationContext()).player2
                                    .isPlaying()) {
                                ((ApplicationSingleton) getApplicationContext()).player2
                                        .start();
                            }
                        }
                    }

                    isPlayerPlaying = false;
                    isPlayerPlaying2 = false;

                } else {
                    Utilities.showAlertDialog(this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection",
                            false);
                }

            }

            if (requestCode == REQUEST_CODE_RADIO) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    if (handler != null) {
                        handler.removeCallbacks(myRunnable);
                        handler.removeCallbacks(null);
                    }

                    isProgressShow = false;
                    isFirstTime = true;
                    listLenght = 0;

                } else {
                    Utilities.showAlertDialog(this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection",
                            false);
                }

            }

            if (requestCode == REQUEST_CODE_DEVOTE) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    if (handler != null) {
                        handler.removeCallbacks(myRunnable);
                        handler.removeCallbacks(null);
                    }

                    isProgressShow = false;
                    isFirstTime = true;
                    listLenght = 0;

                } else {
                    Utilities.showAlertDialog(this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection",
                            false);
                }
            }

            if (requestCode == REQUEST_CODE_VIDEO_LIST) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    if (handler != null) {
                        handler.removeCallbacks(myRunnable);
                        handler.removeCallbacks(null);
                    }

                    isProgressShow = false;
                    isFirstTime = true;
                    listLenght = 0;

                } else {
                    Utilities.showAlertDialog(this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection",
                            false);
                }
            }

            if (requestCode == REQUEST_CODE_LIST_OF_ALERTS) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    if (handler != null) {
                        handler.removeCallbacks(myRunnable);
                        handler.removeCallbacks(null);
                    }

                    isProgressShow = false;
                    isFirstTime = true;
                    listLenght = 0;
                    if (data.getBooleanExtra("isPlayerPlaying", false) == true) {
                        enableTextMove();

                        if (((ApplicationSingleton) getApplicationContext()).player != null) {
                            if (!((ApplicationSingleton) getApplicationContext()).player
                                    .isPlaying()) {
                                ((ApplicationSingleton) getApplicationContext()).player
                                        .start();
                                btn_cross_view1.setVisibility(View.VISIBLE);
                                btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                                btn_play_vid_radio.setVisibility(View.INVISIBLE);
                                pb_playpause
                                        .setVisibility(View.GONE);
                                handle.setVisibility(View.VISIBLE);
                                lv_bottom_pager.setVisibility(View.VISIBLE);
                                relative_up_text.setVisibility(View.GONE);
                                relative_down_text.setVisibility(View.VISIBLE);
                                buttonPauseHandleAnimation();
                            }
                        }
                    }

                    isPlayerPlaying = false;

                } else {
                    Utilities.showAlertDialog(this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection",
                            false);
                }
            }

            if (requestCode == REQUEST_CODE_LIST_OF_ALERTS_ITEM) {
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    if (handler != null) {
                        handler.removeCallbacks(myRunnable);
                        handler.removeCallbacks(null);
                    }

                    isProgressShow = false;
                    isFirstTime = true;
                    listLenght = 0;

                } else {
                    Utilities.showAlertDialog(this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection",
                            false);
                }
            }
        }

    }

    // Get Viewer's Count Using Retrofit Library...
    private void getViewCounts(String videoId) {
        RestClient.get().downloadHomeData2(videoId,
                new Callback<YoutubePojoVideoId>() {

                    @Override
                    public void success(YoutubePojoVideoId arg0, Response arg1) {
                        if (arg0.getItems().size() > 0) {
                            try {
                                count = count
                                        + Integer.valueOf(arg0.getItems()
                                        .get(0).getStatistics()
                                        .getViewCount());
                                pos++;
                                if (currentPage != arrayListVideo.size()) {
                                    if (arrayListVideo.get(currentPage)
                                            .getList().size() == pos) {
                                        double amount = Double
                                                .parseDouble(String
                                                        .valueOf(count));
                                        DecimalFormat formatter = new DecimalFormat(
                                                "#,###,###");
                                        String formatted = formatter
                                                .format(amount);
                                        tv_value_project_uplist
                                                .setText(formatted);
                                    } else {
                                        double amount = Double
                                                .parseDouble(String
                                                        .valueOf(count));
                                        DecimalFormat formatter = new DecimalFormat(
                                                "#,###,###");
                                        String formatted = formatter
                                                .format(amount);
                                        tv_value_project_uplist
                                                .setText(formatted);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Exception",
                                        "VideoActivity_getViewCounts"
                                                + e.getMessage());
                            }

                        }

                    }

                    @Override
                    public void failure(RetrofitError arg0) {

                    }
                });
    }

    // Get All Dedications for Administrator who control all the data over
    // server
    public class GetAcceptDedications extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isProgressShow == true && isFromAlert == false) {
                pDialog.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String responce = null;
            ServiceHandler sd = new ServiceHandler();
            responce = sd.makeServiceCall(Config.ROOT_SERVER_CLIENT + Config.ACEPTED_DEDICATION, ServiceHandler.GET);
            return responce;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            count_dedications = 0;
            if (isProgressShow == true && isFromAlert == false) {
                pDialog.setVisibility(View.GONE);
                isProgressShow = false;
            }
            isFromAlert = false;
            if (result != null) {
                try {
                    listAcceptDedications = new ArrayList<GetterSetter>();
                    list = new ArrayList<String>();
                    JSONObject json = new JSONObject(result);

                    if (json.getString("success").equals("true")) {
                        JSONArray jArry = json.getJSONArray("details");
                        for (int i = 0; i < jArry.length(); i++) {
                            JSONObject jsonObj = jArry.getJSONObject(i);
                            GetterSetter getset = new GetterSetter();
                            getset.setId(jsonObj.getString("id"));
                            getset.setNature(jsonObj.getString("nature"));
                            getset.setName(jsonObj.getString("name"));
                            getset.setSex(jsonObj.getString("sex"));
                            getset.setName_Optional(jsonObj.getString("name_optional"));
                            getset.setThere_Is(jsonObj.getString("there_is"));
                            getset.setBlessing(jsonObj.getString("status_blessing"));
                            getset.setEmail(jsonObj.getString("email"));
                            getset.setSigninwith(jsonObj.getString("sign_with"));
                            getset.setTime(jsonObj.getString("time"));
                            getset.setDate(jsonObj.getString("date"));
                            getset.setPublish(jsonObj.getString("publish"));
                            getset.setCount(i + 1);
                            list.add(String.valueOf(getset.getCount()));
                            listAcceptDedications.add(getset);
                            try {
                                if (pref != null) {
                                    if (pref.contains("email")) {
                                        if (!pref.getString("email", null).equals("")) {

                                            if (jsonObj.getString("email").equals(pref.getString("email", null))) {
                                                count_dedications = (count_dedications + 1);
                                                Utilities.setUserIdDedicated(mContext, jsonObj.getString("email"));
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.e("Exception", "VideoActivity_Count?? " + e.getMessage());
                            }
                        }

                        // Set List View For Show ListValues Devote Adapter Here
                        if (listAcceptDedications.size() > 0) {

                            tv_total_dedications.setText(String.valueOf(count_dedications));
                            Collections.reverse(list);
                            ((ApplicationSingleton) getApplicationContext()).dataContainer.setList_Count(list);

                            if (listLenght == 0) {
                                newArrayList = new ArrayList<GetterSetter>();
                                newArrayList = listAcceptDedications;
                                setDevoteListAdapter(newArrayList, isFirstTime);
                                listLenght = listAcceptDedications.size();

                            } else if (listLenght != listAcceptDedications.size()) {
                                isFirstTime = true;
                                newArrayList = new ArrayList<GetterSetter>();
                                newArrayList = listAcceptDedications;
                                setDevoteListAdapter(newArrayList, isFirstTime);
                                listLenght = listAcceptDedications.size();
                            }

                        } else {
                            tv_total_dedications.setText("0");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (isProgressShow == false) {
                refreshList2();
            }
        }
    }

    // Get You Tube Video ID
    class GetYouTubeVideosID extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(
                String... playlistID) {
            ServiceHandler handle = new ServiceHandler();
            String response = handle.makeServiceCall(
                    Config.YOUTUBE_URL_PLAYLIST_UPTO50 + Config.NEXT_PAGE_TOKEN
                            + nexpageToken + Config.PLAY_LIST_KEY
                            + playlistID[0], GET);

            if (response != null) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonPageInfo = jsonObject
                            .getJSONObject("pageInfo");
                    JSONArray jsonArrayItem = jsonObject.getJSONArray("items");

                    if (jsonArrayItem.length() > 0) {
                        for (int i = 0; i < jsonArrayItem.length(); i++) {

                            hashMap = new HashMap<String, String>();
                            JSONObject jsonObj = jsonArrayItem.getJSONObject(i);
                            JSONObject jObjSnippet = jsonObj
                                    .getJSONObject("snippet");
                            JSONObject jObjContentDetails = jsonObj
                                    .getJSONObject("contentDetails");
                            JSONObject jObjThumbnail = jObjSnippet
                                    .getJSONObject("thumbnails");
                            JSONObject jObjThumbHigh = jObjThumbnail
                                    .getJSONObject("high");

                            if (jsonObject.has("nextPageToken")) {
                                hashMap.put("NextPageToken",
                                        jsonObject.getString("nextPageToken"));
                            }

                            if (jsonObject.has("prevPageToken")) {
                                hashMap.put("PreviousPageToken",
                                        jsonObject.getString("prevPageToken"));
                            }

                            if (jsonPageInfo.has("totalResults")) {
                                hashMap.put("TotalVideos",
                                        jsonPageInfo.getString("totalResults"));
                            }

                            if (jObjSnippet.has("channelTitle"))
                                hashMap.put("ChannelTitle",
                                        jObjSnippet.getString("channelTitle"));

                            if (jObjSnippet.has("title"))
                                hashMap.put("Title",
                                        jObjSnippet.getString("title"));

                            if (jObjSnippet.has("description"))
                                hashMap.put("Description",
                                        jObjSnippet.getString("description"));

                            if (jObjContentDetails.has("videoId"))
                                hashMap.put("VideoId",
                                        jObjContentDetails.getString("videoId"));

                            if (jObjThumbHigh.has("url"))
                                hashMap.put("Url",
                                        jObjThumbHigh.getString("url"));

                            if (jObjThumbnail.has("standard")) {
                                JSONObject jObjThumbStandard = jObjThumbnail
                                        .getJSONObject("standard");
                                if (jObjThumbStandard.has("url"))
                                    hashMap.put("Url",
                                            jObjThumbStandard.getString("url"));
                            }

                            if (jObjThumbnail.has("maxres")) {
                                JSONObject jObjMaxres = jObjThumbnail
                                        .getJSONObject("maxres");
                                if (jObjMaxres.has("url"))
                                    hashMap.put("Url",
                                            jObjMaxres.getString("url"));
                            }
                            arrayListVideo.get(position_list).getList()
                                    .add(hashMap);
                        }

                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Utilities.showAlertDialog(VideoActivity.this,
                                        "", "No Results Available", false);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Utilities.showAlertDialog(VideoActivity.this,
                                "Internet Connection Error",
                                "Your internet connection is too slow...",
                                false);

                    }
                });
            }
            return arrayListVideo.get(position_list).getList();
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);
            if (result != null) {
                progress_loading_more.setVisibility(View.GONE);
                footer_tv.setVisibility(View.GONE);

                // Set List View For Show Videos Adapter Here
                int currentPosition = lv_videos_shows.getFirstVisiblePosition();
                mVidAdapter = new VideoLectureAdapter(mContext, arrayListVideo
                        .get(position_list).getList());
                mVidAdapter.notifyDataSetChanged();
                lv_videos_shows.setAdapter(mVidAdapter);
                // Setting new scroll position
                lv_videos_shows.setSelectionFromTop(currentPosition + 1, 0);
            }
        }
    }

    // Get Radio Programs For Users to show using Retrofit library...
    public void GetAllRadioPrograms() {
        RestClientRadioPrograms.get().getRadioPrograms(new Callback<Response>() {

            @Override
            public void failure(RetrofitError arg0) {
            }

            @Override
            public void success(Response response, Response arg1) {
                TypedInput body = response.getBody();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
                    StringBuilder out = new StringBuilder();
                    String newLine = System.getProperty("line.separator");
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                        out.append(newLine);
                    }

                    try {
                        radioLinkList = new ArrayList<GetterSetter>();
                        radioLinkList2 = new ArrayList<GetterSetter>();
                        JSONObject jsonObjs = new JSONObject(out.toString());
                        JSONArray jArray = jsonObjs.getJSONArray("value");
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject jsonObj = jArray.getJSONObject(i);
                            GetterSetter getset = new GetterSetter();
                            getset.setId(jsonObj.getString("id"));

                            if (!jsonObj.getString("title").equals("")) {
                                getset.setTitle(jsonObj.getString("title"));
                            } else {
                                getset.setTitle("");
                            }

                            if (!jsonObj.getString("description").equals("")) {
                                getset.setDescriptions(jsonObj.getString("description"));
                            } else {
                                getset.setDescriptions("");
                            }

                            if (!jsonObj.getString("image").equals("")) {
                                getset.setThumbnailHigh(jsonObj.getString("image"));
                            } else {
                                getset.setThumbnailHigh("");
                            }

                            if (!jsonObj.getString("radio_program").equals("")) {
                                getset.setRadio_programs(jsonObj.getString("radio_program"));
                            } else {
                                getset.setRadio_programs("");
                            }

                            if (!jsonObj.getString("duration").equals("")) {
                                getset.setDuration(jsonObj.getString("duration"));
                            } else {
                                getset.setDuration("");
                            }

                            if (!jsonObj.getString("time").equals("")) {
                                getset.setTime(jsonObj.getString("time"));
                            } else {
                                getset.setTime("");
                            }

                            if (!jsonObj.getString("date").equals("")) {
                                getset.setDate(jsonObj.getString("date"));
                            } else {
                                getset.setDate("");
                            }

                            if (!jsonObj.getString("status").equals("")) {
                                getset.setStatus(jsonObj.getString("status"));
                            } else {
                                getset.setStatus("");
                            }

                            if (getIntent().getStringExtra("Message").equals("MediaPlayer")) {
                                if ((MyNotification.getInstance() != null)) {
                                    if (MyNotification.getInstance().getPlayId().equals(jsonObj.getString("id"))) {
                                        index = i;
                                    }
                                }
                            }

                            if (getIntent().getStringExtra("Message").equals("RadioAlarmAlert")) {

                                if (Utilities.getRadioAlarmID(mContext).equals(jsonObj.getString("id"))) {
                                    index = i;
                                }
                            }

                            if (Utilities.isPlayerStart(VideoActivity.this)) {
                                if (Utilities.getPlayerId(VideoActivity.this).equals(jsonObj.getString("id"))) {
                                    index = i;
                                }
                            }

                            if (Utilities.isPlayerCancel(VideoActivity.this)) {
                                if (Utilities.getPlayerIdForCancel(VideoActivity.this).equals(jsonObj.getString("id"))) {
                                    index = i;
                                }
                            }
                            radioLinkList.add(getset);
                            radioLinkList2.add(getset);
                        }

                        if (radioLinkList.size() > 0) {
                            loading_media.setVisibility(View.GONE);
                            handle.setVisibility(View.VISIBLE);
                            lv_bottom_pager.setVisibility(View.VISIBLE);

                            // Set Adapter On Radio List Lecture...
                            setAdapterForListBottom(radioLinkList, false);

                            if (getIntent().getStringExtra("Message").equals("MediaPlayer")) {
                                if (MyNotification.getInstance() != null) {
                                    if (((ApplicationSingleton) getApplicationContext()).player != null) {
                                        if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {

                                            changeIndex(index, false);

                                            if (prefSize != null) {
                                                params.height = prefSize.getInt("Size", 0);
                                            }

                                            btn_play_vid_radio.setVisibility(View.INVISIBLE);
                                            pb_playpause.setVisibility(View.GONE);
                                            btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                                            btn_cross_view1.setVisibility(View.VISIBLE);
                                            relative_up_text.setVisibility(View.GONE);
                                            relative_down_text.setVisibility(View.VISIBLE);
                                            updateSeekBar(((ApplicationSingleton) getApplicationContext()).player, seek_bar_radio1);
                                            buttonPauseHandleAnimation();
                                            // enableTextMarquee();
                                            enableTextMove();

                                        } else {

                                            changeIndex(index, false);

                                            if (prefSize != null) {
                                                params.height = prefSize.getInt("Size", 0);
                                            }

                                            btn_play_vid_radio.setVisibility(View.VISIBLE);
                                            pb_playpause.setVisibility(View.GONE);
                                            btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                                            btn_cross_view1.setVisibility(View.VISIBLE);
                                            relative_up_text.setVisibility(View.VISIBLE);
                                            relative_down_text.setVisibility(View.GONE);
                                            updateSeekBar(((ApplicationSingleton) getApplicationContext()).player, seek_bar_radio1);
                                            buttonPlayHandleAnimation();
                                            // disableTextMarquee();
                                            disableTextMove();
                                        }
                                    } else {
                                        changeIndex(0, false);
                                    }

                                } else {
                                    changeIndex(0, false);
                                }

                            } else if (getIntent().getStringExtra("Message").equals("RadioAlarmAlert")) {

                                radioAlertStr = getIntent().getStringExtra("Message");

                                if (((ApplicationSingleton) getApplicationContext()).player != null) {

                                    if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {

                                        changeIndex(index, false);

                                        if (prefSize != null) {
                                            params.height = prefSize.getInt("Size", 0);
                                        }

                                        stopService(new Intent(VideoActivity.this, MyRadioService.class));
                                        MyRadioService.getInstance().getHandler().removeCallbacks(MyRadioService.getInstance().getRunnable());
                                        ((ApplicationSingleton) getApplicationContext()).player.stop();
                                        ((ApplicationSingleton) getApplicationContext()).player.release();
                                        ((ApplicationSingleton) getApplicationContext()).player = new MediaPlayer();

                                        relative_up_text.setVisibility(View.GONE);
                                        relative_down_text.setVisibility(View.VISIBLE);
                                        updateSeekBar(((ApplicationSingleton) getApplicationContext()).player, seek_bar_radio1);
                                        buttonPauseHandleAnimation();
                                        // enableTextMarquee();
                                        // enableTextMove();

                                    } else {

                                        changeIndex(index, false);

                                        if (prefSize != null) {
                                            params.height = prefSize.getInt("Size", 0);
                                        }

                                        relative_up_text.setVisibility(View.VISIBLE);
                                        relative_down_text.setVisibility(View.GONE);
                                        updateSeekBar(((ApplicationSingleton) getApplicationContext()).player, seek_bar_radio1);
                                        buttonPlayHandleAnimation();
                                        // enableTextMarquee();
                                        // enableTextMove();
                                    }
                                } else {
                                    changeIndex(0, false);
                                }

                            } else if (Utilities.isPlayerStart(VideoActivity.this)) {

                                changeIndex(index, false);

                            } else if (Utilities.isPlayerCancel(VideoActivity.this)) {

                                changeIndex(index, false);

                            } else {
                                changeIndex(0, false);
                            }

                        } else {
                            loading_media.setVisibility(View.VISIBLE);
                            handle.setVisibility(View.GONE);
                            lv_bottom_pager.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
        });

    }

    // Set List Adapter For List At Bottom
    private void setAdapterForListBottom(ArrayList<GetterSetter> radioLinkList, Boolean isCancelClick) {
        radioAdapter = new RadioLectureAdapter(VideoActivity.this, radioLinkList);
        lv_bottom_pager.setAdapter(radioAdapter);
        radioAdapter.notifyDataSetChanged();
        if (isCancelClick) {
            changeIndex(0, false);
        }
    }

    // Change ListView Index To Show...
    private void changeIndex(int index, boolean replace) {
        GetterSetter getterSetterCurrent = radioLinkList.get(index);
        radioLinkList.remove(index);

        if (replace)
            radioLinkList.add(index, getterSetter);
        radioAdapter.notifyDataSetChanged();
        getterSetter = getterSetterCurrent;
        tv_time_start.setText("");
        tv_time_start.setText("00:00:00");
        tv_time_end.setText("");
        tv_time_end.setText(getterSetter.getDuration());
        tv_lecture_title2.setText(Utilities.decodeImoString(getterSetter.getTitle()));
        tv_lecture_description2.setText(Utilities.decodeImoString(getterSetter.getDescriptions()));
        setGravityTextViews();

        if (radioAlertStr.equals("RadioAlarmAlert")) {
            startPlayRadio();
            radioAlertStr = "";
        }

        // Set Image On Sliding Panel Top View...
        Picasso.with(this).load(getterSetter.getThumbnailHigh()).placeholder(R.drawable.below_thubnails)
                .error(R.drawable.below_thubnails).into(img_radio_bottom_view1);

        // Add Values For Notification According To Playing Radio
        // Lecture...
        addNotificationData(getterSetter.getId(), getterSetter.getTitle(),
                getterSetter.getDescriptions(), getterSetter.getDuration(),
                getterSetter.getThumbnailHigh(), getterSetter.getTime());
    }

    // SetGravity Of TextView At RunTime...
    private void setGravityTextViews() {
        if (Utilities.hasHebChars(Utilities.decodeImoString(getterSetter
                .getTitle()))) {

            tv_lecture_title1.setText(Utilities.decodeImoString(getterSetter
                    .getTitle()));
            mHorizontalScrollViewLayout.removeAllViews();
            mHorizontalScrollViewLayout.addView(tv_lecture_title1);
            Utilities.mSwitchTextDirection = true;
            Utilities.fixGravity(tv_lecture_title1);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mHorizontalScrollView.smoothScrollTo(
                            tv_lecture_title1.getRight(),
                            tv_lecture_title1.getTop());
                }
            }, 100L);

        } else {

            tv_lecture_title1.setText(Utilities.decodeImoString(getterSetter
                    .getTitle()));
            mHorizontalScrollViewLayout.removeAllViews();
            mHorizontalScrollViewLayout.addView(tv_lecture_title1);
            Utilities.mSwitchTextDirection = false;
            Utilities.fixGravity(tv_lecture_title1);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mHorizontalScrollView.smoothScrollTo(
                            tv_lecture_title1.getLeft(),
                            tv_lecture_title1.getTop());

                }
            }, 100L);

        }

        if (Utilities.hasHebChars(Utilities.decodeImoString(getterSetter
                .getDescriptions()))) {

            tv_lecture_description1.setText(Utilities.decodeImoString(
                    getterSetter.getDescriptions()).replaceAll("\n", ""));
            mHorizontalScrollViewLayout2.removeAllViews();
            mHorizontalScrollViewLayout2.addView(tv_lecture_description1);
            Utilities.mSwitchTextDirection = true;
            Utilities.fixGravity(tv_lecture_description1);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    mHorizontalScrollView2.smoothScrollTo(
                            tv_lecture_description1.getRight(),
                            tv_lecture_description1.getTop());

                }
            }, 100L);

        } else {

            tv_lecture_description1.setText(Utilities.decodeImoString(
                    getterSetter.getDescriptions()).replaceAll("\n", ""));
            mHorizontalScrollViewLayout2.removeAllViews();
            mHorizontalScrollViewLayout2.addView(tv_lecture_description1);
            Utilities.mSwitchTextDirection = false;
            Utilities.fixGravity(tv_lecture_description1);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    mHorizontalScrollView2.smoothScrollTo(
                            tv_lecture_description1.getLeft(),
                            tv_lecture_description1.getTop());

                }
            }, 100L);

        }
    }

    // Initialize Bottom View For Title To Move Text...
    private void initializeRadioFlashesTitle() {

        if (mNewsFlashManagerTitle != null) {
            mNewsFlashManagerTitle.stop();
            mNewsFlashManagerTitle = null;
        }

        mNewsFlashManagerTitle = new NewsFlashManagerTITLE(VideoActivity.this,
                mHorizontalScrollView, mHorizontalScrollViewLayout);

    }

    // Initialize Bottom View For Descriptions To Move Text...
    private void initializeRadioFlashesDes() {

        if (mNewsFlashManagerDes != null) {
            mNewsFlashManagerDes.stop();
            mNewsFlashManagerDes = null;
        }

        mNewsFlashManagerDes = new NewsFlashManagerDES(VideoActivity.this, mHorizontalScrollView2, mHorizontalScrollViewLayout2);
    }

    // Radio Lecture Title...
    private class NewsFlashManagerTITLE extends HorizontalTicker {

        public NewsFlashManagerTITLE(Context context, HorizontalScrollView horizontalscrollview, LinearLayout linearlayout) {
            super(context, horizontalscrollview, linearlayout);
        }

        public void addContentToView(LinearLayout linearlayout) {
            linearlayout.removeView(tv_lecture_title1);
            addEmptyView();
            linearlayout.addView(tv_lecture_title1);
            addEmptyView();
        }

        public void dd(String st, TickerDirection direction, LinearLayout linearlayout) {
            addContentToView(linearlayout);
            tv_lecture_title1.setText(st);
            this.mTickerDirection = direction;
            this.mHorizontalScrollViewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new C14591());
            this.mScrollingViewHandler = new Handler();
            this.mScrollingViewRunnable = new C14602();
            restart();
        }
    }

    // Radio Lecture Descriptions...
    private class NewsFlashManagerDES extends HorizontalTicker {

        public NewsFlashManagerDES(Context context, HorizontalScrollView horizontalscrollview, LinearLayout linearlayout) {
            super(context, horizontalscrollview, linearlayout);

        }

        public void addContentToView(LinearLayout linearlayout) {
            linearlayout.removeView(tv_lecture_description1);
            addEmptyView();
            linearlayout.addView(tv_lecture_description1);
            addEmptyView();
        }

        public void dd2(String st, TickerDirection direction, LinearLayout linearlayout) {
            addContentToView(linearlayout);
            tv_lecture_description1.setText(st.replaceAll("\n", ""));
            this.mTickerDirection = direction;
            this.mHorizontalScrollViewLayout.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new C14591());
            this.mScrollingViewHandler = new Handler();
            this.mScrollingViewRunnable = new C14602();
            restart();
        }
    }

    // Set Notification Data
    public String[] addNotificationData(String id, String title, String descriptions, String duration, final String image, String time) {
        str = new String[6];
        str[0] = title;
        str[1] = descriptions;
        str[2] = duration;

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                str[3] = Utilities.BitMapToString(Utilities
                        .getBitmapFromURL(image));
            }
        });

        str[4] = time;
        str[5] = id;

        return str;
    }

    // SeekBar Change Listener Call Here...
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (((ApplicationSingleton) getApplicationContext()).player != null && fromUser) {
                ((ApplicationSingleton) getApplicationContext()).player.seekTo(progress);
                seekBar.setProgress(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            pb_playpause.setVisibility(View.VISIBLE);
            isLoading = true;
        }
    };

    // Update Seek Bar
    public static void updateSeekBar(final MediaPlayer mediaPlayer, final SeekBar seekBar2) {

        // Worker thread that will update the seekbar as each song is playing
        Thread t = new Thread() {
            @Override
            public void run() {

                currentPosition = 0;
                int total = (int) mediaPlayer.getDuration();

                seekBar2.setMax(total);
                while (mediaPlayer != null && mediaPlayer.getCurrentPosition() < total) {
                    try {

                        Thread.sleep(1000);
                        currentPosition = mediaPlayer.getCurrentPosition();
                    } catch (InterruptedException e) {
                        return;
                    } catch (Exception e) {
                        return;
                    }

                    seekBar2.setProgress(mediaPlayer.getCurrentPosition());
                }

            }

        };
        t.start();

    }

    // Refresh List Of Accepted Dedication After 5 Sec...
    public void refreshList(final ArrayList<GetterSetter> listAcceptDedications, final boolean b) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            handler = new Handler();
            myRunnable = new Runnable() {
                public void run() {

                    setDevoteListAdapter(listAcceptDedications, b);
                }
            };
            handler.postDelayed(myRunnable, 5000);

        } else {
            Utilities.showAlertDialog(VideoActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            if (handler != null) {
                handler.removeCallbacks(myRunnable);
            }
        }
    }

    // Refresh ArrayList Data Of Accepted Dedication After 5 Sec...
    public void refreshList2() {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            handler4 = new Handler();
            myRunnable2 = new Runnable() {
                public void run() {

                    new GetAcceptDedications().execute();

                }
            };
            handler4.postDelayed(myRunnable2, 5000);

        } else {
            Utilities.showAlertDialog(VideoActivity.this, "Internet Connection Error", "Please connect to working Internet connection", false);
            if (handler4 != null) {
                handler4.removeCallbacks(myRunnable2);
            }
        }
    }

    // Set All Dedication Adapter For First Time...
    private void setDevoteListAdapter(
            ArrayList<GetterSetter> listAcceptDedications, Boolean isfirstTime) {
        if (isfirstTime == false) {
            // Rotate List View Items After 5 Sec...
            Collections.rotate(listAcceptDedications, 1);
        }

        int currentPosition = lv_devotes_new.getFirstVisiblePosition();

        ((ApplicationSingleton) getApplicationContext()).dataContainer.setList_AcceptedDedication(listAcceptDedications);

        madapter = new VideoListAdapter(mContext, listAcceptDedications, list, "VideoActivity");
        madapter.notifyDataSetChanged();
        lv_devotes_new.setAdapter(madapter);
        // Setting new scroll position
        lv_devotes_new.setSelectionFromTop(currentPosition, 0);
        isfirstTime = false;
        refreshList(listAcceptDedications, isfirstTime);
    }


    // Get Boolean Check Value
    public Boolean getCheckVal() {
        return check;
    }

    // Get Play Button Using Instance
    public Button getPlayButton() {
        return btn_play_vid_radio;
    }

    // Get Pause Button Using Instance
    public Button getPauseButton() {
        return btn_pause_vid_radio;
    }

    // Get Cancel Button Using Instance
    public Button getCancelButton() {
        return btn_cross_view1;
    }

    public RelativeLayout getButtonView() {
        return dragView;
    }


    public SeekBar getSeekBar1() {
        return seek_bar_radio1;
    }

    public SlidingUpPanelLayout getSlidingDrawer() {
        return mLayout;
    }

    public TextView getTimeStartTv() {
        return tv_time_start;
    }


    public Handler gethandler() {
        return handler;
    }


    public Runnable getRunnable() {
        return myRunnable;
    }


    public RelativeLayout getRelateLayout() {
        return handle;
    }

    public ListView getListViewBotom() {
        return lv_bottom_pager;
    }

    public ProgressBar getProgressBarPlayPause() {
        return pb_playpause;
    }


    // Get Activity Visible Or Not
    public Boolean getActivityVisible() {
        return inTheActivity;
    }

    // Get Activity Visible Or Not
    public void getPlayButtonInvisible() {
        buttonPlayHandleAnimation();
    }

    // Get Activity Visible Or Not
    public void getPauseButtonInvisible() {
        buttonPauseHandleAnimation();
    }

    // Get Marquee enable
    public void getMarqueeEnable() {
        // enableTextMarquee();
        enableTextMove();
    }

    // Get Marquee disable
    public void getMarqueeDisable() {
        // disableTextMarquee();
        disableTextMove();
    }


    // Set To Check Player Is Playing Or Not...
    public Boolean setPlayerPlaying(boolean val) {
        isPlayerPlaying = val;
        return isPlayerPlaying;
    }

    // Set To Check Player Is Playing Or Not...
    public Boolean setPlayerPlaying2(boolean val) {
        isPlayerPlaying2 = val;
        return isPlayerPlaying2;
    }

    // Get To Check Player Is Playing Or Not...
    public Boolean getPlayerPlaying() {
        return isPlayerPlaying;
    }

    // Get To Check Player Is Playing Or Not...
    public Boolean getPlayerPlaying2() {
        return isPlayerPlaying2;
    }

    // Update Seek bar...
    public void updateSeekBar() {
        updateSeekBar(((ApplicationSingleton) getApplicationContext()).player, seek_bar_radio1);
    }

    // Set To Check Player Is Playing Or Not...
    public Boolean setLoading(boolean val) {
        isLoading = val;
        return isLoading;
    }

    public RelativeLayout getRelativeUp() {
        return relative_up_text;
    }

    public RelativeLayout getRelativeDown() {
        return relative_down_text;
    }

    // OnStop
    @Override
    protected void onStop() {
        // Here to check you are in this activity or not for generate
        // notification...
        if (inTheActivity) {
            if (((ApplicationSingleton) getApplicationContext()).player != null) {
                if (((ApplicationSingleton) getApplicationContext()).player
                        .isPlaying()) {

                    new MyNotification(this, str);

                    // hideNotifications();

                } else {
                    if (MyNotification.getInstance() != null) {
                        MyNotification.getInstance().notificationCancel();
                    }

                    // hideNotifications();
                }
            } else {
                if (MyNotification.getInstance() != null) {
                    MyNotification.getInstance().notificationCancel();
                }

                // hideNotifications();
            }
        }
        super.onStop();
    }

    // Hide Notifications of GCM...
    @SuppressWarnings("unused")
    private void hideNotifications() {
        if (GCMNotificationIntentService.getInstance() != null) {
            GCMNotificationIntentService.getInstance().notificationCancelOne();
        }

        if (GCMNotificationIntentService.getInstance() != null) {
            GCMNotificationIntentService.getInstance().notificationCancelTwo();
        }

        if (GCMNotificationIntentService.getInstance() != null) {
            GCMNotificationIntentService.getInstance()
                    .notificationCancelThree();
        }

        if (GCMNotificationIntentService.getInstance() != null) {
            GCMNotificationIntentService.getInstance().notificationCancelFour();
        }
    }

    // Call onDestroy
    @Override
    protected void onDestroy() {
        Log.e("onDestroy", "onDestroy");
        Utilities.setUserIdDedicated(mContext, "");
        try {
            if (isBackPressed) {
                if (((ApplicationSingleton) getApplicationContext()).player != null) {

                    if (((ApplicationSingleton) getApplicationContext()).player
                            .isPlaying()) {

                        new MyNotification(this, str);

                    } else {
                        if (MyNotification.getInstance() != null) {
                            MyNotification.getInstance().notificationCancel();
                            stopService(new Intent(VideoActivity.this,
                                    MyRadioService.class));
                        } else {
                            stopService(new Intent(VideoActivity.this,
                                    MyRadioService.class));
                        }

                        System.exit(0);
                    }

                } else {

                    if (MyNotification.getInstance() != null) {
                        MyNotification.getInstance().notificationCancel();
                        stopService(new Intent(VideoActivity.this,
                                MyRadioService.class));

                    } else {
                        stopService(new Intent(VideoActivity.this,
                                MyRadioService.class));
                    }

                    System.exit(0);
                }

                if (handler != null) {
                    handler.removeCallbacks(myRunnable);
                    handler.removeCallbacks(null);
                }

                if (handler4 != null) {
                    handler4.removeCallbacks(myRunnable2);
                    handler4.removeCallbacks(null);
                }

            } else {
                if (handler != null) {
                    handler.removeCallbacks(myRunnable);
                    handler.removeCallbacks(null);
                }

                if (handler4 != null) {
                    handler4.removeCallbacks(myRunnable2);
                    handler4.removeCallbacks(null);
                }

                if (MyNotification.getInstance() != null) {
                    MyNotification.getInstance().notificationCancel();
                    stopService(new Intent(VideoActivity.this,
                            MyRadioService.class));

                } else {
                    stopService(new Intent(VideoActivity.this,
                            MyRadioService.class));
                }
                System.exit(0);
            }

            finish();

        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isBackPressed = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // OnBackPress
    @Override
    public void onBackPressed() {
        if (mLayout != null
                && (mLayout.getPanelState() == PanelState.EXPANDED || mLayout
                .getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    // onTouch Listener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (((ApplicationSingleton) getApplicationContext()).player != null) {

            if (isLoading == false) {
                if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
                    btn_pause_vid_radio.setVisibility(View.VISIBLE);
                    buttonPauseHandleAnimation();

                } else {
                    btn_play_vid_radio.setVisibility(View.VISIBLE);
                    buttonPlayHandleAnimation();
                }
            }
        } else {
            btn_play_vid_radio.setVisibility(View.VISIBLE);
            buttonPlayHandleAnimation();
        }
        return true;
    }

    // Pause Button Invisible After 2 Sec...
    public void buttonPauseHandleAnimation() {
        handler2 = new Handler();
        myRun = new Runnable() {
            public void run() {
                btn_pause_vid_radio.setVisibility(View.INVISIBLE);
                handler2.removeCallbacks(myRun);
            }
        };
        handler2.postDelayed(myRun, 2000);
    }

    // Play Button Invisible After 2 Sec...
    public void buttonPlayHandleAnimation() {
        handler3 = new Handler();
        myRun2 = new Runnable() {
            public void run() {
                btn_play_vid_radio.setVisibility(View.INVISIBLE);
                handler3.removeCallbacks(myRun2);
            }
        };
        handler3.postDelayed(myRun2, 2000);
    }

    // Enable TextView marquee at run time...
    public void enableTextMarquee() {
        tv_lecture_title1.setSelected(true);
        tv_lecture_description1.setSelected(true);
        tv_lecture_title1.setEllipsize(TruncateAt.MARQUEE);
        tv_lecture_description1.setEllipsize(TruncateAt.MARQUEE);
    }

    // Get Text View Gravity To Show text At Its Original Position...
    public void getTextGravityView() {
        setGravityTextViews();
    }

    // Get Total Size List
    public int getTotalSize() {
        return total_size;
    }

    // Set Total Size List
    public void setTotalSize(int total_size) {
        this.total_size = total_size;
    }

    // Call Intent here..
    public void getCallIntent() {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:"));
        startActivity(call);
    }
}
