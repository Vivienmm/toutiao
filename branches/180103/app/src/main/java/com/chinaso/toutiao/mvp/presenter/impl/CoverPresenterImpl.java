package com.chinaso.toutiao.mvp.presenter.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chinaso.toutiao.app.SharedPreferenceSplash;
import com.chinaso.toutiao.mvp.entity.StartUpEntity;
import com.chinaso.toutiao.mvp.presenter.CoverPresenter;
import com.chinaso.toutiao.mvp.ui.activity.CoverActivity;
import com.chinaso.toutiao.mvp.view.CoverView;
import com.chinaso.toutiao.mvp.view.base.BaseView;
import com.chinaso.toutiao.net.NetworkService;
import com.chinaso.toutiao.util.Utils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CoverPresenterImpl implements CoverPresenter{
    private CoverView mView;
    private CoverActivity mActivity;
    private SharedPreferenceSplash sharedPreferenceSplash;

    public CoverPresenterImpl(CoverActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mView = (CoverView)view;
    }

    @Override
    public void onCreate() {
        sharedPreferenceSplash = new SharedPreferenceSplash();
        final String info = sharedPreferenceSplash.getSplashInfoId(mActivity);
        NetworkService.getSplashInstance().getNewSplashBkgData(info)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StartUpEntity>() {
                    @Override
                    public void call(StartUpEntity entity) {
                        if (TextUtils.isEmpty(entity.getImgUrl())) {
                            return;
                        }
                        if (entity.getType() == 1) {
                            mView.showNews(entity);
                        } else {
                            mView.showAdImg(entity);
                        }
                        sharedPreferenceSplash.setSplashInfoId(mActivity, Utils.stringFIFO(entity.getId(), info));
                    }
                });
    }
}
