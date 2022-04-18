package com.wq.esign;
import android.content.Context;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorMatrix;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import android.graphics.drawable.Drawable;
/** 主界面底部导航栏 */
public class NavFoot
{
	//ColorMatrixColorFilter filter;
	private Drawable dr0,dr1;
	private Context context;
	private TextView tvdel;
	public NavFoot(Context context){
		this.context = context;
		showMainNav(context);
	}
	private void showMainNav(Context context){
		tvdel = ((Activity)context).findViewById(R.id.nav_cre_del);
		tvdel.setOnClickListener(new OnNoDoubleClickListener(){
				@Override
				public void onNoDoubleClick(){
					SignManager.addAllToClearList(SignManager.getAllChecked_list());
					EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_REMOVE));
				}
			});
		tvdel.setVisibility(View.VISIBLE);
		tvdel.setEnabled(false);
		tvdel.setTextColor(0xffdedede);
		tvdel.setText("删除");
		tvdel.setTextSize(12);
		int dh = DisplayUtil.dp2px(context,32);
		dr0 = context.getResources().getDrawable(R.drawable.undelete);
		dr0.setBounds(0,0,dh,dh);
		tvdel.setCompoundDrawables(null,dr0,null,null);
		dr1 = context.getResources().getDrawable(R.drawable.delete);
		dr1.setBounds(0,0,dh,dh);
		
		((Activity)context).findViewById(R.id.nav_cre_sendto).setVisibility(View.GONE);
		((Activity)context).findViewById(R.id.nav_cre_insertimg).setVisibility(View.GONE);
		((Activity)context).findViewById(R.id.nav_cre_img2text).setVisibility(View.GONE);
		((Activity)context).findViewById(R.id.nav_cre_voicein).setVisibility(View.GONE);
		
		//ColorMatrix matrix = new ColorMatrix();
		//matrix.setSaturation(0);
		//filter = new ColorMatrixColorFilter(matrix);
		//类注册到事件巴士
		EventBus.getDefault().register(this);
	}
	
	@Subscribe(threadMode = ThreadMode.POSTING)
	public void onNavReciveItemCheckEvent(EditEvent event) {
		if(!(event instanceof EditEvent))return;
		///Toast.makeText(context,"NavFoot 事件巴士 Action="+event,0).show();
		int v = event.intValue();
		if(v<=0){
			tvdel.setEnabled(false);
			tvdel.setTextColor(0xffdedede);
			tvdel.setCompoundDrawables(null,dr0,null,null);
		}else{
			tvdel.setEnabled(true);
			tvdel.setTextColor(0xffffa500);
			tvdel.setCompoundDrawables(null,dr1,null,null);
		}
	}
}
