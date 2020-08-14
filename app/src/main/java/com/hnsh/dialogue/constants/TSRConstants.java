package com.hnsh.dialogue.constants;

import com.dosmono.universal.common.Constant;

/**
 * Created by Unpar on 18/1/11.
 */

public class TSRConstants {

    public static final String MPUSH_URL = Constant.MPUSH_URL;
    //    public static final String MPUSH_URL = "http://192.168.30.123:9999";
//    public static final String MPUSH_URL ="http://192.168.50.223:9999";
    public static final String HTTP_URL = Constant.HTTP_URL;

    //    public static final String HTTP_ROOT_URL = "http://192.168.30.123:8087";
    public static final String HTTP_ROOT_URL = Constant.HTTP_URL;

    //    public static final long SYNCH_PERIOD = 500;
    public static final long SYNCH_PERIOD = 500;
    //    public static final int PUSH_TIMEOUT = 6 * 1000;
    public static final int PUSH_TIMEOUT = 8 * 1000;
    public static final long MESSAGE_DATA_PROMPT_MS = 5 * 60 * 1000;

    public static final String PUSH_QUERY_BINDING = "com.dosmono.mpush.plugins.interpreter.findlist";
    //    public static final String PUSH_CREATE_BINDING = "com.dosmono.mpush.plugins.interpreter.create";
    public static final String PUSH_CONFIRM_BINDING = "com.dosmono.mpush.plugins.interpreter.create.confirm";
    public static final String PUSH_UNBINDING = "com.dosmono.mpush.plugins.interpreter.unbind";
    public static final String PUSH_SET_IDENTITY = "com.dosmono.mpush.plugins.interpreter.setidentity";
    public static final String PUSH_GET_IDENTITY = "com.dosmono.mpush.plugins.interpreter.getidentity";
    public static final String PUSH_SEND_MESSAGE = "com.dosmono.mpush.frontier.chat"; //"com.dosmono.mpush.plugins.interpreter.chat";
    public static final String PUSH_RECE_MESSAGE = "com.dosmono.mpush.frontier.chat.receive"; //"com.dosmono.mpush.plugins.interpreter.chat.receive";
    public static final String PUSH_GET_CODE = "com.dosmono.mpush.plugins.interpreter.applyinvitationcode";
    public static final String PUSH_CREATE_BINDING = "com.dosmono.mpush.plugins.interpreter.create";
    public static final String PUSH_PUSH_BINDING = "com.dosmono.mpush.plugins.interpreter.create.pushbinding";
    public static final String PUSH_PUSH_UNBIND = "com.dosmono.mpush.plugins.interpreter.unbind.pushunbinding";
    public static final String PUSH_CUSTOMER = "com.dosmono.mpush.plugins.interpreter.setidentity.pushcustomer";
    public static final String PUSH_GET_BOOTINFO = "com.dosmono.mpush.frontier.getbootinfo";
    public static final String PUSH_SET_LANGUAGE = "com.dosmono.mpush.frontier.setslavelanguage";
    public static final String PUSH_RECV_SET_LANGUAGE = "com.dosmono.mpush.frontier.setslavelanguage.receive";
    public static final String PUSH_GET_SYCHINFO = "com.dosmono.mpush.sync.getmsg";
    public static final String PUSH_SET_PARAMS = "com.dosmono.mpush.frontier.setparam";
    public static final String PUSH_RECV_SET_PARAMS = "com.dosmono.mpush.frontier.setparam.receive";
    public static final String PUSH_END_DIALOGUE = "com.dosmono.mpush.frontier.setover";
    public static final String PUSH_RECV_END_DIALOGUE = "com.dosmono.mpush.frontier.setover.receive";
    public static final String PUSH_GET_BINDIDENTITY = "com.dosmono.mpush.plugins.interpreter.getbindidentity";
    public static final String PUSH_SET_ACTIVELANGUAGE = "com.dosmono.mpush.frontier.activelanguage";
    public static final String PUSH_RECV_ACTIVELANGUAGE = "com.dosmono.mpush.frontier.activelanguage.receive";
    public static final String PUSH_SEND_CUSTOM_MESSAGE = "com.dosmono.mpush.plugins.interpreter.sync.custom";
    public static final String PUSH_RECV_CUSTOM_MESSAGE = "com.dosmono.mpush.plugins.interpreter.sync.custom.receive";

