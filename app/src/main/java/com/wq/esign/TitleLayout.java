package com.wq.esign;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.View;
import android.graphics.Typeface;
import android.app.Activity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/** 主标题栏 */
public class TitleLayout {
	private ImageView add;
	private ImageView voice;
	private TextView allsel;
	private TextView ttname;
	private TextView ttnam2;
	private TextView edit;
	private Context context;
	private boolean isEditMode = false;
	private boolean isAll = false;
	private LinearLayout nav;
    public TitleLayout(Context context) {
		this.context = context;
		initLayout();
    }

    private void initLayout() {
		add = ((Activity)context).findViewById(R.id.title_add);
		add.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					SignManager.curSb = null;
					context.getApplicationContext().startActivity(new Intent(context,SignCreActivity.class));
					((Activity)context).overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
				}
			});
		
		voice = ((Activity)context).findViewById(R.id.title_voice);
		voice.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					
				}
			});
		
		allsel = ((Activity)context).findViewById(R.id.title_select_all);
		allsel.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1){
					isAll = !isAll;
					if(isAll){
						allsel.setText("全不选");
						EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_SELECT_ALL));
						SignManager.checkcount = SignManager.getAllItemCountWithoutGroup();
						EventBus.getDefault().post(new EditEvent(SignManager.checkcount));
					}else{
						allsel.setText("全选");
						EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_SELECT_NONE));
						SignManager.checkcount = 0;
						EventBus.getDefault().post(new EditEvent(0));
					}
				}
			});
		allsel.setText("全选");
		
		ttname = ((Activity)context).findViewById(R.id.title_name);
		ttname.setText("易签");
		ttnam2 = ((Activity)context).findViewById(R.id.title_nam2);
		ttnam2.setText("选择易签");
		
		edit = ((Activity)context).findViewById(R.id.title_edit);
		edit.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					isEditMode = !isEditMode;
					if(isEditMode){
						onEdit();
					}else{
						onEditCancel();
					}
					enabledOthers(!isEditMode);
					//Toast.makeText(context,"TABLE_TOP clear completed",0).show();
				}
			});
		edit.setText("编辑");
		//自定义底部导航
		nav = ((Activity)context).findViewById(R.id.nav_foo_main);
		nav.setVisibility(View.GONE);
		//类注册到事件巴士
		EventBus.getDefault().register(this);
    }
	
	private void onEdit(){
		voice.setVisibility(View.GONE);
		add.setVisibility(View.GONE);

		ttname.setVisibility(View.GONE);
		ttnam2.setText(R.string.sign_sel);
		ttnam2.setVisibility(View.VISIBLE);
		allsel.setText(R.string.select_all);
		allsel.setVisibility(View.VISIBLE);
		edit.setText(R.string.cancel);

		nav.setVisibility(View.VISIBLE);
		SignManager.isEdit = true;
		EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_EDIT));
	}
	
	private void onEditCancel(){
		isAll = false;
		allsel.setText("");
		allsel.setVisibility(View.GONE);
		ttname.setVisibility(View.VISIBLE);
		ttnam2.setText("");
		ttnam2.setVisibility(View.GONE);
		voice.setVisibility(View.VISIBLE);
		add.setVisibility(View.VISIBLE);
		edit.setText(R.string.edit);

		nav.setVisibility(View.GONE);
		SignManager.isEdit = false;
		SignManager.checkcount = 0;
		EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_EDIT_CANCEL));
		EventBus.getDefault().post(new EditEvent(0));
	}
	
	private void enabledOthers(boolean v){
		//int texId = context.getResources().getIdentifier("android:id/search_src_text",null,null);
		EditText sv = ((Activity)context).findViewById(R.id.search_et_input);
		//TextView tv = sv.findViewById(texId);
		if(null!=sv){
			sv.setActivated(v);
			sv.setEnabled(v);
		}
		TextView tv = ((Activity)context).findViewById(R.id.cloud_sync_tv);
		tv.setActivated(v);
		tv.setClickable(v);
		tv.setEnabled(v);
	}
	
	@Subscribe(threadMode = ThreadMode.POSTING)
	public void onTitleReciveItemCheckEvent(EditEvent event) {
		if(!(event instanceof EditEvent))return;
		///Toast.makeText(context,"Title 事件巴士 Action="+event,Toast.LENGTH_SHORT).show();
		int v = event.intValue();
		if(v<=0)ttnam2.setText("选择易签");
		else ttnam2.setText("已选择"+v+"个易签");
	}
}
