package com.chinaso.toutiao.mvp.presenter;

import com.chinaso.toutiao.mvp.presenter.base.BasePresenter;

import okhttp3.MultipartBody;

public interface UserEditPresenter extends BasePresenter {
    void saveNameAndAvatar();

    void getNickName(String text);

    void getBodyPhoto(MultipartBody.Part photo);
}
