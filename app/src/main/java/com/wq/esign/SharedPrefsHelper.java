package com.wq.esign;
import android.content.SharedPreferences;
import android.content.Context;
import java.util.Set;
import java.util.HashSet;

public class SharedPrefsHelper
{
	public static void saveItemToSharedPrefs(Context context,SignBeanX sx){
		SharedPreferences msp;
		SharedPreferences.Editor editor;
		msp = context.getSharedPreferences("itemwq_"+sx.getmId(),Context.MODE_PRIVATE);
		editor = msp.edit();//获取编辑对象
		editor.putString("h1",sx.getH1());
		editor.putString("h2",sx.getH2());
		editor.putString("h3",sx.getDateTime());
		editor.putLong("id",sx.getmId());
		editor.putString("key",sx.getKey());
		editor.putString("img",sx.getaIcon());
		editor.commit();
		//顺便(单独)保存id
		msp = context.getSharedPreferences("idwq",Context.MODE_PRIVATE);
		editor = msp.edit();
		Set<String> s = msp.getStringSet("ids",null);
		if(null==s){
			s = new HashSet<String>();
		}
		s.add(String.valueOf(sx.getmId()));
		editor.putStringSet("ids",s);
		editor.commit();
	}

	public static void delItemFromSharedPrefs(Context context,long id){
		SharedPreferences msp;
		SharedPreferences.Editor editor;
		msp = context.getSharedPreferences("itemwq_"+id,Context.MODE_PRIVATE);
		editor = msp.edit();
		editor.clear();
		editor.commit();
		//顺便移除id
		msp = context.getSharedPreferences("idwq",Context.MODE_PRIVATE);
		editor = msp.edit();
		Set<String> s = msp.getStringSet("ids",null);
		if(null!=s && s.size()>0){
			s.remove(String.valueOf(id));
		}
		editor.putStringSet("ids",s);
		editor.commit();
	}
}
