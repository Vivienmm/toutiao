package com.chinaso.toutiao.mvp.entity;

/**
 * Created by Administrator on 17-3-8.
 */

public class SubscribeChannel {
    private String name;
    private boolean isSelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
