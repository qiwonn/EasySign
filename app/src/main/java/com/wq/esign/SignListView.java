package com.wq.esign;
import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.app.AlertDialog;
import android.app.Activity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import android.view.MotionEvent;
import android.widget.Toast;
/** 列表视图 */
public class SignListView extends ListView
{
	private Context context;
	//初始可拉动Y轴方向距离 
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 100; 
	//实际可上下拉动Y轴上的距离 
	private int mMaxYOverscrollDistance; 
	public SignListView(Context context,AttributeSet attr){
		super(context,attr);
		this.context = context;
	}

	@Override
    protected void onFinishInflate() {
        super.onFinishInflate();
		//ListView lst = ((Activity)context).findViewById(R.id.signlist);
		setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4){
					if(SignManager.isEdit)return false;
					SignBeanX sx = SignManager.getDatas().get(p3);
					BottomDialog.showBottomDialog(context,sx);
					return true;
				}
			});
		
		setOnItemClickListener(new OnNoDoubleItemClickListener(){
				@Override
				public void onNoDoubleClick(View p2,int p3){
					//记录当前点击的item
					SignBeanX sb = (SignBeanX)((SignAdapterX)getAdapter()).getItem(p3);
					SignManager.curSb = sb;
					if(!SignManager.isEdit){
						this.setDelay(5000);
						IInsertMap.INSTANCE.clear();
						///new ViewDetailsAsync(getContext().getApplicationContext(), DisplayUtil.EDITTEXT_WIDTH, null).execute();
						SignListView.this.context.startActivity(new Intent(SignListView.this.context,SignDetActivity.class));
						((Activity)SignListView.this.context).overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
					}else{
						this.setDelay(400);
						sb.isChecked = !sb.isChecked;
						SignManager.checkcount = sb.isChecked ? SignManager.checkcount+1 : SignManager.checkcount-1;
						((SignAdapterX)getAdapter()).getContentView(p3,p2);
						EventBus.getDefault().post(new EditEvent(SignManager.checkcount));
					}
				}
			});
			
		initBounceListView();
		//lst.setOverscrollHeader(null);
		//SwipeMenu smenu = createSwipeMenu();
		//swipeItem = new SwipeItemLayout(context,new SwipeMenuLayout(context,smenu));
    }
	
	private void initBounceListView(){ 
		final DisplayMetrics metrics = context.getResources().getDisplayMetrics(); 
		final float density = metrics.density; 
		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE); 
	} 
	
	@Override 
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,  
								   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {  
		//实现的本质就是在这里动态改变了maxOverScrollY的值 
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);  
	}

	/*private SwipeItemLayout swipeItem;
	private float mDownX;
	private int mTouchPosition;
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
			mDownX = ev.getX();
			mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
			View view = getChildAt(mTouchPosition - getFirstVisiblePosition());
			if(null != view)
				Toast.makeText(context,"Item view "+view.toString(),1).show();
			if(!(view instanceof SwipeItemLayout)){
				((com.wq.esign.SignAdapterX.ViewHolder)view.getTag()).isSwiper = true;
				swipeItem.addConvertView(view);
				((SignAdapterX)getAdapter()).getSwipeView(mTouchPosition,swipeItem,this);
			}
			//Toast.makeText(context,"Item view position "+mTouchPosition,1).show();
		}else if(ev.getAction() == MotionEvent.ACTION_MOVE){
			//swipeItem.onSwipe(ev);
		}
		return super.onTouchEvent(ev);
	}
	
	private SwipeMenu createSwipeMenu(){
		SwipeMenu menu = new SwipeMenu();
		// create "删除" item
		SwipeMenuItem deleteItem = new SwipeMenuItem();
		int dw = DisplayUtil.dp2px(context,90);
		deleteItem.setWidth(dw);
		deleteItem.setTitle("删除");
		deleteItem.setTitleSize(16);
		deleteItem.setTitleColor(0xffffa500);
		menu.addMenuItem(deleteItem);
		// create "打开" item
		SwipeMenuItem openItem = new SwipeMenuItem();
		openItem.setWidth(dw);
		openItem.setTitle("打开");
		openItem.setTitleSize(16);
		openItem.setTitleColor(0xffffa500);
		menu.addMenuItem(openItem);
		return menu;
	}*/
}
