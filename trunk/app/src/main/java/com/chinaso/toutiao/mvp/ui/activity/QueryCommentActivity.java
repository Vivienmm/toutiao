package com.chinaso.toutiao.mvp.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.util.DisplayUtil;
import com.chinaso.toutiao.util.NetWorkStatusUtil;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.view.BaseWebView;
import com.chinaso.toutiao.view.CommentToolBar;
import com.chinaso.toutiao.view.CustomActionBar;
import com.chinaso.toutiao.view.NetWorkErrorView;
import com.umeng.socialize.UMShareAPI;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class QueryCommentActivity extends BaseActivity {
    @BindView(R.id.activity_query_comment)
    RelativeLayout rootLayout;
    @BindView(R.id.actionbar)
    CustomActionBar mActionBar;
    @BindView(R.id.webviewComment)
    BaseWebView mWebView;
    @BindView(R.id.default_errorview)
    NetWorkErrorView mErrorView;
    @BindView(R.id.view_line)
    View view_line;
    @BindView(R.id.comment_layout)
    LinearLayout mLinearComment;
    @BindView(R.id.comment_toolbar_view)
    CommentToolBar mCommentToolbar;

    private String newsId;
    private String mContentId;
    private String mCommentUrl;

    @Override
    public int getLayoutId() {
        return R.layout.activity_query_comment;
    }

    @Override
    public void initViews() {
        Intent intent = getIntent();
        if (intent.hasExtra("commentUrl")) {
            mCommentUrl = intent.getStringExtra("commentUrl");
        }

        mActionBar.setLeftViewImg(R.mipmap.actionbar_back);
        mActionBar.setTitleView("国搜新闻");
        mActionBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                finish();
            }
        });
        mErrorView.setOnClickListener(new NetWorkErrorView.ReloadInterface() {
            @Override
            public void reloadWebview() {
                if (NetWorkStatusUtil.isNetworkAvailable(QueryCommentActivity.this)) {
                    mErrorView.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                    mWebView.reload();
                } else {
                    ToastUtil.showToast(QueryCommentActivity.this, getResources().getString(R.string.register_loda_failure), 0);
                }
            }
        });
    }

    private void initWebView() {
        mWebView.setWebViewClient(new CommentWebViewClient());
        mWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void initToolbar(String nid, String contentId) {
                showCommentListToolBar();
                try {
                    newsId = URLDecoder.decode(nid, "UTF-8");
                    mContentId = contentId;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        },"ShowCommentListToolbar");

        mWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void needLogin() {
                if (!TTApplication.isLogin) {
                    shouldLogin();
                }
            }
        }, "NeedLogin");

//        if (SharedPreferencesSetting.getIsLogin()) {
//            String uid = String.valueOf(SharedPreferencesSetting.getUserId());
//            String newsUrl = SharedPreferencesSetting.getIsNewsUrl();
//            mContentId = SharedPreferencesSetting.getContentId();
//            Map<String, String> map = new TreeMap<>();
//            map.put("uid", "uid" + uid);
//            map.put("contentId", "contentId" + mContentId);
//            map.put("nid", "nid" + newsUrl);
//            String sign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
//            String newsUrlEncode = getUrlEncode(newsUrl);
//            String url = ConstantURL.QUERY_COMMENT_URL + "nid=" + newsUrlEncode + "&contentId="
//                    + mContentId + "&uid=" + uid + "&sign=" + sign;
//            mWebView.loadUrl(url);
//        } else {
//            mWebView.loadUrl(mCommentUrl);
//        }
        mWebView.loadUrl(mCommentUrl);
    }

    public void showCommentListToolBar() {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("");
                        subscriber.onCompleted();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>(){
                    @Override
                    public void call(String  o) {
                        view_line.setVisibility(View.VISIBLE);
                        mCommentToolbar.setVisibility(View.VISIBLE);
                        mLinearComment.setVisibility(View.VISIBLE);
                        mCommentToolbar.initToolBar(QueryCommentActivity.this, mWebView, rootLayout, newsId, mContentId, null);
                        controlBottomBarDisplayFeatures();
                    }
                });
    }

    protected void controlBottomBarDisplayFeatures() {
        int h = DisplayUtil.Dp2Px(this, 56);
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) mWebView.getLayoutParams();
        p.bottomMargin = h;
        mWebView.setLayoutParams(p);
    }

    public void reloadWeb() {
        mWebView.reload();
    }

    public void shouldLogin() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }

    private String getUrlEncode(String newsUrl) {
        String newsUrlEncode = null;
        try {
            newsUrlEncode = URLEncoder.encode(newsUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newsUrlEncode;
    }

    public class CommentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (NetWorkStatusUtil.isNetworkAvailable(QueryCommentActivity.this)) {
                mWebView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
            }
        }

    }

    public void setParams(float f) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = f;
        params.dimAmount = f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        /*UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }*/
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.loadUrl("");
        mWebView.clearHistory();
        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = null;
    }


    @Override
    protected void onResume() {
        initWebView();
        super.onResume();
    }

    /**
     * 点击返回键视频停止播放
     */
    @Override
    protected void onPause() {
        mWebView.reload();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            QueryCommentActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
