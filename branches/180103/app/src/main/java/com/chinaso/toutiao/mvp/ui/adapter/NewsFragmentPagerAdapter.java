package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.chinaso.toutiao.mvp.data.NewsChannelItem;
import com.chinaso.toutiao.mvp.ui.fragment.NewsListFragment;

import java.util.List;

public class NewsFragmentPagerAdapter extends FragmentPagerAdapter<NewsChannelItem> {
    public NewsFragmentPagerAdapter(FragmentManager fm, List<NewsChannelItem> channels) {
        super(fm);
        selectedChannels = channels;
    }

    @Override
    public Fragment getItem(int position) {
        return NewsListFragment.newInstance(selectedChannels.get(position).getId());
    }
}
