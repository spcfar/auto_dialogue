package com.hnsh.dialogue.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.PowerManager;
import com.dosmono.logger.Logger;
import com.hnsh.dialogue.R;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.Context.KEYGUARD_SERVICE;

public class ActivityUtil {

    private static volatile boolean SCREENOFF_FLAG = false;

    public static boolean isScreenoffFlag() {
        return SCREENOFF_FLAG;
    }

    public static void setScreenoffFlag(boolean screenoffFlag) {
        SCREENOFF_FLAG = screenoffFlag;
    }

    public static String getTopActivity(Context mContext) {
        ActivityManager mAm = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskInfoList = mAm.getRunningTasks(1);
        String activity_name = mContext.getString(R.string.home_page);
        if (taskInfoList.size() > 0) {

            activity_name = taskInfoList.get(0).topActivity.getClassName();
        }
        Logger.d("getTopActivity" + activity_name);
        return activity_name;
    }

    public static String getRunningActivity() {
        Activity activity = AppManager.getAppManager().getCurrentActivitiy();
        if (activity != null) {
            return activity.getComponentName().getClassName();
        }
        Logger.d("getRunningActivity -> activity == null");
        return getTopActivity(UIUtils.getContext());
    }


    public static String getTopActivityApplication(Context mContext) {
        ActivityManager mAm = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = "";
        List<RunningTaskInfo> taskInfoList = mAm.getRunningTasks(1);
        if (taskInfoList.size() > 0)
            packageName = taskInfoList.get(0).topActivity.getPackageName();
        else {
            packageName = getTopRunningApp(mContext).getPackageName();
        }
        return packageName;
    }

    /**
     * ???????????????????????????
     */
    public static void wakeUpAndUnlock(Context mContext) {
        // ???????????????????????????
        PowerManager pm = (PowerManager) mContext
                .getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // ??????PowerManager.WakeLock??????,???????????????|???????????????????????????,????????????LogCat?????????Tag
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "app:bright");
            wl.acquire(10000); // ????????????
            wl.release(); // ??????
        }
        // ????????????
        KeyguardManager keyguardManager = (KeyguardManager) mContext
                .getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // ????????????
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // ??????
    }


    /**
     * ?????? ????????????????????????app
     *
     * @param mContext
     * @return
     */
    public static UsageStats getTopRunningApp(Context mContext) {
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) mContext.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        //time - 1000 * 1000, time ??????????????????????????????????????????????????????????????? ????????????Activity ??????
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 60, time);
        // Sort the stats by the last time used
        String topPackageName = "";
        UsageStats recentStats = null;
        if (stats != null) {

            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {

                    //mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                    recentStats = usageStats;
                }
            }
//            if (recentStats != null) {
//                topPackageName = recentStats.getPackageName();
//            }
//            if (mySortedMap != null && !mySortedMap.isEmpty()) {
//                topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
//                Logger.e("TopPackage Name", topPackageName);
//            }
        }
        return recentStats;
    }

}
