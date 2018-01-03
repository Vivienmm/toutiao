package com.chinaso.toutiao.mvp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class NewsChannelItem implements Serializable, Parcelable {

    /**
     * 栏目对应ID，后台提供
     */
    private String id;
    /**
     * 栏目对应NAME
     */
    private String name;
    /**
     * 栏目在整体中的排序顺序  rank
     */
    private Integer orderId;
    /**
     * 栏目是否选中
     */
    private Integer selected;
    //是否lock
    private Boolean lock;

    //是否是新增加的
    private Boolean added;
    //栏目type
    private String Type;


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Boolean getAdded() {
        return added;
    }

    public void setAdded(Boolean added) {
        this.added = added;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getOrderId() {
        return orderId;
    }

    public Integer getSelected() {
        return this.selected;
    }

    public void setId(String paramInt) {
        this.id = paramInt;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public void setOrderId(int paramInt) {
        this.orderId = paramInt;
    }

    public void setSelected(Integer paramInteger) {
        this.selected = paramInteger;
    }

    public String toString() {
        return "ChannelItem [id=" + this.id + ", name=" + this.name
                + ", selected=" + this.selected + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.orderId);
        dest.writeValue(this.selected);
        dest.writeValue(this.lock);
        dest.writeValue(this.added);
        dest.writeString(this.Type);
    }

    public NewsChannelItem() {
    }

    protected NewsChannelItem(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.orderId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.selected = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lock = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.added = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.Type = in.readString();
    }

    public static final Parcelable.Creator<NewsChannelItem> CREATOR = new Parcelable.Creator<NewsChannelItem>() {
        @Override
        public NewsChannelItem createFromParcel(Parcel source) {
            return new NewsChannelItem(source);
        }

        @Override
        public NewsChannelItem[] newArray(int size) {
            return new NewsChannelItem[size];
        }
    };
}
