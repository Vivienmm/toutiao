package com.chinaso.toutiao.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by chinaso on 2017/3/6.
 */

public class SubScribeService {
    static SubScribeAPI instance;

    static public SubScribeAPI getInstance() {
        if (instance != null)
            return instance;
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("https://vivienmm.github.io")
                .addConverterFactory(GsonConverterFactory.create(gson))  //增加实体类请求
                .addConverterFactory(ScalarsConverterFactory.create())  //来增加字符串请求
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        instance = restAdapter.create(SubScribeAPI.class);

        return instance;
    }

}
