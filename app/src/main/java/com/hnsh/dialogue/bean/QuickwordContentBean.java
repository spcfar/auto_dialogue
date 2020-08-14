package com.hnsh.dialogue.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class QuickwordContentBean implements Parcelable {

    private long id;
    private String content;
    private boolean isQA;

    protected QuickwordContentBean(Parcel in) {
        id = in.readLong();
        content = in.readString();
        isQA = in.readByte() != 0;
    }

    public QuickwordContentBean(long id, String content, boolean isQA) {
        this.id = id;
        this.content = content;
        this.isQA = isQA;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isQA() {
        return isQA;
    }

    public void setQA(boolean QA) {
        isQA = QA;
    }

    public static final Creator<QuickwordContentBean> CREATOR = new Creator<QuickwordContentBean>() {
        @Override
        public QuickwordContentBean createFromParcel(Parcel in) {
            return new QuickwordContentBean(in);
        }

        @Override
        public QuickwordContentBean[] newArray(int size) {
            return new QuickwordContentBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(content);
        dest.writeByte((byte) (isQA ? 1 : 0));
    }


}
