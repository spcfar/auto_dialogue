package com.hnsh.dialogue.utils;

/**
 * Created by Administrator on 2017/6/22.
 */

public class EventMessage {


    private String msgcode;

    private Object content;

    public String getMsgcode() {
        return msgcode;
    }

    public EventMessage(String code, Object content){
        this.msgcode = code;
        this.content = content;
    }

    public void setMsgcode(String msgcode) {
        this.msgcode = msgcode;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
