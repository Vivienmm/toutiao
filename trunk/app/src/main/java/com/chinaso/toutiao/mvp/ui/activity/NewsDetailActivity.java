package com.chinaso.toutiao.mvp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.entity.NewsListItem;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.util.MyWebSetting;
import com.chinaso.toutiao.view.CustomActionBar;

import butterknife.BindView;

import static com.chinaso.toutiao.mvp.ui.fragment.NewsListFragment.EXTRA_BUNDLE;

/**
 * 1、头条的新闻详情是Webview+ListView<p>
 * 2、网易的新闻详情是HTML+json数据实现的<p>
 */

public class NewsDetailActivity extends BaseActivity {
    @BindView(R.id.actionbar)
    CustomActionBar actionbar;
    @BindView(R.id.newsDetailWeb)
    WebView newsDetailWeb;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initViews() {
        initData();
        initBar();
        initWeb();

    }

    private void initBar() {
        actionbar.setTitleView("国搜头条");
        actionbar.setLeftViewImg(R.mipmap.actionbar_back);
        actionbar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                NewsDetailActivity.this.finish();
            }
        });
    }

    private void initWeb() {
        MyWebSetting setting = new MyWebSetting(this, newsDetailWeb.getSettings());
        setting.setDatabasePath();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        NewsListItem item = bundle.getParcelable(EXTRA_BUNDLE);
        if (TextUtils.isEmpty(item.getUrl()))
            return;
        String url = item.getUrl();
        newsDetailWeb.loadUrl(url);
    }

}
