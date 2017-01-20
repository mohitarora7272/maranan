package com.maranan.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.maranan.ApplicationSingleton;

public class CallReceiver extends BroadcastReceiver {
    private static ThePhoneStateListener phoneStateListener;
    private Context contexts;
    private boolean isPlaying = false;

	@Override
	public void onReceive(Context context, Intent intent) {

		this.contexts = context;
		isPlaying = false;
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (phoneStateListener == null) {
			phoneStateListener = new ThePhoneStateListener(context);
			telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
	public class ThePhoneStateListener extends PhoneStateListener {
	    Context context;
	    public ThePhoneStateListener(Context context) {
	        this.context = context;
	    }
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	    	super.onCallStateChanged(state, incomingNumber);
			try {
				switch (state) {
				
				case TelephonyManager.CALL_STATE_RINGING: {
					
					// PAUSE
					if (((ApplicationSingleton) contexts.getApplicationContext()).player != null) {
					if (((ApplicationSingleton) contexts.getApplicationContext()).player.isPlaying()) {
						((ApplicationSingleton) contexts.getApplicationContext()).player.pause();
						isPlaying = true;
					  }
					}
					
				}
				break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					break;
				
				
				case TelephonyManager.CALL_STATE_IDLE: {
					// PLAY
					if (((ApplicationSingleton) contexts.getApplicationContext()).player != null) {
						if (isPlaying == true) {
							if (!((ApplicationSingleton) contexts.getApplicationContext()).player.isPlaying()) {
								((ApplicationSingleton) contexts.getApplicationContext()).player.start();
								isPlaying = false;
							}
						}
					}
				}
					break;
				
				}
			} catch (Exception ex) {

			}
		}
	}
}
