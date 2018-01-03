package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.app.entity.ModifyUserInfoResponse;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.view.base.BaseView;

public interface UserEditView extends BaseView {
    void setAvatar(String url);
    void setUserName(String userName);

    void showToast(String message);

    void onSuccess(ModifyUserInfoResponse data);
}
