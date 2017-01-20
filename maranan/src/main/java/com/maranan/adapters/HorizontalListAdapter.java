package com.maranan.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maranan.R;
import com.maranan.utils.GetterSetter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HorizontalListAdapter extends BaseAdapter {
    private static HorizontalListAdapter mContext;
    private Context ctx;
    private ViewHolder holder;
    private ArrayList<GetterSetter> listNewsLetter;
    private int pos = 0;

    private Integer[] circleArray = {R.drawable.circle_blue, R.drawable.circle_yellow,
            R.drawable.circle_perpal, R.drawable.circle_green};

    // NewsLetterAdapter Instance
    public static HorizontalListAdapter getInstance() {
        return mContext;
    }

    @SuppressWarnings("deprecation")
    public HorizontalListAdapter(Context ctxx,
                                 ArrayList<GetterSetter> listNewsLetter) {
        mContext = HorizontalListAdapter.this;
        ctx = ctxx;
        this.listNewsLetter = listNewsLetter;
    }

    @Override
    public int getCount() {
        return listNewsLetter.size();
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
        pos = position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.news_letter_list_items, null, false);
            holder.img_pdf = (ImageView) convertView.findViewById(R.id.img_pdf);
            holder.img_circle = (ImageView) convertView.findViewById(R.id.img_circle);
            holder.tv_pdf_count = (TextView) convertView.findViewById(R.id.tv_pdf_count);
            holder.tv_pdf_name = (TextView) convertView.findViewById(R.id.tv_pdf_name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_pdf_name.setText(listNewsLetter.get(position).getTitle());
        holder.tv_pdf_count.setText(String.valueOf(listNewsLetter.get(position).getCountNew()));

        Picasso.with(ctx)
                .load(listNewsLetter.get(position).getImage())
                .placeholder(R.drawable.below_thubnails)
                .error(R.drawable.below_thubnails).into(holder.img_pdf);

        if (pos < 4) {
            Bitmap circles = BitmapFactory.decodeResource(ctx.getResources(), circleArray[pos]);
            holder.img_circle.setImageBitmap(circles);
            pos++;

        } else {
            pos = 0;
            Bitmap circles = BitmapFactory.decodeResource(ctx.getResources(), circleArray[pos]);
            holder.img_circle.setImageBitmap(circles);
            pos++;
        }

        return convertView;
    }

    class ViewHolder {
        ImageView img_pdf;
        ImageView img_circle;
        TextView tv_pdf_count;
        TextView tv_pdf_name;
    }

    public Integer[] getCircleArray() {
        return circleArray;
    }

    public TextView getCountTv() {
        return holder.tv_pdf_count;
    }
}
