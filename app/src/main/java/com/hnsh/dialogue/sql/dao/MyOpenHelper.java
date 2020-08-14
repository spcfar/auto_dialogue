package com.hnsh.dialogue.sql.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

/**
 * @项目名： mono_travel
 * @包名： com.dosmono.asmack.dao
 * @文件名: MyOpenHelper
 * @创建者: Administrator
 * @创建时间: 2017/12/4 004 10:39
 * @描述： GreenDao数据库升级帮助类
 */

public class MyOpenHelper extends DaoMaster.DevOpenHelper {


    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Log.i("database version", oldVersion + "---先前和更新之后的版本---" + newVersion);
        if (oldVersion < newVersion){
            Log.i("database version", "onUpgrade: 有新版本更新 --->" + newVersion);
            MigrationHelper.getInstance().migrate(db, AppInfoDao.class);
            MigrationHelper.getInstance().migrate(db, MessageContentDao.class);
        }
    }
}
