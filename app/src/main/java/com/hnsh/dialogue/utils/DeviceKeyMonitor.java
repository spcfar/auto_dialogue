package com.hnsh.dialogue.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

import com.dosmono.logger.Logger;

/**
 * @author lingu
 * @create 2020/5/12 14:13
 * @Describe
 */
public class DeviceKeyMonitor {


    /**
     * 参数
     */

    private static final String F1_KEY_DOWN = "KEYCODE_F1_DOWN";
    private static final String F2_KEY_DOWN= "KEYCODE_F2_DOWN";
    private static final String F1_KEY_UP= "KEYCODE_F1_UP";
    private static final String F2_KEY_UP= "KEYCODE_F2_UP";

    private Context mContext;
    private BroadcastReceiver mDeviceKeyReceiver = null;
    private OnKeyListener mListener;

    public DeviceKeyMonitor(Context context, OnKeyListener listener) {
        mContext = context;
        mListener = listener;
        mDeviceKeyReceiver = new BroadcastReceiver() {
            long startDownTime=-1;
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent!= null){
                    String action =intent.getAction();
                    switch (action){
                        case F1_KEY_DOWN:
                        case F2_KEY_DOWN:
                            if(startDownTime < 0){
                                startDownTime = SystemClock.elapsedRealtime();
                                Logger.d("startDownTime:"+startDownTime);
                            }


                             break ;
                        case F1_KEY_UP:
                            long endATime = SystemClock.elapsedRealtime() - startDownTime;
                            Logger.d( SystemClock.elapsedRealtime() +"endATime："+endATime);
                            if(endATime > 3000){

                                mListener.onAKeyLongClick();
                            }else {
                                mListener.onAKeyClick();
                            }
                            startDownTime=-1;
                            break ;
                        case F2_KEY_UP:
                            long endBTime = SystemClock.elapsedRealtime() - startDownTime;
                            Logger.d("endBTime："+endBTime);
                            if(endBTime > 3000){
                                mListener.onBKeyLongClick();
                            }else {
                                mListener.onBKeyClick();
                            }
                            startDownTime=-1;
                            break ;

                            default:
                                break;
                    }
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(F1_KEY_DOWN);
        intentFilter.addAction(F2_KEY_DOWN);
        intentFilter.addAction(F1_KEY_UP);
        intentFilter.addAction(F2_KEY_UP);
        mContext.registerReceiver(mDeviceKeyReceiver, intentFilter);
    }

    public interface OnKeyListener{
        void onAKeyClick();
        void onAKeyLongClick();
        void onBKeyClick();
        void onBKeyLongClick();
    }

    public void unregister(){
        if (mDeviceKeyReceiver != null){
            mContext.unregisterReceiver(mDeviceKeyReceiver);
            mDeviceKeyReceiver = null;
        }
    }


}
