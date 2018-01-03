package com.chinaso.toutiao.mvp.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.data.NewsChannelItem;
import com.chinaso.toutiao.mvp.listener.OnRecyclerItemClickListener;
import com.chinaso.toutiao.mvp.presenter.MgChannelPresenter;
import com.chinaso.toutiao.mvp.presenter.impl.MgChannelPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.ui.adapter.RecyclerDragAdapter;
import com.chinaso.toutiao.mvp.ui.adapter.UnSelectedRecyclerAdapter;
import com.chinaso.toutiao.mvp.ui.module.DisableScrollLinearLayoutManager;
import com.chinaso.toutiao.mvp.ui.module.DragTouchHelper;
import com.chinaso.toutiao.mvp.ui.module.OnGridItemClickListener;
import com.chinaso.toutiao.mvp.view.MgChannelView;
import com.chinaso.toutiao.view.CustomActionBar;
import com.chinaso.toutiao.view.RecycleItemDivider;
import com.chinaso.toutiao.view.ScrollRecycleView;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MgChannelActivity extends BaseActivity implements MgChannelView {

    @BindView(R.id.other_channels)
    ScrollRecycleView otherListView;
    @BindView(R.id.userGridView)
    RecyclerView userRecycleView;
    @BindView(R.id.actionbar)
    CustomActionBar titleBar;
    @BindView(R.id.tab_indicator)
    MagicIndicator tabIndicator;

    private List<NewsChannelItem> otherChannelList = new ArrayList<>();
    //用户栏目列表
    private List<NewsChannelItem> userChannelList = new ArrayList<>();
    private List<NewsChannelItem> showDataList = new ArrayList<>();
    private List<String> mTabList = new ArrayList<>();
    private UnSelectedRecyclerAdapter unSelectedAdapter;
    private int tabIndex;
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();
    private DragTouchHelper dragTouchHelper;
    private RecyclerDragAdapter userAdapter;
    private MgChannelPresenter mgChannelPresenter = new MgChannelPresenterImpl();

    @Override
    public int getLayoutId() {
        return R.layout.activity_mg_channel;
    }

    @Override
    public void initViews() {
        mgChannelPresenter.attachView(this);
        mgChannelPresenter.onCreate();
        titleBar.setTitleView("频道管理");
        titleBar.setLeftViewImg(R.mipmap.actionbar_back);
        titleBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                backAction();
            }
        });
        userAdapter = new RecyclerDragAdapter(MgChannelActivity.this, userChannelList);
        userRecycleView.setAdapter(userAdapter);
        userRecycleView.setLayoutManager(new GridLayoutManager(this, 4));

        dragTouchHelper = new DragTouchHelper(MgChannelActivity.this, userChannelList, userAdapter);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(dragTouchHelper);
        userRecycleView.addOnItemTouchListener(new OnGridItemClickListener(userRecycleView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                addToUnSelectList(position);
            }

            @Override
            public void onLongPress(RecyclerView.ViewHolder holder, int position) {
                //如果不是最后一个就拖拽
                if (holder.getLayoutPosition() != userChannelList.size() - 1) {
                    itemTouchHelper.startDrag(holder);
                }

            }
        });

        itemTouchHelper.attachToRecyclerView(userRecycleView);


        unSelectedAdapter = new UnSelectedRecyclerAdapter(MgChannelActivity.this, showDataList);
        DisableScrollLinearLayoutManager layoutManager = new DisableScrollLinearLayoutManager(this);
        layoutManager.setScrollEnabled(false);
        otherListView.setLayoutManager(layoutManager);
        otherListView.setAdapter(unSelectedAdapter);
        otherListView.addItemDecoration(new RecycleItemDivider(this));
        unSelectedAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerClick(final int position) {
                addToUserList(position);
            }
        });
    }


    @Override
    public void setDragData(List list) {
        userChannelList = list;
        userAdapter = new RecyclerDragAdapter(MgChannelActivity.this, userChannelList);
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUnSelectedChannel(List list) {
        otherChannelList = list;
        getTabDatas(mTabList.get(0));
        unSelectedAdapter = new UnSelectedRecyclerAdapter(MgChannelActivity.this, showDataList);
        unSelectedAdapter.notifyDataSetChanged();
    }

    @Override
    public void initIndicator(List list) {
        mTabList = list;
        getTabDatas(mTabList.get(0));
        tabIndicator.setBackgroundResource(R.drawable.indicator_bg);
        CommonNavigator mCommonNavigator = new CommonNavigator(MgChannelActivity.this);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mTabList == null ? 0 : mTabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);

                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mTabList.get(index) + "频道");
                clipPagerTitleView.setTextColor(Color.parseColor("#bc2a2a"));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tabIndex = index;
                        mFragmentContainerHelper.handlePageSelected(index);
                        //数据更新
                        getTabDatas(mTabList.get(index));
                        unSelectedAdapter.notifyDataSetChanged();
                    }
                });
                badgePagerTitleView.setInnerPagerTitleView(clipPagerTitleView);

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                float navigatorHeight = context.getResources().getDimension(R.dimen.common_navigator_height);
                float borderWidth = UIUtil.dip2px(context, 1);
                float lineHeight = navigatorHeight - 2 * borderWidth;
                indicator.setLineHeight(lineHeight);
                indicator.setRoundRadius(10);
                indicator.setYOffset(borderWidth);
                indicator.setColors(Color.parseColor("#bc2a2a"));
                return indicator;
            }
        });

        tabIndicator.setNavigator(mCommonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(tabIndicator);
    }

    private void getTabDatas(String string) {
        showDataList.clear();
        for (int i = 0; i < otherChannelList.size(); i++) {
            if (otherChannelList.get(i).getType().equals(string)) {
                showDataList.add(otherChannelList.get(i));
            }
        }
    }

    private void addToUserList(final int position) {
        final NewsChannelItem channel = unSelectedAdapter.getChannnelList().get(position);
        //hasChanged = true;
        delete(channel.getId());
        showDataList.remove(position);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                try {
                    userChannelList.add(channel);
                    userAdapter.notifyDataSetChanged();

                    unSelectedAdapter.notifyDataSetChanged();
                } catch (Exception localException) {
                    localException.printStackTrace();
                }
            }
        }, 50L);
    }

    private void delete(String s) {
        for (int i = 0; i < otherChannelList.size(); i++) {
            if (otherChannelList.get(i).getId().equals(s)) {
                otherChannelList.remove(i);
            }
        }

    }

    private void addToUnSelectList(final int position) {
        if (!userChannelList.get(position).getLock()) {//是否可拖拽

            //  hasChanged = true;
            final NewsChannelItem channel = userAdapter.getList().get(position);//获取点击的频道内容
            otherChannelList.add(channel);
            getTabDatas(mTabList.get(tabIndex));

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    try {
                        userChannelList.remove(position);
                        userAdapter.notifyDataSetChanged();
                        //数据更新
                        unSelectedAdapter.notifyDataSetChanged();

                    } catch (Exception localException) {
                        localException.printStackTrace();
                    }
                }
            }, 50L);
        }
    }


    @Override
    public List getUserList() {
        List<NewsChannelItem> userChannels = userAdapter.getList();

        for (int i = 0; i < userChannels.size(); i++) {
            userChannels.get(i).setAdded(false);
        }
        return userChannels;
    }

    @Override
    public List getUnSelectedUserList() {
        for (int i = 0; i < otherChannelList.size(); i++) {
            otherChannelList.get(i).setAdded(false);
        }
        return otherChannelList;
    }

    @Override
    public void showErrorMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        backAction();
    }


    private void backAction() {
        mgChannelPresenter.saveChannels();
        MgChannelActivity.this.finish();
    }

}
