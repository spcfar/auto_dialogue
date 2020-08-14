package com.hnsh.dialogue.factory.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hnsh.dialogue.R;


/**
 * @项目名： DosPen
 * @包名： dialog
 * @文件名: CommonDialog
 * @创建者: Administrator
 * @创建时间: 2017/12/16 016 11:27
 * @描述： TODO
 */

public class CommonTwoDialog extends Dialog implements ICommonDialog {

    private TextView mDialog_title;
    private TextView mDialog_content;
    private TextView mDialog_cancel;
    private TextView mDialog_confirm;

    public CommonTwoDialog(@NonNull Context context) {
        super(context, R.style.dialog);

        setContentView(R.layout.dialog_common_two);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        initView();
    }

    private void initView() {
        mDialog_title = findViewById(R.id.tv_dialog_title);
        mDialog_content = findViewById(R.id.tv_dialog_content);
        mDialog_cancel = findViewById(R.id.tv_dialog_cancel);
        mDialog_confirm = findViewById(R.id.tv_dialog_confirm);
    }

    @Override
    public ICommonDialog setTitle(String title) {
        mDialog_title.setText(title);
        return this;
    }

    @Override
    public ICommonDialog isTitleShow(boolean show) {
        if (show) {
            mDialog_title.setVisibility(View.VISIBLE);
        } else {
            mDialog_title.setVisibility(View.GONE);
        }
        return this;
    }

    @Override
    public ICommonDialog setContent(String content) {
        mDialog_content.setText(content);
        return this;
    }

    @Override
    public void setOnCancleClick(View.OnClickListener listener) {
        mDialog_cancel.setOnClickListener(listener);
    }

    @Override
    public void setOnConfirmClick(View.OnClickListener listener) {
        mDialog_confirm.setOnClickListener(listener);
    }


    @Override
    public ICommonDialog dialogShow() {
        show();
        return this;
    }

    @Override
    public void dialogDismiss() {
        dismiss();
    }

    @Override
    public ICommonDialog setBtnCancleText(String str) {
        mDialog_cancel.setText(str);
        return this;
    }

    @Override
    public ICommonDialog setBtnConfirmText(String str) {
        mDialog_confirm.setText(str);
        return this;
    }

    @Override
    public ICommonDialog setBtnCancleVisible(int visible) {
        mDialog_cancel.setVisibility(visible);
        return this;
    }

    @Override
    public ICommonDialog setType(int type) {
        getWindow().setType(type);
        return this;
    }

    @Override
    public ICommonDialog setCanceledTouchOutside(boolean cancle) {
        setCanceledOnTouchOutside(cancle);
        return this;
    }

    @Override
    public ICommonDialog setContentGravity(int gravity) {
        mDialog_content.setGravity(gravity);
        return this;
    }
}
