package com.hnsh.dialogue.bean.db;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * 分类信息实体类
 */
@Entity(nameInDb = "TypeInfo")
public class TypeInfoBean implements Parcelable {


    @Id(autoincrement = true)
    private Long id;
    private String typeName;
    @Unique
    private long typeId;
    private long typePid;
    private int typeLevel;
    private int sort;
    @Transient
    private List<TypeInfoBean> next;
    @Transient
    private boolean isSelected = false;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    protected TypeInfoBean(Parcel in) {
        id = in.readLong();
        typeName = in.readString();
        typeId = in.readLong();
        typePid = in.readLong();
        typeLevel = in.readInt();
        sort = in.readInt();
    }

    @Generated(hash = 1403055888)
    public TypeInfoBean(Long id, String typeName, long typeId, long typePid, int typeLevel,
                        int sort) {
        this.id = id;
        this.typeName = typeName;
        this.typeId = typeId;
        this.typePid = typePid;
        this.typeLevel = typeLevel;
        this.sort = sort;
    }

    @Generated(hash = 1064542750)
    public TypeInfoBean() {
    }

    public static final Creator<TypeInfoBean> CREATOR = new Creator<TypeInfoBean>() {
        @Override
        public TypeInfoBean createFromParcel(Parcel in) {
            return new TypeInfoBean(in);
        }

        @Override
        public TypeInfoBean[] newArray(int size) {
            return new TypeInfoBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(typeName);
        dest.writeLong(typeId);
        dest.writeLong(typePid);
        dest.writeInt(typeLevel);
        dest.writeInt(sort);
    }

    public Long getId() {
        return id;
    }


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getTypePid() {
        return typePid;
    }

    public void setTypePid(long typePid) {
        this.typePid = typePid;
    }

    public int getTypeLevel() {
        return typeLevel;
    }

    public void setTypeLevel(int typeLevel) {
        this.typeLevel = typeLevel;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<TypeInfoBean> getNext() {
        return next;
    }

    public void setNext(List<TypeInfoBean> next) {
        this.next = next;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
