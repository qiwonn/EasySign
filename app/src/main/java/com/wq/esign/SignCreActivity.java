package com.wq.esign;
import android.content.ContentResolver;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.text.TextUtils;
import org.greenrobot.eventbus.EventBus;
/** 创建界面 */
public class SignCreActivity extends SignActivity 
{
	private TitleSignCreLayout tit;
	private NavFootCreLayout nav;
	@Override
	protected void setContentLayout()
	{
		setContentView(R.layout.cre_sign);
		tit = new TitleSignCreLayout(this,true);
		new TuWenCreLayout(this);
		nav = new NavFootCreLayout(this,GlobalConst.RK_CREATE);
		beBack = false;
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
		//解注册
        //EventBus.getDefault().unregister(this);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ContentResolver resolver = getContentResolver();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalConst.PHOTO_SUCCESS:
                    EditText et = findViewById(R.id.hb_cre_edit);
					IInsertAPic iin = new IInsertAPic();
					String v = iin.insertByPhoto(this.getApplicationContext(),intent,resolver,et);
					if(!TextUtils.isEmpty(v)){
						Toast.makeText(this,v,0).show();
					}
                    break;
                case GlobalConst.CAMERA_SUCCESS:
					et = findViewById(R.id.hb_cre_edit);
					iin = new IInsertAPic();
                    v = iin.insertByCamera(this.getApplicationContext(),intent,et);
					if(!TextUtils.isEmpty(v)){
						Toast.makeText(this,v,0).show();
					}
                    break;
                default:
                    break;
            }
        }
    }

	public void setDefaultStatus(){
		final EditText edit = SignCreActivity.this.findViewById(R.id.hb_cre_edit);
		edit.setOnFocusChangeListener(new View.OnFocusChangeListener(){
				@Override
				public void onFocusChange(View p1, boolean p2)
				{
					///Toast.makeText(SignDetActivity.this,"onFocusChange p2= "+p2,1).show();
					if(p2){//获得焦点
						//确保只监听一次事件
						edit.setOnFocusChangeListener(null);
						edit.setOnClickListener(null);
						//Show Modify Layout
						nav.showModLayout();
						tit.showModLayout();
					}
				}
			});

		edit.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if(!edit.isFocusable())edit.setFocusable(true);
					if(!edit.isFocusableInTouchMode())edit.setFocusableInTouchMode(true);
					if(!edit.hasFocus()){
						edit.requestFocus();
					}
					///Toast.makeText(SignDetActivity.this,"EditText点击事件",1).show();
				}
			});
		edit.setFocusable(false);
		edit.setFocusableInTouchMode(false);

		//Show Detail Layout
		nav.showDetLayout();
		tit.showDetLayout();
	}
	
	private boolean beBack;
	/**
	 * 监听Back键按下事件
	 * 注意:
	 * super.onBackPressed()会自动调用finish()方法,关闭
	 * 当前Activity.
	 * 若要屏蔽Back键盘,注释该行代码即可
	 */
	@Override
	public void onBackPressed() {
		if(beBack)return;
		beBack = true;
		//存档-异步
		tit.archiving(true);
		//Toast.makeText(this.getApplicationContext(),"按下了back键",1).show();
	}
}
