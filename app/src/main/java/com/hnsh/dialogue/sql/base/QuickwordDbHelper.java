package com.hnsh.dialogue.sql.base;


import com.hnsh.dialogue.bean.db.QACategoryBean;
import com.hnsh.dialogue.bean.db.QAInfoBean;
import com.hnsh.dialogue.bean.db.QuestionInfoBean;
import com.hnsh.dialogue.bean.db.TranslationInfoBean;
import com.hnsh.dialogue.bean.db.TypeInfoBean;
import com.hnsh.dialogue.sql.dao.QAInfoBeanDao;
import com.hnsh.dialogue.sql.dao.QuestionInfoBeanDao;
import com.hnsh.dialogue.sql.dao.TranslationInfoBeanDao;
import com.hnsh.dialogue.utils.EmptyUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 快捷语管理
 * 包含了 快捷常用语 和 常用问答 两个的部分的数据库操作
 */
public class QuickwordDbHelper {


    /**
     * 根据分类信息查询常用语
     *
     * @param categoryId 分类id
     * @return 快捷用语集合
     */
    private final static DbManager dbManager = DbManager.INSTANCE();

    public static List<QuestionInfoBean> queryPhraseInfoList(long categoryId) {
        return dbManager
                .getQuestionInfoBeanDao()
                .queryBuilder()
                .where(QuestionInfoBeanDao.Properties.TypeId.eq(categoryId))
                .list();
    }


    public static QAInfoBean queryQAInfoBeanByText(String text) {
        List<QAInfoBean> list =  dbManager.getQAInfoBeanDao().queryBuilder().where(QAInfoBeanDao.Properties.Content.like(text)).list();
        if(EmptyUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    public static QuestionInfoBean queryQuestionByText(String text) {
        List<QuestionInfoBean> list =  dbManager.getQuestionInfoBeanDao().queryBuilder().where(QuestionInfoBeanDao.Properties.Content.like(text)).list();
        if(EmptyUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据快捷用语Id 和语言id查看对应的翻译信息
     *
     * @param phraseId 快捷用语Id
     * @param langId   语言id
     * @return 翻译信息
     */
    public static TranslationInfoBean queryTranslationInfo(long phraseId, int langId) {
        return dbManager
                .getTranslationInfoBeanDao()
                .queryBuilder()
                .where(TranslationInfoBeanDao.Properties.QId.eq(phraseId))
                .where(TranslationInfoBeanDao.Properties.LaId.eq(langId))
                .unique();
    }

    /**
     * 查询 快捷用语
     *
     * @return
     */
    public static List<TypeInfoBean> queryCategoryInfoList() {
        return dbManager.getTypeInfoBeanDao().queryBuilder().list();
    }

    /**
     * 清空快捷用语分类信息
     */
    public static void clearCategoryInfoList() {
        dbManager.getTypeInfoBeanDao().deleteAll();
    }

    /**
     * 清空快捷用语信息
     */
    public static void clearPhraseInfoList() {
        dbManager.getQuestionInfoBeanDao().deleteAll();
    }

    /**
     * 清空快捷用语信息
     */
    public static void clearTranslationInfoList() {
        dbManager.getTranslationInfoBeanDao().deleteAll();
    }

    public static void clearQACategoryList() {
        dbManager.getQACategoryBeanDao().deleteAll();
    }

    public static void clearQAInfoList() {
        dbManager.getQAInfoBeanDao().deleteAll();
    }

    /**
     * 清空 快捷用语及其 分类和翻译的信息
     */
    public static void clearPhraseRelated() {
        dbManager.getTypeInfoBeanDao().deleteAll();
        dbManager.getQuestionInfoBeanDao().deleteAll();
        dbManager.getTranslationInfoBeanDao().deleteAll();
    }

    /**
     * 批量添加 快捷用语 信息
     *
     * @param phraseInfoList
     */
    public static void insertPhraseInfoList(List<QuestionInfoBean> phraseInfoList) {
        dbManager.getQuestionInfoBeanDao().insertOrReplaceInTx(phraseInfoList);
    }

    /**
     * 批量 添加分类 信息
     *
     * @param categoryInfoList
     */
    public static void insertCategoryInfoList(List<TypeInfoBean> categoryInfoList) {
        dbManager.getTypeInfoBeanDao().insertOrReplaceInTx(categoryInfoList);
    }

    /**
     * 批量 添加翻译 信息
     *
     * @param translationInfoList
     */
    public static void insertTranslationList(List<TranslationInfoBean> translationInfoList) {
        dbManager.getTranslationInfoBeanDao().insertOrReplaceInTx(translationInfoList);
    }

    /**
     * 添加问答分类信息
     *
     * @param categoryBeanList
     */

    public static void insertQACategoryList(List<QACategoryBean> categoryBeanList) {
        dbManager.getQACategoryBeanDao().insertOrReplaceInTx(categoryBeanList);
    }

    public static void insertQAInfoList(List<QAInfoBean> qaInfoBeanList) {
        dbManager.getQAInfoBeanDao().insertOrReplaceInTx(qaInfoBeanList);
    }

    /**
     * 查询批量问答信息
     *
     * @param categoryId
     * @return
     */
    public static List<QAInfoBean> queryQAInfoList(long categoryId, int lang) {
        QueryBuilder<QAInfoBean> builder = dbManager.getQAInfoBeanDao().queryBuilder();
        return builder.where(
                builder.and(QAInfoBeanDao.Properties.TypeId.eq(categoryId)
                        , QAInfoBeanDao.Properties.LanguageId.eq(lang)
                        , QAInfoBeanDao.Properties.IsQuestionOrAnswer.eq(0)))
                .list();
    }

    public static List<QACategoryBean> queryQACategoryList() {

        return dbManager.getQACategoryBeanDao().queryBuilder().list();
    }


    /**
     * 获取问题翻译
     *
     * @param questionId
     * @param langId
     * @return
     */
    public static QAInfoBean queryQuestionTranslation(long questionId, int langId) {
        QueryBuilder<QAInfoBean> builder = dbManager.getQAInfoBeanDao().queryBuilder();
        if(langId == 0){
            return builder.where(QAInfoBeanDao.Properties.Id.eq(questionId)).unique();
        }
        return builder.where(
                builder.and(QAInfoBeanDao.Properties.LinkId.eq(questionId)
                        , QAInfoBeanDao.Properties.LanguageId.eq(langId))).unique();
    }


    /**
     * 获取答案翻译
     *
     * @return
     */
    public static QAInfoBean queryAnswerTranslation(Long answerId) {

        QueryBuilder<QAInfoBean> builder = dbManager.getQAInfoBeanDao().queryBuilder();
        QueryBuilder<QAInfoBean> builder2 = dbManager.getQAInfoBeanDao().queryBuilder();

        QAInfoBean bean = builder.where(QAInfoBeanDao.Properties.Id.eq(answerId)).unique();
        if (EmptyUtils.isNotEmpty(bean)) {

            long questionId = bean.getLinkId();

            return builder2.where(QAInfoBeanDao.Properties.Id.eq(questionId)).unique();
        }
        return null;
    }

    /**
     * 获取问题的答案
     *
     * @param questionId
     * @return
     */
    public static List<QAInfoBean> queryAnswerByQuestion(long questionId) {
        return dbManager.getQAInfoBeanDao().queryBuilder().where(QAInfoBeanDao.Properties.LinkUpId.eq(questionId)).list();
    }

}
