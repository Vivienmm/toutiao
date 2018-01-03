package com.chinaso.toutiao.util;

import android.net.Uri;
import android.os.Bundle;

import com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;


/**
 * 匹配action 与网页交互
 */
public class SolveUrl {

    private String contentUrl;
    private String scheme;
    private String action;
    private String title;
    private String id;
    private String content;

    public static final String SCHEME = "chinaso.app";

    public static final String SHOW_NEWS = "ShowNews";
    public static final String SHOW_WEB_AND_REFRESH = "ShowWebAndRefresh";
    public static final String SHOW_MANAGER = "ShowManager";
    public static final String SHOW_WEB = "ShowWeb";

    public SolveUrl(String url) {
        Uri uri = Uri.parse(url);
        contentUrl = uri.getQueryParameter("url");
        scheme = uri.getScheme();
        action = uri.getQueryParameter("action");
        title = uri.getQueryParameter("title");
        id = uri.getQueryParameter("id");
        content = uri.getQueryParameter("content");
    }

    public boolean dispatchAction(BaseActivity from, Object... args) {

        if (!scheme.equals(SCHEME)) {
            return false;
        }

        switch (action) {

            case SHOW_NEWS:
                Bundle aBundleShowNews = new Bundle();
                aBundleShowNews.putString("newsShowUrl", contentUrl);
                from.startActivityForResult(VerticalDetailActivity.class, aBundleShowNews, 5);
                break;
            case SHOW_MANAGER:
            case SHOW_WEB_AND_REFRESH:
                Bundle aBundleShowManager = new Bundle();
                aBundleShowManager.putString("newsShowUrl", contentUrl);
                from.startActivityForResult(VerticalDetailActivity.class, aBundleShowManager, 4);
                break;

            default:
                break;
        }
        return true;
    }
}
