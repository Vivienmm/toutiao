package com.chinaso.toutiao.mvp.entity.user;

import android.text.TextUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class LoginResponse extends RegisterSmsCode implements Serializable {

    private long userId;//用户id

    private String nickName;//昵称

    private String email;//邮箱

    private String avatar;//头像url

    private int emailStatus;//邮箱状态，0：未开通，1，已开通

    private String msisdn;//手机号码

    private int msisdnStatus;//手机状态，0：未开通，1，已开通

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public int getMsisdnStatus() {
        return msisdnStatus;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isResult() {

        return result;
    }

    public String getErrorMessage() {
        return message;
    }

    public long getUserId() {
        return userId;
    }

    public String getNickName() {
        if (TextUtils.isEmpty(nickName)) {
            return "";
        }
        try {
            nickName = URLDecoder.decode(nickName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getEmailStatus() {
        return emailStatus;
    }
}
