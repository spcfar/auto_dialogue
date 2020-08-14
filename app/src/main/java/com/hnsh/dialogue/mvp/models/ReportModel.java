package com.hnsh.dialogue.mvp.models;

import android.content.Context;
import com.dosmono.logger.Logger;
import com.dosmono.universal.common.Constant;
import com.dosmono.universal.entity.push.ReportInfo;
import com.dosmono.universal.entity.push.ReportReply;
import com.dosmono.universal.push.report.IReportCallback;
import com.dosmono.universal.push.report.ReportHepler;
import com.dosmono.universal.utils.NetworkUtils;
import com.dosmono.universal.utils.UUIDHelper;
import com.dosmono.universal.utils.Utils;
import com.hnsh.dialogue.utils.CommonUtil;
import com.hnsh.dialogue.utils.PrefsUtils;

/**
 * Created by <Yang Tao> on <18/2/8>.
 */

public class ReportModel {
    private static final String PREFS_REPORT_STATE = "ReportInfoState";
    private Context mContext;

    public ReportModel(Context context) {
        mContext = context;
        ReportHepler.INSTANCE.setCallback(new IReportCallback() {
            @Override
            public void onReport(int state, ReportReply reportReply) {
                if(null != reportReply) {
                    PrefsUtils.setPrefs(mContext, PREFS_REPORT_STATE, true);
                }
            }
        });
    }

    public boolean report() {
        boolean state = true;
            ReportInfo info = new ReportInfo();
            info.setProductModel(CommonUtil.getProductModel());
            info.setBluetooth(Utils.INSTANCE.getBluetoothMac());
            info.setIMEI(Utils.INSTANCE.getIMEI(mContext));
            info.setSerialNumber(CommonUtil.getDeviceId());
            info.setUniqueIdentification(Constant.ID_TYPE_SN);
            info.setWirelessMac(NetworkUtils.INSTANCE.getMacAddress("wlan"));
            info.setUuid(UUIDHelper.INSTANCE.getUUID(mContext));
            info.setDeviceName("Translator");

            Logger.i("reportinfo:" + info.toString());

            state = ReportHepler.INSTANCE.reportDeviceInfo(info);
        return state;
    }
}
