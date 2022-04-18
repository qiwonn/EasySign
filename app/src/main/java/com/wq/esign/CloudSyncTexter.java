package com.wq.esign;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.app.Activity;
/** 云同步控件 */
public class CloudSyncTexter
{
	private Context context;
	public CloudSyncTexter(Context context){
		this.context = context;
		specifiedTextColorAndClickable();
	}

	/**
	 * 使用SpannableStringBuilder设置点击事件
	 */
	private void specifiedTextColorAndClickable() {
		String str = "点击开启";//context.getResources().getString(R.string.click_open);
		String st2 = "qisu 云服务可实时同步您的易签";//context.getResources().getString(R.string.cloud_tip);
		SpannableStringBuilder spannableSB = new SpannableStringBuilder();
		spannableSB.append(st2).append("\n").append(str);
		int al = spannableSB.length();
		//构造一个文本点击的Span
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View view) {
				Toast.makeText(context.getApplicationContext(),"云同步功能敬请期待",Toast.LENGTH_SHORT).show();
			}
			@Override
			public void updateDrawState(TextPaint tp){
				tp.setUnderlineText(false);
			}
		};
		spannableSB.setSpan(clickableSpan, al - str.length(), al, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		//构造一个改变字体颜色的Span
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xffffa500);
		spannableSB.setSpan(colorSpan, al - str.length(),al, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		TextView textView = ((Activity)context).findViewById(R.id.cloud_sync_tv);
		textView.setText(spannableSB);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
