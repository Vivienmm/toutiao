package com.chinaso.toutiao.mvp.data.readhistory;

import android.database.Cursor;

import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.greendao.gen.DaoSession;
import com.chinaso.toutiao.greendao.gen.ReadHistoryEntityDao;
import com.chinaso.toutiao.mvp.data.GreenDaoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chinaso on 2017/2/28.
 */

public class ReadHistoryManageDao {
    private List<ReadHistoryEntity> allReadEntities = new ArrayList<>();

    public ReadHistoryManageDao() {
        allReadEntities = GreenDaoManager.getInstance().getSession().getReadHistoryEntityDao().queryBuilder().build().list();
    }

    public List<ReadHistoryEntity> getAllHistorys() {
        return allReadEntities;
    }

    //插入前判断unique的url是否已存在
    public int insertItem(ReadHistoryEntity entity) {
        try {
            ReadHistoryEntity readEntity = GreenDaoManager.getInstance().getSession().getReadHistoryEntityDao().queryBuilder().where(ReadHistoryEntityDao.Properties.Url.eq(entity.getUrl())).where(ReadHistoryEntityDao.Properties.ReadDate.eq(entity.getReadDate())).build().unique();
            if (readEntity != null) {
                //已收藏
                return Constants.COLLECTION_ALREADY;
            } else {
                GreenDaoManager.getInstance().getSession().insert(entity);
                notifyData();
                return Constants.COLLECTION_SUCCEEDED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.COLLECTION_FAILED;
        }

    }

    public List<String> getAllDate() {

        List<String> result = new ArrayList<>();
        for (int i = 0; i < allReadEntities.size(); i++) {
            String date = allReadEntities.get(i).getReadDate();
            if (!result.contains(date)) {
                result.add(date);
            }
        }
        return result;
    }
    private static final String SQL_DISTINCT_ENAME = "SELECT DISTINCT "+ReadHistoryEntityDao.Properties.ReadDate.columnName+" FROM "+ReadHistoryEntityDao.TABLENAME;
    public List<String> getAllDates() {
       DaoSession session= GreenDaoManager.getInstance().getSession();
        List<String> result = new ArrayList<>();
        Cursor c = session.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
        try{
            if (c.moveToFirst()) {
                do {
                    result.add(c.getString(0));
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }

    public List<ReadHistoryEntity> getSelectedHistory(String date) {
        List<ReadHistoryEntity> historyLists = new ArrayList<>();
        historyLists = GreenDaoManager.getInstance().getSession().getReadHistoryEntityDao().queryBuilder().where(ReadHistoryEntityDao.Properties.ReadDate.eq(date)).build().list();
        return historyLists;
    }

    private void notifyData() {
        allReadEntities.clear();
        allReadEntities.addAll(GreenDaoManager.getInstance().getSession().getReadHistoryEntityDao().queryBuilder().build().list());
    }
}
