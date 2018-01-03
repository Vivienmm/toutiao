package com.chinaso.toutiao.mvp.presenter.impl;

import android.support.annotation.NonNull;

import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.entity.user.UserInfoManager;
import com.chinaso.toutiao.mvp.interactor.RegisterInteractor;
import com.chinaso.toutiao.mvp.interactor.impl.RegisterInteractorImpl;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;
import com.chinaso.toutiao.mvp.presenter.RegisterPresenter;
import com.chinaso.toutiao.mvp.view.RegisterView;
import com.chinaso.toutiao.mvp.view.base.BaseView;
import com.chinaso.toutiao.util.DebugUtil;
import com.chinaso.toutiao.util.PhoneUtils;
import com.chinaso.toutiao.util.SignCodeUtil;
import com.chinaso.toutiao.util.secure.AESUtils;
import com.chinaso.toutiao.util.secure.JniUtil;
import com.chinaso.toutiao.util.secure.MD5Util;
import com.chinaso.toutiao.util.secure.PackageUtil;

import java.util.Map;
import java.util.TreeMap;

public class RegisterPresenterImpl implements RegisterPresenter, RequestCallBack<LoginResponse> {
    public static final String TAG = "RegisterPresenterImpl";

    private RegisterInteractor mInteractor;
    private RegisterView mView;
    private String phoneNumber;
    private String code;
    private String smsCode;
    private String setPwd;
    private String confirmPwd;

    public RegisterPresenterImpl() {
        this.mInteractor = new RegisterInteractorImpl();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mView = (RegisterView) view;
    }

    @Override
    public void success(LoginResponse data) {
        if (data != null) {
            code = data.getCode();//获取验证码
            mView.ResponseSuccessCode(data);
            if (data.getCode() == null) {//注册账户
                UserInfoManager.getInstance().setLoginSuccess(data, 1, phoneNumber, confirmPwd);
                SharedPreferencePrefUserInfo.setIsRegister(true);
            }
        }
    }

    @Override
    public void onError(String errorMsg) {
        mView.showErrorMsg(errorMsg);
    }


    /**
     * 获取验证码
     */
    @Override
    public void getSmsCode() {
        phoneNumber = mView.getRegisterPhoneNumber();
        if (PhoneUtils.isPhoneNumberValid(phoneNumber) && phoneNumber != null) {
            Map<String, String> map = new TreeMap<>();
            JniUtil jniUtil = new JniUtil();
            String userKey = JniUtil.getUserKey(PackageUtil.getSignature(TTApplication.getApp()));
            String numberKey = AESUtils.encode(phoneNumber, userKey);
            map.put("mobileNumber", "mobileNumber" + numberKey);
            map.put("type", "type" + mView.getSmsCodeType());
            String numberSign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
            mInteractor.getRegisterCode(numberKey, numberSign, mView.getSmsCodeType(), this);
        } else {
            mView.showErrorMsg("请输入正确的手机号");
            mView.showAnimationError();
        }
    }

    /**
     * 注册用户处理
     */
    @Override
    public void register() {
        getUserInfo();
        if (emptyCheck()) {
            Map<String, String> map = new TreeMap<>();
            JniUtil jniUtil = new JniUtil();
            String userKey = JniUtil.getUserKey(PackageUtil.getSignature(TTApplication.getApp()));
            DebugUtil.e(TAG, "userKey:" + userKey);

            String numberKey = AESUtils.encode(phoneNumber, userKey);
            map.put("username", "username" + numberKey);

            String passwordKey = AESUtils.encode(confirmPwd, userKey);
            DebugUtil.e(TAG, "passwordKey:" + passwordKey + ":" + confirmPwd);
            map.put("password", "password" + passwordKey);

            DebugUtil.e(TAG, "smsCodeKye:" + smsCode);
            map.put("smsCode", "smsCode" + smsCode);

            String registerSign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
            DebugUtil.e(TAG, "MAP:" + SignCodeUtil.getAsceCode(map));
            registerUser(numberKey, passwordKey, smsCode, registerSign);
        }
    }

    /**
     * 修改密码处理
     */
    @Override
    public void modifyPWD() {
        getUserInfo();
        if (emptyCheck()) {
            Map<String, String> map = new TreeMap<>();
            JniUtil jniUtil = new JniUtil();
            String userKey = JniUtil.getUserKey(PackageUtil.getSignature(TTApplication.getApp()));

            String passwordKey = AESUtils.encode(confirmPwd, userKey);
            map.put("password", "password" + passwordKey);

            String phoneKey = AESUtils.encode(phoneNumber, userKey);
            map.put("mobileNumber", "mobileNumber" + phoneKey);

            map.put("smsCode", "smsCode" + smsCode);
            DebugUtil.e(TAG, "code" + smsCode);

            String findPasswordSign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
            mInteractor.modify(phoneKey, passwordKey, smsCode, findPasswordSign, this);
        }
    }

    private void getUserInfo() {
        phoneNumber = mView.getRegisterPhoneNumber();
        smsCode = mView.getRegisterCode();
        setPwd = mView.getRegisterSetPwd();
        confirmPwd = mView.getRegisterConfirmPwd();
    }

    private void registerUser(String numKey, String pwdKey, String smsCode, String regSign) {
        mInteractor.register(numKey, pwdKey, smsCode, regSign, this);
    }

    private boolean emptyCheck() {
        if (!PhoneUtils.isPhoneNumberValid(phoneNumber)) {
            mView.showToast("请输入正确的手机号");
            return false;
        }
        if (!PhoneUtils.notEmpty(smsCode)) {
            mView.showToast("请输入验证码");
            return false;
        }
        if (!PhoneUtils.notPassWord(setPwd) || !PhoneUtils.notPassWord(confirmPwd)) {
            mView.showToast("请输入6-16个字符的密码");
            return false;
        }
        if (!PhoneUtils.match(setPwd, confirmPwd)) {
            mView.showToast("两次输入的密码不一样");
            return false;
        }
        if (!PhoneUtils.match(smsCode, smsCode)) {
            mView.showToast("验证码输入错误");
            return false;
        }
        return true;
    }

}
