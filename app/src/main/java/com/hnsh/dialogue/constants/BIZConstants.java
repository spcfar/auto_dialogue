package com.hnsh.dialogue.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * biz的常量管理
 */
public final class BIZConstants {

    /**
     * 有设备信息相关的常量
     */
    public final static class Device {
        //主机角色
        public final static int HOST_ROLE = 1;
        //从机角色
        public final static int FELLOW_ROLE = 0;

        public final static String KEY_SESSION_ID = "sessionId";

        public static boolean UPDATE_DESKTOP_PICTURE = false;


    }


    public final static class SP {

        public final static String SP_SMDESKTOP = "SMdesktop";

        public final static String SP_SMSTANDBY = "SMstandby";

        public final static String SP_ADVERT = "advert";
        //广告图片
        public final static String SP_PICTURE_PATH = "adv_pics_json";
        //待机图片
        public final static String SP_SCREEN_OFF_PICTURE_PATH = "screen_off_picture_json";
        //桌面图片
        public final static String SP_FELLOW_DESKTOP_PICTURE_PATH = "desktop_picture_json";

        public final static String SP_NEED_UPDATE_FELLOW_DESKTOP = "update_fellow_desktop";

        public static String THIRD_PART_DEVICEID = "third_part_device_id";

        public final static String SP_REQUEST_TIME_WHEN_SCREEN_OFF = "request_time_when_screen_off";
        public final static String SP_CHECK_UPGRADE_WHEN_SCREEN_OFF = "check_upgrade_when_screen_off";


        /**
         * =========首页模块化
         **/
        public static final String SP_HOME_WEB_CONFIG_ID = "sp_template_web_configId";
        public static final String SP_TEMPLATE_THEME_INFO = "sp_key_template_theme_json";
        /**======end 首页模块化**/


        //第一次启动app
        public static final String SP_FIRST_APP = "sp_first_app_key";
    }


    /**
     * 三亚后台http请求常量
     */
    public final static class Dmonkey {

        public final static String URL_BASE = "http://www.sh.dmonkey.cn/shuanghou/";
        //        public final static String URL_BASE = "http://192.168.50.109:8089/shuanghou/";
//        public final static String URL_BASE = "http://192.168.50.109:8088/shuanghou/";
//        public final static String URL_BASE = "";
        //发送安装的应用版本信息
        public final static String URL_SEND_DEV_APP_VER = "api/device/devApplication";
        //发送固件版本信息
        public final static String URL_SEND_DEV_FIRM_VER = "api/device/firmVersion";

        //模板中apk的数据接口
        public final static String URL_GET_TEMPLATE_PACKAGETS = "api/template/templatePackages";

        //获取 常用语 快捷用语版本号
        public final static String URL_GET_QA_PHRASE_VER = "api/commConfig/getVersion";
        //常用问答
        public final static String URL_GET_COMMON_QA = "LanguageSwag/quickQuestion/list";
        //快捷用语
        public final static String URL_GET_COMMON_PHRASE = "api/usedQuestion/getUsedQuestionData";
        //对话翻译统计数据（包括 选择语言，快捷用语，常用问答）
        public final static String URL_SEND_DIALOGUE_STAT_DATA = "api/StatisticsSwag/statDialogue";
        //真人翻译 使用统计数据
        public final static String URL_SEND_HUMAN_TRANSLATION_STAT_DATA = "api/StatisticsSwag/statArtificial";
        //心跳包
        public final static String URL_SEND_HEART_BEAT = "api/device/heartbeatPacket";

        //发送经纬度
        public final static String URL_SEND_LOCATION_DATA = "api/device/devPoint";

        //保存录制视频的地址
        public final static String URL_SAVE_UPLOAD_RECORD_VIDEO_URL = "api/v1/videofile/info/savevideo";

        //开机初始化
        public final static String URL_TEMPLATE_FIND_CONFIG = "api/template/findConfig";

        //后台秘钥
        public final static String DEVICE_SECRET_KEY = "l3hOFg3eddhVFEIoZd7Loe9qsUp9d7yP";
        //快捷用语 key
        public final static String KEY_QUICK_PHRASE_VERSION = "usedQVersion";
        //常用问答 key
        public final static String KEY_COMMON_QA_VERSION = "quickQVersion";

        public final static String KEY_CHANNELID = "channelId";

        public final static String KEY_EXAMINATION_PAGEAGE = "com.dosmono.hnsh.test";
        public final static String KEY_HUMAN_TRANSLATION_PACKAGE = "com.iol8.te_hw";

        public final static String KEY_DIALOG_TRANSLATION = "com.dosmono.translator";
        public final static String KEY_ENABLE_PRO = "Enable_";

        public final static Map<String, Integer> PHRASE_STAT = new HashMap();
        public final static Map<String, Integer> QA_STAT = new HashMap();
        public final static Map<String, Integer> FELLOW_LANG_STAT = new HashMap();
        public static int EXMINATION_STAT = 0;

        //获取首页菜单数据
        public final static String URL_MAIN_MENU_DATA = "api/template/getTemplateInfo";
    }

}
