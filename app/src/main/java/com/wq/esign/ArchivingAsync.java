package com.wq.esign;
import android.os.AsyncTask;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/** 异步创建存档 */
public class ArchivingAsync extends AsyncTask<SignBeanX,Void,Void>
{
	private IWQFunc<Void> finishedCallback;
	private Context context;
	private String text;
	private boolean isUpdate;
	public ArchivingAsync(Context context,String text,IWQFunc<Void> callback,boolean isUpdate){
		super();
		this.context = context;
		this.text = text;
		finishedCallback = callback;
		this.isUpdate = isUpdate;
	}
	@Override
	protected Void doInBackground(SignBeanX... p1)
	{
		if(p1==null && p1.length<=0)return null;
		SignBeanX xp = p1[0];
		final long id = xp.getmId();
		if(TextUtils.isEmpty(xp.getKey())){
			String k = DiskCacheManager.hashKeyForDisk(text+"\n"+id);
			xp.setKey(k);
		}
		//文本转输入流
		InputStream is = new ByteArrayInputStream(text.getBytes());
		//文本的前两行h1和h2
		String[] strs = getTwoLineText(is,IInsertMap.INSTANCE.size()>0);
		String h1 = "",h2="",date="";
		if(strs!=null){
			if(strs.length>0)h1 = strs[0];
			if(strs.length>1)h2 = strs[1];
		}
		date = DateUtil.getYYMMDDHHNNstr();
		xp.setH1(h1);
		xp.setH2(h2);
		xp.setDateTime(date);
		//第一张原图路径
		String ico = IInsertMap.INSTANCE.getFistInsert();
		if(!TextUtils.isEmpty(ico)){
			xp.setaIcon(xp.getKey());
		}else{
			xp.setaIcon("");
		}
		//时间戳作为唯一标识
		String sid = String.valueOf(id);
		//应用版本号
		int v = DiskCacheManager.mVersion;
		//缓存文件
		DiskCacheManager dcm = new DiskCacheManager(context,v,"file");
		dcm.saveCacheBytes(sid,text.getBytes());
		if(IInsertMap.INSTANCE.size()>0){
			//缩略图
			Bitmap bm = ImageLruCache.getTinyFitSampleBitmap(ico);
			//缓存缩略图
			dcm = new DiskCacheManager(context,v,"thumbnail");
			dcm.saveCacheBytes(sid,bitmapToBytes(bm));
			//缓存到缩略图管理器
			WQThumbNailManager.add(sid,new BitmapDrawable(bm));
			//保存插图路径到数据库
			SignSQLiteHelper sdb = new SignSQLiteHelper(context,SignSQLiteHelper.DATABASE,null,1);
			if(isUpdate)
				sdb.updateInsertImg(id,IInsertMap.INSTANCE.toStrArrayList(id));
			else 
				sdb.insertInsertImg(IInsertMap.INSTANCE.toStrArrayList(id));
		}
		//保存到数据库
		SignSQLiteHelper sdb = new SignSQLiteHelper(context,SignSQLiteHelper.DATABASE,null,1);
		if(isUpdate){
			sdb.updateItem(id,xp.toStrArray());
			sdb.updateFileContent(id,text);
		}else{
			sdb.insertItem(xp.toStrArray());
			sdb.insertFileContent(id,text);
		}
		//保存到静态列表中并把该元素的位置为第一位
		SignManager.insertFirst(xp);
		//样式化文本
		int w = TextUtils.isEmpty(xp.getaIcon()) ? DisplayUtil.tv_width_long : DisplayUtil.tv_width_shor;
		xp.setH3(DisplayUtil.appendEllipsizeStr(h1,h2,DateUtil.isToday(date),DisplayUtil.tpaint,w));
		//清空插图
		IInsertMap.INSTANCE.clear();
		context = null;
		text = null;
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		super.onPostExecute(result);
		if(null != finishedCallback)
			finishedCallback.invoke(null);
		context = null;
		text = null;
		finishedCallback = null;
	}
	
	private byte[] bitmapToBytes(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
		return baos.toByteArray();
	}
	
	public static String[] getTwoLineText(InputStream inputStream,boolean hasImg){
		int len = hasImg ? 22 : 32;
		String[] strs = new String[]{"",""};
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(isr);
        String oneLine = "";
		int n = 0;
        try {
            while ((oneLine = bufferedReader.readLine()) != null) {
				if(n>1)break;
               	if(TextUtils.isEmpty(oneLine) || oneLine.equals("\n") || oneLine.contains(String.valueOf(GlobalConst.IMG)))continue;
			   	strs[n++] = oneLine.length()<len?oneLine.trim():oneLine.substring(0,len).trim();
            }
			isr.close();
			bufferedReader.close();
            return strs;
        } catch (IOException e) {
            e.printStackTrace();
        }
		return strs;
	}
}
