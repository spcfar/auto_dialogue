package com.hnsh.dialogue.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import com.dosmono.logger.Logger;
import com.dosmono.universal.common.Constant;

import java.io.File;
import java.io.Serializable;

public class BroadcastsUtil {
    private static final String TAG = "BroadcastsUtil";

    public static final String EXTRA_ACTION_UPGRADE_ABLE  = "extra_action_upgrade_able";//有升级可用更新
    public static final String EXTRA_ACTION_CLEANUP = "extra_action_cleanup";//一键清除广播
    public static final String EXTRA_ACTION_DIALOG_NOTIFY = "com.dosmono.biz.notifydialogbroadcast";
    public static final String EXTRA_ACTION_LAUNCHER_NOTIFY = "com.dosmono.biz.notifylauncherbroadcast";

    public static void sendBoastcasts(String action, Context context) {
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }

    public static void sendBoastcasts(String action, Context context, String key, int value) {
        Intent intent = new Intent(action);
        intent.putExtra(key, value);
        context.sendBroadcast(intent);
    }

    public static void sendLocalBoastcasts(String action, Context context) {
        Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendBoastcasts(String action, Context context, String key, Serializable value) {
        Intent intent = new Intent(action);
        intent.putExtra(key, value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendBoastcasts(String action, Context context, String key, String value) {
        Intent intent = new Intent(action);
        intent.putExtra(key, value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    public static void sendBoastcasts1(String action, Context context, String[] keys, String[] values) {
        Intent intent = new Intent(action);
        for (int i = 0; i < values.length; i++) {
            intent.putExtra(keys[i], values[i]);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**刷新存储目录广播*/
    public static void sendStorageRefreshBroacast(Context context) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(Constant.ROOT_PATH));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**刷新存储目录广播(指定路径)*/
    public static void sendStorageRefreshBroacast(Context context, String filePath){
        Logger.i("send broadcast refresh storage: ");
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

}
