package com.chinaso.toutiao.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.chinaso.toutiao.app.entity.AppInitData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SharedPreferenceAPPInitData {
    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 设置初始化数据
     */
    public static void setAppInitData(AppInitData data) {
        ByteArrayOutputStream toByte = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(toByte);
            oos.writeObject(data);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //对byte[]进行Base64编码
        String initDataStr = new String(Base64.encode(toByte.toByteArray(), Base64.DEFAULT));
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PrefKey.INIT_APP, Context.MODE_PRIVATE).edit();
        editor.putString(PrefKey.INIT_APP_KEY, initDataStr);
        editor.apply();
    }

    /**
     * 读取初始化数据
     */
    public static AppInitData getAppInitData(){
        SharedPreferences preferences = mContext.getSharedPreferences(PrefKey.INIT_APP, Context.MODE_PRIVATE);
        String initDataStr = preferences.getString(PrefKey.INIT_APP_KEY, " ");

        byte[] base64Bytes = Base64.decode(initDataStr,Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois = null;
        AppInitData ret=null;
        try {
            ois = new ObjectInputStream(bais);
            ret=(AppInitData) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ret;

    }

    /**
     * 设置初始化数据是否从已经服务器加载
     */
    public static void setAppInitFlag(Boolean flag) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(PrefKey.INIT_APP, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PrefKey.INIT_APP_DATA_FLAG_KEY, flag);
        editor.apply();
    }

    /**
     * 读取初始化数据是否从已经服务器加载
     *
     * @return
     */
    public static boolean getAppInitFlag() {
        SharedPreferences preferences = mContext.getSharedPreferences(PrefKey.INIT_APP, Context.MODE_PRIVATE);
        return preferences.getBoolean(PrefKey.INIT_APP_DATA_FLAG_KEY, false);
    }
}
