package com.hnsh.dialogue.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hnsh.dialogue.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 水波纹特效
 * Created by fbchen2 on 2016/5/25.
 */
public class WaveView extends View {
    private float mInitialRadius;   // 初始波纹半径
    private float mMaxRadius;   // 最大波纹半径
    private long mDuration = 2000; // 一个波纹从创建到消失的持续时间
    private int mSpeed = 500;   // 波纹的创建速度，每500ms创建一个
    private float mMaxRadiusRate = 0.85f;
    private boolean mMaxRadiusSet;

    private boolean mIsRunning;

    private Context mContext;

    private long mLastCreateTime;
    private List<Circle> mCircleList = new ArrayList<Circle>();

    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                newCircle();
                postDelayed(mCreateCircle, mSpeed);
            }
        }
    };

    private Interpolator mInterpolator = new LinearInterpolator();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private Bitmap mBitmap;
    public WaveView(Context context) {
        super(context);
        this.mContext = context;
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mCirclePaint.setAntiAlias(true);//抗锯齿
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
    }

    public void setStyle(Paint.Style style) {
        mPaint.setStyle(style);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!mMaxRadiusSet) {
            mMaxRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f;
        }
    }

    public void setMaxRadiusRate(float maxRadiusRate) {
        mMaxRadiusRate = maxRadiusRate;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setBitmap(Bitmap bitmap){
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //记得判空
                if(palette==null)return;
                int bgColors[] = new int[2];
                bgColors[0] = 0xffea5928;
                bgColors[1] = 0xffea5928;
                //palette取色不一定取得到某些特定的颜色，这里通过取多种颜色来避免取不到颜色的情况
                if (palette.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                    bgColors[0] = palette.getDarkVibrantColor(Color.TRANSPARENT);
                    bgColors[1] = palette.getVibrantColor(Color.TRANSPARENT);
                } else if (palette.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT) {
                    bgColors[0] = palette.getDarkMutedColor(Color.TRANSPARENT);
                    bgColors[1] = palette.getMutedColor(Color.TRANSPARENT);
                } else {
                    bgColors[0] = palette.getLightMutedColor(Color.TRANSPARENT);
                    bgColors[1] = palette.getLightVibrantColor(Color.TRANSPARENT);
                }

                LinearGradient gradient=new LinearGradient(0, 0, 0, 75f,bgColors[0],bgColors[1], Shader.TileMode.CLAMP);
                mPaint.setShader(gradient);

                mPaint.setColor(bgColors[0]);
            }
        });
    }

    public void setFlag(Context context,String flag){
        Glide.with(context)
                .asBitmap()
                .load(flag)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mBitmap = resource;
                        invalidate();
                    }
                });
    }

    /**
     * 开始
     */
    public void start() {
        if(mIsRunning){
            stopImmediately();
        }
        mIsRunning = true;
        mCreateCircle.run();
    }

    /**
     * 缓慢停止
     */
    public void stop() {
        mIsRunning = false;
    }

    /**
     * 立即停止
     */
    public void stopImmediately() {
        mIsRunning = false;
        mCircleList.clear();
        invalidate();
    }


    public void click(){
        if(mIsRunning){
            stopImmediately();
        }else {
            start();
        }
    }

    public boolean isStop(){
        return !this.mIsRunning;
    }




    protected void onDraw(Canvas canvas) {
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            float radius = circle.getCurrentRadius();
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.setAlpha(circle.getAlpha());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
            } else {
                iterator.remove();
            }
        }
        if (mCircleList.size() > 0) {
            postInvalidateDelayed(10);
        }

        mCirclePaint.setColor(mPaint.getColor());

        canvas.drawCircle(getWidth() / 2,getHeight() / 2, 75,mCirclePaint);
        mCirclePaint.setColor(Color.WHITE);
        canvas.drawCircle(getWidth() / 2,getHeight() / 2,20,mCirclePaint);
    }

    public void setInitialRadius(float radius) {
        mInitialRadius = radius;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setMaxRadius(float maxRadius) {
        mMaxRadius = maxRadius;
        mMaxRadiusSet = true;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    private void newCircle() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastCreateTime < mSpeed) {
            return;
        }
        Circle circle = new Circle();
        mCircleList.add(circle);
        invalidate();
        mLastCreateTime = currentTime;
    }

    private class Circle {
        private long mCreateTime;

        Circle() {
            mCreateTime = System.currentTimeMillis();
        }

        int getAlpha() {
            float percent = (getCurrentRadius() - mInitialRadius) / (mMaxRadius - mInitialRadius);
//            Log.e("huanghuang","per:"+percent + "  mInterpolator.getInterpolation(percent):"+ mInterpolator.getInterpolation(percent));
            float dd = mInterpolator.getInterpolation(percent);
            if(dd<0.5){
                dd=0.5f;
            }
            return (int) (255 - percent * 255);
        }

        float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return mInitialRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mInitialRadius);
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
    }

}
