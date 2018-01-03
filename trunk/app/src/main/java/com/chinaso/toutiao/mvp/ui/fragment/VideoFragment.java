package com.chinaso.toutiao.mvp.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.entity.VideoMenuVo;
import com.chinaso.toutiao.mvp.ui.adapter.VideoFragmentPagerAdapter;
import com.chinaso.toutiao.mvp.ui.fragment.base.BaseFragment;
import com.chinaso.toutiao.util.AppInitDataUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VideoFragment extends BaseFragment {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;

    private CommonNavigator mCommonNavigator;
    private static int cIndex = 0;

    private List<VideoMenuVo> channels = new ArrayList<>();
    private final int CODE_REQUEST_MNG = 1;
    private ColorTransitionPagerTitleView simplePagerTitleView;
    private CommonNavigatorAdapter magicAdapter;

    @Override
    public int initLayout() {
        return R.layout.fragment_video;
    }

    @Override
    public void initViews(View view) {
        TTApplication.isNightMode = SharedPreferencesSetting.getIsNightMode();
        if (TTApplication.isNightMode) {
            mMagicIndicator.setBackgroundColor(getResources().getColor(R.color.nightBackground));
        } else {
            mMagicIndicator.setBackgroundColor(Color.WHITE);
        }
        mCommonNavigator = new CommonNavigator(getActivity());
        mCommonNavigator.setSkimOver(false);
        mCommonNavigator.setFollowTouch(false);

        channels = AppInitDataUtil.getVideoMenuVoList();

        mViewPager.setAdapter(new VideoFragmentPagerAdapter(getChildFragmentManager(), channels));
        magicAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return channels.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                //simplePagerTitleView.setNormalColor(R.color.black);
                if (TTApplication.isNightMode) {
                    simplePagerTitleView.setNormalColor(Color.WHITE);
                } else {
                    simplePagerTitleView.setNormalColor(Color.BLACK);
                }
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.colorAccent));
                simplePagerTitleView.setText(channels.get(index).getName());
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            //设置下划线
            @Override
            public IPagerIndicator getIndicator(Context context) {
                //return null;
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.colorAccent));
                return indicator;
            }
        };
        mCommonNavigator.setAdapter(magicAdapter);
        //回到之前的index
        if (channels.size() > cIndex) {
            mViewPager.setCurrentItem(cIndex);
            mCommonNavigator.onPageSelected(cIndex);//返回后继续原来的index
        }
        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);


    }

    public void initIndicator(boolean bn) {

        if (bn) {
            mMagicIndicator.setBackgroundColor(getResources().getColor(R.color.nightBackground));
            simplePagerTitleView.setNormalColor(Color.WHITE);
            //magicAdapter.notifyDataSetInvalidated();
            magicAdapter.notifyDataSetChanged();
        } else {
            mMagicIndicator.setBackgroundColor(Color.WHITE);
            simplePagerTitleView.setNormalColor(Color.BLACK);
            //magicAdapter.notifyDataSetInvalidated();
            magicAdapter.notifyDataSetChanged();

        }
    }
}