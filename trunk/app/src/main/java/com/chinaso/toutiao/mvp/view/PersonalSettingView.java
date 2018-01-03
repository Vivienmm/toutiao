package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.mvp.view.base.BaseView;

/**
 * Created by Administrator on 17-3-9.
 */

public interface PersonalSettingView extends BaseView {
    public String getNickName();
    public void showNickName(String name);
    public void showAvatar();
    public void showSex(String info);
    public void showBirthDay(String info);
    public void showDeviceName(String info);
    public void finishActivity();
}
