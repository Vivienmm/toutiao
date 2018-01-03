package com.chinaso.toutiao.mvp.ui.module;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class DisableScrollLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled=true;


    public DisableScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public DisableScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag){
        this.isScrollEnabled=flag;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnabled&&super.canScrollVertically();
    }





}
