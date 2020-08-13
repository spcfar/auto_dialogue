package com.hnsh.dialogue.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.UUID;

/**
 * @author lingu
 * @create 2019/11/28 17:54
 * @Describe
 */
public class UUIDUtils {
    public static String initUUID(Context context) {
        String uuid = SharedPreUtils.getString(context,"dialoguedemo_device_id","");
        if (TextUtils.isEmpty(uuid)) {
            uuid = getUUID();
            Log.d("UUIDUtils","uuid:"+uuid);
            SharedPreUtils.saveString(context,"dialoguedemo_device_id",uuid);
        }

        return uuid;
    }

    private static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }
}