    //    public static String SERVER_HOST = "http://192.168.124.128:9999"; //Constant.MPUSH_URL;
//    public static String SERVER_HOST = MPUSH_URL; //"http://192.168.50.222:9999"; //Constant.MPUSH_URL;
    public static String SERVER_HOST = MPUSH_URL; //"http://192.168.50.222:9999"; //Constant.MPUSH_URL;
//    public static String SERVER_HOST = "http://192.168.30.123:9999"; //"http://192.168.50.222:9999"; //Constant.MPUSH_URL;

    public static final String BIND_RELATION = "bind_relation";
    public static final String BIND_RELATION_FLAG = "bind_relation_flag";
    public static final String UNBIND_BROADCAST = "com.dosmono.setting.unbind";
    public static final String TIME_BROADCAST = "com.dosmono.setting.time";
    public static final String NEW_TIME_BROADCAST = "com.dosmono.setting.newtime";
    public static final String TIME_COUNT = "count";
    //自己的信息
    public static final int DIALOGUE_MSG_TYPE_ONESELF = 0;
    //对方的信息
    public static final int DIALOGUE_MSG_TYPE_TARGET = 1;
    //日期
    public static final int DIALOGUE_MSG_TYPE_DATE = 2;

    public static final int FONT_MAX = 36;
    public static final int FONT_MEDIUM = 32;
    public static final int FONT_MIN = 28;

    public static final int KJ_FONT_MAX = 48;
    public static final int KJ_FONT_MEDIUM = 36;
    public static final int KJ_FONT_MIN = 28;

    public static final String ACTION_DIALOGUE = "com.dosmono.dialogue.dialogue";
    public static final String ACTION_KJDIALOGUE = "com.dosmono.dialogue.kjdialogue";
    public static final String EXTRA_LANG_ID = "LanguageId";
    public static final String EXTRA_FOLLOW_LANG_ID = "FollowLanId";

    public static final int BIND_STATE_NONE = 0x00;
    public static final int BIND_STATE_UNBIND = 0x01;
    public static final int BIND_STATE_BINDED = 0x02;

    public static final String USE_PATTERN_CLIENT = "0";
    public static final String USE_PATTERN_WORKER = "1";

    public static final String KEY_FLAG = "flag";
    public static final String KEY_DATA = "data";

    public static final int SETTING_STORE_TYPE_NOT_ALL = 0;
    public static final int SETTING_STORE_TYPE_ONLY_VOICE = 1;
    public static final int SETTING_STORE_TYPE_ONLY_CHARACTER = 2;
    public static final int SETTING_STORE_TYPE_CHARACTER_AND_VOICE = 3;

    public static final int MSG_STATE_SENDING = 0;
    public static final int MSG_STATE_FAILURE = 1;
    public static final int MSG_STATE_SUCCESS = 2;

    public static final String EXTRA_INTENT_ACTION_SETTING = "extra_intent_aciton_setting";

    //边检版本
    public static final int TYPE_INSPECTION = 1;

    public static final String COMMON_SHORTCUT = "0";
    public static final String SPECIAL_SHORTCUT = "1";

    public static final String DIALOGUE_VOICE_TOGGLE_CLOSE = "0";
    public static final String DIALOGUE_VOICE_TOGGLE_OPEN = "1";

    public static final int EXTRA_REQUEST_CODE_SETTING = 101;

