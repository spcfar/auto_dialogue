package com.hnsh.dialogue.recognizer;

import android.content.Context;
import android.os.Environment;

import com.dosmono.logger.Logger;
import com.dosmono.model.ai.synthesis.ICallback;
import com.dosmono.model.ai.synthesis.SynthesisModel;
import com.dosmono.model.ai.synthesis.SynthesisReply;

import org.jetbrains.annotations.Nullable;

/**
 * 语音播放
 */
public abstract class SynthesisImpl {
    private static final String AUDIO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dosmono/tts.mp3";
    private Context mContext;
    private SynthesisModel mModel;

    public SynthesisImpl(Context context) {
        mContext = context;
    }

    public void start(String text, int langId) {
        Logger.d("SynthesisImpl:"+text + "  langId:"+langId);
        mModel = SynthesisModel.Companion.creator(mContext)
                .session(0)
                .text(text)
                .langId(langId)
                .audioPath(AUDIO_PATH)
                .callback(synthesisCallback)
                .build();
        mModel.start();
    }

    public void stop() {
        if(mModel != null) mModel.stop();
    }

    public void destroy() {
        if(mModel != null) mModel.destroy();
    }

    private ICallback synthesisCallback = new ICallback() {

        @Override
        public void onAudio(@Nullable SynthesisReply reply) {
            Logger.d("SynthesisImpl:onAudio");
        }

        @Override
        public void onStarted(int session) {
            Logger.d("SynthesisImpl:onStarted-->"+session);
        }

        @Override
        public void onFinished(int session) {
            Logger.d("SynthesisImpl:onFinished-->"+session);
            onSynFinished(session);
        }

        @Override
        public void onError(int state, int session) {
            Logger.w("synthesis error : " + state);
        }
    };

    public abstract void onSynFinished(int session);
}
