package com.hnsh.dialogue.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 快捷用语实体类
 *
 */
@Entity(nameInDb = "QuestionInfo")
public class QuestionInfoBean implements Parcelable {

    @Id(autoincrement = true)
    private Long id;
    @Unique
    private long qId;
    private long typeId;
    private String content;
    private int sort;


    protected QuestionInfoBean(Parcel in) {
        qId = in.readInt();
        typeId = in.readInt();
        content = in.readString();
        sort = in.readInt();
    }

    @Generated(hash = 1857155928)
    public QuestionInfoBean(Long id, long qId, long typeId, String content, int sort) {
        this.id = id;
        this.qId = qId;
        this.typeId = typeId;
        this.content = content;
        this.sort = sort;
    }

    @Generated(hash = 1758332723)
    public QuestionInfoBean() {
    }

    public static final Creator<QuestionInfoBean> CREATOR = new Creator<QuestionInfoBean>() {
        @Override
        public QuestionInfoBean createFromParcel(Parcel in) {
            return new QuestionInfoBean(in);
        }

        @Override
        public QuestionInfoBean[] newArray(int size) {
            return new QuestionInfoBean[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(qId);
        dest.writeLong(typeId);
        dest.writeString(content);
        dest.writeInt(sort);
    }

    public Long getId() {
        return id;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getQId() {
        return this.qId;
    }

    public void setQId(long qId) {
        this.qId = qId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "QuestionInfoBean{" +
                "id=" + id +
                ", qId=" + qId +
                ", typeId=" + typeId +
                ", content='" + content + '\'' +
                ", sort=" + sort +
                '}';
    }
}
