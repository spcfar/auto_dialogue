package com.hnsh.dialogue.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.hnsh.dialogue.mvp.presenters.base.IPresenter;
import com.hnsh.dialogue.mvp.view.base.IView;
import com.hnsh.dialogue.views.FloatBack;

/**
 * Created by Unpar on 17/9/22.
 */

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity, IView {

    protected final String TAG = this.getClass().getSimpleName();

    protected P mPresenter;

    private boolean isVisible = false;

    protected boolean isHideFloatBack = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            int layoutResid = initContextView(savedInstanceState);
            if (layoutResid != 0) {
                setContentView(layoutResid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupActivityComponent();
        initData(savedInstanceState);
        if (mPresenter != null) mPresenter.initData(getIntent());
    }

    @Override
    public void showLoading(int seconds) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        isVisible = true;
        //隐藏悬浮back键
        if (isHideFloatBack)
            FloatBack.Instance(this).addBlockActivity(this.getClass().getName()).hideFloatBall();
        if (mPresenter != null) mPresenter.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mPresenter != null) mPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPresenter != null) mPresenter.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        isVisible = false;
        if (mPresenter != null) mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) mPresenter.onDestroy();
        mPresenter = null;

//        CommonApp.INSTANCE.getRefWatcher().watch(this);
    }

    @Override
    public void showMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isVisible) {
                    Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void launchActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void launchActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void killMyself() {
        finish();
    }


}
