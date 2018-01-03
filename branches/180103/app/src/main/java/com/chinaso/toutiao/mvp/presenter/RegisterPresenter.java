package com.chinaso.toutiao.mvp.presenter;

import com.chinaso.toutiao.mvp.presenter.base.BasePresenter;

public interface RegisterPresenter extends BasePresenter {

    void getSmsCode();

    void register();

    void modifyPWD();
}
