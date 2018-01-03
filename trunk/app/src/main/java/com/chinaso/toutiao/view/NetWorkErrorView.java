package com.chinaso.toutiao.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaso.toutiao.R;

public class NetWorkErrorView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private Button mReload;
    private TextView mSetNet;

    public NetWorkErrorView(Context context) {
        super(context);
        initView();

    }
    public NetWorkErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NetWorkErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.mContext = getContext();
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.view_net_error, this);
        mReload = (Button) view.findViewById(R.id.reLoad_bt);
        mSetNet = (TextView) view.findViewById(R.id.setWeb);
        mSetNet.setOnClickListener(this);
    }

    public void setOnClickListener(final ReloadInterface reloadInterface) {
        mReload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadInterface.reloadWebview();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setWeb) {
            Intent setintent = null;
            //判断手机系统的版本  即API大于10 就是3.0或以上版本
            if (android.os.Build.VERSION.SDK_INT > 10) {
                setintent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            } else {
                setintent = new Intent();
                ComponentName component = new ComponentName("com.android.settings", "com.android.settings.settings");
                setintent.setComponent(component);
                setintent.setAction("android.intent.action.VIEW");
            }
            mContext.startActivity(setintent);
        }
    }

    public interface ReloadInterface {
        void reloadWebview();
    }

}
