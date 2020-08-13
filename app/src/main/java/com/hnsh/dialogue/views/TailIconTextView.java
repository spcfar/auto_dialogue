package com.hnsh.dialogue.views;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unpar on 17/12/22.
 */

public class TailIconTextView extends AppCompatTextView {

    private String showText;
    private Drawable showImage;
    private Rect imgRect;
    private SpanInfo spanInfo;

    private Handler mHandler = new Handler();

    class SpanInfo {
        int delay, count, index;
        List<Drawable> images;

        SpanInfo() {
            delay = 300;
            count = index = 0;
            images = new ArrayList<>();
        }
    }

    public TailIconTextView(Context context) {
        super(context);
    }

    public TailIconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TailIconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String text, Drawable drawable, Rect bounds) {
        showText = text;
        showImage = drawable;
        if(showImage != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(showText + "  ");
            ImageSpan span = new ImageSpan(showImage);
            showImage.setBounds(bounds);
//            showImage.setBounds(5, -15, 42, 22);
            imgRect = showImage.getBounds();
            builder.setSpan(span, showText.length(), showText.length()+1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

            setText(builder);
        }else {
            setText(showText);
        }
    }

    public void startImageAnim() {
        stopImageAnim();

        if(showImage instanceof AnimationDrawable) {
            spanInfo = new SpanInfo();
            spanInfo.count = ((AnimationDrawable) showImage).getNumberOfFrames();
            if(spanInfo.count > 0) {
                spanInfo.delay = ((AnimationDrawable) showImage).getDuration(0);
                spanInfo.index = 0;
                for(int i=0; i<spanInfo.count; i++) {
                    Drawable drawable = ((AnimationDrawable) showImage).getFrame(i);
                    spanInfo.images.add(drawable);
                }
                mHandler.post(runnable);
            }
        }
    }

    public void stopImageAnim() {
        mHandler.removeCallbacks(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(spanInfo != null && spanInfo.count > 0) {
                int index = spanInfo.index + 1;
                if(index >= spanInfo.count) index = 0;
                spanInfo.index = index;
                Drawable drawable = spanInfo.images.get(index);
                drawable.setBounds(imgRect);
                ImageSpan span = new ImageSpan(drawable);

                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(showText + "  ");
                builder.setSpan(span, showText.length(), showText.length()+1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
                setText(builder);

                mHandler.postDelayed(runnable, spanInfo.delay);
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        stopImageAnim();
    }
}
