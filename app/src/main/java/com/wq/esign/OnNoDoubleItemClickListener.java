package com.wq.esign;
/** 防重复点击列表项监听抽象类 */
public abstract class OnNoDoubleItemClickListener implements android.widget.AdapterView.OnItemClickListener 
{
	private long delay = 5000;
	private long lastMillis = 0;
	@Override
	public void onItemClick(android.widget.AdapterView<?> p1, android.view.View p2, int p3, long p4)
	{
		long curMillis = System.currentTimeMillis();
		if(curMillis - lastMillis > delay){
			lastMillis = curMillis;
			onNoDoubleClick(p2,p3);
		}
	}
	public abstract void onNoDoubleClick(android.view.View p2,int p3);
	public void setDelay(int d){delay = d;}
}
