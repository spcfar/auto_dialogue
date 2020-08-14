package com.hnsh.dialogue.bean;

import java.io.Serializable;

/**
 * @author lingu
 * @create 2020/6/30 18:08
 * @Describe
 */
public class ThirdApkInfo implements Serializable {
    public String apkCom;
    public String apkName;
    public String apkUrl;
    public String hash;
    public int newVersion;
    public String patchInfo;
    public String size;
    public String apkPath;

    @Override
    public String toString() {
        return "ThirdApkInfo{" +
                "apkCom='" + apkCom + '\'' +
                ", apkName='" + apkName + '\'' +
                ", apkUrl='" + apkUrl + '\'' +
                ", hash='" + hash + '\'' +
                ", newVersion='" + newVersion + '\'' +
                ", patchInfo='" + patchInfo + '\'' +
                ", apkPath='" + apkPath + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
