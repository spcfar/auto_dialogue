package com.hnsh.dialogue.bean.cbs;

/**
 * Created by <Yang Tao> on <18/3/1>.
 */

public class GetCodeRequest {

    private String deviceId;
    private int type;

    public GetCodeRequest() {

    }

    public GetCodeRequest(String deviceId, int type) {
        this.deviceId = deviceId;
        this.type = type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
