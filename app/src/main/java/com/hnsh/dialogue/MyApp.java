package com.hnsh.dialogue;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.dosmono.library.iflytek.Iflytek;
import com.dosmono.logger.Logger;
import com.dosmono.universal.app.DeviceInfo;
import com.dosmono.universal.app.Framework;
import com.dosmono.universal.common.Constant;
import com.hnsh.dialogue.utils.CommonUtil;
import com.hnsh.dialogue.utils.UIUtils;
import com.hnsh.dialogue.utils.UUIDUtils;

/**
 * @author lingu
 * @create 2019/12/3 8:37
 * @Describe
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("dialoguedemo");
        initFramework();
        UIUtils.setContext(this);
    }

    private void initFramework() {
        Iflytek.INSTANCE.initIflytek(getApplicationContext());
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setPushUrl(Constant.MPUSH_URL)
                .setPushAccount(UUIDUtils.initUUID(this))
                .setPushDevid("543142001000076")  //.setPushDevid()CommonUtil.getDeviceId()
                .setPushTags("push-" + System.currentTimeMillis())
                .setSupportBpush(true)

                .setOnlyMarkType(Constant.ID_TYPE_SN)
                .setIdentification("543142001000076")  //.setIdentification(CommonUtil.getDeviceId())
                .setDevType(Constant.DEV_PARTNER)
                .setAutoUpdateConfig(true);
        Framework.INSTANCE.initFramework(this, deviceInfo);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
