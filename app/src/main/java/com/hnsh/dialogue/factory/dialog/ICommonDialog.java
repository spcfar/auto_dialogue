package com.hnsh.dialogue.factory.dialog;

import android.view.View;

/**
 * @项目名： DosPen
 * @包名： dialog
 * @文件名: ICommonDialog
 * @创建者: Administrator
 * @创建时间: 2017/12/16 016 11:24
 * @描述： TODO
 */

public interface ICommonDialog {

    ICommonDialog setTitle(String title);
    ICommonDialog isTitleShow(boolean show);
    ICommonDialog setContent(String content);
    void setOnCancleClick(View.OnClickListener listener);
    void setOnConfirmClick(View.OnClickListener listener);
    ICommonDialog dialogShow();
    void dialogDismiss();
    ICommonDialog setBtnCancleText(String str);
    ICommonDialog setBtnConfirmText(String str);
    ICommonDialog setBtnCancleVisible(int visible);
    ICommonDialog setType(int type);
    ICommonDialog setCanceledTouchOutside(boolean cancle);
    ICommonDialog setContentGravity(int gravity);
}
