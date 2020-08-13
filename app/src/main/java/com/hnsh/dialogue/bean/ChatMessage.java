package com.hnsh.dialogue.bean;

import java.io.Serializable;

/**
 * @author lingu
 * @create 2019/12/3 17:00
 * @Describe
 */
public class ChatMessage implements Serializable {

    //0 left 1 right
    public int msgType;
    public String sourceMsg;
    public String translateMsg;
    public boolean inPlay;
    public String msgMp3;

    @Override
    public String toString() {
        return "ChatMessage{" +
                "msgType=" + msgType +
                ", sourceMsg='" + sourceMsg + '\'' +
                ", translateMsg='" + translateMsg + '\'' +
                ", inPlay=" + inPlay +
                ", msgMp3='" + msgMp3 + '\'' +
                '}';
    }
}
