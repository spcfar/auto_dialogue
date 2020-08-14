package com.hnsh.dialogue.bean.cbs;

public class SetLanguageReq extends BaseRequest {
    private int lang;

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "SetLanguageReq{" +
                "lang=" + lang +
                '}';
    }
}
