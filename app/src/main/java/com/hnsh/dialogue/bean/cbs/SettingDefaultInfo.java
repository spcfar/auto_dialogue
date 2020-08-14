package com.hnsh.dialogue.bean.cbs;

/**
 * @项目名： Translator
 * @包名： com.dosmono.common.entity
 * @文件名: SettingDefaultInfo
 * @创建者: Administrator
 * @创建时间: 2018/3/1 001 15:48
 * @描述： TODO
 */

public class SettingDefaultInfo {

    private int usePattern;
    private int systemLanguage;
    private int workerLanguage;

    public int getUsePattern() {
        return usePattern;
    }

    public void setUsePattern(int usePattern) {
        this.usePattern = usePattern;
    }

    public int getSystemLanguage() {
        return systemLanguage;
    }

    public void setSystemLanguage(int systemLanguage) {
        this.systemLanguage = systemLanguage;
    }

    public int getWorkerLanguage() {
        return workerLanguage;
    }

    public void setWorkerLanguage(int workerLanguage) {
        this.workerLanguage = workerLanguage;
    }
}
