package com.hnsh.dialogue.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @项目名： BIZ
 * @包名： com.dosmono.common.entity
 * @文件名: AppInfo
 * @创建者: zer
 * @创建时间: 2018/9/21 9:19
 * @描述： TODO
 */
@Entity
public class AppInfo implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String packageName;
    private String name;
    private byte[]   icon;
    private String packagePath;
    private String versionName;
    private int      versionCode;
    private boolean  isSystem;
    private boolean  isToDesktop;

    public AppInfo(String packageName, String name, byte[] icon, String packagePath,
                   String versionName, int versionCode, boolean isSystem) {
        this.setName(name);
        this.setIcon(icon);
        this.setPackageName(packageName);
        this.setPackagePath(packagePath);
        this.setVersionName(versionName);
        this.setVersionCode(versionCode);
        this.setSystem(isSystem);
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(final byte[] icon) {
        this.icon = icon;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(final boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(final String packagePath) {
        this.packagePath = packagePath;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(final int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(final String versionName) {
        this.versionName = versionName;
    }

    @Generated(hash = 474970388)
    public AppInfo(Long id, String packageName, String name, byte[] icon, String packagePath,
                   String versionName, int versionCode, boolean isSystem, boolean isToDesktop) {
        this.id = id;
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
        this.packagePath = packagePath;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.isSystem = isSystem;
        this.isToDesktop = isToDesktop;
    }

    @Generated(hash = 1656151854)
    public AppInfo() {
    }

    @Override
    public String toString() {
        return "pkg name: " + getPackageName() +
                "\napp name: " + getName() +
                "\napp path: " + getPackagePath() +
                "\napp v name: " + getVersionName() +
                "\napp v code: " + getVersionCode() +
                "\nis system: " + isSystem();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsSystem() {
        return this.isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public boolean getIsToDesktop() {
        return this.isToDesktop;
    }

    public void setIsToDesktop(boolean isToDesktop) {
        this.isToDesktop = isToDesktop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.packageName);
        dest.writeString(this.name);
        dest.writeByteArray(this.icon);
        dest.writeString(this.packagePath);
        dest.writeString(this.versionName);
        dest.writeInt(this.versionCode);
        dest.writeByte(this.isSystem
                       ? (byte) 1
                       : (byte) 0);
        dest.writeByte(this.isToDesktop
                       ? (byte) 1
                       : (byte) 0);
    }

    protected AppInfo(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.packageName = in.readString();
        this.name = in.readString();
        this.icon = in.createByteArray();
        this.packagePath = in.readString();
        this.versionName = in.readString();
        this.versionCode = in.readInt();
        this.isSystem = in.readByte() != 0;
        this.isToDesktop = in.readByte() != 0;
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo(source);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };
}
