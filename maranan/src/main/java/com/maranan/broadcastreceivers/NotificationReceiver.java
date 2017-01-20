package com.maranan.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.maranan.ApplicationSingleton;
import com.maranan.VideoActivity;
import com.maranan.notifications.MyNotification;

public class NotificationReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		try {
		if (action.equalsIgnoreCase("com.example.app.ACTION_PLAY")) {
			
			if (((ApplicationSingleton) context.getApplicationContext()).player.isPlaying()) {
				
				((ApplicationSingleton) context.getApplicationContext()).player.pause();
				MyNotification.getInstance().getNotificationForChange(false);
				
				if (VideoActivity.getInstance().getCancelButton() != null)
				VideoActivity.getInstance().getCancelButton().setVisibility(View.VISIBLE);
				if (VideoActivity.getInstance().getPauseButton() != null)
				VideoActivity.getInstance().getPauseButton().setVisibility(View.INVISIBLE);
				if (VideoActivity.getInstance().getPlayButton() != null)
				VideoActivity.getInstance().getPlayButton().setVisibility(View.VISIBLE);
				if (VideoActivity.getInstance().getRelateLayout() != null)
				VideoActivity.getInstance().getRelateLayout().setVisibility(View.VISIBLE);
				if (VideoActivity.getInstance().getListViewBotom() != null)
				VideoActivity.getInstance().getListViewBotom().setVisibility(View.VISIBLE);
				VideoActivity.getInstance().getPlayButtonInvisible();
				VideoActivity.getInstance().getMarqueeDisable();
				VideoActivity.getInstance().getTextGravityView();
			
			} else if (!((ApplicationSingleton) context.getApplicationContext()).player.isPlaying()) {
				
				((ApplicationSingleton) context.getApplicationContext()).player.start();
				MyNotification.getInstance().getNotificationForChange(true);
				
				if (VideoActivity.getInstance().getRelateLayout() != null)
				VideoActivity.getInstance().getRelateLayout().setVisibility(View.VISIBLE);
				if (VideoActivity.getInstance().getListViewBotom() != null)
				VideoActivity.getInstance().getListViewBotom().setVisibility(View.VISIBLE);
				if (VideoActivity.getInstance().getPlayButton() != null)
				VideoActivity.getInstance().getPlayButton().setVisibility(View.INVISIBLE);
				if (VideoActivity.getInstance().getPauseButton() != null)
				VideoActivity.getInstance().getPauseButton().setVisibility(View.VISIBLE);
				if (VideoActivity.getInstance().getCancelButton() != null)
				VideoActivity.getInstance().getCancelButton().setVisibility(View.VISIBLE);
				if (VideoActivity.getInstance().getRelativeUp() != null)
				VideoActivity.getInstance().getRelativeUp().setVisibility(View.GONE);
				if (VideoActivity.getInstance().getRelativeDown() != null)
				VideoActivity.getInstance().getRelativeDown().setVisibility(View.VISIBLE);
				VideoActivity.getInstance().getPauseButtonInvisible();
				VideoActivity.getInstance().getMarqueeEnable();
			}
		 } 
		
		} catch (Exception e) {
			Log.e("Exception??", ""+e.getMessage());
		}
	}

}
