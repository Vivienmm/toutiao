package com.chinaso.toutiao.mvp.interactor.impl;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.interactor.LoginInteractor;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;
import com.chinaso.toutiao.net.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInteractorImpl implements LoginInteractor {

    /**
     * 实现账户名登录
     *
     * @param name
     * @param password
     * @param sign
     * @param callback
     */
    @Override
    public void login(String name, String password, String sign, final RequestCallBack<LoginResponse> callback) {
        Call<LoginResponse> loginResponseCall = NetworkService.getSplashInstance().login(name, password, sign);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse == null) {
                    callback.onError("服务器响应异常");
                } else if (loginResponse.isResult()) {
                    callback.success(loginResponse);

                } else {
                    callback.onError(loginResponse.getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError(TTApplication.getApp().getString(R.string.register_loda_failure));
            }
        });
    }

    @Override
    public void thirdLogin(String oauth_provider, String oauth_userid, String nickname, String avatar, String sign, final RequestCallBack<LoginResponse> callback) {
        Call<LoginResponse> loginResponseCall = NetworkService.getSplashInstance().thirdLogin(oauth_provider, oauth_userid, nickname, avatar, sign);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse == null) {
                    callback.onError("服务器异常响应");
                } else if (loginResponse.isResult()) {
                    callback.success(loginResponse);
                } else {
                    callback.onError(loginResponse.getErrorMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError(TTApplication.getApp().getResources().getString(R.string.register_loda_failure));
            }
        });
    }
}
