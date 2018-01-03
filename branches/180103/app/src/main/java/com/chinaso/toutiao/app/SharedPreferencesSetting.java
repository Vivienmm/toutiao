package com.chinaso.toutiao.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static com.chinaso.toutiao.app.PrefKey.APP_USER;
import static com.chinaso.toutiao.app.PrefKey.SETTING;


/**
 * 应用设置帮助类，SharedPreference存储
 *
 * @author liuyao
 */
public final class SharedPreferencesSetting {
    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 存储从服务器获得的最新版本号
     */
    public static void setLastVersionCodeFromServer(String versionCode) {
        setString(SETTING, PrefKey.VER_CODE_FROM_SERVER_KEY, versionCode);
    }

    public static String getLastVersionCodeFromServer() {
        return getString(SETTING, PrefKey.VER_CODE_FROM_SERVER_KEY, "");
    }

    /** 设置新闻详情页面的字体 */
    public static void setIsFontSize(String flag) {
        setString(APP_USER, PrefKey.APP_FONT_SIZE, flag);
    }

    public static String getIsFontSize() {
        return getString(APP_USER, PrefKey.APP_FONT_SIZE, "M");
    }


    /** 设置保存的关于新闻的部分contentId */
    public static void setContentId(String contentId) {
        setString(APP_USER, PrefKey.READ_COMMENT_CONTENTID, contentId);
    }

    public static String getContentId() {
        return getString(APP_USER, PrefKey.READ_COMMENT_CONTENTID, null);
    }

    public static void setIsNightMode(boolean flag) {
        setBoolean(APP_USER, PrefKey.NIGHT_MODE_KEY, flag);
    }

    public static boolean getIsNightMode() {
        return getBoolean(APP_USER, PrefKey.NIGHT_MODE_KEY, false);
    }

    /** 是否有新推送消息 */
    public static boolean getIsMiPushNewMessage() {
        return getBoolean(SETTING, PrefKey.PUSH_NEW_MESSAGE_KEY, false);
    }

    public static void setIsMiPushNewMessage(boolean flag) {
        setBoolean(SETTING, PrefKey.PUSH_NEW_MESSAGE_KEY, flag);
    }

    /** 判断是否开启新推送 */
    public static void setIsPUSHOPEN(boolean flag) {
        setBoolean(SETTING, PrefKey.PUSH_OPEN_KEY, flag);
    }

    public static boolean getIsPUSHOPEN() {
        return getBoolean(SETTING, PrefKey.PUSH_OPEN_KEY, false);
    }

    /** 判断是否wifi下下载图片 */
    public static void setShowImgNOWifi(boolean flag) {
        setBoolean(SETTING, PrefKey.SETTING_NOWIFI_IMG_KEY, flag);
    }

    public static boolean getShowImgNOWifi() {
        return getBoolean(SETTING, PrefKey.SETTING_NOWIFI_IMG_KEY, true);
    }

    public static void setInteger(String filename, String key, int value) {
        Editor editor = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setString(String filename, String key, String value) {
        Editor editor = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setBoolean(String filename, String key, boolean value) {
        Editor editor = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setLong(String filename, String key, long value) {
        Editor editor = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static int getInteger(String filename, String key, Integer defaultValue) {
        SharedPreferences preferences = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    public static String getString(String filename, String key, String defaultValue) {
        SharedPreferences preferences = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    public static boolean getBoolean(String filename, String key, boolean defaultValue) {
        SharedPreferences preferences = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }

    public static long getLong(String filename, String key, long defaultValue) {
        SharedPreferences preferences = mContext.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return preferences.getLong(key, defaultValue);
    }
}
