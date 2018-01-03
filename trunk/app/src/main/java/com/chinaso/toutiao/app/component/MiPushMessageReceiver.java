package com.chinaso.toutiao.app.component;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MiPushMessageReceiver extends PushMessageReceiver {
    private final String PUSH_STATE_PROTO = "0";  //导航栏是原生系统
    private final String PUSH_STATE_WEB = "1";  //全部是网页,和新闻详情页一致。使用较多
    private final String PUSH_STATE_DETAIL = "2";  //和搜索页一致
    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        SharedPreferencesSetting.setIsMiPushNewMessage(false); //用于“我的通知”小红点显示
        mMessage = message.getContent();
        try {
            JSONObject jsonObject = new JSONObject(mMessage);
            String type = jsonObject.getString("t");
            String urlStr = jsonObject.getString("u");
            String titleBar = jsonObject.has("n") ? (jsonObject.getString("n")) : null;
            switch (type) {
                case PUSH_STATE_PROTO:
                    /*Intent intent00 = new Intent();
                    intent00.setClass(context, VerticalListWebViewActivity.class);
                    intent00.putExtra("pushUrl", urlStr);
                    intent00.putExtra("title", titleBar);
                    intent00.putExtra(Constants.MIPUSH, Constants.MIPUSH);
                    intent00.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent00);*/
                    break;
                case PUSH_STATE_WEB:
                    Intent intent01 = new Intent(context, VerticalDetailActivity.class);
                    intent01.putExtra("pushUrl", urlStr);
                    intent01.putExtra(Constants.MIPUSH, Constants.MIPUSH);
                    intent01.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent01);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        SharedPreferencesSetting.setIsMiPushNewMessage(true); //用于“我的通知”小红点显示

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                //System.out.println("toutiao-mRegId" + mRegId);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }
    }
}
