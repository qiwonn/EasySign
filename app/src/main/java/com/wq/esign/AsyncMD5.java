package com.wq.esign;
import android.os.AsyncTask;
import android.content.Context;
import android.text.TextUtils;
/** 异步计算MD5 */
public class AsyncMD5 extends AsyncTask<String,Void,String>
{
	private IWQFunc<String> finishedCallback;
	public AsyncMD5(IWQFunc<String> callback){
		super();
		finishedCallback = callback;
	}
	@Override
	protected String doInBackground(String... p1)
	{
		if(p1==null || p1.length<=0)return null;
		long id = SignManager.curSb.getmId();
		//MD5数字签名验证内容有没有修改
		String md5key = DiskCacheManager.hashKeyForDisk(p1[0]+"\n"+id);
		if(md5key.equals(SignManager.curSb.getKey())){
			//没做任何修改,无需存档
			return null;
		}
		return md5key;
	}

	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		if(null!=finishedCallback)
			finishedCallback.invoke(result);
	}
}
