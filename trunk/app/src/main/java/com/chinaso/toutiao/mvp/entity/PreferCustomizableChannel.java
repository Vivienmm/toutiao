package com.chinaso.toutiao.mvp.entity;

public class PreferCustomizableChannel {
    private String channel;
    private int resId;
    private boolean selected;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "PreferCustomizableChannel=" + channel + "; resId="+resId+"; selected="+selected;
    }
}
