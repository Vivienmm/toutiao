package com.chinaso.toutiao.mvp.ui.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.TTApplication;

import butterknife.ButterKnife;

public abstract class BaseActivity extends FragmentActivity {
    public abstract int getLayoutId();

    public abstract void initViews();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        TTApplication.registerActivity(this);
        initViews();
    }

    public void startActivityForResult(Class<?> aTargetClass, Bundle aBundle) {
        Intent i = new Intent(this, aTargetClass);
        if (aBundle != null) {
            i.putExtras(aBundle);
        }
        startActivity(i);
    }
    public void startActivity(Class<?> aTargetClass, Bundle aBundle) {
        Intent i = new Intent(this, aTargetClass);
        if (aBundle != null) {
            i.putExtras(aBundle);
        }
        startActivity(i);
    }

    public void startActivityForResult(Class<?> aTargetClass, Bundle aBundle, int aRequestCode) {
        Intent i = new Intent(this, aTargetClass);
        if (aBundle != null) {
            i.putExtras(aBundle);
        }
        startActivityForResult(i, aRequestCode);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateNightMode();
//        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

    public void updateNightMode() {

        View view = this.findViewById(R.id.nightmode);
        if (null == view)
            return;

        if (TTApplication.isNightMode) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

}
