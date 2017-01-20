package com.maranan.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maranan.R;
import com.maranan.utils.DateTest;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.Utilities;

public class VideoListAdapter extends BaseAdapter {
	private static VideoListAdapter mContext;
	private Context ctx;
	private ArrayList<GetterSetter> listAcceptDedicationss;
	private ArrayList<String> list_count;
	private TextView tv_time_video;
	private ArrayList<String> list_favEmail;
	private String activityName;
	private ImageButton img_man_check_uncheck;
	private ImageButton img_map_check_uncheck;
	private SharedPreferences pref;
	
	// PrayerAdapter Instance
	public static VideoListAdapter getInstance() {
		return mContext;
	}

	public VideoListAdapter(Context ctx, ArrayList<GetterSetter> listAcceptDedicationss, ArrayList<String> list_count, String activityName) {
		mContext = VideoListAdapter.this;
		this.ctx = ctx;
		this.listAcceptDedicationss = listAcceptDedicationss;
		this.list_count = list_count;
		list_favEmail = new ArrayList<String>();
		list_favEmail = Utilities.getfavArrayListPref(ctx);
		this.activityName = activityName;
		pref = ctx.getSharedPreferences("RegisterPref", Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return listAcceptDedicationss.size();
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
			
			LayoutInflater mInflater = (LayoutInflater) ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			
			if (listAcceptDedicationss.get(position).getNature().equals(ctx.getResources().getString(R.string.success_hebrew)))
			{
				convertView = mInflater.inflate(R.layout.activity_video_list_items_blue, null);
				TextView tv_success_vid = (TextView) convertView.findViewById(R.id.tv_success_vid);
				TextView tv_name_vid = (TextView) convertView.findViewById(R.id.tv_name_vid);
				tv_time_video = (TextView) convertView.findViewById(R.id.tv_time_video);
				TextView tv_data = (TextView) convertView.findViewById(R.id.tv_data);
				TextView tv_dedications = (TextView) convertView.findViewById(R.id.tv_dedication_text);
				tv_success_vid.setText(listAcceptDedicationss.get(position).getNature());
				//tv_time_video.setText(listAcceptDedicationss.get(position).getTime());
				ImageView img_check_small = (ImageView) convertView.findViewById(R.id.img_check_small);
				img_check_small.setVisibility(View.GONE);
				
				
				
				if (activityName.equals("PrayerScreen")) {
					RelativeLayout relative_videos = (RelativeLayout) convertView.findViewById(R.id.relative_videos);
					relative_videos.setVisibility(View.GONE);
					tv_success_vid.setVisibility(View.GONE);
					tv_name_vid.setPadding(0, 20, 10, 0);
					tv_name_vid.setTextSize(18f);
				
				}else if(activityName.equals("PrayerScreenPopUp")){
					
					if(listAcceptDedicationss.get(position).getEmail().equals(pref.getString("email", null))){
						RelativeLayout relative_popup = (RelativeLayout) convertView.findViewById(R.id.relative_popup);
						relative_popup.setVisibility(View.VISIBLE);
						tv_dedications.setVisibility(View.GONE);
						 img_man_check_uncheck = (ImageButton) convertView.findViewById(R.id.img_man_check_uncheck);
						 img_map_check_uncheck = (ImageButton) convertView.findViewById(R.id.img_map_check_uncheck);
					}else{
						RelativeLayout relative_popup = (RelativeLayout) convertView.findViewById(R.id.relative_popup);
						relative_popup.setVisibility(View.GONE);
						tv_dedications.setVisibility(View.VISIBLE);
					}
					
				}
				
				new DateTest(ctx, Utilities.getCurrentDateDiffFormat(), listAcceptDedicationss.get(position).getDate().trim(), listAcceptDedicationss, position, "VideoListActivity");
				
				String[] separated = listAcceptDedicationss.get(position).getEmail().split("@");
				tv_data.setText(separated[0]);
				if (list_count.size() >= 10000) {
					char first = list_count.get(position).charAt(0);
					tv_dedications.setText(String.valueOf(first)+" "+"...");
				
				}else{
					tv_dedications.setText(list_count.get(position));
				}
				
				if (list_favEmail.size() > 0) {
					for (int i = 0; i < list_favEmail.size(); i++) {
						if (list_favEmail.get(i).equals(listAcceptDedicationss.get(position).getEmail())) {
							img_check_small.setVisibility(View.VISIBLE);
						}
					}
				}
				
				
				if (listAcceptDedicationss.get(position).getName_Optional().equals("")){
				    
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getName().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getSex().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getThere_Is().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getBlessing().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")));
				
				}else{
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				}
				
			
			}else if(listAcceptDedicationss.get(position).getNature().equals(ctx.getResources().getString(R.string.medicine_hebrew))){
				
				convertView = mInflater.inflate(R.layout.activity_video_list_items_green, null);
				TextView tv_success_vid = (TextView) convertView.findViewById(R.id.tv_success_vid);
				TextView tv_name_vid = (TextView) convertView.findViewById(R.id.tv_name_vid);
				tv_time_video = (TextView) convertView.findViewById(R.id.tv_time_video);
				TextView tv_data = (TextView) convertView.findViewById(R.id.tv_data);
				TextView tv_dedications = (TextView) convertView.findViewById(R.id.tv_dedication_text);
				
				tv_success_vid.setText(listAcceptDedicationss.get(position).getNature());
				//tv_time_video.setText(listAcceptDedicationss.get(position).getTime());
				ImageView img_check_small = (ImageView) convertView.findViewById(R.id.img_check_small);
				img_check_small.setVisibility(View.GONE);
			
				if (activityName.equals("PrayerScreen")) {
					RelativeLayout relative_videos = (RelativeLayout) convertView.findViewById(R.id.relative_videos);
					relative_videos.setVisibility(View.GONE);
					tv_success_vid.setVisibility(View.GONE);
					tv_name_vid.setPadding(0, 20, 10, 0);
					tv_name_vid.setTextSize(18f);
				
				}else if(activityName.equals("PrayerScreenPopUp")){
					if(listAcceptDedicationss.get(position).getEmail().equals(pref.getString("email", null))){
						RelativeLayout relative_popup = (RelativeLayout) convertView.findViewById(R.id.relative_popup);
						relative_popup.setVisibility(View.VISIBLE);
						tv_dedications.setVisibility(View.GONE);
						 img_man_check_uncheck = (ImageButton) convertView.findViewById(R.id.img_man_check_uncheck);
						 img_map_check_uncheck = (ImageButton) convertView.findViewById(R.id.img_map_check_uncheck);
					}else{
						RelativeLayout relative_popup = (RelativeLayout) convertView.findViewById(R.id.relative_popup);
						relative_popup.setVisibility(View.GONE);
						tv_dedications.setVisibility(View.VISIBLE);
					}
				}
				
				new DateTest(ctx, Utilities.getCurrentDateDiffFormat(), listAcceptDedicationss.get(position).getDate().trim(), listAcceptDedicationss, position, "VideoListActivity");
				
				String[] separated = listAcceptDedicationss.get(position).getEmail().split("@");
				tv_data.setText(separated[0]);
				if (list_count.size() >= 10000) {
					char first = list_count.get(position).charAt(0);
					tv_dedications.setText(String.valueOf(first)+" "+"...");
				
				}else{
					tv_dedications.setText(list_count.get(position));
				}
				
				if (list_favEmail.size() > 0) {
					for (int i = 0; i < list_favEmail.size(); i++) {
						if (list_favEmail.get(i).equals(listAcceptDedicationss.get(position).getEmail())) {
							img_check_small.setVisibility(View.VISIBLE);
						}
					}
				}
				
				if (listAcceptDedicationss.get(position).getName_Optional().equals("")){
				    
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getName().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getSex().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getThere_Is().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getBlessing().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")));
				
				}else{
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				}
			
			}else if(listAcceptDedicationss.get(position).getNature().equals(ctx.getResources().getString(R.string.InMemoryof_hebrew))){
				
				convertView = mInflater.inflate(R.layout.activity_video_list_items_red, null);
				TextView tv_success_vid = (TextView) convertView.findViewById(R.id.tv_success_vid);
				TextView tv_name_vid = (TextView) convertView.findViewById(R.id.tv_name_vid);
				tv_time_video = (TextView) convertView.findViewById(R.id.tv_time_video);
				TextView tv_data = (TextView) convertView.findViewById(R.id.tv_data);
				TextView tv_dedications = (TextView) convertView.findViewById(R.id.tv_dedication_text);
				
				tv_success_vid.setText(listAcceptDedicationss.get(position).getNature());
				//tv_time_video.setText(listAcceptDedicationss.get(position).getTime());
				
				ImageView img_check_small = (ImageView) convertView.findViewById(R.id.img_check_small);
				img_check_small.setVisibility(View.GONE);
				
				
				
				if (activityName.equals("PrayerScreen")) {
					RelativeLayout relative_videos = (RelativeLayout) convertView.findViewById(R.id.relative_videos);
					relative_videos.setVisibility(View.GONE);
					tv_success_vid.setVisibility(View.GONE);
					tv_name_vid.setPadding(0, 20, 10, 0);
					tv_name_vid.setTextSize(18f);
				
				}else if(activityName.equals("PrayerScreenPopUp")){
					if(listAcceptDedicationss.get(position).getEmail().equals(pref.getString("email", null))){
						RelativeLayout relative_popup = (RelativeLayout) convertView.findViewById(R.id.relative_popup);
						relative_popup.setVisibility(View.VISIBLE);
						tv_dedications.setVisibility(View.GONE);
						 img_man_check_uncheck = (ImageButton) convertView.findViewById(R.id.img_man_check_uncheck);
						 img_map_check_uncheck = (ImageButton) convertView.findViewById(R.id.img_map_check_uncheck);
					}else{
						RelativeLayout relative_popup = (RelativeLayout) convertView.findViewById(R.id.relative_popup);
						relative_popup.setVisibility(View.GONE);
						tv_dedications.setVisibility(View.VISIBLE);
					}
				}
				
				new DateTest(ctx, Utilities.getCurrentDateDiffFormat(), listAcceptDedicationss.get(position).getDate().trim(), listAcceptDedicationss, position, "VideoListActivity");
				
				String[] separated = listAcceptDedicationss.get(position).getEmail().split("@");
				tv_data.setText(separated[0]);
				if (list_count.size() >= 10000) {
					char first = list_count.get(position).charAt(0);
					tv_dedications.setText(String.valueOf(first)+" "+"...");
				
				}else{
					tv_dedications.setText(list_count.get(position));
				}
				
				if (list_favEmail.size() > 0) {
					for (int i = 0; i < list_favEmail.size(); i++) {
						if (list_favEmail.get(i).equals(listAcceptDedicationss.get(position).getEmail())) {
							img_check_small.setVisibility(View.VISIBLE);
						}
					}
				}
				
				if (listAcceptDedicationss.get(position).getName_Optional().equals("")){
				    
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getName().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getSex().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getThere_Is().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getBlessing().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")));
				
				}else{
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				}
			
			}else{
				convertView = mInflater.inflate(R.layout.activity_video_list_items_blue, null);
				TextView tv_success_vid = (TextView) convertView.findViewById(R.id.tv_success_vid);
				TextView tv_name_vid = (TextView) convertView.findViewById(R.id.tv_name_vid);
				tv_time_video = (TextView) convertView.findViewById(R.id.tv_time_video);
				TextView tv_data = (TextView) convertView.findViewById(R.id.tv_data);
				TextView tv_dedications = (TextView) convertView.findViewById(R.id.tv_dedication_text);
				
				tv_success_vid.setText(listAcceptDedicationss.get(position).getNature());
				//tv_time_video.setText(listAcceptDedicationss.get(position).getTime());
				
				ImageView img_check_small = (ImageView) convertView.findViewById(R.id.img_check_small);
				img_check_small.setVisibility(View.GONE);
				
				if (activityName.equals("PrayerScreen")) {
					RelativeLayout relative_videos = (RelativeLayout) convertView.findViewById(R.id.relative_videos);
					relative_videos.setVisibility(View.GONE);
					tv_success_vid.setVisibility(View.GONE);
					tv_name_vid.setPadding(0, 20, 10, 0);
					tv_name_vid.setTextSize(18f);
				
				}else if(activityName.equals("PrayerScreenPopUp")){
					if(listAcceptDedicationss.get(position).getEmail().equals(pref.getString("email", null))){
						RelativeLayout relative_popup = (RelativeLayout) convertView.findViewById(R.id.relative_popup);
						relative_popup.setVisibility(View.VISIBLE);
						tv_dedications.setVisibility(View.GONE);
						 img_man_check_uncheck = (ImageButton) convertView.findViewById(R.id.img_man_check_uncheck);
						 img_map_check_uncheck = (ImageButton) convertView.findViewById(R.id.img_map_check_uncheck);
					}else{
						RelativeLayout relative_popup = (RelativeLayout) convertView.findViewById(R.id.relative_popup);
						relative_popup.setVisibility(View.GONE);
						tv_dedications.setVisibility(View.VISIBLE);
					}
				}
				
				new DateTest(ctx, Utilities.getCurrentDateDiffFormat(), listAcceptDedicationss.get(position).getDate().trim(), listAcceptDedicationss, position, "VideoListActivity");
				
				String[] separated = listAcceptDedicationss.get(position).getEmail().split("@");
				tv_data.setText(separated[0]);
				if (list_count.size() >= 10000) {
					char first = list_count.get(position).charAt(0);
					tv_dedications.setText(String.valueOf(first)+" "+"...");
				
				}else{
					tv_dedications.setText(list_count.get(position));
				}
				
				if (list_favEmail.size() > 0) {
					for (int i = 0; i < list_favEmail.size(); i++) {
						if (list_favEmail.get(i).equals(listAcceptDedicationss.get(position).getEmail())) {
							img_check_small.setVisibility(View.VISIBLE);
						}
					}
				}
				
				if (listAcceptDedicationss.get(position).getName_Optional().equals("")){
				    
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getName().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getSex().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getThere_Is().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				
				}else if(listAcceptDedicationss.get(position).getBlessing().equals("")){
					
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")));
				
				}else{
					tv_name_vid.setText(Utilities.decodeImoString(listAcceptDedicationss.get(position).getName().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getSex().trim()+" "
							+ listAcceptDedicationss.get(position).getThere_Is().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getName_Optional().trim().replace("*", "'")+" "
							+ listAcceptDedicationss.get(position).getBlessing().trim()));
				}
			}
			
			
		if (activityName.equals("PrayerScreenPopUp") && listAcceptDedicationss.get(position).getEmail().equals(pref.getString("email", null))) {
			img_man_check_uncheck.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listAcceptDedicationss.get(position)
							.getImageResourceMan() == R.drawable.gray_man) {

						listAcceptDedicationss.get(position)
								.setImageResourceMan(R.drawable.blue_man);

					} else {
						listAcceptDedicationss.get(position)
								.setImageResourceMan(R.drawable.gray_man);
					}

					notifyDataSetChanged();
				}
			});

			img_map_check_uncheck.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listAcceptDedicationss.get(position)
							.getImageResourceMap() == R.drawable.gray_map) {

						listAcceptDedicationss.get(position)
								.setImageResourceMap(R.drawable.blue_map);

					} else {
						listAcceptDedicationss.get(position)
								.setImageResourceMap(R.drawable.gray_map);
					}

					notifyDataSetChanged();
				}
			});

			img_man_check_uncheck.setBackgroundResource(listAcceptDedicationss
					.get(position).getImageResourceMan());
			img_map_check_uncheck.setBackgroundResource(listAcceptDedicationss
					.get(position).getImageResourceMap());
		}
		
		return convertView;
	}
	
	//Get TextView To Show Date Time Left...
	public TextView getDateTimeTv() {
		return tv_time_video;
	}

}
