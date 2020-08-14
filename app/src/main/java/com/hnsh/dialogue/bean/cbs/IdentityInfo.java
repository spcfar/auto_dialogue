package com.hnsh.dialogue.bean.cbs;

/**
 * Created by <Yang Tao> on <18/3/1>.
 */

public class IdentityInfo {

    private String deviceId;
    private String identity;
    private int	lang;
    private int type = 1;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }
}
