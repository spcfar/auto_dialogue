package com.hnsh.dialogue.bean.cbs;

public class BindIdentity {

    private String deviceId;
    private String identity;//0表示客户，1表示工作人员
    private String deviceIdOther;
    private String identityOther;//0表示客户，1表示工作人员

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

    public String getDeviceIdOther() {
        return deviceIdOther;
    }

    public void setDeviceIdOther(String deviceIdOther) {
        this.deviceIdOther = deviceIdOther;
    }

    public String getIdentityOther() {
        return identityOther;
    }

    public void setIdentityOther(String identityOther) {
        this.identityOther = identityOther;
    }

    @Override
    public String toString() {
        return "BindIdentity{" +
                "deviceId='" + deviceId + '\'' +
                ", identity='" + identity + '\'' +
                ", deviceIdOther='" + deviceIdOther + '\'' +
                ", identityOther='" + identityOther + '\'' +
                '}';
    }
}
