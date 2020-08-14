package com.hnsh.dialogue.bean.db;


import com.hnsh.dialogue.bean.cbs.MessageContent;
import com.hnsh.dialogue.sql.base.DbManager;
import com.hnsh.dialogue.sql.dao.MessageContentDao;

import java.util.List;

/**
 * @项目名： BIZ_Project
 * @包名： com.dosmono.common.db
 * @文件名: DialogueDbHelper
 * @创建者: zer
 * @创建时间: 2018/11/19 19:22
 * @描述： TODO
 */
public class DialogueDbHelper {
    private static DialogueDbHelper  mInstance;
    private final MessageContentDao mDialogueDao ;

    private DialogueDbHelper() {
        mDialogueDao = DbManager.INSTANCE().getDialogueDao();
    }

    public static DialogueDbHelper INSTANCE() {
        if (null == mInstance) {
            synchronized (DialogueDbHelper.class) {
                if (null == mInstance) {
                    mInstance = new DialogueDbHelper();
                }
            }
        }
        return mInstance;
    }

    public long insert(MessageContent entity) {
        return mDialogueDao.insertOrReplace(entity);
    }

    public void delete(MessageContent entity) {
        mDialogueDao.queryBuilder()
                    .where(MessageContentDao.Properties.SendTimeMs.eq(entity.getSendTimeMs()))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
        mDialogueDao.delete(entity);
    }

    public void deleteAll() {
        mDialogueDao.deleteAll();
    }

    public List<MessageContent> queryAll() {
        return mDialogueDao.queryBuilder().build().list();
    }

    public void update(MessageContent entity){
        mDialogueDao.update(entity);
    }
}
