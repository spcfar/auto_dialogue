package com.hnsh.dialogue.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.dosmono.logger.Logger;
import com.dosmono.universal.common.Constant;
import com.dosmono.universal.thread.ExecutorManager;
import com.hnsh.dialogue.R;
import com.hnsh.dialogue.bean.db.DialogueDbHelper;
import com.hnsh.dialogue.constants.TSRConstants;
import com.hnsh.dialogue.factory.DialogFactory;
import com.hnsh.dialogue.factory.dialog.ICommonDialog;
import com.hnsh.dialogue.ui.HeaderActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * @项目名： BizInspection
 * @包名： com.dosmono.common.utils
 * @文件名: CheckStateUtil
 * @创建者: zer
 * @创建时间: 2018/7/12 11:27
 * @描述： TODO
 */
public class CheckStateUtil {

    public static boolean checkClickState(Context context){
        if (!NetUtils.isConnected(context)){
            showNetDialog(context,context.getString(R.string.common_net_error_two));
            return false;
        }
        ArrayList<SDUtils.SDCardStat> SdCardStatList = SDUtils.getSDCardStats(context);
        if(SdCardStatList!=null && SdCardStatList.size()>=1){
            String freesize = SDUtils.floatFormat((float)(SdCardStatList.get(0).freeSize/1024/1024));
            if (Float.parseFloat(freesize) <= 500){
                showStorageFullDialog(context);
                return false;
            }
        }

        return true;
    }

    public static boolean checkBindState(Context context){
        int bindState = PrefsUtils.getPrefs(context, TSRConstants.BIND_RELATION_FLAG, -1);
        if (bindState == TSRConstants.BIND_STATE_UNBIND || bindState == TSRConstants.BIND_STATE_NONE){
            return false;
        }
        return true;
    }

    private static long CurrentClickTime = 0;
    private static long LastClickTime = 0;
    private static long ClickInterval = 500;
    public static boolean checkDoubleClickState(){
        CurrentClickTime = System.currentTimeMillis();
        boolean result = CurrentClickTime - LastClickTime > ClickInterval;
        Logger.d("[ debug ] checkDoubleClickState resutl = "+result+",CurrentClickTime = "+CurrentClickTime+",LastClickTime = "+LastClickTime);
        LastClickTime = CurrentClickTime;
        return result;
    }

    private static void showNetDialog(final Context context, String str){
        final ICommonDialog iCommonDialog = DialogFactory.createCommonTwoDialog(context)
                                                         .setContent(str)
                                                         .setBtnCancleText(context.getString(R.string.cancel))
                                                         .setBtnConfirmText(context.getString(R.string.set_net))
                                                         .setCanceledTouchOutside(false)
                                                         .dialogShow();
        iCommonDialog.setOnCancleClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCommonDialog.dialogDismiss();
            }
        });
        iCommonDialog.setOnConfirmClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCommonDialog.dialogDismiss();
                Intent intent = new Intent(TSRConstants.EXTRA_INTENT_ACTION_SETTING);
                intent.putExtra(TSRConstants.KEY_FLAG,0);
                ((HeaderActivity)context).startActivityForResult(intent,TSRConstants.EXTRA_REQUEST_CODE_SETTING);
            }
        });
    }

    private static void showBindDialog(final Context context, String str) {
        final ICommonDialog iCommonDialog = DialogFactory.createCommonTwoDialog(context)
                                                         .setContent(str)
                                                         .setBtnCancleText(context.getString(R.string.cancel))
                                                         .setBtnConfirmText(context.getString(R.string.pair))
                                                         .setCanceledTouchOutside(false)
                                                         .dialogShow();
        iCommonDialog.setOnCancleClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCommonDialog.dialogDismiss();
            }
        });
        iCommonDialog.setOnConfirmClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCommonDialog.dialogDismiss();
                Intent intent = new Intent(TSRConstants.EXTRA_INTENT_ACTION_SETTING);
                intent.putExtra(TSRConstants.KEY_FLAG,2);
                ((HeaderActivity)context).startActivityForResult(intent,TSRConstants.EXTRA_REQUEST_CODE_SETTING);
            }
        });
    }

    private static void showStorageFullDialog(final Context context){
        final ICommonDialog iCommonDialog = DialogFactory.createCommonTwoDialog(context)
                                                         .setContent(context.getString(R.string.dialogue_storage_full_tips))
                                                         .setBtnCancleText(context.getString(R.string.dialogue_storage_full_clean))
                                                         .setBtnConfirmText(context.getString(R.string.cancel))
                                                         .setCanceledTouchOutside(false)
                                                         .dialogShow();
        iCommonDialog.setOnCancleClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCommonDialog.dialogDismiss();
                ExecutorManager.Companion.build().getCommonThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        SDUtils.deleteFile(new File(Constant.ROOT_PATH));
                        DialogueDbHelper.INSTANCE().deleteAll();
                        BroadcastsUtil.sendStorageRefreshBroacast(context);
                        BroadcastsUtil.sendLocalBoastcasts(BroadcastsUtil.EXTRA_ACTION_CLEANUP,context);
                    }
                });
            }
        });
        iCommonDialog.setOnConfirmClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCommonDialog.dialogDismiss();
            }
        });
    }
}
