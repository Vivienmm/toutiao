package com.chinaso.toutiao.mvp.ui.activity;


import android.widget.ImageView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.view.CustomActionBar;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpInfoActivity extends BaseActivity {


    @BindView(R.id.actionbar)
    CustomActionBar mCustomBar;
    @BindView(R.id.helpImg)
    ImageView helpImg;


    @Override
    public int getLayoutId() {
        return R.layout.activity_help_info;
    }

    @Override
    public void initViews() {
        initBar();
    }

    @OnClick(R.id.helpImg)
    public void backAction() {
        HelpInfoActivity.this.finish();
    }

    private void initBar() {
        mCustomBar.setLeftViewImg(R.mipmap.actionbar_back);

        mCustomBar.setTitleView("使用帮助");

        mCustomBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                HelpInfoActivity.this.finish();
            }
        });
    }


}
