package com.wq.esign;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.util.AttributeSet;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.method.LinkMovementMethod;
import android.text.Spannable;
import android.text.Editable;
import android.text.TextUtils;
import android.app.Activity;
import android.view.View;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.greenrobot.eventbus.EventBus;
/** 创建标题栏布局 */
public class TitleSignCreLayout {
	private TextView tvcompl;
	private TextView tvsign;
	private Context context;
	private boolean isCreate;
    public TitleSignCreLayout(Context context,boolean isCreate) {
		this.context = context;
		this.isCreate = isCreate;
		onFinishInflate();
    }

    private void onFinishInflate() {
		((Activity)context).findViewById(R.id.title_ret_cre).setOnClickListener(new OnNoDoubleClickListener(){
				@Override
				public void onNoDoubleClick(){
					archiving(true);
				}
			});
			
		tvsign = ((Activity)context).findViewById(R.id.title_sign_cre);
		tvsign.setText("创建易签");
		
		tvcompl = ((Activity)context).findViewById(R.id.title_comp_cre);
		tvcompl.setOnClickListener(new OnNoDoubleClickListener(){
				@Override
				public void onNoDoubleClick(){
					archiving(false);
					if(isCreate)
						((SignCreActivity)context).setDefaultStatus();
					else
						((SignDetActivity)context).setDefaultStatus();
				}
			});
		tvcompl.setText("完成");
		
		///specifiedTextColorAndClickable();
    }
	
	/**
	 * 使用SpannableStringBuilder设置点击事件
	 */
	/*private void specifiedTextColorAndClickable() {
		String str = context.getResources().getString(R.string.sign_des);
		SpannableStringBuilder spannableSB = new SpannableStringBuilder();
		spannableSB.append(str);
		NoDoubleClickableSpan clickableSpan = new NoDoubleClickableSpan() {
			@Override
			public void onNoDoubleClick(){
				archiving(true);
			}
			@Override
			public void updateDrawState(TextPaint tp){
				tp.setUnderlineText(false);
			}
		};
		spannableSB.setSpan(clickableSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		//构造一个改变字体颜色的Span
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xffffa500);
		spannableSB.setSpan(colorSpan, 0,1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		TextView textView = ((Activity)context).findViewById(R.id.title_sign_cre);
		textView.setText(spannableSB);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}*/
	
