package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ImagePager extends PagerAdapter {
    private List<ViewGroup> strDrawables;

    public  ImagePager(List<ViewGroup> strDrawables) {
        this.strDrawables=new ArrayList<>(strDrawables);
    }

    @Override
    public int getCount() {
        return strDrawables.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        ViewGroup imageView = strDrawables.get(position);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
