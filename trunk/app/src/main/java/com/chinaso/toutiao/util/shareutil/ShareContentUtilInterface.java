package com.chinaso.toutiao.util.shareutil;


import com.chinaso.toutiao.mvp.entity.ShareInfoEntity;

public interface ShareContentUtilInterface {
    void startShare(ShareInfoEntity shareEntity, int i);
    void onDestoryShare();
}
