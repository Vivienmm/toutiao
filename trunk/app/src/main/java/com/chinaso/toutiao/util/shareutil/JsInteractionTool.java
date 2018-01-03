package com.chinaso.toutiao.util.shareutil;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.mvp.entity.QueryCommentNum;
import com.chinaso.toutiao.mvp.entity.ShareInfoEntity;
import com.chinaso.toutiao.mvp.entity.WebJSInfoEntity;
import com.chinaso.toutiao.mvp.ui.activity.MainActivity;
import com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity;
import com.chinaso.toutiao.mvp.ui.activity.VideoActivity;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.net.NetworkService;
import com.chinaso.toutiao.net.ShortUrlFactory;
import com.chinaso.toutiao.util.DebugUtil;
import com.chinaso.toutiao.util.NetWorkStatusUtil;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class JsInteractionTool {
    private BaseActivity mActivity;
    private String mPush = "";
    private UMImage shareImage;
    private String platform;
    private ShareContentUtilInterface mShareContentUtilSearch;
    private ShareInfoEntity shareInfoEntity;

    private WebJSInfoEntity mWebJSInfoEntity;

    public JsInteractionTool(WebView webView, String json, Context context) {
        this(webView, json, context, null);
    }

    public JsInteractionTool(WebView webView, String json, Context context, ShareContentUtilInterface shareContentUtilSearch) {
        mShareContentUtilSearch = shareContentUtilSearch;
        JsAggregate(webView, json, context);
    }

    public void setPushMessage(String mpush) {
        this.mPush = mpush;
    }

    /**
     * 该类是核心，仅添加action，其他参数请添加构造函数
     *
     * @param json
     * @param mContext
     */
    private void JsAggregate(WebView mWebView, String json, Context mContext) {
        mActivity = (BaseActivity) mContext;
        shareInfoEntity = new ShareInfoEntity();
        mWebJSInfoEntity = new WebJSInfoEntity();
        try {
            JSONObject jsObj = new JSONObject(json);
            String action = jsObj.getString("action");//action作为区分
            switch (action) {
                case "Back":
                    backAction(mWebView);
                    break;
                case "GetVideo"://视频播放，参数:url
                    getVideoAction(jsObj);
                    break;
                case "Share"://网页内分享按钮,调取分享页面，参数：String title,url,content,pic
                    getShareContent(jsObj);
                    break;
                case "ShowShareToolbar"://评论栏原生分享按钮，传递数据
                    showShareToolbarAction(jsObj);
                    break;
                case "ShowCommentToolbar"://评论栏原生评论按钮，传递数据
                    showCommentToolbarAction(jsObj);
                    break;
                case "PlayVideo"://控制视频播放，参数：url
                    playVideoAction(mWebView, jsObj);
                    break;
                case "ShowNews":
                    showNewsAction(jsObj);
                    break;
                case "ShowCommentListToolbar":
                    showCommentListToolbarAction(jsObj);
                    break;
                case "NewsSetting":
                    newsSettingAction(mWebView, jsObj);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新开activity播放视频
     */
    private void getVideoAction(JSONObject jsObj) throws JSONException {
        String url = jsObj.getString("url");
        Intent i = new Intent();
        i.setClass(mActivity, VideoActivity.class);
        i.putExtra("videoUrl", url);
        mActivity.startActivity(i);
    }

    private void newsSettingAction(final WebView mWebView, JSONObject jsObj) {
        Log.i("ly","enter newsSettingAction");
        if (mActivity instanceof VerticalDetailActivity) {
            Log.i("ly","enter 1");
            ((VerticalDetailActivity) mActivity).showTopWebMenu();
        }
        if (SharedPreferencesSetting.getIsFontSize() != null) {
            Log.i("ly","enter 2");
            mWebJSInfoEntity = new Gson().fromJson(jsObj.toString(), WebJSInfoEntity.class);
            final String type = SharedPreferencesSetting.getIsFontSize();
            final String fontAction = mWebJSInfoEntity.getFont();
            mWebJSInfoEntity.setFont(fontAction);
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:" + fontAction + "('" + type + "')");
                }
            });
        }
    }

    private void showCommentListToolbarAction(JSONObject jsObj) {
        mWebJSInfoEntity = new Gson().fromJson(jsObj.toString(), WebJSInfoEntity.class);
        mWebJSInfoEntity.setNid(mWebJSInfoEntity.getNid());
        mWebJSInfoEntity.setContentId(mWebJSInfoEntity.getContentId());
        try {
            mWebJSInfoEntity.setNid(URLDecoder.decode(mWebJSInfoEntity.getNid(), "utf-8"));
//            if (mActivity instanceof QueryCommentActivity) {
//                ((QueryCommentActivity) mActivity).showCommentListToolBar();
//            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void showNewsAction(JSONObject jsObj) throws JSONException {
        Bundle aBundleShowNews = new Bundle();
        aBundleShowNews.putString("newsShowUrl", jsObj.getString("url"));
//      aBundleShowNews.putString("newsShowUrl","http://zhangxi.me/bridge/bridge.html");//测试所用
//      aBundleShowNews.putString("newsShowUrl","http://m.chinaso365.com/test/newslist.html");
        mActivity.startActivityForResult(VerticalDetailActivity.class, aBundleShowNews, 5);
    }

    private void playVideoAction(final WebView mWebView, JSONObject jsObj) throws JSONException {
        final String function = jsObj.getString("function");
        if (NetWorkStatusUtil.isNetworkAvailable(mActivity)) {
            if (NetWorkStatusUtil.isWifi(mActivity)) {
                mWebView.post(new Runnable() {

                    @Override
                    public void run() {
                        mWebView.loadUrl("javascript: " + function);
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("您当前正在移动网络下观看，是否继续？");
                builder.setTitle("提示");
                builder.setPositiveButton("继续播放", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mWebView.post(new Runnable() {

                            @Override
                            public void run() {
                                mWebView.loadUrl("javascript: " + function);
                            }
                        });
                    }
                });

                builder.setNegativeButton("停止播放", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mActivity);
            builder1.setMessage("您没有接入网络，暂时无法播放");
            builder1.setTitle("提示");
            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    boolean post = mWebView.post(new Runnable() {

                        @Override
                        public void run() {
                            mWebView.loadUrl("javascript: " + function);
                        }
                    });
                }
            });
            builder1.create().show();
        }
    }

    private void showCommentToolbarAction(final JSONObject jsObj) throws JSONException {
        mWebJSInfoEntity = new Gson().fromJson(jsObj.toString(), WebJSInfoEntity.class);
        SharedPreferencesSetting.setContentId(jsObj.getString("contentId"));
        try {
            String newNid = mWebJSInfoEntity.getNid();
            mWebJSInfoEntity.setNid(URLDecoder.decode(newNid, "utf-8"));

            NetworkService.getSplashInstance().querycommentnum(newNid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QueryCommentNum>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(QueryCommentNum result) {
                        ((VerticalDetailActivity) mActivity).showShareAndCommentBottomMenu();
                        ((VerticalDetailActivity) mActivity).updateCommentNum(result);
                    }
                });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void showShareToolbarAction(final JSONObject jsObj) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                getShareContent(jsObj);
                subscriber.onNext(" ");
                subscriber.onCompleted();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        ((VerticalDetailActivity) mActivity).showShareBottomMenu();
                    }
                });
    }
