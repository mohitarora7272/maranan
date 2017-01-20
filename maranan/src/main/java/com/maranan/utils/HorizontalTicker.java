package com.maranan.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


public abstract class HorizontalTicker {
    public static final int DEFAULT_TICKER_SPEED = 50;
    public static final int DEFAULT_TICKER_SPEED2 = 15;
    private Context mContext;
    private int mCurrentXPosition;
    protected HorizontalScrollView mHorizontalScrollView;
    protected LinearLayout mHorizontalScrollViewLayout;
    private int mHorizontalScrollViewWidth;
    private boolean mIsTickerActive;
    protected Handler mScrollingViewHandler;
    protected Runnable mScrollingViewRunnable;
    protected TickerDirection mTickerDirection;
    protected int mTickerSpeed;
    protected int mTickerSpeed2;

    /* renamed from: widgets.HorizontalTicker.1 */
	public class C14591 implements OnGlobalLayoutListener {
        public C14591() {
        }

        @SuppressWarnings("deprecation")
		public void onGlobalLayout() {
            if (VERSION.SDK_INT < 16) {
                HorizontalTicker.this.mHorizontalScrollViewLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
                HorizontalTicker.this.mHorizontalScrollViewLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            HorizontalTicker.this.getScrollMaxAmount();
        }
    }

    /* renamed from: widgets.HorizontalTicker.2 */
   public class C14602 implements Runnable {
        public C14602() {
        }

		@Override
		public void run() {
            if (HorizontalTicker.this.mTickerDirection == TickerDirection.RIGHT) {
                HorizontalTicker.this.moveScrollViewToRight();
                if (HorizontalTicker.this.mScrollingViewRunnable != null) {
                    HorizontalTicker.this.mScrollingViewHandler.postDelayed(HorizontalTicker.this.mScrollingViewRunnable, (long) HorizontalTicker.this.mTickerSpeed);
                }
            } else 
            {
                HorizontalTicker.this.moveScrollViewToLeft();
                if (HorizontalTicker.this.mScrollingViewRunnable != null) {
                    HorizontalTicker.this.mScrollingViewHandler.postDelayed(HorizontalTicker.this.mScrollingViewRunnable, (long) HorizontalTicker.this.mTickerSpeed2);
                }
            }
           
        }
    }

    public enum TickerDirection {
        LEFT,
        RIGHT
    }

    public abstract void addContentToView(LinearLayout linearLayout);

    public HorizontalTicker(Context context, HorizontalScrollView scrollView, LinearLayout layout) {
        this.mTickerSpeed = DEFAULT_TICKER_SPEED;
        this.mTickerSpeed2 = DEFAULT_TICKER_SPEED2;
        this.mIsTickerActive = false;
        this.mContext = context;
        //this.mTickerDirection = tickerDirection;
        initTicker(scrollView, layout);
    }

    @SuppressLint({"NewApi"})
    private void initTicker(HorizontalScrollView scrollView, LinearLayout layout) {
        this.mHorizontalScrollView = scrollView;
        this.mHorizontalScrollViewLayout = layout;
        this.mHorizontalScrollViewLayout.removeAllViewsInLayout();
        this.mHorizontalScrollView.setHorizontalScrollBarEnabled(false);
        calculateTickerSpeed();
        calculateTickerSpeed2();
       // addEmptyView();
       //addContentToView(this.mHorizontalScrollViewLayout);
       // addEmptyView();
       /* this.mHorizontalScrollViewLayout.getViewTreeObserver().addOnGlobalLayoutListener(new C14591());
        this.mScrollingViewHandler = new Handler();
        this.mScrollingViewRunnable = new C14602();
        //restart();
*/    }

    public void addEmptyView() {
        TextView emptyView = new TextView(this.mContext);
        emptyView.setText("");
        emptyView.setLayoutParams(new LayoutParams(Utilities.getScreenWidth(this.mContext), -2));
        this.mHorizontalScrollViewLayout.addView(emptyView);
    }

    @SuppressWarnings("deprecation")
	private void getScrollMaxAmount() {
        this.mHorizontalScrollViewWidth = this.mHorizontalScrollView.getChildAt(0).getMeasuredWidth() - ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public void start() {
        if (!this.mIsTickerActive) {
            this.mIsTickerActive = true;
            this.mScrollingViewHandler.postDelayed(this.mScrollingViewRunnable, (long) this.mTickerSpeed);
        }
    }

    public void restart() {
        if (this.mIsTickerActive) {
            stop();
        }
        this.mHorizontalScrollView.scrollTo(0, this.mHorizontalScrollViewWidth);
        start();
    }

    public void moveScrollViewToRight() {
    	
        this.mCurrentXPosition = (this.mHorizontalScrollView.getScrollX()-1) - 2;
      
        if (this.mCurrentXPosition <= 0) {
            scrollFinished();
            this.mCurrentXPosition = this.mHorizontalScrollViewWidth;
        }
        this.mHorizontalScrollView.scrollTo(this.mCurrentXPosition, 0);
    }

    protected void scrollFinished() {
    }

    public void moveScrollViewToLeft() {
    	//Log.e("Dp", "ToPixel??" +Utilities.convertDipToPixels(this.mContext, 1));
        
    	this.mCurrentXPosition = (this.mHorizontalScrollView.getScrollX()-1) + 2;
       
       
        if(this.mHorizontalScrollViewWidth == this.mCurrentXPosition){
        	scrollFinished();
        	this.mCurrentXPosition = 0;
        }
       
        this.mHorizontalScrollView.scrollTo(this.mCurrentXPosition, 0);
    }
    
    public void stop() {
        if (this.mIsTickerActive) {
            this.mIsTickerActive = false;
            clearTimers();               
        }
    }

    private void clearTimers() {
        if (this.mScrollingViewHandler != null && this.mScrollingViewRunnable != null) {
            this.mScrollingViewHandler.removeCallbacks(this.mScrollingViewRunnable);
        }
    }

    private void calculateTickerSpeed() {
        this.mTickerSpeed = (int) (50.0f / Utilities.getScreenDensity(this.mContext));
    }
    
    private void calculateTickerSpeed2() {
       this.mTickerSpeed2 = (int) (15.0f / Utilities.getScreenDensity(this.mContext));
    }

    public void setTickerSpeed(int tickerSpeedInMillisecond) {
        this.mTickerSpeed = tickerSpeedInMillisecond;
    }

    public int getTickerSpeed() {
        return this.mTickerSpeed;
    }

    public boolean isTickerActive() {
        return this.mIsTickerActive;
    }
}
