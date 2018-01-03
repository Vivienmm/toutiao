package com.chinaso.toutiao.mvp.listener;

public interface RequestCallBack<T> {
    void success(T data);

    void onError(String errorMsg);
}
