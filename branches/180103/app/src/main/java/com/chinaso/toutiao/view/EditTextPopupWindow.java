package com.chinaso.toutiao.view;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.mvp.data.collection.CollectionEntity;
import com.chinaso.toutiao.mvp.data.collection.CollectionManageDao;
import com.chinaso.toutiao.mvp.entity.InsertComment;
import com.chinaso.toutiao.mvp.entity.QuestionResponse;
import com.chinaso.toutiao.mvp.entity.user.UserInfoManager;
import com.chinaso.toutiao.mvp.ui.activity.DetailCommentActivity;
import com.chinaso.toutiao.mvp.ui.activity.QueryCommentActivity;
import com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.net.NetworkService;
import com.chinaso.toutiao.util.SignCodeUtil;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.util.secure.MD5Util;
import com.chinaso.toutiao.view.segmentcontrol.SegmentControl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;

import static com.chinaso.toutiao.mvp.ui.activity.VerticalDetailActivity.isCollection;

/**
 * 自定义评论、更多设置的弹出框
 * Created by yangfang on 2016/3/24.
 */
public class EditTextPopupWindow extends PopupWindow implements View.OnClickListener {
    private String FUNCTION = "function";
    private String COMMENT = "comment";

    private View rootView;
    private BaseActivity mContext;

    private TextView mCommentCancleTV;
    private TextView mCommentConfirmTV;
    private EditText mCommentET;

    private SegmentControl mFunctionSegmentHorzontal;
    private ImageView mFunctionInterested;
    private ImageView mFunctionClollect;
    private ImageView mFunctionCopy;
    private ImageView mFunctionReport;
    private ImageView mFunctionRefresh;
    private CheckSwitchButton mFunctionCheckSB;

    private String urlIntent;
    private String nid;
    private String mContentId;
    private WebView webView;
    private String type;
    private String fontAction;

    public EditTextPopupWindow(final BaseActivity context, String mNewsId, String mContentId, String mode) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        nid = mNewsId;
        this.mContentId = mContentId;
        if (mode.equals(COMMENT)) {
            rootView = inflater.inflate(R.layout.item_comment, null);
        } else if (mode.equals(FUNCTION)) {
            rootView = inflater.inflate(R.layout.item_function, null);
        }
        this.setContentView(rootView);

