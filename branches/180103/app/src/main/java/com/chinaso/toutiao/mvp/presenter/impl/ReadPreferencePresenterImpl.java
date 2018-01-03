package com.chinaso.toutiao.mvp.presenter.impl;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.entity.PreferCustomizableChannel;
import com.chinaso.toutiao.mvp.presenter.ReadPreferChannelPresenter;
import com.chinaso.toutiao.mvp.view.ReadPreferChannelView;
import com.chinaso.toutiao.mvp.view.base.BaseView;

import java.util.ArrayList;
import java.util.List;

public class ReadPreferencePresenterImpl implements ReadPreferChannelPresenter {
    private ReadPreferChannelView view;
    private List<PreferCustomizableChannel> channels = new ArrayList<>();
    private List<PreferCustomizableChannel> channelsSelected = new ArrayList<>();
    @Override
    public void attachView(@NonNull BaseView view) {
        this.view = (ReadPreferChannelView)view;
    }
    @Override
    public void onCreate() {
        if (view == null) {
            return;
        }
        view.initView(channels);
    }

    @Override
    public void initData() {
        Resources resources = TTApplication.getApp().getResources();
        TypedArray ar = resources.obtainTypedArray(R.array.prefer_channel_icon);
        int len = ar.length();
        String[] preferChannelName = resources.getStringArray(R.array.prefer_channel_name);
        PreferCustomizableChannel customizableChannel;
        for (int i = 0; i < len; i++) {
            customizableChannel = new PreferCustomizableChannel();
            customizableChannel.setChannel(preferChannelName[i]);
            customizableChannel.setResId(ar.getResourceId(i, 0));
            customizableChannel.setSelected(false);
            channels.add(customizableChannel);
        }
        ar.recycle();
    }

    @Override
    public void selectedBoy() {
        view.selectBoyState();
    }

    @Override
    public void selectedGirl() {
        view.selectGirlState();
    }

    @Override
    public void selectedChannelItem(List<PreferCustomizableChannel> channelsSelected ) {
        this.channelsSelected = channelsSelected;
        if (channelsSelected == null) {
            view.confirmState(false);
        } else if (channelsSelected.size() > 0) {
            view.confirmState(true);
        } else {
            view.confirmState(false);
        }
    }

    @Override
    public void confirm2Server() {
        view.showSuccessSelected();
        for (PreferCustomizableChannel channel : channelsSelected ) {
            if(channel.isSelected()) {
                Log.i("ReadPreferencePresenter", "confirm: "+channel.getChannel());
            }
        }
    }
}
