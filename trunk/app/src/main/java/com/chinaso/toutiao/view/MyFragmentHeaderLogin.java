package com.chinaso.toutiao.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.entity.user.UserInfoManager;
import com.chinaso.toutiao.mvp.interactor.impl.LoginInteractorImpl;
import com.chinaso.toutiao.mvp.listener.RequestCallBack;
import com.chinaso.toutiao.mvp.ui.activity.LoginActivity;
import com.chinaso.toutiao.mvp.ui.activity.RegisterActivity;
import com.chinaso.toutiao.mvp.ui.activity.SettingActivity;
import com.chinaso.toutiao.util.GlideCircleTransform;
import com.chinaso.toutiao.util.SignCodeUtil;
import com.chinaso.toutiao.util.secure.AESUtils;
import com.chinaso.toutiao.util.secure.JniUtil;
import com.chinaso.toutiao.util.secure.MD5Util;
import com.chinaso.toutiao.util.secure.PackageUtil;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

import static com.chinaso.toutiao.R.id.qq_login;
import static com.chinaso.toutiao.R.id.user_edit;
import static com.chinaso.toutiao.R.id.user_login;
import static com.chinaso.toutiao.R.id.user_register;
import static com.chinaso.toutiao.R.id.weibo_login;

public class MyFragmentHeaderLogin extends LinearLayout implements View.OnClickListener, RequestCallBack<LoginResponse> {
    @BindView(R.id.settingActivity)
    TextView settingActivity;
    @BindView(user_edit)
    ImageView userEdit;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.successLoginLayout)
    RelativeLayout successLoginLayout;
    @BindView(user_login)
    TextView userLogin;
    @BindView(R.id.view_login_divider)
    View viewLoginDivider;
    @BindView(user_register)
    TextView userRegister;
    @BindView(R.id.phoneNumLoginLayout)
    RelativeLayout phoneNumLoginLayout;
    @BindView(R.id.weixin_login)
    TextView weixinLogin;
    @BindView(weibo_login)
    TextView weiboLogin;
    @BindView(qq_login)
    TextView qqLogin;
    @BindView(R.id.otherLoginLayout)
    LinearLayout otherLoginLayout;

    private Context context;

    private SHARE_MEDIA platfrom = null;
    private UMShareAPI mShareAPI;

    private String nickName;
    private String userId;
    private String imgUrl;

    private LoginListener loginListener;

    public MyFragmentHeaderLogin(Context context) {
        super(context);
        initView();
    }

    public MyFragmentHeaderLogin(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyFragmentHeaderLogin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    private void initView() {
        context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.login_layout, this);
        settingActivity = (TextView) view.findViewById(R.id.settingActivity);
        userLogin = (TextView) view.findViewById(user_login);
        userRegister = (TextView) view.findViewById(user_register);
        weixinLogin = (TextView) view.findViewById(R.id.weixin_login);
        weiboLogin = (TextView) view.findViewById(weibo_login);
        qqLogin = (TextView) view.findViewById(qq_login);
        successLoginLayout = (RelativeLayout) view.findViewById(R.id.successLoginLayout);
        phoneNumLoginLayout = (RelativeLayout) view.findViewById(R.id.phoneNumLoginLayout);
        otherLoginLayout = (LinearLayout) view.findViewById(R.id.otherLoginLayout);
        userEdit = (ImageView) view.findViewById(user_edit);
        userName = (TextView) view.findViewById(R.id.user_name);

        settingActivity.setOnClickListener(this);
        userLogin.setOnClickListener(this);
        userRegister.setOnClickListener(this);
        weixinLogin.setOnClickListener(this);
        weiboLogin.setOnClickListener(this);
        qqLogin.setOnClickListener(this);

        initLogin();
    }

    private int mAccountType = -1;// 0:email 1:phone 2:third par

    private void initLogin() {
        mShareAPI = UMShareAPI.get(TTApplication.getApp());
        mAccountType = UserInfoManager.getInstance().getUserType();
    }

    public UMShareAPI getUmShareAPI() {
        return mShareAPI;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.settingActivity:
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
            case user_login:
                context.startActivity(new Intent(context, LoginActivity.class));
                break;
            case user_register:
                context.startActivity(new Intent(context, RegisterActivity.class));
                break;
            case R.id.weixin_login:
                platfrom = SHARE_MEDIA.WEIXIN;
                loginByThird();
                break;
            case weibo_login:
                Toast.makeText(context, "暂未开通", Toast.LENGTH_SHORT).show();
                break;
            case qq_login:
                platfrom = SHARE_MEDIA.QQ;
                loginByThird();
                break;
            default:
                break;
        }
    }

    private void loginByThird() {
        if (mShareAPI.isInstall(TTApplication.mActivity, platfrom)) {
            mShareAPI.doOauthVerify((Activity) context, platfrom, umAuthListener);
        } else {
//            ToastUtil.showToast(TTApplication.mActivity, "没有安装" + platform + "客户端，无法" + platform + "登陆", R.mipmap.toast_net_err);
        }
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(context, "登录 成功", Toast.LENGTH_SHORT).show();
            Log.i("dsc", "onComplete: " + "success");
            mShareAPI.getPlatformInfo(TTApplication.mActivity, platform, umGetInfoListener);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(context, "授权 失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(context, "授权 取消", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * getUserInfo
     * QQ:{is_yellow_vip=0, yellow_vip_level=0, profile_image_url=http://q.qlogo.cn/qqapp/1105890066/B2C578A25A9EBD4FD1B3F76B4E4F3EBF/100,
     * screen_name=琉璃半岛, msg=, vip=0, city=丰台, gender=男, province=北京, level=0,
     * is_yellow_year_vip=0, openid=B2C578A25A9EBD4FD1B3F76B4E4F3EBF}
     * <p>
     * weixin:{{unionid=oMhzgv_PpYjH9Z_xbXor9hUrYq04, country=中国, nickname=城主, city=, province=北京, language=zh_CN,
     * headimgurl=http://wx.qlogo.cn/mmopen/PiajxSqBRaEIfJsMKQnEnt3iab3o22LsrFA5nffnX4yvguzDuTNg4rI441WDj5ZnRneCPKmwbj5CtrNceNEcAdBA/0,
     * sex=1, openid=o4vUjwJ48YJi6qxfuxtWOA5pblOI}}
     **/
    private UMAuthListener umGetInfoListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data == null) {
                return;
            }
            Log.i("dsc", "onComplete(data)d: " + data);
            if (platform == SHARE_MEDIA.QQ) {
                nickName = data.get("screen_name");
                Log.i("dsc", "onComplete: " + nickName);
                Set<String> keys = data.keySet();
                for (String key : keys) {
                    if (key == "screen_name") {
                        nickName = data.get(key);
                    }
                    if (key == "openid") {
                        userId = data.get("openid");
                    }
                    if (key == "profile_image_url") {
                        imgUrl = data.get("profile_image_url");
                    }
                }
                loginThirdpart("qq");

            } else if (platform == SHARE_MEDIA.WEIXIN) {
                Set<String> keys = data.keySet();
                for (String key : keys) {
                    if (key == "nickname") {
                        nickName = data.get(key);
                        nickName = enValid(nickName);
                    }
                    if (key == "unionid") {
                        userId = data.get(key);
                    }
                    //headimgurl
                    if (key == "headimgurl") {
                        imgUrl = data.get("headimgurl");
                    }
                }

                loginThirdpart("weixin");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(context, "获取用户信息失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(context, "获取用户信息失败", Toast.LENGTH_SHORT).show();
        }
    };

    private String enValid(String string) {
        String regex = "[a-zA-Z0-9\u4e00-\u9fa5]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(string);
        String result = "";
        while (match.find()) {
            result = result + match.group(0);
        }
        return result;
    }

    public void loginThirdpart(String provider) {
        String platform;
        JniUtil jniUtil = new JniUtil();
        String userKey = JniUtil.getUserKey(PackageUtil.getSignature(TTApplication.getApp()));
        if (!TextUtils.isEmpty(userKey)) {
            userId = AESUtils.encode(userId, userKey);
            platform = AESUtils.encode(provider, userKey);
        } else {
            Toast.makeText(context, "包签名错误", Toast.LENGTH_SHORT).show();
            return;
        }

        TreeMap<String, String> map = new TreeMap<>();
        map.put("oauth_provider", "oauth_provider" + platform);
        map.put("oauth_userid", "oauth_userid" + userId);
        map.put("nickname", "nickname" + nickName);
        map.put("avatar", "avatar" + imgUrl);
        String sign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
        new LoginInteractorImpl().thirdLogin(platform, userId, nickName, imgUrl, sign, this);
    }

    public void updateHeaderLoginUI() {
        phoneNumLoginLayout.setVisibility(GONE);
        otherLoginLayout.setVisibility(GONE);
        successLoginLayout.setVisibility(VISIBLE);
        LoginResponse loginfo = UserInfoManager.getInstance().getLoginResponse();
        if (!TextUtils.isEmpty(loginfo.getAvatar())) {
            Glide.with(context)
                    .load(UserInfoManager.getInstance().getLoginResponse().getAvatar())
                    .bitmapTransform(new GlideCircleTransform(context))
                    .crossFade(1000)
                    .into(userEdit);
            userName.setText(UserInfoManager.getInstance().getLoginResponse().getNickName());
        }
    }

    public void updateHeaderLogoutUI() {
        phoneNumLoginLayout.setVisibility(VISIBLE);
        otherLoginLayout.setVisibility(VISIBLE);
        successLoginLayout.setVisibility(GONE);
    }

    @Override
    public void success(LoginResponse data) {
        SharedPreferencePrefUserInfo.setIsSavePassword(true);
        SharedPreferencePrefUserInfo.setUserId(data.getUserId());
        UserInfoManager.getInstance().setLoginSuccess(data, mAccountType, nickName, "");
        updateHeaderLoginUI();
        loginListener.successLogin(true);
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }

    public interface LoginListener {
        void successLogin(boolean flag);
    }
}
