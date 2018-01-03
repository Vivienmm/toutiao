package com.chinaso.toutiao.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 视频列表的每个item信息。
 */
public class VideoListItem implements Parcelable {
    private String videoApi;
    /** 网页视频播放 */
    private String url;
    private String title;
    private String description;
    private String picture;
    private String mname;
    private String time;
    private String duration;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getVideoApi() {
        return videoApi;
    }

    public void setVideoApi(String videoApi) {
        this.videoApi = videoApi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.videoApi);
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.picture);
        dest.writeString(this.mname);
        dest.writeString(this.time);
        dest.writeString(this.duration);
    }

    public VideoListItem() {
    }

    protected VideoListItem(Parcel in) {
        this.videoApi = in.readString();
        this.url = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.picture = in.readString();
        this.mname = in.readString();
        this.time = in.readString();
        this.duration = in.readString();
    }

    public static final Parcelable.Creator<VideoListItem> CREATOR = new Parcelable.Creator<VideoListItem>() {
        @Override
        public VideoListItem createFromParcel(Parcel source) {
            return new VideoListItem(source);
        }

        @Override
        public VideoListItem[] newArray(int size) {
            return new VideoListItem[size];
        }
    };
}
