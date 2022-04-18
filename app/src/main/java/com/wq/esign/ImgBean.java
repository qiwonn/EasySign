package com.wq.esign;
/** 富文本插图 */
public class ImgBean
{
	public long id;
	public int index = -1;
	public String value = "";
	private android.text.SpannableString spanstr;
	public ImgBean(long id,int index,String value,android.text.SpannableString p3){
		this.id = id;
		this.index = index;
		this.value = value;
		this.spanstr = p3;
	}
	
	public android.text.SpannableString spanstr(){
		return spanstr;
	}
	
	public String[] toStrArray(){
		return new String[]{String.valueOf(id),String.valueOf(index),value};
	}
	
	public String[] toStrArrayLid(long lid){
		return new String[]{String.valueOf(lid),String.valueOf(index),value};
	}
}
