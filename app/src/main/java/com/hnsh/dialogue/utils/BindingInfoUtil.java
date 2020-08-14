package com.hnsh.dialogue.utils;

import android.content.Context;

import com.hnsh.dialogue.bean.cbs.BindingInfo;
import com.hnsh.dialogue.mvp.models.PrefsSettingDefaultModel;

/**
 * @项目名： Translator
 * @包名： com.dosmono.common.utils
 * @文件名: BindingInfoUtil
 * @创建者: Administrator
 * @创建时间: 2018/3/23 023 11:05
 * @描述： TODO
 */

public class BindingInfoUtil {

    private static final String PREFS_KEY_DEVICEIDONE = "key_deviceIdOne";
    private static final String PREFS_KEY_DEVICEIDTWO = "key_deviceIdTwo";
    private static final String PREFS_KEY_MASTERDEVICEID = "key_masterDeviceId";
    private static final String PREFS_KEY_MASTERLANG = "key_masterlang";
    private static final String PREFS_KEY_BINDING_GID = "key_binding_gid";
    private static final String PREFS_KEY_BINDING_SLAVELANG = "key_binding_slavelang";
    private static final String PREFS_KEY_BINDING_FONTSIZE = "key_binding_fontSize";
    private static final String PREFS_KEY_BINDING_VOICESWITCH = "key_binding_voiceSwitch";
    private static final String PREFS_KEY_PAIR_DEVICE = "key_pair_device";


    public static void  setBindingInfo(Context context, BindingInfo info){
        if (null != info){
            PrefsUtils.setPrefs(context,PREFS_KEY_DEVICEIDONE,info.getDeviceIdOne() == null ? "" : info.getDeviceIdOne());
            PrefsUtils.setPrefs(context,PREFS_KEY_DEVICEIDTWO,info.getDeviceIdTwo() == null ? "" : info.getDeviceIdTwo());
            PrefsUtils.setPrefs(context,PREFS_KEY_MASTERDEVICEID,info.getMasterDeviceId() == null ? "" : info.getMasterDeviceId());
            PrefsUtils.setPrefs(context,PREFS_KEY_MASTERLANG,info.getLang());
            PrefsUtils.setPrefs(context,PREFS_KEY_BINDING_GID,info.getGid() == null ? "" : info.getGid());
            PrefsUtils.setPrefs(context,PREFS_KEY_BINDING_SLAVELANG,info.getSlavelang());
            PrefsUtils.setPrefs(context,PREFS_KEY_BINDING_FONTSIZE,info.getFontSize() == null ? "" : info.getFontSize());
            PrefsUtils.setPrefs(context,PREFS_KEY_BINDING_VOICESWITCH,info.getVoiceSwitch() == null ? "" : info.getVoiceSwitch());

            if (info.getMasterDeviceId().equals(CommonUtil.getDeviceId())){
                PrefsSettingDefaultModel.INSTANCE().setWorkerLang(info.getLang());
            }

            if(info.getDeviceIdOne().equals(CommonUtil.getDeviceId())){
                PrefsUtils.setPrefs(context,PREFS_KEY_PAIR_DEVICE,info.getDeviceIdTwo());
            }else{
                PrefsUtils.setPrefs(context,PREFS_KEY_PAIR_DEVICE,info.getDeviceIdOne());
            }
        }
    }

    public static BindingInfo getBindingInfo(Context context){
        BindingInfo info = new BindingInfo();
        String deviceIdOne = PrefsUtils.getPrefs(context, PREFS_KEY_DEVICEIDONE, "");
        String deviceIdTwo = PrefsUtils.getPrefs(context, PREFS_KEY_DEVICEIDTWO, "");
        String masterDeviceid = PrefsUtils.getPrefs(context, PREFS_KEY_MASTERDEVICEID, "");
        int lang = PrefsUtils.getPrefs(context, PREFS_KEY_MASTERLANG, -1);
        String gid = PrefsUtils.getPrefs(context, PREFS_KEY_BINDING_GID, "");
        int slaveLang = PrefsUtils.getPrefs(context, PREFS_KEY_BINDING_SLAVELANG, -1);
        String fontSize = PrefsUtils.getPrefs(context, PREFS_KEY_BINDING_FONTSIZE, "");
        String voiceSwitch = PrefsUtils.getPrefs(context, PREFS_KEY_BINDING_VOICESWITCH, "");

        info.setDeviceIdOne(deviceIdOne);
        info.setDeviceIdTwo(deviceIdTwo);
        info.setMasterDeviceId(masterDeviceid);
        info.setLang(lang);
        info.setGid(gid);
        info.setSlavelang(slaveLang);
        info.setFontSize(fontSize);
        info.setVoiceSwitch(voiceSwitch);
        return info;
    }

    public static String getPairDeviceId(Context context){

        return PrefsUtils.getPrefs(context, PREFS_KEY_PAIR_DEVICE, "");
    }
}
