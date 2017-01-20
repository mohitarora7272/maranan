package com.maranan;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.maranan.adapters.ListDeatailedImageAdapter;
import com.maranan.infiniteviewpager.InfinitePagerAdapter;
import com.maranan.infiniteviewpager.InfiniteViewPager;
import com.maranan.utils.ConnectionDetector;
import com.maranan.utils.Utilities;

import java.io.IOException;


public class ListDetailed extends AppCompatActivity implements OnClickListener,
        OnCompletionListener, OnPreparedListener, OnErrorListener, SeekBar.OnSeekBarChangeListener {

    private Button btn_phone_list;
    private Button btn_play_pause;
    private ProgressBar progress_loading_list;
    private SeekBar seek_bar_list;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private TextView tv_date_time;
    private TextView tv_title;
    private TextView tv_description;
    private RelativeLayout relativeLayout1;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    //private MediaPlayer mp;
    private Handler mHandler = new Handler();
    public Boolean isFirstTimePlay = true;
    private RelativeLayout relativeLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private ListDeatailedImageAdapter adapter_auto;
    private InfiniteViewPager pager_infinite;
    private NestedScrollView nestedScroll;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        // Handle UnCaughtException Exception Handler
//		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler(
//				this));
        setContentView(R.layout.activity_list_detailed);
        cd = new ConnectionDetector(this);
        isInternetPresent = cd.isConnectingToInternet();
        initializeView();

    }

    // initilalizeView
    private void initializeView() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        pager_infinite = (InfiniteViewPager) findViewById(R.id.pager_auto);
        pager_infinite.startAutoScroll();
        adapter_auto = new ListDeatailedImageAdapter(this, getIntent().getStringExtra("image"));
        PagerAdapter wrapperAdapter = new InfinitePagerAdapter(adapter_auto);
        pager_infinite.setAdapter(wrapperAdapter);
        btn_phone_list = (Button) findViewById(R.id.btn_phone_lists);
        btn_phone_list.setOnClickListener(ListDetailed.this);
        btn_play_pause = (Button) findViewById(R.id.btn_play_pauses);
        btn_play_pause.setOnClickListener(ListDetailed.this);
        btn_play_pause.setTag(R.drawable.play_blue);
        btn_play_pause.setBackgroundResource(R.drawable.play_blue);
        progress_loading_list = (ProgressBar) findViewById(R.id.progress_loading_lists);
        seek_bar_list = (SeekBar) findViewById(R.id.seek_bar_lists);
        seek_bar_list.setProgress(0);
        seek_bar_list.setThumb(null);
        seek_bar_list.setPadding(-1, 0, -1, 0);
        tv_start_time = (TextView) findViewById(R.id.tv_start_time);
        tv_start_time.setText("0:00");
        tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        tv_end_time.setText("0:00");
        tv_date_time = (TextView) findViewById(R.id.tv_date_time);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_description = (TextView) findViewById(R.id.tv_description);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        nestedScroll = (NestedScrollView) findViewById(R.id.nestedScroll);
        setIntentData();
        setMediaPlayer();
    }


    // Set Media Player...
    private void setMediaPlayer() {
        //mp = new MediaPlayer();
        if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
            if (((ApplicationSingleton) getApplicationContext()).player2.isPlaying()) {

                isFirstTimePlay = false;
                btn_play_pause.setTag(R.drawable.pause_blue);
                btn_play_pause.setBackgroundResource(R.drawable.pause_blue);
                updateProgressBar();

            } else {

                ((ApplicationSingleton) getApplicationContext()).player2 = new MediaPlayer();

                if (Utilities.isComingFromNoti(this)) {

                    if (getIntent().getStringExtra("radio_alert") != null) {
                        if (!getIntent().getStringExtra("radio_alert").equals("")) {
                            Utilities.setSharedPrefForNoti(this, false, "", "");
                            isFirstTimePlay = false;
                            progress_loading_list.setVisibility(View.VISIBLE);
                            btn_play_pause.setVisibility(View.GONE);
                            startFromUrl(getIntent().getStringExtra("radio_alert"));
                        } else {
                            Utilities.setSharedPrefForNoti(this, false, "", "");
                        }
                    } else {
                        Utilities.setSharedPrefForNoti(this, false, "", "");
                    }
                }
            }
        }
        seek_bar_list.setOnSeekBarChangeListener(ListDetailed.this);
    }

    // Set Intent Data...
    private void setIntentData() {
        if (getIntent().getStringExtra("title") != null) {
            if (!getIntent().getStringExtra("title").equals("")) {
                tv_title.setText(Utilities.decodeImoString(getIntent().getStringExtra("title")));
            }
        }

        if (getIntent().getStringExtra("description") != null) {
            if (!getIntent().getStringExtra("description").equals("")) {
                tv_description.setText(Utilities.decodeImoString(getIntent().getStringExtra("description")));
            }
        }

        if (getIntent().getStringExtra("date") != null) {
            if (!getIntent().getStringExtra("date").equals("")) {
                tv_date_time.setText(getIntent().getStringExtra("date") + " " + getIntent().getStringExtra("time"));
            }
        }
        if (getIntent().getStringExtra("radio_alert") != null) {
            if (!getIntent().getStringExtra("radio_alert").equals("")) {
                btn_play_pause.setVisibility(View.VISIBLE);
                relativeLayout1.setVisibility(View.VISIBLE);

            } else {
                btn_play_pause.setVisibility(View.INVISIBLE);
                relativeLayout1.setVisibility(View.INVISIBLE);
            }

        } else {
            btn_play_pause.setVisibility(View.INVISIBLE);
        }

        if (getIntent().getStringExtra("phone") != null) {
            if (!getIntent().getStringExtra("phone").equals("")) {

                btn_phone_list.setVisibility(View.VISIBLE);

            } else {
                btn_phone_list.setVisibility(View.INVISIBLE);
            }

        } else {
            btn_phone_list.setVisibility(View.INVISIBLE);
        }

        if (getIntent().getStringExtra("radio_alert").equals("") && getIntent().getStringExtra("phone").equals("")) {
            relativeLayout.setVisibility(View.GONE);
            nestedScroll.setPadding(0, 0, 0, 0);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phone_lists:
                if (((ApplicationSingleton) getApplicationContext()).player2.isPlaying()) {
                    if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
                        btn_play_pause.setTag(R.drawable.play_blue);
                        btn_play_pause.setBackgroundResource(R.drawable.play_blue);
                        ((ApplicationSingleton) getApplicationContext()).player2.pause();
                    }
                }
                if (getIntent().getStringExtra("phone") != null) {
                    if (!getIntent().getStringExtra("phone").equals("")) {
                        Utilities.getCallIntent(this,
                                getIntent().getStringExtra("phone"));
                    }
                }
                break;

            case R.id.btn_play_pauses:
                if (isInternetPresent) {
                    if (btn_play_pause.getTag().equals(R.drawable.play_blue)) {

                        if (getIntent().getStringExtra("radio_alert") != null) {
                            if (!getIntent().getStringExtra("radio_alert").equals("")) {

                                if (isFirstTimePlay) {
                                    progress_loading_list.setVisibility(View.VISIBLE);
                                    btn_play_pause.setVisibility(View.GONE);
                                    startFromUrl(getIntent().getStringExtra("radio_alert"));

                                } else {
                                    if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
                                        ((ApplicationSingleton) getApplicationContext()).player2.start();
                                        btn_play_pause.setTag(R.drawable.pause_blue);
                                        btn_play_pause.setBackgroundResource(R.drawable.pause_blue);
                                        //updateProgressBar();
                                    }
                                }

                            } else {
                                Utilities.showSnackbar(this, "Alert Not Found");
                            }
                        } else {
                            Utilities.showSnackbar(this, "Alert Not Found");
                        }

                    } else {
                        if (((ApplicationSingleton) getApplicationContext()).player2.isPlaying()) {
                            if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
                                btn_play_pause.setTag(R.drawable.play_blue);
                                btn_play_pause.setBackgroundResource(R.drawable.play_blue);
                                ((ApplicationSingleton) getApplicationContext()).player2.pause();
                            }
                        }
                    }

                } else {
                    Utilities.showAlertDialog(this, "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                }
                break;

            default:
                break;
        }

    }

    // Here is the method to start mp3 from url...
    public void startFromUrl(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // String radioUrl = getStreamingURL(URL);
                try {
                    ((ApplicationSingleton) getApplicationContext()).player2.reset();
                    ((ApplicationSingleton) getApplicationContext()).player2.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    ((ApplicationSingleton) getApplicationContext()).player2.setOnPreparedListener(ListDetailed.this);
                    ((ApplicationSingleton) getApplicationContext()).player2.setOnCompletionListener(ListDetailed.this);
                    ((ApplicationSingleton) getApplicationContext()).player2.setOnErrorListener(ListDetailed.this);
                    ((ApplicationSingleton) getApplicationContext()).player2.setDataSource(ListDetailed.this, Uri.parse(url));
                    ((ApplicationSingleton) getApplicationContext()).player2.prepareAsync();

                } catch (IllegalArgumentException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                } catch (IllegalStateException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();

                } catch (IOException e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }

        }).start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isFirstTimePlay = false;
        btn_play_pause.setTag(R.drawable.pause_blue);
        btn_play_pause.setBackgroundResource(R.drawable.pause_blue);
        btn_play_pause.setVisibility(View.VISIBLE);
        progress_loading_list.setVisibility(View.GONE);
        if (!mp.isPlaying()) {
            mp.start();
            updateProgressBar();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isFirstTimePlay = true;
        seek_bar_list.setProgress(0);
        btn_play_pause.setBackgroundResource(R.drawable.play_blue);
        btn_play_pause.setTag(R.drawable.play_blue);
        tv_start_time.setText("");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = ((ApplicationSingleton) getApplicationContext()).player2.getDuration();
        int currentPosition = Utilities.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        ((ApplicationSingleton) getApplicationContext()).player2.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = ((ApplicationSingleton) getApplicationContext()).player2.getDuration();
            long currentDuration = ((ApplicationSingleton) getApplicationContext()).player2.getCurrentPosition();

            // Displaying Total Duration time
            tv_end_time.setText("" + Utilities.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            tv_start_time.setText("" + Utilities.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (Utilities.getProgressPercentage(currentDuration, totalDuration));
            seek_bar_list.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    // onBackPress
    @Override
    public void onBackPressed() {
        mHandler.removeCallbacks(mUpdateTimeTask);
//		if (((ApplicationSingleton) getApplicationContext()).player2 != null) {
//			if (((ApplicationSingleton) getApplicationContext()).player2.isPlaying()) {
//				mHandler.removeCallbacks(mUpdateTimeTask);
//				((ApplicationSingleton) getApplicationContext()).player2.stop();
//				((ApplicationSingleton) getApplicationContext()).player2.release();
//				((ApplicationSingleton) getApplicationContext()).player2 = new MediaPlayer();
//			}
//		}
        Intent mintent = new Intent();
        mintent.putExtra("alert_id", getIntent().getStringExtra("alert_id"));
        setResult(RESULT_OK, mintent);
        finish();
        overridePendingTransition(R.anim.activity_open_scale,
                R.anim.activity_close_translate);
        super.onBackPressed();
    }

}
