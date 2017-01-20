package com.maranan.infiniteviewpager;

import android.app.Activity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.maranan.R;

import java.util.ArrayList;
import java.util.List;


public class OnPageChangeListenerForInfiniteIndicator implements
		OnPageChangeListener {
	private Activity activity;
	private List<ImageView> pageIndicatorList = new ArrayList<ImageView>();
	private LinearLayout containerIndicator;
	private int viewPagerActivePosition;
	private int positionToUse = 0;
	private int actualPosition;

	public OnPageChangeListenerForInfiniteIndicator(Activity activity,
			int currentItem) {
		this.activity = activity;
		this.actualPosition = currentItem;
		this.viewPagerActivePosition = currentItem;
		loadIndicators();
	}

	private void loadIndicators() {
		containerIndicator = (LinearLayout) activity
				.findViewById(R.id.viewPagerCountDots);
		if (pageIndicatorList.size() < 1) {
			for (int i = 0; i < 3; i++) {
				ImageView imageView = new ImageView(activity);
				imageView.setImageResource(R.drawable.selecteditem_dot);// normal
																		// indicator
																		// image
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				params.setMargins(4, 0, 4, 0);
				imageView.setLayoutParams(params);
				
				// imageView.setLayoutParams(new
				// ViewGroup.LayoutParams(activity.getResources().getDimensionPixelOffset(R.dimen.home_banner_indicator_width),
				// ViewGroup.LayoutParams.MATCH_PARENT));
				pageIndicatorList.add(imageView);
			}
		}
		containerIndicator.removeAllViews();
		for (int x = 0; x < pageIndicatorList.size(); x++) {
			ImageView imageView = pageIndicatorList.get(x);
			imageView
					.setImageResource(x == positionToUse ? R.drawable.selecteditem_dot
							: R.drawable.nonselecteditem_dot); // active and
																// notactive
																// indicator
			containerIndicator.addView(imageView);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		actualPosition = position;
		int positionToUseOld = positionToUse;
		if (actualPosition < viewPagerActivePosition && positionOffset < 0.5f) {
			positionToUse = actualPosition % 3;
		} else {
			if (positionOffset > 0.5f) {
				positionToUse = (actualPosition + 1) % 3;
			} else {
				positionToUse = actualPosition % 3;
			}
		}
		if (positionToUseOld != positionToUse) {
			loadIndicators();
		}
	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == 0) {
			viewPagerActivePosition = actualPosition;
			positionToUse = viewPagerActivePosition % 3;
			loadIndicators();
		}
	}
}