	private SignBeanX xp;
	public void archiving(final boolean isGoBack){
		EditText et = ((Activity)context).findViewById(R.id.hb_cre_edit);
		final String text = et.getText().toString();
		//内容为不为空
		if(!TextUtils.isEmpty(text)){
			if(null==xp){
				if(isCreate){
					xp = new SignBeanX(System.currentTimeMillis());
					SignManager.curSb = xp;
				}
				else xp = SignManager.curSb;
			}
			final long id = xp.getmId();
			//添加插图
			String nowtext = addInsertImg(et,id);
			//获取硬盘缓存
			DiskCacheManager dcm = new DiskCacheManager(context.getApplicationContext(),DiskCacheManager.mVersion,"file");
			String cstr = dcm.getCacheString(String.valueOf(id));
			if(TextUtils.isEmpty(cstr)){
				Toast.makeText(context.getApplicationContext(),"正在存档"+id,0).show();
				//存档-异步
				archivingAsync(et,xp,isGoBack,false);
			}else{
				//数据库插图数据
				SignSQLiteHelper sdb = new SignSQLiteHelper(context.getApplicationContext(),SignSQLiteHelper.DATABASE,null,1);
				List<String[]> il = sdb.queryInsertImg(id);
				if(null!=il){
					if(il.size()>1){
						Collections.sort(il, new Comparator<String[]>(){
								@Override
								public int compare(String[] p1, String[] p2){
									return p1[1].compareTo(p2[1]);
								}
							});
					}
					String[] ts;
					for(int i=0;i<il.size();i++){
						ts = il.get(i);
						cstr += "\n" + ts[1] + ts[2];
					}
				}
				//MD5数字签名验证内容有没有修改
				String nowmd5key = DiskCacheManager.hashKeyForDisk(nowtext);
				String lasmd5key = DiskCacheManager.hashKeyForDisk(cstr);
				if(nowmd5key.equals(lasmd5key)){
					//没做任何修改
					Toast.makeText(context.getApplicationContext(),"内容未做修改",0).show();
					if(isGoBack){
						//二级缓存(全局)
						IInsertMapManager.add(String.valueOf(id),new WQSignDetail(Editable.Factory.getInstance().newEditable(et.getEditableText())));
						context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(),MainActivity.class));
						((Activity)context).overridePendingTransition(R.anim.back_enter,R.anim.back_exit);
					}
				}else{
					Toast.makeText(context.getApplicationContext(),"内容已修改",0).show();
					//存档-异步
					archivingAsync(et,xp,isGoBack,true);
				}
				///FileUtil.str2File(nowtext+"\n\n"+cstr,"/aplayerlog/nowtext.txt");
			}
		}
		else{
			//内容清空即删除
			SignManager.addToClearList(SignManager.curSb);
			context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(),MainActivity.class));
			((Activity)context).overridePendingTransition(R.anim.back_enter,R.anim.back_exit);
			EventBus.getDefault().post(Integer.valueOf(GlobalConst.ACTION_REMOVE));
		}
		if(null!=et && et.hasFocusable())
			SoftKeyboardManager.hideKeyboard((Activity)context);
	}
	
	private String addInsertImg(EditText et,long id){
		//一级缓存(临时) 清空
		IInsertMap.INSTANCE.clear();
		StringBuilder nowtext = new StringBuilder(et.getText().toString());
		//字符串测试代码
		//StringBuilder bu = new StringBuilder();
		Editable etable = et.getEditableText();
		//获取插图Span数组
		WQImageSpan[] spans = etable.getSpans(0,etable.toString().length(),WQImageSpan.class);
		//bu.append(spans.length).append("张图\n");//test
		if(null!=spans && spans.length>0){
			List<String> slist = new ArrayList<String>();
			int index;
			for(WQImageSpan g : spans){
				index = etable.getSpanStart(g);
				//bu.append(index).append(" ").append(g.source().substring(28)).append("\n");
				slist.add(String.valueOf(index)+g.source());
				//一级缓存(临时) 添加
				IInsertMap.INSTANCE.add(String.valueOf(index),new ImgBean(id,index,g.source(),null));
			}
			//升序
			Collections.sort(slist, new Comparator<String>(){
					@Override
					public int compare(String p1, String p2){
						return p1.compareTo(p2);
					}
				});
			for(int i=0;i<slist.size();i++){
				nowtext.append("\n").append(slist.get(i));
			}
		}
		return nowtext.toString();
	}
	
	private void archivingAsync(final EditText et,SignBeanX xp,final boolean isGoBack,boolean isUpdate){
		final long id = xp.getmId();
		new ArchivingAsync(context,et.getEditableText().toString(), new IWQFunc<Void>(){
				@Override
				public void invoke(Void o1){
					if(isGoBack){
						//二级缓存(全局)
						IInsertMapManager.add(String.valueOf(id),new WQSignDetail(Editable.Factory.getInstance().newEditable(et.getEditableText())));
						context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(),MainActivity.class));
						((Activity)context).overridePendingTransition(R.anim.back_enter,R.anim.back_exit);
						if(isCreate)EventBus.getDefault().postSticky(Integer.valueOf(GlobalConst.ACTION_ADD));
						else EventBus.getDefault().postSticky(Integer.valueOf(GlobalConst.ACTION_MODIFY));
					}
				}
			},isUpdate).execute(xp);
	}
	
	public void showDetLayout(){
		tvcompl.setVisibility(View.INVISIBLE);
		tvsign.setText("易签详情");
	}
	
	public void showModLayout(){
		tvcompl.setVisibility(View.VISIBLE);
		tvsign.setText("修改易签");
	}
}