        if (mode.equals(COMMENT)) {
            initViewComment();
            initCommentPopuwindowMode();
        } else if (mode.equals(FUNCTION)) {
            initFunctionPopuwindowMode();
            initViewFunction();
        }
    }

    /**
     * 新闻的popupwindow
     */
    private void initFunctionPopuwindowMode() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ColorDrawable drawable = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(drawable);
    }

    /**
     * 评论的popupwindow
     */
    private void initCommentPopuwindowMode() {
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ColorDrawable drawable = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(drawable);
        mCommentET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

        });
    }

    private void initViewFunction() {
        mFunctionSegmentHorzontal = (SegmentControl) rootView.findViewById(R.id.segment_control);

        mFunctionInterested = (ImageView) rootView.findViewById(R.id.iv_not_interested);
        mFunctionClollect = (ImageView) rootView.findViewById(R.id.iv_collect);
        mFunctionCopy = (ImageView) rootView.findViewById(R.id.iv_copy_link);
        mFunctionReport = (ImageView) rootView.findViewById(R.id.iv_report);
        mFunctionRefresh = (ImageView) rootView.findViewById(R.id.iv_refresh);
        mFunctionCheckSB = (CheckSwitchButton) rootView.findViewById(R.id.openday);

        mFunctionInterested.setOnClickListener(this);
        mFunctionClollect.setOnClickListener(this);
        mFunctionCopy.setOnClickListener(this);
        mFunctionReport.setOnClickListener(this);
        mFunctionRefresh.setOnClickListener(this);
        mFunctionCheckSB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((VerticalDetailActivity) mContext).setNigthMode(isChecked);
                SharedPreferencesSetting.setIsNightMode(isChecked);
            }
        });

        mFunctionCheckSB.setChecked(SharedPreferencesSetting.getIsNightMode());

        if (SharedPreferencesSetting.getIsFontSize() != null) {
            int index;
            String type = SharedPreferencesSetting.getIsFontSize();
            switch (type) {
                case "S":
                    index = 0;
                    break;
                case "M":
                    index = 1;
                    break;
                case "L":
                    index = 2;
                    break;
                case "XL":
                    index = 3;
                    break;
                default:
                    index = 1;
                    break;
            }
            mFunctionSegmentHorzontal.setSelectedIndex(index);
        }
        mFunctionSegmentHorzontal.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                switch (index) {
                    case 0:
                        type = "S";
                        break;
                    case 1:
                        type = "M";
                        break;
                    case 2:
                        type = "L";
                        break;
                    case 3:
                        type = "XL";
                        break;
                    default:
                        type = "M";
                        break;
                }
                SharedPreferencesSetting.setIsFontSize(type);
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:" + fontAction + "('" + type + "')");
                    }
                });
            }
        });

    }

    private void initViewComment() {
        mCommentCancleTV = (TextView) rootView.findViewById(R.id.comment_cancle);
        mCommentConfirmTV = (TextView) rootView.findViewById(R.id.comment_confirm);
        mCommentET = (EditText) rootView.findViewById(R.id.comment_text);
        mCommentCancleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mCommentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(getTextString())) {
                    mCommentConfirmTV.setBackgroundResource(R.drawable.comment_confirm_xml);
                } else {
                    mCommentConfirmTV.setBackgroundResource(R.drawable.comment_confirm_press_xml);
                    mCommentConfirmTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            confirmComment();
                        }
                    });
                }
            }
        });
    }

    private String getTextString() {
        return mCommentET.getText().toString();
    }

    private void confirmComment() {
        Long userId = UserInfoManager.getInstance().getLoginResponse().getUserId();
        String user = String.valueOf(userId);
        Integer uid = Integer.valueOf(user);
        try {
            nid = URLDecoder.decode(nid, "UTF-8");
            Log.e("TAG", "FUNCTION" + nid);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String name = UserInfoManager.getInstance().getLoginResponse().getNickName();
        Map<String, String> map = new TreeMap<>();
        map.put("uid", "uid" + uid);
        map.put("nick", "nick" + name);
        map.put("content", "content" + getTextString());
        map.put("nid", "nid" + nid);
        map.put("contentId", "contentId" + mContentId);
        String sign = MD5Util.md5(SignCodeUtil.getAsceCode(map));

        Call<InsertComment> insertCommentCall = NetworkService.getSplashInstance().insertcomment(uid, name, getTextString(), nid, mContentId, sign);
        insertCommentCall.enqueue(new Callback<InsertComment>() {
            @Override
            public void onResponse(Call<InsertComment> call, retrofit2.Response<InsertComment> response) {
                InsertComment insertComment = response.body();
                if (insertComment == null) {
                    return;
                }
                if (insertComment.getResult()) {
                    ToastUtil.showToast(mContext, insertComment.getError(), 0);
                    if (mContext instanceof QueryCommentActivity) {
                        ((QueryCommentActivity) mContext).reloadWeb();
                    }
                    setTextString(null);
                } else {
                    ToastUtil.showToast(mContext, insertComment.getError(), 0);
                }
                dismiss();
            }

            @Override
            public void onFailure(Call<InsertComment> call, Throwable t) {
                ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.register_loda_failure), 0);
                dismiss();
            }
        });
    }

    private void setTextString(String flag) {
        mCommentET.setText(flag);
    }

    /**
     * 功能窗的点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_not_interested:
                Call<QuestionResponse> questionResponseCall = NetworkService.getSplashInstance().getQuestionType(nid, "8");
                questionResponseCall.enqueue(new Callback<QuestionResponse>() {
                    @Override
                    public void onResponse(Call<QuestionResponse> call, retrofit2.Response<QuestionResponse> response) {
                        if (response.body().getResult()) {
                            ToastUtil.showToast(mContext, mContext.getString(R.string.not_signal), 0);
                        } else {
                            ToastUtil.showToast(mContext, mContext.getString(R.string.news_flag_fail), 0);
                        }
                    }

                    @Override
                    public void onFailure(Call<QuestionResponse> call, Throwable t) {
                        ToastUtil.showToast(mContext, mContext.getString(R.string.register_loda_failure), 0);
                    }
                });
                break;
            case R.id.iv_collect:
                collection();
                if (null != ((VerticalDetailActivity) mContext).getShareToolBar()) {
                    ((VerticalDetailActivity) mContext).getShareToolBar().changeCollectionImgBg(isCollection);
                }

                if (null != ((VerticalDetailActivity) mContext).getShareCommentToolBar()) {
                    ((VerticalDetailActivity) mContext).getShareCommentToolBar().changeCollectionImgBg(isCollection);
                }
                break;
            case R.id.iv_copy_link:
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("url", urlIntent);
                if (clipData.toString() != null) {
                    cmb.setPrimaryClip(clipData);
                    ToastUtil.showToast(mContext, mContext.getString(R.string.news_copy_success), 0);
                }
                break;
            case R.id.iv_report:
                String urlStr = null;
                try {
                    urlStr = URLEncoder.encode(nid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url = Constants.REPORT_PROBLEM + urlStr;
                Bundle bundle = new Bundle();
                bundle.putString("report", url);
                Intent intent = new Intent();
                intent.setClass(mContext, DetailCommentActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                this.dismiss();
                break;
            case R.id.iv_refresh:
                webView.reload();
                break;
            default:
                break;
        }
    }

    private void collection() {
        Toast toast = Toast.makeText(mContext, "",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        if (isCollection) {
            new CollectionManageDao().deleteCollectionByUrl(webView.getUrl());
            toast.setText(R.string.news_cancle_collect);
        } else {
            CollectionEntity entity = new CollectionEntity();
            entity.setTitle(webView.getTitle());
            entity.setUrl(webView.getUrl());
            entity.setType(2);
            new CollectionManageDao().insertItem(entity);
            toast.setText(R.string.news_collect);
            webView.getUrl();
        }
        toast.show();
        isCollection = !isCollection;
    }

    /**
     * 新闻功能窗所需要的参数
     *
     * @param urlIntent
     * @param nid
     * @param webView
     * @param fontAction
     */
    public void setParams(String urlIntent, String nid, WebView webView, String fontAction) {
        this.urlIntent = urlIntent;
        this.nid = nid;
        this.webView = webView;
        this.fontAction = fontAction;
    }
}
