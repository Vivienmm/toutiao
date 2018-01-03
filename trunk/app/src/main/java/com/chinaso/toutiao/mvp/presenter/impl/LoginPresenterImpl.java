package com.chinaso.toutiao.mvp.presenter.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.entity.user.UserInfoManager;
import com.chinaso.toutiao.mvp.interactor.LoginInteractor;
import com.chinaso.toutiao.mvp.interactor.impl.LoginInteractorImpl;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;
import com.chinaso.toutiao.mvp.presenter.LoginPresenter;
import com.chinaso.toutiao.mvp.view.LoginView;
import com.chinaso.toutiao.mvp.view.base.BaseView;
import com.chinaso.toutiao.util.PhoneUtils;
import com.chinaso.toutiao.util.SignCodeUtil;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.util.secure.AESUtils;
import com.chinaso.toutiao.util.secure.JniUtil;
import com.chinaso.toutiao.util.secure.MD5Util;
import com.chinaso.toutiao.util.secure.PackageUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPresenterImpl implements LoginPresenter, RequestCallBack<LoginResponse> {
    private String nickName = "";
    private String userId = " ";
    private String imgUrl = " ";
    private UMShareAPI mShareAPI = null;
    private LoginView mView;
    private String name;
    private String password;
    private LoginInteractor mInteractor;
    private int mAccountType = -1;// 0:email 1:phone 2:third part

    public LoginPresenterImpl() {
        mInteractor = new LoginInteractorImpl();
    }

    @Override
    public void onCreate() {
        mShareAPI = UMShareAPI.get(TTApplication.getApp());
        mAccountType = UserInfoManager.getInstance().getUserType();
    }

    public UMShareAPI getUMShareAPI() {
        return mShareAPI;
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mView = (LoginView) view;
    }

    @Override
    public void success(LoginResponse data) {
        SharedPreferencePrefUserInfo.setIsSavePassword(true);
        SharedPreferencePrefUserInfo.setUserId(data.getUserId());
        UserInfoManager.getInstance().setLoginSuccess(data, mAccountType, mView.getAccountName(), mView.getAccountPwd());
        mView.loginSuccess(data);
    }

    @Override
    public void onError(String errorMsg) {
        mView.showErrorMsg(errorMsg);
    }

    /**
     * 用户名登录
     */
    @Override
    public void loginPwd() {
        name = mView.getAccountName();
        password = mView.getAccountPwd();
        if (PhoneUtils.isPhoneNumberValid(name)) {
            mAccountType = 1;
        }
        if (mAccountType == -1) {
            mView.showErrorMsg("账户信息错误");
        }
        String sign = getLoginSign();
        mInteractor.login(name, password, sign, this);
    }

    @Override
    public void loginThirdResponse() {
        SHARE_MEDIA platform = mView.getPlatForm();
        if (mShareAPI.isInstall(TTApplication.mActivity, platform)) {
            mShareAPI.doOauthVerify(TTApplication.mActivity, platform, umAuthListener);
        } else {
            ToastUtil.showToast(TTApplication.mActivity, "没有安装" + platform + "客户端，无法" + platform + "登陆", R.mipmap.toast_net_err);
        }
    }

    /**
     * auth callback interface
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            mView.showLoginMessage("登录 成功");
            mShareAPI.getPlatformInfo(TTApplication.mActivity, platform, umGetInfoListener);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            mView.showLoginMessage("授权 失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            mView.showLoginMessage("授权 取消");
        }
    };

    /**
     * getUserInfo
     **/
    private UMAuthListener umGetInfoListener = new UMAuthListener() {

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data == null) {
                return;
            }
            //暂不支持新浪微博platform == SHARE_MEDIA.SINA
            if (platform == SHARE_MEDIA.QQ) {
                Set<String> keys = data.keySet();
                for (String key : keys) {
//                    nickName = key.equals("screen_name") ? data.get(key): "";
//                    userId =  key.equals("openid") ? data.get(key): "";
//                    imgUrl =  key.equals("profile_image_url") ? data.get(key): "";
                    if (key == "screen_name") {
                        nickName = data.get(key);
                    }
                    if (key == "openid") {
                        userId = data.get("openid");
                    }
                    //profile_image_url
                    if (key == "profile_image_url") {
                        imgUrl = data.get("profile_image_url");
                    }
                }
                loginThirdpart("qq");
            }
            if (platform == SHARE_MEDIA.WEIXIN) {
                Set<String> keys = data.keySet();
                for (String key : keys) {
//                    nickName = (key=="nickname") ? enValid(data.get(key)) : "";
//                    userId = (key=="uniconid") ? data.get(key) : "";
//                    imgUrl = ("headimgurl".equals(key)) ? data.get(key) : " ";
                    if (key == "nickname") {
                        nickName = data.get(key);
                        nickName = enValid(nickName);
                    }
                    if (key == "unionid") {
                        userId = data.get(key);
                    }
                    //headimgurl
                    if (key == "headimgurl") {
                        imgUrl = data.get("headimgurl");
                    }
                }
                loginThirdpart("weixin");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            mView.showLoginMessage("获取用户信息失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            mView.showLoginMessage("获取用户信息失败");
        }
    };

    private void loginThirdpart(String provider) {
        String platform;
        JniUtil jniUtil = new JniUtil();
        String userKey = JniUtil.getUserKey(PackageUtil.getSignature(TTApplication.getApp()));
        if (!TextUtils.isEmpty(userKey)) {
            userId = AESUtils.encode(userId, userKey);
            platform = AESUtils.encode(provider, userKey);
        } else {
            mView.showErrorMsg("包签名错误");
            return;
        }

        TreeMap<String, String> map = new TreeMap<>();
        map.put("oauth_provider", "oauth_provider" + platform);
        map.put("oauth_userid", "oauth_userid" + userId);
        map.put("nickname", "nickname" + nickName);
        map.put("avatar", "avatar" + imgUrl);
        String sign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
        mInteractor.thirdLogin(platform, userId, nickName, imgUrl, sign, this);
    }

    private String enValid(String string) {
        String regex = "[a-zA-Z0-9\u4e00-\u9fa5]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(string);
        String result = "";
        while (match.find()) {
            result = result + match.group(0);
        }
        return result;
    }

    @Nullable
    private String getLoginSign() {
        JniUtil jniUtil = new JniUtil();
        String userKey = JniUtil.getUserKey(PackageUtil.getSignature(TTApplication.getApp()));
        if (!TextUtils.isEmpty(userKey)) {
            name = AESUtils.encode(name, userKey);
            password = AESUtils.encode(password, userKey);
        }
        if (mAccountType == -1) {
            mView.showErrorMsg("包签名错误");
        }
        TreeMap<String, String> map = new TreeMap<>();
        map.put("username", "username" + name);
        map.put("password", "password" + password);
        String sign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
        mInteractor.login(name, password, sign, this);
        return sign;
    }
}
