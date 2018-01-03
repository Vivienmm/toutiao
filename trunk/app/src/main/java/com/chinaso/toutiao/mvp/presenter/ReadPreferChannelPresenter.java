package com.chinaso.toutiao.mvp.presenter;

import com.chinaso.toutiao.mvp.entity.PreferCustomizableChannel;
import com.chinaso.toutiao.mvp.presenter.base.BasePresenter;

import java.util.List;

public interface ReadPreferChannelPresenter extends BasePresenter{
    void onCreate();
    void initData();
    void selectedBoy();
    void selectedGirl();
    void selectedChannelItem(List<PreferCustomizableChannel> channelsSelected );
    void confirm2Server();
}
