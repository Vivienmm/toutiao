package com.chinaso.toutiao.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VideoService {
    static VideoServiceAPI instance;
    static public VideoServiceAPI getInstance() {
        if (instance != null)
            return instance;
        Gson gson=new GsonBuilder().setLenient().create();
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("http://video.chinaso.com")
                .addConverterFactory(GsonConverterFactory.create(gson))  //增加实体类请求
                .addConverterFactory(ScalarsConverterFactory.create())  //来增加字符串请求
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        instance = restAdapter.create(VideoServiceAPI.class);

        return instance;
    }
}
