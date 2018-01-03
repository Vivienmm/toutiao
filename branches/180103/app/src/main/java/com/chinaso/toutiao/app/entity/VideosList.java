package com.chinaso.toutiao.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class VideosList implements Parcelable {
    private List<VideoListItem> list;

    public List<VideoListItem> getList() {
        return list;
    }

    public void setList(List<VideoListItem> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
    }

    public VideosList() {
    }

    protected VideosList(Parcel in) {
        this.list = in.createTypedArrayList(VideoListItem.CREATOR);
    }

    public static final Parcelable.Creator<VideosList> CREATOR = new Parcelable.Creator<VideosList>() {
        @Override
        public VideosList createFromParcel(Parcel source) {
            return new VideosList(source);
        }

        @Override
        public VideosList[] newArray(int size) {
            return new VideosList[size];
        }
    };
}
