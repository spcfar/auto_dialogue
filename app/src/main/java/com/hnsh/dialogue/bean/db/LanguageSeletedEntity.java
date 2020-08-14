package com.hnsh.dialogue.bean.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @项目名： Translator
 * @包名： com.dosmono.common.entity
 * @文件名: LanguageSeletedEntity
 * @创建者: Administrator
 * @创建时间: 2018/3/19 019 10:19
 * @描述： TODO
 */

@Entity
public class LanguageSeletedEntity {
    @Id(autoincrement = true)
    private Long id;
    @Unique
    private int   lanId;
    private int count;

    private long lastClickTime;

    @Generated(hash = 847171824)
    public LanguageSeletedEntity(Long id, int lanId, int count,
                                 long lastClickTime) {
        this.id = id;
        this.lanId = lanId;
        this.count = count;
        this.lastClickTime = lastClickTime;
    }
    @Generated(hash = 760343817)
    public LanguageSeletedEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getLanId() {
        return this.lanId;
    }
    public void setLanId(int lanId) {
        this.lanId = lanId;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public long getLastClickTime() {
        return this.lastClickTime;
    }
    public void setLastClickTime(long lastClickTime) {
        this.lastClickTime = lastClickTime;
    }
}
