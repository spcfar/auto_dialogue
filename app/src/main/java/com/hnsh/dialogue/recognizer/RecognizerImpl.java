package com.hnsh.dialogue.recognizer;

import android.content.Context;
import android.os.Environment;

import com.dosmono.logger.Logger;
import com.dosmono.model.ai.AIConstants;
import com.dosmono.model.ai.recognizer.ICallback;
import com.dosmono.model.ai.recognizer.IVolumeCallback;
import com.dosmono.model.ai.recognizer.RecognizerModel;
import com.dosmono.model.ai.recognizer.RecognizerReply;

import org.jetbrains.annotations.Nullable;

/**
 * 语言识别
 */
public abstract class RecognizerImpl {
    private static final String AUDIO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dosmono/stt.mp3";
    private Context mContext;
    private RecognizerModel mModel;


    public RecognizerImpl(Context context) {
        mContext = context;
    }

    public void start(int langId) {
        mModel = RecognizerModel.Companion.creator(mContext)
                .session(0)
                .langId(langId)
                .recognizerMode(AIConstants.RECOGNIZER_MODE_SHORT)
                .saveFile(true)
                .encodeWay(AIConstants.INSTANCE.getAUDIO_ENCODE_MP3())
                .filePath(AUDIO_PATH)
                .callback(recognizerCallback)
                .volumeCallback(new IVolumeCallback() {
                    @Override
                    public void onVolume(int i) {
                        onChangeVolume(i);
                    }
                })
                .build();
        mModel.start();
    }

    public void stop() {
        if(mModel != null) mModel.stop();
    }

    public void destroy() {
        if(mModel != null) mModel.destroy();
    }

    private ICallback recognizerCallback = new ICallback() {
        @Override
        public void onResult(@Nullable RecognizerReply reply) {
            Logger.i("recognizer result : " + reply);
            onRecognizerResult(reply);
        }

        @Override
        public void onEncodeFile(@Nullable String path) {
            Logger.i("recognizer file : " + path);
        }

        @Override
        public void onError(int state) {
            Logger.w("recognizer error : " + state);
        }
    };



    public abstract void onRecognizerResult(RecognizerReply result);
    public abstract void onChangeVolume(int volumeCount);
    public abstract void onError(int state);

}
