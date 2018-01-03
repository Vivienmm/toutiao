package com.chinaso.toutiao.net;


import android.graphics.Bitmap;

import com.chinaso.toutiao.app.entity.AppInitData;
import com.chinaso.toutiao.app.entity.ModifyUserInfoResponse;
import com.chinaso.toutiao.app.entity.NewsList;
import com.chinaso.toutiao.app.entity.VideosList;
import com.chinaso.toutiao.app.entity.update.VersionUpdateResponse;
import com.chinaso.toutiao.mvp.entity.InsertComment;
import com.chinaso.toutiao.mvp.entity.QueryCommentNum;
import com.chinaso.toutiao.mvp.entity.QuestionResponse;
import com.chinaso.toutiao.mvp.entity.ShituResponse;
import com.chinaso.toutiao.mvp.entity.StartUpEntity;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface NetworkServiceAPI {
    //init
    @GET("/1/initializaiton")
    Observable<AppInitData> getInitData();

    //get list
    @GET("/category/main")
    Call<List<String>> getList(@Query("id") String id);

    //version update - 测试接口内容未更新
    @GET("/1/server/appupdate")
    Call<VersionUpdateResponse> getServerVersionInfo(@Query("version") int version_code);

    @GET("/1/smsCode")
    Call<LoginResponse> smsCode(@Query("mobileNumber") String mobileNumber, @Query("sign") String sign, @Query("type") String type);

    @GET("/1/register")
    Call<LoginResponse> register(@Query("username") String username, @Query("password") String password, @Query("smsCode") String smsCode, @Query("sign") String sign);

    @GET("/1/login")
    Call<LoginResponse> login(@Query("username") String username, @Query("password") String password, @Query("sign") String sign);

    //problem
    @GET("/1/question")
    Call<QuestionResponse> getQuestionType(@Query("url") String url, @Query("types") String types);

//    //启动页
//    @GET("/1/startuppage")
//    Call<List<SplashEntity>> getSplashBkgData();

    //新的启动页
    @GET("/1/newstartuppage")
    Observable<StartUpEntity> getNewSplashBkgData(@Query("ids") String ids);

    //shitu
    @Multipart
    @POST("/index.php/Home/ClassifiyApp/androidUpload")
    Call<ShituResponse> shituUpload(@Part MultipartBody.Part photo);

    //第三方登录
    @GET("/1/thirdpartlogin")
    Call<LoginResponse> thirdLogin(@Query("oauth_provider") String oauth_provider,
                                   @Query("oauth_userid") String oauth_userid, @Query("nickname") String nickname,
                                   @Query("avatar") String avatar, @Query("sign") String sign);

    @GET("/1/findpd")
    Call<LoginResponse> findpd(@Query("mobileNumber") String mobileNumber, @Query("password") String password, @Query("smsCode") String smsCode, @Query("sign") String sign);


    @GET("/1/comment/insertcomment")
    Call<InsertComment> insertcomment(@Query("uid") Integer uid, @Query("nick") String nick, @Query("content") String content, @Query("nid") String nid, @Query("contentId") String contentId, @Query("sign") String sign);

    //modify userinfo
    @Multipart
    @POST("/1/modifyUserInfo")
    Call<ModifyUserInfoResponse> modifyUserInfo(@Part("userId") RequestBody userId, @Part("nickName") RequestBody nickName, @Part MultipartBody.Part photo, @Part("sign") RequestBody sign);

    @Multipart
    @POST("/1/modifyUserInfo")
    Call<ModifyUserInfoResponse> modifyUserInfo(@Part("userId") RequestBody userId, @Part("nickName") RequestBody nickName, @Part("sign") RequestBody sign);

    @GET("/1/comment/querycommentnum")
    Observable<QueryCommentNum> querycommentnum(@Query("nid") String nid);

    @GET("/1/category/main")
    Observable<NewsList> fetchNewsList(@Query("id") String id, @Query("page") String page);

    @GET("/1/category/main")
    Observable<VideosList> fetchVideosList(@Query("id") String id, @Query("page") String page);

    @GET()
    Observable<Bitmap> fetchBitmap();
}

   
