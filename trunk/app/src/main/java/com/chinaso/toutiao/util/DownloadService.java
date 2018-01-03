package com.chinaso.toutiao.util;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

public class DownloadService extends Service {

    private boolean flag = true;
    private String downloadPath;
    private String type;
    private DownloadManager dManager = null;
    private long ext = 0l;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadCompleteReceiver, filter);
        dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return super.onStartCommand(intent, flags, startId);
        } else if (TextUtils.isEmpty(intent.getStringExtra("downloadPath"))) {
            return super.onStartCommand(intent, flags, startId);
        } else {
            downloadPath = intent.getStringExtra("downloadPath");
            if (intent.hasExtra("type"))
                type = intent.getStringExtra("type");
            DownloadManager.Query query = new DownloadManager.Query();
            Cursor cursor = dManager.query(query);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getColumnIndex(DownloadManager.COLUMN_ID);
                        int ID = cursor.getInt(id);
                        int status = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int uriId = cursor.getColumnIndex(DownloadManager.COLUMN_URI);
                        String uriPath = cursor.getString(uriId);
                        int stausCode = cursor.getInt(status);
                        if (uriPath == null || uriPath.equals(downloadPath)) {
                            if (stausCode == DownloadManager.STATUS_FAILED) {
                                dManager.remove(ext);
                            } else if (stausCode == DownloadManager.STATUS_SUCCESSFUL) {
                                dManager.remove(ext);
                            } else {
                                flag = false;
                                Toast.makeText(getApplicationContext(), "正在下载……", Toast.LENGTH_LONG).show();
                            }
                        }
                    } while (cursor.moveToNext());
                    cursor.close();
                }
                if (flag) {
                    startDownload();
                }
            }
            return Service.START_STICKY;
        }
    }

    public static void startService(Context xContext, String content) {
        mContext=xContext;
        Intent sIntent = new Intent();
        sIntent.setClassName(xContext.getPackageName(), DownloadService.class.getName());
        sIntent.putExtra("downloadPath", content);
        xContext.startService(sIntent);
    }

    public static void startService(Context xContext, String content, String type) {
        mContext=xContext;
        Intent sIntent = new Intent();
        sIntent.setClassName(xContext.getPackageName(), DownloadService.class.getName());
        sIntent.putExtra("downloadPath", content);
        sIntent.putExtra("type", type);
        xContext.startService(sIntent);
    }

    //判断是否可直接调用DownLoadManager
    private void startDownload(){
        int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:com.android.providers.downloads"));
            mContext.startActivity(intent);
        } else {
            requestDownload();
        }
    }
    private void requestDownload() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadPath));
        //设置下载时 的网络
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        //设置下载保存的路径
        // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fk.apk");
        // sdcard的目录下的download文件夹
        if (TextUtils.isEmpty(type)){
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getFileName(downloadPath));
        } else {
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getFileName(downloadPath) + type);
        }

        //设置在下载中和下载完成的时候，通知栏都显示下载进度
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(downloadPath);
        request.setDescription("网络内容下载");
        ext = dManager.enqueue(request);
    }

    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/"), url.length());
    }

    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadCompletedId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

            if (ext != downloadCompletedId) {// 检查是否是自己的下载队列 id, 有可能是其他应用的
                return;
            }
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                DownloadManager.Query query = new DownloadManager.Query();
                //在广播中取出下载任务的id
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                query.setFilterById(id);
                Cursor c = manager.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int state = c.getInt(columnIndex);
                    switch (state) {
                        // 判断下载状态
                        case DownloadManager.STATUS_SUCCESSFUL: {
                            //获取文件下载路径
                            String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                            DebugUtil.i("downloadSuFilename", filename);
                            File file = new File(filename);
                            Intent intentFile = getFileIntent(file);
                            startActivity(intentFile);
                        }
                        break;
                        case DownloadManager.STATUS_FAILED:
                            ToastUtil.showToast(mContext,"下载失败，请检查网络",Toast.LENGTH_LONG);
                            break;
                        case DownloadManager.STATUS_PAUSED:
                            ToastUtil.showToast(mContext,"下载暂停",Toast.LENGTH_LONG);
                            break;
                        case DownloadManager.STATUS_PENDING:
                            //Log.i(TAG, "onReceive: pending");
                            break;
                        case DownloadManager.STATUS_RUNNING:
                            //Log.i(TAG, "onReceive: running");
                            break;
                        default:
                            break;

                    }

                }
                c.close();
            }


        }
    };

    public Intent getFileIntent(File file) {
        Uri uri = Uri.fromFile(file);
        String type = DownLoadFile.getMIMEType(file);
        DebugUtil.i("downloadSuType", type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadCompleteReceiver);
    }

}
