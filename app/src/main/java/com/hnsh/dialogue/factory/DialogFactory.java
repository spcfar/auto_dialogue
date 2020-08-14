package com.hnsh.dialogue.factory;

import android.app.Dialog;
import android.content.Context;

import com.hnsh.dialogue.R;
import com.hnsh.dialogue.factory.dialog.CommonDialog;
import com.hnsh.dialogue.factory.dialog.CommonTwoDialog;
import com.hnsh.dialogue.factory.dialog.ICommonDialog;

/**
 * @项目名： Translator
 * @包名： com.dosmono.common.factory
 * @文件名: DialogFactory
 * @创建者: Administrator
 * @创建时间: 2018/3/7 007 17:43
 * @描述： TODO
 */

public class DialogFactory {

    public static ICommonDialog crateCommonDialog(Context context){
        return new CommonDialog(context);
    }

    public static ICommonDialog createCommonTwoDialog(Context context){
        return new CommonTwoDialog(context);
    }

    public static Dialog createLoadingDialog(Context context, boolean isCanceledTouch){
        Dialog loadingDialog = new Dialog(context, R.style.progress_dialog);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(isCanceledTouch);
        loadingDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        return loadingDialog;
    }
}
