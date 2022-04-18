package com.wq.esign.swipelistview;
import android.content.Context;
import android.view.animation.Interpolator;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;
/** 滑动控制器 */
public class SwipeController
{
	private int mSwipeDirection;
	private int mDownX;
	private int MIN_FLING;
	private int MAX_VELOCITYX;
	private int mBaseX;
	
	public boolean isFling = false;
	public  boolean mSwipEnable = true;
	
	private Interpolator mCloseInterpolator;
	private Interpolator mOpenInterpolator;
	private ScrollerCompat mOpenScroller;
	private ScrollerCompat mCloseScroller;
	public GestureDetectorCompat mGestureDetector;
	public OnGestureListener mGestureListener;
	
	private View mContentView;
	private SwipeMenuView mMenuView;
	
	public SwipeController(Context context,Interpolator closeInterpolator, Interpolator openInterpolator){
		mCloseInterpolator = closeInterpolator;
		mOpenInterpolator = openInterpolator;
		init(context);
	}
	
	public void setSwipeDirection(int swipeDirection) {
		mSwipeDirection = swipeDirection;
	}
	public int getSwipeDirection(){
		return mSwipeDirection;
	}
	public void setSwipEnable(boolean swipEnable){
		mSwipEnable = swipEnable;
	}
	public boolean getSwipEnable(){
		return mSwipEnable;
	}
	
	private void init(Context context) {
		MIN_FLING = dp2px(context,15);
		MAX_VELOCITYX = -dp2px(context,500);
		
		mGestureListener = new SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				isFling = false;
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
								   float velocityX, float velocityY) {
				if (Math.abs(e1.getX() - e2.getX()) > MIN_FLING
					&& velocityX < MAX_VELOCITYX) {
					isFling = true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		};
		mGestureDetector = new GestureDetectorCompat(context,mGestureListener);
		// mScroller = ScrollerCompat.create(getContext(), new
		// BounceInterpolator());
		if (mCloseInterpolator != null)
			mCloseScroller = ScrollerCompat.create(context,mCloseInterpolator); 
		else 
			mCloseScroller = ScrollerCompat.create(context);
		
		if (mOpenInterpolator != null)
			mOpenScroller = ScrollerCompat.create(context,mOpenInterpolator); 
		else 
			mOpenScroller = ScrollerCompat.create(context);  
	}
	
	public void setConvertView(View v){
		mContentView = v;
	}
	
	public void setMenuView(SwipeMenuView v){
		mMenuView = v;
	}
	
	public boolean onSwipe(MotionEvent event,int width,int heigh) {
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

				swipe(dis,width,heigh);
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
	
	private void swipe(int dis,int mWidth,int mHeigh) {
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
	}
	
	public boolean computeScroll(int width,int heigh){
		if (mMenuView.isOpen()) {
			if (mOpenScroller.computeScrollOffset()) {
				swipe(mOpenScroller.getCurrX()*mSwipeDirection,width,heigh);
				return true;
			}
		} else {
			if (mCloseScroller.computeScrollOffset()) {
				swipe((mBaseX - mCloseScroller.getCurrX())*mSwipeDirection,width,heigh);
				return true;
			}
		}
		Toast.makeText(mContentView.getContext(),"compute scroll "+mSwipeDirection+" "+mSwipEnable,0).show();
		return false;
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
		Toast.makeText(mContentView.getContext(),"smooth close",0).show();
	}

	public boolean smoothOpenMenu() {
		if(!mSwipEnable)
			return false;

		mMenuView.stateOpen();
		if (mSwipeDirection == SwipeMenuListView.DIRECTION_LEFT)
			mOpenScroller.startScroll(-mContentView.getLeft(), 0, mMenuView.getView().getMeasuredWidth(), 0, 350);
		else 
			mOpenScroller.startScroll(mContentView.getLeft(), 0, mMenuView.getView().getMeasuredWidth(), 0, 350);
		Toast.makeText(mContentView.getContext(),"smooth open",0).show();
		return true;
	}

	public void closeMenu(int width,int heigh) {
		if (mCloseScroller.computeScrollOffset())
			mCloseScroller.abortAnimation();
		
		if (mMenuView.isOpen()) {
			mMenuView.stateClose();
			swipe(0,width,heigh);
		}
	}

	public void openMenu(int width,int heigh) {
		if(!mSwipEnable)
			return;

		if (mMenuView.isClose()) {
			mMenuView.stateOpen();
			swipe(mMenuView.getView().getMeasuredWidth() * mSwipeDirection,width,heigh);
		}
	}
	
	private int dp2px(Context context,int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
											   context.getResources().getDisplayMetrics());
	}
}
