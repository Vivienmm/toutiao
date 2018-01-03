package com.chinaso.toutiao.mvp.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.mvp.data.collection.CollectionManageDao;
import com.chinaso.toutiao.mvp.data.readhistory.ReadHistoryManageDao;
import com.chinaso.toutiao.mvp.listener.UpdateUserInfoEvent;
import com.chinaso.toutiao.mvp.ui.activity.CollectionActivity;
import com.chinaso.toutiao.mvp.ui.activity.DetailCommentActivity;
import com.chinaso.toutiao.mvp.ui.activity.FeedBackActivity;
import com.chinaso.toutiao.mvp.ui.activity.HelpInfoActivity;
import com.chinaso.toutiao.mvp.ui.activity.MainActivity;
import com.chinaso.toutiao.mvp.ui.activity.MessageActivity;
import com.chinaso.toutiao.mvp.ui.activity.ReadHistoryActivity;
import com.chinaso.toutiao.mvp.ui.fragment.base.BaseFragment;
import com.chinaso.toutiao.util.SignCodeUtil;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.util.VersionUpdate;
import com.chinaso.toutiao.util.secure.MD5Util;
import com.chinaso.toutiao.view.MyFragmentHeaderLogin;

import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.chinaso.toutiao.app.TTApplication.isNightMode;

public class MyFragment extends BaseFragment {
    @BindView(R.id.headerLogin)
    public MyFragmentHeaderLogin headerLogin;
    @BindView(R.id.subscribeTV)
    TextView subscribeTV;
    @BindView(R.id.collectTV)
    TextView collectTV;
    @BindView(R.id.followUpTV)
    TextView followUpTV;
    @BindView(R.id.readHistoryTV)
    TextView readHistoryTV;
    @BindView(R.id.subscribeImg)
    ImageView subscribeImg;
    @BindView(R.id.collectImg)
    ImageView collectImg;
    @BindView(R.id.followUpImg)
    ImageView followUpImg;
    @BindView(R.id.readhistoryImg)
    ImageView readhistoryImg;
    @BindView(R.id.subscribeLayout)
    LinearLayout subscribeLayout;
    @BindView(R.id.collectLayout)
    LinearLayout collectLayout;
    @BindView(R.id.followUpLayout)
    LinearLayout followUpLayout;
    @BindView(R.id.readhistoryLayout)
    LinearLayout readhistoryLayout;

    @BindView(R.id.layout_comment)
    RelativeLayout layoutComment;
    @BindView(R.id.layout_message)
    RelativeLayout layoutMessage;
    @BindView(R.id.layoutOfflineRead)
    RelativeLayout layoutOfflineRead;

    @BindView(R.id.modeButton)
    Button modeButton;
    @BindView(R.id.layout_help)
    RelativeLayout layoutHelp;
    @BindView(R.id.layout_feedback)
    RelativeLayout layoutFeedback;
    @BindView(R.id.img_message_redDot)
    ImageView img_message_redDot;

    private Activity context;
    private ReadHistoryManageDao readHistoryManageDao;
    private CollectionManageDao collectionManageDao;
    private boolean isNewPush; //显示有推送状态

    @Override
    public int initLayout() {
        return R.layout.fragment_my_new;
    }

