package com.wq.esign;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.text.TextUtils;
import android.text.TextPaint;
import android.util.TypedValue;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableStringBuilder;
import android.text.Spannable;
import android.graphics.Paint;
import android.text.TextPaint;
/** 显示工具 */
public class DisplayUtil {
	public static int tit_bar_heigh;
	public static int tv_width_long;
	public static int tv_width_shor;
	public static int screen_width;
	public static int EDITTEXT_WIDTH;

	public static TextPaint tpaint;
	
	public static void initMeasure(Context context){
		Resources res = context.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		//屏幕宽度
		screen_width = dm.widthPixels;
		//像素密度
		float scale = dm.density;
		float dp1 = (GlobalConst.DP_SID_CONT<<1);
		float dp2 = dp1 + GlobalConst.DP_IMG_WIDT + GlobalConst.DP_SID_CONT + 5;
		tv_width_long = screen_width - ((int) (dp1 * scale + 0.5f));
		tv_width_shor = screen_width - ((int) (dp2 * scale + 0.5f));
		//标题栏高度
		tit_bar_heigh = (int)(scale * GlobalConst.DP_TIT_BART + 0.5f);
		//ET宽度
		int px3 = -(int)(scale * GlobalConst.DP_SID_CONT + 0.5f);
		EDITTEXT_WIDTH = screen_width + (px3<<1);
		res = null;
	}

	public static void initTextPaint(Context context,int textSize){
		Paint paint = new Paint();
		paint.setTextSize(DisplayUtil.sp2px(context,textSize));
		tpaint = new TextPaint(paint);
	}
	
	public static SpannableStringBuilder appendEllipsizeStr(String p1,String p2,String p3,TextPaint p4,int p5){
		SpannableStringBuilder bu = new SpannableStringBuilder();
		if(p1.length()>12){
			p1 = TextUtils.ellipsize(p1,p4,p5,TextUtils.TruncateAt.END).toString();
			bu.append(p1.substring(0,p1.length()-2)).append("…\n");
		}else{
			bu.append(p1).append("…\n");
		}
		ForegroundColorSpan hintspan = new ForegroundColorSpan(GlobalConst.COLOR_HINT);
		if(TextUtils.isEmpty(p2)){
			bu.append(p3);
			bu.setSpan(hintspan,bu.length()-p3.length(),bu.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			return bu;
		}
		if(p2.length()>12){
			p2 = TextUtils.ellipsize(p2,p4,p5,TextUtils.TruncateAt.END).toString();
			bu.append(p2.substring(0,p2.length()-2)).append("…\n");
		}
		bu.append(p3);
		bu.setSpan(hintspan,bu.length()-(p2.length()+p3.length()),bu.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return bu;
	}

	public static String appendEllipsizeHtmlStr(String p1,String p2,String p3,TextPaint p4,int p5){
		StringBuilder bu = new StringBuilder();
		if(p1.length()>12){
			p1 = TextUtils.ellipsize(p1,p4,p5,TextUtils.TruncateAt.END).toString();
			bu.append(p1.substring(0,p1.length()-2)).append("…\n");
		}else{
			bu.append(p1).append("…\n");
		}
		if(TextUtils.isEmpty(p2)){
			return bu.append("<font color=\"#c0c0c0\">").append(p3).append("</font>").toString();
		}
		if(p2.length()>12){
			p2 = TextUtils.ellipsize(p2,p4,p5,TextUtils.TruncateAt.END).toString();
			bu.append("<font color=\"#c0c0c0\">").append(p2.substring(0,p2.length()-2)).append("…</font><br></br>");
		}
		bu.append("<font color=\"#c0c0c0\">").append(p3).append("</font>");
		FileUtil.str2File(bu.toString(),"/aplayerlog/spannablestringbuilder_htmlstr.txt");
		return bu.toString();
	}

    /**
     * convert px to its equivalent dp
     *
     * 将px转换为与之相等的dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale =  context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * convert dp to its equivalent px
     *
     * 将dp转换为与之相等的px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * convert px to its equivalent sp
     *
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     *
     * 将sp转换为px
     */
    public static int sp2px(Context context,int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
											   context.getResources().getDisplayMetrics());
    }
}
