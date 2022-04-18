package com.wq.esign;
import android.os.AsyncTask;
import android.content.Context;
import android.widget.Toast;
import android.widget.BaseAdapter;
/** 异步置顶数据 */
public class TopDBAsync extends AsyncTask<BaseAdapter,Void,BaseAdapter>
{
	private IWQFunc<Void> func = null;
	private Context context;
	public TopDBAsync(Context context,IWQFunc<Void> func){
		this.context = context;
		this.func = func;
	}

	@Override
	protected BaseAdapter doInBackground(BaseAdapter... p1)
	{
		queryTop();
		return p1[0];
	}

	@Override
	protected void onPostExecute(BaseAdapter result)
	{
		super.onPostExecute(result);
		if(null!=result){
			result.notifyDataSetChanged();
		}
		Toast.makeText(context,"Top completed",0).show();
		context = null;
	}
	
	private void queryTop(){
		SignSQLiteHelper sdb = new SignSQLiteHelper(context,SignSQLiteHelper.DATABASE,null,1);
		sdb.translate(SignSQLiteHelper.TABLE,SignSQLiteHelper.TABLE_TOP,SignManager.topSx.toStrArray());
		SignManager.zhiTop();
	}
}
