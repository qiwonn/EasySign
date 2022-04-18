package com.wq.esign;
import android.os.AsyncTask;
import android.content.Context;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.InputStream;
import android.text.TextUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.BaseAdapter;
import com.wq.esign.swipelistview.SwipeListAdapter;
/** 异步请求数据 */
public class QueryItemDataAsync extends AsyncTask<BaseAdapter,Void,BaseAdapter>
{
	private IWQFunc<Void> func = null;
	private Context context;
	public QueryItemDataAsync(Context context,IWQFunc<Void> func){
		this.context = context;
		this.func = func;
	}

	@Override
	protected BaseAdapter doInBackground(BaseAdapter... p1)
	{
		queryData();
		return p1[0];
	}

	@Override
	protected void onPostExecute(BaseAdapter result)
	{
		super.onPostExecute(result);
		//更新列表视图
		if(null!=result && null != SignManager.getDatas()){
			((SwipeListAdapter)result).setDatas(SignManager.getDatas());
			result.notifyDataSetChanged();
		}
		Toast.makeText(context,"Load completed",0).show();
		context = null;
	}
	
	private void queryData(){
		//硬盘缓存管理器
		DiskCacheManager dcmt = new DiskCacheManager(context,DiskCacheManager.mVersion,"thumbnail");
		InputStream ins = null;
		//top table
		SignSQLiteHelper sdb = new SignSQLiteHelper(context,SignSQLiteHelper.DATABASE,null,1);
		List<SignBeanX> toplist = queryTopItem(sdb,dcmt,ins);
		List<SignBeanX> sblist = queryItem(sdb,dcmt,ins);
		//进行分组
		sblist = yearGrouped(topGrouped(toplist,sblist.size()),sblist);
		//保存到静态列表
		SignManager.setDatas(sblist);
		//初始化静态清空列表
		SignManager.setClearList(new ArrayList<SignBeanX>());
		/*StringBuilder bu = new StringBuilder();
		for(SignBeanX b : sblist){
			bu.append(b.toString());
		}
		FileUtil.str2File(bu.toString(),"/aplayerlog/item_db.txt");*/
	}
	
	private List<SignBeanX> queryTopItem(SignSQLiteHelper sdb,DiskCacheManager dcmt,InputStream ins){
		List<String[]>  sarlist = sdb.queryItemTop();//查询数据库(阻塞式)
		//降序排列
		sortArray(sarlist);
		//置顶
		List<SignBeanX> toplist = Collections.synchronizedList(new ArrayList<SignBeanX>());
		SignBeanX xp;
		if(null!=sarlist && sarlist.size()>0){
			for(String[] s : sarlist){
				xp = createBean(s,dcmt,ins);
				xp.setzhiTop(true);
				toplist.add(xp);
			}
		}
		return toplist;
	}
	
	private List<SignBeanX> queryItem(SignSQLiteHelper sdb,DiskCacheManager dcmt,InputStream ins){
		//非置顶
		List<SignBeanX> sblist = Collections.synchronizedList(new ArrayList<SignBeanX>());
		List<String[]> sarlist = sdb.queryItem();//查询数据库(阻塞式)
		//降序排列
		sortArray(sarlist);
		SignBeanX xp;
		if(null!=sarlist && sarlist.size()>0){
			for(String[] s : sarlist){
				xp = createBean(s,dcmt,ins);
				sblist.add(xp);
			}
		}
		return sblist;
	}
	
	private SignBeanX createBean(String[] s,DiskCacheManager dcmt,InputStream ins){
		String ts = DateUtil.isToday(s[3]);
		SignBeanX xp = new SignBeanX(Long.parseLong(s[0]),s[1],s[2],ts,s[4],s[5]);
		xp.setDateTime(s[3]);
		setDateTime(xp,s[3]);
		//载入缩略图
		if(!TextUtils.isEmpty(s[4])){
			ins = dcmt.getCacheInputStream(s[0]);
			if(null != ins){
				WQThumbNailManager.add(s[0],new BitmapDrawable(BitmapFactory.decodeStream(ins)));
			}
		}
		int w = TextUtils.isEmpty(s[4]) ? DisplayUtil.tv_width_long : DisplayUtil.tv_width_shor;
		xp.setH3(DisplayUtil.appendEllipsizeStr(s[1],s[2],ts,DisplayUtil.tpaint,w));
		return xp;
	}
	
	private void sortArray(List<String[]> sarlist){
		//排序(数量必须要2个以上)
		if(null != sarlist && sarlist.size()>1){
			//置顶降序
			Collections.sort(sarlist, new Comparator<String[]>(){
					@Override
					public int compare(String[] p1, String[] p2){
						return p2[3].compareTo(p1[3]);
					}
				});
		}
	}
	
	private void setDateTime(SignBeanX x,String dateStr){
		String[] s2 = dateStr.split(" ");
		String[] s3 = s2[0].split("/");
		x.year = (short)Integer.parseInt(s3[0]);
		x.month = (byte)Integer.parseInt(s3[1]);
		x.day = (byte)Integer.parseInt(s3[2]);
		s3 = s2[1].split(":");
		x.hour = (byte)Integer.parseInt(s3[0]);
		x.minute = (byte)Integer.parseInt(s3[1]);
	}
	
	//置顶组
	private List<SignBeanX> topGrouped(List<SignBeanX>toplist,int bsize){
		if(toplist.size() <= 0 || bsize <= 0)return toplist;
		SignBeanX g = new SignBeanX(-1,context.getResources().getString(R.string.zhitop),"","","",null);
		g.isSession = true;
		toplist.add(0,g);
		return toplist;
	}
	
	//年份组
	private List<SignBeanX> yearGrouped(List<SignBeanX>toplist,List<SignBeanX> sblist){
		if(sblist.size() <= 0)return sblist;
		//年份分组
		SignBeanX g = sblist.get(0);
		int syear = g.year;
		if(toplist.size() > 0 || g.year != DateUtil.year){
			g = new SignBeanX(-1,String.valueOf(syear),"","","",null);
			g.isSession = true;
			toplist.add(g);
		}
		SignBeanX b;
		for(int i=0;i<sblist.size();i++){
			b = sblist.get(i);
			if(b.year != syear){
				syear = b.year;
				g = new SignBeanX(-1,String.valueOf(b.year),"","","",null);
				g.isSession = true;
				toplist.add(g);
			}
			toplist.add(b);
		}
		return toplist;
	}
	
	//元素聚集单循环排序
	/*private void sort666(List<SignBeanX> sblist){
		//置顶(时间换空间)
		int z = -1;
		int i = sblist.size();
		int j = i - 1;
		while((--i) > z){
			if(sblist.get(j).getzhiTop()){
		 		sblist.add(0,sblist.remove(j));
		 		z++;
			}
		 	else{
		 		j--;
		 	}
		}
	}*/
}
