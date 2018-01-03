package com.chinaso.toutiao.app;

public final class SharedPreferencePrefUserInfo {

    /**
     * 设置app是否已登录
     *
     * @param flag
     */
    public static void setIsLogin(boolean flag) {
        SharedPreferencesSetting.setBoolean(PrefKey.APP_USER, PrefKey.APP_USER_IS_LOGIN_KEY, flag);
    }

    /**
     * 读取app是否登录
     *
     * @return
     */
    public static boolean getIsLogin() {
        return SharedPreferencesSetting.getBoolean(PrefKey.APP_USER, PrefKey.APP_USER_IS_LOGIN_KEY, false);
    }

    /**
     * 设置是否保存密码
     *
     * @param flag
     */
    public static void setIsSavePassword(boolean flag) {
        SharedPreferencesSetting.setBoolean(PrefKey.APP_USER, PrefKey.APP_USER_SAVE_PASSWORD, flag);
    }

    /**
     * 判断是否保存密码
     *
     * @return
     */
    public static boolean getIsSavePassword() {
        return SharedPreferencesSetting.getBoolean(PrefKey.APP_USER, PrefKey.APP_USER_SAVE_PASSWORD, false);
    }


    /**
     * 保存当前登录的userId
     *
     * @param id
     */
    public static void setUserId(long id) {
        SharedPreferencesSetting.setLong(PrefKey.APP_USER, PrefKey.APP_USER_IS_USERID, id);
    }

    public static Long getUserId() {
        return SharedPreferencesSetting.getLong(PrefKey.APP_USER, PrefKey.APP_USER_IS_USERID, 0);
    }

    /**
     * 保存当前登录账号类型
     *
     * @param type
     */
    public static void setUserType(int type) {
        SharedPreferencesSetting.setInteger(PrefKey.APP_USER, PrefKey.APP_USER_USERTYPE_KEY, type);
    }

    /**
     * 读取当前登录账号类型
     *
     * @return
     */
    public static Integer getUserType() {
        return SharedPreferencesSetting.getInteger(PrefKey.APP_USER, PrefKey.APP_USER_USERTYPE_KEY, -1);
    }


    /**
     * 保存当前登录用户名
     *
     * @param name
     */
    public static void setUserName(String name) {
        SharedPreferencesSetting.setString(PrefKey.APP_USER, PrefKey.APP_USER_USERNAME_KEY, name);
    }

    /**
     * 读取当前登录用户名
     *
     * @return
     */
    public static String getUserName() {
        return SharedPreferencesSetting.getString(PrefKey.APP_USER, PrefKey.APP_USER_USERNAME_KEY, "");
    }

    /**
     * 读取当前登录用户密码
     *
     * @return
     */
    public static String getUserPassword() {
        return SharedPreferencesSetting.getString(PrefKey.APP_USER, PrefKey.APP_USER_PASSWORD_KEY, "");
    }

    /**
     * 保存当前登录用户密码
     *
     * @param password
     */
    public static void setUserPassword(String password) {
        SharedPreferencesSetting.setString (PrefKey.APP_USER, PrefKey.APP_USER_PASSWORD_KEY, password);
    }


    /**
     * 设置是否已经注册
     *
     * @param flag
     */
    public static void setIsRegister(boolean flag) {
        SharedPreferencesSetting.setBoolean(PrefKey.APP_USER, PrefKey.APP_USER_IS_REGISTER_KEY, flag);
    }

    /**
     * 判断是否注册
     *
     * @return
     */
    public static boolean getIsRegister() {
        return SharedPreferencesSetting.getBoolean(PrefKey.APP_USER, PrefKey.APP_USER_IS_REGISTER_KEY, false);
    }

    /**
     * 个人设置跟帖设备名称
     * @param  deviceId
     */
    public static void setDeviceName(int deviceId){
        SharedPreferencesSetting.setInteger(PrefKey.APP_USER, PrefKey.APP_USER_COMMENT_DEVICE_ID, deviceId);
    }

    public static int getDeviceName(){
        return SharedPreferencesSetting.getInteger(PrefKey.APP_USER, PrefKey.APP_USER_COMMENT_DEVICE_ID, 0);
    }
}
