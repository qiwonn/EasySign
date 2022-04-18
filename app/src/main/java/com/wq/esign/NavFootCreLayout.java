package com.wq.esign;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.AttributeSet;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.graphics.drawable.Drawable;
import org.greenrobot.eventbus.EventBus;
/** 底部导航栏 */
public class NavFootCreLayout {
	private Drawable dr0,dr1;
	private TextView ivsendt;
	private TextView ivdelet;
	private TextView ivinser;
	private TextView ivimg2t;
	private TextView ivvoice;
	private Context context;
    public NavFootCreLayout(Context context,int p1) {
		this.context = context;
		onFinishInflate(p1);
    }

    private void onFinishInflate(int rk) {
		int sz = 12;
		ivdelet = ((Activity)context).findViewById(R.id.nav_cre_del);
		ivdelet.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(),MainActivity.class));
					((Activity)context).overridePendingTransition(R.anim.back_enter,R.anim.back_exit);
					SignManager.addToClearList(SignManager.curSb);
					EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_REMOVE));
				}
			});
		ivdelet.setText("删除");
		ivdelet.setTextColor(0xffffa500);
		ivdelet.setTextSize(sz);
		
		ivsendt = ((Activity)context).findViewById(R.id.nav_cre_sendto);
		ivsendt.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					Toast.makeText(context.getApplicationContext(),"发送功能敬请期待",0).show();
				}
			});
		ivsendt.setText("发送");
		ivsendt.setTextColor(0xffffa500);
		ivsendt.setTextSize(sz);
		int dh = DisplayUtil.dp2px(context,32);
		dr0 = context.getResources().getDrawable(R.drawable.unsend2);
		dr0.setBounds(0,0,dh,dh);
		dr1 = context.getResources().getDrawable(R.drawable.send2);
		dr1.setBounds(0,0,dh,dh);
			
		ivinser = ((Activity)context).findViewById(R.id.nav_cre_insertimg);
		ivinser.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					final CharSequence[] items = { "相册", "相机" ,"取消"};
					AlertDialog dlg = new AlertDialog.Builder(context).setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int item) {
                                //这里item是根据选择的方式,
                                //在items数组里面定义了两种方式, 拍照的下标为1所以就调用拍照方法
                                if(item==1){
                                    Intent getImageByCamera= new Intent("android.media.action.IMAGE_CAPTURE");
                                    ((Activity)context).startActivityForResult(getImageByCamera, GlobalConst.CAMERA_SUCCESS);
                                }else if(item == 0){
                                    Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                                    getImage.addCategory(Intent.CATEGORY_OPENABLE);
                                    getImage.setType("image/*");
                                    ((Activity)context).startActivityForResult(getImage, GlobalConst.PHOTO_SUCCESS);
                                }else{
									dialog.dismiss();
								}
                            }
                        }).create();
					dlg.show();
				}
			});
		ivinser.setText("插入图片");
		ivinser.setTextColor(0xffffa500);
		ivinser.setTextSize(sz);

		ivimg2t = ((Activity)context).findViewById(R.id.nav_cre_img2text);
		ivimg2t.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					Toast.makeText(context.getApplicationContext(),"图片转文字功能敬请期待",0).show();
				}
			});
		ivimg2t.setText("图片转文字");
		ivimg2t.setTextColor(0xffffa500);
		ivimg2t.setTextSize(sz);
			
		ivvoice = ((Activity)context).findViewById(R.id.nav_cre_voicein);
		ivvoice.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					Toast.makeText(context.getApplicationContext(),"语音输入功能敬请期待",0).show();
				}
			});
		ivvoice.setText("语音输入");
		ivvoice.setTextColor(0xffffa500);
		ivvoice.setTextSize(sz);
    }

	public void showDetLayout(){
		ivsendt.setVisibility(View.VISIBLE);
		ivdelet.setVisibility(View.VISIBLE);
		ivinser.setVisibility(View.GONE);
		ivimg2t.setVisibility(View.GONE);
		ivvoice.setVisibility(View.GONE);
	}

	public void showModLayout(){
		ivsendt.setVisibility(View.VISIBLE);
		ivdelet.setVisibility(View.GONE);
		ivinser.setVisibility(View.VISIBLE);
		ivimg2t.setVisibility(View.VISIBLE);
		ivvoice.setVisibility(View.VISIBLE);
	}
	
	public void unenabledSendTab(){
		ivsendt.setTextColor(0xffdedede);
		ivsendt.setEnabled(false);
		ivsendt.setCompoundDrawables(null,dr0,null,null);
	}
	
	public void enabledSendTab(){
		ivsendt.setTextColor(0xffffa500);
		ivsendt.setEnabled(true);
		ivsendt.setCompoundDrawables(null,dr1,null,null);
	}
}
