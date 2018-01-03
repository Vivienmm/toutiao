package com.chinaso.toutiao.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.mvp.data.collection.CollectionManageDao;
import com.chinaso.toutiao.mvp.entity.QueryCommentNum;
import com.chinaso.toutiao.mvp.entity.ShareInfoEntity;
import com.chinaso.toutiao.mvp.entity.WebJSInfoEntity;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.view.VerticalView;
import com.chinaso.toutiao.net.ShortUrlFactory;
import com.chinaso.toutiao.util.DebugUtil;
import com.chinaso.toutiao.util.DisplayUtil;
import com.chinaso.toutiao.util.NetWorkStatusUtil;
import com.chinaso.toutiao.util.PermissionChecker;
import com.chinaso.toutiao.util.SolveUrl;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.util.shareutil.JsInteractionTool;
import com.chinaso.toutiao.util.shareutil.ShareContentUtil;
import com.chinaso.toutiao.util.shareutil.ShareContentUtilInterface;
import com.chinaso.toutiao.view.BaseWebView;
import com.chinaso.toutiao.view.CommentToolBar;
import com.chinaso.toutiao.view.CustomActionBar;
import com.chinaso.toutiao.view.EditTextPopupWindow;
import com.chinaso.toutiao.view.NetWorkErrorView;
import com.chinaso.toutiao.view.ShareToolBar;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMShareAPI;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class VerticalDetailActivity extends BaseActivity implements VerticalView {

    @BindView(R.id.webview)
    BaseWebView webView;
    @BindView(R.id.actionbar)
    CustomActionBar bar;
    @BindView(R.id.share_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.comment_toolbar_view)
    CommentToolBar mCommentToolbar;
    @BindView(R.id.default_errorview)
    NetWorkErrorView mErrorView;
    @BindView(R.id.view_line)
    View view_line;
    @BindView(R.id.rl_wb)
    RelativeLayout rl;
    @BindView(R.id.nightmode)
    View nightmode;
    @BindView(R.id.share_toolBar_view)
    ShareToolBar shareToolBar;

    private String urlIntent = "";
    private String mpushString = "";
    public static boolean isCollection = false; // 默认没有收藏
    private ShareInfoEntity mShareInfoEntity = new ShareInfoEntity();
    private ShareContentUtilInterface shareContentUtil;
    private WebJSInfoEntity mWebJSInfoEntity = new WebJSInfoEntity();

    //判断权限
    private static final int REQUEST_CODE = 14; // 请求码
    private PermissionChecker mPermissionsChecker; // 权限检测器
    private final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,};
    private boolean checkPermission = false;

    public ShareToolBar getShareToolBar() {
        return shareToolBar;
    }

    public CommentToolBar getShareCommentToolBar() {
        return mCommentToolbar;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vertical_detail;
    }

    @Override
    public void initViews() {

        initIntentData();

        initView();
        shareContentUtil = new ShareContentUtil(this);
        Config.OpenEditor = true;
        mPermissionsChecker = new PermissionChecker(this);
    }


    private void initIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("newsUrl")) {
                urlIntent = bundle.getString("newsUrl");
            }
            if (getIntent().hasExtra("pushUrl")) {
                urlIntent = getIntent().getStringExtra("pushUrl");
            }
            if (getIntent().hasExtra(Constants.MIPUSH)) {
                mpushString = bundle.getString(Constants.MIPUSH);
            }
        }
        Log.i("dsc", "initIntentData: +++===="+urlIntent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        bar.setLeftViewImg(R.mipmap.actionbar_back);
        bar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                if (mpushString.equals(Constants.MIPUSH)) {
                    Intent intent = new Intent();
                    intent.setClass(VerticalDetailActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                VerticalDetailActivity.this.finish();
            }
        });

        mErrorView.setOnClickListener(new NetWorkErrorView.ReloadInterface() {
            @Override
            public void reloadWebview() {
                if (NetWorkStatusUtil.isNetworkAvailable(VerticalDetailActivity.this)) {
                    bar.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.VISIBLE);
                    webView.reload();
                } else {
                    ToastUtil.showToast(VerticalDetailActivity.this, getResources().getString(R.string.register_loda_failure), 0);
                }
            }
        });
        webView.setWebViewClient(new NewsWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void post(String webAction) {

                if (TextUtils.isEmpty(webAction)) { return; }

                jsHandler(webAction);

            }

        }, "ActionBridge");
        webView.loadUrl(urlIntent);
    }

    private void jsHandler(String webAction) {
        JsInteractionTool vJsInteract = new JsInteractionTool(webView, webAction, VerticalDetailActivity.this, shareContentUtil);
        vJsInteract.setPushMessage(mpushString);
        mShareInfoEntity = vJsInteract.getShareInfoEntity();
        mWebJSInfoEntity = vJsInteract.getWebJSInfoEntity();
        if (android.webkit.URLUtil.isValidUrl(mWebJSInfoEntity.getUrl())) {
            mShareInfoEntity.setTitle(mWebJSInfoEntity.getTitle());
            try {
                new ShortUrlFactory(new ShortUrlFactory.FactoryCallBack() {
                    @Override
                    public void callBack(String url) {
                        mShareInfoEntity.setTargetUrl(url);
                    }
                }).factoryShortUrl(mWebJSInfoEntity.getUrl());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mShareInfoEntity.setContent(mWebJSInfoEntity.getContent());
            mShareInfoEntity.setPicUrl(mWebJSInfoEntity.getPic());

            final String type = SharedPreferencesSetting.getIsFontSize();
            final String fontAction = mWebJSInfoEntity.getFont();
            mWebJSInfoEntity.setFont(fontAction);
            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:articleIndex.fontSizeChange" + "('" + type + "')");
                }
            });
        }
    }

    public void showShareBottomMenu() {
        view_line.setVisibility(View.VISIBLE);
        shareToolBar.setVisibility(View.VISIBLE);
        shareToolBar.initToolBar(this, webView, mShareInfoEntity);
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) webView.getLayoutParams();
        p.bottomMargin = DisplayUtil.Dp2Px(this, 56);
        webView.setLayoutParams(p);
    }

    public void showTopWebMenu() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("function");
                subscriber.onCompleted();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {
                        initFunctionPopupWindow(s).showAtLocation(rl, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
                        setWindowBgAlpha(0.5f);
                    }
                });
    }

    public void showShareAndCommentBottomMenu() {
        view_line.setVisibility(View.VISIBLE);
        mCommentToolbar.setVisibility(View.VISIBLE);
        mCommentToolbar.initToolBar(VerticalDetailActivity.this, webView, rl, mWebJSInfoEntity.getNid(),
                mWebJSInfoEntity.getContentId(), mShareInfoEntity);
        RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams) webView.getLayoutParams();
        p.bottomMargin = DisplayUtil.Dp2Px(VerticalDetailActivity.this, 56);
        webView.setLayoutParams(p);
    }

    public void updateCommentNum(QueryCommentNum num) {
        if (num == null) {
            DebugUtil.d("VerticalDetailActivity", "数据为空");
            return;
        }
        if (num.isResult()) {
            mCommentToolbar.setCommentNum(num.getTotalNum());
        } else {
            DebugUtil.d("VerticalDetailActivity", null == num.getError() ? "出错了":num.getError());
        }
    }

    public void setNigthMode(boolean flag) {
        if (flag) {
            nightmode.setVisibility(View.VISIBLE);
        } else {
            nightmode.setVisibility(View.GONE);
        }
    }

    private EditTextPopupWindow initFunctionPopupWindow(String mode) {
        final EditTextPopupWindow mPopupWindow = new EditTextPopupWindow(VerticalDetailActivity.this, null, null, mode);
        mPopupWindow.setParams(urlIntent, mWebJSInfoEntity.getNid(), webView, mWebJSInfoEntity.getFont());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!mPopupWindow.isShowing()) {
                    setWindowBgAlpha(1.0f);
                }
            }
        });
        return mPopupWindow;
    }

    @Override
    public void showErrorMsg(String message) {

    }

    public class NewsWebViewClient extends WebViewClient {

        public NewsWebViewClient() {
            super();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (TextUtils.isEmpty(url)) return true;
            Uri uri = Uri.parse(url);
            String shceme = uri.getScheme();
            String action = uri.getQueryParameter("action");
            if (shceme.equals(SolveUrl.SCHEME) && action.equals("Back")) {
                if (mpushString.equals(Constants.MIPUSH)) {
                    startActivity(MainActivity.class, null);
                }
                VerticalDetailActivity.this.finish();
                return true;
            }

            SolveUrl solveUrl = new SolveUrl(url);
            return solveUrl.dispatchAction(VerticalDetailActivity.this);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            initCollectionImageBtn(url);

            shareToolBar.changeRefreshImgBg(true);

            mCommentToolbar.changeCollectionImgBg(isCollection);
            shareToolBar.changeCollectionImgBg(isCollection);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            shareToolBar.changeRefreshImgBg(false);
            if (NetWorkStatusUtil.isNetworkAvailable(VerticalDetailActivity.this)) {
                mErrorView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            webView.setVisibility(View.GONE);
            mErrorView.setVisibility(View.VISIBLE);
            bar.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
        }

    }

    private void initCollectionImageBtn(String urlOriginal) {
        isCollection = new CollectionManageDao().isCollected(urlOriginal);
    }

    private void setWindowBgAlpha(float f) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = f;
        params.dimAmount = f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (SharedPreferencesSetting.getContentId() != null) {
//            NetworkService.getSplashInstance().querycommentnum(mWebJSInfoEntity.getNid())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<QueryCommentNum>() {
//                        @Override
//                        public void onCompleted() {
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onNext(QueryCommentNum queryCommentNum) {
//                            updateCommentNum(queryCommentNum);
//                        }
//                    });
//            mWebJSInfoEntity.setContentId(SharedPreferencesSetting.getContentId());
//        }

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS) & !checkPermission) {
            String notice = "存储空间权限用于下载,确定关闭权限否？";
            PermissionsActivity.startActivityForResult(this, notice, REQUEST_CODE, PERMISSIONS);
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else if (mpushString.equals(Constants.MIPUSH)) {
            Intent intent = new Intent();
            intent.setClass(VerticalDetailActivity.this, MainActivity.class);
            startActivity(intent);
            VerticalDetailActivity.this.finish();
        } else {
            VerticalDetailActivity.this.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 分享相关，不准动**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        // 拒绝时,缺少主要权限,
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            checkPermission = true;
        }
        /**使用SSO授权必须添加如下代码 */
        //UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        DebugUtil.d("result", "onActivityResult");
        /*UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.loadUrl("");
        webView.clearHistory();
        if (webView != null) {
            webView.destroy();
        }
        webView = null;

        SharedPreferencesSetting.setContentId(null);
        shareContentUtil.onDestoryShare();
    }
}
