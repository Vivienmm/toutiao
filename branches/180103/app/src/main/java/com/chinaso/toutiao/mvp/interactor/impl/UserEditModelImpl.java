package com.chinaso.toutiao.mvp.interactor.impl;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.entity.ModifyUserInfoResponse;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.interactor.UserEditModel;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;
import com.chinaso.toutiao.net.NetworkService;
import com.chinaso.toutiao.util.DebugUtil;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserEditModelImpl implements UserEditModel<ModifyUserInfoResponse> {

    @Override
    public void modifyPhotoInfo(RequestBody userId, RequestBody nickName, MultipartBody.Part photo, RequestBody sign, final RequestCallBack<ModifyUserInfoResponse> callBack) {
        Call<ModifyUserInfoResponse> modifyUserInfoResponseCall = NetworkService.getSplashInstance().modifyUserInfo(userId, nickName, photo, sign);
        modifyUserInfoResponseCall.enqueue(new Callback<ModifyUserInfoResponse>() {
            @Override
            public void onResponse(Call<ModifyUserInfoResponse> call, Response<ModifyUserInfoResponse> response) {
                ModifyUserInfoResponse modifyUserInfoResponse = response.body();
                if (modifyUserInfoResponse == null) {
                    DebugUtil.e(DebugUtil.TAG_NET_ERR, "响应空，接口异常");
                    return;
                }
                if (modifyUserInfoResponse.isResult()) {
                    callBack.success(modifyUserInfoResponse);

                } else {
                    callBack.onError("更新失败: " + modifyUserInfoResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ModifyUserInfoResponse> call, Throwable t) {
                callBack.onError(TTApplication.getApp().getResources().getString(R.string.register_loda_failure));
            }
        });
    }

    @Override
    public void modifyUserInfo(RequestBody userId, RequestBody nickName, RequestBody sign, final RequestCallBack<ModifyUserInfoResponse> callBack) {
        Call<ModifyUserInfoResponse> modifyUserInfoResponseCall = NetworkService.getSplashInstance().modifyUserInfo(userId, nickName, sign);
        modifyUserInfoResponseCall.enqueue(new Callback<ModifyUserInfoResponse>() {
            @Override
            public void onResponse(Call<ModifyUserInfoResponse> call, Response<ModifyUserInfoResponse> response) {
                ModifyUserInfoResponse modifyUserInfoResponse = response.body();
                if (modifyUserInfoResponse == null) {
                    DebugUtil.e(DebugUtil.TAG_NET_ERR, "响应空，接口异常");
                    return;
                }
                if (modifyUserInfoResponse.isResult()) {
                    callBack.success(modifyUserInfoResponse);

                } else {
                    callBack.onError("更新失败: " + modifyUserInfoResponse.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ModifyUserInfoResponse> call, Throwable t) {
                callBack.onError(TTApplication.getApp().getResources().getString(R.string.register_loda_failure));
            }
        });
    }
}
