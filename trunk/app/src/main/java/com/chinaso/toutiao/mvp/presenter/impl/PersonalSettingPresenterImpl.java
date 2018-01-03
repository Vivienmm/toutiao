package com.chinaso.toutiao.mvp.presenter.impl;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.SharedPreferenceMark;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.app.TTApplication;
import com.chinaso.toutiao.app.entity.ModifyUserInfoResponse;
import com.chinaso.toutiao.mvp.entity.user.UserInfoManager;
import com.chinaso.toutiao.mvp.listener.UpdateUserInfoEvent;
import com.chinaso.toutiao.mvp.presenter.base.BasePresenter;
import com.chinaso.toutiao.mvp.ui.activity.PersonalSettingActivity;
import com.chinaso.toutiao.mvp.ui.fragment.SimpleCalendarDialogFragment;
import com.chinaso.toutiao.mvp.view.PersonalSettingView;
import com.chinaso.toutiao.mvp.view.base.BaseView;
import com.chinaso.toutiao.net.NetworkService;
import com.chinaso.toutiao.util.CropUtils;
import com.chinaso.toutiao.util.DebugUtil;
import com.chinaso.toutiao.util.DialogPermission;
import com.chinaso.toutiao.util.FileUtil;
import com.chinaso.toutiao.util.PermissionUtil;
import com.chinaso.toutiao.util.SignCodeUtil;
import com.chinaso.toutiao.util.secure.AESUtils;
import com.chinaso.toutiao.util.secure.JniUtil;
import com.chinaso.toutiao.util.secure.MD5Util;
import com.chinaso.toutiao.util.secure.PackageUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.TreeMap;

import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.chinaso.toutiao.mvp.ui.activity.PersonalSettingActivity.REQUEST_CODE_ALBUM;
import static com.chinaso.toutiao.mvp.ui.activity.PersonalSettingActivity.REQUEST_CODE_CROUP_PHOTO;
import static com.chinaso.toutiao.mvp.ui.activity.PersonalSettingActivity.REQUEST_CODE_TAKE_PHOTO;

public class PersonalSettingPresenterImpl implements BasePresenter {
    private PersonalSettingView mView;
    private PersonalSettingActivity mActivity;
    private Uri uri;
    private boolean isChange;
    private okhttp3.RequestBody requestBody;
    private MultipartBody.Part photo;
    private File file;
    public PersonalSettingPresenterImpl(PersonalSettingActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mView = (PersonalSettingView) view;
    }

    @Override
    public void onCreate() {
        file = new File(FileUtil.getCachePath(mActivity), "user-avatar.jpg");
    }

    public void showNickName() {
        String nameStr = UserInfoManager.getInstance().getLoginResponse().getNickName();
        mView.showNickName(nameStr);
    }

    public void showAvatar() {
        mView.showAvatar();
    }

