package com.hnsh.dialogue.utils;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author lingu
 * @create 2019/12/3 15:42
 * @Describe
 */
public class GlideUtil {
    public static void loadImg(Context context, String imgUrl, ImageView imageView){
        Glide.with(context)
                .load(imgUrl)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);



    }

    public static void loadImg(Context context, int redImg, ImageView imageView){
        Glide.with(context)
                .load(redImg)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);

    }
}
