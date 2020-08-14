package com.hnsh.dialogue.factory;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dosmono.logger.Logger;
import com.dosmono.universal.utils.MD5;
import com.hnsh.dialogue.bean.DmonkeyParam;
import com.hnsh.dialogue.constants.BIZConstants;
import com.hnsh.dialogue.utils.CommonUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class DmonkeyParamFactory {


    public static DmonkeyParam create(Map content) {
        long salt = System.currentTimeMillis();
//        String serial = CommonUtil.getDeviceId();
        // TODO 正式需要换回来呀
        String serial = "182152001000001";
        String sign = serial + salt + BIZConstants.Dmonkey.DEVICE_SECRET_KEY;
        sign = MD5.INSTANCE.md5(sign);
        return new DmonkeyParam(serial, String.valueOf(salt), sign, content);
    }

    public static JSONObject createJson(Map content){
        DmonkeyParam dmonkeyParam = create(content);
        return (JSONObject) JSON.toJSON(dmonkeyParam);
    }

    public static Map<String, Object> parameters(Map content){
        Map<String, Object> mapParam = new LinkedHashMap<>();
        DmonkeyParam dmonkeyParam = create(content);

        mapParam.put("sno",dmonkeyParam.getSno());
        mapParam.put("salt",dmonkeyParam.getSalt());
        mapParam.put("sign",dmonkeyParam.getSign());
        Logger.d("sno==="+dmonkeyParam.getSno());
        Logger.d("salt==="+dmonkeyParam.getSalt());
        Logger.d("sign==="+dmonkeyParam.getSign());
        mapParam.putAll(content);
        return mapParam;
    }


}
