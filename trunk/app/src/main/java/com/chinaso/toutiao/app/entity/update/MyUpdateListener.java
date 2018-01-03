package com.chinaso.toutiao.app.entity.update;

public interface MyUpdateListener {
    void onUpdateReturned(int updateStatus, VersionUpdateResponse updateInfo);
}
