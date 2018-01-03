package com.chinaso.toutiao.app.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常情况下default数据
 */
public class AppDefInitData {

    public static String[] hotWordDefList = {"美食", "电影", "酒店住宿", "休闲娱乐", "外卖", "自助餐", "KTV", "机票/火车票", "周边游", "美甲美睫",
            "火锅", "生日蛋糕", "甜品饮品", "水上乐园", "汽车服务", "美发", "丽人", "景点", "足疗按摩", "运动健身", "健身", "超市", "买菜",
            "小吃快餐"};

    public static String feedbackURL = "http://m.chinaso.com/yjfk_app.html";
    public static String aboutURL="http://m.chinaso.com/aboutus_app.html";
    public static String helpURL="http://m.chinaso.com/ydy_app.html#";

    public static List<VideoMenuVo> videoMenuVoList=new ArrayList<>();
    public static List<NewsMenuVo> newsMenuVoList=new ArrayList<>();

    static{

        String JsonVideoStr = "[{\"name\":\"热播\",\"id\":\"video_热播\"}," +
                "{\"name\":\"新闻\",\"id\":\"video_新闻\"}," +
                "{\"name\":\"搞笑\",\"id\":\"video_搞笑\"}," +
                "{\"name\":\"汽车\",\"id\":\"video_汽车\"}," +
                "{\"name\":\"微镜头\",\"id\":\"video_微镜头\"}," +
                "{\"name\":\"体育\",\"id\":\"video_体育\"}]";
        try {
            JSONArray jsonVideoColums = new JSONArray(JsonVideoStr);
            for (int i = 0; i < jsonVideoColums.length(); i++) {
                JSONObject columItem = jsonVideoColums.getJSONObject(i);

                VideoMenuVo colum = new VideoMenuVo();
                colum.setName(columItem.optString("name"));
                colum.setId(columItem.optString("id"));

                videoMenuVoList.add(colum);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String newsJsonStr = "[{\"id\":\"news_all\",\"name\":\"热搜\",\"lock\":true,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_326862\",\"name\":\"国际\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_326866\",\"name\":\"财经\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_326526\",\"name\":\"互联网\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_326868\",\"name\":\"军情\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3275482\",\"name\":\"娱乐\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3271500\",\"name\":\"科技\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3271036\",\"name\":\"美食\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3274700\",\"name\":\"体育\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_325790\",\"name\":\"报刊\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3275478\",\"name\":\"家居\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_326882\",\"name\":\"社会\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3283694\",\"name\":\"汽车\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3283946\",\"name\":\"健康\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3283942\",\"name\":\"图片\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_326864\",\"name\":\"政务\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3294446\",\"name\":\"头条\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3297276\",\"name\":\"品读\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3297270\",\"name\":\"教育\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3288740\",\"name\":\"江苏\",\"lock\":false,\"type\":\"地方\",\"show\":false}," +
                "{\"id\":\"news_3287542\",\"name\":\"旅游\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3291146\",\"name\":\"影视\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3291148\",\"name\":\"时评\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3291150\",\"name\":\"美图\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3291156\",\"name\":\"美容\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3293632\",\"name\":\"大连\",\"lock\":false,\"type\":\"地方\",\"show\":false}," +
                "{\"id\":\"news_3293730\",\"name\":\"东北\",\"lock\":false,\"type\":\"地方\",\"show\":false}," +
                "{\"id\":\"news_3293750\",\"name\":\"河北\",\"lock\":false,\"type\":\"地方\",\"show\":false}," +
                "{\"id\":\"news_3293756\",\"name\":\"河南\",\"lock\":false,\"type\":\"地方\",\"show\":false}," +
                "{\"id\":\"news_3294094\",\"name\":\"山东\",\"lock\":false,\"type\":\"地方\",\"show\":false}," +
                "{\"id\":\"news_3295940\",\"name\":\"徐州\",\"lock\":false,\"type\":\"地方\",\"show\":false}," +
                "{\"id\":\"news_3299250\",\"name\":\"人文\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3299254\",\"name\":\"生活\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3299258\",\"name\":\"港澳台\",\"lock\":false,\"type\":\"推荐\",\"show\":false}," +
                "{\"id\":\"news_3299782\",\"name\":\"浙江\",\"lock\":false,\"type\":\"地方\",\"show\":false}," +
                "{\"id\":\"news_3301348\",\"name\":\"传媒\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3291158\",\"name\":\"时尚\",\"lock\":false,\"type\":\"推荐\",\"show\":true}," +
                "{\"id\":\"news_3303674\",\"name\":\"福建\",\"lock\":false,\"type\":\"地方\",\"show\":false}]}";
        try {
            JSONArray jsonNewsColums = new JSONArray(newsJsonStr);
            for (int i = 0; i < jsonNewsColums.length(); i++) {
                JSONObject columItem = jsonNewsColums.getJSONObject(i);

                NewsMenuVo colum = new NewsMenuVo();
                colum.setName(columItem.optString("name"));
                colum.setId(columItem.optString("id"));
                colum.setType(columItem.optString("type"));
                colum.setLock(columItem.optBoolean("lock"));
                colum.setShow(columItem.optBoolean("show"));

                newsMenuVoList.add(colum);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
