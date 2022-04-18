package com.wq.esign;
import android.text.style.ImageSpan;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.graphics.Canvas;
/** 自定义ImageSpan样式 */
public class WQImageSpan extends ImageSpan
{
	private int marleft=0,marrigh=0;
	private String source;
	public WQImageSpan(android.content.Context context, android.graphics.Bitmap b,String source) {
		super(context,b);
		this.source = source;
	}
	public WQImageSpan(android.content.Context context, android.graphics.Bitmap b,String source,int marleft,int marrigh) {
		this(context,b,source);
		this.marleft = marleft;
		this.marrigh = marrigh;
	}
	public String source(){
		return source;
	}
	
	public int getSize(Paint paint, CharSequence text, int start, int end,
					   Paint.FontMetricsInt fm) {
		Drawable d = getDrawable();
		Rect rect = d.getBounds();
		if (fm != null) {
			Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
			int fontHeight4 = (fmPaint.bottom - fmPaint.top)>>2;
			int drHeight2 = (rect.bottom - rect.top)>>1;

			int top = drHeight2 - fontHeight4;
			int bottom = drHeight2 + fontHeight4;

			fm.ascent = -bottom;
			fm.top = -bottom;
			fm.bottom = top;
			fm.descent = top;
		}
		return rect.right;
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end,
					 float x, int top, int y, int bottom, Paint paint) {
		Drawable b = getDrawable();
		canvas.save();
		int transY = (((bottom - top) - b.getBounds().bottom) >> 1) + top;
		canvas.translate(x, transY);
		b.draw(canvas);
		canvas.restore();
	}
}
