package com.wq.esign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Environment;
import android.widget.ImageView;
import android.util.LruCache;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.Map;
import java.util.concurrent.Executors;
import java.io.File;
import android.graphics.drawable.*;
import android.content.res.*;
import java.io.*;
import android.net.*;
import android.content.*;
import android.provider.*;

public class ImageLruCache {
    private static LruCache<String, Bitmap> mCaches;

    /**
     * 定义上下文对象
     */
    private Context mContext;

    //private static Handler mHandler;

    //声明线程池,全局只有一个线程池,所有访问网络图片，只有这个池子去访问。
    //private static ExecutorService mPool;

    //解决错位问题，定义一个存标记的集合
    private Map<ImageView, String> mTags = new LinkedHashMap<ImageView, String>();

    public ImageLruCache(Context context) {
        this.mContext = context;
        initializedCaches(context);
        /*if (mHandler == null) {
            //实例化Handler
            mHandler = new Handler();
        }*/

        //if (mPool == null) {
            //创建固定大小的线程池
        //    mPool = Executors.newFixedThreadPool(3);
            //创建一个缓存的线程池,生产者和消费者，一个线程生产，必须得消费完成后再生产
            /*Executors.newCachedThreadPool();
			 Executors.newSingleThreadExecutor();//创建一个单线程池
			 Executors.newScheduledThreadPool();//创建一个计划的任务池*/
        //}
    }

	public static void initializedCaches(Context context){
		if (mCaches == null) {
            //申请内存空间
            int maxSize = (int) (Runtime.getRuntime().freeMemory() / 4);
            //实例化LruCache
            mCaches = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    //判断添加进入的value的占用内存的大小
                    //这里默认sizeOf是返回1，不占用，内存会不够用，所以要给它一个具体占用内存的大小
                    //return super.sizeOf(key, value);
                    //获取Bitmap的大小
                    return value.getRowBytes() * value.getHeight();
                }
            };
        }
	}
	
    /**
     * 给imageView加载url对应的图片
     *
     * @param iv
     * @param url
     */
    public static void display(ImageView iv, String url) {
        System.out.println("加载url对应的图片：" + url);
        //1.从内存中获取
        Bitmap bitmap = mCaches.get(url);
        if (bitmap != null) {
            //内存中有，显示图片
            iv.setImageBitmap(bitmap);
            System.out.println("加载url内存图片：" + url);
            return;
        }

        //2.内存中没有，从本地获取
        bitmap = loadFromLocal(url);
        if (bitmap != null) {
            //本地有,显示
            iv.setImageBitmap(bitmap);
            return;
        }
    }

    /**
     * 从本地获取图片
     *
     * @param url
     * @return bitmap
     */
    private static Bitmap loadFromLocal(String url) {
        //本地需要存储路径
        //File file = getCacheFile(url);
        File file = new File(url);
        if (file.exists()) {
            //本地有
            //把文件解析成Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            //存储到内存
            mCaches.put(url, bitmap);

            return bitmap;
        }

        return null;
    }

    /**
     * 获取缓存文件路径(缓存目录)
     *
     * @return 缓存的文件
     */
    private File getCacheFile(String url) {
        //获取当前的状态,Environment是环境变量
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //挂载状态，sd卡存在
            File dir = new File(Environment.getExternalStorageDirectory(),
								"/Android/data/" + mContext.getPackageName() + "/icon");
            if (!dir.exists()) {
                //文件不存在,就创建
                dir.mkdirs();
            }
            //此处的url可能会很长，一般会使用md5加密
            return new File(dir, url);
        } else {
            File dir = new File(mContext.getCacheDir(), "/icon");
            if (!dir.exists()) {
                //文件不存在,就创建
                dir.mkdirs();
            }
            return new File(dir, url);
        }
    }

	public static Bitmap getSmallFitSampleBitmap(String file_path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 32;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file_path, options);
	}
	
	public static Bitmap getTinyFitSampleBitmap(String file_path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file_path, options);
		int inSampleSize = 1;
		if (options.outWidth > 32 && options.outHeight > 32) {
			int tmpw = Math.min(options.outWidth, options.outHeight);
			inSampleSize = Math.round((float) tmpw * 0.03125f);
		}
		else if(options.outWidth > 32){
			inSampleSize = Math.round((float) options.outWidth * 0.03125f);
		}
		else if(options.outHeight > 32){
			inSampleSize = Math.round((float) options.outHeight * 0.03125f);
		}
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file_path, options);
	}
	
	public static Bitmap getFitSampleBitmap(String file_path, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file_path, options);
		options.inSampleSize = getFitInSampleSize(width, height, options);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file_path, options);
	}
	public static int getFitInSampleSize(int reqWidth, int reqHeight, BitmapFactory.Options options) {
		int inSampleSize = 1;
		if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
			int widthRatio = Math.round((float) options.outWidth / (float) reqWidth);
			int heightRatio = Math.round((float) options.outHeight / (float) reqHeight);
			inSampleSize = Math.min(widthRatio, heightRatio);
		}
		return inSampleSize;
	}
	
	public static Drawable bitmapToDrawable(Resources resources, Bitmap bm) {
        Drawable drawable = new BitmapDrawable(resources, bm);
        return drawable;
    }
	
	//保存图像到相册
    public static String saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
			// 插入图库
			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return file.getPath();
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
