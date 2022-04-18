package com.wq.esign;
import java.util.Map;
import java.util.HashMap;
import android.graphics.drawable.Drawable;
/** 缩略图管理器 */
public class WQThumbNailManager
{
	//<时间戳,Drawable>
	private static Map<String,Drawable> mp;
	public static void initialize(){
		mp = new HashMap<String,Drawable>();
	}
	
	public static Drawable get(String k){
		return mp.get(k);
	}
	
	public static void add(String k,Drawable v){
		mp.put(k,v);
	}
	
	public static void remove(String k){
		if(mp.containsKey(k))mp.remove(k);
	}
}
