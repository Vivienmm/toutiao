package com.chinaso.toutiao.mvp.data.collection;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by chinaso on 2016/12/22.
 * 针对数据库存储,与CollectionItem一一对应
 */


@Entity
public class CollectionEntity {

    @Id(autoincrement = true)
    Long id;
    String title;
    @Unique
    String url; //不重复
    int type;

    @Generated(hash = 1606379032)
    public CollectionEntity(Long id, String title, String url, int type) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.type = type;
    }

    @Generated(hash = 1951715304)
    public CollectionEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
