package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.mvp.entity.StartUpEntity;
import com.chinaso.toutiao.mvp.view.base.BaseView;

public interface SplashView extends BaseView {

    void initSplashView();

    void showSplashBkg(StartUpEntity entity);
}
