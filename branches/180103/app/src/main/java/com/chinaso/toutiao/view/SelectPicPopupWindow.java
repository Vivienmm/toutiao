package com.chinaso.toutiao.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chinaso.toutiao.R;

public class SelectPicPopupWindow extends PopupWindow {

    private View view;
    private TextView mTakeCamera;
    private TextView mTakePhoto;
    private TextView mTakeCancle;
    private Context mContext;

    public SelectPicPopupWindow(Context context, View.OnClickListener itemOnclicker) {
        super(context);
        this.mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_register_photo, null);
        initView(view);
        mTakeCancle.setOnClickListener(itemOnclicker);
        mTakeCamera.setOnClickListener(itemOnclicker);
        mTakePhoto.setOnClickListener(itemOnclicker);
        this.setContentView(view);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable drawable = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(drawable);
    }

    private void initView(View view) {
        mTakeCamera = (TextView) view.findViewById(R.id.register_set_camera);
        mTakePhoto = (TextView) view.findViewById(R.id.register_take_photo);
        mTakeCancle = (TextView) view.findViewById(R.id.register_set_cancle);
    }

}
