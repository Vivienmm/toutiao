package com.chinaso.toutiao.mvp.entity.user;

/**
 * Created by yangfang on 2016/1/13.
 * 手机注册返回的值
 */
public class RegisterSmsCode {
    public boolean result;//修改结果
    public String message;//错误信息
    public String code;//验证码

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public boolean isResult() {
        return result;
    }
}
