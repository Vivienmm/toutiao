package com.chinaso.toutiao.mvp.listener;

/**
 * Created by chinaso on 2017/3/2.
 */

public class UpdateTextSizeEvent {

    private String textSize;

    public UpdateTextSizeEvent(String textSize) {
        this.textSize = textSize;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }
}
