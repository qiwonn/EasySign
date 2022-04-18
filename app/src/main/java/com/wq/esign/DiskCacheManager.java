package com.wq.esign;
import com.jakewharton.disklrucache.DiskLruCache;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
/** 硬盘缓存管理器 */
public class DiskCacheManager
{
	public static int mVersion = 1;
	private DiskLruCache mDiskLruCache = null;
	private DiskLruCache.Editor mEditor = null;
    private DiskLruCache.Snapshot mSnapshot = null;
	
	public DiskCacheManager(Context context, int version,String wqfolderName){
		openCache(context,version,wqfolderName);
	}
	
	private void openCache(Context context, int version,String wqfolderName)
	{
		try {
			File cacheDir = getDiskCacheDir(context, wqfolderName);
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			mDiskLruCache = DiskLruCache.open(cacheDir, version, 1, 10 * 1024 * 1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static File getDiskCacheDir(Context context, String uniqueName)
	{
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
     * 获取缓存 editor
     *
     * @param key 缓存的key
     * @return editor
     * @throws IOException
     */
    private DiskLruCache.Editor edit(String key) throws IOException {
        if (mDiskLruCache != null) {
            mEditor = mDiskLruCache.edit(key);
        }
        return mEditor;
    }

    /**
     * 根据 key 获取缓存缩略
     *
     * @param key 缓存的key
     * @return Snapshot
     */
    private DiskLruCache.Snapshot snapshot(String key) {
        if (mDiskLruCache != null) {
            try {
                mSnapshot = mDiskLruCache.get(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mSnapshot;
    }
	
	//移除(指定)缓存
	public void removeCache(String key)
	{
		try {
			mDiskLruCache.remove(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//计算MD5
	public static String hashKeyForDisk(String wqKey)
	{
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(wqKey.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(wqKey.hashCode());
		}
		return cacheKey;
	}

	//字节数组转十六进制字符串
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		String hex;
		int i = -1;
		for (; ++i < bytes.length;) {
			hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	//(清空)缓存
	public void clearCache() {
		try {
			//delete()方法内部会调用close()
			mDiskLruCache.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveCacheStringSync(String key,String value) {
		BufferedWriter writer = null;
		DiskLruCache.Editor editor = null;
		try {
			editor = mDiskLruCache.edit(key);
			OutputStream os = editor.newOutputStream(0);
            writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(value);
            editor.commit();
			//here saveCache done,the cache is ready now
		} catch (IOException e) {
			e.printStackTrace();
			try {
                if (editor != null)
                    editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
		}finally {
			try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
	}

    /**
     * 存入byte数组
     *
     * @param key   cache'key
     * @param bytes bytes to save
     */
    public void saveCacheBytes(String key,byte[] bytes) {
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = edit(key);
            if (editor == null) {
                return;
            }
            out = editor.newOutputStream(0);
            out.write(bytes);
            out.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	/**
     * 获取缓存的 byte 数组
     *
     * @param key cache'key
     * @return bytes
     */
    public byte[] getCacheBytes(String key) {
        byte[] bytes = null;
        InputStream inputStream = getCacheInputStream(key);
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[256];
        int len = 0;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
	
	/**
     * 获取 缓存数据的 InputStream
     *
     * @param key cache'key
     * @return InputStream
     */
    public InputStream getCacheInputStream(String key) {
        DiskLruCache.Snapshot snapshot = snapshot(key);
        if (snapshot == null) 
            return null;
        return snapshot.getInputStream(0);
    }
	
	//获取文本类型缓冲
	public String getCacheString(String key){
		InputStream is = getCacheInputStream(key);
		if(null == is)return null;
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		try{
			while ((len = is.read(buf)) != -1){
				result.write(buf,0,len);
			}
			return result.toString(StandardCharsets.UTF_8.name());
		}catch (IOException e){}
		return "";
	}
	/*public String getCacheString(String key){
		InputStream is = getCacheInputStream(key);
		if(null == is)return "";
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuilder buffer = new StringBuilder();
			String line;
			if((line = in.readLine()) != null){
				buffer.append(line);
			}
			while ((line = in.readLine()) != null) {
				buffer.append("\n").append(line);
			}
			return buffer.toString();
		}catch(IOException e){}
        return null;
	}*/

    /**
     * 同步记录文件
     */
    public void flush() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	/************************************************************************
     **********************  缓存网络图 分割线  ****************************
     ************************************************************************/
	public void saveCacheFromURLAsync(final String imageUrl) {
		new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String key = hashKeyForDisk(imageUrl);
						DiskLruCache.Editor editor = null;

						editor = mDiskLruCache.edit(key);
						if (editor != null) {
							OutputStream outputStream = editor.newOutputStream(0);
							if (downloadUrlToStream(imageUrl, outputStream)) {
								editor.commit();
							} else {
								editor.abort();
							}
						}
						//不该频繁的flush
						mDiskLruCache.flush();
						//saveCache done,the bitmap is ready now
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
	}

	public boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void readCacheToImageView(ImageView imageView,String imageUrl) {
		//读取缓存
		try {
			DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKeyForDisk(imageUrl));
			if (snapshot != null) {
				InputStream is = snapshot.getInputStream(0);
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				imageView.setImageBitmap(bitmap);
			} else {
				imageView.setImageBitmap(null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeCache()
	{
		try {
			//关闭DiskLruCache,与open对应
			mDiskLruCache.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*public void readItemCaches(File f,LinkedList<SignBeanX> xlist){
		InputStream is = null;
		BufferedReader in = null;
		String img = "",h1="",h2="",h3="";
		String line ="";
		long id = -1;
		String[] sa = f.list();
		String key = "";
		int i=-1;
		while(++i<sa.length){
			if(sa[i].length()<=20)continue;
			key = sa[i].split(".")[0];
			//读取缓存
			try {
				mSnapshot = mDiskLruCache.get(key);
				if (mSnapshot == null) continue;
				is = mSnapshot.getInputStream(0);
				in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = in.readLine()) != null) {
					int d = line.indexOf(":");
					if(d>=0){
						String ub = line.substring(0,d);
						d++;
						if(ub.equals("h1") && d < line.length()){
							h1 = line.substring(d,line.length());
						}else if(ub.equals("h2") && d < line.length()){
							h2 = line.substring(d,line.length());
						}else if(ub.equals("h3") && d < line.length()){
							h3 = line.substring(d,line.length());
						}else if(ub.equals("id") && d < line.length()){
							id = Long.parseLong(line.substring(d,line.length()));
						}else if(ub.equals("img") && d < line.length()){
							img = line.substring(d,line.length());
						}
					}
				}
				xlist.add(new SignBeanX(id,h1,h2,h3,img,key));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/
	
	public void readTextCaches(File f,final IWQFunc<InputStream> callback){
		InputStream is = null;
		String key ="";
		String[] sa = f.list();
		int i=-1;
		while(++i<sa.length){
			if(sa[i].length()<32)continue;
			key = sa[i].split(".")[0];
			//读取缓存
			try {
				mSnapshot = mDiskLruCache.get(key);
				if (mSnapshot == null) continue;
				is = mSnapshot.getInputStream(0);
				callback.invoke(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
