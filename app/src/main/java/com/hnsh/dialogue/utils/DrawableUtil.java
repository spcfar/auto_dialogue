package com.hnsh.dialogue.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * @author lingu
 * @create 2019/12/26 10:51
 * @Describe
 */
public class DrawableUtil {

    private DrawableUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     *
     * @param orientation  方向
     * @param cornerRadius 圆角
     * @param colors  渐变颜色数组
     * @return
     * 背景颜色
     */
    @SuppressLint("WrongConstant")
    public static Drawable getGradualChangeDrawable(GradientDrawable.Orientation orientation, int cornerRadius, int[] colors){
        GradientDrawable aDrawable=new GradientDrawable(orientation,colors);
        aDrawable.setCornerRadius(cornerRadius);
        aDrawable.setGradientType(GradientDrawable.RECTANGLE);
        return aDrawable;
    }

    /**
     * 矩形
     * @param orientation  方向
     * @param colors  渐变颜色数组
     * @return
     */
    public static Drawable getGradualChangeDrawable(GradientDrawable.Orientation orientation, int[] colors){
        return getGradualChangeDrawable(orientation,0,colors);
    }


    /**
     *
     * @param orientation 方向
     * @param cornerRadius 圆角
     * @param startColor 开始颜色
     * @param endColor 结束颜色
     * @return
     * 按钮背景 点击效果
     */
    public static StateListDrawable getGradualDrawable(GradientDrawable.Orientation orientation, int cornerRadius, int startColor, int endColor) {
        return getStateListDrawable(getSolidRectDrawable(cornerRadius,  colorDeep(startColor)) , getGradualChangeDrawable(orientation,cornerRadius,new int[]{startColor,endColor}));
    }

    public static StateListDrawable getGradualDrawable(GradientDrawable.Orientation orientation, int startColor, int endColor) {
        return getStateListDrawable(getSolidRectDrawable(0, colorDeep(startColor)) , getGradualChangeDrawable(orientation,0,new int[]{startColor,endColor}));
    }





    /**
     * 实体  状态选择器
     *
     * @param cornerRadius 圆角半径
     * @param pressedColor 按下颜色
     * @param normalColor  正常的颜色
     * @return 状态选择器
     */
    public static StateListDrawable getDrawable(int cornerRadius, int pressedColor, int normalColor) {
        return getStateListDrawable(getSolidRectDrawable(cornerRadius, pressedColor), getSolidRectDrawable(cornerRadius, normalColor));
    }



    /**
     * 实体 按下的颜色加深
     *
     * @param cornerRadius 圆角半径
     * @param normalColor  正常的颜色
     * @return 状态选择器
     */

    public static StateListDrawable getDrawable(int cornerRadius, int normalColor) {
        return getDrawable(cornerRadius, colorDeep(normalColor), normalColor);
    }





    /**
     * 得到实心的drawable, 一般作为选中，点中的效果
     *
     * @param cornerRadius 圆角半径
     * @param solidColor   实心颜色
     * @return 得到实心效果
     */
    private static GradientDrawable getSolidRectDrawable(int cornerRadius, int solidColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        // 设置矩形的圆角半径
        gradientDrawable.setCornerRadius(cornerRadius);
        // 设置绘画图片色值
        gradientDrawable.setColor(solidColor);
        // 绘画的是矩形
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);


        return gradientDrawable;
    }






    /**
     * 背景选择器
     *
     * @param pressedDrawable 按下状态的Drawable
     * @param normalDrawable  正常状态的Drawable
     * @return 状态选择器
     */
    private static StateListDrawable getStateListDrawable(Drawable pressedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
        //设置不能用的状态
        //默认其他状态背景
        GradientDrawable gray = getSolidRectDrawable(10, Color.GRAY);
        stateListDrawable.addState(new int[]{}, gray);
        return stateListDrawable;
    }









    /**
     * 加深颜色
     *
     * @param color 原色
     * @return 加深后的
     */
    public static int colorDeep(int color) {

        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        float ratio = 0.8F;

        red = (int) (red * ratio);
        green = (int) (green * ratio);
        blue = (int) (blue * ratio);

        return Color.argb(alpha, red, green, blue);
    }

    /**
     * @param color 背景颜色
     * @return 前景色是否深色
     */
    private static boolean isTextColorDark(int color) {
        float a = (Color.red(color) * 0.299f + Color.green(color) * 0.587f + Color.blue(color) * 0.114f);
        return a > 180;
    }


}
