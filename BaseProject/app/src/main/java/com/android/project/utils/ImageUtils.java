package com.android.project.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.android.project.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import static com.android.project.utils.UIUtils.getResources;

/**
 * 图片相关工具类
 *
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/5/10
 */

public class ImageUtils {


    public static final int REQUEST_CODE_CHOOSE = 23;

    /**
     * 选择图片
     * @param activity 当前Activity
     * @param maxSize  图片数量
     */
    public static void choose(Activity activity,int maxSize){
        Matisse.from(activity)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(maxSize)
                .theme(R.style.Matisse_XuanZhe)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    public static Phoenix.Builder with(SimpleDraweeView simpleDraweeView){
        return Phoenix.with(simpleDraweeView);
    }

    public static void upload(String url){

    }

}