//  //保留
//    private void getVideoAction(JSONObject jsObj) throws JSONException {
//        String url = jsObj.getString("url");
//        Intent it = new Intent(Intent.ACTION_VIEW);
//        it.setDataAndType(Uri.parse(url), "video/mp4");
//        mActivity.startActivity(it);
//    }
//
//    private void refreshAction(final WebView mWebView) {
//        if (mWebViewCardItem != null) {
//            mhandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    mWebViewCardItem.updateViewContent();
//                }
//            });
//        } else {
//            mWebView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mWebView.reload();
//                }
//            });
//        }
//    }

    private void backAction(final WebView mWebView) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                if (mPush.equals(Constants.MIPUSH)) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, MainActivity.class);
                    mActivity.startActivity(intent);
                } else if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    mActivity.finish();
                }
            }
        });
    }

    private void getShareContent(JSONObject jsObj) {
        try {
            platform = jsObj.has("platform") ? jsObj.getString("platform") : null;
            shareImage = new UMImage(mActivity, jsObj.getString("pic"));

            shareInfoEntity.setDefaultImg(BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher));
            shareInfoEntity.setPicUrl(jsObj.getString("pic"));
            shareInfoEntity.setContent(jsObj.getString("content").length() == 0 ? jsObj.getString("title") : jsObj.getString("content"));
            shareInfoEntity.setTitle(jsObj.getString("title"));

            new ShortUrlFactory(new ShortUrlFactory.FactoryCallBack() {
                @Override
                public void callBack(String url) {
                    shareInfoEntity.setTargetUrl(url);
                    dispatchShare();
                }
            }).factoryShortUrl(jsObj.getString("url"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatchShare() {
        if (platform == null) { //native分享按钮
            return;
        }
        if (platform.length() == 0) {//网页内分享按钮，全平台
            DebugUtil.i("share", "JsInteraction+default platform");
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mShareContentUtilSearch.startShare(shareInfoEntity, 1);
                }
            });
        }
        if (platform.length() > 0) {
            final ShareContent platformContent = new ShareContent();
            platformContent.mText = shareInfoEntity.getContent();
            platformContent.mTitle = shareInfoEntity.getTitle();
            platformContent.mTargetUrl = shareInfoEntity.getTargetUrl();
            platformContent.mMedia = shareImage;
            switch (platform) {
                case "WechatSession"://分享给微信好友
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                                    .setShareContent(platformContent)
                                    .share();
                        }
                    });

                    break;
                case "WechatTimeline"://分享到朋友圈
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                                    .setShareContent(platformContent)
                                    .share();
                        }
                    });

                    break;
                case "QQ"://分享给QQ好友
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ).setCallback(umShareListener)
                                    .setShareContent(platformContent)
                                    .share();
                        }
                    });

                    break;
                default:
                    break;

            }

        }

    }

    public ShareInfoEntity getShareInfoEntity() {
        return shareInfoEntity;
    }

    public WebJSInfoEntity getWebJSInfoEntity() {
        return mWebJSInfoEntity;
    }

    /**
     * 分享回调
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            DebugUtil.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity, " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mActivity, " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity, " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
