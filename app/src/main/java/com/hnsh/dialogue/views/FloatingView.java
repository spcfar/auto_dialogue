package com.hnsh.dialogue.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.hnsh.dialogue.R;
import com.hnsh.dialogue.utils.UIUtils;

public class FloatingView extends View {


    public static int height = 64;
    public static int width = 64;
    private Paint paint;


    public FloatingView(Context context){
        super(context);
        paint = new Paint();
        height = UIUtils.dip2Px(context,64);
        width = UIUtils.dip2Px(context,64);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(height,width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
        //画大圆
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.state_one));
        canvas.drawCircle(width/2,width/2,width/2,paint);
        //画小圆圈
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(width/2,width/2, (float) (width*1.0/4),paint);
         **/
        paint.setAntiAlias(true);
        BitmapDrawable bmpDraw=(BitmapDrawable)getResources().getDrawable(R.mipmap.back_key,null);
        Bitmap bmp=bmpDraw.getBitmap();
        canvas.drawBitmap(bmp,0,0,paint);
    }

}
