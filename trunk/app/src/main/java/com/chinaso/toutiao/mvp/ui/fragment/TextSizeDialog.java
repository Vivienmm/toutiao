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
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.mvp.listener.UpdateTextSizeEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by chinaso on 2017/3/2.
 */

public class TextSizeDialog extends DialogFragment {


    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.sBigRadio)
    RadioButton sBigRadio;
    @BindView(R.id.bigRadio)
    RadioButton bigRadio;
    @BindView(R.id.middleRadio)
    RadioButton middleRadio;
    @BindView(R.id.smallRadio)
    RadioButton smallRadio;
    @BindView(R.id.tvSizeRG)
    RadioGroup tvSizeRG;
    @BindView(R.id.confirmTv)
    TextView confirmTv;
    @BindView(R.id.cancelTv)
    TextView cancelTv;
    private String mParam1;
    private FragmentDialog.OnFragmentInteractionListener mListener;

    public TextSizeDialog() {
        // Required empty public constructor
    }


    public static TextSizeDialog newInstance(String param1) {
        TextSizeDialog fragment = new TextSizeDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
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
        View view = inflater.inflate(R.layout.textsize_dialog, container, false);
        ButterKnife.bind(this, view);

        switch (mParam1) {
            case "S":
                smallRadio.setChecked(true);
                break;
            case "M":
                middleRadio.setChecked(true);
                break;
            case "L":
                bigRadio.setChecked(true);
                break;
            case "XL":
                sBigRadio.setChecked(true);
                break;
            default:
                middleRadio.setChecked(true);
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

    @OnClick({R.id.sBigRadio, R.id.bigRadio, R.id.middleRadio, R.id.smallRadio, R.id.confirmTv, R.id.cancelTv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sBigRadio:
                break;
            case R.id.bigRadio:
                break;
            case R.id.middleRadio:
                break;
            case R.id.smallRadio:
                break;
            case R.id.confirmTv:
                switch (tvSizeRG.getCheckedRadioButtonId()) {
                    case R.id.sBigRadio:
                        SharedPreferencesSetting.setIsFontSize("XL");
                        EventBus.getDefault().post(new UpdateTextSizeEvent("特大号字"));
                        break;
                    case R.id.bigRadio:
                        SharedPreferencesSetting.setIsFontSize("L");
                        EventBus.getDefault().post(new UpdateTextSizeEvent("大号字"));
                        break;
                    case R.id.middleRadio:
                        SharedPreferencesSetting.setIsFontSize("M");
                        EventBus.getDefault().post(new UpdateTextSizeEvent("中号字"));
                        break;
                    case R.id.smallRadio:
                        SharedPreferencesSetting.setIsFontSize("S");
                        EventBus.getDefault().post(new UpdateTextSizeEvent("小号字"));
                        break;
                }
                dismiss();
                break;
            case R.id.cancelTv:
                dismiss();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
