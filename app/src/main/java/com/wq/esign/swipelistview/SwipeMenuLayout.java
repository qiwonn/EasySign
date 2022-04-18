package com.wq.esign.swipelistview;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

public class SwipeMenuLayout extends FrameLayout {

	private static final int CONTENT_VIEW_ID = 2;
	private static final int MENU_VIEW_ID = 1;

	private int mSwipeDirection;

	private View mContentView;
	private SwipeMenuView mMenuView;
	private int mDownX;
	private GestureDetectorCompat mGestureDetector;
	private OnGestureListener mGestureListener;
	private boolean isFling;
	private int MIN_FLING = dp2px(15);
	private int MAX_VELOCITYX = -dp2px(500);
	private ScrollerCompat mOpenScroller;
	private ScrollerCompat mCloseScroller;
	private int mBaseX;
	private int position;
	private Interpolator mCloseInterpolator;
	private Interpolator mOpenInterpolator;

	private boolean mSwipEnable = true;

	public SwipeMenuLayout(View contentView) {
		this(contentView, null, null);
	}

	public SwipeMenuLayout(View contentView, Interpolator closeInterpolator, Interpolator openInterpolator) {
		super(contentView.getContext());
		mCloseInterpolator = closeInterpolator;
		mOpenInterpolator = openInterpolator;
		mContentView = contentView;
		mMenuView = new SwipeMenuView(contentView.getContext());
		init();
	}

	private SwipeMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private SwipeMenuLayout(Context context) {
		super(context);
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
		if(null != mMenuView)mMenuView.setPosition(position);
	}

	public void setSwipeDirection(int swipeDirection) {
		mSwipeDirection = swipeDirection;
	}

	private void init() {
		setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
		
		mGestureListener = new SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				isFling = false;
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
								   float velocityX, float velocityY) {
				// TODO
				if (Math.abs(e1.getX() - e2.getX()) > MIN_FLING
					&& velocityX < MAX_VELOCITYX) {
					isFling = true;
				}
				// Log.i("byz", MAX_VELOCITYX + ", velocityX = " + velocityX);
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		};
		mGestureDetector = new GestureDetectorCompat(getContext(),
													 mGestureListener);

		// mScroller = ScrollerCompat.create(getContext(), new
		// BounceInterpolator());
		if (mCloseInterpolator != null) {
			mCloseScroller = ScrollerCompat.create(getContext(),
												   mCloseInterpolator);
		} else {
			mCloseScroller = ScrollerCompat.create(getContext());
		}
		if (mOpenInterpolator != null) {
			mOpenScroller = ScrollerCompat.create(getContext(),
												  mOpenInterpolator);
		} else {
			mOpenScroller = ScrollerCompat.create(getContext());
		}

		/*LayoutParams contentParams = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mContentView.setLayoutParams(contentParams);
		if (mContentView.getId() < 1) {
			mContentView.setId(CONTENT_VIEW_ID);
		}*/
		
