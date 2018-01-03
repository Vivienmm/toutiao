package com.chinaso.toutiao.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class NewsList implements Parcelable {
//    private String slide;
    private List<NewsListItem> list;

    public List<NewsListItem> getList() {
        return list;
    }

    public void setList(List<NewsListItem> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.list);
    }

    public NewsList() {
    }

    protected NewsList(Parcel in) {
        this.list = new ArrayList<>();
        in.readList(this.list, NewsListItem.class.getClassLoader());
    }

    public static final Parcelable.Creator<NewsList> CREATOR = new Parcelable.Creator<NewsList>() {
        @Override
        public NewsList createFromParcel(Parcel source) {
            return new NewsList(source);
        }

        @Override
        public NewsList[] newArray(int size) {
            return new NewsList[size];
        }
    };

    @Override
    public String toString() {
        return "长度="+list.size();
    }
}
