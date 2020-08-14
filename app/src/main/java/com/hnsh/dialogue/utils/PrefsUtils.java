package com.hnsh.dialogue.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefsUtils {
	
	private static final String PREFS_NAME = "Intercom";
	private static final int MODE = Context.MODE_PRIVATE;

	public static void setPrefs(Context context, String name, String value){
		setPrefs(context, PREFS_NAME, name, value);
	}

	public static void setPrefs(Context context, String prefsName, String name, String value){
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		Editor edit = prefs.edit();
		edit.putString(name, value);
		edit.commit();
	}

	public static void setPrefs(Context context, String name, boolean value){
		setPrefs(context, PREFS_NAME, name, value);
	}

	public static void setPrefs(Context context, String prefsName, String name, boolean value){
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		Editor edit = prefs.edit();
		edit.putBoolean(name, value);
		edit.commit();
	}

	public static void setPrefs(Context context, String name, int value){
		setPrefs(context, PREFS_NAME, name, value);
	}
	
	public static void setPrefs(Context context, String prefsName, String name, int value){
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		Editor edit = prefs.edit();
		edit.putInt(name, value);
		edit.commit();
	}

	public static void setPrefs(Context context, String name, long value){
		setPrefs(context, PREFS_NAME, name, value);
	}
	
	public static void setPrefs(Context context, String prefsName, String name, long value){
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		Editor edit = prefs.edit();
		edit.putLong(name, value);
		edit.commit();
	}

	public static void setPrefs(Context context, String name, float value){
		setPrefs(context, PREFS_NAME, name, value);
	}
	
	public static void setPrefs(Context context, String prefsName, String name, float value){
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		Editor edit = prefs.edit();
		edit.putFloat(name, value);
		edit.commit();
	}

	public static String getPrefs(Context context, String name, String defValue) {
		return getPrefs(context, PREFS_NAME, name, defValue);
	}
	
	public static String getPrefs(Context context, String prefsName, String name, String defValue) {
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		return prefs.getString(name, defValue);
	}

	public static boolean getPrefs(Context context, String name, boolean defValue) {
		return getPrefs(context, PREFS_NAME, name, defValue);
	}

	public static boolean getPrefs(Context context, String prefsName, String name, boolean defValue) {
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		return prefs.getBoolean(name, defValue);
	}

	public static int getPrefs(Context context, String name, int defValue) {
		return getPrefs(context, PREFS_NAME, name, defValue);
	}
	
	public static int getPrefs(Context context, String prefsName, String name, int defValue) {
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		return prefs.getInt(name, defValue);
	}

	public static long getPrefs(Context context, String name, long defValue) {
		return getPrefs(context, PREFS_NAME, name, defValue);
	}
	
	public static long getPrefs(Context context, String prefsName, String name, long defValue) {
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		return prefs.getLong(name, defValue);
	}

	public static float getPrefs(Context context, String name, float defValue) {
		return getPrefs(context, PREFS_NAME, name, defValue);
	}
	
	public static float getPrefs(Context context, String prefsName, String name, float defValue) {
		SharedPreferences prefs = context.getSharedPreferences(prefsName, MODE);
		return prefs.getFloat(name, defValue);
	}
}
