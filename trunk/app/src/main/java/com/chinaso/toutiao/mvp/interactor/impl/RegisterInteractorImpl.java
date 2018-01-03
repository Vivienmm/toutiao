package com.chinaso.toutiao.mvp.interactor.impl;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.interactor.RegisterInteractor;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;
import com.chinaso.toutiao.net.NetworkService;
import com.chinaso.toutiao.util.DebugUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterInteractorImpl implements RegisterInteractor {

    private static final String TAG = "RegisterInteractorImpl";

    /**
     * 获取验证码
     *
     * @param numberKey
     * @param numberSign
     * @param type
     * @param callback
     */
    @Override
    public void getRegisterCode(String numberKey, String numberSign, String type, final RequestCallBack<LoginResponse> callback) {
        Call<LoginResponse> registerSmsCodeCall = NetworkService.getSplashInstance().smsCode(numberKey, numberSign, type);
        registerSmsCodeCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse smsCode = response.body();
                if (smsCode == null) {
                    DebugUtil.e(DebugUtil.TAG_NET_ERR, "响应空，接口异常");
                    return;
                }
                if (smsCode.isResult()) {
                    callback.success(smsCode);
                    DebugUtil.e(TAG, "registerSmsCodeResponse" + smsCode.getMessage() + smsCode.getCode());
                } else {
                    DebugUtil.e(TAG, "onError" + smsCode.getMessage());
                    callback.onError(smsCode.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError(TTApplication.getApp().getString(R.string.register_loda_failure));
            }
        });
    }

    /**
     * 注册用户
     *
     * @param numKey
     * @param pwdKey
     * @param smsCode
     * @param regSign
     * @param callback
     */
    @Override
    public void register(String numKey, String pwdKey, String smsCode, String regSign, final RequestCallBack<LoginResponse> callback) {
        Call<LoginResponse> loginResponseCall = NetworkService.getSplashInstance().register(numKey, pwdKey, smsCode, regSign);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse == null) {
                    DebugUtil.e(DebugUtil.TAG_NET_ERR, "响应空，接口异常");
                    return;
                }
                if (loginResponse.isResult()) {
                    callback.success(loginResponse);
                    DebugUtil.e(TAG, "Result:" + loginResponse.isResult() + loginResponse.getUserId() + "////" + loginResponse.getMsisdn());
                } else {
                    DebugUtil.e(TAG, "loginResponse" + loginResponse.getUserId() + "---" + loginResponse.getErrorMessage());
                    callback.onError(loginResponse.getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError(TTApplication.getApp().getString(R.string.register_loda_failure));
            }
        });
    }

    /**
     * 修改密码接口
     *
     * @param phoneKey
     * @param passwordKey
     * @param smsCode
     * @param findPasswordSign
     * @param
     */
    @Override
    public void modify(String phoneKey, String passwordKey, String smsCode, String findPasswordSign, final RequestCallBack<LoginResponse> callback) {

        Call<LoginResponse> findPasswordResponseCall = NetworkService.getSplashInstance().findpd(phoneKey, passwordKey, smsCode, findPasswordSign);
        findPasswordResponseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse findPasswordResponse = response.body();
                if (findPasswordResponse == null) {
                    DebugUtil.e(DebugUtil.TAG_NET_ERR, "响应空，接口异常");
                    return;
                }
                if (findPasswordResponse.isResult()) {
                    callback.success(findPasswordResponse);

                } else {
                    callback.onError(findPasswordResponse.getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError(TTApplication.getApp().getResources().getString(R.string.register_loda_failure));
            }
        });
    }

}
