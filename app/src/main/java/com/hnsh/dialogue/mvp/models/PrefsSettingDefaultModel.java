package com.hnsh.dialogue.mvp.models;
import com.dosmono.logger.Logger;
import com.hnsh.dialogue.bean.cbs.SettingDefaultInfo;
import com.hnsh.dialogue.utils.PrefsUtils;
import com.hnsh.dialogue.utils.UIUtils;

/**
 * @项目名： Translator
 * @包名： com.dosmono.common.model
 * @文件名: PrefsSettingDefaultModel
 * @创建者: Administrator
 * @创建时间: 2018/3/1 001 15:55
 * @描述： TODO
 */

public class PrefsSettingDefaultModel {
    private final String PREFS_USE_PATTERN = "settingDefaultUsePattern";
    private final String PREFS_SYSTEM_LANGUAGE = "settingDefaultSystemLanguage";
    private final String PREFS_WORKER_LANGUAGE = "settingDefaultWorkerLanguage";

    private static PrefsSettingDefaultModel instance;

    private PrefsSettingDefaultModel(){

    }

    public static PrefsSettingDefaultModel INSTANCE(){
        if (instance == null){
            synchronized (PrefsSettingDefaultModel.class){
                if (instance == null){
                    instance = new PrefsSettingDefaultModel();
                }
            }
        }
        return instance;
    }

    public void saveSettingDefaultInfo(SettingDefaultInfo info){
        if (info != null){
            PrefsUtils.setPrefs(UIUtils.getContext(),PREFS_USE_PATTERN,info.getUsePattern());
            PrefsUtils.setPrefs(UIUtils.getContext(),PREFS_SYSTEM_LANGUAGE,info.getSystemLanguage());
            PrefsUtils.setPrefs(UIUtils.getContext(),PREFS_WORKER_LANGUAGE,info.getWorkerLanguage());
        }
    }

    public void saveSettingDefaultSystemLang(int systemLanguage){
        PrefsUtils.setPrefs(UIUtils.getContext(),PREFS_SYSTEM_LANGUAGE,systemLanguage);
    }

    public int getUsePattern(){
        return PrefsUtils.getPrefs(UIUtils.getContext(),PREFS_USE_PATTERN,0);
    }

    public void setUsePattern(int pattern){
        PrefsUtils.setPrefs(UIUtils.getContext(),PREFS_USE_PATTERN,pattern);
    }

    public int getWorkerLang(){
        Logger.d("[ debug ] getWorkerLang");
        return PrefsUtils.getPrefs(UIUtils.getContext(),PREFS_WORKER_LANGUAGE,0);
    }

    public void setWorkerLang(int workerLang){
        PrefsUtils.setPrefs(UIUtils.getContext(),PREFS_WORKER_LANGUAGE,workerLang);
        Logger.d("[ debug ] setWorkerLang workerLang ="+workerLang);
    }

    /**
     * 获取默认设置存储信息（工作模式、系统语种、工作语言）
     * @return
     */
    public SettingDefaultInfo getSettingDefaultInfo(){
        SettingDefaultInfo info = new SettingDefaultInfo();
        info.setUsePattern(PrefsUtils.getPrefs(UIUtils.getContext(),PREFS_USE_PATTERN,0));
        info.setSystemLanguage(PrefsUtils.getPrefs(UIUtils.getContext(),PREFS_SYSTEM_LANGUAGE,0));
        info.setWorkerLanguage(PrefsUtils.getPrefs(UIUtils.getContext(),PREFS_WORKER_LANGUAGE,0));
        return info;
    }
}
