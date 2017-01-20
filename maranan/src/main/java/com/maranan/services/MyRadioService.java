package com.maranan.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import com.maranan.ApplicationSingleton;
import com.maranan.VideoActivity;
import com.maranan.notifications.MyNotification;
import com.maranan.utils.Utilities;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyRadioService extends Service implements OnCompletionListener,
        OnPreparedListener, OnBufferingUpdateListener, OnErrorListener,
        OnSeekCompleteListener, OnInfoListener {
    // MediaPlayer myPlayer;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private static MyRadioService mContext;
    protected PowerManager.WakeLock mWakeLock;
    private String RadioURL;
    private double startTime = 0;
    private Handler myHandler;

    // VideoNewActivity Instance
    public static MyRadioService getInstance() {
        return mContext;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if ((String) intent.getExtras().get("RadioUrl") != null) {
                RadioURL = (String) intent.getExtras().get("RadioUrl");

            } else {
                RadioURL = "";
                stopService(intent);
            }
        } catch (Exception e) {
            Log.e("onStartCommand", "Exception?? " + e.getMessage());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = MyRadioService.this;
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        myHandler = new Handler();
        if (((ApplicationSingleton) getApplicationContext()).player == null) {
            ((ApplicationSingleton) getApplicationContext()).player = new MediaPlayer();
        }
        ((ApplicationSingleton) getApplicationContext()).player.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("Error", "ee?? " + what);
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mWakeLock.release();
        myHandler.removeCallbacks(UpdateSongTime);

        if (((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
            ((ApplicationSingleton) getApplicationContext()).player.stop();
            ((ApplicationSingleton) getApplicationContext()).player.release();
            ((ApplicationSingleton) getApplicationContext()).player = null;
        } else {
            ((ApplicationSingleton) getApplicationContext()).player.stop();
            ((ApplicationSingleton) getApplicationContext()).player.release();
            ((ApplicationSingleton) getApplicationContext()).player = null;
        }

        // Stop Service here...
        //stopService(intent);

    }

    @Override
    public void onStart(Intent intent, int startid) {
        if (progressStatus == 0) {
            if (RadioURL != null) {
                if (!RadioURL.equals("")) {
                    progressStatus = 1;
                    VideoActivity.getInstance().setLoading(true);
                    VideoActivity.getInstance().getProgressBarPlayPause().setVisibility(View.VISIBLE);
                    VideoActivity.getInstance().getPlayButton().setVisibility(View.INVISIBLE);
                    VideoActivity.getInstance().getPauseButton().setVisibility(View.INVISIBLE);
                    startRadio(RadioURL);
                }
            }
        } else {
            VideoActivity.getInstance().getProgressBarPlayPause().setVisibility(View.GONE);
            VideoActivity.getInstance().getPlayButton().setVisibility(View.INVISIBLE);
            VideoActivity.getInstance().getPauseButton().setVisibility(View.INVISIBLE);
            VideoActivity.getInstance().getCancelButton().setVisibility(View.VISIBLE);
            VideoActivity.getInstance().getRelateLayout().setVisibility(View.VISIBLE);
            VideoActivity.getInstance().getListViewBotom().setVisibility(View.VISIBLE);
            VideoActivity.getInstance().getRelativeUp().setVisibility(View.GONE);
            VideoActivity.getInstance().getRelativeDown().setVisibility(View.VISIBLE);
            VideoActivity.getInstance().getPauseButtonInvisible();
            VideoActivity.getInstance().getMarqueeEnable();
            VideoActivity.getInstance().setLoading(false);

            if (!((ApplicationSingleton) getApplicationContext()).player.isPlaying()) {
                ((ApplicationSingleton) getApplicationContext()).player.start();
                VideoActivity.getInstance().updateSeekBar();
            }
        }
    }

    // Start Radio Here
    public void startRadio(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // String radioUrl = getStreamingURL(URL);
                try {
                    ((ApplicationSingleton) getApplicationContext()).player.setDataSource(MyRadioService.this, Uri.parse(url));
                    ((ApplicationSingleton) getApplicationContext()).player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    ((ApplicationSingleton) getApplicationContext()).player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                    ((ApplicationSingleton) getApplicationContext()).player.setOnCompletionListener(MyRadioService.this);
                    ((ApplicationSingleton) getApplicationContext()).player.setOnPreparedListener(MyRadioService.this);
                    ((ApplicationSingleton) getApplicationContext()).player.setOnSeekCompleteListener(MyRadioService.this);
                    ((ApplicationSingleton) getApplicationContext()).player.setOnBufferingUpdateListener(MyRadioService.this);
                    ((ApplicationSingleton) getApplicationContext()).player.setOnInfoListener(MyRadioService.this);
                    ((ApplicationSingleton) getApplicationContext()).player.setOnErrorListener(MyRadioService.this);
                    ((ApplicationSingleton) getApplicationContext()).player.prepareAsync();

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
    public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer arg0) {
        if(arg0.isPlaying()){
            VideoActivity.getInstance().getProgressBarPlayPause().setVisibility(View.GONE);
            VideoActivity.getInstance().setLoading(false);
        }else{
            VideoActivity.getInstance().getProgressBarPlayPause().setVisibility(View.VISIBLE);
            VideoActivity.getInstance().setLoading(true);
        }
    }

    // onBufferingUpdate
    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int percent) {

        VideoActivity.getInstance().getProgressBarPlayPause().setVisibility(View.GONE);
    }

    // onPrepared
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (!mediaPlayer.isPlaying()) {
            if (((ApplicationSingleton) getApplicationContext()).player == mediaPlayer) {
                VideoActivity.getInstance().getProgressBarPlayPause().setVisibility(View.GONE);
                VideoActivity.getInstance().getPlayButton().setVisibility(View.INVISIBLE);
                VideoActivity.getInstance().getPauseButton().setVisibility(View.VISIBLE);
                VideoActivity.getInstance().getCancelButton().setVisibility(View.VISIBLE);
                VideoActivity.getInstance().getRelateLayout().setVisibility(View.VISIBLE);
                VideoActivity.getInstance().getListViewBotom().setVisibility(View.VISIBLE);
                VideoActivity.getInstance().getRelativeUp().setVisibility(View.GONE);
                VideoActivity.getInstance().getRelativeDown().setVisibility(View.VISIBLE);
                VideoActivity.getInstance().getPauseButtonInvisible();
                VideoActivity.getInstance().getMarqueeEnable();
                VideoActivity.getInstance().setLoading(false);

                try {
                    mediaPlayer.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (Utilities.isPlayerStart(mContext)) {
                    mediaPlayer.seekTo(Utilities.getPlayerPosition(mContext));
                }

                mediaPlayer.start();
                VideoActivity.getInstance().updateSeekBar();
                startTime = mediaPlayer.getCurrentPosition();

                String hms = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours((long) startTime),
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime)
                                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) startTime)),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)));
                VideoActivity.getInstance().getTimeStartTv().setText(hms);
                myHandler.postDelayed(UpdateSongTime, 100);
            }
        }

    }

    // onCompletion
    @Override
    public void onCompletion(MediaPlayer arg0) {
        if (MyNotification.getInstance() != null) {
            if (MyNotification.getInstance().isNotificationGenerate().equals(true)) {
                MyNotification.getInstance().getNotificationForChange(false);
            }
        }

        VideoActivity.getInstance().getSeekBar1().setProgress(0);
        VideoActivity.getInstance().getTimeStartTv().setText("");
        VideoActivity.getInstance().getTimeStartTv().setText("00:00:00");
        VideoActivity.getInstance().getProgressBarPlayPause().setVisibility(View.GONE);
        VideoActivity.getInstance().getPauseButton().setVisibility(View.INVISIBLE);
        VideoActivity.getInstance().getPlayButton().setVisibility(View.VISIBLE);
        VideoActivity.getInstance().getPlayButtonInvisible();
        VideoActivity.getInstance().getRelativeUp().setVisibility(View.VISIBLE);
        VideoActivity.getInstance().getRelativeDown().setVisibility(View.GONE);
        //VideoActivity.getInstance().disableTextMarquee();
        VideoActivity.getInstance().getMarqueeDisable();
        VideoActivity.getInstance().getTextGravityView();
    }

    // Streaming URL Here
    public String getStreamingURL(String string) {

        String html = "";
        String result = "";
        try {
            URL updateURL = new URL(string);
            URLConnection conn = updateURL.openConnection();
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);

            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            html = new String(baf.toByteArray());

            String re1 = ".*?";
            String re2 = "((?:http|https)(?::\\/{2}[\\w]+)(?:[\\/|\\.]?)(?:[^\\s\"]*))";

            Pattern p = Pattern.compile(re1 + re2, Pattern.CASE_INSENSITIVE
                    | Pattern.DOTALL);
            Matcher m = p.matcher(html);
            if (m.find()) {
                String httpurl1 = m.group(1);
                result = httpurl1;
            } else {

            }

        } catch (Exception e) {
            Log.e("StreamingError", ">> " + e.getMessage());
        }
        return result;
    }

    // Error Handling here
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.e("MEDIA_ERROR_UNKNOWN==>", "unknown media playback error");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e("ERROR_SERVER_DIED==>>", "server connection died");
            default:
                Log.e("audio playback error",
                        "generic audio playback error");
                break;
        }

        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                Log.e("MEDIA_ERROR_IO==>>", "IO media error");
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                Log.e("ERROR_MALFORMED==>>", "media error, malformed");
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                Log.e("ERROR_UNSUPPORTED==>>", "unsupported media content");
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                Log.e("ERROR_TIMED_OUT==>", "media timeout error");
                break;
            default:
                Log.e("playback error==>>", "unknown playback error");
                break;
        }

        return true;
    }

    // Get Media Player Instance
    public MediaPlayer getMediaPlayer() {
        return ((ApplicationSingleton) getApplicationContext()).player;
    }

    // Stop Services...
    public boolean stopService(Intent intent) {
        return super.stopService(intent);
    }

    // Update Radio Duration Here...
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = ((ApplicationSingleton) getApplicationContext()).player.getCurrentPosition();
            String hms = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours((long) startTime),
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) startTime)),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)));
            VideoActivity.getInstance().getTimeStartTv().setText(hms);
            myHandler.postDelayed(this, 100);
        }
    };

    public Runnable getRunnable() {
        return UpdateSongTime;
    }

    public Handler getHandler() {
        return handler;
    }
}
