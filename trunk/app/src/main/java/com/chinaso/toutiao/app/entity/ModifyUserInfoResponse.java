package com.chinaso.toutiao.app.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/29 0029.
 */
public class ModifyUserInfoResponse implements Serializable {
    boolean result;//登录结果是否正确

    String message;//错误信息

    long userId;//用户id

    String nickName;//昵称

    String email;//邮箱

    String avatar;//头像url

    int emailStatus;//邮箱状态，0：未开通，1，已开通

    String msisdn;//手机号码

    public String getMsisdn() {
        return msisdn;
    }

    public int getMsisdnStatus() {
        return msisdnStatus;
    }

    int msisdnStatus;//手机状态，0：未开通，1，已开通

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isResult() {

        return result;
    }

    public String getMessage() {
        return message;
    }

    public long getUserId() {
        return userId;
    }

    public String getNickName() {
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
