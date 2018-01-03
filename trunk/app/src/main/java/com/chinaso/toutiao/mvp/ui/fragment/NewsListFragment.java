package com.chinaso.toutiao.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.entity.NewsListItem;
import com.chinaso.toutiao.mvp.data.readhistory.ReadHistoryEntity;
import com.chinaso.toutiao.mvp.data.readhistory.ReadHistoryManageDao;
import com.chinaso.toutiao.mvp.interactor.impl.NewsListInteractorImpl;
import com.chinaso.toutiao.mvp.listener.OnRecyclerItemClickListener;
import com.chinaso.toutiao.mvp.presenter.impl.NewsListPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.NewsDetailActivity;
import com.chinaso.toutiao.mvp.ui.adapter.NewsListAdapter;
import com.chinaso.toutiao.mvp.ui.fragment.base.BaseFragment;
import com.chinaso.toutiao.mvp.view.ListViewView;
import com.chinaso.toutiao.view.RecycleViewDivider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_FLING;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

public class NewsListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ListViewView<List<NewsListItem>> {
    private String url;
    private NewsListAdapter newsAdapter = null;
    public final static String EXTRA_BUNDLE = "NEWS_ENTERY";
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    TextView empty_view;

    private ReadHistoryManageDao readHistoryManageDao = new ReadHistoryManageDao();
    private boolean mIsAllLoaded;
    private NewsListPresenterImpl listPresenter;
    public static NewsListFragment newInstance(String url) {

        NewsListFragment fragment = new NewsListFragment();
        Bundle arguments = new Bundle();
        arguments.putString("url", url);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            savedInstanceState = this.getArguments();
            url = savedInstanceState.getString("url");
        }
        url = savedInstanceState.getString("url");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        listPresenter.onSaveState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listPresenter.onRestoreState(savedInstanceState);
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public int initLayout() {
        return R.layout.fragment_newslist;
    }

    @Override
    public void initViews(View view) {
        initSwipeRefreshLayout();
        initRecyclerView();
        initPresenter();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#d7a101"),Color.parseColor("#54c745"),Color.parseColor("#f16161"),Color.BLUE,Color.YELLOW);
    }

    private void initRecyclerView() {
        //        FastScrollLinearLayoutManager manager = new FastScrollLinearLayoutManager(getActivity());
//        manager.setSpeedFast();
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), RecycleViewDivider.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                        .findLastVisibleItemPosition();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
//                Log.i("dsc", "onScrollStateChanged: before;\n lastVisibleItemposition="+lastVisibleItemPosition
//                    +"\n misalloaded="+mIsAllLoaded
//                    +"\n totalItemcount="+totalItemCount);
                switch (newState) {
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                    case SCROLL_STATE_FLING:
                        if (!mIsAllLoaded && visibleItemCount > 0 && lastVisibleItemPosition >= totalItemCount - 1) {
                            listPresenter.loadMore();
                            recyclerView.scrollToPosition(newsAdapter.getItemCount() - 2);
                        }
                        break;
                    case SCROLL_STATE_IDLE:
                        break;
                }
            }
        });
        newsAdapter = new NewsListAdapter();
        recyclerView.setAdapter(newsAdapter);
        newsAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerClick(int position) {
                Date curDate = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dateStr = formatter.format(curDate);
                NewsListItem item = newsAdapter.getLists().get(position);
                ReadHistoryEntity entity = new ReadHistoryEntity();
                entity.setTitle(item.getTitle());
                entity.setUrl(item.getUrl());
                entity.setReadDate(dateStr);
                readHistoryManageDao.insertItem(entity);

//                Intent intent = new Intent(getActivity(), VerticalDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_BUNDLE, newsAdapter.getLists().get(position));
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initPresenter() {
        listPresenter = new NewsListPresenterImpl(new NewsListInteractorImpl());
        listPresenter.setListNewsId(url);
        listPresenter.attachView(this);
        listPresenter.onCreate();
    }

    /** swipeRefreshLayout */
    @Override
    public void onRefresh() {
        listPresenter.refreshData();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refresh(List<NewsListItem> items) {
        newsAdapter.setLists(items);
        if (items == null && newsAdapter.getLists() == null) {
            empty_view.setVisibility(View.VISIBLE);
        } else {
            empty_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadMore(List<NewsListItem> items) {
        newsAdapter.addMore(items);
        swipeRefreshLayout.setRefreshing(false);
        mIsAllLoaded = false;
    }

    @Override
    public void showErrorMsg(String message) {
        Toast.makeText(getActivity(), "message:" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listPresenter.onDestory();
    }
}
