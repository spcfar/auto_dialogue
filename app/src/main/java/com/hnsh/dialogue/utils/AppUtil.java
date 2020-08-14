package com.hnsh.dialogue.utils;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.dosmono.logger.Logger;
import com.hnsh.dialogue.bean.db.AppInfo;
import com.hnsh.dialogue.constants.BIZConstants;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * @项目名： BIZ
 * @包名： com.dosmono.common.utils
 * @文件名: AppUtil
 * @创建者: zer
 * @创建时间: 2018/9/21 9:18
 * @描述： TODO
 */
public class AppUtil {

    public static List<AppInfo> getAppsInfo(Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getBean(pm, pi);
            if (ai == null) continue;
            list.add(ai);
        }
        return list;
    }

    public static PackageInfo getAppInfo(Context context, @Nullable String packname) {
        if (packname == null || packname.isEmpty()) {
            packname = "com.dosmono.translator";
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo;
    }

    private static AppInfo getBean(final PackageManager pm, final PackageInfo pi) {
        if (pm == null || pi == null) return null;
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        byte[] icon = drawable2Bytes(ai.loadIcon(pm));
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
    }

    public static void launchApp(Context context, final String packageName) {
        if (isSpace(packageName)) return;
        //是否是禁用状态
        if (PrefsUtils.getPrefs(context, BIZConstants.Dmonkey.KEY_ENABLE_PRO + packageName, 0) == 1) {
            return;
        }
        context.startActivity(getLaunchAppIntent(context, packageName, true));
    }

    private static Intent getLaunchAppIntent(Context context, final String packageName, final boolean isNewTask) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) return null;
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static byte[] drawable2Bytes(final Drawable drawable) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), Bitmap.CompressFormat.PNG);
    }

    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;//true为安装了，false为未安装 } }

        }
    }

    private static String EXTRA_UPGRADE_TYPE = "upgradeType";
    private static String EXTRA_PATH = "packetPath";
    private static String EXTRA_OTA_TYPE = "OtaType";
    private static String EXTRA_PACKET_NAME = "package";
    private static String EXTRA_APP_VERSION = "AppVersion";

    public static void installApk(Context mContext, String version) {
        Intent intent = new Intent();
        ComponentName component = new ComponentName("com.dosmono.upgrade", "com.dosmono.upgrade.Launcher");
        intent.setComponent(component);
        intent.putExtra(EXTRA_UPGRADE_TYPE, 1);
        //  intent.putExtra(EXTRA_PATH, info.packetPath);
        intent.putExtra(EXTRA_PACKET_NAME, mContext.getPackageName());
        intent.putExtra(EXTRA_APP_VERSION, version);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("text", "text");
        mContext.startActivity(intent);
    }

    /**
     * 静默安装
     *
     * @param apkPath apk 路径
     * @param pName   包名
     * @return
     */
    public static boolean installApp(String apkPath, String pName) {
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder msg = new StringBuilder();
        try {
            process = new ProcessBuilder("pm", "install", "-i", pName, "-r", apkPath).start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                msg.append(s);
            }
        } catch (Exception e) {

        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {

            }
            if (process != null) {
                process.destroy();
            }
        }
        Logger.i("result:" + msg.toString() + " --> " + successMsg);
        //如果含有“success”单词则认为安装成功
        return successMsg.toString().
                equalsIgnoreCase("success");

    }

    /**
     * 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等 并且可以直接调用
     */
    public static PackageInfo doStartApplicationWithPackageName(Context context, String packagename) {

        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
            Logger.d("doStartApplicationWithPackageName：" + packageinfo + " code:" + packageinfo.versionCode + " name:" + packageinfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageinfo;
    }

    public static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);

            MessageDigest digester = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[8192];
            int byteCount;
            while ((byteCount = in.read(bytes)) > 0) {
                digester.update(bytes, 0, byteCount);
            }
            value = bytes2Hex(digester.digest());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length * 2];
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }

        return new String(res);
    }

    /**
     * 适配android9的安装方法。
     * 全部替换安装
     */
    private static String TAG = "dosmono";
    private static int mSessionId = -1;

    public static void installAppOnAndroidP(Context mContext, String apkFilePath, Class<?> cls) {
        Logger.d(TAG, "installApp()------->" + apkFilePath);
        File apkFile = new File(apkFilePath);
        if (!apkFile.exists()) {
            Logger.d(TAG, "文件不存在");
        }

        PackageInfo packageInfo = mContext.getPackageManager().getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        if (packageInfo != null) {
            String packageName = packageInfo.packageName;
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            Logger.d("ApkActivity", "packageName=" + packageName + ", versionCode=" + versionCode + ", versionName=" + versionName);
        }

        PackageInstaller packageInstaller = mContext.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams sessionParams
                = new PackageInstaller.SessionParams(PackageInstaller
                .SessionParams.MODE_FULL_INSTALL);
        Logger.d(TAG, "apkFile length" + apkFile.length());
        sessionParams.setSize(apkFile.length());

        try {
            mSessionId = packageInstaller.createSession(sessionParams);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.d(TAG, "sessionId---->" + mSessionId);
        if (mSessionId != -1) {
            boolean copySuccess = onTransfesApkFile(mContext, apkFilePath);
            Logger.d(TAG, "copySuccess---->" + copySuccess);
            if (copySuccess) {
                execInstallAPP(mContext, apkFile.getName(),cls);
            }
        }


    }



    /**
     * 执行安装并通知安装结果
     */
    private static void execInstallAPP(Context mContext, String filename, Class<?> cls) {
        Logger.d(TAG, "--------------------->execInstallAPP()<------------------");
        PackageInstaller.Session session = null;
        try {
            session = mContext.getPackageManager().getPackageInstaller().openSession(mSessionId);
            Intent intent = new Intent(mContext, cls);
            intent.putExtra("filename", filename);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                    1, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            session.commit(pendingIntent.getIntentSender());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }




    /**
     * 通过文件流传输apk
     *
     * @param apkFilePath
     * @return
     */
    private static boolean onTransfesApkFile(Context mContext, String apkFilePath) {
        Logger.d(TAG, "---------->onTransfesApkFile()<---------------------");
        InputStream in = null;
        OutputStream out = null;
        PackageInstaller.Session session = null;
        boolean success = false;
        try {
            File apkFile = new File(apkFilePath);
            session = mContext.getPackageManager().getPackageInstaller().openSession(mSessionId);
            out = session.openWrite("base.apk", 0, apkFile.length());
            in = new FileInputStream(apkFile);
            int total = 0, c;
            byte[] buffer = new byte[1024 * 1024];
            while ((c = in.read(buffer)) != -1) {
                total += c;
                out.write(buffer, 0, c);
            }
            session.fsync(out);
            Logger.d(TAG, "streamed " + total + " bytes");
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != session) {
                session.close();
            }
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }
}