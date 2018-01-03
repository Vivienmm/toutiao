package com.chinaso.toutiao.mvp.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by chinaso on 2016/12/19.
 * 针对数据库存储,与NewsChannelItem是一一对应的关系
 */
@Entity
public class NewsChannelEntity {
    @Id
    private String id;//
    private String name;
    private int orderId;
    private Boolean selected;
    private Boolean lock;
    private Boolean added;
    private String type;

    @Generated(hash = 210615322)
    public NewsChannelEntity(String id, String name, int orderId, Boolean selected,
            Boolean lock, Boolean added, String type) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.selected = selected;
        this.lock = lock;
        this.added = added;
        this.type = type;
    }
    @Generated(hash = 1641572530)
    public NewsChannelEntity() {
    }
    public Boolean getSelected() {
        return this.selected;
    }
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
    public int getOrderId() {
        return this.orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public Boolean getAdded() {
        return added;
    }

    public void setAdded(Boolean added) {
        this.added = added;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((NewsChannelEntity)obj).getId());
    }

    @Override
    public String toString() {
        return "id="+id+"; name="+name;
    }
}
