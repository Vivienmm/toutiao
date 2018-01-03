package com.chinaso.toutiao.mvp.entity;

/**
 * Created by Administrator on 17-3-16.
 * 首页启动信息
 */

public class StartUpEntity {
    /**
     * endTime :
     * id : fc30335a7324986b679960f50609cbd4
     * imgUrl : http://n4.cmsfile.pg0.cn/group1/M00/FA/AB/Cgqg11i3iF6AJIPnAArdFpUzfSo339.png
     * linkUrl :
     * startTime : 2017-3-2
     * title : 风景
     * type : 0
     */

    private String endTime;
    private String id;
    private String imgUrl;
    private String linkUrl;
    private String startTime;
    private String title;
    private int type;  //1是新闻详情，其他默认进入搜索结果页

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "启动页信息：id=" + id +"\n"
                + "title=" + title + "\n"
                +"imageUrl=" + imgUrl + "\n"
                + "type=" + type;
    }
}
