package com.chinaso.toutiao.mvp.presenter.impl;

import android.support.annotation.NonNull;

import com.chinaso.toutiao.mvp.data.NewsChannelItem;
import com.chinaso.toutiao.mvp.data.NewsChannelManageDao;
import com.chinaso.toutiao.mvp.presenter.MgChannelPresenter;
import com.chinaso.toutiao.mvp.view.MgChannelView;
import com.chinaso.toutiao.mvp.view.base.BaseView;

import java.util.ArrayList;
import java.util.List;

public class MgChannelPresenterImpl implements MgChannelPresenter {
    private MgChannelView mgView;
    //未选择栏目列表
    private ArrayList<NewsChannelItem> otherChannelList = new ArrayList<>();
    //用户栏目列表
    private ArrayList<NewsChannelItem> userChannelList = new ArrayList<>();
    private List<String> mTabList = new ArrayList<>();
    private NewsChannelManageDao manageDao;
    @Override
    public void onCreate() {
        manageDao = new NewsChannelManageDao();
        userChannelList = (ArrayList<NewsChannelItem>) manageDao.getSelectedChannel();
        otherChannelList = (ArrayList<NewsChannelItem>) manageDao.getUnselectedChannel();
        mTabList = manageDao.getChannelNameList();
        getChannelInfo();
    }

    private void getChannelInfo() {
        mgView.initIndicator(mTabList);
        mgView.setDragData(userChannelList);
        mgView.setUnSelectedChannel(otherChannelList);

    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mgView = (MgChannelView) view;
    }



    @Override
    public void saveChannels() {
        manageDao.deleteAllChannel();
        manageDao.saveChannel(mgView.getUserList());
        manageDao.saveOtherChannel(mgView.getUnSelectedUserList());
    }

}
