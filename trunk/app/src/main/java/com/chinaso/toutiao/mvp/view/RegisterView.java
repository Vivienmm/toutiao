package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.view.base.BaseView;

public interface RegisterView extends BaseView {

    void showToast(String message);

    void ResponseSuccessCode(LoginResponse data);

    String getRegisterPhoneNumber();

    void showAnimationError();

    String getRegisterCode();

    String getRegisterConfirmPwd();

    String getRegisterSetPwd();

    String getSmsCodeType();
}
