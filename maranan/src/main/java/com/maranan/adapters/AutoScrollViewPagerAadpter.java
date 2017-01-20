package com.maranan.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.squareup.picasso.Picasso;

public class AutoScrollViewPagerAadpter extends PagerAdapter {
	public static AutoScrollViewPagerAadpter context;
	private Context ctx;
	String[] flag = new String[] {
			"http://50.87.25.184/wall_images/wall_image_1.jpg",
			"http://50.87.25.184/wall_images/wall_image_2.jpg",
			"http://50.87.25.184/wall_images/wall_image_3.jpg" };

	public static AutoScrollViewPagerAadpter getInstance() {
		return context;
	}

	public AutoScrollViewPagerAadpter(Context videoNewActivity) {
		ctx = videoNewActivity;
		context = AutoScrollViewPagerAadpter.this;
	}

	@Override
	public int getCount() {
		return flag.length;
	}

	/**
	 * Create the page for the given position. The adapter is responsible for
	 * adding the view to the container given here, although it only must ensure
	 * this is done by the time it returns from .
	 * 
	 * @param position
	 *            The page position to be instantiated.
	 * @return Returns an Object representing the new page. This does not need
	 *         to be a View, but can be some other container of the page.
	 */
	@Override
	public Object instantiateItem(View collection, final int position) {
		ImageView view = new ImageView(ctx);
		view.setScaleType(ScaleType.FIT_XY);
		Picasso.with(ctx).load(flag[position])
				.into(view);

		view.setOnClickListener(new OnClickListener() {

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
	 * @param position
	 *            The page position to be removed.
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
	 *
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
