package com.hnsh.dialogue.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 翻译信息实体类
 */

@Entity(nameInDb = "TranslationInfo")
public class TranslationInfoBean implements Parcelable {


    @Id(autoincrement = true)
    private Long id;
    @Unique
    private long traId;
    private long qId;
    private long laId;
    private String content;


    protected TranslationInfoBean(Parcel in) {
        traId = in.readLong();
        qId = in.readLong();
        laId = in.readLong();
        content = in.readString();
    }

    @Generated(hash = 32098580)
    public TranslationInfoBean(Long id, long traId, long qId, long laId, String content) {
        this.id = id;
        this.traId = traId;
        this.qId = qId;
        this.laId = laId;
        this.content = content;
    }

    @Generated(hash = 1819547370)
    public TranslationInfoBean() {
    }

    public static final Creator<TranslationInfoBean> CREATOR = new Creator<TranslationInfoBean>() {
        @Override
        public TranslationInfoBean createFromParcel(Parcel in) {
            return new TranslationInfoBean(in);
        }

        @Override
        public TranslationInfoBean[] newArray(int size) {
            return new TranslationInfoBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(traId);
        dest.writeLong(qId);
        dest.writeLong(laId);
        dest.writeString(content);
    }

    public long getTraId() {
        return traId;
    }

    public void setTraId(long traId) {
        this.traId = traId;
    }

    public long getQId() {
        return qId;
    }

    public void setQId(long qId) {
        this.qId = qId;
    }

    public long getLaId() {
        return laId;
    }

    public void setLaId(long laId) {
        this.laId = laId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TranslationInfoBean{" +
                "id=" + id +
                ", traId=" + traId +
                ", qId=" + qId +
                ", laId=" + laId +
                ", content='" + content + '\'' +
                '}';
    }
}
