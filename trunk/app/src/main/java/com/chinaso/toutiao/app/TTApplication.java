package com.chinaso.toutiao.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.chinaso.toutiao.mvp.entity.user.UserInfoManager;
import com.umeng.socialize.PlatformConfig;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.lang.ref.WeakReference;
import java.util.List;


public class TTApplication extends Application {

    /* 存储版本更新信息*/
    private static int updateStatus = Constants.VERSION_UPDATE_NO;
    /*小米推送*/
    public static final String MI_APP_ID = "2882303761517530732";
    public static final String MI_APP_KEY = "5401753057732";
    public static final String TAG = "com.chinaso.toutiao";

    public static Activity mActivity;
    private static TTApplication ttApplication;

    /*判断是否登录*/
    public static boolean isLogin;
    public static boolean isShowImgNoWifi;
    public static boolean isNightMode; //设置夜间模式
    private static boolean isDebug = true;

    /* 是否有增加频道 */
    private static boolean hasNew = false;

    public static boolean isDebug() {
        return isDebug;
    }

    /*用于切换服务器，发版改为MOB_BASE*/
    public static String MOB_SERVER = Constants.SERVER_TEST_BASE;

    public static String getServer() {
        return MOB_SERVER;
    }

    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesSetting.init(this);
        SharedPreferenceAPPInitData.init(this);
        ttApplication = this;
        configServer();

        initShareConfig();
        if (SharedPreferencePrefUserInfo.getIsLogin()) {
            setIsLogin(true);
        } else {
            setIsLogin(false);
        }

        isShowImgNoWifi = SharedPreferencesSetting.getShowImgNOWifi();

        UserInfoManager.getInstance().init(this);
        //小米推送
        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(this, MI_APP_ID, MI_APP_KEY);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        TTApplication.isLogin = isLogin;
        SharedPreferencePrefUserInfo.setIsLogin(isLogin);
    }

    private void configServer() {

        try {
            ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            boolean isDebugInterface = appInfo.metaData.getBoolean("DEBUG_MODE");
            isDebug = isDebugInterface;
            if (isDebugInterface) {
                MOB_SERVER = Constants.SERVER_TEST_BASE;
            } else {
                MOB_SERVER = Constants.SERVER_BASE;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initShareConfig() {
        //微信 appid appsecret
        PlatformConfig.setWeixin("wxf8a8d13e85b9cb07", "7a640afdd7e386106094afc97faea54e");//只有这个申请有登录的权限，上面的只有分享权限！！！
        //新浪微博 appkey appsecret,暂未申请到
//        PlatformConfig.setSinaWeibo("3921700954","04b48b094faeb16683c32669824ebdad");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105890066", "KEYR26gjCDtMEDO8m4i");
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static void registerActivity(Activity activity) {
        mActivity = new WeakReference<>(activity).get();
    }

    //获取Application
    public static TTApplication getApp() {
        return ttApplication;
    }

    public static int getUpdateStatus() {
        return updateStatus;
    }

    public static void setUpdateStatus(int updateStatus) {
        TTApplication.updateStatus = updateStatus;
    }

    public static boolean getHasNew() {
        return hasNew;
    }

    public static void setHasNew(boolean hasNew) {
        TTApplication.hasNew = hasNew;
    }
}
