package com.wq.esign;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;
import android.app.Activity;
import android.widget.Toast;
/** 自定义下划线文本编辑控件 */
public class LinedEditTextX extends com.hanks.lineheightedittext.LineHeightEditText {

    private Rect mRect;
    private Paint mPaint;

    private float lineTextGap = 1.0f;
    private int lineColor = Color.GRAY;
    private float lineWidth = 1.0f;

	int height;
	int line_height;
	private float height_line;//line_height的倒数
	int extr_height;
	int count;


	int hp;//基于baseline的偏移
	private int scrolly;
	int tvheigh;

	private int baseline;
	int padtop;
	int left;
	int righ;

    public LinedEditTextX(Context context, AttributeSet attrs) {
        super(context, attrs);

		setCursorColor(0xffffa500);

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LinedEditTextX);
			lineTextGap = ta.getFloat(R.styleable.LinedEditTextX_lineTextGap, 1.0f);
			lineColor = ta.getColor(R.styleable.LinedEditTextX_lineColor, Color.GRAY);
			lineWidth = ta.getFloat(R.styleable.LinedEditTextX_lineWidth, 1.0f);
			ta.recycle();
        }

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(0xffe0e0e0);
        //mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(lineWidth);

		post(new Runnable(){
				@Override
				public void run(){
					height = getHeight();
					line_height = getLineHeight();
					extr_height = (int)getLineSpacingExtra();
					height_line = 1.0f/line_height;
					count = (int)(height * height_line);
					left = getPaddingLeft();
					righ = getRight() - getPaddingRight();
					padtop = getPaddingTop();
					tvheigh = ((Activity)getContext()).findViewById(R.id.hb_cre_time).getMeasuredHeight();
					Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
					hp = fmi.descent + (((fmi.leading - fmi.top) + (fmi.bottom - fmi.descent) + extr_height)>>1);
					fmi = getPaint().getFontMetricsInt();
					baseline = getLineBounds(0, mRect);//first line
				}
			});
    }

    @Override
    protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*int scry = scrolly;
		int index = (int)((scry - tvheigh)*height_line);

       	//if (getLineCount() > count)
        //    count = getLineCount();//for long text with scrolling

        Rect r = mRect;
        Paint paint = mPaint;
		//paint.setColor(0xffe0e0e0);//test code
		int offset = index * line_height;
		int basel = r.bottom + offset;
		int bly = baseline + offset + hp;//test code
		int bal = basel + line_height;//test code

        //for (int i=0;i < count;i++) {
        //    canvas.drawLine(left, basel, righ, basel, paint);
        //    basel += line_height;//next line
        //}

		paint.setColor(0xffffa500);//test code
		canvas.drawLine(left, bly, righ, bly, paint);

        //super
		paint.setColor(0xffff0000);//test code
		canvas.drawLine(left, bal, righ, bal, paint);*/
    }

	public void scrollY(int p1){
		scrolly = p1;
	}
}
