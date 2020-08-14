package com.hnsh.dialogue.bean.cbs;

/**
 * Created by <Yang Tao> on <18/3/1>.
 */

public class BindingInfo {

    private String deviceIdOne; //	设备1
    private String deviceIdTwo; //	body	设备2
    private String masterDeviceId; //	body	工作人员设备
    private int	lang; //	body	工作人员语言
    private String gid; //	body	绑定id
    private int state;

    private int slavelang;//边检如果是-1，则表示会话结束，否则进入相应语种的聊天界面
    private String fontSize;//字体大小
    private String voiceSwitch;//声音开关

    public int getSlavelang() {
        return slavelang;
    }

    public void setSlavelang(int slavelang) {
        this.slavelang = slavelang;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getVoiceSwitch() {
        return voiceSwitch;
    }

    public void setVoiceSwitch(String voiceSwitch) {
        this.voiceSwitch = voiceSwitch;
    }

    public String getDeviceIdOne() {
        return deviceIdOne;
    }

    public void setDeviceIdOne(String deviceIdOne) {
        this.deviceIdOne = deviceIdOne;
    }

    public String getDeviceIdTwo() {
        return deviceIdTwo;
    }

    public void setDeviceIdTwo(String deviceIdTwo) {
        this.deviceIdTwo = deviceIdTwo;
    }

    public String getMasterDeviceId() {
        return masterDeviceId;
    }

    public void setMasterDeviceId(String masterDeviceId) {
        this.masterDeviceId = masterDeviceId;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "BindingInfo{" +
                "deviceIdOne='" + deviceIdOne + '\'' +
                ", deviceIdTwo='" + deviceIdTwo + '\'' +
                ", masterDeviceId='" + masterDeviceId + '\'' +
                ", lang=" + lang +
                '}';
    }
}
