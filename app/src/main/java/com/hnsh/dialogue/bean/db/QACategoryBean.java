package com.hnsh.dialogue.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

@Entity(nameInDb = "QACategory")
public class QACategoryBean implements Parcelable {


    /**
     * typeId : 247
     * typeName : 提示
     * haveNextlevel : 0
     * sort : 1
     * dataInfos : [{"id":1043,"content":"您要咨询哪方面的问题？","isQuestionOrAnswer":0,"linkId":0,"linkUpId":null,"languageId":0,"typeId":247,"sortId":0,"answerList":null}]
     */

    @Id(autoincrement = true)
    private Long id;
    @Unique
    private int typeId;
    private String typeName;
    private int haveNextlevel;
    private int sort;
    @Transient
    private List<QAInfoBean> dataInfos;
    @Transient
    private List<QACategoryBean> typeDataInfos;

    protected QACategoryBean(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        typeId = in.readInt();
        typeName = in.readString();
        haveNextlevel = in.readInt();
        sort = in.readInt();
        dataInfos = in.createTypedArrayList(QAInfoBean.CREATOR);
        typeDataInfos = in.createTypedArrayList(QACategoryBean.CREATOR);
    }

    @Generated(hash = 1067623473)
    public QACategoryBean(Long id, int typeId, String typeName, int haveNextlevel, int sort) {
        this.id = id;
        this.typeId = typeId;
        this.typeName = typeName;
        this.haveNextlevel = haveNextlevel;
        this.sort = sort;
    }

    @Generated(hash = 379609612)
    public QACategoryBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getHaveNextlevel() {
        return haveNextlevel;
    }

    public void setHaveNextlevel(int haveNextlevel) {
        this.haveNextlevel = haveNextlevel;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<QAInfoBean> getDataInfos() {
        return dataInfos;
    }

    public void setDataInfos(List<QAInfoBean> dataInfos) {
        this.dataInfos = dataInfos;
    }

    public List<QACategoryBean> getTypeDataInfos() {
        return typeDataInfos;
    }

    public void setTypeDataInfos(List<QACategoryBean> typeDataInfos) {
        this.typeDataInfos = typeDataInfos;
    }

    public static final Creator<QACategoryBean> CREATOR = new Creator<QACategoryBean>() {
        @Override
        public QACategoryBean createFromParcel(Parcel in) {
            return new QACategoryBean(in);
        }

        @Override
        public QACategoryBean[] newArray(int size) {
            return new QACategoryBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(typeId);
        dest.writeString(typeName);
        dest.writeInt(haveNextlevel);
        dest.writeInt(sort);
        dest.writeTypedList(dataInfos);
        dest.writeTypedList(typeDataInfos);
    }
}
