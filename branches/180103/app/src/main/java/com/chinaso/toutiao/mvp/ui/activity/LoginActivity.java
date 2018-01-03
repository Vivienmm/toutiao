package com.chinaso.toutiao.mvp.ui.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.listener.UpdateUserInfoEvent;
import com.chinaso.toutiao.mvp.presenter.impl.LoginPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.view.LoginView;
import com.chinaso.toutiao.util.PhoneUtils;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.view.CustomActionBar;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.greenrobot.event.EventBus;


public class LoginActivity extends BaseActivity implements LoginView {
    @BindView(R.id.actionbar)
    CustomActionBar customActionBar;
    @BindView(R.id.login_account)
    TextInputEditText account;
    @BindView(R.id.login_password)
    TextInputEditText mPassword;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.qq_login)
    ImageView qq_login;
    @BindView(R.id.weixin_login)
    ImageView weixin_login;
    @BindView(R.id.tv_forget_password)
    TextView forget_password;
    @BindView(R.id.register)
    TextView mRegister;

    LoginPresenterImpl mPresenter = new LoginPresenterImpl();
    private SHARE_MEDIA platfrom;
    private String username;
    private String password;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {
        initActionBar();
        mPresenter.attachView(this);
        mPresenter.onCreate();
        account.clearFocus();
    }

    @OnTextChanged({R.id.login_account, R.id.login_password})
    void afterTextChanged() {
        if (!TextUtils.isEmpty(PhoneUtils.getText(mPassword))
                && !TextUtils.isEmpty(account.getText().toString())) {
            login.setEnabled(true);
        } else {
            login.setEnabled(false);
        }
    }

    private void initActionBar() {
        customActionBar.setTitleView(getString(R.string.login));
        customActionBar.setLeftViewImg(R.mipmap.actionbar_back);
        customActionBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                finish();
            }
        });
    }

    @OnClick({R.id.login, R.id.qq_login, R.id.weixin_login, R.id.tv_forget_password, R.id.register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                mPresenter.loginPwd();
                break;
            case R.id.qq_login:
                platfrom = SHARE_MEDIA.QQ;
                mPresenter.loginThirdResponse();
                break;
            case R.id.weixin_login:
                platfrom = SHARE_MEDIA.WEIXIN;
                mPresenter.loginThirdResponse();
                break;
            case R.id.tv_forget_password:
                startActivity(FindPasswordActivity.class, null);
                break;
            case R.id.register:
                startActivity(RegisterActivity.class,null);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void loginSuccess(LoginResponse data) {
        //登陆后更新视图
        EventBus.getDefault().post(new UpdateUserInfoEvent(true));
        finish();
    }

    @Override
    public void showLoginMessage(String message) {
        Toast.makeText(TTApplication.mActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMsg(String message) {
        ToastUtil.showToast(TTApplication.mActivity, message, 0);
    }

    @Override
    public String getAccountName() {
        return PhoneUtils.getText(account);
    }

    @Override
    public String getAccountPwd() {
        return PhoneUtils.getText(mPassword);
    }

    @Override
    public SHARE_MEDIA getPlatForm() {
        return platfrom;
    }

    /**
     * 隐藏键盘点击空白处
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtil.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.getUMShareAPI().onActivityResult(requestCode, resultCode, data);
    }
}
