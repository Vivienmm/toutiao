package com.chinaso.toutiao.mvp.presenter;

import com.chinaso.toutiao.mvp.data.collection.CollectionEntity;
import com.chinaso.toutiao.mvp.presenter.base.BasePresenter;

import java.util.List;

public interface CollectionPresenter extends BasePresenter {

    //清空所有收藏
    void clearAllCollections();
    //获取所有收藏
    List<CollectionEntity> getAllCollections();
    //编辑收藏
    void editCollection(int pos, String title, String url);

    void editCollection(CollectionEntity entity);
    //删除所选收藏
    void deleteCollection(int pos);

    void deleteCollectionByUrl(String url);

    void addCollection(CollectionEntity entity);
    //进入收藏链接
    void gotoSelectedCollection();
    //
    CollectionEntity getSelectedCollection(int pos);

    void showCollectionState(int state);

    boolean isCollected(String url);

}
