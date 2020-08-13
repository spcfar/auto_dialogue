package com.hnsh.dialogue.utils;

import android.annotation.SuppressLint;
import android.os.Build;

public class CommonUtil {
    @SuppressLint("MissingPermission")
    public static String getDeviceId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        }
        return  Build.SERIAL;
    }
}
