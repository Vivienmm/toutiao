package com.chinaso.toutiao.mvp.ui.activity;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.view.CustomActionBar;

import butterknife.BindView;

public class OfflineReadActivity extends BaseActivity {

    @BindView(R.id.offlineReadBar)
    CustomActionBar offlineReadBar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_offline_read;
    }

    @Override
    public void initViews() {
        offlineReadBar.setLeftViewImg(R.mipmap.actionbar_back);
        offlineReadBar.setTitleView("离线阅读");
        offlineReadBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                OfflineReadActivity.this.finish();
            }
        });
    }

}
