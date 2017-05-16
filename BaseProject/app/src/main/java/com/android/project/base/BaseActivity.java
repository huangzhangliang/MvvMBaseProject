package com.android.project.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.android.barball_library.LoadingDialog.LoadingDialog;
import com.android.project.ui.activity.MainActivity;
import com.android.project.bean.EventBusModel;
import com.android.project.utils.OkUtils;
import com.android.project.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public abstract class BaseActivity extends AppCompatActivity {

    private LoadingDialog mLoadingDialog;
    private boolean isUseEventBus;
    private boolean isUsed;
    private static List<Activity> activities = new LinkedList<>();
    private List<OnLoadingDialogCancel> mOnLoadingDialogCancelList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isUseEventBus && !isUsed){
            isUsed = true;
            EventBus.getDefault().register(this);
        }
    }

    public void useEventBus(boolean isUse){
        isUseEventBus = isUse;
    }


    public void postEvent(String eventMessage) {
        postEvent(eventMessage,null);
    }

    public void postEvent(String eventMessage,Object model) {
        EventBus.getDefault().post(new EventBusModel(eventMessage,model));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusModel event) {

    }

    public LoadingDialog getLoadingDialog(){
        return mLoadingDialog;
    }

    private void initLoadingDialog(){
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                OkUtils.getInstance().cancelTag(this);
                if (mOnLoadingDialogCancelList.size() > 0){
                    for (OnLoadingDialogCancel dialogCancel : mOnLoadingDialogCancelList){
                        dialogCancel.onCancel();
                    }
                }
                loadingDialogCancel();
            }
        });
    }

    /**
     * 显示加载对话框
     */
    public void showLoadingDialog() {
        if (mLoadingDialog == null){
            initLoadingDialog();
        }
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }


    /**
     * 隐藏加载对话框
     */
    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }


    /**
     * @param clazz  跳转的activity.class
     */
    public void launchActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * @param intent 使用扩展的Intent
     *
     * 示例
     launchActivity(new IntentExpand(Activity.class) {
        @Override
        public void extraValue(Intent intent) {
            intent.putExtra("key","value");
        }
     });
     */
    public void launchActivity(IntentExpand intent) {
        startActivity(intent.intent);
    }

    public abstract class IntentExpand{
        public Intent intent;
        public IntentExpand(Class clazz) {
            intent = new Intent(BaseActivity.this,clazz);
            extraValue(intent);
        }
        public abstract void extraValue(Intent intent);
    }

    /**
     * @param clazz 跳转的activity.class
     * @param key 携带数据的key
     * @param serializable 携带的Serializable类型参数
     */
    public void launchActivity(Class clazz,String key, Serializable serializable) {
        launchActivity(clazz,new String[]{key},serializable);
    }


    /**
     * @param clazz 跳转的activity.class
     * @param key 携带数据的key
     * @param parcelable 携带的Parcelable类型参数
     */
    public void launchActivity(Class clazz,String key, Parcelable parcelable) {
        launchActivity(clazz,new String[]{key},parcelable);
    }


    /**
     * @param clazz 跳转的activity.class
     * @param keys keys数组
     * @param serializables 携带的Serializable类型参数数组
     */
    public void launchActivity(Class clazz,String[] keys, Serializable...serializables) {
        Intent intent = new Intent(this, clazz);
        for (int i = 0;i<keys.length;i++){
            intent.putExtra(keys[i], serializables[i]);
        }
        startActivity(intent);
    }


    /**
     * @param clazz 跳转的activity.class
     * @param keys keys数组
     * @param parcelables 携带的Parcelable类型参数数组
     */
    public void launchActivity(Class clazz,String[] keys, Parcelable...parcelables) {
        Intent intent = new Intent(this, clazz);
        for (int i = 0;i<keys.length;i++){
            intent.putExtra(keys[i], parcelables[i]);
        }
        startActivity(intent);
    }


    /**
     * 监听手机home键事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && this instanceof MainActivity) {
            moveTaskToBack(true);
            return  false;
        } else{
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        if (isUseEventBus){
            EventBus.getDefault().unregister(this);
        }
        OkUtils.getInstance().cancelTag(this);
        removeActivity(this);
        super.onDestroy();
    }

    /**
     * loadingDialog取消时会调用这个方法（可以以这个方法中取消网络请求之类的操作）
     */
    public void loadingDialogCancel(){

    }

    public interface OnLoadingDialogCancel{
        void onCancel();
    }

    public void addOnLoadingDialogCancel(OnLoadingDialogCancel onLoadingDialogCancel){
        if (!mOnLoadingDialogCancelList.contains(onLoadingDialogCancel)) {
            mOnLoadingDialogCancelList.add(onLoadingDialogCancel);
        }
    }

    public void removeOnLoadingDialogCancel(OnLoadingDialogCancel onLoadingDialogCancel){
        if (mOnLoadingDialogCancelList.contains(onLoadingDialogCancel)) {
            mOnLoadingDialogCancelList.remove(onLoadingDialogCancel);
        }
    }


    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }

//        if (this instanceof HomeActivity){
//            if (System.currentTimeMillis() - mPerTime > 2000){
//                Toast.makeText(UIUtils.getContext(),"再按一次退出应用",Toast.LENGTH_SHORT).show();
//                mPerTime = System.currentTimeMillis();
//                return;
//            }
//        }

    }

    public static void addActivity(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
        for (Activity a : activities) {
            Log.e(" addActivity ", a.getClass().getName());
        }
    }

    public static void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
        for (Activity a : activities) {
            Log.e(" removeActivity ", a.getClass().getName());
        }
    }

    public static void finishAllActivity() {
        for (final Activity activity : activities) {
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                    }
                });
            } else {
                activities.remove(activity);
            }
        }

    }


    /**
     * Activity销毁方法
     * 延迟执行
     */
    public void destroy (){
        UIUtils.postTaskDelay(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },100);
    }

}
