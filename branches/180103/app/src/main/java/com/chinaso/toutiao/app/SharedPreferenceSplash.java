package com.chinaso.toutiao.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Administrator on 17-3-16.
 * 保存启动页的数据
 */

public class SharedPreferenceSplash {
    private final String SPLASH = "splash";
    private final String SAVED_IDS_KEY = "ids";
    private final String SAVED_IDS_DEFAULT_VALUE = "";

    public void setSplashInfoId(Context context,String ids) {
        Editor editor = context.getSharedPreferences(SPLASH, Context.MODE_PRIVATE).edit();
        editor.putString(SAVED_IDS_KEY, ids);
        editor.apply();
    }

    public String getSplashInfoId(Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences(SPLASH, Context.MODE_PRIVATE);
        return preferences.getString(SAVED_IDS_KEY, SAVED_IDS_DEFAULT_VALUE);
    }

}
