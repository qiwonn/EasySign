package com.wq.esign;
import android.os.AsyncTask;
import android.content.Context;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.text.TextUtils;
import android.text.SpannableString;
import android.text.Editable;
import java.util.List;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
/** 异步查看详情 */
public class ViewDetailsAsync extends AsyncTask<EditText,Void,EditText>
{
	private IWQFunc<Void> finishedCallback;
	private Context context;
	private int viewWidth;
	private WQSignDetail detail;
	//private SpannableStringBuilder strable;
	public ViewDetailsAsync(Context context,int viewWidth,IWQFunc<Void> callback){
		super();
		this.context = context;
		this.viewWidth = viewWidth;
		finishedCallback = callback;
	}
	@Override
	protected EditText doInBackground(EditText... p1)
	{
		if(SignManager.curSb == null)return null;
		final long key = SignManager.curSb.getmId();//时间戳作为唯一标识
		String skey = String.valueOf(key);
		//获取详情从全局缓存(二级缓存)
		detail = IInsertMapManager.getDetail(skey);
		if(null != detail){
			return p1[0];
		}
		else{
			//没有可使用的全局缓存,则载入缓存文件(三级缓存)
			DiskCacheManager dcm = new DiskCacheManager(context,DiskCacheManager.mVersion,"file");
			String content = dcm.getCacheString(skey);
			//内容是否包含插图
			if(!TextUtils.isEmpty(SignManager.curSb.getaIcon())){
				Editable eta = Editable.Factory.getInstance().newEditable(content);
				//请求插图数据
				SignSQLiteHelper sdb = new SignSQLiteHelper(context,SignSQLiteHelper.DATABASE,null,1);
				List<String[]> alist = sdb.queryInsertImg(key);
				int index; //索引
				String dir;//路径
				Bitmap bmp;//位图
				SpannableString spanstr;
				IInsertAPic apic = new IInsertAPic();
				//List<WQSpannableStr> spanlist = new ArrayList<WQSpannableStr>();
				for(String[] b : alist){
					index = Integer.parseInt(b[1]);
					dir = b[2];
					bmp = BitmapFactory.decodeFile(dir);
					spanstr = apic.getSpannableString(context,bmp,viewWidth,dir);
					if(index <0 || index >= content.length()){
						eta.append(spanstr);
					}else{
						eta.replace(index,index + GlobalConst.IMG.length,spanstr);
					}
					//spanlist.add(new WQSpannableStr(index,spanstr));
					//加入到插图临时缓存
					IInsertMap.INSTANCE.add(b[1],new ImgBean(key,index,dir,null));
				}
				
				/*for(WQSpannableStr a : spanlist){
					if(a.index <0 || a.index >= content.length()){
						eta.append(a.spanstr);
					}else{
						eta.insert(a.index,a.spanstr);
					}
				}*/
				detail = new WQSignDetail(eta);
			}
			else{
				detail = new WQSignDetail(content);
			}
			//添加详情到全局缓存(二级缓存)
			IInsertMapManager.add(skey,detail);
		}
		return p1[0];
	}

	@Override
	protected void onPostExecute(EditText result)
	{
		super.onPostExecute(result);
		if(null != result){
			if(detail != null){
				if(detail.editable() != null){
					result.setText(detail.editable());
				}else if(detail.content() != null){
					result.setText(detail.content());
				}
			}
		}
		//Toast.makeText(context,"Detail ready completed",0).show();
		context = null;
		//strable = null;
		finishedCallback = null;
	}
}
