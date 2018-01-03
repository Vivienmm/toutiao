package com.chinaso.toutiao.mvp.interactor.impl;

import com.chinaso.toutiao.app.entity.NewsList;
import com.chinaso.toutiao.app.entity.NewsListItem;
import com.chinaso.toutiao.app.entity.VideoListItem;
import com.chinaso.toutiao.app.entity.VideosList;
import com.chinaso.toutiao.mvp.interactor.NewsListInteractor;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;
import com.chinaso.toutiao.net.NetworkService;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsListInteractorImpl implements NewsListInteractor {
    @Override
    public Subscription loadNews(final RequestCallBack<List<NewsListItem>> listener, final String newsId, int pageNum) {
        return NetworkService.getInstance().fetchNewsList(newsId, pageNum + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            listener.onError(e.getMessage());
                        } catch (IllegalStateException st) {
                            st.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(NewsList newsList) {
                        listener.success(newsList.getList());
                    }
                });
    }

    @Override
    public Subscription loadVideo(final RequestCallBack<List<VideoListItem>> listener, final String videoId, int pageNum) {
        return NetworkService.getInstance().fetchVideosList(videoId, pageNum + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VideosList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                        } catch (IllegalStateException st) {
                            st.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(VideosList videosList) {
                        listener.success(videosList.getList());
                    }
                });
    }
}
