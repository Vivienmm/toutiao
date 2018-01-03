package com.chinaso.toutiao.mvp.data;

import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.entity.NewsMenuVo;
import com.chinaso.toutiao.greendao.gen.NewsChannelEntityDao;
import com.chinaso.toutiao.util.AppInitDataUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class NewsChannelManageDao {
    /**
     * 服务器请求最新频道
     */
    private List<NewsChannelEntity> serverChannels = new ArrayList<>();
    /**
     * 本地数据库查询所有频道
     */
    private List<NewsChannelEntity> allChannels = new ArrayList<>();

    public NewsChannelManageDao() {

        allChannels = GreenDaoManager.getInstance().getSession().getNewsChannelEntityDao().queryBuilder().build().list();
        initServerChannel(AppInitDataUtil.getNewsMenuVoList());
        if (allChannels.size() <= 0) {
            insertChannel2DB(serverChannels);
        } else {
            deleteAllChannel();
            matchDif();
            insertChannel2DB(allChannels);
        }


    }

    private void initServerChannel(List<NewsMenuVo> newsMenuVoList) {
        int len = newsMenuVoList.size();
        for (int i = 0; i < len; i++) {
            int showNum = 0;
            int notShowNum = 0;
            NewsMenuVo bean = newsMenuVoList.get(i);
            NewsChannelEntity entity = new NewsChannelEntity(bean.getId(), bean.getName(), showNum, true, bean.isLock(), false, bean.getType());
            if (bean.isShow()) {
                showNum++;
                entity.setSelected(true);
                entity.setOrderId(showNum);
            } else {
                notShowNum++;
                entity.setSelected(false);
                entity.setOrderId(notShowNum);
            }
            serverChannels.add(entity);
        }
    }

    private void matchDif() {

        deleteUnusedChannel();
        addNewChannel();
    }

    private void addNewChannel() {
        int lenNew = serverChannels.size();
        List<NewsChannelEntity> curServerChannels = new ArrayList<>();
        curServerChannels.addAll(serverChannels);
        for (int i = 0; i < lenNew; i++) {
            for (int g = 0; g < allChannels.size(); g++) {

                if (serverChannels.get(i).getId().equals(allChannels.get(g).getId())) {
                    allChannels.get(g).setName(serverChannels.get(i).getName());
                    allChannels.get(g).setLock(serverChannels.get(i).getLock());
                    allChannels.get(g).setType(serverChannels.get(i).getType());

                    curServerChannels.remove(serverChannels.get(i));
                    break;
                }

            }

        }
        if (curServerChannels.size() > 0) {
            TTApplication.setHasNew(true);
            for (int m = 0; m < curServerChannels.size(); m++) {
                int index = curServerChannels.get(m).getOrderId();
                allChannels.add(index, curServerChannels.get(m));
                allChannels.get(index).setAdded(true);
            }
        }
    }

    private void deleteUnusedChannel() {
        int lenNew = serverChannels.size();
        List<NewsChannelEntity> removeChannels = new ArrayList<>();
        for (int i = 0; i < allChannels.size(); i++) {
            Boolean isChecked = false;
            for (int g = 0; g < lenNew; g++) {

                if (serverChannels.get(g).getId().equals(allChannels.get(i).getId())) {
                    isChecked = true;
                    break;
                }

            }
            if (!isChecked) {
                removeChannels.add(allChannels.get(i));
            } else {
                isChecked = false;
            }

        }
        if (removeChannels.size() > 0) {
            for (int i = 0; i < removeChannels.size(); i++) {

                allChannels.remove(removeChannels.get(i));
            }
        }
    }

    private void insertChannel2DB(List<NewsChannelEntity> list) {
        int len = list.size();
        for (int i = 0; i < len; i++) {
            NewsChannelEntity bean = list.get(i);
            insertChannel(bean.getId(), bean.getName(), bean.getOrderId(), bean.getSelected(), bean.getLock(), bean.getAdded(), bean.getType());
        }
    }
    public List<NewsChannelItem> getSelectedChannel() {
        List<NewsChannelItem> channels = new ArrayList<>();
        allChannels = GreenDaoManager.getInstance().getSession().getNewsChannelEntityDao().queryBuilder().build().list();
        for (int i = 0; i < allChannels.size(); i++) {
            if (allChannels.get(i).getSelected()) {
                NewsChannelItem channel = new NewsChannelItem();
                channel.setId(allChannels.get(i).getId());
                channel.setLock(allChannels.get(i).getLock());
                channel.setName(allChannels.get(i).getName());
                channel.setOrderId(allChannels.get(i).getOrderId());
                channel.setAdded(allChannels.get(i).getAdded());
                channel.setType(allChannels.get(i).getType());
                channels.add(channel);
            }
        }
        return channels;
    }

    //获取其他的频道
    public List<NewsChannelItem> getUnselectedChannel() {
        List<NewsChannelItem> channels = new ArrayList<>();
        int len = allChannels.size();
        for (int i = 0; i < len; i++) {
            if (!allChannels.get(i).getSelected()) {
                NewsChannelItem userChannel = new NewsChannelItem();
                userChannel.setId(allChannels.get(i).getId());
                userChannel.setName(allChannels.get(i).getName());
                userChannel.setLock(allChannels.get(i).getLock());
                userChannel.setOrderId(allChannels.get(i).getOrderId());
                userChannel.setAdded(allChannels.get(i).getAdded());
                userChannel.setType(allChannels.get(i).getType());
                channels.add(userChannel);
            }
        }
        return channels;
    }

    public List<String> getChannelNameList() {
        List<String> list = new ArrayList<>();
        List<String> mTabList = new ArrayList<>();

        int len = allChannels.size();
        for (int i = 0; i < len; i++) {
            String type = allChannels.get(i).getType();
            list.add(type);
        }
        LinkedHashSet<String> getTypes = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(getTypes);
        for (String type : list) {
            mTabList.add(type);
        }

        return mTabList;
    }

    /**
     * 保存用户频道到数据库
     *
     * @param channelList
     */
    public void saveChannel(List<NewsChannelItem> channelList) {
        for (int i = 0; i < channelList.size(); i++) {
            NewsChannelItem NewsChannelItem = channelList.get(i);
            insertChannel(NewsChannelItem.getId(), NewsChannelItem.getName(), i, true, NewsChannelItem.getLock(), NewsChannelItem.getAdded(), NewsChannelItem.getType());
        }
    }

    /**
     * 保存其他频道到数据库
     *
     * @param otherList
     */
    public void saveOtherChannel(List<NewsChannelItem> otherList) {
        int len = otherList.size();
        for (int i = 0; i < len; i++) {
            NewsChannelItem NewsChannelItem = otherList.get(i);
            insertChannel(NewsChannelItem.getId(), NewsChannelItem.getName(), i, false, NewsChannelItem.getLock(), NewsChannelItem.getAdded(), NewsChannelItem.getType());
        }

    }

    public void deleteAllChannel() {
        int len = allChannels.size();
        for (int i = 0; i < len; i++) {
            deleteChannel(allChannels.get(i).getId());
        }
    }

    /**
     * 根据名字更新某条数据的名字
     *
     * @param prevId   原Id
     * @param mBoolean 修改变量
     */
    private void updateAdded(String prevId, Boolean mBoolean) {
        NewsChannelEntity findChannel = GreenDaoManager.getInstance().getSession().getNewsChannelEntityDao().queryBuilder()
                .where(NewsChannelEntityDao.Properties.Id.eq(prevId)).build().unique();
        if (findChannel != null) {
            findChannel.setAdded(mBoolean);
            GreenDaoManager.getInstance().getSession().getNewsChannelEntityDao().update(findChannel);
        }
    }


    /**
     * 根据名字删除某频道
     *
     * @param id
     */
    private void deleteChannel(String id) {
        NewsChannelEntityDao channelDao = GreenDaoManager.getInstance().getSession().getNewsChannelEntityDao();
        NewsChannelEntity findChannel = channelDao.queryBuilder().where(NewsChannelEntityDao.Properties.Id.eq(id)).build().unique();
        if (findChannel != null) {
            channelDao.deleteByKey(findChannel.getId());
        }
    }

    /**
     * 本地数据里添加一个channel
     *
     * @param id   id
     * @param name 名字
     */
    private void insertChannel(String id, String name, int orderId, Boolean selected, Boolean lock, Boolean add, String type) {
        NewsChannelEntityDao channelDao = GreenDaoManager.getInstance().getSession().getNewsChannelEntityDao();

        selected = selected == null ? false : selected;

        NewsChannelEntity channel = new NewsChannelEntity(id, name, orderId, selected, lock, add, type);
        channelDao.insert(channel);
    }

}