    public static final String LOCALE_LANGUAGE_ZH = "zh";
    public static final String LOCALE_LANGUAGE_EN = "en";
    public static final String LOCALE_LANGUAGE_JA = "ja";

    public static final int COMMON_EVN_TYPE = 0;
    public static final int INSPENCTIONO_EVN_TYPE = 1;

    public static final String MODULE_DIALOGUE = "com.module.dialogue";
    public static final String MODULE_ADD = "com.module.add";
    public static final String MODULE_SYSTEM_DESKTOP = "com.module.desktop";
    public static final String MODULE_MENU_SYSTEM = "com.module.menu";

    public static final String HTML_FLAG = "html";
    public static final int ELECTRONIC_MENU = 0;
    public static final int DESKTOP_SYSTEM = 1;

    public static final String APP_TYPE_H5 = "2";
    public static final String APP_TYPE_NATIVE = "1";

    public static final int WIFI_STATE_CONNECTING = 2;
    public static final int WIFI_STATE_CONNECTED = 1;
    public static final int WIFI_STATE_NONE = 0;

    public static final int ZH_LANGUAGE_ID = 0;
    public static final int HK_LANGUAGE_ID = 1;
    public static final int EN_LANGUAGE_ID = 3;
    public static final int RU_LANGUAGE_ID = 24;
    public static final int JA_LANGUAGE_ID = 18;

    /**
     * 自定义对话指令
     */
    public static final String CUSTOM_COMMAND_DIALOGUE = "dialogue_command";
    /**
     * 自定义轮播图指令
     **/
    public static final String CUSTOM_COMMAND_BANNER = "banner_command";
    /**
     * 自定义广告指令
     **/
    public static final String CUSTOM_COMMAND_ADVERT = "advert_command";
    /**
     * 自定义从机返回首页指令
     **/
    public static final String CUSTOM_COMMAND_FOLLOW_FINISH = "follow_finish_command";
    /**
     * 自定义允许从机说话控制指令
     **/
    public static final String CUSTOM_COMMAND_OPEN_FOLLOW_SPEAK = "open_follow_speak_command";
    /**
     * 自定义关闭从机说话控制指令
     **/
    public static final String CUSTOM_COMMAND_CLOSE_FOLLOW_SPEAK = "close_follow_speak_command";
    /**
     * 自定义打开检查询问
     **/
    public static final String CUSTOM_COMMAND_OPEN_EXAMINATION = "open_examination_command";
    /**
     * 共同关机
     **/
    public static final String CUSTOM_COMMAND_SHUTDOWN_TOGETHER = "shutdown_together_command";
    /**
     * 自定义打开真人翻译命令
     **/
    public static final String CUSTOM_COMMAND_HUMAN_TRANSLATION = "human_translation_command";
    /**
     * 从机通知主机正在说话
     **/
    public static final String CUSTOM_COMMAND_FELLOW_SPEAKING_MESSAGE = "fellow_speaking_message";
    /**
     * 从机通知告诉主机停止说话
     **/
    public static final String CUSTOM_COMMAND_FELLOW_STOP_SPEAKING_MESSAGE = "fellow_stop_speaking_message";
    /**
     * 主机通知从机更新常用语
     **/
    public static final String CUSTOM_COMMAND_FELLOW_UPDATE_QUICK_WORD = "fellow_update_quick_word_command";

    /**
     * 主机控制从机进行录像执法
     **/
    public static final String CUSTOM_COMMAND_FELLOW_START_RECORD_VIDEO = "fellow_start_record_video";

    /**
     * 主机控制从机关闭录像执法
     **/
    public static final String CUSTOM_COMMAND_FELLOW_STOP_RECORD_VIDEO = "fellow_stop_record_video";


    /**
     * 从机反馈主机录制结果
     **/
    public static final String CUSTOM_COMMAND_RECORD_RESULT = "fellow_record_result";

    public static final String CUSTOM_FELLOW_REPLY = "fellow_reply";

}
