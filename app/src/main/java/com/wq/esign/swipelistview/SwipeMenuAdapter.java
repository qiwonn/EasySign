package com.wq.esign.swipelistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

public class SwipeMenuAdapter implements WrapperListAdapter,
OnSwipeItemClickListener {

    private ListAdapter mAdapter;
    private Context mContext;
    private IOnMenuItemClickListener onMenuItemClickListener;

    public SwipeMenuAdapter(Context context, ListAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwipeMenuLayout layout = null;
        if (convertView == null) {
            View contentView = mAdapter.getView(position, convertView, parent);
			if(((SwipeListAdapter)mAdapter).isTitle(position)){
				//layout = new SwipeMenuLayout(contentView);
				//layout.setPosition(position);
				return contentView;
			}else{
				SwipeMenuListView listView = (SwipeMenuListView) parent;
				/*layout = new SwipeMenuLayout(contentView,
								new SwipeMenuView(mContext),
								new SwipeController(mContext,listView.getCloseInterpolator(),listView.getOpenInterpolator()));*/
				layout = new SwipeMenuLayout(contentView,listView.getCloseInterpolator(),listView.getOpenInterpolator());
				layout.setPosition(position);
				layout.getMenuView().setOnSwipeItemClickListener(this);
			}
        } else {
			if(((SwipeListAdapter)mAdapter).isTitle(position)){
				return mAdapter.getView(position,convertView,parent);
			}else{
				layout = (SwipeMenuLayout) convertView;
				layout.closeMenu();
				layout.setPosition(position);
				mAdapter.getView(position,layout.getContentView(),parent);
			}
        }
        if (mAdapter instanceof BaseSwipListAdapter) {
            boolean swipEnable = (((BaseSwipListAdapter) mAdapter).getSwipEnableByPosition(position));
            //layout.setSwipeEnable(swipEnable);
			layout.setSwipEnable(swipEnable);
        }
        return layout;
    }

    @Override
    public void onItemClick(SwipeMenuView view, int id) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(),id);
        }
    }

    public void setOnSwipeItemClickListener(
		IOnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return mAdapter;
    }

}
