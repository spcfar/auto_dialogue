package com.hnsh.dialogue.bean;

/**
 * @项目名： BIZ_Project
 * @包名： com.dosmono.common.entity.wifi
 * @文件名: WifiEncriptEntity
 * @创建者: zer
 * @创建时间: 2019/3/11 10:25
 * @描述： TODO
 */
public class WifiEncriptEntity {
    private int encription;
    private String name;

    public int getEncription() {
        return encription;
    }

    public void setEncription(int encription) {
        this.encription = encription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WifiEncriptEntity{" +
                "encription=" + encription +
                ", name='" + name + '\'' +
                '}';
    }
}
