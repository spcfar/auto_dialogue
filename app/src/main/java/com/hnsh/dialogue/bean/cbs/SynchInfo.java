package com.hnsh.dialogue.bean.cbs;

import java.util.List;

public class SynchInfo extends BaseRequest {

    private List<String> msgList;

    public List<String> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<String> msgList) {
        this.msgList = msgList;
    }

    private String listToString() {
        StringBuffer sb = new StringBuffer();
        int count = msgList == null ? 0 : msgList.size();
        if(count > 0) {
            try {
                for(String item : msgList) {
                    sb.append(item).append(",");
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    @Override
    public String toString() {
        return "SynchInfo{" +
                super.toString() +
                "msgList=" + listToString() +
                '}';
    }
}
