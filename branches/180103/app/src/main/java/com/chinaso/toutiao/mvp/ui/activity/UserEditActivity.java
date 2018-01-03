package com.chinaso.toutiao.mvp.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.Constants;
import com.chinaso.toutiao.app.SharedPreferenceMark;
import com.chinaso.toutiao.app.SharedPreferencePrefUserInfo;
import com.chinaso.toutiao.app.entity.ModifyUserInfoResponse;
import com.chinaso.toutiao.mvp.entity.user.UserInfoManager;
import com.chinaso.toutiao.mvp.listener.UpdateUserInfoEvent;
import com.chinaso.toutiao.mvp.presenter.UserEditPresenter;
import com.chinaso.toutiao.mvp.presenter.impl.UserEditPresenterImpl;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.mvp.view.UserEditView;
import com.chinaso.toutiao.util.CropUtils;
import com.chinaso.toutiao.util.DialogPermission;
import com.chinaso.toutiao.util.FileUtil;
import com.chinaso.toutiao.util.PermissionUtil;
import com.chinaso.toutiao.util.PhoneUtils;
import com.chinaso.toutiao.util.ToastUtil;
import com.chinaso.toutiao.view.CustomActionBar;
import com.chinaso.toutiao.view.SelectPicPopupWindow;

import java.io.File;

