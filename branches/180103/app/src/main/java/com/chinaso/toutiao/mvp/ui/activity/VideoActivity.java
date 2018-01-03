package com.chinaso.toutiao.mvp.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.app.entity.VideoItemAPI;
import com.chinaso.toutiao.app.entity.VideoListItem;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.net.VideoService;
import com.chinaso.toutiao.util.NetWorkStatusUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import pl.tajchert.sample.DotsTextView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoActivity extends BaseActivity {

    @BindView(R.id.vv)
    VideoView videoView;
    @BindView(R.id.dot_loading)
    DotsTextView dotsTextView;
    @BindView(R.id.ll_loading)
    ViewGroup loadingViewGroup;
    @BindView(R.id.leftBtn)
    ImageButton backImageBtn;
    @BindView(R.id.ll_back)
    ViewGroup backLLayout;

    private MediaController mediaController;
    private BackBtnControlHandler backBtnControlHandler;
    private static final int HIDE_BACK_BTN_MESSAGE = 0;
    private int timeoffset = 0;


    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    public void initViews() {
        timeoffset = 0;
        backBtnControlHandler = new BackBtnControlHandler(this);
        mediaController = new MediaController(this);
        dotsTextView.showAndPlay();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                dotsTextView.hideAndStop();
                loadingViewGroup.setVisibility(View.GONE);
                backBtnControlHandler.sendEmptyMessageDelayed(HIDE_BACK_BTN_MESSAGE, 3000);
            }
        });

        Bundle videoBundl = getIntent().getExtras();
        VideoListItem videoItem = videoBundl.getParcelable("videoInfo");
        String[] apis = new String[0];
        String api = null;
        try {
            api = videoItem.getVideoApi();
            apis = api.split("=");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        VideoService.getInstance().fetchVideoItemAPI(apis[1]).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<VideoItemAPI>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                        } catch (IllegalStateException st) {
                            st.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(VideoItemAPI videoAPI) {
                        String url = videoAPI.getVideos().get(1).getUrl();
                        prepareToPlayVideo(url);
                    }
                });
    }


    /**
     * 控制返回箭头3s后消失
     */
    static class BackBtnControlHandler extends Handler {
        WeakReference<VideoActivity> wr = null;

        public BackBtnControlHandler(VideoActivity activity) {
            wr = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (wr.get() != null) {
                switch (msg.what) {
                    case HIDE_BACK_BTN_MESSAGE:
                        wr.get().backImageBtn.setVisibility(View.GONE);
                        wr.get().backLLayout.setClickable(false);
                        break;
                }

            }
            super.handleMessage(msg);
        }
    }

    @OnClick({R.id.ll_back, R.id.leftBtn})
    void onBackClick() {
        VideoActivity.this.finish();
    }

    @OnTouch(R.id.vv)
    boolean onTouchVV(View v, MotionEvent event) {
        controlBackBtn();
        return false;
    }


    /**
     * 准备播放视频
     * 检查网络
     *
     * @param url
     */
    private void prepareToPlayVideo(final String url) {
        if (NetWorkStatusUtil.isNetworkAvailable(this)) {
            if (NetWorkStatusUtil.isWifi(this)) {
                startVideo(url);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("您当前正在移动网络下观看，是否继续？");
                builder.setTitle("提示");
                builder.setPositiveButton("继续播放", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startVideo(url);
                    }
                });

                builder.setNegativeButton("停止播放", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        VideoActivity.this.finish();
                    }
                });
                builder.create().show();

            }
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("您没有接入网络，暂时无法播放");
            builder1.setTitle("提示");
            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    VideoActivity.this.finish();
                }
            });
            builder1.create().show();
        }

    }

    /**
     * 开始播放视频
     */
    private void startVideo(String url) {
        videoView.setVideoURI(Uri.parse(url));
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();
    }

    private void controlBackBtn() {
        if (mediaController.isShowing()) {
            backImageBtn.setVisibility(View.GONE);
            backLLayout.setClickable(false);
            //如果队列有，删除3s的控制
            backBtnControlHandler.removeMessages(HIDE_BACK_BTN_MESSAGE);
        } else {
            backImageBtn.setVisibility(View.VISIBLE);
            backLLayout.setClickable(true);
            //添加到消息队列3s后消失
            backBtnControlHandler.sendEmptyMessageDelayed(HIDE_BACK_BTN_MESSAGE, 3000);
        }
    }

    @Override
    protected void onResume() {
        dotsTextView.showAndPlay();
        loadingViewGroup.setVisibility(View.VISIBLE);
        backImageBtn.setVisibility(View.VISIBLE);
        backLLayout.setClickable(true);
        videoView.seekTo(timeoffset);
        videoView.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        timeoffset = videoView.getCurrentPosition();
        backBtnControlHandler.removeMessages(HIDE_BACK_BTN_MESSAGE);
        videoView.suspend();
        super.onPause();
    }

}
