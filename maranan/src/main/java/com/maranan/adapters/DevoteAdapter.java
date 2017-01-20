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
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maranan.R;
import com.maranan.utils.Config;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.Utilities;

public class DevoteAdapter extends BaseAdapter  {

	private Context ctx;
	private static DevoteAdapter mContext;
	private ArrayList<GetterSetter> listDevotes;
	private ArrayList<String> listNew;
	
	
	// DevoteActivity Instance
	public static DevoteAdapter getInstance() {
		return mContext;
	}

	public DevoteAdapter(Context ctxx, ArrayList<GetterSetter> listDevote) {
		ctx = ctxx;
		mContext = DevoteAdapter.this;
		listDevotes = listDevote;
		listNew = new ArrayList<String>(); 
		for (GetterSetter getterSetter : listDevote) {
			listNew.add(getterSetter.getTime());	
		}
	}

	@Override
	public int getCount() {
		return listDevotes.size();
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
			convertView = LayoutInflater.from(ctx).inflate(R.layout.activity_list_item_register, null, false);
			holder.tv_success_item = (TextView) convertView.findViewById(R.id.tv_success_item);
			holder.tv_name_item = (TextView) convertView.findViewById(R.id.tv_name_item);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.btn_check_uncheck = (ImageButton) convertView.findViewById(R.id.btn_check_uncheck);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.btn_check_uncheck.setTag(position);
		holder.tv_time.setTag(position);
		holder.tv_success_item.setText(listDevotes.get(position).getNature());
		//holder.tv_time.setText(listDevotes.get(position).getTime());

		if (listDevotes.get(position).getName_Optional().equals("")) {

			holder.tv_name_item.setText(Utilities.decodeImoString(listDevotes.get(position).getName()
					.trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getSex().trim()
					+ " "
					+ listDevotes.get(position).getThere_Is().trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getBlessing().trim()));

		} else if (listDevotes.get(position).getName().equals("")) {

			holder.tv_name_item.setText(Utilities.decodeImoString(listDevotes.get(position).getSex()
					.trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getThere_Is().trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getName_Optional().trim().replace("*", "'")
					+ " " + listDevotes.get(position).getBlessing().trim()));

		} else if (listDevotes.get(position).getSex().equals("")) {

			holder.tv_name_item.setText(Utilities.decodeImoString(listDevotes.get(position).getName()
					.trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getThere_Is().trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getName_Optional().trim().replace("*", "'")
					+ " " + listDevotes.get(position).getBlessing().trim()));

		} else if (listDevotes.get(position).getThere_Is().equals("")) {

			holder.tv_name_item.setText(Utilities.decodeImoString(listDevotes.get(position).getName()
					.trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getSex().trim()
					+ " "
					+ listDevotes.get(position).getName_Optional().trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getBlessing().trim()));

		} else if (listDevotes.get(position).getBlessing().equals("")) {

			holder.tv_name_item.setText(Utilities.decodeImoString(listDevotes.get(position).getName()
					.trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getSex().trim()
					+ " "
					+ listDevotes.get(position).getThere_Is().trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getName_Optional().trim().replace("*", "'")));

		} else {
			holder.tv_name_item.setText(Utilities.decodeImoString(listDevotes.get(position).getName()
					.trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getSex().trim()
					+ " "
					+ listDevotes.get(position).getThere_Is().trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getName_Optional().trim().replace("*", "'")
					+ " "
					+ listDevotes.get(position).getBlessing().trim()));
		}
        
		// Set onclick listener on button states changed...
		holder.btn_check_uncheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listDevotes.get(position).getImageResource() != R.drawable.icon_failed) {
					if (listDevotes.get(position).getPublish().equals("pending")) {
						if (listDevotes.get(position).getImageResource() == R.drawable.circle_white) {
							holder.btn_check_uncheck.setBackgroundResource(R.drawable.right);
							listDevotes.get(position).setImageResource(R.drawable.right);
							listDevotes.get(position).setTime(ctx.getResources().getString(R.string.pending_approval));
							notifyDataSetChanged();
							new UpdateStatus().execute(listDevotes.get(position).getId(), "pending");

						} else if (listDevotes.get(position).getImageResource() == R.drawable.right) {
							listDevotes.get(position).setImageResource(R.drawable.circle_white);
							listDevotes.get(position).setTime("");
							notifyDataSetChanged();
							new UpdateStatus().execute(listDevotes.get(position).getId(), "pause");
						}

					}else if(listDevotes.get(position).getPublish().equals("accept")){
						if (listDevotes.get(position).getImageResource() == R.drawable.circle_white) {
							listDevotes.get(position).setImageResource(R.drawable.right);
							listDevotes.get(position).setTime(listNew.get(position));
							Utilities.setUserIdDedicated(ctx, listDevotes.get(position).getEmail());
							notifyDataSetChanged();
							new UpdateStatus().execute(listDevotes.get(position).getId(), "accept");

						} else if (listDevotes.get(position).getImageResource() == R.drawable.right) {
							listDevotes.get(position).setImageResource(R.drawable.circle_white);
							listDevotes.get(position).setTime("");
							Utilities.setUserIdDedicated(ctx, "");
							notifyDataSetChanged();
							new UpdateStatus().execute(listDevotes.get(position).getId(), "pause");
						}

					}else if(listDevotes.get(position).getPublish().equals("pause") && listDevotes.get(position).getAdmin().equals("admin")){
						if (listDevotes.get(position).getImageResource() == R.drawable.circle_white) {
							listDevotes.get(position).setImageResource(R.drawable.right);
							listDevotes.get(position).setTime(listNew.get(position));
							Utilities.setUserIdDedicated(ctx, listDevotes.get(position).getEmail());
							notifyDataSetChanged();
							new UpdateStatus().execute(listDevotes.get(position).getId(), "accept");

						} else if (listDevotes.get(position).getImageResource() == R.drawable.right) {
							listDevotes.get(position).setImageResource(R.drawable.circle_white);
							listDevotes.get(position).setTime("");
							Utilities.setUserIdDedicated(ctx, "");
							notifyDataSetChanged();
							new UpdateStatus().execute(listDevotes.get(position).getId(), "pause");
						}
					}
					else if(listDevotes.get(position).getPublish().equals("pause") && listDevotes.get(position).getAdmin().equals("")){
						if (listDevotes.get(position).getImageResource() == R.drawable.circle_white) {
							listDevotes.get(position).setImageResource(R.drawable.right);
							listDevotes.get(position).setTime(ctx.getResources().getString(R.string.pending_approval));
							notifyDataSetChanged();
							new UpdateStatus().execute(listDevotes.get(position).getId(), "pending");

						} else if (listDevotes.get(position).getImageResource() == R.drawable.right) {
							listDevotes.get(position).setImageResource(R.drawable.circle_white);
							listDevotes.get(position).setTime("");
							notifyDataSetChanged();						
							new UpdateStatus().execute(listDevotes.get(position).getId(), "pause");
						}
					}
				}
				
			}
		});
		
        // Set devotes values here according to user selections... 
		if (listDevotes.get(position).getPublish().equals("pending")) {
			if (listDevotes.get(position).getImageResource() == R.drawable.circle_white) {
				holder.tv_time.setText("");

			} else if (listDevotes.get(position).getImageResource() == R.drawable.right) {
				holder.tv_time.setText(listNew.get(position));
			}

		}else if(listDevotes.get(position).getPublish().equals("accept")){
			if (listDevotes.get(position).getImageResource() == R.drawable.circle_white) {
				holder.tv_time.setText("");

			} else if (listDevotes.get(position).getImageResource() == R.drawable.right) {
				holder.tv_time.setText(listNew.get(position));
			}

		}else if(listDevotes.get(position).getPublish().equals("pause") && listDevotes.get(position).getAdmin().equals("admin")){
			if (listDevotes.get(position).getImageResource() == R.drawable.circle_white) {
				holder.tv_time.setText("");

			} else if (listDevotes.get(position).getImageResource() == R.drawable.right) {
				holder.tv_time.setText(listNew.get(position));
			}
		}
		else if(listDevotes.get(position).getPublish().equals("pause") && listDevotes.get(position).getAdmin().equals("")){
			if (listDevotes.get(position).getImageResource() == R.drawable.circle_white) {
				holder.tv_time.setText("");

			} else if (listDevotes.get(position).getImageResource() == R.drawable.right) {
				holder.tv_time.setText(ctx.getResources().getString(R.string.pending_approval));
			}
		
		}else if(listDevotes.get(position).getPublish().equals("reject")){
			SpannableStringBuilder builder = new SpannableStringBuilder();
			SpannableString redSpannable = new SpannableString(ctx.getResources().getString(R.string.failed_hebrew));
			redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, ctx.getResources().getString(R.string.failed_hebrew).length(), 0);
			builder.append(redSpannable);
			holder.tv_time.setText(builder);
		}
		
		holder.btn_check_uncheck.setBackgroundResource(listDevotes.get(position).getImageResource());
		return convertView;
	}

	public class ViewHolder {
		TextView tv_success_item, tv_name_item, tv_sex_item, tv_thereIS_item,
		tv_nameoptional_item, tv_acronymus_item, tv_time;
		public ImageButton btn_check_uncheck;
	}

	// Declare UpdateStatus Dedication...
	class UpdateStatus extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jObj = null;
			HttpParams params2 = new BasicHttpParams();
			params2.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			DefaultHttpClient mHttpClient = new DefaultHttpClient(params2);
			String url = Config.ROOT_SERVER_CLIENT + Config.UPDATE_STATUS;

			HttpPost httppost = new HttpPost(url);

			MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			try {
				multipartEntity.addPart("id", new StringBody(params[0]));
				multipartEntity.addPart("status", new StringBody(params[1]));
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
		}
	}
	
	public ArrayList<String> getTimeList(){
		return listNew;
		
	}

}
