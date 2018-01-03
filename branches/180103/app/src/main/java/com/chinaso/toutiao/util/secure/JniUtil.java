package com.chinaso.toutiao.util.secure;

import android.util.Log;

public class JniUtil {

    public static native String stringFromJNI();
    public static native String getUserKey(String sign);
    static {
        Log.d("ly","load native-lib");
        System.loadLibrary("native-lib");
    }
}
