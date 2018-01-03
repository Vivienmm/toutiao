package com.chinaso.toutiao.util;

import android.util.Log;

/**
 * debug方法
 */
public class DebugUtil {
	private static boolean isDebug;
	public static String TAG_LY = "ly";
	public static String TAG_CARD= "CardWatch";
	public static String TAG_TIME= "TimeWatch";
	public static String TAG_NET_ERR= "NetWatch";
	public static String TAG_EVENT= "EventWatch";
	public static String TAG_CAPTURE= "Capture";
	public static String TAG_PULLREFRESH= "PullRefreshWatch";
	public static boolean isDebug() {
		return isDebug;
	}

	/**
	 * 设置开启debug模式
	 * @param flag true debug; false no debug
	 */
	public static void setDebug(boolean flag) {
		isDebug = flag;
	}
	public static void i(String tag,String msg){
		if(isDebug){
			Log.i(tag, msg);
		}
	}
	public static void d(String tag,String msg){
		if(isDebug){
			Log.d(tag, msg);
		}
	}
	public static void e(String tag,String msg){
		if(isDebug){
			Log.e(tag, msg);
		}
	}
	
}
