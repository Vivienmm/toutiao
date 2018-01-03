package com.chinaso.toutiao.mvp.presenter.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.app.entity.ModifyUserInfoResponse;
import com.chinaso.toutiao.mvp.entity.user.UserInfoManager;
import com.chinaso.toutiao.mvp.interactor.UserEditModel;
import com.chinaso.toutiao.mvp.interactor.impl.UserEditModelImpl;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;
import com.chinaso.toutiao.mvp.listener.UpdateUserInfoEvent;
import com.chinaso.toutiao.mvp.presenter.UserEditPresenter;
import com.chinaso.toutiao.mvp.view.UserEditView;
import com.chinaso.toutiao.mvp.view.base.BaseView;
import com.chinaso.toutiao.util.SignCodeUtil;
import com.chinaso.toutiao.util.secure.AESUtils;
import com.chinaso.toutiao.util.secure.JniUtil;
import com.chinaso.toutiao.util.secure.MD5Util;
import com.chinaso.toutiao.util.secure.PackageUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserEditPresenterImpl implements UserEditPresenter, RequestCallBack<ModifyUserInfoResponse> {
    private UserEditView mView;
    private String nickName;
    private MultipartBody.Part photo;
    private UserEditModel<ModifyUserInfoResponse> mUserModel = new UserEditModelImpl();

    @Override
    public void onCreate() {
        initUserNameOrAvatar();
    }

    /**
     * init username and avatar
     */
    private void initUserNameOrAvatar() {
        if (UserInfoManager.getInstance().getLoginResponse() != null) {
            mView.setUserName(UserInfoManager.getInstance().getLoginResponse().getNickName());
            mView.setAvatar(UserInfoManager.getInstance().getLoginResponse().getAvatar());
        } else {
            mView.showErrorMsg(TTApplication.getApp().getString(R.string.no_network));
        }
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mView = (UserEditView) view;
    }

    @Override
    public void success(ModifyUserInfoResponse data) {
        if (data != null) {
            mView.showToast("更新用户资料成功");
            UserInfoManager.getInstance().getLoginResponse().setAvatar(data.getAvatar());
            UserInfoManager.getInstance().getLoginResponse().setNickName(data.getNickName());
            UserInfoManager.getInstance().updateLocalLoginResponse();
            EventBus.getDefault().post(new UpdateUserInfoEvent(true));
            SharedPreferencePrefUserInfo.setIsRegister(false);
            mView.onSuccess(data);
        }
    }


    @Override
    public void onError(String errorMsg) {
        mView.showErrorMsg(errorMsg);
    }

    /**
     * update username and avatar
     */
    @Override
    public void saveNameAndAvatar() {
        String userId = String.valueOf(UserInfoManager.getInstance().getUserId());
        try {
            nickName = URLEncoder.encode(nickName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //secure check
        JniUtil jniUtil = new JniUtil();
        String userKey = JniUtil.getUserKey(PackageUtil.getSignature(TTApplication.getApp()));
        if (!TextUtils.isEmpty(userKey)) {
            userId = AESUtils.encode(userId, userKey);
        } else {
            mView.showErrorMsg("包签名错误");
            return;
        }
        TreeMap<String, String> map = new TreeMap<>();
        map.put("userId", "userId" + userId);
        map.put("nickName", "nickName" + nickName);
        String sign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
        if (photo != null) {
            mUserModel.modifyPhotoInfo(RequestBody.create(null, userId),
                    RequestBody.create(null, nickName), photo, RequestBody.create(null, sign), this);
        }else {
            mUserModel.modifyUserInfo(RequestBody.create(null, userId), RequestBody.create(null, nickName),
                    RequestBody.create(null, sign),this);
        }

    }

    @Override
    public void getNickName(String text) {
        this.nickName = text;
    }

    @Override
    public void getBodyPhoto(MultipartBody.Part photo) {
        this.photo = photo;
    }

}
