package com.maranan.utils;

public class Config {
	// Google Console APIs developer key
    // Replace this key with your's
	// UC1EhBfEKFkpba74cRDBEXxA (Client Channel ID)
	// PLrLp80RyVyk1ErFf5dVNgtXT3UD1iOr6U (CLient PlayLit ID)
	// UCyN_rob3oiFxp1qtd3r7nCQ (My Channel ID)
	// Client YouTube Play List Link...
	//https://www.youtube.com/channel/UC1EhBfEKFkpba74cRDBEXxA/playlists
	
	// You tube developer link to upload video...
	//https://developers.google.com/youtube/v3/code_samples/java#post_a_channel_bulletin
    
	public static final String DEVELOPER_KEY = "AIzaSyAhV9mM16u8tcBlu5THHNsKuctLkinkHMU";
    public static final String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search?";
    public static final String PLAY_LIST_URL = "https://www.googleapis.com/youtube/v3/playlistItems?";
    public static final String PLAY_LIST_ID_URL = "https://www.googleapis.com/youtube/v3/playlists?";
    public static final String VIEW_COUNT_URL = "https://www.googleapis.com/youtube/v3/videos?";
    public static final String KEY = "key=";
    public static final String VIDEO_ID = "id=";
    public static final String SERVER_KEY = KEY+"AIzaSyCx9vEuKRcqS92nduiKgNi2-ucJwu77yZw";
    public static final String CHANNEL_KEY = "&channelId=";
    public static final String CHANNEL_ID = CHANNEL_KEY+"UC1EhBfEKFkpba74cRDBEXxA";
    public static final String PLAY_LIST_KEY = "&playlistId=";
    public static final String PART = "&part=snippet,id,contentDetails";
    public static final String PART_ADD = "&part=snippet,contentDetails,statistics,status";
    public static final String ORDER = "&order=date";
    public static final String START_INDEX_ONE = "&start-index=1";
    public static final String START_INDEX_51 = "&start-index=51";
    public static final String NEXT_PAGE_TOKEN = "&pageToken=";
    public static final String MAX_RESULT = "&maxResults=50";
    public static final String PLAYLIST_ID = PLAY_LIST_KEY+"PLrLp80RyVyk1ErFf5dVNgtXT3UD1iOr6U";
    public static final String YOUTUBE_URL_SEARCH = SEARCH_URL+ SERVER_KEY + CHANNEL_ID + PART + ORDER + START_INDEX_ONE + MAX_RESULT;
    public static final String YOUTUBE_URL_PLAYLIST = PLAY_LIST_URL + SERVER_KEY + PART + START_INDEX_ONE + MAX_RESULT + ORDER;
    public static final String YOUTUBE_URL_PLAYLIST_UPTO50 = PLAY_LIST_URL + SERVER_KEY + PART + MAX_RESULT + ORDER;

    // GETTING PLAY LIST OF YOU TUBE FROM VERSION 2.1
    public static final String GET_PLAY_LIST_V2 = "https://gdata.youtube.com/feeds/api/playlists/"+PLAYLIST_ID+"?v=2.1&alt=jsonc";
     
    // YouTube video id
    public static final String YOUTUBE_VIDEO_CODE = "9qGNSnkLbfk";
    
    // Getting play list id from this API  
    public static final String GET_PLAY_LIST_ID = PLAY_LIST_ID_URL + SERVER_KEY + ORDER + PART + CHANNEL_ID;
    public static final String GET_VIDEO_VIEW_COUNT = VIEW_COUNT_URL + SERVER_KEY + PART_ADD + VIDEO_ID;
    public static String ROOT_YOUTUBE = "https://www.googleapis.com/youtube/v3";
    public static String ROOT_SERVER_CLIENT_OLD = "http://173.254.28.169/~maranant";
  	public static String ROOT_SERVER_CLIENT = "http://50.87.25.184/";
    public static String ROOT_DEMO_CLIENT ="http://demo.inextsolutions.com/maranan";
    public static String ROOT_DEMO = "http://shofarj.com/marananapp";
    public static String REGISTER_API = "/registration.php?";
    public static String LOGIN_API = "/login.php?";
    public static String FORGOT_PASSWORD = "/forgot_password.php?";
    public static String DEDICATE = "/dedicate.php?";
    public static String GETDEDICATE = "/getdedication.php?";
    public static String DELETEDEDICATION = "/deletededication.php?";
    public static String PAYMENT_STATUS = "/update_payment_status.php?";
    public static String ACEPTED_DEDICATION = "/accepted_dedication.php";
    public static String UPDATE_STATUS = "/update_status.php?";
    public static String GET_LECTURE = "/lectures.php";
    public static String GET_RADIO_LECTURE = "/get_radio_status.php";
    public static String SEND_GCM_ID = "add_gcm_user.php?registration_id=";
    public static String FAVOURITE = "/favourite.php?";
    public static String GET_FAVOURITE = "/get_favourite.php?";
    public static String GET_UNIQUEDATE = "/uniquedate.php?";
    public static String GET_PLAYLIST = "get_playlist_id.php";
    public static String GET_YOUTUBE_CHANNEL = "/get_youtube_details.php";
    
}
