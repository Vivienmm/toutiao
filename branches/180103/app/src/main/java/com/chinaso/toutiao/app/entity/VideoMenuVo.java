package com.chinaso.toutiao.app.entity;

import java.io.Serializable;

public class VideoMenuVo implements Serializable {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "id=" + id + "\n" + "name=" + name;
    }
}