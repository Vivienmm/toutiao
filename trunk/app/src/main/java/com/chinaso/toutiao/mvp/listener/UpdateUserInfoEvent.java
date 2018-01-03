package com.chinaso.toutiao.mvp.listener;

public class UpdateUserInfoEvent {

    private boolean mFlag;
    public boolean getFlag() {
        return mFlag;
    }

    public void setFlag(boolean mFlag) {
        this.mFlag = mFlag;
    }

    public UpdateUserInfoEvent(boolean flag){
        mFlag=flag;
    }

}
