package com.hnsh.dialogue.bean.cbs;

/**
 * @项目名： Translator
 * @包名： com.dosmono.common.entity
 * @文件名: ConfirmBindingReq
 * @创建者: Administrator
 * @创建时间: 2018/3/1 001 16:39
 * @描述： TODO
 */

public class ConfirmBindingReq {
    private String deviceId;
    private String invitationCode;
    private String isMaster;
    private int type;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(String isMaster) {
        this.isMaster = isMaster;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
