package com.maranan.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;

import com.maranan.ApplicationSingleton;
import com.maranan.VideoActivity;
import com.maranan.notifications.MyNotification;
import com.maranan.services.MyRadioService;

public class DeleteSwipeBroadcastReceiver extends BroadcastReceiver {
	private SharedPreferences pref;
	private Editor edit;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String action = intent.getAction();
			if (action.equalsIgnoreCase("com.example.app.ACTION_DELETE")) {
				if (((ApplicationSingleton) context.getApplicationContext()).player != null) {
				if (((ApplicationSingleton) context.getApplicationContext()).player.isPlaying()) {
					if (MyRadioService.getInstance().getHandler() != null) {
						MyRadioService.getInstance().getHandler().removeCallbacks(MyRadioService.getInstance().getRunnable());
					}
					
					context.stopService(new Intent(context, MyRadioService.class));
					if (MyNotification.getInstance().getnotificationmanager() != null) {
						MyNotification.getInstance().notificationCancel();
					}
					
					VideoActivity.getInstance().getCancelButton().setVisibility(View.INVISIBLE);
					VideoActivity.getInstance().getTimeStartTv().setText("");
					VideoActivity.getInstance().getTimeStartTv().setText("00:00:00");
					VideoActivity.getInstance().getSeekBar1().setProgress(0);
					VideoActivity.getInstance().getPauseButton().setVisibility(View.INVISIBLE);
					VideoActivity.getInstance().getPlayButton().setVisibility(View.VISIBLE);
					VideoActivity.getInstance().getRelateLayout().setVisibility(View.VISIBLE);
					VideoActivity.getInstance().getListViewBotom().setVisibility(View.VISIBLE);
					if (VideoActivity.getInstance().getRelativeUp() != null)
					VideoActivity.getInstance().getRelativeUp().setVisibility(View.VISIBLE);
					if (VideoActivity.getInstance().getRelativeDown() != null)
					VideoActivity.getInstance().getRelativeDown().setVisibility(View.GONE);
					VideoActivity.getInstance().getPlayButtonInvisible();
					VideoActivity.getInstance().getMarqueeDisable();
					VideoActivity.getInstance().getTextGravityView();
					pref = context.getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
					if (pref != null) 
					if (pref.contains("Noti1") && pref.contains("Noti2") && pref.contains("Noti3")) {
						edit = pref.edit();
						edit.putBoolean("Noti1", false);
						edit.putBoolean("Noti2", false);
						edit.putBoolean("Noti3", false);
						edit.commit();
					}

				} else if (!((ApplicationSingleton) context.getApplicationContext()).player.isPlaying()) {
					if (MyRadioService.getInstance().getHandler() != null) {
						MyRadioService.getInstance().getHandler().removeCallbacks(MyRadioService.getInstance().getRunnable());
					}
					context.stopService(new Intent(context, MyRadioService.class));
					
					if (MyNotification.getInstance().getnotificationmanager() != null) {
						MyNotification.getInstance().notificationCancel();
					}else{
						MyNotification.getInstance().notificationCancel();
					}
					
					VideoActivity.getInstance().getCancelButton().setVisibility(View.INVISIBLE);
					VideoActivity.getInstance().getTimeStartTv().setText("");
					VideoActivity.getInstance().getTimeStartTv().setText("00:00:00");
					VideoActivity.getInstance().getSeekBar1().setProgress(0);
					VideoActivity.getInstance().getPauseButton().setVisibility(View.INVISIBLE);
					VideoActivity.getInstance().getPlayButton().setVisibility(View.VISIBLE);
					VideoActivity.getInstance().getRelateLayout().setVisibility(View.VISIBLE);
					VideoActivity.getInstance().getListViewBotom().setVisibility(View.VISIBLE);
					if (VideoActivity.getInstance().getRelativeUp() != null)
					VideoActivity.getInstance().getRelativeUp().setVisibility(View.VISIBLE);
					if (VideoActivity.getInstance().getRelativeDown() != null)
					VideoActivity.getInstance().getRelativeDown().setVisibility(View.GONE);
					VideoActivity.getInstance().getPlayButtonInvisible();
					VideoActivity.getInstance().getMarqueeDisable();
					VideoActivity.getInstance().getTextGravityView();
					
					pref = context.getSharedPreferences("NotificationPref", Context.MODE_PRIVATE);
					if (pref != null) 
					if (pref.contains("Noti1") && pref.contains("Noti2") && pref.contains("Noti3")) {
						edit = pref.edit();
						edit.putBoolean("Noti1", false);
						edit.putBoolean("Noti2", false);
						edit.putBoolean("Noti3", false);
						edit.commit();
					}

				  }
				}
			} 
		} catch (Exception e) {

		}
	}
}
