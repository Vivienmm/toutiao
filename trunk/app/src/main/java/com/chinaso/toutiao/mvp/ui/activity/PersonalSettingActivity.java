package com.chinaso.toutiao.mvp.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.entity.user.LoginResponse;
import com.chinaso.toutiao.mvp.entity.user.UserInfoManager;
import com.chinaso.toutiao.mvp.listener.UpdateDeviceIdEvent;
import com.chinaso.toutiao.mvp.listener.UpdateUserInfoEvent;
import com.chinaso.toutiao.mvp.presenter.impl.PersonalSettingPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.ui.fragment.FragmentDialog;
import com.chinaso.toutiao.mvp.view.PersonalSettingView;
import com.chinaso.toutiao.util.GlideCircleTransform;
import com.chinaso.toutiao.util.PermissionUtil;
import com.chinaso.toutiao.view.CustomActionBar;
import com.chinaso.toutiao.view.SelectPicPopupWindow;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PersonalSettingActivity extends BaseActivity implements PersonalSettingView{
    @BindView(R.id.personalSettingBar)
    CustomActionBar personalSettingBar;
    @BindView(R.id.headPortraitImg)
    ImageView headPortraitImg;
    @BindView(R.id.nickNameTv)
    TextView nickNameTv;
    @BindView(R.id.headPortraitLayout)
    LinearLayout headPortraitLayout;
    @BindView(R.id.namePortraitLayout)
    LinearLayout namePortraitLayout;
    @BindView(R.id.logoutBtn)
    Button logoutBtn;
    @BindView(R.id.deviceNameTV)
    TextView deviceNameTV;
    @BindView(R.id.modeImg)
    ImageView modeImg;
    @BindView(R.id.sexSelectedLayout)
    LinearLayout sexSelectedLayout;
    @BindView(R.id.sexSelectedTV)
    TextView sexSelectedTV;
    @BindView(R.id.birthDayTV)
    TextView birthDayTV;
    @BindView(R.id.birthDayLayout)
    LinearLayout birthDayLayout;
    @BindView(R.id.deviceNameLayout)
    LinearLayout deviceNameLayout;

    private SelectPicPopupWindow popupWindow;
    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    public static final float NORMAL_ALPHA = 1.0f;
    public static final int REQUEST_CODE_ALBUM = 2;
    public static final int REQUEST_CODE_CROUP_PHOTO = 3;
    public static final float CHANGE_ALPHA = 0.4f;


    private PersonalSettingPresenterImpl mPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_setting;
    }

    @Override
    public void initViews() {
        personalSettingBar.setTitleView("个人设置");
        personalSettingBar.setLeftViewImg(R.mipmap.actionbar_back);
        personalSettingBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                mPresenter.uploadUserInfo();
                PersonalSettingActivity.this.finish();
            }
        });

        mPresenter = new PersonalSettingPresenterImpl(this);
        mPresenter.attachView(this);
        mPresenter.onCreate();

        mPresenter.showDeviceName(SharedPreferencePrefUserInfo.getDeviceName());
        initPopuWindow();
        mPresenter.showNickName();
        mPresenter.showAvatar();

        EventBus.getDefault().register(this);

    }

    @Override
    public void showNickName(String name) {
        nickNameTv.setText(name);
    }

    @Override
    public void showAvatar() {
        LoginResponse loginfo = UserInfoManager.getInstance().getLoginResponse();
        if (!TextUtils.isEmpty(loginfo.getAvatar())) {
            Glide.with(TTApplication.getApp())
                    .load(UserInfoManager.getInstance().getLoginResponse().getAvatar())
                    .bitmapTransform(new GlideCircleTransform(TTApplication.getApp()))
                    .crossFade(1000)
                    .into(headPortraitImg);
        }
    }

    private void initPopuWindow() {
        popupWindow = new SelectPicPopupWindow(this, itemOnclicker);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setParams(NORMAL_ALPHA);
            }
        });
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public View.OnClickListener itemOnclicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            switch (v.getId()) {
                case R.id.register_set_camera:
                    if (PermissionUtil.hasCameraPermission(PersonalSettingActivity.this)) {
                        mPresenter.uploadAvatarFromPhotoRequest();
                    }
                    break;
                case R.id.register_take_photo:
                    mPresenter.uploadAvatarFromAlbumRequest();
                    break;
                case R.id.register_set_cancle:
                    popupWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public void onEventMainThread(UpdateDeviceIdEvent event) {
        mPresenter.showDeviceName(event.getdeviceId());
    }

    @OnClick({R.id.headPortraitLayout, R.id.namePortraitLayout, R.id.sexSelectedLayout, R.id.birthDayLayout, R.id.deviceNameLayout, R.id.modeImg, R.id.logoutBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headPortraitLayout:
                popupWindow.showAtLocation(logoutBtn, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
                setParams(CHANGE_ALPHA);
                break;
            case R.id.namePortraitLayout:
                mPresenter.showChangeNameDialog();
                break;
            case R.id.sexSelectedLayout:
                mPresenter.selectedSex();
                break;
            case R.id.birthDayLayout:
                mPresenter.setBirthDay();
                break;
            case R.id.deviceNameLayout:
                int deviceId = SharedPreferencePrefUserInfo.getDeviceName();
                FragmentDialog dialogFragment = FragmentDialog.newInstance(deviceId);
                dialogFragment.show(getFragmentManager(), "dialog");
                break;
            case R.id.modeImg:
                modeImg.setImageResource(R.mipmap.switch_off);
                break;
            case R.id.logoutBtn:
                UserInfoManager.getInstance().logOut();
                EventBus.getDefault().post(new UpdateUserInfoEvent(true));
                PersonalSettingActivity.this.finish();
                break;
            default:
                break;
        }
    }

    public void setParams(float f) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = f;
        params.dimAmount = f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_SHOWCAMERA) {
            mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showErrorMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getNickName() {
        return null;
    }

    @Override
    public void showDeviceName(String info) {
        deviceNameTV.setText(info);
    }

    @Override
    public void showSex(String info) {
        sexSelectedTV.setText(info);
    }

    @Override
    public void showBirthDay(String info) {
        birthDayTV.setText(info);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
