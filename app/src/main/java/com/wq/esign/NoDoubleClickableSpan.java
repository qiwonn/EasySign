package com.wq.esign;
import android.text.style.ClickableSpan;
import android.view.View;

public abstract class NoDoubleClickableSpan extends ClickableSpan
{
	private long lastMillis = 0;
	@Override
	public void onClick(View p1)
	{
		long curMillis = System.currentTimeMillis();
		if(curMillis - lastMillis > 5000){
			lastMillis = curMillis;
			onNoDoubleClick();
		}
	}
	public abstract void onNoDoubleClick();
}
