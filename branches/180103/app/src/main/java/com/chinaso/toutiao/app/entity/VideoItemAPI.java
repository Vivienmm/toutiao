package com.chinaso.toutiao.app.entity;

import java.util.List;

public class VideoItemAPI {
    /**
     * GlobalID : 5956460
     * title : 第八届亚冬会开幕 中国选手收获一银一铜
     * videos : [{"type":"v","url":""},{"type":"v_sd","url":"http://vod.chinaso.com/video/2017/2/20/20172201487549837586_106_2.mp4"},{"type":"v_hd","url":"http://vod.chinaso.com/video/2017/2/20/20172201487549837586_106_1.mp4"}]
     * photo : http://n1.video.pg0.cn//images/2017/2/20//20172201487549837586_106_1.jpg
     * Summary : null
     * id : 5956460
     */

    private String GlobalID;
    private String title;
    private String photo;
    private Object Summary;
    private String id;
    private List<VideosEntity> videos;

    public String getGlobalID() {
        return GlobalID;
    }

    public void setGlobalID(String GlobalID) {
        this.GlobalID = GlobalID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Object getSummary() {
        return Summary;
    }

    public void setSummary(Object Summary) {
        this.Summary = Summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<VideosEntity> getVideos() {
        return videos;
    }

    public void setVideos(List<VideosEntity> videos) {
        this.videos = videos;
    }

    public static class VideosEntity {
        /**
         * type : v
         * url :
         */

        private String type;
        private String url;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    @Override
    public String toString() {
        return "[VideoItemAPI:{GlobalID:"+GlobalID+", title:"+title+", id:"+id+"} ";
    }
}
