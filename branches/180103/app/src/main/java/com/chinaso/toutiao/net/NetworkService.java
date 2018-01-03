package com.chinaso.toutiao.net;


import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.TTApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class NetworkService {
    static NetworkServiceAPI instance;

	static NetworkServiceAPI splash_instance;
	static NetworkServiceAPI shituInstance;

	static public NetworkServiceAPI getInstance(){
		if (instance != null)
			return instance;
		Gson gson=new GsonBuilder().setLenient().create();
		Retrofit restAdapter = new Retrofit.Builder()
				.baseUrl(TTApplication.getServer())
				.addConverterFactory(GsonConverterFactory.create(gson))  //增加实体类请求
				.addConverterFactory(ScalarsConverterFactory.create())  //来增加字符串请求
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.build();
		instance = restAdapter.create(NetworkServiceAPI.class);

		return instance;
	}



	static public NetworkServiceAPI getSplashInstance() {
		if(splash_instance != null) {
			return splash_instance;
		}

		Retrofit restAdapter = new Retrofit.Builder()
				.baseUrl(Constants.CHINASO_BASE)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		splash_instance = restAdapter.create(NetworkServiceAPI.class);

		return splash_instance;
	}


	static public NetworkServiceAPI getShituInstance(){
		if (shituInstance != null)
			return shituInstance;

		Gson gson=new GsonBuilder().setLenient().create();
		Retrofit restAdapter = new Retrofit.Builder()
				.baseUrl(Constants.MOB_SHITU_BASE)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();
		shituInstance = restAdapter.create(NetworkServiceAPI.class);

		return shituInstance;
	}
}
