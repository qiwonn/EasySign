package com.wq.esign;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileReader;
import java.io.FileOutputStream;
import android.os.Environment;
import com.wq.esign.R;
/** 文件工具 */
public class FileUtil {

	@SuppressWarnings("unused")
	public static void str2File(String result, String outPath) {
		FileOutputStream fos = null;
		try{
			String folder = Environment.getExternalStorageDirectory().getAbsolutePath();
			File txt = new File(folder + outPath);
			if (!txt.exists()) {
				txt.createNewFile();
			}
			byte bytes[] = new byte[512];
			bytes = result.getBytes();
			fos = new FileOutputStream(txt);
			fos.write(bytes);
			fos.flush();
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try
			{
				if(null!=fos)fos.close();
			}
			catch (IOException e)
			{}
		}

	}

	public static String loadTxt(String path){
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath();
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try{
			File file = new File(folder+path);
			if(null==file){
				return "";
			}
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine())!=null){
				content.append(line);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			try{
				if(reader!=null){
					reader.close();
				}
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		return content.toString();
	}
}
