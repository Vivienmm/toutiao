package com.chinaso.toutiao.util.shareutil;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.entity.ShareInfoEntity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;


/**
 */
public class ShareContentUtil extends ShareContentUtilAbstract {
    // private UMSocialService mycontroller;
    private Activity mycontext;
    private UMImage myshareImage;//分享图片
    private String myTitle;//分享标题
    private String mycontent;//分享内容
    private String myshareurl;//分享链接


    //对应每个平台的分享内容格式
    private ShareContent weixinContent, qzoneShareContent, qqShareContent, commonShareContent, tencent, sms, mail;


    public ShareContentUtil(Activity context) {
        mycontext = context;
    }


    @Override
    protected void initShareContent(ShareInfoEntity shareEntity) {
        myTitle = TextUtils.isEmpty(shareEntity.getTitle()) ? "中国搜索" : shareEntity.getTitle();
        mycontent = TextUtils.isEmpty(shareEntity.getContent()) ? shareEntity.getTargetUrl() : shareEntity.getContent();
        myshareImage = TextUtils.isEmpty(shareEntity.getPicUrl()) ? (new UMImage(mycontext, shareEntity.getDefaultImg())) : (new UMImage(mycontext, shareEntity.getPicUrl()));
        myshareurl = shareEntity.getTargetUrl();
    }

    /**
     * 设置分享内容
     */
    @Override
    protected void initSharePlatform() {

        ShareContent defaultContent = new ShareContent();
        defaultContent.mText = "分享国搜新闻 【" + myTitle + "】" + myshareurl + " (分享自#中国搜索客户端#) ";

        //微信好友、朋友圈分享
        weixinContent = new ShareContent();
        weixinContent.mText = mycontent;
        weixinContent.mTitle = myTitle;
        weixinContent.mTargetUrl = myshareurl;
        weixinContent.mMedia = myshareImage;

        // 设置QQ空间分享内容

        qzoneShareContent = new ShareContent();
        qzoneShareContent.mText = mycontent;
        qzoneShareContent.mTargetUrl = myshareurl;
        qzoneShareContent.mTitle = myTitle;
        qzoneShareContent.mMedia = myshareImage;

        //QQ好友分享
        qqShareContent = new ShareContent();
        qqShareContent.mText = mycontent;
        qqShareContent.mTitle = myTitle;
        qqShareContent.mTargetUrl = myshareurl;
        qqShareContent.mMedia = myshareImage;


        // 设置邮件分享内容， 如果需要分享图片则只支持本地图片
        UMImage image = new UMImage(mycontext, R.mipmap.ic_launcher);
        mail = new ShareContent();
        String mailshare = "&nbsp;&nbsp;&nbsp;&nbsp;我在中国搜索浏览新闻，想给你分享这篇文章：\r\n" + "<br>&nbsp;&nbsp;&nbsp;&nbsp;" + "【" + myTitle + "】" + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;具体内容请戳: " + myshareurl;
        mail.mTitle = "中国搜索新闻分享";
        mail.mText = mailshare;
        mail.mMedia = image;

        // 设置短信分享内容
        sms = new ShareContent();
        sms.mText = "分享国搜新闻 【" + myTitle + "】" + "\r\n" + myshareurl + " (分享自#国搜头条客户端#)";


        //新浪微博分享
        commonShareContent = new ShareContent();
        commonShareContent.mText = "分享国搜新闻 【" + myTitle + "】" + myshareurl + " (分享自#国搜头条客户端#) ";
        commonShareContent.mMedia = myshareImage;
        commonShareContent.mTitle = myTitle;

    }

    @Override
    protected void beginShare(int i) {
        switch (i) {
            case 1:
                /**分享面板增加自定义按钮,以及不同分享平台不同分享内容，不同回调监听**/
                new ShareAction(mycontext).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.DOUBAN,SHARE_MEDIA.EMAIL, SHARE_MEDIA.SMS //SHARE_MEDIA.TENCENT,SHARE_MEDIA.RENREN,,
                        )

                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if ( share_media == SHARE_MEDIA.RENREN) {
                                    new ShareAction(mycontext).setPlatform(share_media).setCallback(umShareListener)

                                            .setShareContent(commonShareContent)
                                            .share();
                                }
                                if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE || share_media == SHARE_MEDIA.WEIXIN_FAVORITE) {
                                    new ShareAction(mycontext).setPlatform(share_media).setCallback(umShareListener)
                                            .setShareContent(weixinContent)
                                            .share();
                                }
                                if (share_media == SHARE_MEDIA.QQ || share_media == SHARE_MEDIA.QZONE) {
                                    new ShareAction(mycontext).setPlatform(share_media).setCallback(umShareListener)
                                            .setShareContent(qqShareContent)
                                            .share();
                                }
                               /* if (share_media == SHARE_MEDIA.TENCENT) {
                                    new ShareAction(mycontext).setPlatform(share_media).setCallback(umShareListener)
                                            .setShareContent(tencent)
                                            .share();
                                }*/
                                if (share_media == SHARE_MEDIA.EMAIL) {
                                    new ShareAction(mycontext).setPlatform(share_media).setCallback(umShareListener)
                                            .setShareContent(mail)
                                            .share();
                                }
                                if (share_media == SHARE_MEDIA.SMS||share_media == SHARE_MEDIA.DOUBAN ) {
                                    new ShareAction(mycontext).setPlatform(share_media).setCallback(umShareListener)
                                            .setShareContent(sms)
                                            .share();
                                }


                            }

                        }).open();
                break;
            case 2:
                //因为长链接问题，ShowFaces不能够实现QQ的分享
                new ShareAction(mycontext).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                        SHARE_MEDIA.QZONE
                )

                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

                                if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE || share_media == SHARE_MEDIA.WEIXIN_FAVORITE) {
                                    new ShareAction(mycontext).setPlatform(share_media).setCallback(umShareListener)
                                            .setShareContent(weixinContent)
                                            .share();
                                }
                                if (share_media == SHARE_MEDIA.QZONE) {
                                    new ShareAction(mycontext).setPlatform(share_media).setCallback(umShareListener)
                                            .setShareContent(qqShareContent)
                                            .share();
                                }

                            }

                        }).open();
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestoryShare() {

    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("SharePlatform", "platform成功" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mycontext, " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mycontext, " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mycontext, " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mycontext, " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
