package com.maranan.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.maranan.R;
import com.maranan.VideoActivity;
import com.maranan.services.MyRadioService;
import com.maranan.utils.ConnectionDetector;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RadioLectureAdapter extends BaseAdapter {
    private static RadioLectureAdapter mContext;
    private Context ctx;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    private ViewHolder holder;
    private ArrayList<GetterSetter> radioLecList;

    // RadioLectureAdapter Instance
    public static RadioLectureAdapter getInstance() {
        return mContext;
    }

    @SuppressWarnings("deprecation")
    public RadioLectureAdapter(Context ctxx, ArrayList<GetterSetter> result) {
        ctx = ctxx;
        mContext = RadioLectureAdapter.this;
        cd = new ConnectionDetector(ctx);
        isInternetPresent = cd.isConnectingToInternet();
        radioLecList = result;
    }

    @Override
    public int getCount() {
        return radioLecList.size();
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.bottom_radio, null, false);
            holder.btn_cross = (Button) convertView.findViewById(R.id.btn_cross);
            holder.img_radio_bottom = (ImageView) convertView.findViewById(R.id.img_radio_bottom);
            holder.seek_bar = (SeekBar) convertView.findViewById(R.id.seek_bar);
            holder.tv_lecture_title_list = (TextView) convertView.findViewById(R.id.tv_lecture_title_list);
            holder.tv_lecture_title_list.setSelected(false);
            holder.tv_lecture_description11 = (TextView) convertView.findViewById(R.id.tv_lecture_description11);
            holder.tv_lecture_description11.setSelected(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.btn_cross.setTag(position);
        holder.img_radio_bottom.setTag(position);
        holder.seek_bar.setTag(position);
        holder.tv_lecture_title_list.setTag(position);
        holder.tv_lecture_description11.setTag(position);

        holder.tv_lecture_title_list.setText(Utilities.decodeImoString(radioLecList.get(position).getTitle()));
        holder.tv_lecture_description11.setText(Utilities.decodeImoString(radioLecList.get(position).getDescriptions()));

        Picasso.with(ctx)
                .load(radioLecList.get(position).getThumbnailHigh())
                .placeholder(R.drawable.below_thubnails)
                .error(R.drawable.below_thubnails).into(holder.img_radio_bottom);

        if (position == 0) {
            holder.seek_bar.setVisibility(View.GONE);
            holder.btn_cross.setVisibility(View.GONE);
            holder.seek_bar.setThumb(null);
        } else {
            holder.seek_bar.setVisibility(View.GONE);
            holder.btn_cross.setVisibility(View.GONE);
        }

        holder.btn_cross.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isInternetPresent) {

                    VideoActivity.getInstance().getSlidingDrawer().clearAnimation();
                    VideoActivity.getInstance().getPauseButton().setVisibility(View.INVISIBLE);
                    VideoActivity.getInstance().getPlayButton().setVisibility(View.VISIBLE);
                    VideoActivity.getInstance().getButtonView().setVisibility(View.GONE);
                    ctx.stopService(new Intent(ctx, MyRadioService.class));

                } else {
                    Utilities.showAlertDialog(ctx, "Internet Connection Error",
                            "Please connect to working Internet connection",
                            false);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        Button btn_cross;
        ImageView img_radio_bottom;
        SeekBar seek_bar;
        TextView tv_lecture_title_list, tv_lecture_description11;
    }

    public SeekBar getSeekBar() {
        return holder.seek_bar;
    }
}
