package com.chinaso.toutiao.mvp.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {
    private View mView;

    public abstract int initLayout();

    public abstract void initViews(View view);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView=  LayoutInflater.from(getActivity()).inflate(initLayout(), container, false);
            ButterKnife.bind(this, mView);
            initViews(mView);
        }
        return mView;
    }
}
