package com.chinaso.toutiao.mvp.presenter.impl;

import android.support.annotation.NonNull;

import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.mvp.data.collection.CollectionEntity;
import com.chinaso.toutiao.mvp.data.collection.CollectionManageDao;
import com.chinaso.toutiao.mvp.presenter.CollectionPresenter;
import com.chinaso.toutiao.mvp.view.CollectionView;
import com.chinaso.toutiao.mvp.view.base.BaseView;

import java.util.List;

public class CollectionPresenterImpl implements CollectionPresenter {

    CollectionView collectionView;
    private CollectionManageDao collectionManageDao;


    @Override
    public void attachView(@NonNull BaseView view) {
        this.collectionView = (CollectionView) view;
    }

    @Override
    public void onCreate() {
        collectionManageDao = new CollectionManageDao();

        initViewFunc();
    }

    //封装view中的原子方法
    private void initViewFunc() {
        collectionView.initTopBar();
        collectionView.initSwipeMenuListView();
        collectionView.initMenu();
    }


    @Override
    public void clearAllCollections() {
        collectionManageDao.deleteAllCollections();
        collectionView.notifyAdapter();
    }

    @Override
    public List<CollectionEntity> getAllCollections() {
        return collectionManageDao.getAllCollections();
//        return null;
    }

    @Override
    public CollectionEntity getSelectedCollection(int pos) {
        return collectionManageDao.getSelectedCollection(pos);
    }

    @Override
    public void editCollection(int pos, String title, String url) {
        showCollectionState(collectionManageDao.editSelectedItem(pos, title, url));
        collectionView.notifyAdapter();
    }

    @Override
    public void editCollection(CollectionEntity entity) {
        showCollectionState(collectionManageDao.editSelectedItem(entity));
        collectionView.notifyAdapter();
    }

    @Override
    public void deleteCollection(int pos) {

        collectionManageDao.deleteSelectedItem(pos);
        collectionView.notifyAdapter();

    }

    @Override
    public void deleteCollectionByUrl(String url) {
        collectionManageDao.deleteCollectionByUrl(url);
    }

    @Override
    public void addCollection(CollectionEntity entity) {
        collectionManageDao.insertItem(entity);
    }

    @Override
    public void gotoSelectedCollection() {

    }

    @Override
    public void showCollectionState(int state) {
        String stateStr = null;
        switch (state) {
            case Constants.COLLECTION_ALREADY:
                stateStr = "已收藏";
                break;
            case Constants.COLLECTION_SUCCEEDED:
                stateStr = "收藏成功";
                break;
            case Constants.COLLECTION_FAILED:
                stateStr = "收藏失败";
                break;
        }
        collectionView.showToast(stateStr);

    }

    @Override
    public boolean isCollected(String url) {
        return collectionManageDao.isCollected(url);
    }
}
