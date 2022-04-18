package com.wq.esign;

public abstract class OnNoDoubleClickListener implements android.view.View.OnClickListener
{
	private long lastMillis = 0;
	@Override
	public void onClick(android.view.View p1)
	{
		long curMillis = System.currentTimeMillis();
		if(curMillis - lastMillis > 3000){
			lastMillis = curMillis;
			onNoDoubleClick();
		}
	}
	public abstract void onNoDoubleClick();
}
