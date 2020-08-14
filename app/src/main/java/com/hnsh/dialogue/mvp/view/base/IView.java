package com.hnsh.dialogue.mvp.view.base;

import android.content.Intent;

/**
 * Created by jess on 16/4/22.
 */
public interface IView {

    void showLoading();

    void hideLoading();

    void showMessage(String var1);

    void launchActivity(Intent var1);

    void launchActivityForResult(Intent var1, int var2);

    void killMyself();

    void showLoading(int seconds);
}
