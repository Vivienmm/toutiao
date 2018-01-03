package com.chinaso.toutiao.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.List;

/**
 * 网络状态判断
 */
  
public class NetWorkStatusUtil {  
  
    /** 
     * 网络是否可用 
     *  
     * @param context
     * @return 
     */  
	public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager cm = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo network = cm.getActiveNetworkInfo();  
        return network != null && network.isAvailable();
    }  
  
    /** 
     * Gps是否打开 
     *  
     * @param context 
     * @return 
     */  
    public static boolean isGpsEnabled(Context context) {  
        LocationManager locationManager = ((LocationManager) context  
                .getSystemService(Context.LOCATION_SERVICE));  
        List<String> accessibleProviders = locationManager.getProviders(true);  
        return accessibleProviders != null && accessibleProviders.size() > 0;  
    }  
  
    /** 
     * wifi是否打开 
     */  
    public static boolean isWifiEnabled(Context context) {  
        ConnectivityManager mgrConn = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        TelephonyManager mgrTel = (TelephonyManager) context  
                .getSystemService(Context.TELEPHONY_SERVICE);  
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn  
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel  
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);  
    }  
  
    /** 
     * 判断当前网络是否是wifi网络 
     * if(activeNetInfo.getType()==ConnectivityManager.TYPE_MOBILE) { //判断3G网 
     *  
     * @param context 
     * @return boolean 
     */  
    public static boolean isWifi(Context context) {  
        ConnectivityManager connectivityManager = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  

        return (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }  
  
    /** 
     * 判断当前网络是否是2G/3G/4G网络 
     *  
     * @param context 
     * @return boolean 
     */  
    public static boolean isMobile(Context context) {  
        ConnectivityManager connectivityManager = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  

        return (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }  
}  