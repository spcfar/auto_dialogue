package com.hnsh.dialogue.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @author lingu
 * @create 2019/12/4 14:11
 * @Describe
 */
public class LanguageInfo implements Parcelable {

    public String flag;
    public int id;
    public String name;
    public String subname_zh;

    @Override
    public String toString() {
        return "LanguageInfo{" +
                "flag='" + flag + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", subname_zh='" + subname_zh + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.flag);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.subname_zh);
    }

    public LanguageInfo() {
    }

    protected LanguageInfo(Parcel in) {
        this.flag = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.subname_zh = in.readString();
    }

    public static final Creator<LanguageInfo> CREATOR = new Creator<LanguageInfo>() {
        @Override
        public LanguageInfo createFromParcel(Parcel source) {
            return new LanguageInfo(source);
        }

        @Override
        public LanguageInfo[] newArray(int size) {
            return new LanguageInfo[size];
        }
    };
}
