package com.chinaso.toutiao.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.mvp.listener.UpdateUserInfoEvent;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.ui.fragment.HomeFragment;
import com.chinaso.toutiao.mvp.ui.fragment.MyFragment;
import com.chinaso.toutiao.mvp.ui.fragment.SubscribeFragment;
import com.chinaso.toutiao.mvp.ui.fragment.VideoFragment;
import com.chinaso.toutiao.util.PermissionUtil;
import com.chinaso.toutiao.util.VersionUpdate;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import thinkfreely.changemodelibrary.ChangeModeController;

import static com.chinaso.toutiao.R.id.home_text;
import static com.chinaso.toutiao.R.id.my_text;
import static com.chinaso.toutiao.R.id.theme_text;
import static com.chinaso.toutiao.R.id.video_text;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(home_text)
    TextView homeText;
    @BindView(video_text)
    TextView videoText;
    @BindView(theme_text)
    TextView themeText;
    @BindView(my_text)
    TextView myText;
    @BindView(R.id.nightmode)
    View nightmode;

    Resources rs;
    int menuTextBgColor, menuTextBgCurrentColor;
    Fragment homeFragment, videoFragment, themeFragment, myFragment;
    Fragment mCurrentFragment;
    FragmentManager fm;
    Bundle mSavedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //1. 在要立即切换效果的页面调用此方法
        ChangeModeController.getInstance().init(this, R.attr.class).setTheme(this, R.style.DayTheme, R.style.NightTheme);
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        homeText = (TextView) findViewById(home_text);
        homeText.setOnClickListener(this);
        videoText = (TextView) findViewById(video_text);
        videoText.setOnClickListener(this);
        themeText = (TextView) findViewById(theme_text);
        themeText.setOnClickListener(this);
        myText = (TextView) findViewById(my_text);
        myText.setOnClickListener(this);
        nightmode = this.findViewById(R.id.nightmode);

        initResources();
        initFragment(mSavedInstanceState);
        //版本更新测试
        VersionUpdate.checkVerUpdate(this, Constants.VERSION_UPDATE_INIT_REQUEST);
    }

    private void initResources() {
        rs = getResources();
        menuTextBgColor = rs.getColor(R.color.menu_text_bg);
        menuTextBgCurrentColor = rs.getColor(R.color.menu_text_bg_current);
    }

    public void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            videoFragment = new VideoFragment();
//            themeFragment = new ThemeFragment();
            themeFragment = new SubscribeFragment();
            myFragment = new MyFragment();
            mCurrentFragment = homeFragment;
            fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.content, homeFragment, "homeFragment")
                    .add(R.id.content, videoFragment, "videoFragment").hide(videoFragment)
                    .add(R.id.content, themeFragment, "themeFragment").hide(themeFragment)
                    .add(R.id.content, myFragment, "myFragment").hide(myFragment)
                    .commitAllowingStateLoss();
        } else {
            homeFragment = getSupportFragmentManager().findFragmentByTag("homeFragment");
            videoFragment = getSupportFragmentManager().findFragmentByTag("videoFragment");
            themeFragment = getSupportFragmentManager().findFragmentByTag("themeFragment");
            myFragment = getSupportFragmentManager().findFragmentByTag("myFragment");

            //show()一个即可
            fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .show(homeFragment)
                    .hide(videoFragment)
                    .hide(myFragment)
                    .hide(themeFragment)
                    .commit();
        }

    }

    //获取Event事件，更新UI
    public void onEventMainThread(UpdateUserInfoEvent event) {
        boolean flag = event.getFlag();
        if (myFragment != null && myFragment.isAdded()) {
            ((MyFragment) myFragment).headerLogin.updateHeaderLoginUI();
        }

    }

    @SuppressLint("NewApi")
    private void clearSelection() {
        homeText.setTextColor(menuTextBgColor);
        Drawable drawableHome = rs.getDrawable(R.mipmap.menu_home);
        homeText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableHome, null, null);

        videoText.setTextColor(menuTextBgColor);
        Drawable drawableVideo = rs.getDrawable(R.mipmap.menu_video);
        videoText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableVideo, null, null);

        myText.setTextColor(menuTextBgColor);
        Drawable drawableMy = rs.getDrawable(R.mipmap.menu_my);
        myText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableMy, null, null);

        themeText.setTextColor(menuTextBgColor);
        Drawable drawableTheme = rs.getDrawable(R.mipmap.menu_theme);
        themeText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableTheme, null, null);

    }

    public void switchFragment(String toTag) {
        Fragment to = fm.findFragmentByTag(toTag);
        if (mCurrentFragment != to) {
            fm.beginTransaction().hide(mCurrentFragment).show(to).commit();
            mCurrentFragment = to;
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        clearSelection();
        switch (v.getId()) {
            case home_text:
                homeText.setTextColor(menuTextBgCurrentColor);
                Drawable drawableHome = rs.getDrawable(R.mipmap.menu_home_current);
                homeText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableHome, null, null);
                switchFragment("homeFragment");
                break;
            case video_text:
                videoText.setTextColor(menuTextBgCurrentColor);
                Drawable drawableTop = rs.getDrawable(R.mipmap.menu_video_current);
                videoText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableTop, null, null);
                switchFragment("videoFragment");
                break;
            case theme_text:
                themeText.setTextColor(menuTextBgCurrentColor);
                Drawable drawableTheme = rs.getDrawable(R.mipmap.menu_theme_current);
                themeText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableTheme, null, null);
                switchFragment("themeFragment");
                break;
            case my_text:
                myText.setTextColor(menuTextBgCurrentColor);
                Drawable drawableSetting = rs.getDrawable(R.mipmap.menu_my_current);
                myText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableSetting, null, null);
                switchFragment("myFragment");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        VersionUpdate.unRegisterUpdate();
        ChangeModeController.onDestory();
    }

    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出头条", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length <= 0) {
                    return;
                }
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initFragment(mSavedInstanceState);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = fm.findFragmentByTag("myFragment");
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    public void setNightMode(boolean isNightMode) {
        if (isNightMode) {
            nightmode.setVisibility(View.GONE);
            ChangeModeController.changeNight(this, R.style.NightTheme);//切换夜间模式
            ((VideoFragment) videoFragment).initIndicator(true);
            ((HomeFragment) homeFragment).initIndicator(true);
        } else {
            nightmode.setVisibility(View.GONE);
            ChangeModeController.changeDay(this, R.style.DayTheme);//切换日间模式
            ((VideoFragment) videoFragment).initIndicator(false);
            ((HomeFragment) homeFragment).initIndicator(false);
        }
    }
}
