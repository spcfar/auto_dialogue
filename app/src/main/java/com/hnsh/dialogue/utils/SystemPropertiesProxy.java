package com.hnsh.dialogue.utils;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexFile;

/**
 * Created by Administrator on 2017/12/6.
 */

public class SystemPropertiesProxy {

    public SystemPropertiesProxy() {
    }

    public static String getString(Context context, String key) throws IllegalArgumentException {
        String ret = "";

        try {
            ClassLoader cl = context.getClassLoader();
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[]{String.class};
            Method get = SystemProperties.getMethod("get", paramTypes);
            Object[] params = new Object[]{new String(key)};
            ret = (String)get.invoke(SystemProperties, params);
        } catch (IllegalArgumentException var8) {
            throw var8;
        } catch (Exception var9) {
            ret = "";
        }

        return ret;
    }

    public static String getString(Context context, String key, String def) throws IllegalArgumentException {
        String ret;
        try {
            ClassLoader cl = context.getClassLoader();
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[]{String.class, String.class};
            Method get = SystemProperties.getMethod("get", paramTypes);
            Object[] params = new Object[]{new String(key), new String(def)};
            ret = (String)get.invoke(SystemProperties, params);
        } catch (IllegalArgumentException var9) {
            throw var9;
        } catch (Exception var10) {
            ret = def;
        }

        return ret;
    }

    public static Integer getInt(Context context, String key, int def) throws IllegalArgumentException {
        Integer ret = Integer.valueOf(def);

        try {
            ClassLoader cl = context.getClassLoader();
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            Class[] paramTypes = new Class[]{String.class, Integer.TYPE};
            Method getInt = SystemProperties.getMethod("getInt", paramTypes);
            Object[] params = new Object[]{new String(key), new Integer(def)};
            ret = (Integer)getInt.invoke(SystemProperties, params);
        } catch (IllegalArgumentException var9) {
            throw var9;
        } catch (Exception var10) {
            ret = Integer.valueOf(def);
        }

        return ret;
    }

    public static void set(Context context, String key, String val) throws IllegalArgumentException {
        try {
            new DexFile(new File("/system/app/Settings.apk"));
            ClassLoader cl = context.getClassLoader();
            Class SystemProperties = Class.forName("android.os.SystemProperties");
            Class[] paramTypes = new Class[]{String.class, String.class};
            Method set = SystemProperties.getMethod("set", paramTypes);
            Object[] params = new Object[]{new String(key), new String(val)};
            set.invoke(SystemProperties, params);
        } catch (IllegalArgumentException var9) {
            throw var9;
        } catch (Exception var10) {
        }

    }
}
