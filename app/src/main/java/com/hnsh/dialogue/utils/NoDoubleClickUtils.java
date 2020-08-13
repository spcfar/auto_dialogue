package com.hnsh.dialogue.utils;

/**
 * Created by acer- on 2017/7/30.
 * 防止多次点击
 */

public class NoDoubleClickUtils {
    private  static long lastClickTime=0;//上次点击的时间
    private  static int spaceTime = 500;//时间间隔
    public synchronized static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击
//        Logger.d("sss:"+(currentTime - lastClickTime));
        if(lastClickTime<=0){
            isAllowClick= false;
        }else {
            if ((currentTime - lastClickTime) > spaceTime) {
                isAllowClick= false;
            } else {
                //被多次点击
                isAllowClick = true;
            }
        }
        lastClickTime = currentTime;
//        Logger.d("isAllowClick:"+isAllowClick);
        return isAllowClick;
    }
}
