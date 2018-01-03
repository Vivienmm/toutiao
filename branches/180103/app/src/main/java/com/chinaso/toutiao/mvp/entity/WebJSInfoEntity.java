package com.chinaso.toutiao.mvp.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-9-8.
 * 该实体类：网页新闻详情通过JS传递到本地的数据
 * 旧的js交互数据：{title, url, content, pic, count, commentUrl, nid, contentId, fchkLcom};
 * 最新的action：{title, url, content, pic, commentURL, nid, contentId}
 */
public class WebJSInfoEntity implements Parcelable {
    /** 都有的 */
    private String title;//分享标题
    private String url;//分享目标链接
    private String content;//分享内容
    private String pic;//分享图片链接
    private String nid;
    private String commentURL;
    private String contentId;

    /** 旧的 */
    private String count;
    private String fchkLcom;

    /** 最新的 */
    private String font;

    private Bitmap defaultImg;//没有图片URL时默认的图片

    public String getCommentURL() {
        return commentURL;
    }

    public void setCommentURL(String commentURL) {
        this.commentURL = commentURL;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Bitmap getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(Bitmap defaultImg) {
        this.defaultImg = defaultImg;
    }

    public String getFchkLcom() {
        return fchkLcom;
    }

    public void setFchkLcom(String fchkLcom) {
        this.fchkLcom = fchkLcom;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "title="+title+"; url="+url+"; content="+content+"; pic=" + pic+"; nid="+nid +"; commentURL = " + commentURL+"; contentId="+contentId+"; font = ";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.content);
        dest.writeString(this.pic);
        dest.writeString(this.nid);
        dest.writeString(this.commentURL);
        dest.writeString(this.contentId);
        dest.writeString(this.count);
        dest.writeString(this.fchkLcom);
        dest.writeString(this.font);
        dest.writeParcelable(this.defaultImg, flags);
    }

    public WebJSInfoEntity() {
    }

    protected WebJSInfoEntity(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.content = in.readString();
        this.pic = in.readString();
        this.nid = in.readString();
        this.commentURL = in.readString();
        this.contentId = in.readString();
        this.count = in.readString();
        this.fchkLcom = in.readString();
        this.font = in.readString();
        this.defaultImg = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<WebJSInfoEntity> CREATOR = new Creator<WebJSInfoEntity>() {
        @Override
        public WebJSInfoEntity createFromParcel(Parcel source) {
            return new WebJSInfoEntity(source);
        }

        @Override
        public WebJSInfoEntity[] newArray(int size) {
            return new WebJSInfoEntity[size];
        }
    };
}
