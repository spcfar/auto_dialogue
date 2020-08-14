package com.hnsh.dialogue.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dosmono.logger.Logger;
import com.dosmono.universal.thread.TimeoutManager;
import com.hnsh.dialogue.R;
import com.hnsh.dialogue.factory.DialogFactory;
import com.hnsh.dialogue.mvp.models.PrefsSettingDefaultModel;
import com.hnsh.dialogue.mvp.presenters.base.IPresenter;
import com.hnsh.dialogue.services.TimeoutCallback;
import com.hnsh.dialogue.utils.TUtils;

/**
 * @项目名： inspection
 * @包名： com.dosmono.dialogue.activitys
 * @文件名: HeaderActivity
 * @创建者: zer
 * @创建时间: 2018/6/26 17:02
 * @描述： TODO
 */
public abstract class HeaderActivity<P extends IPresenter> extends BaseActivity<P> {

    protected Context mContext;
    private Dialog mLoadingDialog;
    private int LOADING_TASK = 0x18;
    protected TimeoutCallback timeoutCallback;

    private TimeoutManager<Integer> timeoutManager = new TimeoutManager<>(new TimeoutManager.ICallback<Integer>() {
        @Override
        public void onTimeout(Integer integer) {
            hideLoading();
            if (timeoutCallback != null) {
                timeoutCallback.onTimeout();
            }
        }
    });

    @Override
    public void setupActivityComponent() {
        this.mContext = this;
    }

    @Override
    public void showLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == mLoadingDialog) {
                    mLoadingDialog = DialogFactory.createLoadingDialog(mContext, false);
                }

                if (!mLoadingDialog.isShowing()) {
                    mLoadingDialog.show();
                }
                timeoutManager.addTask(LOADING_TASK, 8000);
            }
        });

    }

    @Override
    public void showLoading(final int seconds) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == mLoadingDialog) {
                    mLoadingDialog = DialogFactory.createLoadingDialog(mContext, false);
                }

                if (!mLoadingDialog.isShowing()) {
                    mLoadingDialog.show();
                }
                timeoutManager.addTask(LOADING_TASK, seconds * 1000);
            }
        });

    }

    @Override
    public void hideLoading() {
        Logger.d("============hideLoading");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
                        mLoadingDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        timeoutManager.cancel(LOADING_TASK);
    }

    @Override
    public void showMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TUtils.showToast(message);
            }
        });
    }

    public void setTitleLeft(String leftText) {
//        LinearLayout header_back = findViewById(R.id.ll_header_back);
//        TextView left_text = findViewById(R.id.tv_header_left_text);
//        left_text.setText(leftText);
//        header_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }


    public int getLocalRole() {
        return PrefsSettingDefaultModel.INSTANCE().getUsePattern();
    }

}
