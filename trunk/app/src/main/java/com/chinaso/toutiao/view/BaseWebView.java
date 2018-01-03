package com.chinaso.toutiao.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.chinaso.toutiao.mvp.ui.activity.VideoActivity;
import com.chinaso.toutiao.net.ShortUrlFactory;
import com.chinaso.toutiao.util.MyWebSetting;
import com.chinaso.toutiao.util.NetWorkStatusUtil;
import com.chinaso.toutiao.util.shareutil.JsInteractionTool;

import java.io.UnsupportedEncodingException;

public class BaseWebView extends WebView {
    private Context mContext;
    private WebView mWebView;

    {
        if (!isInEditMode()) {
            mContext = this.getContext();
            mWebView = this;
            initSetting();
            secureSetting();
            initJS();

//            setDownloadListener(new MyWebViewDownLoadListener());
//            setOnLongClickListener(new MyOnLongClickListener());
        }
    }

    public BaseWebView(Context context) {
        super(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initSetting() {
        WebSettings settings = getSettings();
        MyWebSetting unifySetting = new MyWebSetting(mContext, settings);
        unifySetting.setSaveMode();
        unifySetting.setZoomMode();
        unifySetting.setUseWideViewPort(true);
        unifySetting.setDatabasePath();//开启Application H5 Caches 功能
        settings.setDefaultTextEncodingName("GBK");// 设置字符编码
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setSaveFormData(true);//使用缓存
        settings.setJavaScriptEnabled(true);
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//该句是因为出现了dequeueBuffer failed 崩溃的情况
    }

    private void secureSetting() {
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
    }

    private void initJS() {
        addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void post(String webAction) {
                if (null != webAction) {
                    new JsInteractionTool(mWebView, webAction, mContext);
                }
            }
        }, "ActionBridge");

        addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void play(String videourl) {
                Log.i("lxj", "BaseWebVideo");
                startVideoActivity(videourl);
            }
        }, "GetVideo");




        /**
         * GetVideo和PlayVideo是两种视频播放模式，二选一。
         * 个别手机视频性不好，暂时屏蔽网页内视频播放
         */
//        addJavascriptInterface(new Object() {
//            @JavascriptInterface
//            public void initPlayVideo(String playVideo) {
//                Log.i("dsc", "initPlayVideo: "+playVideo);
//                playVideo(playVideo);
//            }
//        }, "PlayVideo");
    }

    /**
     * 新开activity播放视频
     * @param url
     */
    private void startVideoActivity(String url) {
        Intent i = new Intent();
        i.setClass(mContext, VideoActivity.class);
        i.putExtra("videoUrl", url);
        mContext.startActivity(i);
    }

    @Deprecated
    public void initDeprecatedJS(final Handler mHandler) {
        addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void initToolbar(final String title, final String url, final String content, final String pic) {
                //title:标题; url:新闻地址; content:摘要; pic:需要分享的图片地址
                try {
                    ShortUrlFactory factory = new ShortUrlFactory(new ShortUrlFactory.FactoryCallBack() {
                        @Override
                        public void callBack(String url) {
//                            mShareInfoEntity.setDefaultImg(BitmapFactory.decodeResource(getResources(), R.mipmap.icon));
//                            mShareInfoEntity.setPicUrl(pic);
//                            mShareInfoEntity.setTargetUrl(url);
//                            mShareInfoEntity.setContent(content.length() == 0 ? title : content);
//                            mShareInfoEntity.setTitle(title);
//
//                            mHandler.sendEmptyMessage(SHOW_BOTTOMBAR);
                        }
                    });
                    factory.factoryShortUrl(url);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, "ShowShareToolbar");

        addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void initToolbar(final String title, final String url, final String content, final String pic, final int count,
                                    final String commentUrl, final String nid, final String contentId, final String fchkLcom) {
//                mWebJSInfoEntity.setmContentId(contentId);
//                mImageButtonComment.setNotificationNumber(count);
//                SharedPreferencesSetting.setContentId(contentId);
//                try {
//                    mWebJSInfoEntity.setmNewsId(URLDecoder.decode(nid, "utf-8"));
//                    ShortUrlFactory factory = new ShortUrlFactory(new ShortUrlFactory.FactoryCallBack() {
//                        @Override
//                        public void callBack(String url) {
//
//                            mShareInfoEntity.setDefaultImg(BitmapFactory.decodeResource(getResources(), R.mipmap.icon));
//                            mShareInfoEntity.setPicUrl(pic);
//                            mShareInfoEntity.setTargetUrl(url);
//                            mShareInfoEntity.setContent(content.length() == 0 ? title : content);
//                            mShareInfoEntity.setTitle(title);
//
//                            mHandler.sendEmptyMessage(SHOW_COMMENTBAR);
//                            mHandler.sendEmptyMessage(SHOW_COMMENT_REFRESH);
//                        }
//                    });
//                    factory.factoryShortUrl(URLDecoder.decode(url, "utf-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
            }

        }, "ShowCommentToolbar");

