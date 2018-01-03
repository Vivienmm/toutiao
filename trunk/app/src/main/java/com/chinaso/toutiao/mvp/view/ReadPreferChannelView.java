package com.chinaso.toutiao.mvp.view;

import com.chinaso.toutiao.mvp.entity.PreferCustomizableChannel;
import com.chinaso.toutiao.mvp.view.base.BaseView;

import java.util.List;

public interface ReadPreferChannelView extends BaseView{
    /** 初始化控件状态 */
    void initView(List<PreferCustomizableChannel> lists);
    /** 男性选择偏好 */
    void selectBoyState();
    /** 女性选择偏好 */
    void selectGirlState();
    /** 确认键是否可以操作 */
    void confirmState(boolean state);
    /** 确认后的提示 */
    void showSuccessSelected();
}
