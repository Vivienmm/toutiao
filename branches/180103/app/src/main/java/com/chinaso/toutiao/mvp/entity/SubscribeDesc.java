package com.chinaso.toutiao.mvp.entity;

/**
 * Created by Administrator on 17-3-1.
 */

public class SubscribeDesc {
    private String name;
    private String imgUrl;
    private String desc;
    private boolean isSubscribe;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    private String column;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SubscribeDesc: { name:" + name + ", desc:" + desc + "; isSubscribe:" + isSubscribe + " }";
    }
}
