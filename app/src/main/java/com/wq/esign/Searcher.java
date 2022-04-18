package com.wq.esign;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.app.Activity;
import android.animation.ValueAnimator;
import android.view.Gravity;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.Editable;
/** 搜索框 */
public class Searcher
{
	private Context context;
	private LinearLayout lmask;
	private EditText sv;
	private ImageView cl;
	private TextView go;
	public Searcher(Context context){
		this.context = context;
		initLayout();
	}

	//private int rightx;
    private void initLayout() {
		sv = ((Activity)context).findViewById(R.id.search_et_input);
		sv.addTextChangedListener(new TextWatcher(){
				@Override
				public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void onTextChanged(CharSequence p1, int p2, int p3, int p4){}
				@Override
				public void afterTextChanged(Editable p1){
					if(null == p1 || TextUtils.isEmpty(p1.toString())){
						setLayoutMask(true);
					}else{
						setLayoutMask(false);
					}
				}
			});
		sv.setBackgroundResource(R.drawable.search_bg);
		sv.setHint("搜索易签");
		//android.graphics.drawable.Drawable dr = context.getResources().getDrawable(R.drawable.delete_fill);
		android.graphics.drawable.Drawable dr0 = context.getResources().getDrawable(R.drawable.search);
		//int ph = DisplayUtil.dp2px(context,28);
		//dr.setBounds(0,0,ph,ph);
		dr0.setBounds(0,0,34,34);
		sv.setCompoundDrawables(dr0,null,null,null);
		sv.setCompoundDrawablePadding(DisplayUtil.dp2px(context,4));
		/*rightx = sv.getWidth()
		 - sv.getPaddingRight()
		 - dr.getIntrinsicWidth();
		 sv.setOnTouchListener(new View.OnTouchListener() {
		 @Override
		 public boolean onTouch(View v, MotionEvent event) {
		 //如果不是松开事件，不再处理
		 if (event.getAction() != MotionEvent.ACTION_UP)
		 return false;
		 //方法getCompoundDrawables()得到一个长度为4的数组，分别表示左上右下四张图片
		 android.graphics.drawable.Drawable drawable = sv.getCompoundDrawables()[2];
		 //如果右边没有Drawable，不再处理
		 if (drawable == null)
		 return false;
		 if (event.getX() > rightx){
		 sv.setText("");
		 }
		 return false;
		 }
		 });*/
		sv.setOnClickListener(listener);
		setDefaultStatus();

		cl = ((Activity)context).findViewById(R.id.search_iv_del);
		cl.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					cl.setVisibility(View.GONE);
					sv.setBackgroundResource(R.drawable.search_bg);
					sv.setText("");
				}
			});
		cl.setForeground(context.getResources().getDrawable(R.drawable.delete_fill));
		cl.setVisibility(View.GONE);

		go = ((Activity)context).findViewById(R.id.search_tv_cancel);
		go.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					go.setVisibility(View.GONE);
					lmask.setVisibility(View.GONE);
					cl.setVisibility(View.GONE);
					sv.setBackgroundResource(R.drawable.search_bg);
					sv.setText("");
					startTranslateYInAnim();
				}
			});
		go.setText("取消");
		go.setVisibility(View.GONE);
    }

	private View.OnClickListener listener = new View.OnClickListener(){
		@Override
		public void onClick(View p1){
			sv.setOnClickListener(null);

			if(!sv.isFocusable())sv.setFocusable(true);
			if(!sv.isFocusableInTouchMode())sv.setFocusableInTouchMode(true);
			if(!sv.hasFocus())sv.requestFocus();

			startTranslateYOutAnim();

			//Toast.makeText(context,"search",0).show();
		}
	};

	public void startTranslateYOutAnim() {
		if(null == lmask){
			lmask = ((Activity)context).findViewById(R.id.fl_mask);
			lmask.setBackgroundColor(0x4d000000);
		}
		lmask.setVisibility(View.VISIBLE);
		setLayoutMask(true);
		translateYAnim(0,-DisplayUtil.tit_bar_heigh);
    }
	public void startTranslateYInAnim() {
		setDefaultStatus();
		translateYAnim(-DisplayUtil.tit_bar_heigh,0);
    }

	private void setDefaultStatus(){
		SoftKeyboardManager.hideKeyboard((Activity)context);
		sv.setFocusable(false);
		sv.setFocusableInTouchMode(false);
		sv.setOnClickListener(listener);
		sv.setOnFocusChangeListener(new View.OnFocusChangeListener(){
				@Override
				public void onFocusChange(View p1, boolean p2){
					if(p2){//获得焦点
						//确保只监听一次事件
						sv.setOnFocusChangeListener(null);
						sv.setOnClickListener(null);
						SoftKeyboardManager.showSoftKeyboard((Activity)context);
						//Toast.makeText(context.getApplicationContext(),"Have focus                       ",0).show();
					}
				}
			});
	}

	private void translateYAnim(final int dfy,final int dty){
		final LinearLayout docv = (LinearLayout)sv.getParent().getParent();
		ValueAnimator vanim = ValueAnimator.ofInt(dfy,dty).setDuration(200);
		vanim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
				@Override
				public void onAnimationUpdate(ValueAnimator p1)
				{
					int v = p1.getAnimatedValue();
					docv.setY(v);
					if(v==dty){
						((Activity)context).runOnUiThread(new Runnable(){
								@Override
								public void run(){
									if(dty == 0){
										go.setVisibility(View.GONE);
									}else{
										go.setVisibility(View.VISIBLE);
									}
									//Toast.makeText(context.getApplicationContext(),"Translate end",0).show();
								}
							});
					}
				}
			});
		vanim.start();
	}

	private void setLayoutMask(boolean p1){
		if(p1){
			lmask.setBackgroundColor(0x4d000000);
			lmask.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View p1){
						lmask.setVisibility(View.GONE);
						cl.setVisibility(View.GONE);
						sv.setBackgroundResource(R.drawable.search_bg);
						sv.setText("");
						startTranslateYInAnim();
					}
				});
		}else{
			lmask.setBackgroundColor(0xffffffff);
			lmask.setOnClickListener(null);
			sv.setBackgroundResource(R.drawable.search_edit);
			cl.setVisibility(View.VISIBLE);
		}
	}
}
