package com.wq.esign;
import android.content.Context;
import android.content.Intent;
import android.content.ContentResolver;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.Selection;
import android.text.Layout;
import android.text.Spannable;
import java.io.FileNotFoundException;
/** 插入图片 */
public class IInsertAPic
{
	public static int LINE_HEIGHT;
	private static int LINES;
	private static String IMG_STR;
	public IInsertAPic(){}
	
	public String insertByPhoto(Context context,Intent intent,ContentResolver resolver,EditText et){
		//获得图片的uri
		Uri originalUri = intent.getData();
		Bitmap originalBitmap = null;
		try{
			originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
		}
		catch (FileNotFoundException e){
			Toast.makeText(context, "文件没找到", Toast.LENGTH_SHORT).show();
			return null;
		}
		String u = RealPathFromUriUtils.getRealPathFromUri(context,originalUri);
		SpannableString spannableString = getSpannableString(context,originalBitmap,et.getMeasuredWidth(),u);
		if(spannableString != null){
			int index = et.getSelectionStart(); //获取光标所在位置
			Editable edit_text = et.getEditableText();
			edit_text.insert(index,"\n\n");
			//将选择的图片追加到EditText中光标所在位置
			if(index <0 || index >= edit_text.length()){
				edit_text.append(spannableString);
			}else{
				edit_text.insert(++index, spannableString);
			}
			return u;
		}else{
			Toast.makeText(context, "获取图片失败", Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	
	public String insertByCamera(Context context,Intent intent,EditText et){
		Bundle extras = intent.getExtras();
		Bitmap originalBitmap1 = (Bitmap) extras.get("data");
		if(originalBitmap1 != null){
			String s = ImageLruCache.saveImageToGallery(context,originalBitmap1);
			SpannableString spannableString = getSpannableString(context,originalBitmap1,et.getMeasuredWidth(),s);
			if(spannableString != null){
				int index = et.getSelectionStart(); //获取光标所在位置
				Editable edit_text = et.getEditableText();
				edit_text.insert(index,"\n\n");
				//将选择的图片追加到EditText中光标所在位置
				if(index <0 || index >= edit_text.length()){
					edit_text.append(spannableString);
				}else{
					edit_text.insert(++index, spannableString);
				}
				return s;
			}else{
				Toast.makeText(context, "获取图片失败", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(context, "获取图片失败", Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	
	private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //计算宽、高缩放率
        float scanleWidth = (float)newWidth/width;
        float scanleHeight = (float)newHeight/height;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth,scanleHeight);
        //旋转图片 动作
        ///matrix.postRotate(45);
        // 创建新的图片Bitmap
        return Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
    }
	
	public SpannableString getSpannableString(Context context,Bitmap originalBitmap,int vWidth,String u){
		int h = (int)((float)vWidth/originalBitmap.getWidth()*originalBitmap.getHeight());
		Bitmap bitmap = resizeImage(originalBitmap, vWidth, h);
		if(bitmap != null){
			//根据Bitmap对象创建ImageSpan对象
			WQImageSpan imageSpan = new WQImageSpan(context,bitmap,u);
			//创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
			SpannableString spannableString = new SpannableString(String.valueOf(GlobalConst.IMG));
			//用ImageSpan对象替换face
			spannableString.setSpan(imageSpan, 0, GlobalConst.IMG.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			//将选择的图片追加到EditText中光标所在位置
			return spannableString;
		}
		return null;
	} 
	
	
	public static int getCurrentCursorLine(EditText editText) {
        int selectionStart = Selection.getSelectionStart(editText.getText());
        Layout layout = editText.getLayout();
        if (-1 != selectionStart) {
            return layout.getLineForOffset(selectionStart);
        }
        return -1;
    }
	
}
