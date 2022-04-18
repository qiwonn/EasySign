package com.wq.esign;
import android.content.Context;
import android.widget.TextView;
import android.app.Activity;
/** 图文详情布局 */
public class TuWenDetLayout
{
	public TuWenDetLayout(Context context){
		onFinishInflate(context);
	}

    private void onFinishInflate(Context context) {
		if(SignManager.curSb != null)
			((TextView)((Activity)context).findViewById(R.id.hb_cre_time)).setText(SignManager.curSb.getDateTime());
    }
}
