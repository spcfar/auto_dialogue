package com.hnsh.dialogue.mvp.presenters.base;

import android.content.Intent;

/**
 * Created by jess on 16/4/28.
 */
public interface IPresenter {

    void initData(Intent var1);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
