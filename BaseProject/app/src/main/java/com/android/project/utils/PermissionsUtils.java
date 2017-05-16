package com.android.project.utils;

import android.content.Context;

import com.android.barball_library.acp.Acp;
import com.android.barball_library.acp.AcpListener;
import com.android.barball_library.acp.AcpOptions;

import java.util.List;

/**
 * 动态权限申请工具包装
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/4/1
 */

public class PermissionsUtils{


    public static void apply(final Context context, String[] permission,final OnGrantedListener onGrantedListener){
        apply(context,null,null,null,null,null,onGrantedListener,permission);
    }

    public static void apply(final Context context, String deniedMessage,String rationalMessageText,String[] permission,final OnGrantedListener onGrantedListener){
        apply(context,deniedMessage,null,null,rationalMessageText,null,onGrantedListener,permission);
    }

    public static void apply(final Context context,
                             String deniedMessage,
                             String deniedCloseBtnText,
                             String deniedSettingBtnText,
                             String rationalMessageText,
                             String rationalBtnText,
                             final OnGrantedListener onGrantedListener,
                             String... permission){
        Acp.getInstance(context).request(new AcpOptions.Builder()
                        .setPermissions(permission)
                /*以下为自定义提示语、按钮文字*/
                .setDeniedMessage(deniedMessage)
                .setDeniedCloseBtn(deniedCloseBtnText)
                .setDeniedSettingBtn(deniedSettingBtnText)
                .setRationalMessage(rationalMessageText)
                .setRationalBtn(rationalBtnText)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        onGrantedListener.onGranted();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        onGrantedListener.onDenied(permissions);
                    }
                });
    }

    public interface OnGrantedListener{
        void onGranted(); // 用户全部同意回调
        void onDenied(List<String> permissions); // 用户拒绝或同意一部分回调
    }


}
