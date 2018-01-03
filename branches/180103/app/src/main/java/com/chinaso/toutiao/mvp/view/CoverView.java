package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.mvp.entity.StartUpEntity;
import com.chinaso.toutiao.mvp.view.base.BaseView;

public interface CoverView extends BaseView {
    public void showNews(StartUpEntity entity);
    public void showAdImg(StartUpEntity entity);
}
