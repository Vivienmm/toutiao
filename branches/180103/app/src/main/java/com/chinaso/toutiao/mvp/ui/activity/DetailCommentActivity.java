package com.chinaso.toutiao.mvp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.util.DebugUtil;
import com.chinaso.toutiao.util.NetWorkStatusUtil;
import com.chinaso.toutiao.util.SolveUrl;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.view.BaseWebView;
import com.chinaso.toutiao.view.CustomActionBar;
import com.chinaso.toutiao.view.NetWorkErrorView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

import static com.chinaso.toutiao.R.id.detailCommentActionbar;


public class DetailCommentActivity extends BaseActivity {
    private static final String TAG = DetailCommentActivity.class.getSimpleName();
    private String urlIntent;
    private String title;
    private Bundle bundle;

    @BindView(detailCommentActionbar)
    CustomActionBar mActionBar;
    @BindView(R.id.detailCommentErrorview)
    NetWorkErrorView detailCommentErrorview;
    @BindView(R.id.webView)
    BaseWebView mWebView;


    @Override
    public int getLayoutId() {
        return R.layout.activity_detail_comment;
    }

    @Override
    public void initViews() {
        initData();
        initActionBar();
        initWebView();
    }

    private void initData() {
        bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        //我的评论
        if (getIntent().hasExtra("commentUrl")) {
            urlIntent = bundle.getString("commentUrl");
            DebugUtil.e(TAG, "urlIntent:" + urlIntent);
        }
        //功能介绍
        if (bundle.containsKey("title")) {
            title = bundle.getString("title");
            DebugUtil.e(TAG, "title:" + title);
        }
        if (bundle.containsKey("url")) {
            urlIntent = bundle.getString("url");
            DebugUtil.e(TAG, "urlIntent:" + urlIntent);
        }
        //国搜协议
        if (bundle.containsKey("server")) {
            urlIntent = bundle.getString("server");
            DebugUtil.e(TAG, "urlIntent:" + urlIntent);
        }
        //举报问题
        if (bundle.containsKey("report")) {
            urlIntent = bundle.getString("report");
            DebugUtil.e(TAG, "urlIntent" + urlIntent);
        }
    }

    private void initActionBar() {
        mActionBar.setLeftViewImg(R.mipmap.actionbar_back);
        if (!TextUtils.isEmpty(title)) {
            mActionBar.setTitleView(title);
        } else {
            mActionBar.setTitleView("我的评论");
        }
        if (bundle.containsKey("server")) {
            mActionBar.setTitleView("国搜协议");
        }
        if (bundle.containsKey("report")) {
            mActionBar.setTitleView("举报问题");
            mActionBar.setVisibility(View.GONE);
        }
        mActionBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                finish();
            }
        });
    }

    private void initWebView() {
        mWebView.loadUrl(urlIntent);
        mWebView.setWebViewClient(new CommentDetailWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (message != null) {
                    ToastUtil.showToast(DetailCommentActivity.this, message, 0);
                }
                result.cancel();
                return true;
            }

        });
        detailCommentErrorview.setOnClickListener(new NetWorkErrorView.ReloadInterface() {
            @Override
            public void reloadWebview() {
                if (NetWorkStatusUtil.isNetworkAvailable(DetailCommentActivity.this)) {
                    mWebView.reload();
                    mWebView.setVisibility(View.VISIBLE);
                    detailCommentErrorview.setVisibility(View.GONE);
                } else {
                    ToastUtil.showToast(DetailCommentActivity.this, getResources().getString(R.string.register_loda_failure), 0);
                }
            }
        });
    }

    private class CommentDetailWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url)) {
                SolveUrl solveUrl = new SolveUrl(url);
                return solveUrl.dispatchAction(DetailCommentActivity.this);
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mWebView.setVisibility(View.GONE);
            detailCommentErrorview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
