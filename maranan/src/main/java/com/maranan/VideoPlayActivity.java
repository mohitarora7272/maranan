package com.maranan;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.maranan.utils.Config;

public class VideoPlayActivity extends Fragment implements
		YouTubePlayer.OnInitializedListener,
		YouTubePlayer.OnFullscreenListener,
		YouTubePlayer.PlaybackEventListener,
		YouTubePlayer.PlaylistEventListener, Provider,
		YouTubePlayer.PlayerStateChangeListener, OnClickListener {

	private static VideoPlayActivity mContext;
	private YouTubePlayerView playerView;
	private YouTubePlayer player;
	private static final int RECOVERY_DIALOG_REQUEST = 1;
	private String vid_id;
	private ImageView img_sideBtn_In;
	private boolean check;
	private ArrayList<HashMap<String, String>> arrayList;
	private ArrayList<String> listVidIdNew;
	private View relate_Side;
	private View main_layout;
	private int currentUi;
	private Editor editor;
	boolean wasRestored;
	SharedPreferences pref, pref2;
	Handler h;
	int pos = 0;


	public VideoPlayActivity(String vid_id, boolean check, ArrayList<HashMap<String, String>> arrayList,
			ArrayList<HashMap<String, String>> arrayListFull) {
		this.vid_id = vid_id;
		this.check = check;
		this.arrayList = arrayList;
		listVidIdNew = new ArrayList<String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_video_play, container, false);
		mContext = VideoPlayActivity.this;
		main_layout = rootView.getRootView();
		getActivity();
		pref = getActivity().getSharedPreferences("StoreValues", Activity.MODE_PRIVATE);
		editor = pref.edit();
		editor.putInt("key", pos);
		initializeView(rootView);
		return rootView;
	}

	// initializeView
	private void initializeView(final View rootView) {
		playerView = (YouTubePlayerView) rootView.findViewById(R.id.player_view_new);
		playerView.initialize(Config.DEVELOPER_KEY, this);
		img_sideBtn_In = (ImageView) rootView.findViewById(R.id.img_sideBtn_In);
		img_sideBtn_In.setOnClickListener(this);
		relate_Side = (View) rootView.findViewById(R.id.relate_Side);
		relate_Side.setOnClickListener(this);

		// Check Video ID and boolean Values for playing videos in sequence...
		if (check == true && arrayList.size() >= 14) {
			for (int i = 0; i < 14; i++) {
				listVidIdNew.add((String) (arrayList.get(i).get("VideoId")));
			}

		} else if (check == true && arrayList.size() <= 14) {
			for (int i = 0; i < arrayList.size(); i++) {
				listVidIdNew.add((String) (arrayList.get(i).get("VideoId")));
			}
		} 
	}

	// VideoPlayActivity Instance
	public static VideoPlayActivity getInstance() {
		return mContext;
	}

	@Override
	public void onInitializationFailure(Provider provider,
			YouTubeInitializationResult errorReason) {
		if (errorReason.isUserRecoverableError()) {
			errorReason.getErrorDialog(getActivity(), RECOVERY_DIALOG_REQUEST)
					.show();
		} else {
			String errorMessage = String.format(getString(R.string.failed),
					errorReason.toString());
			Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void onInitializationSuccess(Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			/** add listeners to YouTubePlayer instance **/
			this.player = player;
			this.wasRestored = wasRestored;
			player.setPlayerStateChangeListener(this);
			player.setPlaybackEventListener(this);
			player.setOnFullscreenListener(this);
			player.setPlaylistEventListener(this);
			player.setPlayerStyle(PlayerStyle.DEFAULT);
			player.setFullscreen(true);
			player.setShowFullscreenButton(true);
			player.setManageAudioFocus(true);
			player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
					| YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
					| YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT
					| YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
					| YouTubePlayerView.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| YouTubePlayerView.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| YouTubePlayerView.SYSTEM_UI_FLAG_FULLSCREEN
					| YouTubePlayerView.FOCUS_AFTER_DESCENDANTS
					| YouTubePlayerView.KEEP_SCREEN_ON);
			player.loadVideo(vid_id);
			editor.putBoolean("wasRestored", true);
			editor.commit();
		}
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		super.getActivity().onWindowFocusChanged(hasFocus);
		if (!hasFocus) {
			Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			getActivity().sendBroadcast(closeDialog);
		}
	}

	@SuppressWarnings("unused")
	private Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) getActivity().findViewById(R.id.player_view_new);
	}

	@Override
	public void onFullscreen(boolean arg0) {
	}

	@Override
	public void onBuffering(boolean arg0) {
		currentUi = main_layout.getSystemUiVisibility();
		if (currentUi == 0) {
			//img_sideBtn_In.setVisibility(View.VISIBLE);
			//relate_Side.setVisibility(View.VISIBLE);
			//hideButton();
		}
	}

	@Override
	public void onPaused() {
	}

	@Override
	public void onPlaying() {
	}

	@Override
	public void onSeekTo(int arg0) {
	}

	@Override
	public void onStopped() {
	}

	@Override
	public void onAdStarted() {
	}

	@Override
	public void onError(ErrorReason reason) {
		if (reason.toString().equals("UNAUTHORIZED_OVERLAY")) {
			if (!player.isPlaying()) {
				player.play();
			} else {
				player.play();
			}
		} else if (reason.toString().equals("NOT_PLAYABLE") && check == false) {

		} else if (reason.toString().equals("NOT_PLAYABLE") && check == true) {
			
			setVideoEndCredentials();
		}

	}

	@Override
	public void onLoaded(String arg0) {
	}

	@Override
	public void onLoading() {
	}

	@Override
	public void onVideoEnded() {
		setVideoEndCredentials();
	}

	@Override
	public void onVideoStarted() {
	}

	@Override
	public void initialize(String arg0, OnInitializedListener arg1) {
	}

	@Override
	public void onNext() {
	}

	@Override
	public void onPlaylistEnded() {
	}

	@Override
	public void onPrevious() {
	}

	// finishActivity Here...
	private void finishActivity() {
		Intent mintent = new Intent();
	    mintent.putExtra("isPlayerPlaying", VideoDrawerActivity.getInstance().getIntentValue());
	    mintent.putExtra("isPlayerPlaying2", VideoDrawerActivity.getInstance().getIntentValue2());
		getActivity();
		getActivity().setResult(Activity.RESULT_OK, mintent);
		getActivity().finish();
		getActivity().overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);
	}

	// Click Listener on Widgets
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.img_sideBtn_In:
			VideoDrawerActivity
					.getInstance()
					.getDrawer()
					.openDrawer(
							VideoDrawerActivity.getInstance()
									.getRelativeLayout());
			break;

		case R.id.relate_Side:
			VideoDrawerActivity
					.getInstance()
					.getDrawer()
					.openDrawer(
							VideoDrawerActivity.getInstance()
									.getRelativeLayout());
			break;

		case R.id.player_view_new:
			
			break;

		default:
			break;
		}

	}

	// Set End Video Loading Credentials
	private void setVideoEndCredentials() {
		if (listVidIdNew.size() > 0) {
			if (listVidIdNew.size() != pref.getInt("key", -1)) {
				if (pref.getInt("key", -1) == pos) {
					if (pref.getBoolean("StoreValues", true) == true) {
						player.loadVideo(listVidIdNew.get(pos));
						pos++;
						pref = getActivity().getSharedPreferences("StoreValues", Activity.MODE_PRIVATE);
						editor = pref.edit();
						editor.putInt("key", pos);
						editor.commit();
					}
				}
			} else {
				finishActivity();
			}

		} else {
			finishActivity();
		}
	}

	//Hide side button after 1 second for first time...
	@SuppressWarnings("unused")
	private void hideButton() {
		h = new Handler();
		h.postDelayed(new Runnable() {

			public void run() {
				img_sideBtn_In.setVisibility(View.INVISIBLE);
				relate_Side.setVisibility(View.VISIBLE);
			}
		}, 4000);
	}

	// get ImageView From Instance
	public ImageView getImageView() {
		return img_sideBtn_In;
	}

	// get ImageView From Instance
	public View getView() {
		return relate_Side;
	}

}
