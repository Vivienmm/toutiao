package com.chinaso.toutiao.mvp.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.entity.StartUpEntity;
import com.chinaso.toutiao.mvp.presenter.impl.SplashPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.view.SplashView;
import com.chinaso.toutiao.util.DisplayUtil;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import butterknife.BindView;

/**
 * 和手机国搜保持一致，需要修改
 */
public class SplashActivity extends BaseActivity implements SplashView {

    @BindView(R.id.activity_splash)
    FrameLayout activity_splash;
    @BindView(R.id.splashStartImg)
    ImageView splashStartImg;
    @BindView(R.id.splashAdBkgImg)
    ImageView adBkgImageView;
    @BindView(R.id.txtTitle)
    TextView adTitleTextView;
    @BindView(R.id.splashNewsLayout)
    ViewGroup picAdBkgViewGroup;
    @BindView(R.id.splashAdImg)
    ImageView adImageView;

    private SplashPresenterImpl mSplashPresenter;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final int SHOW_AD = 1002;
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private final int TRANSITION_TIME = 500;
    private final int START_IMG_OFFSET = 1000;
    private final int AD_IMG_OFFSET = 1500;
    private SplashHandler splashHandler;

    static class SplashHandler extends Handler {
        WeakReference<SplashActivity> softReference;
        SplashHandler(SplashActivity activity){
            this.softReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            SplashActivity splashActivity = softReference.get();
            if (splashActivity != null) {
                switch (msg.what) {
                    case GO_HOME:
                        splashActivity.startAcitivity(MainActivity.class);
                        break;
                    case GO_GUIDE:
                        splashActivity.startAcitivity(GuideViewActivity.class);
                        break;
                    case SHOW_AD:
                        splashActivity.picAdBkgViewGroup.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        }
    }

    private void startAcitivity(Class<?> otherAct) {
        Intent intent = new Intent(SplashActivity.this, otherAct);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initViews() {
        splashHandler = new SplashHandler(this);

        mSplashPresenter = new SplashPresenterImpl(this);
        mSplashPresenter.attachView(this);
        mSplashPresenter.onCreate();
        mSplashPresenter.initSplashView();

    }

    @Override
    public void initSplashView() {
        AlphaAnimation splashAnim = new AlphaAnimation(1,0);
        splashAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                splashStartImg.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
        splashAnim.setDuration(TRANSITION_TIME);
        splashAnim.setStartOffset(START_IMG_OFFSET);
        splashAnim.setFillAfter(true);
        splashStartImg.startAnimation(splashAnim);

        AlphaAnimation splashAdAnim = new AlphaAnimation(0, 1);
        splashAdAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                splashHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                adBkgImageView.setVisibility(View.VISIBLE);
                adImageView.setVisibility(View.VISIBLE);
            }
        });
        splashAdAnim.setDuration(TRANSITION_TIME);
        splashAdAnim.setStartOffset(AD_IMG_OFFSET);
        splashAdAnim.setFillAfter(true);
        adImageView.startAnimation(splashAdAnim);
        adBkgImageView.startAnimation(splashAdAnim);
    }

    @Override
    public void showSplashBkg(final StartUpEntity entity) {
        if (entity == null || TextUtils.isEmpty(entity.getImgUrl())) {
            return;
        }
        if (!TextUtils.isEmpty(entity.getTitle()) && (entity.getType() == 1)) {
            splashHandler.sendEmptyMessageDelayed(SHOW_AD, START_IMG_OFFSET);
            adTitleTextView.setText(entity.getTitle());
        } else {
            picAdBkgViewGroup.setVisibility(View.GONE);
        }

        //广告图
        Picasso.with(SplashActivity.this)
                .load(entity.getImgUrl())
                .resize(DisplayUtil.getScreenW(SplashActivity.this), DisplayUtil.getScreenH(SplashActivity.this))
                .centerCrop()
                .into(adImageView);

        //添加链接
        if (!TextUtils.isEmpty(entity.getLinkUrl())) {
            adImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    splashHandler.removeCallbacksAndMessages(null);
//                        UserOperationStatistics.getInstance(getApplicationContext()).statistic(Constants.START_PAGET_EXTRA, page.getLinkUrl(), Constants.START_PAGET_EXTRA);
                    Intent[] intents = new Intent[2];
                    intents[0] = new Intent(SplashActivity.this, MainActivity.class);
                    if (1 == entity.getType()) {
                        intents[1] = new Intent(SplashActivity.this, VerticalDetailActivity.class);
                        intents[1].putExtra("newsUrl", entity.getLinkUrl());
                    }
                    SplashActivity.this.startActivities(intents);
                    SplashActivity.this.finish();
                }
            });
        }
    }

    @Override
    public void showErrorMsg(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
