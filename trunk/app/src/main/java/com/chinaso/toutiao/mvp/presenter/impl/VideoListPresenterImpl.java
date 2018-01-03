package com.chinaso.toutiao.mvp.presenter.impl;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.chinaso.toutiao.app.entity.VideoListItem;
import com.chinaso.toutiao.mvp.interactor.NewsListInteractor;
import com.chinaso.toutiao.mvp.interactor.impl.NewsListInteractorImpl;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;
import com.chinaso.toutiao.mvp.presenter.base.BasePresenter;
import com.chinaso.toutiao.mvp.view.ListViewView;
import com.chinaso.toutiao.mvp.view.base.BaseView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class VideoListPresenterImpl implements BasePresenter, RequestCallBack<List<VideoListItem>> {
    private ListViewView listNewsView;
    private String videoId;
    private int pageNum = 0;
    private List<VideoListItem> mLists = new ArrayList<>();
    private final String OUTSTATEKEY = "outStateKey";
    private NewsListInteractor newsListInteractor;
    private int REFRESH_TYPE = 1;
    private int LOADMORE_TYPE = 2;
    private int TYPE;
    Subscription subscription;

    public VideoListPresenterImpl(NewsListInteractorImpl interactor) {
        this.newsListInteractor = interactor;
    }

    @Override
    public void onCreate() {
        if (listNewsView == null) {
            return;
        }
        subscription = newsListInteractor.loadVideo(this, videoId, 0);
        TYPE = REFRESH_TYPE;
        listNewsView.showProgress();
    }


    public void onSaveState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelableArrayList(OUTSTATEKEY, (ArrayList<? extends Parcelable>) mLists);
        }
    }

    public void onRestoreState(Bundle outState){
        if (outState != null) {
            mLists = outState.getParcelableArrayList(OUTSTATEKEY);
            TYPE = REFRESH_TYPE;
            success(mLists);
        }
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        this.listNewsView = (ListViewView) view;
    }

    public void setListVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public void success(List<VideoListItem> items) {
        if (TYPE == REFRESH_TYPE) {
            listNewsView.refresh(items);
        } else if (TYPE == LOADMORE_TYPE) {
            listNewsView.hideProgress();
            listNewsView.loadMore(items);
        }
        listNewsView.hideProgress();
    }

    @Override
    public void onError(String errorMsg) {
    }

    public void refreshData() {
        newsListInteractor.loadVideo(this, videoId, 0);
        TYPE = REFRESH_TYPE;
    }

    public void loadMore() {
        ++pageNum;
        newsListInteractor.loadVideo(this, videoId, pageNum);
        TYPE = LOADMORE_TYPE;
    }

    public void onDestory() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
