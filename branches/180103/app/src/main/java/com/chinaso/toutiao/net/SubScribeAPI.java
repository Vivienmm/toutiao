package com.chinaso.toutiao.net;

import com.chinaso.toutiao.mvp.entity.SubScribeColumn;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by chinaso on 2017/3/6.
 */

public interface SubScribeAPI {
    @GET(("/subscriber.json"))
    Call<List<SubScribeColumn>> getSubscribColumn();
}
