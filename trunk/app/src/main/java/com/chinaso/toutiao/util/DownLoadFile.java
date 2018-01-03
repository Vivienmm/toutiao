package com.chinaso.toutiao.util;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 文件下载，获取文件名及其类型
 */
public class DownLoadFile {
	public static String getFileName(String resourceUrl) {
		String urlString = resourceUrl;
		String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
		URL url = null;
		try {
			fileName = URLDecoder.decode(fileName, "utf-8");
			url = new URL(urlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!(Environment.getExternalStorageState())
				.equals(Environment.MEDIA_MOUNTED))
			return null;
		File directory = Environment.getExternalStorageDirectory();
		File file = new File(directory, fileName);
		if (file.exists())
			return fileName;

		FileOutputStream fos = null;
		InputStream input = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Accept-Encoding", "identity");
			conn.setDoInput(true); // 允许输入流，即允许下载
			conn.setDoOutput(true); // 允许输出流，即允许上传
			conn.setUseCaches(false); // 不使用缓冲
			conn.setRequestMethod("GET"); // 使用get请求
			if (conn.getResponseCode() == 200) {
				input = conn.getInputStream();
				conn.connect();
				fos = new FileOutputStream(file);
				int len = 0;
				byte[] b = new byte[1024];
				while ((len = input.read(b)) != -1) {
					fos.write(b, 0, len);
				}
			}
			fos.flush();
			fos.close();
			input.close();
			conn.disconnect();
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getMIMEType(File file){
		String type = "";
		String fName = file.getName();
		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* 依扩展名的类型决定MimeType */
		if (end.equals("pdf")) {
			type = "application/pdf";//
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio/*";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video/*";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image/*";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		}else {
			// /*如果无法直接打开，就跳出软件列表给用户选择 */
			type = "*/*";
		}
		return type;
	}
}
