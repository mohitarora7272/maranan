package com.maranan.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.maranan.ApplicationSingleton;
import com.maranan.R;
import com.maranan.VideoActivity;
import com.maranan.VideoDrawerActivity;
import com.maranan.utils.GetterSetter;
import com.maranan.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ScreenSlidePagerAdapter extends PagerAdapter {
    public static ScreenSlidePagerAdapter context;
    private Context ctx;
    private ArrayList<String> listVideo;
    private int pos;

    public static ScreenSlidePagerAdapter getInstance() {
        return context;
    }

    public ScreenSlidePagerAdapter(Context videoNewActivity, ArrayList<GetterSetter> arrayList) {
        ctx = videoNewActivity;
        context = ScreenSlidePagerAdapter.this;
        listVideo = new ArrayList<String>();
        for (int i = 0; i < arrayList.size(); i++) {
            listVideo.add(arrayList.get(i).getList().get(0).get("Url"));
        }

        // Set News Letter Image At Top
        if (listVideo.size() == arrayList.size()) {
            if (((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_NewsletterUp().size() > 0) {
                listVideo.add(((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_NewsletterUp().get(0).getImage_thumb());
                Log.e("ss", "ss? " + listVideo.size());
            }
        }
    }

    @Override
    public int getCount() {
        return listVideo.size();
    }

    /**
     * Create the page for the given position. The adapter is responsible for
     * adding the view to the container given here, although it only must ensure
     * this is done by the time it returns from .
     *
     * @param position The page position to be instantiated.
     * @return Returns an Object representing the new page. This does not need
     * to be a View, but can be some other container of the page.
     */
    @Override
    public Object instantiateItem(View collection, final int position) {
        ImageView view = new ImageView(ctx);
        view.setScaleType(ScaleType.FIT_XY);
        Picasso.with(ctx)
                .load(listVideo.get(position))
                .placeholder(R.color.black)
                .error(R.color.black).into(view);


        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (position + 1 != listVideo.size()) {
                    hitClickForVideo(position);

                } else {
                    if (((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_NewsletterUp() != null && ((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_NewsletterUp().size() > 0) {
                        Utilities.downloadAndOpenPDF(ctx,
                                ((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_NewsletterUp().get(0).getPdf(),
                                ((ApplicationSingleton) ctx.getApplicationContext()).dataContainer.getList_NewsletterUp().get(0).getTitle());

                    } else if (VideoActivity.arrayListVideo.get(position).getList() != null && VideoActivity.arrayListVideo.get(position).getList().size() > 0) {
                        hitClickForVideo(position);
                    }
                }
            }
        });


        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    /**
     * Remove a page for the given position. The adapter is responsible for
     * removing the view from its container, although it only must ensure this
     * is done by the time it returns from .
     *
     * @param position The page position to be removed.
     */
    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((ImageView) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    /**
     * Called when the a change in the shown pages has been completed. At this
     * point you must ensure that all of the pages have actually been added or
     * removed from the container as appropriate.
     */
    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }


    // Get Position Of the view Pager is Selected....
    public int getPosition() {
        return pos;
    }

    private void hitClickForVideo(int position) {
        pos = position;
        VideoActivity.getInstance().getActivityVisible().equals(false);
        VideoActivity.getInstance().gethandler().removeCallbacks(VideoActivity.getInstance().getRunnable());
        VideoActivity.getInstance().gethandler().removeCallbacks(null);

        if (((ApplicationSingleton) ctx.getApplicationContext()).player2 != null) {
            if (((ApplicationSingleton) ctx.getApplicationContext()).player2.isPlaying()) {
                ((ApplicationSingleton) ctx.getApplicationContext()).player2.pause();
                VideoActivity.getInstance().setPlayerPlaying(false);
                VideoActivity.getInstance().setPlayerPlaying2(true);
            }
        }

        if (((ApplicationSingleton) ctx.getApplicationContext()).player != null) {
            if (((ApplicationSingleton) ctx.getApplicationContext()).player.isPlaying()) {
                ((ApplicationSingleton) ctx.getApplicationContext()).player.pause();
                VideoActivity.getInstance().getMarqueeDisable();
                VideoActivity.getInstance().getCancelButton().setVisibility(View.VISIBLE);
                VideoActivity.getInstance().getPauseButton().setVisibility(View.INVISIBLE);
                VideoActivity.getInstance().getPlayButton().setVisibility(View.VISIBLE);
                VideoActivity.getInstance().getRelateLayout().setVisibility(View.VISIBLE);
                VideoActivity.getInstance().getListViewBotom().setVisibility(View.VISIBLE);
                VideoActivity.getInstance().getPlayButtonInvisible();
                VideoActivity.getInstance().setPlayerPlaying(true);
            }
        }

        if (!VideoActivity.arrayListVideo.get(position).getList().get(0).get("VideoId").equals("")) {

            Intent videoIntent = new Intent(ctx, VideoDrawerActivity.class);
            videoIntent.putExtra("Check", VideoActivity.getInstance().getCheckVal());
            videoIntent.putExtra("isPlayerPlaying", VideoActivity.getInstance().getPlayerPlaying());
            videoIntent.putExtra("isPlayerPlaying2", VideoActivity.getInstance().getPlayerPlaying2());
            videoIntent.putExtra("ArrayList", VideoLectureAdapter.getInstance().getNewArrayList());
            videoIntent.putExtra("ArrayListFull", VideoActivity.arrayListVideo.get(position).getList());
            videoIntent.putExtra("video_Id", VideoActivity.arrayListVideo.get(position).getList().get(0).get("VideoId"));
            ((Activity) ctx).startActivityForResult(videoIntent, VideoActivity.REQUEST_CODE);

        } else {
            Utilities.showSnackbar(ctx, ctx.getResources().getString(R.string.not_available));
        }
    }

}
