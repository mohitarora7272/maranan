package com.maranan.restclient;

import com.maranan.pojo.YoutubePojoVideoId;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

public interface Api {
   
	@GET("/videos?key=AIzaSyCx9vEuKRcqS92nduiKgNi2-ucJwu77yZw&part=snippet,contentDetails,statistics,status")
	void downloadHomeData2(@Query("id") String videoID, Callback<YoutubePojoVideoId> callback);
    
    @GET("/add_gcm_user.php")
   	void downloadHomeData3(@Query("registration_id") String key, @Query("time_zone") String key2, @Query("device_key") String key3, Callback<Response> callback);
    
    @GET("/get_radio_status.php")
    void getRadioPrograms(Callback<Response> callback);
    
    @GET("/get_favourite.php")
   	void getFavourite(@Query("email") String key, Callback<Response> callback);
    
    @GET("/uniquedate.php")
    void getUniquedates(Callback<Response> callback);
    
    @GET("/get_newsletter_status.php")
    void getNewsLetters(Callback<Response> callback);
    
}