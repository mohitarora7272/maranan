package com.maranan.infiniteviewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * A FragmentPagerAdapter that can be used to achieve paging wrap-around when
 * you have less than 4 pages. Duplicate instances of pages will be created to
 * fulfill the min case.
 */
public class MinFragmentPagerAdapter extends PagerAdapter {

	private FragmentPagerAdapter adapter;

	public MinFragmentPagerAdapter(PagerAdapter adapter) {
		super();
	}

	public void setAdapter(FragmentPagerAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public int getCount() {
		int realCount = adapter.getCount();
		if (realCount == 1) {
			return 4;
		} else if (realCount == 2 || realCount == 3) {
			return realCount * 2;
		} else {
			return realCount;
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return adapter.isViewFromObject(view, object);
	}

	/**
	 * Warning: If you only have 1-3 real pages, this method will create
	 * multiple, duplicate instances of your Fragments to ensure wrap-around is
	 * possible. This may be a problem if you have editable fields or transient
	 * state (they will not be in sync).
	 * 
	 * @param position
	 * @return
	 */

	public Fragment getItem(int position) {
		int realCount = adapter.getCount();
		if (realCount == 1) {
			return adapter.getItem(0);
		} else if (realCount == 2 || realCount == 3) {
			return adapter.getItem(position % realCount);
		} else {
			return adapter.getItem(position);
		}
	}

}
