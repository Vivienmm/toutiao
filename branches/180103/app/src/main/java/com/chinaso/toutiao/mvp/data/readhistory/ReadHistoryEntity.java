package com.chinaso.toutiao.mvp.data.readhistory;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


/**
 * Created by chinaso on 2017/2/28.
 */
@Entity
public class ReadHistoryEntity {
    @Id(autoincrement = true)
    Long id;
    String title;
    String url;
    String readDate;

    @Generated(hash = 1888916520)
    public ReadHistoryEntity(Long id, String title, String url, String readDate) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.readDate = readDate;
    }

    @Generated(hash = 1527132846)
    public ReadHistoryEntity() {
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

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
