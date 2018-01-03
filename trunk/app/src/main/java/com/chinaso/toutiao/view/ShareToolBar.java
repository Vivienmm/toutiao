package com.chinaso.toutiao.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.data.collection.CollectionEntity;
import com.chinaso.toutiao.mvp.data.collection.CollectionManageDao;
import com.chinaso.toutiao.mvp.entity.ShareInfoEntity;
import com.chinaso.toutiao.mvp.listener.HomeButtonEvent;
import com.chinaso.toutiao.mvp.ui.activity.MainActivity;
import com.chinaso.toutiao.util.shareutil.ShareContentUtil;
import com.chinaso.toutiao.util.shareutil.ShareContentUtilInterface;

import de.greenrobot.event.EventBus;

import static com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity.isCollection;

public class ShareToolBar extends LinearLayout implements View.OnClickListener {
    private TextView tvHome;
    private TextView tvFresh;
    private TextView tvShare;
    private TextView tvCollection;
    private Context mContext;
    private WebView mWebView;
    //分享相关
    private ShareContentUtilInterface shareContentUtil;
    private ShareInfoEntity mShareInfoEntity;

    {
        mContext = getContext();
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.share_toolbar, this);
        tvHome = (TextView) view.findViewById(R.id.tvButtonHome);
        tvShare = (TextView) view.findViewById(R.id.tvButtonShare);
        tvFresh = (TextView) view.findViewById(R.id.tvButtonFresh);
        tvCollection = (TextView) view.findViewById(R.id.tvButtonCollection);
        tvHome.setOnClickListener(this);
        tvShare.setOnClickListener(this);
        tvFresh.setOnClickListener(this);
        tvCollection.setOnClickListener(this);
    }

    public ShareToolBar(Context context) {
        super(context);
    }

    public ShareToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initToolBar(Activity activity, WebView webView, ShareInfoEntity shareInfoEntity) {
        mWebView = webView;
        shareContentUtil = new ShareContentUtil(activity);
        mShareInfoEntity = shareInfoEntity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvButtonHome:
                Intent intent2 = new Intent();
                intent2.setClass(mContext, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(intent2);
                EventBus.getDefault().post(new HomeButtonEvent(0));
                break;
            case R.id.tvButtonShare:
                shareContentUtil.startShare(mShareInfoEntity, 1);
                break;
            case R.id.tvButtonFresh:
                mWebView.reload();
                break;
            case R.id.tvButtonCollection:
                collection();
                break;
            default:
                break;
        }
    }

    private void collection() {
        Toast toast = Toast.makeText(mContext, "",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        if (isCollection) {
            new CollectionManageDao().deleteCollectionByUrl(mWebView.getUrl());
            toast.setText("已取消收藏");
        } else {
            CollectionEntity entity = new CollectionEntity();
            entity.setTitle(mWebView.getTitle());
            entity.setUrl(mWebView.getUrl());
            entity.setType(2);
            new CollectionManageDao().insertItem(entity);
            toast.setText("已收藏");
            mWebView.getUrl();
        }
        isCollection = !isCollection;
        changeCollectionImgBg(isCollection);
        toast.show();
    }

    public void changeCollectionImgBg(boolean arg) {
        int res = arg ? R.mipmap.webview_collection_pressed : R.mipmap.webview_collection_normal;
        tvCollection.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(res), null, null);
    }

    public void changeRefreshImgBg(boolean arg) {
        int res = arg ? R.mipmap.webview_refresh_stop : R.mipmap.webview_refresh_pressed;
        tvFresh.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(res), null, null);
    }
}
