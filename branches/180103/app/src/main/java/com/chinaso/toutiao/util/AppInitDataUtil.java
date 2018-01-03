package com.chinaso.toutiao.util;


import android.text.TextUtils;

import com.chinaso.toutiao.app.SharedPreferenceAPPInitData;
import com.chinaso.toutiao.app.entity.AppInitData;
import com.chinaso.toutiao.app.entity.AppDefInitData;
import com.chinaso.toutiao.app.entity.NewsMenuVo;
import com.chinaso.toutiao.app.entity.VideoMenuVo;

import java.util.Arrays;
import java.util.List;

import static com.chinaso.toutiao.app.entity.AppDefInitData.newsMenuVoList;


/**
 * Created by liuyao on 2016/7/2 0002.
 * 所有首页初始化数据调用在此封装，默认返回AppDefAllData里的默认值
 */
public class AppInitDataUtil {
    private static AppInitData initData;
    static{
        if (initData == null) {
            if (SharedPreferenceAPPInitData.getAppInitFlag()) {
                initData = SharedPreferenceAPPInitData.getAppInitData();
            }
        }
    }
    public static String getFeedbackurl() {
        AppInitData data = initData;
        if(data != null){
            String feedbackURL = data.getFeedbackurl();
            if(!TextUtils.isEmpty(feedbackURL)){
                return feedbackURL;
            }
        }
        return AppDefInitData.feedbackURL;
    }
    public static String getAboutUrl() {
        AppInitData data = initData;
        if(data != null){
            String aboutURL = data.getAbouturl();
            if(!TextUtils.isEmpty(aboutURL)){
                return aboutURL;
            }
        }
        return AppDefInitData.aboutURL;
    }
    public static String getHelpUrl() {
        AppInitData data = initData;
        if(data != null){
            String helpURL = data.getHelpurl();
            if(!TextUtils.isEmpty(helpURL)){
                return helpURL;
            }
        }
        return AppDefInitData.helpURL;
    }

    public static List<String> getHotWordList() {
        AppInitData data = initData;
        return Arrays.asList(AppDefInitData.hotWordDefList);
    }
    public static List<VideoMenuVo>getVideoMenuVoList(){
        AppInitData data = initData;
        if(data != null){
            List<VideoMenuVo> videoMenuVoList = data.getVideoMenuVoList();
            if (!(videoMenuVoList==null || videoMenuVoList.size()==0)) {
                return videoMenuVoList;
            }
        }
        return AppDefInitData.videoMenuVoList;
    }
    public static List<NewsMenuVo> getNewsMenuVoList(){
        AppInitData data = initData;
        if(data != null){
            List<NewsMenuVo> newsMenuVoList = data.getNewsMenuVoList();
            if (!(newsMenuVoList==null || newsMenuVoList.size()==0)) {
                return newsMenuVoList;
            }
        }
        return newsMenuVoList;
    }

    public static void setAppInitData(AppInitData data) {
        initData=data;
    }

}
