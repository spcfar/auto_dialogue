package com.hnsh.dialogue.bean.cbs;

import com.hnsh.dialogue.bean.db.QACategoryBean;

import java.util.List;

public class QAResultBean {


    /**
     * typeList : [{"typeId":78,"typeName":"快捷用语","haveNextlevel":0,"sort":3,"dataInfos":[],"typeDataInfos":null}]
     * version : v1.0
     */

    private String version;
    private List<QACategoryBean> typeList;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<QACategoryBean> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<QACategoryBean> typeList) {
        this.typeList = typeList;
    }
}
