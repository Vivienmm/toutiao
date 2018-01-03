package com.chinaso.toutiao.mvp.presenter.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chinaso.toutiao.app.SharedPreferenceAPPInitData;
import com.chinaso.toutiao.app.SharedPreferenceSplash;
import com.chinaso.toutiao.app.entity.AppInitData;
import com.chinaso.toutiao.mvp.entity.StartUpEntity;
import com.chinaso.toutiao.mvp.presenter.SplashPresenter;
import com.chinaso.toutiao.mvp.ui.activity.SplashActivity;
import com.chinaso.toutiao.mvp.view.SplashView;
import com.chinaso.toutiao.mvp.view.base.BaseView;
import com.chinaso.toutiao.net.NetworkService;
import com.chinaso.toutiao.util.AppInitDataUtil;
import com.chinaso.toutiao.util.Utils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SplashPresenterImpl implements SplashPresenter{
    private SplashActivity mActivity;
    private SplashView mView;

    private SharedPreferenceSplash sharedPreferenceSplash;

    public SplashPresenterImpl(SplashActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mView = (SplashView) view;
    }

    @Override
    public void onCreate() {

        initAPPData();

        initSplashData();

    }

    private void initAPPData() {
        NetworkService.getInstance().getInitData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AppInitData>() {
                    @Override
                    public void call(AppInitData appInitData) {
                        SharedPreferenceAPPInitData.setAppInitData(appInitData);
                        SharedPreferenceAPPInitData.setAppInitFlag(true);
                        AppInitDataUtil.setAppInitData(appInitData);
                    }
                });
    }

    private void initSplashData() {
        sharedPreferenceSplash = new SharedPreferenceSplash();
        final String ids = sharedPreferenceSplash.getSplashInfoId(mActivity);
        NetworkService.getSplashInstance().getNewSplashBkgData(ids)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StartUpEntity>() {
                    @Override
                    public void call(StartUpEntity entity) {
                        if (entity != null) {
                            if ( !TextUtils.isEmpty(entity.getId())) {
                                sharedPreferenceSplash.setSplashInfoId(mActivity, Utils.stringFIFO(entity.getId(), ids));
                            }
                            if (!TextUtils.isEmpty(entity.getImgUrl())) {
                                mView.showSplashBkg(entity);
                            }
                        }
                    }
                });
    }

    public void initSplashView() {
        mView.initSplashView();
    }

}
