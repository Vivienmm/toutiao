package com.chinaso.toutiao.mvp.ui.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.mvp.listener.UpdateDeviceIdEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class FragmentDialog extends DialogFragment {


    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.brandNameRadio)
    RadioButton brandNameRadio;
    @BindView(R.id.manufactureNameRadio)
    RadioButton manufactureNameRadio;
    @BindView(R.id.androidNameRadio)
    RadioButton androidNameRadio;
    @BindView(R.id.noneShowRadio)
    RadioButton noneShowRadio;
    @BindView(R.id.rgDevices)
    RadioGroup rgDevices;
    @BindView(R.id.confirmTv)
    TextView confirmTv;
    @BindView(R.id.cancelTv)
    TextView cancelTv;
    private int mParam1;
    private OnFragmentInteractionListener mListener;

    public FragmentDialog() {
        // Required empty public constructor
    }


    public static FragmentDialog newInstance(int param1) {
        FragmentDialog fragment = new FragmentDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        ButterKnife.bind(this, view);
        brandNameRadio.setText(android.os.Build.BRAND + " " + android.os.Build.MODEL);
        manufactureNameRadio.setText(android.os.Build.MANUFACTURER + "手机");
        switch (mParam1) {
            case 0:
                brandNameRadio.setChecked(true);
                break;
            case 1:
                manufactureNameRadio.setChecked(true);
                break;
            case 2:
                androidNameRadio.setChecked(true);
                break;
            case 3:
                noneShowRadio.setChecked(true);
                break;
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick({R.id.brandNameRadio, R.id.manufactureNameRadio, R.id.androidNameRadio, R.id.noneShowRadio,
            R.id.rgDevices, R.id.confirmTv, R.id.cancelTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.brandNameRadio:
                break;
            case R.id.manufactureNameRadio:
                break;
            case R.id.androidNameRadio:
                break;
            case R.id.noneShowRadio:
                break;
            case R.id.rgDevices:
                break;
            case R.id.confirmTv:
                switch (rgDevices.getCheckedRadioButtonId()) {
                    case R.id.brandNameRadio:
                        SharedPreferencePrefUserInfo.setDeviceName(0);
                        EventBus.getDefault().post(new UpdateDeviceIdEvent(0));
                        break;
                    case R.id.manufactureNameRadio:
                        SharedPreferencePrefUserInfo.setDeviceName(1);
                        EventBus.getDefault().post(new UpdateDeviceIdEvent(1));
                        break;
                    case R.id.androidNameRadio:
                        SharedPreferencePrefUserInfo.setDeviceName(2);
                        EventBus.getDefault().post(new UpdateDeviceIdEvent(2));
                        break;
                    case R.id.noneShowRadio:
                        SharedPreferencePrefUserInfo.setDeviceName(3);
                        EventBus.getDefault().post(new UpdateDeviceIdEvent(3));
                        break;
                }
                dismiss();
            case R.id.cancelTv:
                dismiss();
                break;
            default:
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
