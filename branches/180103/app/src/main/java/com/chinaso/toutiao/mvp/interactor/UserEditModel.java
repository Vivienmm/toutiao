package com.chinaso.toutiao.mvp.interactor;

import com.chinaso.toutiao.mvp.listener.RequestCallBack;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface UserEditModel<T> {
    void modifyPhotoInfo(RequestBody userId, RequestBody nickName, MultipartBody.Part photo,
                        RequestBody sign, RequestCallBack<T>callBack);

    void modifyUserInfo(RequestBody userId, RequestBody nickName, RequestBody sign, RequestCallBack<T>callBack);
}
