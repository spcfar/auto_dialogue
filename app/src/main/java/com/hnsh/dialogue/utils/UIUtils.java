package com.hnsh.dialogue.utils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * @项目名： dosmono_travel
 * @包名： com.dosmono.information.util
 * @文件名: UIUtils
 * @创建者: zer
 * @创建时间: 2018/5/7 17:42
 * @描述： TODO
 */
public class UIUtils {

    private static Toast mToast;
    private static Application mContext;

    public static void showToast(Context context, String msg) {
        showToast(context,msg, Toast.LENGTH_SHORT);
    }

    private static void showToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, "", duration);
        }
        mToast.setText(msg);
        mToast.show();
    }

    public static int dip2Px(Context context, int dip) {
        // px/dip = density;
        // density = dpi/160
        // 320*480 density = 1 1px = 1dp
        // 1280*720 density = 2 2px = 1dp

        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    public static int px2dip(Context context, int px) {

        float density = context.getResources().getDisplayMetrics().density;
        int dip = (int) (px / density + 0.5f);
        return dip;
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static Context getContext(){
        return mContext;
    }

    public static void setContext(Application context){
        mContext = context;
    }




}
