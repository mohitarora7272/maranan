package com.maranan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.maranan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class NavDrawerListAdapter extends BaseAdapter {
    private static NavDrawerListAdapter mContext;
    Context ctx;
    ArrayList<HashMap<String, String>> listId;

    // VideoNewActivity Instance
    public static NavDrawerListAdapter getInstance() {
        return mContext;
    }

    @SuppressWarnings("deprecation")
    public NavDrawerListAdapter(Context videoLectureListActivity,
                                ArrayList<HashMap<String, String>> youTubeArrayList) {
        ctx = videoLectureListActivity;
        mContext = NavDrawerListAdapter.this;
        listId = youTubeArrayList;


    }

    @Override
    public int getCount() {
        return listId.size();
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.activity_drawer_items, null, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img_view_thumbnail = (ImageView) convertView.findViewById(R.id.img_view_thumbnail_drawer);

        Picasso.with(ctx)
                .load(listId.get(position).get("Url"))
                .placeholder(R.drawable.below_thubnails)
                .error(R.drawable.below_thubnails).into(holder.img_view_thumbnail);
        return convertView;
    }

    class ViewHolder {
        ImageView img_view_thumbnail;
    }


    // Get New Array List
    public ArrayList<HashMap<String, String>> getNewArrayList() {
        return listId;

    }
}
