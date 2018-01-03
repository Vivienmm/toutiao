package com.chinaso.toutiao.util;

import com.chinaso.toutiao.app.Constants;

public class TimeControlUtil {

    public static long lastClickTime = 0;

    public static boolean isFastClick() {
        if ((System.currentTimeMillis() - lastClickTime > Constants.MIN_TIME_BETWEEN_CLICK)) {
            lastClickTime = System.currentTimeMillis();
            return false;
        }
        else
            return true;
    }
}
