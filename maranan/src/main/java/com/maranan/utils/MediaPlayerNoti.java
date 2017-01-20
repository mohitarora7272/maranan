package com.maranan.utils;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;

import com.maranan.ApplicationSingleton;
import com.maranan.adapters.AlertsListAdapter;

public abstract class MediaPlayerNoti implements OnCompletionListener, OnPreparedListener, OnErrorListener{
	Context context;
	@SuppressWarnings("unused")
	private ProgressDialog pDialog;
	
	 public MediaPlayerNoti(Context context, String url) {
	        this.context = context;
	        startFromUrl(url);
	    }
	
	// Here is the method to start mp3 from url...
	public void startFromUrl(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					((ApplicationSingleton) context.getApplicationContext()).player2.reset();
					((ApplicationSingleton) context.getApplicationContext()).player2 = new MediaPlayer();
					((ApplicationSingleton) context.getApplicationContext()).player2.setAudioStreamType(AudioManager.STREAM_MUSIC);
					((ApplicationSingleton) context.getApplicationContext()).player2.setOnPreparedListener(MediaPlayerNoti.this);
					((ApplicationSingleton) context.getApplicationContext()).player2.setOnCompletionListener(MediaPlayerNoti.this);
					((ApplicationSingleton) context.getApplicationContext()).player2.setOnErrorListener(MediaPlayerNoti.this);
					((ApplicationSingleton) context.getApplicationContext()).player2.setDataSource(context, Uri.parse(url));
					((ApplicationSingleton) context.getApplicationContext()).player2.prepareAsync();

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
	
	// Prepare Media player to play...
	@Override
	public void onPrepared(MediaPlayer mp) {
		
		if (!mp.isPlaying()) {
			mp.start();
			AlertsListAdapter.getInstance().playNotificationAlert();
			AlertsListAdapter.getInstance().setClickEnable(true);
		}
	}

	// Oncompletion Listener To Listen Media Player
	@Override
	public void onCompletion(MediaPlayer mp) {
		if (!mp.isPlaying()) {
			mp.stop();
			mp.release();
			//Utilities.setSharedPrefForNoti(context, false, "", "");
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.e("error??", "?? " + what);
		return true;
	}
}
