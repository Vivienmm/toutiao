package com.chinaso.toutiao.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class NewsListItem implements Parcelable , Serializable{
    private String id;
    private String description;
    private String isComment;
    private String mname;
    private String nid;
    private String time;
    private String title;
    private String url;
    private String sign;
    private List<String> pictureList;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsComment() {
        return isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "title="+title+
                "\n desc="+description+
                "\n picus=" + pictureList.size()+
                "\n url01="+pictureList.get(0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.isComment);
        dest.writeString(this.mname);
        dest.writeString(this.nid);
        dest.writeString(this.time);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.sign);
        dest.writeStringList(this.pictureList);
    }

    public NewsListItem() {
    }

    protected NewsListItem(Parcel in) {
        this.id = in.readString();
        this.description = in.readString();
        this.isComment = in.readString();
        this.mname = in.readString();
        this.nid = in.readString();
        this.time = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.sign = in.readString();
        this.pictureList = in.createStringArrayList();
    }

    public static final Creator<NewsListItem> CREATOR = new Creator<NewsListItem>() {
        @Override
        public NewsListItem createFromParcel(Parcel source) {
            return new NewsListItem(source);
        }

        @Override
        public NewsListItem[] newArray(int size) {
            return new NewsListItem[size];
        }
    };
}
