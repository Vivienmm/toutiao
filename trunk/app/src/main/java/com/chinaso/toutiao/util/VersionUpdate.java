package com.chinaso.toutiao.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.SharedPreferencesSetting;
import com.chinaso.toutiao.app.entity.update.MyUpdateListener;
import com.chinaso.toutiao.app.entity.update.VersionUpdateResponse;
import com.chinaso.toutiao.net.NetworkService;
import com.chinaso.toutiao.view.CustomUpdateDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VersionUpdate {

    private static VersionUpdate instance;
    private Context context;
    private MyUpdateListener myUpdateListener = null;

    private VersionUpdate(Context context) {
        this.context = context;
    }

    public static VersionUpdate getInstance(Context context) {
        if (instance == null) {
            instance = new VersionUpdate(context);
        }
        return instance;
    }

    public static void checkVerUpdate(Context context, int request) {
        getInstance(context).update(request);
    }

    /**
     * 获取软件版本号
     *
     * @return
     */
    private int getVerCode() {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 检测是否需要更新
     */
    private void isNeedToUpdate(final MyUpdateListener myUpdateListener) {

        if (!NetWorkStatusUtil.isNetworkAvailable(context)) {
            myUpdateListener.onUpdateReturned(Constants.VERSION_UPDATE_NO_NETWORK, null); //无网络连接
        } else {

            Call<VersionUpdateResponse> versionUpdateResponseCall = NetworkService.getInstance().getServerVersionInfo(getVerCode());
            versionUpdateResponseCall.enqueue(new Callback<VersionUpdateResponse>() {

                @Override
                public void onResponse(Call<VersionUpdateResponse> call, Response<VersionUpdateResponse> response) {
                    VersionUpdateResponse versionUpdateResponse = response.body();
                    if (versionUpdateResponse == null) {
                        myUpdateListener.onUpdateReturned(Constants.VERSION_UPDATE_TIME_OUT, null); //连接超时
                    } else {
                        if (null != versionUpdateResponse.getUpdate() && versionUpdateResponse.getUpdate()) {
                            //提示更新
                            myUpdateListener.onUpdateReturned(Constants.VERSION_UPDATE_YES, versionUpdateResponse);
                        } else {
                            myUpdateListener.onUpdateReturned(Constants.VERSION_UPDATE_NO, null); //无版本更新
                        }
                    }
                }

                @Override
                public void onFailure(Call<VersionUpdateResponse> call, Throwable t) {
                    myUpdateListener.onUpdateReturned(Constants.VERSION_UPDATE_TIME_OUT, null); //连接超时
                }
            });
        }
    }

    /**
     * 判断是否需要更新，并提供回调函数
     * 主要用于设置界面检测新版本“New”图标显示
     */
    private void updateForSetting() {
        isNeedToUpdate(new MyUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, VersionUpdateResponse updateInfo) {
                switch (updateStatus) {
                    case Constants.VERSION_UPDATE_YES:
                        myUpdateListener.onUpdateReturned(Constants.VERSION_UPDATE_YES, null);
                        break;
                    case Constants.VERSION_UPDATE_NO:
                        myUpdateListener.onUpdateReturned(Constants.VERSION_UPDATE_NO, null);
                        break;
                    case Constants.VERSION_UPDATE_NO_NETWORK:
                        myUpdateListener.onUpdateReturned(Constants.VERSION_UPDATE_NO_NETWORK, null);
                        break;
                    case Constants.VERSION_UPDATE_TIME_OUT:
                        myUpdateListener.onUpdateReturned(Constants.VERSION_UPDATE_TIME_OUT, null);
                        break;
                }
            }
        });
    }

    /**
     * 初始化时更新
     * 点击设置界面“新版本监测”
     * 有更新显示提示框
     */
    private void update(final int request) {
        isNeedToUpdate(new MyUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, VersionUpdateResponse updateInfo) {
                switch (updateStatus) {
                    case Constants.VERSION_UPDATE_YES:
                        //提示更新
                        if (request == Constants.VERSION_UPDATE_INIT_REQUEST) {
                            if (SharedPreferencesSetting.getLastVersionCodeFromServer().equals(updateInfo.getNew_version())) {

                            } else {
                                suggestVerUpdate(updateInfo, request);
                            }
                        } else if (request == Constants.VERSION_UPDATE_SETTING_REQUEST) {
                            suggestVerUpdate(updateInfo, request);
                        }
                        break;
                    case Constants.VERSION_UPDATE_NO:
                        if (request == Constants.VERSION_UPDATE_SETTING_REQUEST) {
                            Toast.makeText(context, context.getString(R.string.no_new_version), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Constants.VERSION_UPDATE_NO_NETWORK:
                        if (request == Constants.VERSION_UPDATE_SETTING_REQUEST) {
                            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Constants.VERSION_UPDATE_TIME_OUT:
                        if (request == Constants.VERSION_UPDATE_SETTING_REQUEST) {
                            Toast.makeText(context, context.getString(R.string.no_new_version), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    /**
     * 更新提示框
     * 分初始化界面和设置界面，通过requestCode区分
     */
    private void suggestVerUpdate(final VersionUpdateResponse updateResponse, int requeseCode) {
        final CustomUpdateDialog.Builder builder = new CustomUpdateDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_version_update, null);

        builder.setContentView(v).setTitle(R.string.txt_update_title).setMessage(updateResponse.getUpdate_log()).setPositiveButton(R.string.txt_btn_update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk(updateResponse.getApk_url());
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.txt_btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                unRegisterUpdate();
            }
        });

        final CustomUpdateDialog dialog = builder.create();

        switch (requeseCode) {
            case Constants.VERSION_UPDATE_SETTING_REQUEST:
                v.findViewById(R.id.check_ignore).setVisibility(View.GONE);
                break;
            case Constants.VERSION_UPDATE_INIT_REQUEST:
                v.findViewById(R.id.check_ignore).setVisibility(View.VISIBLE);
                ((CheckBox) v.findViewById(R.id.check_ignore)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            //忽略更新
                            SharedPreferencesSetting.setLastVersionCodeFromServer(updateResponse.getNew_version());
                            dialog.dismiss();
                            unRegisterUpdate();
                        }
                    }
                });
                break;
        }

        dialog.show();
    }


    /**
     * 开启Service下载apk
     * 下载完成处理广播，提示安装
     *
     * @param apk_url
     */
    private void downloadApk(String apk_url) {
//        String endType = apk_url.substring(apk_url.lastIndexOf(".") + 1, apk_url.length()).toLowerCase();
//        if (endType.equals("apk")) {
//            DownloadService.startService(context, apk_url);
//        }

        DownloadService.startService(context, apk_url, ".apk");
    }

    /**
     *
     */
    public void setUpdateListener(MyUpdateListener myUpdateListener) {
        this.myUpdateListener = myUpdateListener;
        updateForSetting();
    }

    public static void unRegisterUpdate() {
        if (instance != null) {
            instance = null;
        }
    }

}
