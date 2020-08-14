package com.hnsh.dialogue.sql.base;

import android.database.sqlite.SQLiteDatabase;
import com.hnsh.dialogue.sql.dao.AppInfoDao;
import com.hnsh.dialogue.sql.dao.DaoMaster;
import com.hnsh.dialogue.sql.dao.DaoSession;
import com.hnsh.dialogue.sql.dao.LanguageSeletedEntityDao;
import com.hnsh.dialogue.sql.dao.MessageContentDao;
import com.hnsh.dialogue.sql.dao.MyOpenHelper;
import com.hnsh.dialogue.sql.dao.QACategoryBeanDao;
import com.hnsh.dialogue.sql.dao.QAInfoBeanDao;
import com.hnsh.dialogue.sql.dao.QuestionInfoBeanDao;
import com.hnsh.dialogue.sql.dao.TranslationInfoBeanDao;
import com.hnsh.dialogue.sql.dao.TypeInfoBeanDao;
import com.hnsh.dialogue.utils.UIUtils;

/**
 * @项目名： Translator
 * @包名： com.dosmono.common.db
 * @文件名: DbManager
 * @创建者: Administrator
 * @创建时间: 2018/3/19 019 10:24
 * @描述： TODO
 */

public class DbManager {
    private static DbManager mInstance;
    private final DaoMaster.DevOpenHelper mHelper;

    private DbManager(){
        mHelper = new MyOpenHelper(UIUtils.getContext(), "dosmono_translator.db");
    }

    public static DbManager INSTANCE(){
        if (null == mInstance){
            synchronized (DbManager.class){
                if (null == mInstance){
                    mInstance = new DbManager();
                }
            }
        }

        return mInstance;
    }

    public DaoSession getDaoSession(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public AppInfoDao getAppInfoDao(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession.getAppInfoDao();
    }

    public MessageContentDao getDialogueDao(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession.getMessageContentDao();
    }

    public LanguageSeletedEntityDao getLanguageSeletedEntityDao(){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession.getLanguageSeletedEntityDao();
    }


    public QuestionInfoBeanDao getQuestionInfoBeanDao(){
        return getDaoSession().getQuestionInfoBeanDao();
    }

    public TypeInfoBeanDao getTypeInfoBeanDao(){
        return getDaoSession().getTypeInfoBeanDao();
    }

    public TranslationInfoBeanDao getTranslationInfoBeanDao(){
        return getDaoSession().getTranslationInfoBeanDao();
    }

    public QACategoryBeanDao getQACategoryBeanDao(){
        return getDaoSession().getQACategoryBeanDao();
    }

    public QAInfoBeanDao getQAInfoBeanDao(){
        return getDaoSession().getQAInfoBeanDao();
    }

}