    public void showChangeNameDialog() {
        final EditText editText = new EditText(mActivity);
        editText.setText(mView.getNickName());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(mActivity);
        inputDialog.setTitle("修改昵称").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mView.showNickName(editText.getText().toString());
                    }
                }).setNegativeButton("取消",new  DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    public void selectedSex() {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_select_sex, null);
        RadioGroup group = (RadioGroup) view.findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int radioButtonId = arg0.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) view.findViewById(radioButtonId);
                mView.showSex(rb.getText().toString());
            }
        });
        new AlertDialog.Builder(mActivity)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
    }

    public void setBirthDay() {
        SimpleCalendarDialogFragment fragment = new SimpleCalendarDialogFragment();
        fragment.show(mActivity.getSupportFragmentManager(), "test-simple-calendar");
        fragment.setOnSelectedLisnter(new SimpleCalendarDialogFragment.OnSelectedDayChangeListener() {
            @Override
            public void onSelectedDay(String dataStr) {
                mView.showBirthDay(dataStr);
            }
        });
    }

    public void showDeviceName(int deviceId) {
        String info ;
        switch (deviceId) {
            case 0:
                info = Build.BOARD + " " + Build.MODEL;
                break;
            case 1:
                info = Build.MANUFACTURER + "手机";
                break;
            case 2:
                info = "Android手机";
                break;
            case 3:
                info = "不显示";
                break;
            default:
                info = "无法获取设备";
                break;
        }
        mView.showDeviceName(info);
    }

    public void uploadUserInfo() {
        String userId = String.valueOf(UserInfoManager.getInstance().getUserId());
        String nickName = mView.getNickName();
        try {
            nickName = URLEncoder.encode(nickName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //secure check
        JniUtil jniUtil = new JniUtil();
        String userKey = jniUtil.getUserKey(PackageUtil.getSignature(mActivity));
        if (!TextUtils.isEmpty(userKey)) {
            userId = AESUtils.encode(userId, userKey);
        } else {
            mView.showErrorMsg("包签名错误");
            return;
        }

        TreeMap<String, String> map = new TreeMap<>();
        map.put("userId", "userId" + userId);
        map.put("nickName", "nickName" + nickName);
        final String sign = MD5Util.md5(SignCodeUtil.getAsceCode(map));

        if (photo != null) {
            Call<ModifyUserInfoResponse> modifyUserInfoResponseCall = NetworkService.getSplashInstance().modifyUserInfo(RequestBody.create(null, userId), RequestBody.create(null, nickName), photo, RequestBody.create(null, sign));
            modifyUserInfoResponseCall.enqueue(new Callback<ModifyUserInfoResponse>() {

                @Override
                public void onResponse(Call<ModifyUserInfoResponse> call, Response<ModifyUserInfoResponse> response) {
                    ModifyUserInfoResponse modifyUserInfoResponse = response.body();
                    if (modifyUserInfoResponse == null) {
                        DebugUtil.e(DebugUtil.TAG_NET_ERR, "响应空，接口异常");
                        return;
                    }
                    if (modifyUserInfoResponse.isResult()) {
                        mView.showErrorMsg("更新用户资料成功");
                        UserInfoManager.getInstance().getLoginResponse().setAvatar(modifyUserInfoResponse.getAvatar());
                        UserInfoManager.getInstance().getLoginResponse().setNickName(modifyUserInfoResponse.getNickName());
                        UserInfoManager.getInstance().updateLocalLoginResponse();
                        EventBus.getDefault().post(new UpdateUserInfoEvent(true));
                        isChange = false;
                        SharedPreferencePrefUserInfo.setIsRegister(false);
                        mView.finishActivity();

                    } else {
                        mView.showErrorMsg("更新失败: " + modifyUserInfoResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ModifyUserInfoResponse> call, Throwable t) {
                    mView.showErrorMsg(TTApplication.getApp().getResources().getString(R.string.register_loda_failure));
                }

            });
        } else {

            Call<ModifyUserInfoResponse> modifyUserInfoResponseCall = NetworkService.getSplashInstance().modifyUserInfo(RequestBody.create(null, userId), RequestBody.create(null, nickName), RequestBody.create(null, sign));
            modifyUserInfoResponseCall.enqueue(new Callback<ModifyUserInfoResponse>() {

                @Override
                public void onResponse(Call<ModifyUserInfoResponse> call, Response<ModifyUserInfoResponse> response) {
                    ModifyUserInfoResponse modifyUserInfoResponse = response.body();
                    if (modifyUserInfoResponse == null) {
                        DebugUtil.e(DebugUtil.TAG_NET_ERR, "响应空，接口异常");
                        return;
                    }
                    if (modifyUserInfoResponse.isResult()) {
                        mView.showErrorMsg("更新用户资料成功");
                        UserInfoManager.getInstance().getLoginResponse().setAvatar(modifyUserInfoResponse.getAvatar());
                        UserInfoManager.getInstance().getLoginResponse().setNickName(modifyUserInfoResponse.getNickName());
                        UserInfoManager.getInstance().updateLocalLoginResponse();
                        EventBus.getDefault().post(new UpdateUserInfoEvent(true));
                        SharedPreferencePrefUserInfo.setIsRegister(false);
                        isChange = false;
                        mView.finishActivity();

                    } else {
                        mView.showErrorMsg("更新失败");
                    }
                }

                @Override
                public void onFailure(Call<ModifyUserInfoResponse> call, Throwable t) {
                    mView.showErrorMsg(TTApplication.getApp().getResources().getString(R.string.register_loda_failure));
                }
            });
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_SHOWCAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                uploadAvatarFromPhotoRequest();
            } else {
                if (!SharedPreferenceMark.getHasShowCamera()) {
                    SharedPreferenceMark.setHasShowCamera(true);
                    new DialogPermission(TTApplication.getApp(), "关闭摄像头权限影响扫描功能");
                } else {
                    mView.showErrorMsg("未获取摄像头权限");
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != -1) {
            return;
        }
        if (requestCode == REQUEST_CODE_ALBUM && data != null) {
            Uri newUri;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                newUri = Uri.parse("file:///" + CropUtils.getPath(mActivity, data.getData()));
            } else {
                newUri = data.getData();
            }
            if (newUri != null) {
                cropImageUri(newUri);
            } else {
                mView.showErrorMsg("没有得到相册图片");
            }
        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            startPhotoZoom(uri);
        } else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {
            isChange = true;
            uploadAvatarFromPhoto();
        }
    }

    private void uploadAvatarFromPhoto() {
        compressAndUploadAvatar(file.getPath());
    }

    private void compressAndUploadAvatar(String fileSrc) {
        final File cover = FileUtil.getSmallBitmap(mActivity, fileSrc);
        String mimeType = "image/*";
        requestBody = RequestBody.create(MediaType.parse(mimeType), file);
        String fileName = cover.getName();
        photo = MultipartBody.Part.createFormData("portrait", fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length()), requestBody);

        mView.showAvatar();
        isChange = true;
    }

    /**
     * 裁剪拍照裁剪
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", Constants.USER_AVATAR_MAX_SIZE);//图片输出大小
        intent.putExtra("outputY", Constants.USER_AVATAR_MAX_SIZE);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        mActivity.startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }

    /**
     * 调用系统相册裁剪
     *
     * @param orgUri
     */
    private void cropImageUri(Uri orgUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(orgUri, "image/*");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", Constants.USER_AVATAR_MAX_SIZE);//图片输出大小
        intent.putExtra("outputY", Constants.USER_AVATAR_MAX_SIZE);
        intent.putExtra("output", Uri.fromFile(file));//裁剪输出的位置
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        mActivity.startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }

    public void uploadAvatarFromPhotoRequest() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mActivity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    public void uploadAvatarFromAlbumRequest() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        mActivity.startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM);
    }

}
