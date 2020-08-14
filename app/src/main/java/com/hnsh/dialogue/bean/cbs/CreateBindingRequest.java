package com.hnsh.dialogue.bean.cbs;

/**
 * Created by <Yang Tao> on <18/3/1>.
 */

public class CreateBindingRequest {

    private String deviceId;
    private String invitationCode;
    private int type;

    public CreateBindingRequest(){}

    public CreateBindingRequest(String deviceId, String invitationCode) {
        this.deviceId = deviceId;
        this.invitationCode = invitationCode;

    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
