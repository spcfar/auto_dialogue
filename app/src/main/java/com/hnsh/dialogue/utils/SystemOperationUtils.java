package com.hnsh.dialogue.utils;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;

import com.dosmono.universal.thread.ExecutorManager;

public class SystemOperationUtils {

    private static Instrumentation instrumentation = new Instrumentation();

    public static void goBack() {


        ExecutorManager.Companion.build().getCommonThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void goBackTwoTimes() {
        ExecutorManager.Companion.build().getCommonThread().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    Thread.sleep(500);
                    instrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void shutdown() {
//        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
//        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        UIUtils.getContext().startActivity(intent);

        String action = "com.android.internal.intent.action.REQUEST_SHUTDOWN";
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            action = "android.intent.action.ACTION_REQUEST_SHUTDOWN";
        }
        Intent shutdown = new Intent(action);
        shutdown.putExtra("android.intent.extra.KEY_CONFIRM", false);
        shutdown.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            UIUtils.getContext().startActivity(shutdown);
//            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
