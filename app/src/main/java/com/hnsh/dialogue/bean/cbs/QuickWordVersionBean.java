package com.hnsh.dialogue.bean.cbs;

/**
 * 常用语（包含快捷用语和常用问答）的版本信息
 */
public class QuickWordVersionBean {

    /**
     * usedQVersion : v1.0  快捷用语 版本
     * quickQVersion : V1.0 常用问答 版本
     * continuousQVersion : V2.5
     * uiVersion : v1.4
     */

    private String usedQVersion;
    private String quickQVersion;
    private String continuousQVersion;
    private String uiVersion;
    private Long channelId;

    public String getUsedQVersion() {
        return usedQVersion;
    }

    public void setUsedQVersion(String usedQVersion) {
        this.usedQVersion = usedQVersion;
    }

    public String getQuickQVersion() {
        return quickQVersion;
    }

    public void setQuickQVersion(String quickQVersion) {
        this.quickQVersion = quickQVersion;
    }

    public String getContinuousQVersion() {
        return continuousQVersion;
    }

    public void setContinuousQVersion(String continuousQVersion) {
        this.continuousQVersion = continuousQVersion;
    }

    public String getUiVersion() {
        return uiVersion;
    }

    public void setUiVersion(String uiVersion) {
        this.uiVersion = uiVersion;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "QuickWordVersionBean{" +
                "usedQVersion='" + usedQVersion + '\'' +
                ", quickQVersion='" + quickQVersion + '\'' +
                ", continuousQVersion='" + continuousQVersion + '\'' +
                ", uiVersion='" + uiVersion + '\'' +
                ", channelId=" + channelId +
                '}';
    }
}
