package com.hnsh.dialogue.bean.cbs;

/**
 * @项目名： BizInspection
 * @包名： com.dosmono.common.entity
 * @文件名: AnswerBean
 * @创建者: zer
 * @创建时间: 2018/8/1 10:31
 * @描述： TODO
 */
public class AnswerBean {
    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String aid;
    private String text;

    public AnswerBean(){

    }

    public AnswerBean(String aid, String text) {
        this.aid = aid;
        this.text = text;
    }
}
