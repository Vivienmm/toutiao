package com.chinaso.toutiao.app.entity.update;

public class VersionUpdateResponse {
    private Boolean update;              //友盟为String型
    private String new_version;
    private String apk_url;
    private String update_log;
    private Boolean delta;
    private String new_md5;
    private String target_size;
//    private String patch_md5;
//    private String size;

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public String getNew_version() {
        return new_version;
    }

    public void setNew_version(String new_version) {
        this.new_version = new_version;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getUpdate_log() {
        return update_log;
    }

    public void setUpdate_log(String update_log) {
        this.update_log = update_log;
    }

    public Boolean getDelta() {
        return delta;
    }

    public void setDelta(Boolean delta) {
        this.delta = delta;
    }

    public String getNew_md5() {
        return new_md5;
    }

    public void setNew_md5(String new_md5) {
        this.new_md5 = new_md5;
    }

    public String getTarget_size() {
        return target_size;
    }

    public void setTarget_size(String target_size) {
        this.target_size = target_size;
    }

//    public String getPatch_md5() {
//        return patch_md5;
//    }
//
//    public void setPatch_md5(String patch_md5) {
//        this.patch_md5 = patch_md5;
//    }
//
//    public String getSize() {
//        return size;
//    }
//
//    public void setSize(String size) {
//        this.size = size;
//    }
}
