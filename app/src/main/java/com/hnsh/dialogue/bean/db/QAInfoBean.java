package com.hnsh.dialogue.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity(nameInDb = "QAInfo")
public class QAInfoBean implements Parcelable {


    /**
     * id : 1043
     * content : 您要咨询哪方面的问题？
     * isQuestionOrAnswer : 0
     * linkId : 0
     * linkUpId : null
     * languageId : 0
     * typeId : 247
     * sortId : 0
     */
    @Id(autoincrement = true)
    private Long realId;
    @Unique
    private long id;
    private String content;
    private int isQuestionOrAnswer;
    private long linkId;
    private long linkUpId;
    private long languageId;
    private long typeId;
    private int sortId;

    protected QAInfoBean(Parcel in) {
        if (in.readByte() == 0) {
            realId = null;
        } else {
            realId = in.readLong();
        }
        id = in.readLong();
        content = in.readString();
        isQuestionOrAnswer = in.readInt();
        linkId = in.readLong();
        linkUpId = in.readLong();
        languageId = in.readLong();
        typeId = in.readLong();
        sortId = in.readInt();
    }

    @Generated(hash = 1961050386)
    public QAInfoBean(Long realId, long id, String content, int isQuestionOrAnswer,
                      long linkId, long linkUpId, long languageId, long typeId, int sortId) {
        this.realId = realId;
        this.id = id;
        this.content = content;
        this.isQuestionOrAnswer = isQuestionOrAnswer;
        this.linkId = linkId;
        this.linkUpId = linkUpId;
        this.languageId = languageId;
        this.typeId = typeId;
        this.sortId = sortId;
    }

    @Generated(hash = 2075358235)
    public QAInfoBean() {
    }

    public static final Creator<QAInfoBean> CREATOR = new Creator<QAInfoBean>() {
        @Override
        public QAInfoBean createFromParcel(Parcel in) {
            return new QAInfoBean(in);
        }

        @Override
        public QAInfoBean[] newArray(int size) {
            return new QAInfoBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (realId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(realId);
        }
        dest.writeLong(id);
        dest.writeString(content);
        dest.writeInt(isQuestionOrAnswer);
        dest.writeLong(linkId);
        dest.writeLong(linkUpId);
        dest.writeLong(languageId);
        dest.writeLong(typeId);
        dest.writeInt(sortId);
    }

    public Long getRealId() {
        return realId;
    }

    public void setRealId(Long realId) {
        this.realId = realId;
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

    public int getIsQuestionOrAnswer() {
        return isQuestionOrAnswer;
    }

    public void setIsQuestionOrAnswer(int isQuestionOrAnswer) {
        this.isQuestionOrAnswer = isQuestionOrAnswer;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public long getLinkUpId() {
        return linkUpId;
    }

    public void setLinkUpId(long linkUpId) {
        this.linkUpId = linkUpId;
    }

    public long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(long languageId) {
        this.languageId = languageId;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    @Override
    public String toString() {
        return "QAInfoBean{" +
                "realId=" + realId +
                ", id=" + id +
                ", content='" + content + '\'' +
                ", isQuestionOrAnswer=" + isQuestionOrAnswer +
                ", linkId=" + linkId +
                ", linkUpId=" + linkUpId +
                ", languageId=" + languageId +
                ", typeId=" + typeId +
                ", sortId=" + sortId +
                '}';
    }
}
