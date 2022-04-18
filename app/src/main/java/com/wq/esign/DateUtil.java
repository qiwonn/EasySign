package com.wq.esign;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
/** 日期时间工具类 */
public class DateUtil
{
	public static int year;
	public static int month;
	public static int today;
	public static String todayYMD;
	//初始化今天的日期
	public static void initToday(){
		Calendar CD = Calendar.getInstance();
		year = CD.get(Calendar.YEAR);
		month = CD.get(Calendar.MONTH)+1;
		today = CD.get(Calendar.DATE);
		todayYMD = year+"/"+month+"/"+today;
	}
	/** 
	 * 取得年月日时分格式化字符串
	 * */  
	public static String getYYMMDDHHNNstr()  
	{  
		Calendar CD = Calendar.getInstance();
		return ""+CD.get(Calendar.YEAR)
			+"/"+(CD.get(Calendar.MONTH)+1)
			+"/"+CD.get(Calendar.DATE)
			+" "+CD.get(Calendar.HOUR)
			+":"+CD.get(Calendar.MINUTE);  
	}  
	
	public static String getYY_MM_DD_HHNNstr()  
	{  
		Calendar CD = Calendar.getInstance();
		return CD.get(Calendar.YEAR)
			+" "+(CD.get(Calendar.MONTH)+1)
			+" "+CD.get(Calendar.DATE)
			+" "+CD.get(Calendar.HOUR)
			+":"+CD.get(Calendar.MINUTE);  
	} 
	
	public static String isToday(String tstr){
		String[] t = tstr.split(" ");
		if(t[0].equals(todayYMD)){
			//返回后半部分
			return t[1];
		}else{
			//返回前半部分(除年以外)
			t = t[0].split("/");
			return t[1]+"月"+t[2]+"日";
		}
	}
	
	public static boolean isThisYear(String nyr){
		int i = nyr.indexOf("/");
		if(i<0)return false;
		String jy = String.valueOf(year);
		String ty = nyr.substring(0,i);
		return ty.equals(jy);
	}
	
	public static int parseYear(String dateStr,int index){
		return Integer.parseInt(dateStr.substring(0,index));
	}
}
