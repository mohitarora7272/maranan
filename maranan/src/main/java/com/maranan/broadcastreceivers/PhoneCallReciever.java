package com.maranan.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maranan.ApplicationSingleton;
import com.maranan.VideoActivity;
import com.maranan.notifications.MyNotification;
import com.maranan.services.MyRadioService;

public class PhoneCallReciever extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equalsIgnoreCase("com.example.app.CALL")) {
			
			VideoActivity.getInstance().getCallIntent();
			
			if (((ApplicationSingleton) context.getApplicationContext()).player != null) {
			if (((ApplicationSingleton) context.getApplicationContext()).player.isPlaying()) {
				if (MyRadioService.getInstance().getHandler() != null) {
					MyRadioService.getInstance().getHandler().removeCallbacks(MyRadioService.getInstance().getRunnable());
				}
				context.stopService(new Intent(context, MyRadioService.class));
				if (MyNotification.getInstance().getnotificationmanager() != null) {
					MyNotification.getInstance().notificationCancel();
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
			  }
			}
		
		}

	}

}
