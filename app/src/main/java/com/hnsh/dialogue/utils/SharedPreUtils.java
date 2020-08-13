package com.hnsh.dialogue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;


import com.dosmono.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class SharedPreUtils {
    private static SharedPreferences sp;
    private final static String SP_NAME	= "config";

    public static void saveBoolean(Context context, String key, boolean value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putBoolean(key, value).commit();
    }


    public static void saveString(Context context, String key, String value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putString(key, value).commit();

    }

    public static void clear(Context context) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().clear().commit();
    }


    public static void saveLong(Context context, String key, long value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putLong(key, value).commit();
    }


    public static void saveInt(Context context, String key, int value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putInt(key, value).commit();
    }


    public static void saveFloat(Context context, String key, float value) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        sp.edit().putFloat(key, value).commit();
    }


    public static String getString(Context context, String key, String defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getString(key, defValue);
    }


    public static int getInt(Context context, String key, int defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getInt(key, defValue);
    }


    public static long getLong(Context context, String key, long defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getLong(key, defValue);
    }


    public static float getFloat(Context context, String key, float defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getFloat(key, defValue);
    }


    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sp == null)
            sp = context.getSharedPreferences(SP_NAME, 0);
        return sp.getBoolean(key, defValue);
    }

    /** 删除缓存 */
    public static void remove(Context context, String key) {
        try {
            if (sp == null) sp = context.getSharedPreferences(SP_NAME, 0);
            sp.edit().remove(key).commit();
        }catch (Exception e){
            Logger.d("e",e);
        }

    }


    /**
     * 将SharePref中经过base64编码的对象读取出来
     * @param context
     * @param key
     * @return
     */
    public static Object getObj(Context context, String key) {
        if (sp == null) sp = context.getSharedPreferences(SharedPreUtils.SP_NAME, 0);
        String objBase64 = sp.getString(key, null);
        if (TextUtils.isEmpty(objBase64)) return null;
        // 对Base64格式的字符串进行解码
        try {
            byte[] base64Bytes = Base64.decode(objBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois;
            Object obj = null;
            ois = new ObjectInputStream(bais);
            obj = (Object)ois.readObject();
            ois.close();
            return obj;
        } catch (Exception e) {
            Logger.e(e,"e");
        }
        return null;
    }


    /**
     * 将对象进行base64编码后保存到SharePref中
     *
     * @param context
     * @param key
     */
    public static void saveObjP(Context context, String key, Parcelable parceable) {
        if (sp == null) sp = context.getSharedPreferences(SharedPreUtils.SP_NAME, 0);

        if(parceable==null){
            sp.edit().putString(key,"").commit();
        }else {
            Parcel parcel = Parcel.obtain();
            parcel.setDataPosition(0);
            parceable.writeToParcel(parcel, 0);
            byte[] bytes = parcel.marshall();
            sp.edit().putString(key, Base64.encodeToString(bytes, 0)).commit();
            parcel.recycle();
        }

    }


}
