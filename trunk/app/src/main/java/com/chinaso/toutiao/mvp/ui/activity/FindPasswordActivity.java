package com.chinaso.toutiao.mvp.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.presenter.RegisterPresenter;
import com.chinaso.toutiao.mvp.presenter.impl.RegisterPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.view.RegisterView;
import com.chinaso.toutiao.util.DebugUtil;
import com.chinaso.toutiao.util.DisplayUtil;
import com.chinaso.toutiao.util.PhoneUtils;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.util.WeakReferenceHandler;
import com.chinaso.toutiao.view.CustomActionBar;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class FindPasswordActivity extends BaseActivity implements RegisterView {
    @BindView(R.id.register_number)
    EditText mEditPhone;
    @BindView(R.id.register_sms_code)
    EditText mSmsCode;
    @BindView(R.id.register_pwd)
    EditText mSetPwd;
    @BindView(R.id.register_confirm_pwd)
    EditText mConfirmPwd;
    @BindView(R.id.register_server)
    TextView register_server;
    @BindView(R.id.register)
    Button mRegisterBtn;
    @BindView(R.id.register_login)
    TextView register_login;
    @BindView(R.id.tv_get_password)
    TextView mClickSmsPwd;
    @BindView(R.id.actionbar)
    CustomActionBar mActionBar;
    @BindView(R.id.tv_register_pwd)
    TextView tv_register_pwd;
    @BindView(R.id.rl_server)
    RelativeLayout rl_server;
    private Handler mHandler;
    private int countTime;

    private RegisterPresenter mPresenter = new RegisterPresenterImpl();

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initViews() {
        initBar();
        mPresenter.attachView(this);
        mHandler = new FindPWDHandler(this);
        tv_register_pwd.setText("重置密码");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(DisplayUtil.Dp2Px(this, 110), 0, 0, 0);
        mSetPwd.setLayoutParams(params);
        mRegisterBtn.setText("保存");
        rl_server.setVisibility(View.GONE);
    }

    private void initBar() {
        mActionBar.setTitleView("设置密码");
        mActionBar.setLeftViewImg(R.mipmap.actionbar_back);
        mActionBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                finish();
            }
        });
    }

    @OnTextChanged({R.id.register_number, R.id.register_sms_code,
            R.id.register_pwd, R.id.register_confirm_pwd})
    void afterTextChanged(Editable s) {
        if (PhoneUtils.notEmptyText(mEditPhone) &&
                PhoneUtils.notEmptyText(mSmsCode) &&
                PhoneUtils.notEmptyText(mSetPwd) &&
                PhoneUtils.notEmptyText(mConfirmPwd)) {
            mRegisterBtn.setEnabled(true);
            mClickSmsPwd.setClickable(true);
        } else {
            mRegisterBtn.setEnabled(false);
            mHandler.removeCallbacks(runnable);
        }

        if (PhoneUtils.getTextLength(mConfirmPwd) > PhoneUtils.getTextLength(mSetPwd) - 1) {
            if (!PhoneUtils.match(PhoneUtils.getText(mSetPwd), PhoneUtils.getText(mConfirmPwd))) {
                ToastUtil.showToast(FindPasswordActivity.this, getString(R.string.confirm_pwd), 0);
            }
        }
    }

    @OnClick({R.id.register, R.id.tv_get_password, R.id.register_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                mPresenter.modifyPWD();
                break;
            case R.id.tv_get_password:
                countTime = 60;
                mPresenter.getSmsCode();
                break;
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtil.showToast(this, message, 0);
    }

    @Override
    public void ResponseSuccessCode(LoginResponse data) {
        if (data.getCode() != null) {
            //验证码的返回处理
            if (runnable != null) {
                mHandler.removeCallbacks(runnable);
            }
            mHandler.postDelayed(runnable, 1000);
            DebugUtil.e("", "findPWDSmsCodeResponse" + data.getMessage() + data.getCode());
            ToastUtil.showToast(this, getString(R.string.sendsmscode), 0);
        } else {
            //做修改密码返回的处理
            ToastUtil.showToast(this, "修改成功", 0);
            SharedPreferencePrefUserInfo.setIsSavePassword(false);
            finish();
        }
    }

    @Override
    public void showAnimationError() {
        TranslateAnimation ta = new TranslateAnimation(2, 20, 2, 2);
        mEditPhone.startAnimation(ta);
        ta.setDuration(1000);
        ta.setInterpolator(new CycleInterpolator(5));
    }

    /**
     * 获取注册信息
     *
     * @return
     */
    @Override
    public String getRegisterPhoneNumber() {
        return PhoneUtils.getText(mEditPhone);
    }

    @Override
    public String getRegisterCode() {
        return PhoneUtils.getText(mSmsCode);
    }

    @Override
    public String getRegisterSetPwd() {
        return PhoneUtils.getText(mSetPwd);
    }

    @Override
    public String getSmsCodeType() {
        return "0";
    }

    @Override
    public String getRegisterConfirmPwd() {
        return PhoneUtils.getText(mConfirmPwd);
    }

    @Override
    public void showErrorMsg(String message) {
        ToastUtil.showToast(this, message, 0);
    }


    private class FindPWDHandler extends WeakReferenceHandler<FindPasswordActivity> {

        public FindPWDHandler(FindPasswordActivity reference) {
            super(reference);
        }

        @Override
        protected void handleMessage(FindPasswordActivity reference, Message msg) {

        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mClickSmsPwd.setText(countTime-- + getString(R.string.time_text));
            if (countTime > 0) {
                mHandler.postDelayed(this, 1000);
            } else {
                mClickSmsPwd.setText(getString(R.string.register_get_pwd));
                mClickSmsPwd.setClickable(true);
            }
        }
    };

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
}
