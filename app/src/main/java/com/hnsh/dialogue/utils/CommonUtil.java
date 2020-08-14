package com.hnsh.dialogue.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import com.dosmono.logger.Logger;

import java.util.regex.Pattern;

public class CommonUtil {
    @SuppressLint("MissingPermission")
    public static String getDeviceId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        }
        return  Build.SERIAL;
    }
    public static String getProductModel() {
        String serial = getDeviceId();
        return serial.substring(0, 3);
    }
    public static boolean isSymbol(String str) {
        if (null == str) {
            return false;
        }
        // 大小写不同：\\p 表示包含，\\P 表示不包含
        // \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
//		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        String regEx = "[\\p{P}‘’“”]"; // 参考：http://blog.csdn.net/u011939453/article/details/53301442
        Pattern pattern = Pattern.compile(regEx);
        return pattern.matcher(str.trim()).find();
    }
    /**
     * 版本号比较
     *
     * @param v1
     * @param v2
     * @return 0代表相等，1代表左边大，-1代表右边大 -2 错误
     * Utils.compareVersion("1.0.358_20180820090554","1.0.358_20180820090553")=1
     */
    public static int compareVersion(String v1, String v2) {
        try {
            if (v1.equals(v2)) {
                return 0;
            }
            String[] version1Array = v1.split("[._]");
            String[] version2Array = v2.split("[._]");
            if(version1Array.length != version2Array.length){
                Logger.d("last.length = "+v1.length()+",currentVersion.length = "+v2.length()+",版本号长度不一致，不进行升级");
                return -2;
            }
            int index = 0;
            int minLen = Math.min(version1Array.length, version2Array.length);
            long diff = 0;

            while (index < minLen
                    && (diff = Long.parseLong(version1Array[index])
                    - Long.parseLong(version2Array[index])) == 0) {
                index++;
            }
            if (diff == 0) {
                for (int i = index; i < version1Array.length; i++) {
                    if (Long.parseLong(version1Array[i]) > 0) {
                        return 1;
                    }
                }

                for (int i = index; i < version2Array.length; i++) {
                    if (Long.parseLong(version2Array[i]) > 0) {
                        return -1;
                    }
                }
                return 0;
            } else {
                return diff > 0 ? 1 : -1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    public static String getWireVersion(Context context) {
        return SystemPropertiesProxy.getString(context, "ro.build.display.id");
    }
}
