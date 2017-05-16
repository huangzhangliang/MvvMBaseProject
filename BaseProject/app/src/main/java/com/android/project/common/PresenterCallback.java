package com.android.project.common;



import com.android.project.utils.LogUtils;
import com.lzy.okgo.request.BaseRequest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 网络数据Callback
 *
 * 对GenericCallback进行再次封装，配合BasePresenter对网络请求进行自动处理
 *
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/4/5
 */

public abstract class PresenterCallback<T> {

    public static final String TAG = "PresenterCallback";

    private GenericCallback mGenericCallback;
    private OnErrorListener mOnErrorListener;
    private OnSuccessListener mOnSuccessListener;
    private boolean isLoadMore;


    protected PresenterCallback() {
        //以下代码是通过泛型解析实际参数,泛型必须传
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        final Type type = params[0];


        mGenericCallback = new GenericCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                PresenterCallback.this.onBefore();
            }

            @Override
            public void onSuccess(Object t, Call call, Response response) {
                try {
                    T tempData = Convert.fromJson((String) t,type);
                    if (tempData == null){
                        LogUtils.e(TAG,"解析失败！");
                    }else {
                        PresenterCallback.this.onSuccess(tempData,isLoadMore);
                    }
                    if (mOnSuccessListener != null){
                        mOnSuccessListener.onSuccess();
                    }
                    isLoadMore = false;
                }catch (Exception e){
                    LogUtils.e(TAG,e.getMessage());
                }

            }

            @Override
            public void onCacheSuccess(Object t, Call call) {
                try {
                    T tempData = Convert.fromJson((String) t,type);
                    if (tempData == null){
                        LogUtils.e(TAG,"解析失败！");
                    }else {
                        PresenterCallback.this.onCacheSuccess(tempData);
                    }
                }catch (Exception e){
                    LogUtils.e(TAG,e.getMessage());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call,response,e);
                PresenterCallback.this.onError(e);
                if (mOnErrorListener != null){
                    mOnErrorListener.onError();
                }
            }

            @Override
            public void onCacheError(Call call, Exception e) {
                super.onCacheError(call,e);
                PresenterCallback.this.onCacheError(e);
            }



            @Override
            public void onAfter(Object t, Exception e) {
                try {
                    T tempData = Convert.fromJson((String) t,type);
                    if (tempData == null){
                        LogUtils.e(TAG,"解析失败！");
                    }else {
                        PresenterCallback.this.onAfter(tempData,e);
                    }
                }catch (Exception es){
                    LogUtils.e(TAG,es.getMessage());
                }
            }


            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                super.upProgress(currentSize,totalSize,progress,networkSpeed);
                PresenterCallback.this.upProgress(currentSize, totalSize, progress, networkSpeed);
            }

            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                super.downloadProgress(currentSize,totalSize,progress,networkSpeed);
                PresenterCallback.this.downloadProgress(currentSize, totalSize, progress, networkSpeed);
            }
        };
    }

    /**
     * 网络请求成功的回调
     * @param sd
     */
    public abstract void onSuccess(final T sd,boolean isLoadMore);

    /**
     * 网络请求真正执行前回调
     */
    public void onBefore(){

    }


    /**
     * 缓存读取成功的回调
     * @param csd
     */
    public void onCacheSuccess(T csd){

    }


    /**
     * 加载更多成功回调
     * @param msd
     */
//    public void onMoreSuccess(final T msd){
//
//    }

    /**
     * 网络请求失败的回调
     * @param e
     */
    public void onError(Exception e){

    }


    /**
     * 网络缓存读取失败的回调
     * @param e
     */
    public void onCacheError(Exception e){

    }


    /**
     * 网络请求结束的回调,无论成功失败一定会执行
     * @param t
     * @param e
     */
    public void onAfter(T t, Exception e) {

    }

    /**
     * 上传进度的回调
     * @param currentSize
     * @param totalSize
     * @param progress
     * @param networkSpeed
     */
    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

    }


    /**
     * 下载进度的回调
     * @param currentSize
     * @param totalSize
     * @param progress
     * @param networkSpeed
     */
    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {

    }

    public interface OnSuccessListener{
        void onSuccess();
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        mOnSuccessListener = onSuccessListener;
    }

    public interface OnErrorListener{
        void onError();
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        mOnErrorListener = onErrorListener;
    }

    public GenericCallback getCallback() {
        return mGenericCallback;
    }

    public void isLoadMore(boolean loadMore) {
        isLoadMore = loadMore;
    }

    public boolean isLoadMore() {
        return isLoadMore;
    }
}
