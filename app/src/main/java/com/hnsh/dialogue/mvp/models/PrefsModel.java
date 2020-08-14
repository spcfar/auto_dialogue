package com.hnsh.dialogue.mvp.models;

import android.content.Context;

import com.hnsh.dialogue.constants.BIZConstants;
import com.hnsh.dialogue.constants.TSRConstants;
import com.hnsh.dialogue.utils.PrefsUtils;

/**
 * Created by <Yang Tao> on <18/2/6>.
 */

public class PrefsModel {

    private static final String PREFS_FONT_SIZE = "DialogueFontSize";
    private static final String PREFS_AUTO_PLAY = "DialogueAutoPlay";
    private static final String PREFS_SELECTED_LANG = "SelectedLanguage";
    private static final String PREFS_RECOGNIZE_CONTENT_CONFIRM = "RecognizeConfirm";
    private static final String PREFS_LANGUAGE_CONFIG_MD5 = "mono_language_config";

    private static final String PREFS_IF_NEED_ANSWER = "if_need_answer";
    private static final String PREFS_IF_NEED_PICTURE = "if_need_picture";

    private static final String PREFS_IF_NEED_PICTURE_RES_ID = "if_need_picture_res_id";
    private static final String PREFS_IF_NEED_ANSWER_RES_ID = "if_need_picture_res_id";


    public static int getFontSize(Context context) {
        return PrefsUtils.getPrefs(context, PREFS_FONT_SIZE, TSRConstants.FONT_MAX);
    }

    public static void setFontSize(Context context, int fontSize) {
        PrefsUtils.setPrefs(context, PREFS_FONT_SIZE, fontSize);
    }

    public static boolean isAutoPlay(Context context) {
        return PrefsUtils.getPrefs(context, PREFS_AUTO_PLAY, true);
    }

    public static void setAutoPlay(Context context, boolean autoPlay) {
        PrefsUtils.setPrefs(context, PREFS_AUTO_PLAY, autoPlay);
    }

    public static void setSelectedLanguage(Context context, int landId){
        PrefsUtils.setPrefs(context,PREFS_SELECTED_LANG,landId);
    }

    public static int getSelectedLanguage(Context context){
        return PrefsUtils.getPrefs(context, PREFS_SELECTED_LANG, 0);
    }

    public static void setRecognizeContentConfirm(Context context, boolean value){
        PrefsUtils.setPrefs(context,PREFS_RECOGNIZE_CONTENT_CONFIRM,value);
    }

    public static boolean getRecognizeContentConfirm(Context context){
        return PrefsUtils.getPrefs(context,PREFS_RECOGNIZE_CONTENT_CONFIRM,false);
    }

    public static void setLangConfigMD5(Context context, String value){
        PrefsUtils.setPrefs(context,PREFS_LANGUAGE_CONFIG_MD5,value);
    }

    public static String getLangConfigMD5(Context context){
        return PrefsUtils.getPrefs(context,PREFS_LANGUAGE_CONFIG_MD5,"0");
    }


    public static void setIfNeedPicture(Context context, boolean value){
        PrefsUtils.setPrefs(context,PREFS_IF_NEED_PICTURE,value);
    }

    public static boolean getIfNeedPicture(Context context){
        return PrefsUtils.getPrefs(context,PREFS_IF_NEED_PICTURE,false);
    }

    public static void setIfNeedAnswer(Context context, boolean value){
        PrefsUtils.setPrefs(context,PREFS_IF_NEED_ANSWER,value);
    }

    public static boolean getIfNeedAnswer(Context context){
        //这里默认是true
        return PrefsUtils.getPrefs(context,PREFS_IF_NEED_ANSWER,true);
    }

    public static boolean getIfNeedUpdateDesktop(Context context) {
        return PrefsUtils.getPrefs(context, BIZConstants.SP.SP_NEED_UPDATE_FELLOW_DESKTOP, false);
    }

    public static void setIfNeedUpdateDesktop(Context context, boolean value){
        PrefsUtils.setPrefs(context, BIZConstants.SP.SP_NEED_UPDATE_FELLOW_DESKTOP,value);
    }

    public static boolean getIfEnableHumanTranslation(Context context){
        return PrefsUtils.getPrefs(context, BIZConstants.Dmonkey.KEY_ENABLE_PRO+ BIZConstants.Dmonkey.KEY_HUMAN_TRANSLATION_PACKAGE,0) == 0;
    }

    public static boolean getIfEnablDialogTranslator(Context context){
        return PrefsUtils.getPrefs(context, BIZConstants.Dmonkey.KEY_ENABLE_PRO+ BIZConstants.Dmonkey.KEY_DIALOG_TRANSLATION,0) == 0;
    }

    public static boolean getIfEnablExamination(Context context){
        return PrefsUtils.getPrefs(context, BIZConstants.Dmonkey.KEY_ENABLE_PRO+ BIZConstants.Dmonkey.KEY_EXAMINATION_PAGEAGE,0) == 0;
    }


}

