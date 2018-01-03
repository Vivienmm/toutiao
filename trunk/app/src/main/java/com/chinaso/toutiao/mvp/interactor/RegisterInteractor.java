package com.chinaso.toutiao.mvp.interactor;

import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;

public interface RegisterInteractor {
    void getRegisterCode(String numberKey, String numberSign, String type, RequestCallBack<LoginResponse> callBack);

    void register(String numKey, String pwdKey, String smsCode, String regSign, RequestCallBack<LoginResponse> callback);

    void modify(String phoneKey, String passwordKey, String smsCode, String findPasswordSign, RequestCallBack<LoginResponse> callback);
}
