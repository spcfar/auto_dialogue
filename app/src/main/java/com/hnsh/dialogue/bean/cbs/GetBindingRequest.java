package com.hnsh.dialogue.bean.cbs;

/**
 * Created by <Yang Tao> on <18/3/1>.
 */

public class GetBindingRequest {

    private String deviceId;

    public GetBindingRequest() {

    }

    public GetBindingRequest(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
