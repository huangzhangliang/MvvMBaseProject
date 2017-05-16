package com.android.project.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.barball_library.StyleableToast.StyleableToast;


/**
 * Toast工具类
 *
 * @author Leon(黄长亮)
 * @date 17/5/13
 */

public class ToastUtils {

    public final static String DEFAULT_BG_COLOR = "#FB454545";
    public static StyleableToast styleableToast;


    /**
     * 普通的Toast
     * @param message
     */
    public static void normal(@NonNull String message) {
        normal(UIUtils.getContext(),message,Color.parseColor(DEFAULT_BG_COLOR),Color.WHITE);
    }


    /**
     * 普通的Toast（自定义背景色和文字颜色）
     * @param context
     * @param message
     * @param backgroundColor
     * @param textColor
     */
    public static void normal(@NonNull Context context, @NonNull String message ,@ColorInt int backgroundColor,@ColorInt int textColor) {
        makeText(context,message,backgroundColor,textColor,0);
    }

    /**
     * 带图标的普通Toast
     * @param context
     * @param message
     */
    public static void normal(@NonNull Context context, @NonNull String message ,@DrawableRes int iconRes) {
        makeText(context,message,Color.parseColor(DEFAULT_BG_COLOR),Color.WHITE,iconRes);
    }

    /**
     * 普通的Toast（自定义背景色、文字颜色、图标）
     * @param context
     * @param message
     * @param backgroundColor
     * @param textColor
     */
    public static void makeText(@NonNull final Context context, @NonNull final String message , @ColorInt final int backgroundColor, @ColorInt final int textColor, @DrawableRes final int iconRes) {
        if (styleableToast != null){
            styleableToast.cancel();
        }
        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                styleableToast = new StyleableToast.Builder(context, message).withBackgroundColor(backgroundColor)
                        .withTextColor(textColor)
                        .withDuration(Toast.LENGTH_SHORT)
                        .withIcon(iconRes,true)
                        .build();
                styleableToast.show();
            }
        });
    }





}
