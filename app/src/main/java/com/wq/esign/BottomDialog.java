package com.wq.esign;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.Gravity;
import android.view.ViewGroup;
import android.content.Context;
import org.greenrobot.eventbus.EventBus;
import android.widget.TextView;
/** 底部弹出对话框 qi.wong */
public class BottomDialog
{
	//显示底部弹框
	public static void showBottomDialog(Context con,final SignBeanX sx) {
		//1、使用Dialog、设置style
		final Dialog dialog = new Dialog(con, R.style.BottomDialogTheme);
		//2、设置布局
		View view = View.inflate(con, R.layout.dialog_content_circle, null);
		dialog.setContentView(view);
		//获取弹出窗口
		Window window = dialog.getWindow();
		//设置弹出位置
		window.setGravity(Gravity.BOTTOM);
		//设置弹出动画
		window.setWindowAnimations(R.style.BottomDialogAnimation);
		//设置对话框大小
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		dialog.show();
		((TextView)dialog.findViewById(R.id.dlg_btm_tit)).setText(sx.getH1());
		TextView tvdel = dialog.findViewById(R.id.dlg_btm_del);
		tvdel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					SignManager.addToClearList(sx);
					EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_REMOVE));
					dialog.dismiss();
				}
			});
		tvdel.setText("删除");

		TextView ztop = dialog.findViewById(R.id.dlg_btm_top);
		final boolean isZhiTop = sx.getzhiTop();
		ztop.setText(isZhiTop ? R.string.cancel_zhitop : R.string.zhitop);
		ztop.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					SignManager.setZhiTop(sx);
					if(isZhiTop)
						EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_TOP_CANCEL));
					else
						EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_TOP));
					dialog.dismiss();
				}
			});
			
		TextView tvcan = dialog.findViewById(R.id.dlg_btm_cancel);
		tvcan.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
				}
			});
		tvcan.setText("取消");
	}
}
