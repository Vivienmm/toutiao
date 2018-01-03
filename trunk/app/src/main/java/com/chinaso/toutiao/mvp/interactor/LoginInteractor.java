package com.chinaso.toutiao.mvp.interactor;

import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;

public interface LoginInteractor {
    void login(String name, String password, String sign, RequestCallBack<LoginResponse> callback);

    void thirdLogin(String oauth_provider, String oauth_userid, String nickname, String avatar, String sign,RequestCallBack<LoginResponse> callback);
}
