package com.maranan.adapters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maranan.ApplicationSingleton;
import com.maranan.PrayerScreen;
import com.maranan.R;
import com.maranan.utils.Config;
import com.maranan.utils.DateTest;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.Utilities;

public class PrayerAdapter extends BaseAdapter {
	private static PrayerAdapter mContext;
	private Context ctx;
	private ArrayList<GetterSetter> list;
	private SharedPreferences pref;
	private ViewHolder holder;
	
	// PrayerAdapter Instance
	public static PrayerAdapter getInstance() {
		return mContext;
	}

	public PrayerAdapter(Context ctxx, ArrayList<GetterSetter> list) {
		mContext = PrayerAdapter.this;
		ctx = ctxx;
		this.list = list;
		setArrayList(list);
		pref = ctx.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(ctx).inflate(R.layout.prayer_items, null, false);
			holder.tv_profile_text_left = (TextView)convertView.findViewById(R.id.tv_profile_text_left);
			holder.tv_text_name_right = (TextView)convertView.findViewById(R.id.tv_text_name_right); 
			holder.tv_count_right = (TextView)convertView.findViewById(R.id.tv_count_right); 
			holder.tv_text_name_bottom = (TextView)convertView.findViewById(R.id.tv_text_name_bottom);
			holder.btn_check_uncheck_prayer = (Button)convertView.findViewById(R.id.btn_check_uncheck_prayer); 
			holder.tv_devotion = (TextView)convertView.findViewById(R.id.tv_devotion); 
			holder.img_favourite = (ImageView)convertView.findViewById(R.id.img_favourite);
			convertView.setTag(holder);
		
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.btn_check_uncheck_prayer.setTag(position);
		holder.tv_profile_text_left.setTag(position);
		holder.tv_text_name_right.setTag(position);
		holder.tv_count_right.setTag(position);
		holder.tv_text_name_bottom.setTag(position);
		
		String[] separated = list.get(position).getEmail().split("@");
		holder.tv_profile_text_left.setText(separated[0]);
		holder.tv_count_right.setText(String.valueOf(list.get(position).getCount()));
		
		new DateTest(ctx, Utilities.getCurrentDateDiffFormat(), list.get(position).getDate().trim(), list, position, "PrayerActivity");

		if(pref.getString("email", null)!=null && list.get(position).getEmail()!=null)
		if (pref.getString("email", null).equals(list.get(position).getEmail())) {
			holder.img_favourite.setVisibility(View.GONE);
			holder.tv_devotion.setVisibility(View.VISIBLE);
			list.get(position).setImagefavResource(0);
			list.get(position).setYourfav(ctx.getString(R.string.my_devotion));
			
		}else{
			holder.img_favourite.setVisibility(View.VISIBLE);
			holder.tv_devotion.setVisibility(View.VISIBLE);
		}
		
		 holder.btn_check_uncheck_prayer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (list.get(position).getImageResource() == R.drawable.checked_green) {
					
					list.get(position).setColorResource(R.drawable.red_back_new);
					list.get(position).setImageResource(R.drawable.unchecked_green);
					
					
					PrayerScreen.getInstance().getWhiteTextView().setText(String.valueOf(PrayerScreen.getInstance().getWhiteCount() - 1));
					PrayerScreen.getInstance().setWhiteCount(PrayerScreen.getInstance().getWhiteCount() - 1);
					PrayerScreen.getInstance().getTextViewTop().setText(String.valueOf(Integer.parseInt(PrayerScreen.getInstance().getTextViewTop().getText().toString()) - list.get(position).getCount()));
					
							try {
								// Remove email for uncheck favourite list...
								if (PrayerScreen.getInstance().getfavArrayList().size() > 0) {
									for (int i = 0; i < PrayerScreen.getInstance().getfavArrayList().size(); i++) {
										if (list.get(position).getEmail().equals(PrayerScreen.getInstance().getfavArrayList().get(i))) {
											PrayerScreen.getInstance().getfavArrayList().remove(i);
										}
									}
									
									Utilities.insertArrayListPref(ctx, PrayerScreen.getInstance().getfavArrayList());
									PrayerScreen.getInstance().setfavArrayList();
								}
								
								// Set Favourite Tag According to the selection on the list...
		    					if(list.get(position).getTag().equals("")){
		    						
		    						
		    						list.get(position).setImagefavResource(R.drawable.fav_green);
		    						list.get(position).setYourfav(ctx.getString(R.string.your_favorites_list));
		    					
		    					}else if(list.get(position).getTag().equals("single")){
		    						
		    					
		    						list.get(position).setImagefavResource(0);
		    						list.get(position).setYourfav("");
		    					
		    					}else if(list.get(position).getTag().equals("other")){
		    						
		    						
		    						list.get(position).setImagefavResource(R.drawable.fav_both);
		    						list.get(position).setYourfav(ctx.getString(R.string.you_are_each_other_favourite));
		    					
		    					}else if(list.get(position).getTag().equals("both")){
		    						
		    						
		    						list.get(position).setImagefavResource(R.drawable.fav_blue);
		    						list.get(position).setYourfav(ctx.getString(R.string.added_you_to_fav));
		    					}
								
								 // Set Blue, Red, Green Count At the Top Of the Tabs... 
			                    for (int j = 0; j < ((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().size(); j++) {
									if (list.get(position).getEmail().equals(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().get(j).getEmail())) {
										
										if (((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().get(j).getNature().equals(ctx.getResources().getString(R.string.success_hebrew))) {
											
											PrayerScreen.getInstance().getBlueTextView().setText(String.valueOf(PrayerScreen.getInstance().getBlueCount() - 1));
											PrayerScreen.getInstance().setBlueCount(PrayerScreen.getInstance().getBlueCount() - 1);
										
										}else if(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().get(j).getNature().equals(ctx.getResources().getString(R.string.medicine_hebrew))){
											
											PrayerScreen.getInstance().getGreenTextView().setText(String.valueOf(PrayerScreen.getInstance().getGreenCount() - 1));
											PrayerScreen.getInstance().setGreenCount(PrayerScreen.getInstance().getGreenCount() - 1);
										
										}else if(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().get(j).getNature().equals(ctx.getResources().getString(R.string.InMemoryof_hebrew))){
											
											PrayerScreen.getInstance().getRedTextView().setText(String.valueOf(PrayerScreen.getInstance().getRedCount() - 1));
											PrayerScreen.getInstance().setRedCount(PrayerScreen.getInstance().getRedCount() - 1);
										
										}
									}
								}

							} catch (Exception e) {
								Log.e("Exception??", "PrayerAdpterup?? " + e.getMessage());
							}
							
					
					notifyDataSetChanged();
					
					new Favourite().execute(pref.getString("email", null),  list.get(position).getEmail(), "false");
				
				}else{
					
				
					list.get(position).setColorResource(R.drawable.blue_back_new);
					list.get(position).setImageResource(R.drawable.checked_green);
					
					PrayerScreen.getInstance().getWhiteTextView().setText(String.valueOf(PrayerScreen.getInstance().getWhiteCount() + 1));
					PrayerScreen.getInstance().setWhiteCount(PrayerScreen.getInstance().getWhiteCount() + 1);
					PrayerScreen.getInstance().getTextViewTop().setText(String.valueOf(Integer.parseInt(PrayerScreen.getInstance().getTextViewTop().getText().toString()) + list.get(position).getCount()));
                    
					try {
                    	// Set email for check user select as a favourite list...
    					if (PrayerScreen.getInstance().getfavArrayList().size() > 0) {
    						
    						PrayerScreen.getInstance().getfavArrayList().add(PrayerScreen.getInstance().getfavArrayList().size()-1, list.get(position).getEmail());
    						Utilities.insertArrayListPref(ctx, PrayerScreen.getInstance().getfavArrayList());
    					
    					}else{
    						
    						PrayerScreen.getInstance().getfavArrayList().add(PrayerScreen.getInstance().getfavArrayList().size(), list.get(position).getEmail());
    						Utilities.insertArrayListPref(ctx, PrayerScreen.getInstance().getfavArrayList());
    						PrayerScreen.getInstance().setfavArrayList();
    					}
    					
    					// Set Favourite Tag According to the selection on the list...
    					if(list.get(position).getTag().equals("")){
    						
    						
    						list.get(position).setImagefavResource(R.drawable.fav_green);
    						list.get(position).setYourfav(ctx.getString(R.string.your_favorites_list));
    					
    					}else if(list.get(position).getTag().equals("single")){
    						
    						
    						list.get(position).setImagefavResource(0);
    						list.get(position).setYourfav("");
    					
    					}else if(list.get(position).getTag().equals("other")){
    						
    						
    						list.get(position).setImagefavResource(R.drawable.fav_both);
    						list.get(position).setYourfav(ctx.getString(R.string.you_are_each_other_favourite));
    					
    					}else if(list.get(position).getTag().equals("both")){
    						
    						
    						list.get(position).setImagefavResource(R.drawable.fav_blue);
    						list.get(position).setYourfav(ctx.getString(R.string.added_you_to_fav));
    					
    					}
					
                    // Set Blue, Red, Green Count At the Top Of the Tabs... 
                    for (int j = 0; j < ((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().size(); j++) {
						if (list.get(position).getEmail().equals(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().get(j).getEmail())) {
							
							if (((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().get(j).getNature().equals(ctx.getResources().getString(R.string.success_hebrew))) {
								
								PrayerScreen.getInstance().getBlueTextView().setText(String.valueOf(PrayerScreen.getInstance().getBlueCount() + 1));
								PrayerScreen.getInstance().setBlueCount(PrayerScreen.getInstance().getBlueCount() + 1);
							
							}else if(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().get(j).getNature().equals(ctx.getResources().getString(R.string.medicine_hebrew))){
								
								PrayerScreen.getInstance().getGreenTextView().setText(String.valueOf(PrayerScreen.getInstance().getGreenCount() + 1));
								PrayerScreen.getInstance().setGreenCount(PrayerScreen.getInstance().getGreenCount() + 1);
							
							}else if(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_AcceptedDedication().get(j).getNature().equals(ctx.getResources().getString(R.string.InMemoryof_hebrew))){
								
								PrayerScreen.getInstance().getRedTextView().setText(String.valueOf(PrayerScreen.getInstance().getRedCount() + 1));
								PrayerScreen.getInstance().setRedCount(PrayerScreen.getInstance().getRedCount() + 1);
							
							}
						}
					}
                    
                    } catch (Exception e) {
						Log.e("Exception??", "PrayerAdpter?? "+e.getMessage());
					}
					
					notifyDataSetChanged();
					new Favourite().execute(pref.getString("email", null),  list.get(position).getEmail(), "true");
				}
			}
		});
		
		holder.tv_count_right.setBackgroundResource(list.get(position).getColorResource());
		holder.btn_check_uncheck_prayer.setBackgroundResource(list.get(position).getImageResource());
		holder.img_favourite.setBackgroundResource(list.get(position).getImagefavResource());
		holder.tv_devotion.setText(list.get(position).getYourfav());
		return convertView;
	}

	class ViewHolder {
		TextView tv_profile_text_left, tv_text_name_right, tv_count_right,
				 tv_text_name_bottom, tv_devotion;
		ImageView img_favourite;
		Button btn_check_uncheck_prayer;
	}
	
	// Get TextView To Show Date Time Left...
	public TextView getDateTimeTv() {
		return holder.tv_text_name_bottom;
	}
	
	// Get Array List...
	public ArrayList<GetterSetter> getArrayList() {
		return list;
	}
	
	// Get Array List...
	public void setArrayList(ArrayList<GetterSetter> list) {
		this.list = list;
	}
	
	public void GetFavourites(String email, String fav_email, String status){
		new Favourite().execute(email, fav_email, status);
	}
	
	// Declare UpdateStatus Dedication...
	class Favourite extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			PrayerScreen.getInstance().getProgressBar().setVisibility(View.VISIBLE);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jObj = null;
			HttpParams params2 = new BasicHttpParams();
			params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			DefaultHttpClient mHttpClient = new DefaultHttpClient(params2);
			String url = Config.ROOT_SERVER_CLIENT + Config.FAVOURITE;

			HttpPost httppost = new HttpPost(url);

			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			try {
				multipartEntity.addPart("email", new StringBody(params[0]));
				multipartEntity.addPart("fav_email", new StringBody(params[1]));
				multipartEntity.addPart("status", new StringBody(params[2]));
				httppost.setEntity(multipartEntity);
				HttpResponse response = mHttpClient.execute(httppost);
				HttpEntity r_entity = response.getEntity();
				String strResponse = EntityUtils.toString(r_entity);
				jObj = new JSONObject(strResponse);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			PrayerScreen.getInstance().getProgressBar().setVisibility(View.GONE);
			try {
				if(result.getString("success").equals("true")){
					PrayerScreen.getInstance().GetFavouriteEmails();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
		}
	}
}