		addView(mMenuView.getView());
		addView(mContentView);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public boolean onSwipe(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = (int) event.getX();
				isFling = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int dis = (int) (mDownX - event.getX());
				if (mMenuView.isOpen())
					dis += mMenuView.getView().getMeasuredWidth()*mSwipeDirection;
				
				swipe(dis);
				break;
			case MotionEvent.ACTION_UP:
				if ((isFling || Math.abs(mDownX - event.getX()) > (mMenuView.getView().getMeasuredWidth()>>1)) &&
					Math.signum(mDownX - event.getX()) == mSwipeDirection) {
					// open
					smoothOpenMenu();
				} else {
					// close
					smoothCloseMenu();
					return false;
				}
				break;
		}
		return true;
	}

	public boolean isOpen() {
		return mMenuView.isOpen();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	private void swipe(int dis) {
		if(!mSwipEnable)
			return;
		
		int menu_width = mMenuView.getView().getMeasuredWidth();
		
		if (Math.signum(dis) != mSwipeDirection)
			dis = 0;
		else if (Math.abs(dis) > menu_width)
			dis = menu_width*mSwipeDirection;
		
		mMenuView.getView().layout(-menu_width + mWidth,0,mWidth,mHeigh);

		mContentView.layout(-dis, mContentView.getTop(),
							mContentView.getWidth() -dis, mHeigh);
		/*mMenuView.getView().layout(getMeasuredWidth() - menu_width, 0,
							getMeasuredWidth(),
							getMeasuredHeight());
		
		mContentView.layout(-dis, mContentView.getTop(),
							mContentView.getWidth() -dis, getMeasuredHeight());*/
	}

	@Override
	public void computeScroll() {
		if (mMenuView.isOpen()) {
			if (mOpenScroller.computeScrollOffset()) {
				swipe(mOpenScroller.getCurrX()*mSwipeDirection);
				postInvalidate();
			}
		} else {
			if (mCloseScroller.computeScrollOffset()) {
				swipe((mBaseX - mCloseScroller.getCurrX())*mSwipeDirection);
				postInvalidate();
			}
		}
	}

	public void smoothCloseMenu() {
		mMenuView.stateClose();
		if (mSwipeDirection == SwipeMenuListView.DIRECTION_LEFT) {
			mBaseX = -mContentView.getLeft();
			mCloseScroller.startScroll(0, 0, mMenuView.getView().getMeasuredWidth(), 0, 100);
		} else {
			mBaseX = mMenuView.getView().getRight();
			mCloseScroller.startScroll(0, 0, mMenuView.getView().getMeasuredWidth(), 0, 100);
		}
		postInvalidate();
	}

	public void smoothOpenMenu() {
		if(!mSwipEnable)
			return;
		
		mMenuView.stateOpen();
		if (mSwipeDirection == SwipeMenuListView.DIRECTION_LEFT)
			mOpenScroller.startScroll(-mContentView.getLeft(), 0, mMenuView.getView().getMeasuredWidth(), 0, 350);
		else 
			mOpenScroller.startScroll(mContentView.getLeft(), 0, mMenuView.getView().getMeasuredWidth(), 0, 350);
		
		postInvalidate();
	}

	public void closeMenu() {
		if (mCloseScroller.computeScrollOffset()) {
			mCloseScroller.abortAnimation();
		}
		if (mMenuView.isOpen()) {
			mMenuView.stateClose();
			swipe(0);
		}
	}

	public void openMenu() {
		if(!mSwipEnable)
			return;
		
		if (mMenuView.isClose()) {
			mMenuView.stateOpen();
			swipe(mMenuView.getView().getMeasuredWidth() * mSwipeDirection);
		}
	}

	public View getContentView() {
		return mContentView;
	}

	public SwipeMenuView getMenuView() {
		return mMenuView;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
											   getContext().getResources().getDisplayMetrics());
	}

	private int mWidth,mHeigh;
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth();
		mHeigh = getMeasuredHeight();
		mMenuView.getView().measure(MeasureSpec.makeMeasureSpec(0,
													  MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(
							  getMeasuredHeight(), MeasureSpec.EXACTLY));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mContentView.layout(0, 0, getMeasuredWidth(),
							mContentView.getMeasuredHeight());
		if (mSwipeDirection == SwipeMenuListView.DIRECTION_LEFT) {
			mMenuView.getView().layout(getMeasuredWidth() - mMenuView.getView().getMeasuredWidth(), 0,
							 getMeasuredWidth(),
							 mContentView.getMeasuredHeight());
		} else {
			mMenuView.getView().layout(0, 0,
									   mMenuView.getView().getMeasuredWidth(), mContentView.getMeasuredHeight());
		}
	}

	public void setSwipEnable(boolean swipEnable){
		mSwipEnable = swipEnable;
	}

	public boolean getSwipEnable(){
		return mSwipEnable;
	}
}
