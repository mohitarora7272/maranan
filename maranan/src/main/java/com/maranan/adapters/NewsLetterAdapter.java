package com.maranan.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maranan.ApplicationSingleton;
import com.maranan.R;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.HorizontalListView;
import com.maranan.utils.Utilities;

public class NewsLetterAdapter extends BaseAdapter{
	private static NewsLetterAdapter mContext;
	private Context ctx;
	private HorizontalListAdapter adapter;
	private int pos = 0;
	private HashMap<String, ArrayList<GetterSetter>> hashMapNewsLetter;
	private Boolean isFirstLoad = false;
	
	// NewsLetterAdapter Instance
	public static NewsLetterAdapter getInstance() {
		return mContext;
	}
	
	public NewsLetterAdapter(Context ctxx) {
		mContext = NewsLetterAdapter.this;
		ctx = ctxx;
		hashMapNewsLetter = new HashMap<String, ArrayList<GetterSetter>>();
		if (((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getHashListNewsLetter().size() > 0) {
			this.hashMapNewsLetter = ((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getHashListNewsLetter();
		}
	}

	@Override
	public int getCount() {
		return hashMapNewsLetter.size();
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(ctx).inflate(R.layout.activity_newsletter, null, false);
			holder.horizontal_scroll_up = (HorizontalListView) convertView.findViewById(R.id.horizontal_scroll_up);
			holder.tv_news_date_up = (TextView) convertView.findViewById(R.id.tv_news_date_up);
			holder.tv_news_count_up = (TextView) convertView.findViewById(R.id.tv_news_count_up);
			holder.img_timer = (ImageView) convertView.findViewById(R.id.img_timer);
		
			convertView.setTag(holder);
		
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.horizontal_scroll_up.setTag(position);
		holder.tv_news_date_up.setTag(position);
		holder.tv_news_count_up.setTag(position);
		
		if (!((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_dates().get(position).equals("")) {
			holder.img_timer.setVisibility(View.VISIBLE);
			holder.tv_news_date_up.setText(ctx.getResources().getString(R.string.news_letter_title)+" "+((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_dates().get(position));
			holder.tv_news_count_up.setText(String.valueOf(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getHashListNewsLetter().get(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_dates().get(position)).size()));
			
			adapter = new HorizontalListAdapter(ctx, ((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getHashListNewsLetter().get(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_dates().get(position)));
			holder.horizontal_scroll_up.setAdapter(adapter);
		
		}else{
			holder.img_timer.setVisibility(View.INVISIBLE);
		}
		
		holder.horizontal_scroll_up.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				Utilities.downloadAndOpenPDF(ctx, ((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getHashListNewsLetter().get(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_dates().get(position)).get(pos).getPdf(),
						((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getHashListNewsLetter().get(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_dates().get(position)).get(pos).getTitle());
			}
		});
		
		return convertView;
	}

	class ViewHolder {
		HorizontalListView horizontal_scroll_up;
		TextView tv_news_date_up, tv_news_count_up;
		ImageView img_timer; 
		TextView tv_pdf_count, tv_pdf_name;
	}
	
	public int getPosition(){
		return pos;
	}
	
	public void setPosition(int pos){
		this.pos = pos;
	}
	
	public Boolean getIsFirstLoaded(){
		return isFirstLoad;
	}
	
	public void setIsFirstLoaded(Boolean isFirstLoad){
		this.isFirstLoad = isFirstLoad;
	}

}
