package com.wq.esign;
import android.content.Context;
import android.widget.TextView;
import android.app.Activity;
/** 图文创建布局 */
public class TuWenCreLayout
{
	public TuWenCreLayout(Context context){
		onFinishInflate(context);
	}

    private void onFinishInflate(Context context) {
		String dateTime = DateUtil.getYYMMDDHHNNstr();
		((TextView)((Activity)context).findViewById(R.id.hb_cre_time)).setText(dateTime);
    }
}
