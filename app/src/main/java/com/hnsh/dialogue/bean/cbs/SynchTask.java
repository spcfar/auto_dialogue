package com.hnsh.dialogue.bean.cbs;

import android.os.SystemClock;

public class SynchTask {

    private int sessionId;
    private String msgId;
    private long sendTime;
    private String query;
    private boolean send;

    public SynchTask() {
        this(-1, "", "");
    }

    public SynchTask(int sessionId, String msgId, String query) {
        this(sessionId, msgId, query, SystemClock.uptimeMillis());
    }

    public SynchTask(int sessionId, String msgId, String query, long sendTime) {
//        this.send = send;
        this.msgId = msgId;
        this.query = query;
        this.sessionId = sessionId;
        this.sendTime = sendTime;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    @Override
    public String toString() {
        return "SynchTask{" +
                "sessionId=" + sessionId +
                ", msgId='" + msgId + '\'' +
                ", sendTime=" + sendTime +
                ", query='" + query + '\'' +
                ", send=" + send +
                '}';
    }
}
