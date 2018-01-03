package com.chinaso.toutiao.mvp.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.entity.PreferCustomizableChannel;
import com.chinaso.toutiao.mvp.listener.OnRecyclerItemClickListener;
import com.chinaso.toutiao.mvp.presenter.ReadPreferChannelPresenter;
import com.chinaso.toutiao.mvp.presenter.impl.ReadPreferencePresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.ui.adapter.PreferChannelAdapter;
import com.chinaso.toutiao.mvp.view.ReadPreferChannelView;
import com.chinaso.toutiao.view.CustomActionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ReadingPreferenceActivity extends BaseActivity implements ReadPreferChannelView {

    @BindView(R.id.readingPreferenceBar)
    CustomActionBar readingPreferenceBar;
    @BindView(R.id.sexBoyImg)
    ImageView sexBoyImg;
    @BindView(R.id.sexBoyTV)
    TextView sexBoyTV;
    @BindView(R.id.sexGirlImg)
    ImageView sexGirlImg;
    @BindView(R.id.sexGileTV)
    TextView sexGileTV;
    @BindView(R.id.preferChannelRV)
    RecyclerView preferChannelRV;
    @BindView(R.id.preferConfirmBtn)
    Button preferConfirmBtn;
    @BindView(R.id.activity_reading_preference)
    RelativeLayout activityReadingPreference;

    private PreferChannelAdapter adapter = null;
    private ReadPreferChannelPresenter preferPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_reading_preference;
    }

    @Override
    public void initViews() {
        initBar();

        initRecyclerView();

        preferPresenter = new ReadPreferencePresenterImpl();
        preferPresenter.attachView(this);
        preferPresenter.onCreate();
        preferPresenter.initData();
    }

    private void initBar() {
        readingPreferenceBar.setLeftViewImg(R.mipmap.actionbar_back);
        readingPreferenceBar.setTitleView("阅读偏好");
        readingPreferenceBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                ReadingPreferenceActivity.this.finish();
            }
        });
    }

    private void initRecyclerView() {
        adapter = new PreferChannelAdapter();
        preferChannelRV.setLayoutManager(new GridLayoutManager(this, 3));
        preferChannelRV.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onRecyclerClick(int position) {
                preferPresenter.selectedChannelItem(adapter.getSelectedChannels());
            }
        });
    }

    @OnClick({R.id.sexBoyImg, R.id.sexBoyTV, R.id.sexGirlImg, R.id.sexGileTV, R.id.preferConfirmBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sexBoyImg:
            case R.id.sexBoyTV:
                preferPresenter.selectedBoy();
                break;
            case R.id.sexGirlImg:
            case R.id.sexGileTV:
                preferPresenter.selectedGirl();
                break;
            case R.id.preferConfirmBtn:
                preferPresenter.confirm2Server();
                break;
            default:
                break;
        }
    }

    @Override
    public void initView(List<PreferCustomizableChannel> lists) {
        adapter.setLists(lists);
    }

    @Override
    public void selectBoyState() {
        sexBoyImg.setImageResource(R.mipmap.boy_selected);
        sexGirlImg.setImageResource(R.mipmap.girl);
        sexBoyTV.setTextColor(getResources().getColor(R.color.colorAccent));
        sexGileTV.setTextColor(getResources().getColor(R.color.black));
        selectState();
    }

    @Override
    public void selectGirlState() {
        sexBoyImg.setImageResource(R.mipmap.boy);
        sexGirlImg.setImageResource(R.mipmap.girl_selected);
        sexGileTV.setTextColor(getResources().getColor(R.color.colorAccent));
        sexBoyTV.setTextColor(getResources().getColor(R.color.black));
        selectState();
    }

    private void selectState() {
        int len = adapter.getLists().size();
        for (int i = 0; i < len; i++) {
            if (adapter.mList.get(i).isSelected()) {
                adapter.mList.get(i).setSelected(false);
            }
        }
        preferPresenter.selectedChannelItem(null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void confirmState(boolean state) {
        preferConfirmBtn.setEnabled(state);
        if (state) {
            preferConfirmBtn.setTextColor(getResources().getColor(R.color.white));
            preferConfirmBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            preferConfirmBtn.setTextColor(getResources().getColor(R.color.gray));
            preferConfirmBtn.setBackgroundColor(getResources().getColor(R.color.bg_btn_cancel_update));
        }
    }

    @Override
    public void showSuccessSelected() {
        Toast.makeText(this, "设置成功，将为您定制专属头条", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showErrorMsg(String message) {

    }
}
