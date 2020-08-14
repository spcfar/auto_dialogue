package com.hnsh.dialogue.factory.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

public class CommonDialog extends Dialog implements ICommonDialog {

    private TextView mTvContent;
    private Button mBtnCancle;
    private Button mBtnConfirm;

    public CommonDialog(@NonNull Context context) {
        super(context, R.style.dialog);

        setContentView(R.layout.dialog_common);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        initView();
    }

    private void initView() {
        mTvContent = findViewById(R.id.tv_dialog_content);
        mBtnCancle = findViewById(R.id.btn_dialog_cancel);
        mBtnConfirm = findViewById(R.id.btn_dialog_submit);
    }

    @Override
    public ICommonDialog setTitle(String title) {

        return this;
    }

    @Override
    public ICommonDialog isTitleShow(boolean show) {

        return this;
    }

    @Override
    public ICommonDialog setContent(String content) {
        mTvContent.setText(content);
        return this;
    }

    @Override
    public void setOnCancleClick(View.OnClickListener listener) {
        mBtnCancle.setOnClickListener(listener);
    }

    @Override
    public void setOnConfirmClick(View.OnClickListener listener) {
        mBtnConfirm.setOnClickListener(listener);
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
        mBtnCancle.setText(str);
        return this;
    }

    @Override
    public ICommonDialog setBtnConfirmText(String str) {
        mBtnConfirm.setText(str);
        return this;
    }

    @Override
    public ICommonDialog setBtnCancleVisible(int visible) {
        mBtnCancle.setVisibility(visible);
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
        mTvContent.setGravity(gravity);
        return this;
    }
}