    @Override
    public void initViews(View view) {
        context = getActivity();
        EventBus.getDefault().register(this);

        if (TTApplication.isLogin) {
            updateLoginUI();
            headerLogin.updateHeaderLoginUI();
        } else {
            updateLogOutUI();
            headerLogin.updateHeaderLogoutUI();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        headerLogin.setLoginListener(new MyFragmentHeaderLogin.LoginListener() {
            @Override
            public void successLogin(boolean flag) {
                if (flag) {
                    updateLoginUI();
                } else {
                    updateLogOutUI();
                }
            }
        });
        isNewPush = SharedPreferencesSetting.getIsPUSHOPEN();
        TTApplication.isNightMode = SharedPreferencesSetting.getIsNightMode();
        showNewMessageDot();
        switchNightMode();
    }

    /**
     * 设置未读过新消息，小红点提示和new小图片
     */
    private void showNewMessageDot() {
        //判断是否有新推送的消息，点击“我的通知”后设置为false
        if (isNewPush) {
            img_message_redDot.setVisibility(View.VISIBLE);
        } else {
            img_message_redDot.setVisibility(View.GONE);
        }
    }

    //获取Event事件，更新UI
    public void onEventMainThread(UpdateUserInfoEvent event) {
        if (TTApplication.isLogin()) {
            headerLogin.updateHeaderLoginUI();
            updateLoginUI();
        } else {
            headerLogin.updateHeaderLogoutUI();
            updateLogOutUI();
        }
    }

    private void updateLoginUI() {
        subscribeTV.setVisibility(View.VISIBLE);
        collectTV.setVisibility(View.VISIBLE);
        followUpTV.setVisibility(View.VISIBLE);
        readHistoryTV.setVisibility(View.VISIBLE);
        subscribeTV.setText("0");
        followUpTV.setText("0");
        collectionManageDao = new CollectionManageDao();
        collectTV.setText(collectionManageDao.getAllCollections().size()+"");
        readHistoryManageDao = new ReadHistoryManageDao();
        readHistoryTV.setText(readHistoryManageDao.getAllHistorys().size() + "");
        subscribeImg.setVisibility(View.GONE);
        collectImg.setVisibility(View.GONE);
        followUpImg.setVisibility(View.GONE);
        readhistoryImg.setVisibility(View.GONE);
    }

    private void updateLogOutUI() {
        subscribeTV.setVisibility(View.GONE);
        collectTV.setVisibility(View.GONE);
        followUpTV.setVisibility(View.GONE);
        readHistoryTV.setVisibility(View.GONE);
        subscribeImg.setVisibility(View.VISIBLE);
        collectImg.setVisibility(View.VISIBLE);
        followUpImg.setVisibility(View.VISIBLE);
        readhistoryImg.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.subscribeLayout, R.id.collectLayout, R.id.followUpLayout, R.id.readhistoryLayout,
            R.id.layout_comment, R.id.layout_message,
            R.id.layoutOfflineRead, R.id.modeButton, R.id.layout_help, R.id.layout_feedback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.subscribeLayout:
                Toast.makeText(context, "订阅即将上线，敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.collectLayout:
                getActivity().startActivity(new Intent(getActivity(), CollectionActivity.class));
                break;
            case R.id.followUpLayout:
                Toast.makeText(context, "跟帖即将上线，敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.readhistoryLayout:
                getActivity().startActivity(new Intent(getActivity(), ReadHistoryActivity.class));
                break;
            case R.id.layout_comment:
                goToCommentAcitivity();
                break;
            case R.id.layout_message:
                isNewPush = false;
                showNewMessageDot();
                getActivity().startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.layoutOfflineRead:
                Toast.makeText(context, "订阅即将上线，敬请期待", Toast.LENGTH_SHORT).show();
//                getActivity().startActivity(new Intent(getActivity(), OfflineReadActivity.class));
                break;
            case R.id.modeButton:
                isNightMode = !isNightMode;
                switchNightMode();
                break;
            case R.id.layout_help:
                getActivity().startActivity(new Intent(getActivity(), HelpInfoActivity.class));
                break;
            case R.id.layout_feedback:
                getActivity().startActivity(new Intent(getActivity(), FeedBackActivity.class));
                break;
            default:
                break;
        }
    }

    private void goToCommentAcitivity() {
        if (TTApplication.isLogin) {
            Long id = SharedPreferencePrefUserInfo.getUserId();
            String userId = String.valueOf(id);
            Map<String, String> map = new TreeMap<>();
            map.put("uid", "uid" + userId);
            String sign = MD5Util.md5(SignCodeUtil.getAsceCode(map));
            String url = Constants.CHINASO_BASE + "/1/comment/querycomment_uid?uid=" + id + "&sign=" + sign;
            Intent intent = new Intent(context, DetailCommentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("commentUrl", url);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        } else {
            ToastUtil.showToast(context, "查看评论,请先登录", 0);
        }
    }

    public void switchNightMode() {
        int resId = isNightMode ? R.mipmap.switch_on : R.mipmap.switch_off;
        modeButton.setBackground(getResources().getDrawable(resId));
        ((MainActivity) context).setNightMode(isNightMode);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferencesSetting.setIsPUSHOPEN(isNewPush);
        SharedPreferencesSetting.setIsNightMode(isNightMode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VersionUpdate.unRegisterUpdate();
        EventBus.getDefault().unregister(this);
        SharedPreferencesSetting.setIsNightMode(isNightMode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        headerLogin.getUmShareAPI().onActivityResult(requestCode, resultCode, data);
    }
}
