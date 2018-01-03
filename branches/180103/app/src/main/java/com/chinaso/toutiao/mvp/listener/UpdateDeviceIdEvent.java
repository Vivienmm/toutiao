package com.chinaso.toutiao.mvp.listener;

public class UpdateDeviceIdEvent {
    private int deviceId;

    public UpdateDeviceIdEvent(int deviceId){
        this.deviceId=deviceId;
    }
    public int getdeviceId() {
        return deviceId;
    }

    public void setdeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
