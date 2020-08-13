package com.hnsh.dialogue.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.dosmono.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author lingu
 * @create 2019/12/4 18:23
 * @Describe
 */
public class DisplayUtils {
    /**
     * convert px to its equivalent dp
     * <p>
     * 将px转换为与之相等的dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * convert dp to its equivalent px
     * <p>
     * 将dp转换为与之相等的px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * convert px to its equivalent sp
     * <p>
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 获得图片的像素方法
     *
     * @param bitmap
     */

    public static ArrayList<Integer> getPicturePixel(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // 保存所有的像素的数组，图片宽×高
        int[] pixels = new int[width * height];

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        ArrayList<Integer> rgb=new ArrayList<>();
        for (int i = 0; i < pixels.length; i++) {
            int clr = pixels[i];
            int red = (clr & 0x00ff0000) >> 16; // 取高两位
            int green = (clr & 0x0000ff00) >> 8; // 取中两位
            int blue = clr & 0x000000ff; // 取低两位
//            Log.d("tag", "r=" + red + ",g=" + green + ",b=" + blue);
            int color = Color.rgb(red, green, blue);
            //除去白色和黑色
            if (color!=Color.WHITE && color!=Color.BLACK){
                rgb.add(color);
            }
        }




        return rgb;
    }

    public static int getImageColor(Bitmap bitmap){
        ArrayList<Integer> picturePixel = getPicturePixel(bitmap);
        //计数相同颜色数量并保存
        HashMap<Integer,Integer> color2=new HashMap<>();
        for (Integer color:picturePixel){
            if (color2.containsKey(color)){
                Integer integer = color2.get(color);
                integer++;
                color2.remove(color);
                color2.put(color,integer);

            }else{
                color2.put(color,1);
            }
        }
        //挑选数量最多的颜色
        Iterator iter = color2.entrySet().iterator();
        int count=0;
        int color=0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int value = (int) entry.getValue();
            if (count<value){
                count=value;
                color= (int) entry.getKey();
            }

        }
        if (color==0){
            return 0xffea5928;
        }


        Logger.d("color:"+color);

        return color;
    }


}
