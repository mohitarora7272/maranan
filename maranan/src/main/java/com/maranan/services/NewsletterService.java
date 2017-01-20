package com.maranan.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.maranan.ApplicationSingleton;
import com.maranan.restclient.RestClientRadioPrograms;
import com.maranan.utils.GetterSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class NewsletterService extends Service {
	private static NewsletterService mContext;
	private Boolean isDateEnter = false;
	private Boolean isPDFStackFull = false;
	private Boolean isPDFStackNull = false;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	// DateService Instance
	public static NewsletterService getInstance() {
		return mContext;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			mContext = NewsletterService.this;
			getNewsLetters();

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("DateService", "Exception?? " + e.getMessage());
			stopSelf();
		}
	}
	
	// Get NewsLetter's At The Backgroud services...
	private void getNewsLetters() {
		try {
			RestClientRadioPrograms.get().getNewsLetters(new Callback<Response>() {

						@Override
						public void failure(RetrofitError arg0) {
							stopSelf();
						}

						@Override
						public void success(Response response, Response arg1) {
							ArrayList<GetterSetter> listNewsLettersUp = new ArrayList<GetterSetter>();
							ArrayList<GetterSetter> listNewsLetters = new ArrayList<GetterSetter>();
							ArrayList<String> listDates = new ArrayList<String>();
							TypedInput body = response.getBody();
							try {
								BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
								StringBuilder out = new StringBuilder();
								String newLine = System.getProperty("line.separator");
								String line;
								while ((line = reader.readLine()) != null) {
									out.append(line);
									out.append(newLine);
								}
								try {
									JSONObject json = new JSONObject(out.toString());
                                    if(json.getBoolean("success") == true){
                                    	JSONArray jArry = json.getJSONArray("value");
                                    	
                                    	// Set Count For NewsLetter...
                                    	((ApplicationSingleton) getApplicationContext()).dataContainer.setCount(jArry.length()-1);
    									
                                    	for (int i = 1; i < jArry.length(); i++) {
    										JSONObject jsonObj = jArry.getJSONObject(i);
    										GetterSetter geteset = new GetterSetter();
    										geteset.setId(jsonObj.getString("id"));
    										geteset.setTitle(jsonObj.getString("title"));
    										geteset.setImage(jsonObj.getString("image"));
    										geteset.setPdf(jsonObj.getString("pdf"));
    										geteset.setPdf_pages(jsonObj.getString("pdf_pages"));
    										geteset.setTime(jsonObj.getString("time"));
    										geteset.setDate(jsonObj.getString("date"));
    										geteset.setYear(jsonObj.getString("year"));
    										geteset.setPublish_status(jsonObj.getString("publish_status"));
    										geteset.setImage_thumb(jsonObj.getString("image_thumb"));
    										// Set Count On News Letter PDF's 
    										int count = ((ApplicationSingleton) getApplicationContext()).dataContainer.getCount();
    										geteset.setCountNew(count);
    										((ApplicationSingleton) getApplicationContext()).dataContainer.setCount(count-1);
    										
    										// Set Unique Date's 
    										if(!listDates.contains(jsonObj.getString("year"))) {
    											isDateEnter = true;
    											if (!jsonObj.getString("year").equals("")) {
    												listDates.add(jsonObj.getString("year"));
    											}
    										}
    										
    										listNewsLetters.add(geteset);
    									}
    									
    									if(jArry.length() > 0)
                                    	{
                                    		JSONObject jsonObj = jArry.getJSONObject(0);
    										GetterSetter geteset = new GetterSetter();
    										geteset.setId(jsonObj.getString("id"));
    										geteset.setTitle(jsonObj.getString("title"));
    										geteset.setImage(jsonObj.getString("image"));
    										geteset.setPdf(jsonObj.getString("pdf"));
    										geteset.setPdf_pages(jsonObj.getString("pdf_pages"));
    										geteset.setTime(jsonObj.getString("time"));
    										geteset.setDate(jsonObj.getString("date"));
    										geteset.setYear(jsonObj.getString("year"));
    										geteset.setPublish_status(jsonObj.getString("publish_status"));
    										geteset.setImage_thumb(jsonObj.getString("image_thumb"));
    										
    										if (!isDateEnter) {
        										// Set Unique Date's
    											if (!jsonObj.getString("year").equals("")) {
    												listDates.add(0, jsonObj.getString("year"));
    											}
											}
    										
    										
    										listNewsLettersUp.add(geteset);
    										((ApplicationSingleton) getApplicationContext()).dataContainer.setList_NewsletterUp(listNewsLettersUp);
                                    	}
    									
    									((ApplicationSingleton) getApplicationContext()).dataContainer.setList_dates(listDates);

    									if (((ApplicationSingleton) getApplicationContext()).dataContainer.getList_dates().size() > 0) {
    										HashMap<String, ArrayList<GetterSetter>> hash = new HashMap<String, ArrayList<GetterSetter>>();

    										for (int i = 0; i < ((ApplicationSingleton) getApplicationContext()).dataContainer.getList_dates().size(); i++) {

    											ArrayList<GetterSetter> list = new ArrayList<GetterSetter>();
    											
    											for (int j = 0; j < listNewsLetters.size(); j++) {
    												
    												if (((ApplicationSingleton) getApplicationContext()).dataContainer.getList_dates().get(i).equals(listNewsLetters.get(j).getYear())) {
    													
    													GetterSetter geteset = new GetterSetter();
    													geteset.setId(listNewsLetters.get(j).getId());
    													geteset.setTitle(listNewsLetters.get(j).getTitle());
    													geteset.setImage(listNewsLetters.get(j).getImage());
    													geteset.setPdf(listNewsLetters.get(j).getPdf());
    													geteset.setPdf_pages(listNewsLetters.get(j).getPdf_pages());
    													geteset.setTime(listNewsLetters.get(j).getTime());
    													geteset.setDate(listNewsLetters.get(j).getDate());
    													geteset.setYear(listNewsLetters.get(j).getYear());
    													geteset.setPublish_status(listNewsLetters.get(j).getPublish_status());
    													geteset.setImage_thumb(listNewsLetters.get(j).getImage_thumb());
    													geteset.setCountNew(listNewsLetters.get(j).getCountNew());
    													list.add(geteset);
    													hash.put(((ApplicationSingleton) getApplicationContext()).dataContainer.getList_dates().get(i), list);
    													((ApplicationSingleton) getApplicationContext()).dataContainer.setHashListNewsLetter(hash);
    												}
    											}
    										}
    									}
    									
    									isPDFStackFull = true;
										//Log.e("DataContainer", "List_datesSize?? " +((ApplicationSingleton) getApplicationContext()).dataContainer.getList_dates().size());
										//Log.e("HashSize", "HashSize?? " +((ApplicationSingleton) getApplicationContext()).dataContainer.getHashListNewsLetter().size());
										stopSelf();
										
									}else{
										isPDFStackNull = true;
										stopSelf();
									}
								} catch (JSONException e) {
									e.printStackTrace();
									isPDFStackNull = true;
									stopSelf();
								}

							} catch (Exception e) {
								isPDFStackNull = true;
								Log.e("DateService", "Exception?? " + e.getMessage());
								stopSelf();
							}
						}
					});

		} catch (Exception e) {
			isPDFStackNull = true;
			Log.e("DateService", "Exception?? " + e.getMessage());
			stopSelf();
		}
		
	}
	
	// Check Pdf Stack Full or Not
	public Boolean getIsPDfStackFull() {
		return isPDFStackFull;
	}

	// Check Pdf Stack NUll
	public Boolean getIsPDfStackNull() {
		return isPDFStackNull;
	}
	
}
