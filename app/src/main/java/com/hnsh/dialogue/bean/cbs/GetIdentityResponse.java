package com.hnsh.dialogue.bean.cbs;

/**
 * @项目名： BizInspection
 * @包名： com.dosmono.common.entity
 * @文件名: GetIdentityResponse
 * @创建者: zer
 * @创建时间: 2018/7/12 9:57
 * @描述： TODO
 */
public class GetIdentityResponse {

    private String deviceId;
    private String identity;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "GetIdentityResponse{" +
                "deviceId='" + deviceId + '\'' +
                ", identity='" + identity + '\'' +
                '}';
    }

}
