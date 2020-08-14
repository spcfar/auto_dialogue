package com.hnsh.dialogue.mvp.presenters.base;

import android.content.Intent;

import com.hnsh.dialogue.mvp.models.base.IModel;
import com.hnsh.dialogue.mvp.view.base.IView;

/**
 * Created by jess on 16/4/28.
 */
public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter {

    protected final String TAG = this.getClass().getSimpleName();

    protected M mModel;
    protected V mRootView;

    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mRootView = rootView;
    }

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
    }

    public BasePresenter() {

    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void initData(Intent var1) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {
        if (mModel != null)
            mModel.onDestroy();
        this.mModel = null;
        this.mRootView = null;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    public boolean useEventBus() {
        return true;
    }


    public void register(Object object) {
    }

    public void unregister(Object object) {
    }


}
