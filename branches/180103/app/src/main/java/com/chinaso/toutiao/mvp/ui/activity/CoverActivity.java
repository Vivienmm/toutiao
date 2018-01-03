package com.chinaso.toutiao.mvp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.entity.StartUpEntity;
import com.chinaso.toutiao.mvp.presenter.impl.CoverPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.view.CoverView;

import butterknife.BindView;

public class CoverActivity extends BaseActivity implements CoverView {

    @BindView(R.id.splashAdBkgImg)
    ImageView splashAdBkgImg;
    @BindView(R.id.splashAdImg)
    ImageView splashAdImg;
    @BindView(R.id.newsTitleTV)
    TextView newsTitleTV;
    @BindView(R.id.splashNewsLayout)
    RelativeLayout splashNewsLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cover;
    }

    @Override
    public void initViews() {
        CoverPresenterImpl coverPresenter = new CoverPresenterImpl(this);
        coverPresenter.attachView(this);
        coverPresenter.onCreate();
    }

    @Override
    public void showNews(final StartUpEntity entity) {
        splashNewsLayout.setVisibility(View.VISIBLE);
        newsTitleTV.setText(entity.getTitle());
        Glide.with(this).load(entity.getImgUrl()).into(splashAdImg);
        splashAdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(entity.getLinkUrl())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("newsUrl", entity.getLinkUrl());
                    startActivity(VerticalDetailActivity.class, bundle);
                }
            }
        });
    }

    @Override
    public void showAdImg(StartUpEntity entity) {
        splashAdBkgImg.setVisibility(View.VISIBLE);
        Glide.with(this).load(entity.getImgUrl()).into(splashAdImg);
    }

    @Override
    public void showErrorMsg(String message) {

    }
}