import butterknife.BindView;
import butterknife.OnTextChanged;
import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserEditActivity extends BaseActivity implements UserEditView {

    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_ALBUM = 2;
    private static final int REQUEST_CODE_CROUP_PHOTO = 3;
    private static final float NORMAL_ALPHA = 1.0f;
    private static final float CHANGE_ALPHA = 0.4f;

    @BindView(R.id.user_nickname)
    EditText mEditName;
    @BindView(R.id.user_save)
    Button mBtnSave;
    @BindView(R.id.user_avatar_layout)
    RelativeLayout mUserAvatarLayout;
    @BindView(R.id.actionbar)
    CustomActionBar customActionBar;

    private int length = 13;
    private SelectPicPopupWindow popupWindow;
    private Uri uri;
    private File file;
    private String mNickName;
    private okhttp3.RequestBody requestBody;
    private MultipartBody.Part photo;
    private boolean isChange;
    private UserEditPresenter mPresenter = new UserEditPresenterImpl();
    private boolean isRegister;
    @Override
    public int getLayoutId() {
        return R.layout.activity_user_edit;
    }

    @Override
    protected void onStart() {
        super.onStart();
        file = new File(FileUtil.getCachePath(this), "user-avatar.jpg");
        uri = Uri.fromFile(file);
    }

    @Override
    public void initViews() {
        initActionBar();
        mPresenter.attachView(this);
        mPresenter.onCreate();
        initPopuWindow();
    }

    private void initActionBar() {
        customActionBar.setTitleView("编辑个人资料");
        if (!SharedPreferencePrefUserInfo.getIsRegister()) {
            customActionBar.setLeftViewImg(R.mipmap.actionbar_back);
        }
        if (SharedPreferencePrefUserInfo.getIsRegister()) {
            customActionBar.setRightTV("跳过");
        }
        customActionBar.setOnClickListener(new CustomActionBar.ActionBarInterface() {
            @Override
            public void leftViewClick() {
                onBackPressed();
            }

            @Override
            public void rightImgClick() {

            }

            @Override
            public void rightTVClick() {
                if (SharedPreferencePrefUserInfo.getIsRegister()) {
                    SharedPreferencePrefUserInfo.setIsRegister(false);
                    finish();
                }
            }

            @Override
            public void openNewsClick() {

            }
        });
    }

    private void initPopuWindow() {
        popupWindow = new SelectPicPopupWindow(this, itemsOnclickListener);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setParams(NORMAL_ALPHA);
            }
        });
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @OnTextChanged(R.id.user_nickname)
    void afterTextChanged(Editable s) {
        if (!PhoneUtils.match(PhoneUtils.getText(mEditName), mNickName)) {
            mBtnSave.setEnabled(true);
        } else {
            mBtnSave.setEnabled(false);
        }
    }

    private View.OnClickListener itemsOnclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
            switch (v.getId()) {
                case R.id.register_set_camera:
                    if (PermissionUtil.hasCameraPermission(UserEditActivity.this)) {
                        uploadAvatarFromPhotoRequest();
                    }
                    break;
                case R.id.register_take_photo:
                    uploadAvatarFromAlbumRequest();
                    break;
                case R.id.register_set_cancle:
                    popupWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * update username and Avatar
     */
    private void uploadUserInfo() {
        mPresenter.getNickName(PhoneUtils.getText(mEditName));
        mPresenter.getBodyPhoto(photo);
        mPresenter.saveNameAndAvatar();
    }

    @Override
    public void showErrorMsg(String message) {
        ToastUtil.showToast(this, message, 0);
    }

    @Override
    public void setAvatar(String url) {
    }

    @Override
    public void setUserName(String userName) {
        mNickName = userName;
        mEditName.setText(userName);
        mEditName.setSelection(userName.length());
    }

    @Override
    public void showToast(String message) {
        ToastUtil.showToast(this, message, 0);
    }



    /**
     * modify success
     *
     * @param data
     */
    @Override
    public void onSuccess(ModifyUserInfoResponse data) {
        isChange = false;
        finish();
    }

    /**
     * take photo
     */
    private void uploadAvatarFromPhotoRequest() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    /**
     * take album
     */
    private void uploadAvatarFromAlbumRequest() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == REQUEST_CODE_ALBUM && data != null) {
            Uri newUri = Uri.parse("file:///" + CropUtils.getPath(this, data.getData()));
            if (newUri != null) {
                cropImageUri(newUri, uri);
            } else {
                ToastUtil.showToast(this, "没有得到相册图片", 0);
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
        final File cover = FileUtil.getSmallBitmap(this, fileSrc);
        String mimeType = "image/*";
        requestBody = RequestBody.create(MediaType.parse(mimeType), file);
        String fileName = cover.getName();
        photo = MultipartBody.Part.createFormData("portrait", fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length()), requestBody);
    }

    /**
     * 调用系统相册裁剪
     *
     * @param orgUri
     * @param desUri
     */
    private void cropImageUri(Uri orgUri, Uri desUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(orgUri, "image/*");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", Constants.USER_AVATAR_MAX_SIZE);//图片输出大小
        intent.putExtra("outputY", Constants.USER_AVATAR_MAX_SIZE);
        intent.putExtra("output", desUri);
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }


    /**
     * 裁剪拍照裁剪
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", Constants.USER_AVATAR_MAX_SIZE);//图片输出大小
        intent.putExtra("outputY", Constants.USER_AVATAR_MAX_SIZE);
        intent.putExtra("output", uri);
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }

    private void setParams(float f) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = f;
        params.dimAmount = f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    public boolean isEmpty() {
        if (!PhoneUtils.notEmptyText(mEditName)) {
            ToastUtil.showToast(this, "用户名不能位空", 0);
            return false;
        } else if (PhoneUtils.getTextLength(mEditName) > length) {
            ToastUtil.showToast(this, "昵称不超过13位", 0);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case PermissionUtil.REQUEST_SHOWCAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    uploadAvatarFromPhotoRequest();

                } else {
                    if (!SharedPreferenceMark.getHasShowCamera()) {
                        SharedPreferenceMark.setHasShowCamera(true);
                        new DialogPermission(this, "关闭摄像头权限影响扫描功能");

                    } else {
                        Toast.makeText(this, "未获取摄像头权限", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferencePrefUserInfo.setIsRegister(false);
        UserInfoManager.getInstance().updateLocalLoginResponse();
        EventBus.getDefault().post(new UpdateUserInfoEvent(true));
    }

}