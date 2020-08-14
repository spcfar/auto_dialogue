package com.hnsh.dialogue.bean.cbs;

public class BaseRequest {
    private String msgId;
    private String gid;
    private String deviceId;
    private String receiveDeviceid;

    private long sendTime;
    private long synchTimeout;

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getReceiveDeviceid() {
        return receiveDeviceid;
    }

    public void setReceiveDeviceid(String receiveDeviceid) {
        this.receiveDeviceid = receiveDeviceid;
    }

    public long getSynchTimeout() {
        return synchTimeout;
    }

    public void setSynchTimeout(long synchTimeout) {
        this.synchTimeout = synchTimeout;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "msgId='" + msgId + '\'' +
                ", gid='" + gid + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", receiveDeviceid='" + receiveDeviceid + '\'' +
                ", sendTime=" + sendTime +
                ", synchTimeout=" + synchTimeout +
                '}';
    }
}
