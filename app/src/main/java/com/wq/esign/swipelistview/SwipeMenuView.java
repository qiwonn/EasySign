package com.wq.esign.swipelistview;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import com.wq.esign.R;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;

public class SwipeMenuView implements OnClickListener {
	private static final int STATE_CLOSE = 0;
	private static final int STATE_OPEN = 1;
	private int state = STATE_CLOSE;
	private LinearLayout mView;
	private OnSwipeItemClickListener onItemClickListener;
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public SwipeMenuView(Context context) {
		mView = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.swipe_menu,null);
		
		ImageView ivup = mView.findViewById(R.id.menu_upload);
		ivup.setOnClickListener(this);
		ivup.setImageDrawable(context.getResources().getDrawable(R.drawable.upload_oval));
		ImageView ivde = mView.findViewById(R.id.menu_delete);
		ivde.setOnClickListener(this);
		ivde.setImageDrawable(context.getResources().getDrawable(R.drawable.delete_oval));
	}

	/*private TextView createTitle(SwipeMenuItem item,int id) {
		TextView tv = new TextView(getContext());
		tv.setId(id);
		tv.setGravity(Gravity.CENTER);
		tv.setBackgroundDrawable(item.getBackground());
		tv.setOnClickListener(this);
		if(!TextUtils.isEmpty(item.getTitle())){
			tv.setText(item.getTitle());
			tv.setTextSize(item.getTitleSize());
			tv.setTextColor(item.getTitleColor());
		}
		return tv;
	}*/

	@Override
	public void onClick(View v) {
		if (onItemClickListener != null && isOpen()) {
			onItemClickListener.onItemClick(this,v.getId());
		}
	}

	public OnSwipeItemClickListener getOnSwipeItemClickListener() {
		return onItemClickListener;
	}

	public void setOnSwipeItemClickListener(OnSwipeItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	
	public LinearLayout getView(){
		return mView;
	}
	
	public boolean isOpen() {
		return state == STATE_OPEN;
	}

	public boolean isClose(){
		return state == STATE_CLOSE;
	}

	public void stateOpen(){
		state = STATE_OPEN;
	}

	public void stateClose(){
		state = STATE_CLOSE;
	}

	public int state(){
		return state;
	}
}
