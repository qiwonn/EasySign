package com.wq.esign;
import android.widget.Toast;
import android.content.Context;
import android.widget.BaseAdapter;
/** 消息事件处理 */
public class MsgEventProcess
{
	private BaseAdapter adapter;
	public MsgEventProcess(BaseAdapter adaper){
		this.adapter = adaper;
	}
	public void procMsgEvent(Context context,int what){
		
		switch(what){
			case GlobalConst.ACTION_MODIFY:
				adapter.notifyDataSetChanged();
				break;
			case GlobalConst.ACTION_ADD:
				adapter.notifyDataSetChanged();
				break;
			case GlobalConst.ACTION_REMOVE:
				if(SignManager.getClearListSize() <= 0)
					return;
				//Toast.makeText(context.getApplicationContext(),"\ngetClearListSize = "+SignManager.getClearListSize(),1).show();
				//异步存档-删除
				new ArchivingDelAsync(context.getApplicationContext(),null).execute(adapter);
				break;
			case GlobalConst.ACTION_EDIT:
				SignManager.allEdited();
				adapter.notifyDataSetChanged();
				//Toast.makeText(context.getApplicationContext(),"edit",0).show();
				break;
			case GlobalConst.ACTION_EDIT_CANCEL:
				SignManager.noneEdited();
				SignManager.allUnchecked();
				adapter.notifyDataSetChanged();
				//Toast.makeText(context.getApplicationContext(),"cansel edit",0).show();
				break;
			case GlobalConst.ACTION_SELECT_ALL:
				SignManager.allChecked();
				adapter.notifyDataSetChanged();
				//Toast.makeText(context,"all select",0).show();
				break;
			case GlobalConst.ACTION_SELECT_NONE:
				SignManager.allUnchecked();
				adapter.notifyDataSetChanged();
				//Toast.makeText(context,"none select",0).show();
				break;
			case GlobalConst.ACTION_UPDATE:
				//((SignAdapterX)signlv.getAdapter()).notifyDataSetChanged();
				Toast.makeText(context.getApplicationContext(),"Update Msg",0).show();
				break;
			case GlobalConst.ACTION_TOP:
				if(null == SignManager.topSx)
					return;
				//异步top
				new TopDBAsync(context.getApplicationContext(),null).execute(adapter);
				break;
			case GlobalConst.ACTION_TOP_CANCEL:
				if(null == SignManager.topSx)
					return;
				//异步top cancel
				new TopCancelDBAsync(context.getApplicationContext(),null).execute(adapter);
				break;
			default:
				break;
		}
	}
}
