package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class FragmentPagerAdapter<T> extends FragmentStatePagerAdapter {
    protected List<T> selectedChannels = new ArrayList<>();
    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

//    @Override
//    public Fragment getItem(int position) {
//
//        return VideoListFragment.newInstance(selectedChannels.get(position).getId());
//    }

    @Override
    public int getCount() {
        return selectedChannels.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void clear() {
        selectedChannels.clear();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }
}
