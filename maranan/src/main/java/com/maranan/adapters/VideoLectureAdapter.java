package com.maranan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maranan.R;
import com.maranan.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class VideoLectureAdapter extends BaseAdapter {
    private static VideoLectureAdapter mContext;
    private Context ctx;
    private ArrayList<HashMap<String, String>> listId;
    private ArrayList<HashMap<String, String>> listIdTemp = new ArrayList<HashMap<String, String>>();

    // VideoNewActivity Instance
    public static VideoLectureAdapter getInstance() {
        return mContext;
    }

    @SuppressWarnings("deprecation")
    public VideoLectureAdapter(Context videoLectureListActivity,
                               ArrayList<HashMap<String, String>> youTubeArrayList) {
        ctx = videoLectureListActivity;
        mContext = VideoLectureAdapter.this;
        listId = youTubeArrayList;
        for (int j = 1; j < listId.size(); j++) {
            listIdTemp.add(listId.get(j));
        }
    }

    @Override
    public int getCount() {
        return listIdTemp.size();
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.activity_lecture_list_items_new, null, false);
            holder.tv_title_vid = (TextView) convertView.findViewById(R.id.tv_title_vid);
            holder.tv_desc_vid = (TextView) convertView.findViewById(R.id.tv_desc_vid);
            holder.img_view_thumbnail = (ImageView) convertView.findViewById(R.id.img_view_thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title_vid.setText(Utilities.decodeImoString(listIdTemp.get(position).get("Title")));
        holder.tv_desc_vid.setText(Utilities.decodeImoString(listIdTemp.get(position).get("Description")));
        Picasso.with(ctx)
                .load(listIdTemp.get(position).get("Url"))
                .placeholder(R.drawable.below_thubnails)
                .error(R.drawable.below_thubnails).into(holder.img_view_thumbnail);
        return convertView;
    }

    class ViewHolder {
        TextView tv_title_vid, tv_desc_vid;
        ImageView img_view_thumbnail;
    }

    // Get New Array List
    public ArrayList<HashMap<String, String>> getNewArrayList() {
        return listIdTemp;
    }

    // Get New Array List
    public ArrayList<HashMap<String, String>> getFullArrayList() {
        return listId;
    }
}
