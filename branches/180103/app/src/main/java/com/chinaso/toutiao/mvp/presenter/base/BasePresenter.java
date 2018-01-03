package com.chinaso.toutiao.mvp.presenter.base;

import android.support.annotation.NonNull;

import com.chinaso.toutiao.mvp.view.base.BaseView;

public interface BasePresenter {
    void attachView(@NonNull BaseView view);
    void onCreate();
}
