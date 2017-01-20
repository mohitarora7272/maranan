package com.maranan.adapters;


import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ListDeatailedImageAdapter extends PagerAdapter {
    public static ListDeatailedImageAdapter context;
    private Context ctx;
    private String image;

    public static ListDeatailedImageAdapter getInstance() {
        return context;
    }

    public ListDeatailedImageAdapter(Context ctx, String image) {
        this.ctx = ctx;
        context = ListDeatailedImageAdapter.this;
        this.image = image;
    }

    @Override
    public int getCount() {
        return 1;
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
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(ctx).load(image)
                .into(view);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

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
}
