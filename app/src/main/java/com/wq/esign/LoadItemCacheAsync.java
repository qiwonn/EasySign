package com.wq.esign;
import java.io.File;
import android.content.Context;
import android.widget.Toast;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
/** 异步载入本地item缓存 */
public final class LoadItemCacheAsync
{
	public LoadItemCacheAsync(){}
	
	public void readCachesAsync(final Context context){
		final File f = DiskCacheManager.getDiskCacheDir(context,"item");
		if (!f.exists()) {
			return;
		}
		
		final DiskCacheManager dcm = new DiskCacheManager(context,DiskCacheManager.mVersion,"item");
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					readCaches(dcm,f);
					try
					{
						Thread.sleep(2000);
					}
					catch (InterruptedException e)
					{}
					((MainActivity)context).runOnUiThread(new Runnable(){
							@Override
							public void run()
							{
								//更新列表视图
								Toast.makeText(context,"Load cache completed",0).show();
							}
						});
				}
			}).start();
	}
	//读取缓存数据(阻塞式)
	private void readCaches(DiskCacheManager dcm,File f){
		dcm.readTextCaches(f, new IWQFunc<InputStream>(){
				@Override
				public void invoke(InputStream o1)
				{
					BufferedReader in = null;
					String line ="",ub ="";
					String h1="",h2="",h3="",img ="";
					long id = -1;
					try{
						in = new BufferedReader(new InputStreamReader(o1, "UTF-8"));
						while ((line = in.readLine()) != null) {
							int d = line.indexOf(":");
							if(d<0)continue;
							ub = line.substring(0,d);
							d++;
							if(ub.equals("h1") && d < line.length()){
								h1 = line.substring(d,line.length());
							}else if(ub.equals("h2") && d < line.length()){
								h2 = line.substring(d,line.length());
							}else if(ub.equals("h3") && d < line.length()){
								h3 = line.substring(d,line.length());
							}else if(ub.equals("id") && d < line.length()){
								id = Long.parseLong(line.substring(d,line.length()));
							}else if(ub.equals("img") && d < line.length()){
								img = line.substring(d,line.length());
							}
						}
						SignManager.simplyAdd(new SignBeanX(id,h1,h2,h3,img,img));
					}catch(IOException e){
						return;
					}finally{
						try
						{
							if(null!=in)in.close();
							if(null!=o1)o1.close();
						}
						catch (IOException e)
						{}
					}
				}
			});
	}
}
