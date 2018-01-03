package com.chinaso.toutiao.mvp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.data.NewsChannelItem;
import com.chinaso.toutiao.mvp.data.NewsChannelManageDao;
import com.chinaso.toutiao.mvp.ui.activity.MgChannelActivity;
import com.chinaso.toutiao.mvp.ui.activity.SearchNewsActivity;
import com.chinaso.toutiao.mvp.ui.adapter.NewsFragmentPagerAdapter;
import com.chinaso.toutiao.mvp.ui.fragment.base.BaseFragment;

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
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @BindView(R.id.channel_more)
    ImageView moreBtn;
    @BindView(R.id.searchNewsImgView)
    ImageView searchNewsImgView;

    private CommonNavigator mCommonNavigator;
    private static int cIndex = 0;

    private List<NewsChannelItem> channels = new ArrayList<>();
    private final int CODE_REQUEST_MNG = 1;
    private final int CODE_IS_CHANGE = 1;
    private ColorTransitionPagerTitleView simplePagerTitleView;
    private CommonNavigatorAdapter mIndicatorAdapter;

    @Override
    public int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initViews(View view) {
        initData();
    }

    @OnClick(R.id.searchNewsImgView)
    public void searchNewsImgView() {
        startActivity(new Intent(getActivity(), SearchNewsActivity.class));
    }

    private void initData() {


        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), MgChannelActivity.class), CODE_REQUEST_MNG);
            }
        });


        TTApplication.isNightMode = SharedPreferencesSetting.getIsNightMode();
        if (TTApplication.isNightMode) {
            mMagicIndicator.setBackgroundColor(getResources().getColor(R.color.nightBackground));
        } else {
            mMagicIndicator.setBackgroundColor(getResources().getColor(R.color.white));
        }

        mCommonNavigator = new CommonNavigator(getActivity());
        mCommonNavigator.setSkimOver(false);
        mCommonNavigator.setFollowTouch(false);

        initChannel();

        mViewPager.setAdapter(new NewsFragmentPagerAdapter(getChildFragmentManager(), channels));
        mIndicatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return channels.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                simplePagerTitleView = new ColorTransitionPagerTitleView(context);
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
        mCommonNavigator.setAdapter(mIndicatorAdapter);
        //回到之前的index
        if (channels.size() > cIndex) {
            mViewPager.setCurrentItem(cIndex);
            mCommonNavigator.onPageSelected(cIndex);//返回后继续原来的index
        }

        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);//核心
    }

    private void initChannel() {
        NewsChannelManageDao manageDao = new NewsChannelManageDao();
        channels = manageDao.getSelectedChannel();
    }

    public void initIndicator(boolean bn) {

        if (bn) {
            mMagicIndicator.setBackgroundColor(getResources().getColor(R.color.nightBackground));
            simplePagerTitleView.setNormalColor(Color.WHITE);
            mIndicatorAdapter.notifyDataSetChanged();
        } else {
            mMagicIndicator.setBackgroundColor(Color.WHITE);
            simplePagerTitleView.setNormalColor(Color.BLACK);
            mIndicatorAdapter.notifyDataSetChanged();

        }
    }
}