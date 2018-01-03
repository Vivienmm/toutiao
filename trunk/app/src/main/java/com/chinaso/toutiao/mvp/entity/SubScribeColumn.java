package com.chinaso.toutiao.mvp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chinaso on 2017/3/6.
 */

public class SubScribeColumn {

    /**
     * name : 我的
     * columns : [{"column_name":"时间","isSubscribe":"true","abstract":"教给你如何管理时间"},{"column_name":"豆瓣一刻","isSubscribe":"true","abstract":"豆瓣每日内容精选"}]
     */

    private String name;
    private List<ColumnsBean> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnsBean> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnsBean> columns) {
        this.columns = columns;
    }

    public static class ColumnsBean {
        /**
         * column_name : 时间
         * isSubscribe : true
         * abstract : 教给你如何管理时间
         */

        private String column_name;
        private Boolean isSubscribe;
        @SerializedName("abstract")
        private String abstractX;

        public String getColumn_name() {
            return column_name;
        }

        public void setColumn_name(String column_name) {
            this.column_name = column_name;
        }

        public Boolean getIsSubscribe() {
            return isSubscribe;
        }

        public void setIsSubscribe(Boolean isSubscribe) {
            this.isSubscribe = isSubscribe;
        }

        public String getAbstractX() {
            return abstractX;
        }

        public void setAbstractX(String abstractX) {
            this.abstractX = abstractX;
        }
    }
}
