package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.mvp.view.base.BaseView;

import java.util.List;

public interface MgChannelView extends BaseView {
    void setDragData(List list);
    void setUnSelectedChannel(List list);
    void initIndicator(List list);
    List getUserList();
    List getUnSelectedUserList();
}
