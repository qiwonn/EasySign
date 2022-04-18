package com.wq.esign;
import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.View;
import android.app.Activity;
import com.wq.esign.R;
import com.wq.esign.DisplayUtil;
import com.wq.esign.swipelistview.SwipeListAdapter;
import com.wq.esign.swipelistview.SwipeMenuListView;
import org.greenrobot.eventbus.EventBus;
import com.wq.esign.swipelistview.IOnMenuItemClickListener;
/** 可滑动列表视图封装 */
public class SwipeListView
{
	private SwipeMenuListView listView;
	private SwipeListAdapter adapter;
	
	public SwipeListView(Context context){
		initLayout(context);
	}
	
	private void initLayout(final Context context){
		//适配器指定应用自己定义的xml格式
		listView = ((Activity)context).findViewById(R.id.listView);
        adapter = new SwipeListAdapter(null,context);
        listView.setAdapter(adapter);
        // listener item click event
        listView.setOnMenuItemClickListener(new IOnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(int pos, int id) {
					if(id == R.id.menu_upload){
						Toast.makeText(context.getApplicationContext(),"上传",0).show();
					}else if(id == R.id.menu_delete){
						SignBeanX sx = (SignBeanX)adapter.getItem(pos);
						SignManager.addToClearList(sx);
						EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_REMOVE));
						///Toast.makeText(context.getApplicationContext(),"删除",0).show();
					}
					return false;
				}
			});

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
											   int position, long id) {
					if(SignManager.isEdit)return false;
					SignBeanX sx = (SignBeanX)adapter.getItem(position);
					BottomDialog.showBottomDialog(context,sx);
					///Toast.makeText(context.getApplicationContext(),"长按 item "+position,0).show();
					return true;
				}
			});

		
		listView.setOnItemClickListener(new OnNoDoubleItemClickListener(){
				@Override
				public void onNoDoubleClick(View p2,int p3){
					//记录当前点击的item
					SignBeanX sb = (SignBeanX)adapter.getItem(p3);
					SignManager.curSb = sb;
					if(!SignManager.isEdit){
						this.setDelay(5000);
						IInsertMap.INSTANCE.clear();
						///new ViewDetailsAsync(getContext().getApplicationContext(), DisplayUtil.EDITTEXT_WIDTH, null).execute();
						context.startActivity(new Intent(context,SignDetActivity.class));
						((Activity)context).overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
					}else{
						this.setDelay(400);
						sb.isChecked = !sb.isChecked;
						SignManager.checkcount = sb.isChecked ? SignManager.checkcount+1 : SignManager.checkcount-1;
						adapter.getContentView(p3,p2);
						EventBus.getDefault().post(new EditEvent(SignManager.checkcount));
					}
					///Toast.makeText(context.getApplicationContext(),"点击item "+p3,0).show();
				}
			});
	}
	
	public SwipeListAdapter getAdapter(){
		return adapter;
	}
}
