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
import com.chinaso.toutiao.app.entity.VideoListItem;
import com.chinaso.toutiao.mvp.interactor.impl.NewsListInteractorImpl;
import com.chinaso.toutiao.mvp.listener.OnRecyclerItemClickListener;
import com.chinaso.toutiao.mvp.presenter.impl.VideoListPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.VideoActivity;
import com.chinaso.toutiao.mvp.ui.adapter.VideoListAdapter;
import com.chinaso.toutiao.mvp.ui.fragment.base.BaseFragment;
import com.chinaso.toutiao.mvp.view.ListViewView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.AbsListView.OnScrollListener;

public class VideoListFragment  extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ListViewView<List<VideoListItem>> {
    private String url;
    private VideoListAdapter videoAdapter = null;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    TextView empty_view;

    private boolean mIsAllLoaded;
    VideoListPresenterImpl listPresenter;

    public static VideoListFragment newInstance(String url) {

        VideoListFragment fragment = new VideoListFragment();
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

    @Override
    public int initLayout() {
        return R.layout.fragment_newslist;
    }

    @Override
    public void initViews(View view) {
        ButterKnife.bind(this, view);
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            savedInstanceState = this.getArguments();
            url = savedInstanceState.getString("url");
        }
        url = savedInstanceState.getString("url");
        initPresenter();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#d7a101"),Color.parseColor("#54c745"),Color.parseColor("#f16161"),Color.BLUE,Color.YELLOW);
    }

    private void initRecyclerView() {
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
                switch (newState) {
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                    case OnScrollListener.SCROLL_STATE_FLING:
                        if (!mIsAllLoaded && visibleItemCount > 0 && lastVisibleItemPosition >= totalItemCount - 1) {
                            listPresenter.loadMore();
                            recyclerView.scrollToPosition(videoAdapter.getItemCount() - 2);
                        }
                        break;
                    case OnScrollListener.SCROLL_STATE_IDLE:
                        break;
                }
            }
        });
        videoAdapter = new VideoListAdapter();
        recyclerView.setAdapter(videoAdapter);
        videoAdapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerClick(int position) {
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("videoInfo", videoAdapter.getLists().get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initPresenter() {
        listPresenter = new VideoListPresenterImpl(new NewsListInteractorImpl());
        listPresenter.setListVideoId(url);
        listPresenter.attachView(this);
        listPresenter.onCreate();
    }

    @Override
    public void onRefresh() {
        listPresenter.refreshData();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refresh(List<VideoListItem> items) {
        videoAdapter.setLists(items);
        if (items == null && videoAdapter.getLists() == null) {
            empty_view.setVisibility(View.VISIBLE);
        } else {
            empty_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadMore(List<VideoListItem> items) {
        videoAdapter.addMore(items);
        swipeRefreshLayout.setRefreshing(false);
        mIsAllLoaded = false;
    }

    public void showErrorMsg(String message) {
        Toast.makeText(getActivity(), "message:" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listPresenter.onDestory();
    }
}