//        addJavascriptInterface(new Object() {  //新闻详情
//            @JavascriptInterface
//            public void initToolbar(String title, String url, String content, String pic) {
////                jsShare(title,url,content,pic);
//            }
//        }, "ShowShareToolbar");

        addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void initToolbar(final String title, final String url, final String content, final String pic, final int count,
                                    final String commentUrl, final String nid, final String contentId, final String fchkLcom) {
//                mWebJSInfoEntity.setContentId(contentId);
//                try {
//                    mWebJSInfoEntity.setNid(URLDecoder.decode(nid, "utf-8"));
//                    ShortUrlFactory factory = new ShortUrlFactory(new ShortUrlFactory.FactoryCallBack() {
//                        @Override
//                        public void callBack(String url) {
//                            mShareInfoEntity.setDefaultImg(BitmapFactory.decodeResource(getResources(), R.mipmap.icon));
//                            mShareInfoEntity.setPicUrl(pic);
//                            mShareInfoEntity.setTargetUrl(url);
//                            mShareInfoEntity.setContent(content.length() == 0 ? title : content);
//                            mShareInfoEntity.setTitle(title);
//
//                            mCommentToolbar.setCommentNum(count);
//
//                            NetworkService.getInstance().querycommentnumRx(mWebJSInfoEntity.getNid())
//                                    .subscribeOn(Schedulers.newThread())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(new Observer<QueryCommentNum>() {
//                                        @Override
//                                        public void onCompleted() {
//                                        }
//
//                                        @Override
//                                        public void onError(Throwable e) {
//                                        }
//
//                                        @Override
//                                        public void onNext(QueryCommentNum queryCommentNum) {
//                                            showShareAndCommentBottomMenu(queryCommentNum);
//                                        }
//                                    });
//                        }
//                    });
//                    factory.factoryShortUrl(URLDecoder.decode(url, "utf-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
            }

        }, "ShowCommentToolbar");
    }

    private void playVideo(final String videoUrl) {
        if (NetWorkStatusUtil.isNetworkAvailable(mContext)) {
            if (NetWorkStatusUtil.isWifi(mContext)) {
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(Uri.parse(videoUrl), "video/mp4");
                mContext.startActivity(it);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("您当前正在移动网络下观看，是否继续？");
                builder.setTitle("提示");
                builder.setPositiveButton("继续播放", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent it = new Intent(Intent.ACTION_VIEW);
                        it.setDataAndType(Uri.parse(videoUrl), "video/mp4");
                        mContext.startActivity(it);
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
            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
            builder1.setMessage("您没有接入网络，暂时无法播放");
            builder1.setTitle("提示");
            builder1.setPositiveButton("确定", null);
            builder1.create().show();
        }
    }

//
//    /**
//     * 网页下载
//     */
//    private class MyWebViewDownLoadListener implements DownloadListener {
//
//        @Override
//        public void onDownloadStart(String url, String userAgent,
//                                    String contentDisposition, String mimetype, long contentLength) {
//            Utils.downloadFromWebView(mContext, url);
//        }
//    }

//    /**
//     * 长按下载图片
//     */
//    private class MyOnLongClickListener implements View.OnLongClickListener {
//        @Override
//        public boolean onLongClick(View v) {
//            if (v instanceof WebView) {
//                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
//                if (result != null) {
//                    int type = result.getType();
//                    if (type == WebView.HitTestResult.IMAGE_TYPE
//                            || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
//                        Utils.downloadImage(mContext, result.getExtra());
//                    }
//                }
//            }
//            return false;
//        }
//    }

    /*
     * 解决滑动冲突
     * @param direction 滑动距离
     * @return
     */
    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }
}
