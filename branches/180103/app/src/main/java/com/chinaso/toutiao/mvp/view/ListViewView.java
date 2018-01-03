package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.mvp.view.base.BaseView;

public interface ListViewView<T> extends BaseView {

    void showProgress();

    void hideProgress();

    //    public void setNewsList(List<NewsListItem> items);
    void refresh(T items);

    void loadMore(T items);
}
