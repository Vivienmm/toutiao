package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.mvp.data.collection.CollectionEntity;
import com.chinaso.toutiao.mvp.view.base.BaseView;

public interface CollectionView extends BaseView {

    void initTopBar();

    void initSwipeMenuListView();

    void initMenu();

    void showToast(String s);

    void notifyAdapter();

    void gotoSelectedCollection(CollectionEntity collectionEntity);

}
