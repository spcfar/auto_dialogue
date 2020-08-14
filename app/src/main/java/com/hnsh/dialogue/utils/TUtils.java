package com.hnsh.dialogue.utils;

import android.widget.Toast;

/**
 * @项目名： Translator
 * @包名： com.dosmono.settings.util
 * @文件名: TUtils
 * @创建者: Administrator
 * @创建时间: 2018/3/1 001 13:47
 * @描述： TODO
 */

public class TUtils {

    private static Toast toast;

    public static void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(UIUtils.getContext(), msg, Toast.LENGTH_SHORT);
        }

        toast.setText(msg);
        toast.show();
    }

    public static void cancelToast(){
        if(toast != null){
            toast.cancel();
        }
    }

}
