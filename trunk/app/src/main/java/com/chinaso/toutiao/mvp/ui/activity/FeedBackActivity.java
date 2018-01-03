package com.chinaso.toutiao.mvp.ui.activity;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.util.AppInitDataUtil;
import com.chinaso.toutiao.util.DebugUtil;
import com.chinaso.toutiao.util.NetWorkStatusUtil;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.view.BaseWebView;
import com.chinaso.toutiao.view.CustomActionBar;
import com.chinaso.toutiao.view.NetWorkErrorView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;


public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.feedbackWV)
    BaseWebView mWebView;
    @BindView(R.id.feedbackActionbar)
    CustomActionBar mActionBar;
    @BindView(R.id.feedbackErrorview)
    NetWorkErrorView mErrorView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initViews() {
        initActionBar();

        initView();
    }

    private void initActionBar() {
        mActionBar.setLeftViewImg(R.mipmap.actionbar_back);
        mActionBar.setTitleView("意见反馈");
        mActionBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                finish();
            }
        });
    }

    private void initView() {
        mWebView.loadUrl(AppInitDataUtil.getFeedbackurl());
        mWebView.setWebViewClient(new CommentDetailWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                if (message != null) {
                    ToastUtil.showToast(FeedBackActivity.this, message, 0);
                }
                result.cancel();
                return true;
            }

        });
        mErrorView.setOnClickListener(new NetWorkErrorView.ReloadInterface() {
            @Override
            public void reloadWebview() {
                if (NetWorkStatusUtil.isNetworkAvailable(FeedBackActivity.this)) {
                    mWebView.reload();
                    mWebView.setVisibility(View.VISIBLE);
                    mErrorView.setVisibility(View.GONE);
                } else {
                    ToastUtil.showToast(FeedBackActivity.this, getResources().getString(R.string.no_network), 0);
                }
            }
        });
    }

    private class CommentDetailWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.isEmpty(url)) {
                return true;
            }
            Uri uri = Uri.parse(url);
            if (uri == null) {
                return true;
            }
            final String action = uri.getQueryParameter("action");
            if (action.isEmpty()) {
                return true;
            }
            if (action.equals("FeedBack")) {
                if (!NetWorkStatusUtil.isNetworkAvailable(FeedBackActivity.this)) {
                    ToastUtil.showToast(FeedBackActivity.this, "网络不好", Toast.LENGTH_SHORT);
                    return true;
                }
                final String content = uri.getQueryParameter("content");
                final String contact = uri.getQueryParameter("contact");
                String content_info = content + " 联系方式：" + contact;
                DebugUtil.i("Chinaso", content_info + ":" + action);
                if (TextUtils.isEmpty(content))
                {
                    ToastUtil.showToast(FeedBackActivity.this, "反馈内容不能为空", Toast.LENGTH_SHORT);
                } else {
                    mWebView.post(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.loadUrl("javascript:" + action + "('" + content + "')" + "('" + contact + "')");
                            ToastUtil.showToast(FeedBackActivity.this, "提交反馈成功", Toast.LENGTH_SHORT);
                        }
                    });
                    FeedBackActivity.this.finish();
                }
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mWebView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
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
