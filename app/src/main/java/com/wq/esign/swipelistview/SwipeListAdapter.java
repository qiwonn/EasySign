package com.wq.esign.swipelistview;
import java.util.List;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Environment;
import android.text.TextUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import com.wq.esign.SignBeanX;
import com.wq.esign.WQThumbNailManager;
import com.wq.esign.R;
/** 列表视图适配器 */
public final class SwipeListAdapter extends BaseSwipListAdapter
{
	/**
     * Item类型,int值(必须从0开始依次递增).
     */
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;
    /**
     * Item Type 的数量
     */
    private static final int TYPE_ITEM_COUNT = 2;
    private List<SignBeanX> mData;
    private Context mContext;
	private LayoutInflater inflater;
    public SwipeListAdapter(List<SignBeanX> mData, Context context) {
        this.mData = mData;
        this.mContext = context;
		inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return (mData == null || mData.size() <= 0) ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
	public boolean isEnabled(int position)
	{
		if(mData.get(position).isSession)
            return false;
        
		return super.isEnabled(position);
	}

	@Override
    public int getItemViewType(int position) {
		if(mData.get(position).isSession)
            return TYPE_TITLE;
        else 
            return TYPE_CONTENT;
    }

	@Override
    public int getViewTypeCount() {
        return TYPE_ITEM_COUNT;
    }

	@Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if(getItemViewType(i) == TYPE_CONTENT)
			convertView = getContentView(i,convertView);
		else 
			convertView = getTitleView(i,convertView);
	
        return convertView;
    }

	public View getContentView(int i, View convertView){
		ViewHolder vHolder = null;
        //当Item view为空时才创建
        if (convertView == null) {
            //通过反射的方法加载布局,并绑定对象到Tag
            convertView = inflater.inflate(R.layout.sign_tem_x, null);
            vHolder = new ViewHolder(mContext,convertView);
            convertView.setTag(vHolder); // 标记
        } else { 
			//获取Tag绑定的对象
			if(null!=convertView.getTag()){
				vHolder = (ViewHolder) convertView.getTag();
			}else{
				//重新绑定
				vHolder = new ViewHolder(mContext,convertView);
				convertView.setTag(vHolder); 
			}
        }
		//填充内容
		SignBeanX sb = mData.get(i);
		//注：ArrayList允许添加引用为null的元素
		if(null==sb)
			return convertView;
		
		if(TextUtils.isEmpty(sb.getaIcon())){
			vHolder.iv.setVisibility(View.GONE);
		}else{
			Drawable dra = WQThumbNailManager.get(String.valueOf(sb.getmId()));
			if(null != dra)vHolder.iv.setImageDrawable(dra);
			vHolder.iv.setVisibility(View.VISIBLE);
		}
        vHolder.tv.setText(sb.getH3());
		vHolder.chk.setVisibility(sb.cbvisibility);
		vHolder.chk.setBackground(sb.isChecked ? vHolder.dc1 : vHolder.dc0);
		return convertView;
	}

	//取得分组视图
	private View getTitleView(int i, View convertView){
		ViewHolder2021 viewHolder = null;
        //当view为空时才创建
        if (convertView == null) {
            //通过反射的方法加载布局,并绑定对象到Tag
            convertView = inflater.inflate(R.layout.sign_tem_x_index, null);
            viewHolder = new ViewHolder2021();
            viewHolder.tv = convertView.findViewById(R.id.tem_grouped);
            convertView.setTag(viewHolder);
        } else { 
			//获取Tag绑定的对象
			if(null!=convertView.getTag()){
				viewHolder = (ViewHolder2021) convertView.getTag();
			}else{
				viewHolder = new ViewHolder2021();
				viewHolder.tv = convertView.findViewById(R.id.tem_grouped);
				convertView.setTag(viewHolder); 
			}
        }
		//填充内容
		SignBeanX sb = mData.get(i);
		//注：ArrayList允许添加引用为null的元素
		if(null==sb){
			viewHolder.tv.setText("😊");
			return convertView;
		}
		viewHolder.tv.setText(sb.getH1());
		return convertView;
	}

	public void setDatas(List<SignBeanX> xlist){
		if(null==xlist)return;
		mData = xlist;
	}
	
	//是分组标题吗
	public boolean isTitle(int i){
		return mData.get(i).isSession;
	}
	
	public final class ViewHolder {
		public ImageView chk;
        public TextView tv;
        public ImageView iv;
		public Drawable dc0;
		public Drawable dc1;
		public ViewHolder(Context context,View v){
			chk = v.findViewById(R.id.tem_chk);
            tv = v.findViewById(R.id.tem_h1);
            iv = v.findViewById(R.id.tem_img);
			dc0 = context.getResources().getDrawable(R.drawable.uncheck);
			dc1 = context.getResources().getDrawable(R.drawable.check);
		}
    }

	public final class ViewHolder2021 {
        public TextView tv;
    }
}
