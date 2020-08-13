package com.hnsh.dialogue.recognizer;

import android.content.Context;

import com.dosmono.logger.Logger;
import com.dosmono.model.ai.translate.ICallback;
import com.dosmono.model.ai.translate.TranslateModel;
import com.dosmono.model.ai.translate.TranslateReply;

import org.jetbrains.annotations.Nullable;

/**
 * 翻译
 */
public abstract class TranslateImpl {
    private Context mContext;
    private TranslateModel mModel;

    public TranslateImpl(Context context) {
        mContext = context;
    }

    public void start(String text, int srcLang, int dstLang) {
        mModel = TranslateModel.Companion.creator(mContext)
                .session(0)
                .text(text)
                .srcLangId(srcLang)
                .dstLangId(dstLang)
                .callback(translateCallback)
                .build();
        mModel.start();
    }

    public void stop() {
        if(mModel != null) mModel.stop();
    }

    public void destroy() {
        if(mModel != null) mModel.destroy();
    }

    private ICallback translateCallback = new ICallback() {

        @Override
        public void onResult(@Nullable TranslateReply result) {
            Logger.i("translate result : " + result);
            onTranslateResult(result);
        }

        @Override
        public void onError(int state, int session) {
            Logger.w("translate error : " + state);
        }
    };

    public abstract void onTranslateResult(TranslateReply result);
}
