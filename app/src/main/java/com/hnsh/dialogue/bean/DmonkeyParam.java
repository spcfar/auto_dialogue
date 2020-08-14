package com.hnsh.dialogue.bean;

import java.util.Map;

public class DmonkeyParam {


    private String sno;
    private String salt ;
    private String sign;
    private Map<String, String> content;

    public DmonkeyParam(String sno, String salt, String sign, Map<String, String> content) {
        this.sno = sno;
        this.salt = salt;
        this.sign = sign;
        this.content = content;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }
}
