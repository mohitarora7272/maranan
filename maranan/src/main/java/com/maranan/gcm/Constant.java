package com.maranan.gcm;

public interface Constant {

	// used to share GCM regId with application server - using php app server

	// GCM server using java
	// static final String APP_SERVER_URL =
	// "http://192.168.1.17:8080/GCM-App-Server/GCMNotification?shareRegId=1";

	// Google Project Number
	String GOOGLE_PROJECT_ID = "900260982112";
	String MESSAGE_KEY = "alarm_alert";
	String MESSAGE_KEY2 = "radio_alert";
	String MESSAGE_KEY3 = "newsletter_alert";
	String MESSAGE_KEY4 = "youtube_alert";
	String MESSAGE_KEY5 = "live_broadcast";
	int NOTIFICATION_ID1 = 1001;
	int NOTIFICATION_ID2 = 2001;
	int NOTIFICATION_ID3 = 3001;
	int NOTIFICATION_ID4 = 4001;
	int NOTIFICATION_ID5 = 5001;
	int COMMON_PERMISSION_REQUEST_CODE = 100;
}
