package com.chinaso.toutiao.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utils {

    private final static Pattern URL_PATTERN = Pattern
            .compile("^(https?|ftp|file)://.*");

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    public static String diffDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        long diff = 0;
        try {
            Date d = sdf.parse(date);
            diff = new Date().getTime() - d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long r;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        } else if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        } else if (diff > day) {
            r = (diff / day);
            return r + "天前";
        } else if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        } else if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        } else {
            return "刚刚";
        }
    }

    public static String stringFIFO(String parameter, String parameters) {
        LinkedList<String> mList = new LinkedList<>();
        String[] idArray = parameters.split(",");
        mList.clear();
        for (String id : idArray) {
            if (!TextUtils.isEmpty(id)) {
                mList.add(id);
            }
        }
        if (mList.contains(parameter)) {
            mList.remove(parameter);
        } else {
            if (mList.size() == 5) {
                mList.removeLast();
            }
        }
        mList.addFirst(parameter);

        StringBuilder resultSB = new StringBuilder();
        for (int i = 0; i < mList.size(); i++) {
            resultSB.append(mList.get(i));
            if (i != mList.size() - 1) {
                resultSB.append(",");
            }
        }
        return resultSB.toString();
    }
}
