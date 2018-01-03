package com.chinaso.toutiao.mvp.interactor;

import com.chinaso.toutiao.app.entity.NewsListItem;
import com.chinaso.toutiao.app.entity.VideoListItem;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;

import java.util.List;

import rx.Subscription;

public interface NewsListInteractor {
    Subscription loadNews(RequestCallBack<List<NewsListItem>> listener, String id, int startPage);

    Subscription loadVideo(RequestCallBack<List<VideoListItem>> listener, final String videoId, int pageNum);
}
