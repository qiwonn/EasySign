package com.wq.esign;
import android.os.AsyncTask;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import java.util.List;
import android.widget.Toast;
import android.widget.BaseAdapter;
/** 异步存档之删除 */
public class ArchivingDelAsync extends AsyncTask<BaseAdapter,Void,BaseAdapter>
{
	private IWQFunc<Void> finishedCallback;
	private Context context;
	public ArchivingDelAsync(Context context,IWQFunc<Void> callback){
		super();
		this.context = context;
		finishedCallback = callback;
	}
	@Override
	protected BaseAdapter doInBackground(BaseAdapter... p1)
	{
		if(p1==null && p1.length<=0)return null;
		List<SignBeanX> plist = SignManager.clearListClear();
		if(null == plist || plist.size()<=0)
			return null;
		final int size = plist.size();
		String[] sa = new String[size];
		//应用版本号
		int v = DiskCacheManager.mVersion;
		//缓存文件
		DiskCacheManager dcmf = new DiskCacheManager(context,v,"file");
		DiskCacheManager dcmt = new DiskCacheManager(context,v,"thumbnail");
		SignBeanX sx = null;
		String key = "";
		int i = -1;
		while(++i < size){
			sx = plist.get(i);
			key = String.valueOf(sx.getmId());//时间戳作为唯一标识
			dcmf.removeCache(key);//移除本地的文件缓存
			dcmt.removeCache(key);//移除本地的缩略图缓存
			WQThumbNailManager.remove(key);
			IInsertMapManager.remove(key);
			sa[i] = key;
		}
		//从数据库中删除
		SignSQLiteHelper sdb = new SignSQLiteHelper(context,SignSQLiteHelper.DATABASE,null,1);
		sdb.deleteMany("lid=?",sa);
		//清空删除列表
		plist.clear();
		dcmf.flush();
		dcmt.flush();
		return p1[0];
	}

	@Override
	protected void onPostExecute(BaseAdapter result)
	{
		super.onPostExecute(result);
		if(null != result){
			result.notifyDataSetChanged();
			Toast.makeText(context,"Del completed",0).show();
		}
		context = null;
		finishedCallback = null;
	}
}
