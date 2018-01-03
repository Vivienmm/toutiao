package com.chinaso.toutiao.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.chinaso.toutiao.app.entity.VideoMenuVo;
import com.chinaso.toutiao.mvp.ui.fragment.VideoListFragment;

import java.util.List;

public class VideoFragmentPagerAdapter extends FragmentPagerAdapter<VideoMenuVo> {

    public VideoFragmentPagerAdapter(FragmentManager fm, List<VideoMenuVo> channels) {
        super(fm);
        selectedChannels = channels;
    }

    @Override
    public Fragment getItem(int position) {

        return VideoListFragment.newInstance(selectedChannels.get(position).getId());
    }
}
