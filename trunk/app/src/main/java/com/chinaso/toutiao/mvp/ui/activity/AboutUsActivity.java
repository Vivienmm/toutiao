package com.chinaso.toutiao.mvp.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.util.AppInitDataUtil;
import com.chinaso.toutiao.view.BaseWebView;
import com.chinaso.toutiao.view.CustomActionBar;

import butterknife.BindView;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    CustomActionBar mCustomBar;
    @BindView(R.id.aboutUsWebView)
    BaseWebView aboutUsWebView;

    private ProgressDialog mProgressDialog;
    private Toast toast = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initViews() {
        initBar();
        initWebView();
    }

    private void initWebView() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            String aboutUrl = AppInitDataUtil.getAboutUrl();
            aboutUsWebView.loadUrl(aboutUrl);
            aboutUsWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("tel:")) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(intent);
                        return true;
                    } else if (url.contains("mailto:")) {
                        Intent data = new Intent(Intent.ACTION_SENDTO);
                        data.setData(Uri.parse(url));
                        data.putExtra(Intent.EXTRA_SUBJECT, "业务联系");
                        startActivity(data);
                    } else if (url.contains("action=Back")) {
                        AboutUsActivity.this.finish();
                    }
                    return true;
                }

                //网页开始加载
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    if (mProgressDialog == null) {
                        mProgressDialog = ProgressDialog.show(AboutUsActivity.this, null, "正在加载...");
                    }

                }

                @Override
                public void onPageFinished(WebView view, String url) {//网页加载完成
                    super.onPageFinished(view, url);
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode,
                                            String description, String failingUrl) {
                    // TODO Auto-generated method stub
                    toast = Toast.makeText(getApplicationContext(), "加载失败，请重试！", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        } else {
            toast = Toast.makeText(getApplicationContext(), "请设置网络！", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private void initBar() {
        mCustomBar.setLeftViewImg(R.mipmap.actionbar_back);

        mCustomBar.setTitleView("关于我们");

        mCustomBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                AboutUsActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        doBack();
    }

    private void doBack() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }
}
