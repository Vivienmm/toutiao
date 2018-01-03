package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.view.base.BaseView;
import com.umeng.socialize.bean.SHARE_MEDIA;

public interface LoginView extends BaseView {

    String getAccountName();

    String getAccountPwd();

    SHARE_MEDIA getPlatForm();

    void loginSuccess(LoginResponse data);

    void showLoginMessage(String message);
}
