package com.wq.esign;
import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;
/** 主界面 */
public class MainActivity extends Activity 
{
	private MsgEventProcess meprocess;
	private final Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			meprocess.procMsgEvent(MainActivity.this,msg.what);
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(0xffffffff);//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        }
        setContentView(R.layout.main);
		initialized();
		//注册该页面为订阅者
        EventBus.getDefault().register(this);
    }
	
	@Override
    protected void onPause() {
        super.onPause();
        //同步 DiskCache 的缓存日志
        //DiskCacheManager.flush();
    }
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        //EventBus.getDefault().unregister(MainActivity.class);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			if(requestCode == GlobalConst.SIGN_COMPLETE){
				//int a = intent.getIntExtra("act",-1);
				//onSignComplete(a);
			}
		}
	}
	
	
	
	private ListView signlv;
	//粘性事件sticky = true
	@Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
	public void onReciverItemEvent(Integer event) {
		if(!(event instanceof Integer))return;
		//Toast.makeText(this,"MainActivity 事件巴士 Action="+event,0).show();
		if(null!=mHandler)mHandler.sendEmptyMessage(event.intValue());
	}
	
	private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
	
	private void initialized(){
		//初始化显示测量工具
		DisplayUtil.initMeasure(this.getApplicationContext());
		DisplayUtil.initTextPaint(this,15);
		//初始化今天的日期静态数据
		DateUtil.initToday();
		//初始化插图管理器
		IInsertMapManager.initialize();
		//初始化缩略图管理器
		WQThumbNailManager.initialize();

		new TitleLayout(this);
		new Searcher(this);
		new CloudSyncTexter(this);
		new NavFoot(this);
		//app版本号
		DiskCacheManager.mVersion = getAppVersion(this);
		//列表控件
		SwipeListView listview = new SwipeListView(this);
		meprocess = new MsgEventProcess(listview.getAdapter());
		//异步读取缓存到列表(注：不可在UI线程进行耗时的操作,否则闪退)
		if(SignManager.getDatas() == null)
			new QueryItemDataAsync(getApplicationContext(),null).execute(listview.getAdapter());
	}
}
