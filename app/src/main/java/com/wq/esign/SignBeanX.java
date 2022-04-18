package com.wq.esign;
import android.text.SpannableStringBuilder;
/** 签 */
public class SignBeanX
{
	private long mId;
	private String h1;
	private String h2;
	private SpannableStringBuilder h3;
	private String content;
    private String aIcon;
	private String key;
	private String dtm;
	private boolean isTop;
	//控件是否可见
	public int ivvisibility;
	public int cbvisibility;
	//是否选中
	public boolean isChecked;
	//session 是否是分组栏
	public short year;
	public byte month;
	public byte day;
	public byte hour;
	public byte minute;
	public boolean isSession;
    public SignBeanX(long lid) {
		this(lid,"","","","","");
	}
    public SignBeanX(long lid,String h1,String h2,String h3, String aIcon,String key) {
		this.mId = lid;
        this.h1 = h1;
		this.h2 = h2;
		this.h3 = null;
        this.aIcon = aIcon;
		this.key = key;
		this.isTop = false;
		//visibility
		ivvisibility = 0x08;
		cbvisibility = 0x08;
		//session
		isSession = false;
		isChecked = false;
    }

	public long getmId(){
		return mId;
	}
    public String getH1() {
        return h1;
    }

    public String getH2() {
        return h2;
    }
	
	public SpannableStringBuilder getH3(){
		return h3;
	}
	
    public String getaIcon() {
        return aIcon;
    }

	public String getKey() {
        return key;
    }

	public boolean getzhiTop(){
		return isTop;
	}
	
	public String getDateTime(){
		return dtm;
	}
	
	public void setDateTime(String v){
		dtm = v;
	}
	
	public void setzhiTop(boolean b){
		isTop = b;
	}
	
	public String setKey(String key) {
        return this.key = key;
    }

    public void setH1(String s) {
        h1 = s;
    }

    public void setH2(String s) {
        h2 = s;
    }
	
	public void setH3(SpannableStringBuilder s) {
        h3 = s;
    }
	
    public void setaIcon(String aIcon) {
        this.aIcon = aIcon;
    }

	public String[] toStrArray(){
		return new String[]{String.valueOf(mId),h1,h2,dtm,aIcon,key};
	}

	@Override
	public String toString()
	{
		StringBuilder bu = new StringBuilder("{");
		bu.append("\nid：").append(mId)
		.append("\nh1：").append(h1)
		.append("\nh2：").append(h2)
		.append("\nh3：").append(dtm)
		.append("\nimg：").append(aIcon)
		.append("\nkey：").append(key).append("\n}\n\n");
		return bu.toString();
	}
}
