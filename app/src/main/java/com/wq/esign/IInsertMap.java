package com.wq.esign;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
/** 插图 */
public enum IInsertMap
{
	INSTANCE {
		
		//<索引,富文本插图>
		@Override
		public Map<String,ImgBean> getMap()
		{
			return mp;
		}
		
		@Override
		public void add(String k,ImgBean v)
		{
			mp.put(k,v);
		}
		
		@Override
		public void clear()
		{
			if(null!=mp && mp.size()>0)mp.clear();
		}
		
		@Override
		public int size(){
			return mp.size();
		}

		@Override
		public String getFistInsert()
		{
			if(mp.size()<=0)return "";
			Iterator<Map.Entry<String,ImgBean>> iterator = mp.entrySet().iterator();
			ImgBean ib = null;
			int index = Integer.MAX_VALUE;
			while(iterator.hasNext()){
				ImgBean tb = iterator.next().getValue();
				if(tb.index < index){
					index = tb.index;
					ib = tb;
				}
			}
			return (ib==null ?"":ib.value);
		}

		@Override
		public List<String[]> toStrArrayList(long id)
		{
			if(mp.size()<=0)return null;
			List<String[]> iblist = new ArrayList<String[]>();
			Iterator<Map.Entry<String,ImgBean>> iterator = mp.entrySet().iterator();
			Map.Entry<String,ImgBean> entry = null;
			while(iterator.hasNext()){
				entry = iterator.next();
				iblist.add(entry.getValue().toStrArrayLid(id));
			}
			return iblist;
		}
	};

	//<索引,富文本插图>
	private static Map<String,ImgBean> mp = new HashMap<String,ImgBean>();
	public abstract Map<String,ImgBean> getMap();
	public abstract void add(String k,ImgBean v);
	public abstract void clear();
	public abstract int size();
	public abstract String getFistInsert();
	public abstract List<String[]> toStrArrayList(long id);
}
