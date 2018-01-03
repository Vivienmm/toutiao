package com.chinaso.toutiao.mvp.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.view.BaseWebView;
import com.chinaso.toutiao.view.CustomActionBar;

import butterknife.BindView;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.actionbar)
    CustomActionBar mCustomBar;
    @BindView(R.id.msgWebView)
    BaseWebView msgWebView;
    private ProgressDialog mProgressDialog;
    private Toast toast = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message;
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
            msgWebView.setWebViewClient(new WebViewClient());
            msgWebView.setWebChromeClient(new WebChromeClient());
            msgWebView.loadUrl(TTApplication.getServer()+ Constants.MY_MSG);
        } else {
            toast = Toast.makeText(getApplicationContext(), "请设置网络！", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void initBar() {
        mCustomBar.setLeftViewImg(R.mipmap.actionbar_back);
        mCustomBar.setTitleView("我的通知");

        mCustomBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                MessageActivity.this.finish();
            }
        });
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
