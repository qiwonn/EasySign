package com.wq.esign;
import java.util.List;
import java.util.ArrayList;
/** 列表数据管理器 */
public class SignManager
{
	private static List<SignBeanX> mSbData;
	private static List<SignBeanX> clearList;
	//当前的item
	public static SignBeanX curSb;
	//置顶的item
	public static SignBeanX topSx;
	private static int topEnd = 0;
	public static int checkcount;
	public static boolean isEdit;
	
	public static void setDatas(List<SignBeanX> sblist){
		mSbData = sblist;
	}
	
	public static List<SignBeanX> getDatas(){
		return mSbData;
	}
	
	public static void simplyAdd(SignBeanX sb){
		if(null==mSbData)return;
		mSbData.add(sb);
		curSb = sb;
	}
	
	/*public static int add(SignBeanX sb)
	{
		if(null==mSbData)return -1;
		SignBeanX s =  null;
		if(mSbData.size()>0){
			for(SignBeanX b : mSbData){
				if(b.getmId()==sb.getmId()){
					s = b;
					break;
				}
			}
		}
		if(null==s){
			mSbData.add(sb);
			curSb = sb;
			return mSbData.size();
		}
		return -1;
	}*/
	
	/*public static void remove(SignBeanX sb)
	{
		if(null==mSbData || mSbData.size()<=0)return;
		SignBeanX s =  null;
		for(SignBeanX b : mSbData){
			if(b.getmId()==sb.getmId()){
				s = b;
				break;
			}
		}
		if(null!=s){
			mSbData.remove(s);
		}
	}*/
	
	public static void insertFirst(SignBeanX sb){
		if(null==mSbData)return;
		if(mSbData.contains(sb))
			mSbData.remove(sb);
		boolean isAdded = false;
		SignBeanX a;
		for(int i=0;i<mSbData.size();i++){
			a = mSbData.get(i);
			if(!a.getzhiTop() && !a.isSession){
				mSbData.add(i,sb);
				isAdded = true;
				break;
			}
		}
		if(!isAdded)mSbData.add(sb);
	}
	
	public static void allChecked(){
		if(mSbData.size()<=0)return;
		for(SignBeanX d : mSbData)
			d.isChecked = true;
	}

	public static void allUnchecked(){
		if(mSbData.size()<=0)return;
		for(SignBeanX d : mSbData)
			d.isChecked = false;
	}

	public static void allEdited(){
		if(mSbData.size()<=0)return;
		for(SignBeanX d : mSbData)
			d.cbvisibility = 0x0;
	}

	public static void noneEdited(){
		if(mSbData.size()<=0)return;
		for(SignBeanX d : mSbData)
			d.cbvisibility = 0x08;
	}
	
	public static List<SignBeanX> getAllChecked_list(){
		List<SignBeanX> li = new ArrayList<SignBeanX>();
		if(mSbData.size()<=0)return li;
		for(SignBeanX d : mSbData)
			if(d.isChecked)li.add(d);

		return li;
	}
	
	public static int getAllItemCountWithoutGroup(){
		int c = 0;
		for(SignBeanX d : mSbData)
			if(!d.isSession)c++;
		return c;
	}

	public static void setClearList(List<SignBeanX> clrlist){
		clearList = clrlist;
	}
	public static int addToClearList(SignBeanX sx){
		if(null == sx || null == clearList)return -1;
		clearList.add(sx);
		return clearList.size();
	}
	
	public static int addAllToClearList(List<SignBeanX> p1){
		if(null==clearList || null==p1)return -1;
		clearList.addAll(p1);
		return clearList.size();
	}
	
	public static List<SignBeanX> clearListClear(){
		if(null==clearList || clearList.size()<=0)return null;
		if(null==mSbData || mSbData.size()<=0)return null;
	
		for(SignBeanX sx : clearList)
			mSbData.remove(sx);

		return clearList;
	}
	
	public static void cleanClear(){
		if(null!=clearList && clearList.size()>0)clearList.clear();
	}
	
	public static int getClearListSize(){
		return null == clearList ? -1 : clearList.size();
	}
	
	public static void setZhiTop(SignBeanX sx){
		topSx = sx;
	}
	
	public static void zhiTop(){
		if(null==topSx)return;
		mSbData.remove(topSx);
		mSbData.add(topEnd,topSx);
		topSx.setzhiTop(true);
		//置顶结束位后移
		if(++topEnd >= mSbData.size()-1)topEnd = mSbData.size()-1;
		topSx = null;
	}
	
	public static void zhiTopCancel(){
		if(null==topSx)return;
		mSbData.remove(topSx);
		mSbData.add(topSx);
		topSx.setzhiTop(false);
		//置顶结束位前移
		if(--topEnd < 0)topEnd = 0;
		topSx = null;
	}
	
	public static void search(String s)
	{
		if(null==mSbData)return;
	}
}
