package com.chinaso.toutiao.app;

public class Constants {
    public static final String SERVER_TEST_BASE = "http://m.toutiao.chinaso365.com";
    public static final String SERVER_BASE = "http://m.toutiao.chinaso.com";
    public static final String CHINASO_BASE = "http://mob.chinaso.com";
    //举报页面的接口
    public static final String REPORT_PROBLEM = "http://m.news.chinaso.com/news_jubao.html?url=";

    public static final String MOB_SHITU_BASE = "http://shitu.chinaso.com"; //上传用户操作服务器
    //识图错误页跳转
    public static final String SHITU_ERR = "http://shitu.chinaso.com/erro.php";
    //推送通知
    public static final String MY_MSG = "/html/pushnews.html";

    public static final String REGISTER_SERVER = CHINASO_BASE + "/html/serviceAgreement.html"; //注册服务的国搜协议
    public static final String NEWSDETAIL_COMMENT_URL = CHINASO_BASE + "//1/comment/queryComment?"; //评论页面的接口
    //版本更新
    public static final int VERSION_UPDATE_NO = 0x500;
    public static final int VERSION_UPDATE_YES = 0x501;
    public static final int VERSION_UPDATE_IGNORE = 0x502;
    public static final int VERSION_UPDATE_NO_NETWORK = 0x503;
    public static final int VERSION_UPDATE_TIME_OUT = 0x504;
    public static final int VERSION_UPDATE_INIT_REQUEST = 0x505;
    public static final int VERSION_UPDATE_SETTING_REQUEST = 0x506;

    //两次点击最短时间间隔
    public static final int MIN_TIME_BETWEEN_CLICK = 1000;

    public static final String SECRET_KEY = "Chinaso@2015";

    //收藏
    public static final int COLLECTION_ALREADY = 1;
    public static final int COLLECTION_SUCCEEDED = 2;
    public static final int COLLECTION_FAILED = 3;
    public static final String MIPUSH = "push";

    //阅读
    public static final int READ_ALREADY = 1;
    public static final int READ_SUCCEEDED = 2;
    public static final int READ_FAILED = 3;

    //回看封面最多显示的广告数目
    public static final int MAX_COVER_COUNT = 5;

    //rn bundle 版本号
    public static  final  String BUNDLE_VERSION="201701121430";
    public static final int USER_AVATAR_MAX_SIZE = 100;
}
