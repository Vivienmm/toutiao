package com.chinaso.toutiao.mvp.ui.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.entity.update.MyUpdateListener;
import com.chinaso.toutiao.app.entity.update.VersionUpdateResponse;
import com.chinaso.toutiao.mvp.listener.UpdateTextSizeEvent;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.ui.fragment.TextSizeDialog;
import com.chinaso.toutiao.util.ClearCacheManager;
import com.chinaso.toutiao.util.TimeControlUtil;
import com.chinaso.toutiao.util.VersionUpdate;
import com.chinaso.toutiao.view.CustomActionBar;
import com.xiaomi.mipush.sdk.MiPushClient;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.actionbar)
    CustomActionBar actionbar;
    @BindView(R.id.personalSetting)
    TextView personalSetting;
    @BindView(R.id.bindOtherAccount)
    TextView bindOtherAccount;
    @BindView(R.id.readingPreference)
    TextView readingPreference;
    @BindView(R.id.fontSetting)
    RelativeLayout fontSetting;
    @BindView(R.id.pushButton)
    Button pushButton;
    @BindView(R.id.showImgNowifiBtn)
    Button showImgNowifiBtn;
    @BindView(R.id.clearCache)
    TextView clearCache;
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.checkVersion)
    TextView checkVersion;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.appGrade)
    TextView appGrade;
    @BindView(R.id.msgSplash)
    TextView msgSplash;
    @BindView(R.id.aboutApp)
    TextView aboutApp;
    @BindView(R.id.textSizeTv)
    TextView textSizeTv;

    private boolean isPushOn;
    private boolean isShowImgNOWifi = true;
    private String textSize;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initViews() {
        initBar();
        showVersion();
        checkMyUpdate();

        tvCacheSize.setText(ClearCacheManager.getTotalCacheSize(this));

        initPush();

        initNoWifiShowImg();

        initTextSize();
    }

    //获取Event事件，更新UI
    public void onEventMainThread(UpdateTextSizeEvent event) {
        String size = event.getTextSize();
        textSizeTv.setText(size);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    private void initTextSize() {
        textSize = SharedPreferencesSetting.getIsFontSize();
        switch (textSize) {
            case "S":
                textSizeTv.setText("小号字");
                break;
            case "M":
                textSizeTv.setText("中号字");
                break;
            case "L":
                textSizeTv.setText("大号字");
                break;
            case "XL":
                textSizeTv.setText("特大号字");
                break;
            default:
                textSizeTv.setText("中号字");
                break;
        }

    }

    private void initBar() {
        actionbar.setTitleView("设置");
        actionbar.setLeftViewImg(R.mipmap.actionbar_back);
        actionbar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                finish();
            }
        });
    }

    private void showVersion() {
        PackageManager manager = getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
            String versionName = info.versionName;
            tvVersion.setText("V " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            tvVersion.setText(getResources().getString(R.string.tv_version_fail));
        }
    }

    private void checkMyUpdate() {
        VersionUpdate.getInstance(this).setUpdateListener(new MyUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, VersionUpdateResponse updateInfo) {
                TTApplication.setUpdateStatus(updateStatus);
//                if (updateStatus == Constants.VERSION_UPDATE_YES) {
//                    img_new_message_version.setVisibility(View.VISIBLE);
//                } else {
//                    img_new_message_version.setVisibility(View.INVISIBLE);
//                }
            }
        });
    }

    private void initPush() {
        isPushOn = SharedPreferencesSetting.getIsMiPushNewMessage();
        int pushRes = isPushOn ? R.mipmap.switch_on : R.mipmap.switch_off;
        SharedPreferencesSetting.setIsMiPushNewMessage(isPushOn);
        pushButton.setBackground(getResources().getDrawable(pushRes));
        if (isPushOn) {
            MiPushClient.resumePush(getApplicationContext(), null);
        }else{
            MiPushClient.pausePush(getApplicationContext(), null);
        }
    }

    private void initNoWifiShowImg() {
        int noWifiRes = SharedPreferencesSetting.getShowImgNOWifi() ? R.mipmap.switch_on : R.mipmap.switch_off;
        showImgNowifiBtn.setBackground(getResources().getDrawable(noWifiRes));
    }


    @OnClick({R.id.personalSetting, R.id.bindOtherAccount, R.id.readingPreference, R.id.fontSetting,
            R.id.pushButton, R.id.showImgNowifiBtn, R.id.clearCache, R.id.checkVersion,
            R.id.appGrade, R.id.msgSplash, R.id.aboutApp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personalSetting:
                if (TTApplication.isLogin) {
                    startActivity(PersonalSettingActivity.class, null);
                } else {
                    startActivity(LoginActivity.class, null);
                }
                break;
            case R.id.bindOtherAccount:
                break;
            case R.id.readingPreference:
                startActivity(ReadingPreferenceActivity.class, null);
                break;
            case R.id.fontSetting:

                TextSizeDialog dialogFragment = TextSizeDialog.newInstance(textSize);
                dialogFragment.show(getFragmentManager(), "dialog");
                break;
            case R.id.pushButton:
                pushMessage();
                break;
            case R.id.showImgNowifiBtn:
                showImgNowifi();
                break;
            case R.id.clearCache:
                ClearCacheManager.cleanInternalCache(this);
                tvCacheSize.setText(ClearCacheManager.getTotalCacheSize(this));
                Toast.makeText(this, getResources().getString(R.string.txt_clear_cache), Toast.LENGTH_SHORT).show();
                break;
            case R.id.checkVersion:
                //版本更新测试
                if (!TimeControlUtil.isFastClick()) {
                    VersionUpdate.checkVerUpdate(this, Constants.VERSION_UPDATE_SETTING_REQUEST);
                }
                break;
            case R.id.appGrade:
                break;
            case R.id.msgSplash:
                startActivity(new Intent(this, CoverActivity.class));
                break;
            case R.id.aboutApp:
                Intent aboutIntent = new Intent(this, AboutUsActivity.class);
                startActivity(aboutIntent);
                break;
            default:
                break;
        }
    }

    private void pushMessage() {
        if(isPushOn){
            MiPushClient.pausePush(getApplicationContext(), null);
        }else{
            MiPushClient.resumePush(getApplicationContext(), null);
        }
        int res = isPushOn ? R.mipmap.switch_off : R.mipmap.switch_on;
        SharedPreferencesSetting.setIsMiPushNewMessage(isPushOn);
        pushButton.setBackground(getResources().getDrawable(res));
        isPushOn = !isPushOn;
    }

    private void showImgNowifi() {
        int res = isShowImgNOWifi ? R.mipmap.switch_on : R.mipmap.switch_off;
        SharedPreferencesSetting.setShowImgNOWifi(isShowImgNOWifi);
        showImgNowifiBtn.setBackground(getResources().getDrawable(res));
        isShowImgNOWifi = !isShowImgNOWifi;
        TTApplication.isShowImgNoWifi = isShowImgNOWifi;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
