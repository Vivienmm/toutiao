package com.chinaso.toutiao.mvp.listener;

public class HomeButtonEvent {
    private int mTab;

    public HomeButtonEvent(int Tab){
        mTab=Tab;
    }
    public int getmTab() {
        return mTab;
    }

    public void setmTab(int mTab) {
        this.mTab = mTab;
    }
}
