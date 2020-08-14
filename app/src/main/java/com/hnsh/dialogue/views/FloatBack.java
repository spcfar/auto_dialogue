package com.hnsh.dialogue.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.hnsh.dialogue.utils.ActivityUtil;
import com.hnsh.dialogue.utils.SystemOperationUtils;

import java.util.ArrayList;
import java.util.List;

import static android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
import static android.view.WindowManager.LayoutParams.TYPE_TOAST;

/**
 * 悬浮的返回键
 */
public class FloatBack {


    private FloatingView mFloatBall;
    private WindowManager mWindowManager;
    private static FloatBack INSTANCE;
    private Context mContext;
    private WindowManager.LayoutParams mFloatBallParams;

    private boolean isVisible = false;

    private FloatBack(Context context) {
        this.mContext = context;

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mFloatBallParams = new WindowManager.LayoutParams();
        mFloatBallParams.width = FloatingView.width;
        mFloatBallParams.height = FloatingView.height;
        mFloatBallParams.gravity = Gravity.CENTER_VERTICAL| Gravity.RIGHT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mFloatBallParams.type = TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mFloatBallParams.type = TYPE_TOAST;
        } else {
            mFloatBallParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }


        mFloatBallParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mFloatBallParams.format = PixelFormat.RGBA_8888;
    }

    public static FloatBack Instance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FloatBack(context);
        }
        return INSTANCE;
    }

    public FloatBack addBlockActivity(String activity){
        this.blockActivity.add(activity);
        return this;
    }

    public void showFloatBall() {
        if(!isVisible)
            buildFloatBall();
    }

    public void hideFloatBall() {
        if(!isVisible)
            return;
        if (mWindowManager != null && mFloatBall != null) {
            mWindowManager.removeView(mFloatBall);
            mFloatBall =  null;
        }
        isVisible = false;
    }

    private boolean canPress = true;
    private List<String> blockActivity = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //
            if(blockActivity.contains(ActivityUtil.getTopActivity(mContext))){
                if(isVisible)
                    hideFloatBall();
            }
            canPress = true;
        }
    };

    private void buildFloatBall() {
        mFloatBall = new FloatingView(mContext);
        mWindowManager.addView(mFloatBall, mFloatBallParams);
        isVisible = true;
        mFloatBall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!canPress) {
                    return false;
                }
                canPress = false;
                SystemOperationUtils.goBack();
                handler.sendMessageDelayed(Message.obtain(), 500);
                return false;
            }
        });
    }

}
