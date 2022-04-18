package com.wq.esign;
import android.os.AsyncTask;
import android.content.Context;
import android.widget.Toast;
import android.widget.BaseAdapter;
/** 异步取消置顶数据 */
public class TopCancelDBAsync extends AsyncTask<BaseAdapter,Void,BaseAdapter>
{
	private IWQFunc<Void> func = null;
	private Context context;
	public TopCancelDBAsync(Context context,IWQFunc<Void> func){
		this.context = context;
		this.func = func;
	}

	@Override
	protected BaseAdapter doInBackground(BaseAdapter... p1)
	{
		queryTopCancel();
		return p1[0];
	}

	@Override
	protected void onPostExecute(BaseAdapter result)
	{
		super.onPostExecute(result);
		if(null != result){
			result.notifyDataSetChanged();
		}
		Toast.makeText(context,"Top cancel completed",0).show();
		context = null;
	}

	private void queryTopCancel(){
		SignSQLiteHelper sdb = new SignSQLiteHelper(context,SignSQLiteHelper.DATABASE,null,1);
		sdb.translate(SignSQLiteHelper.TABLE_TOP,SignSQLiteHelper.TABLE,SignManager.topSx.toStrArray());
		SignManager.zhiTopCancel();
	}
}
