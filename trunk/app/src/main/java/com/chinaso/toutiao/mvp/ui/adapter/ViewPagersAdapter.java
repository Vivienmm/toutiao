package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

public class ViewPagersAdapter extends PagerAdapter {
    private List<View> mPagerViewList;

    public ViewPagersAdapter(List<View> mPagerViewList) {
        this.mPagerViewList = mPagerViewList;
    }

    @Override
    public int getCount() {
        if (mPagerViewList.size() > 1) {
            return Short.MAX_VALUE;
        }
        return mPagerViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mPagerViewList.get(position % mPagerViewList.size());
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null) {
                container.removeView(view);
            }
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
    }

}
