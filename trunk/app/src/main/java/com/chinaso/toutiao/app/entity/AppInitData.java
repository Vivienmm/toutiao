package com.chinaso.toutiao.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 初始化服务器数据
 */
public class AppInitData implements Serializable{
    private String abouturl;
    private String feedbackurl;
    private String helpurl;
    private List<VideoMenuVo> videoMenuVoList;
    private List<NewsMenuVo> newsMenuVoList;

    public String getAbouturl() {
        return abouturl;
    }

    public void setAbouturl(String abouturl) {
        this.abouturl = abouturl;
    }

    public String getFeedbackurl() {
        return feedbackurl;
    }

    public void setFeedbackurl(String feedbackurl) {
        this.feedbackurl = feedbackurl;
    }

    public String getHelpurl() {
        return helpurl;
    }

    public void setHelpurl(String helpurl) {
        this.helpurl = helpurl;
    }

    public List<VideoMenuVo> getVideoMenuVoList() {
        return videoMenuVoList;
    }

    public void setVideoMenuVoList(List<VideoMenuVo> videoMenuVoList) {
        this.videoMenuVoList = videoMenuVoList;
    }

    public List<NewsMenuVo> getNewsMenuVoList() {
        return newsMenuVoList;
    }

    public void setNewsMenuVoList(List<NewsMenuVo> newsMenuVoList) {
        this.newsMenuVoList = newsMenuVoList;
    }

    @Override
    public String toString() {
        return "aboutUrl = " + abouturl +
               "\\n" + "feedbackurl = " + feedbackurl +
                "\\n" + "helpUrl = "+ helpurl +
                "\\n" + "VideoMenuVo.length = "+videoMenuVoList.size() +
                "\\n" + "NewsMenuVo.length = " + newsMenuVoList.size();
    }
}
