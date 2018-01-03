package com.chinaso.toutiao.mvp.data.collection;

import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.greendao.gen.CollectionEntityDao;
import com.chinaso.toutiao.mvp.data.GreenDaoManager;

import java.util.ArrayList;
import java.util.List;

public class CollectionManageDao {
    private List<CollectionEntity> allCollectionEntities = new ArrayList<>();

    public CollectionManageDao() {
        allCollectionEntities = GreenDaoManager.getInstance().getSession().getCollectionEntityDao().queryBuilder().build().list();
    }

    public List<CollectionEntity> getAllCollections() {
//        return allCollectionEntities = GreenDaoManager.getInstance().getSession().getCollectionEntityDao().queryBuilder().build().list();
        return allCollectionEntities;
    }

    public void deleteSelectedItem(int pos) {
        //
        CollectionEntityDao collectionManageDao = GreenDaoManager.getInstance().getSession().getCollectionEntityDao();
        CollectionEntity collectionEntity = collectionManageDao.queryBuilder().where(CollectionEntityDao.Properties.Url.eq(allCollectionEntities.get(pos).getUrl())).build().unique();
        if (collectionEntity != null) {
            GreenDaoManager.getInstance().getSession().getCollectionEntityDao().delete(collectionEntity);
        }
        notifyData();
    }

    public void deleteCollectionByUrl(String url) {
        CollectionEntityDao collectionManageDao = GreenDaoManager.getInstance().getSession().getCollectionEntityDao();
        CollectionEntity collectionEntity = collectionManageDao.queryBuilder().where(CollectionEntityDao.Properties.Url.eq(url)).build().unique();
        if (collectionEntity != null) {
            GreenDaoManager.getInstance().getSession().getCollectionEntityDao().delete(collectionEntity);
        }
        notifyData();
    }

    public int editSelectedItem(int pos, String title, String url) {

        try {
            CollectionEntity entity = allCollectionEntities.get(pos);
            CollectionEntity collectionEntity = GreenDaoManager.getInstance().getSession().getCollectionEntityDao().queryBuilder().where(CollectionEntityDao.Properties.Url.eq(url)).build().unique();
            if (collectionEntity != null && !entity.getId().equals(collectionEntity.getId())) {
                //已收藏，排除跟自身一样的判断
                return Constants.COLLECTION_ALREADY;
            } else {
                entity.setTitle(title);
                entity.setUrl(url);
                GreenDaoManager.getInstance().getSession().update(entity);
                notifyData();
                return  Constants.COLLECTION_SUCCEEDED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.COLLECTION_FAILED;
        }
    }

    public int editSelectedItem(CollectionEntity entity) {

        try {
            CollectionEntity collectionEntity = GreenDaoManager.getInstance().getSession().getCollectionEntityDao().queryBuilder().where(CollectionEntityDao.Properties.Url.eq(entity.getUrl())).build().unique();
            if (collectionEntity != null) {
                //已收藏
                notifyData();
                return Constants.COLLECTION_ALREADY;
            } else {

                GreenDaoManager.getInstance().getSession().update(entity);
                notifyData();
                return  Constants.COLLECTION_SUCCEEDED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.COLLECTION_FAILED;
        }
    }

    public boolean isCollected(String url) {
        CollectionEntity collectionEntity = GreenDaoManager.getInstance().getSession().getCollectionEntityDao().queryBuilder().where(CollectionEntityDao.Properties.Url.eq(url)).build().unique();
        return collectionEntity != null;
    }

    //插入前判断unique的url是否已存在
    public int insertItem(CollectionEntity entity) {
        try {
            CollectionEntity collectionEntity = GreenDaoManager.getInstance().getSession().getCollectionEntityDao().queryBuilder().where(CollectionEntityDao.Properties.Url.eq(entity.getUrl())).build().unique();
            if (collectionEntity != null) {
                //已收藏
                return Constants.COLLECTION_ALREADY;
            } else {
                GreenDaoManager.getInstance().getSession().insert(entity);
                notifyData();
                return  Constants.COLLECTION_SUCCEEDED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.COLLECTION_FAILED;
        }

    }

    public int insertItem(String title, String url, int type) {
        //模拟添加数据
        CollectionEntity entity = new CollectionEntity();
        entity.setTitle(title);
        entity.setType(type);
        entity.setUrl(url);
        return insertItem(entity);
    }

    public void deleteAllCollections() {
        GreenDaoManager.getInstance().getSession().getCollectionEntityDao().deleteAll();
        notifyData();
    }

    private void notifyData() {
        allCollectionEntities.clear();
        allCollectionEntities.addAll(GreenDaoManager.getInstance().getSession().getCollectionEntityDao().queryBuilder().build().list());
    }

    public CollectionEntity getSelectedCollection(int pos) {
        if (allCollectionEntities.size() > pos)
            return allCollectionEntities.get(pos);
        else return null;
    }
}
