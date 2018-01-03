package com.chinaso.toutiao.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.mvp.data.collection.CollectionEntity;
import com.chinaso.toutiao.mvp.data.collection.CollectionManageDao;
import com.chinaso.toutiao.mvp.entity.ShareInfoEntity;
import com.chinaso.toutiao.mvp.ui.activity.LoginActivity;
import com.chinaso.toutiao.mvp.ui.activity.QueryCommentActivity;
import com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.util.SignCodeUtil;
import com.chinaso.toutiao.util.SoftKeyboardStateHelper;
import com.chinaso.toutiao.util.secure.MD5Util;
import com.chinaso.toutiao.util.shareutil.ShareContentUtil;
import com.chinaso.toutiao.util.shareutil.ShareContentUtilInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import static com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity.isCollection;

public class CommentToolBar extends LinearLayout implements View.OnClickListener {
    private BaseActivity mActivity;
    private RelativeLayout mRelativeLayoutShare;
    private ImageButton mImageButtonShare;
    private RelativeLayout mRelativeLayoutCollection;
    private ImageButton mImageButtonCollection;
    private NotificationButton mImageButtonComment;
    private TextView mButtonPopwindow;
    private EditTextPopupWindow mPopupWindow;
    private WebView mWebView;
    private View mRelativeView;
    //评论详情页相关
    private String mNewsId;
    private String mContentId;
    //分享相关
    private ShareContentUtilInterface shareContentUtil;
    private ShareInfoEntity mShareInfoEntity;

    private RelativeLayout mRelativeLayoutComment;

    public CommentToolBar(Context context) {
        super(context);
        initView(context);
    }

    public CommentToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommentToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_toolbar, this);
        mRelativeLayoutShare = (RelativeLayout) view.findViewById(R.id.layout_share1);
        mRelativeLayoutCollection = (RelativeLayout) view.findViewById(R.id.layout_collection1);
        mButtonPopwindow = (TextView) view.findViewById(R.id.layout_write);
        mImageButtonShare = (ImageButton) view.findViewById(R.id.imageButtonShare1);
        mImageButtonCollection = (ImageButton) view.findViewById(R.id.imageButtonCollection1);
        mImageButtonComment = (NotificationButton) view.findViewById(R.id.imageButtonComment);
        mRelativeLayoutComment = (RelativeLayout) view.findViewById(R.id.layout_comment);

        mRelativeLayoutShare.setOnClickListener(this);
        mRelativeLayoutCollection.setOnClickListener(this);
        mButtonPopwindow.setOnClickListener(this);
        mImageButtonShare.setOnClickListener(this);
        mImageButtonCollection.setOnClickListener(this);
        mImageButtonComment.setOnClickListener(this);
    }

    public void initToolBar(Activity mActivity, WebView webView, RelativeLayout rl,
                            String mNewsId, String mContentId, ShareInfoEntity mShareInfoEntity) {
        this.mActivity = (BaseActivity) mActivity;
        mWebView = webView;
        mRelativeView = rl;
        this.mNewsId = mNewsId;
        this.mContentId = mContentId;
        this.mShareInfoEntity = mShareInfoEntity;
        this.shareContentUtil = new ShareContentUtil(mActivity);
        if (mActivity instanceof VerticalDetailActivity) {
            mRelativeLayoutShare.setVisibility(VISIBLE);
            mRelativeLayoutCollection.setVisibility(VISIBLE);
            mRelativeLayoutComment.setVisibility(VISIBLE);
        }
    }

    /**
     * 评论弹出框
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (mNewsId == null) {
            mImageButtonComment.setClickable(false);
            mButtonPopwindow.setClickable(false);
        }
        switch (v.getId()) {
            case R.id.layout_write:
                inputComment();
                break;
            case R.id.layout_comment:
            case R.id.imageButtonComment:
                commentActivity();
                break;
            case R.id.layout_share1:
            case R.id.imageButtonShare1:
                shareContentUtil.startShare(mShareInfoEntity, 1);
                break;
            case R.id.layout_collection1:
            case R.id.imageButtonCollection1:
                collection();
                break;
            default:
                break;
        }
    }

    private void commentActivity() {
        Bundle bundle = new Bundle();
        String newsUrl = null;
        String url;
        try {
            newsUrl = URLDecoder.decode(mNewsId, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (TTApplication.isLogin) {
            String uid = String.valueOf(SharedPreferencePrefUserInfo.getUserName());
            Map<String, String> map = new TreeMap<>();
            map.put("uid", "uid" + uid);
            map.put("contentId", "contentId" + mContentId);
            map.put("nid", "nid" + newsUrl);
            String sign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
            url = Constants.NEWSDETAIL_COMMENT_URL + "nid=" + getUrlEncode(newsUrl) +
                    "&contentId=" + mContentId + "&uid=" + uid + "&sign=" + sign;
        } else {
            url = Constants.NEWSDETAIL_COMMENT_URL + "nid=" + getUrlEncode(newsUrl) + "&contentId=" + mContentId;
        }
        bundle.putString("commentUrl", url);
        mActivity.startActivity(QueryCommentActivity.class, bundle);
    }

    private void collection() {
        Toast toast = Toast.makeText(mActivity, "",
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

    private void inputComment() {
        if (TextUtils.isEmpty(mNewsId)) {
            Log.i("comment", "inputComment: " + mNewsId);
            return;
        }
        initCommentPopupWindow();
        if (TTApplication.isLogin) {
            mPopupWindow.showAtLocation(mRelativeView, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
            setWindowBgAlpha(0.5f);
            return;
        }
        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
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

    /**
     * 初始化评论框
     */
    private void initCommentPopupWindow() {
        mPopupWindow = new EditTextPopupWindow(mActivity, mNewsId, mContentId, "comment");
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!mPopupWindow.isShowing()) {
                    setWindowBgAlpha(1.0f);
                }
            }
        });

        final SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(mRelativeView);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
            }

            @Override
            public void onSoftKeyboardClosed() {
                mPopupWindow.dismiss();
                setWindowBgAlpha(1.0f);
            }
        });
    }

    public void setCommentNum(int num) {
        mImageButtonComment.setNotificationNumber(num);
    }

    /**
     * 设置透明度
     *
     * @param f
     */
    public void setWindowBgAlpha(float f) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = f;
        params.dimAmount = f;
        mActivity.getWindow().setAttributes(params);
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void changeCollectionImgBg(boolean arg) {
        if (arg) {
            mImageButtonCollection.setImageDrawable(getResources()
                    .getDrawable(R.mipmap.webview_collection_pressed));
        } else {
            mImageButtonCollection.setImageDrawable(getResources()
                    .getDrawable(R.mipmap.webview_collection_normal));
        }
    }
}