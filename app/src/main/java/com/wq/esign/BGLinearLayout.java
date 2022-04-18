package com.wq.esign;
import android.widget.LinearLayout;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.widget.EditText;
import android.graphics.Paint;
import android.graphics.Rect;
import android.app.Activity;
/** 自定义作为EditText背景板的线性布局 */
public class BGLinearLayout extends LinearLayout
{
	private Paint paint;
	private Rect r;
	private int left,righ;
	private int tvH,hp;
	private int rowline,lineH;
	private int count;
	private EditText et;
	public BGLinearLayout(Context context,AttributeSet att){
		super(context,att);
		post(new Runnable(){
				@Override
				public void run(){
					left = getPaddingLeft();
					righ = getRight() - getPaddingRight();
					if(null==et)et = findViewById(R.id.hb_cre_edit);
					lineH = et.getLineHeight();
					count = et.getMeasuredHeight()/et.getLineHeight();
					int exh = (int)et.getLineSpacingExtra();
					tvH = ((Activity)getContext()).findViewById(R.id.hb_cre_time).getHeight();
					Paint.FontMetricsInt fmi = et.getPaint().getFontMetricsInt();
					hp = tvH - (((fmi.bottom - fmi.descent) + exh)>>1);
				}
			});
	}
	@Override
    protected void onFinishInflate() {
        super.onFinishInflate();
		int pd = DisplayUtil.dp2px(getContext(),GlobalConst.DP_SID_CONT);
		setPadding(pd,0,pd,0);
		
		et = findViewById(R.id.hb_cre_edit);
		
		r = new Rect();

		paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(0xffe0e0e0);
        //mPaint.setColor(lineColor);
        paint.setStrokeWidth(1);
	}
	@Override
    protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int lc = et.getLineCount()-1;
		if (lc > count){
			for(int i=0;i<lc;i++){
				et.getLineBounds(i,r);
				rowline = r.bottom + hp;
				canvas.drawLine(left, rowline, righ, rowline, paint);
			}
		}
		else{
			rowline = tvH;
			for(int i=0;i<count;i++){
				rowline += lineH;
				canvas.drawLine(left, rowline, righ, rowline, paint);
			}
		}
    }
}
