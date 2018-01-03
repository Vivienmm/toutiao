package com.chinaso.toutiao.net;

import com.chinaso.toutiao.app.entity.VideoItemAPI;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface VideoServiceAPI {
    @GET("/api/vmsVideo.php")
    Observable<VideoItemAPI> fetchVideoItemAPI(@Query("GlobalID") String id);
}
