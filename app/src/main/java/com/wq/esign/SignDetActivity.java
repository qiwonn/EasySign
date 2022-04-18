package com.wq.esign;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ScrollView;
/** 详情界面 */
public class SignDetActivity extends SignActivity 
{
	private TitleSignCreLayout tit;
	private EditText edi;
	private NavFootCreLayout nav;
	@Override
	protected void setContentLayout()
	{
		setContentView(R.layout.cre_sign);
		
		edi = findViewById(R.id.hb_cre_edit);
		
		tit = new TitleSignCreLayout(this,false);
		new TuWenDetLayout(this);
		nav = new NavFootCreLayout(this,GlobalConst.RK_DETAIL);
		
		initLayout();
		
		//异步加载详情内容
		new ViewDetailsAsync(getApplicationContext(),DisplayUtil.EDITTEXT_WIDTH,null).execute(edi);
		/*WQSignDetail det = IInsertMapManager.getDetail(String.valueOf(SignManager.curSb.getmId()));
		 if(null != det){
		 if(null != det.editable())setText(det.editable());
		 else if(det.hasContent())setText(det.content());
		 }*/
		beBack = false;
	}
	
	private void initLayout(){
		setDefaultStatus();
	}
	
	public void setDefaultStatus(){
		edi.setOnFocusChangeListener(new View.OnFocusChangeListener(){
				@Override
				public void onFocusChange(View p1, boolean p2)
				{
					if(p2){//获得焦点
						//确保只监听一次事件
						edi.setOnFocusChangeListener(null);
						edi.setOnClickListener(null);
						//Show Modify Layout
						nav.showModLayout();
						tit.showModLayout();
						SoftKeyboardManager.showSoftKeyboard(SignDetActivity.this);
					}
				}
			});

		edi.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if(!edi.isFocusable())edi.setFocusable(true);
					if(!edi.isFocusableInTouchMode())edi.setFocusableInTouchMode(true);
					if(!edi.hasFocus()){
						edi.requestFocus();
					}
					///Toast.makeText(SignDetActivity.this,"EditText点击事件",1).show();
				}
			});
		edi.setFocusable(false);
		edi.setFocusableInTouchMode(false);
		
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
