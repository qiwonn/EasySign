package com.wq.esign;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
/** 插图管理器 */
public class IInsertMapManager
{
	//<时间戳,详情>
	private static Map<String,WQSignDetail> mapmap;
	
	//初始化map
	public static void initialize(){
		if(mapmap == null)mapmap = new HashMap<String,WQSignDetail>();
	}
	
	public static void add(String key,WQSignDetail val){
		if(mapmap.containsKey(key)){
			mapmap.get(key).release();
			mapmap.remove(key);
		}
		mapmap.put(key,val);//没有就创建,有就覆盖
	}
	
	public static void remove(String key){
		//有就移除,没有就无需移除
		if(mapmap.containsKey(key))
			mapmap.remove(key);
	}
	
	//获取内容字符串
	public static String getContent(String k){
		if(!mapmap.containsKey(k))return null;
		return mapmap.get(k).content();
	}
	
	//获取详情
	public static WQSignDetail getDetail(String k){
		if(!mapmap.containsKey(k))return null;
		return mapmap.get(k);
	}
	
	//获取插图富文本列表
	/*public static List<WQSpannableStr> getSpannList(String k){
		if(!mapmap.containsKey(k))return null;
		Map<String,ImgBean> tmap = mapmap.get(k).map();
		if(null == tmap)return null;
		List<WQSpannableStr> iblist = new ArrayList<WQSpannableStr>();
		Iterator<Map.Entry<String,ImgBean>> iterator = tmap.entrySet().iterator();
		Map.Entry<String,ImgBean> entry = null;
		while(iterator.hasNext()){
			entry = iterator.next();
			iblist.add(new WQSpannableStr(entry.getValue().index,entry.getValue().spanstr()));
		}
		return iblist;
	}*/
}
