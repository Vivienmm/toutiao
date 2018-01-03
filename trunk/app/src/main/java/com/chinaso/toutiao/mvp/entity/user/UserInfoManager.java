package com.chinaso.toutiao.mvp.entity.user;

import android.content.Context;
import android.text.TextUtils;

import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.entity.cache.UserInfoCache;
import com.chinaso.toutiao.util.secure.AESUtils;
import com.chinaso.toutiao.util.secure.JniUtil;
import com.chinaso.toutiao.util.secure.PackageUtil;

/**
 * 所有和用户登录相关的方法请在此封装
 * Created by liuyao on 2016/1/6 0006.
 */
public class UserInfoManager {
    private static UserInfoManager ourInstance = new UserInfoManager();
    private LoginResponse mLoginResponse;
    private UserInfoCache userInfoCache;
    private Context mContext;

    public static UserInfoManager getInstance() {
        return ourInstance;
    }

    public void init(Context context) {
        mContext = context;
        userInfoCache = new UserInfoCache(context);
        if (isLogin()) {
            mLoginResponse = userInfoCache.getUserInfo();
        }
    }

    private UserInfoManager() {
    }

    public boolean isLogin() {
        return TTApplication.isLogin();
    }

    public long getUserId() {
        if (mLoginResponse != null) {
            return mLoginResponse.getUserId();
        }
        return 0;
    }

    public int getUserType() {
        return SharedPreferencePrefUserInfo.getUserType();
    }

    public String getUserName() {
        return SharedPreferencePrefUserInfo.getUserName();
    }

    public String getUserPassword() {
        String ret = "";
        JniUtil jniUtil = new JniUtil();
        String userKey = JniUtil.getUserKey(PackageUtil.getSignature(mContext));
        if (!TextUtils.isEmpty(userKey) && !TextUtils.isEmpty(SharedPreferencePrefUserInfo.getUserPassword())) {
            ret = AESUtils.decode(SharedPreferencePrefUserInfo.getUserPassword(), userKey);
        }
        return ret;
    }

    public LoginResponse getLoginResponse() {
        return mLoginResponse;
    }

    /**
     * 登录后存储用户登录信息,需要同时更新视图
     *
     * @param loginResponse
     * @param name
     */
    public void setLoginSuccess(LoginResponse loginResponse, int type, String name, String password) {
        TTApplication.setIsLogin(true);
        SharedPreferencePrefUserInfo.setUserName(name);
        SharedPreferencePrefUserInfo.setUserType(type);

        JniUtil jniUtil = new JniUtil();
        String userKey = JniUtil.getUserKey(PackageUtil.getSignature(mContext));
        if (!TextUtils.isEmpty(userKey)) {

            String ret = AESUtils.encode(password, userKey);//byte[] ret=DesUtil.encrypt(password,userKey);
            if (!TextUtils.isEmpty(ret)) {
                SharedPreferencePrefUserInfo.setUserPassword(ret);
            }
        }

        mLoginResponse = loginResponse;
        userInfoCache.saveUserInfo(mLoginResponse);
    }

    /**
     * 登录下,更新本地存储的用户信息,需要同时更新视图
     */
    public void updateLocalLoginResponse() {
        if (!isLogin()) {
            return;
        }
        userInfoCache.saveUserInfo(mLoginResponse);
    }

    /**
     * 登录下,注销登录,需要同时更新视图
     */
    public void logOut() {
        if (!isLogin()) {
            return;
        }
        TTApplication.setIsLogin(false);
//        TTApplication.isLogin = false;
        //注销时暂时不清楚用户名密码
        //SharedPreferencesSetting.setUserName("");
        //SharedPreferencesSetting.setUserPassword("");
        mLoginResponse = new LoginResponse();
        userInfoCache.saveUserInfo(mLoginResponse);
    }
}
