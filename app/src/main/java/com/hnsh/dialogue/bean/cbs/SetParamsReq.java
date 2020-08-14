package com.hnsh.dialogue.bean.cbs;

public class SetParamsReq extends BaseRequest{

    private String fontSize;
    private String voiceSwitch;

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

    @Override
    public String toString() {
        return "SetParamsReq{" +
                "fontSize='" + fontSize + '\'' +
                ", voiceSwitch='" + voiceSwitch + '\'' +
                '}';
    }
}
