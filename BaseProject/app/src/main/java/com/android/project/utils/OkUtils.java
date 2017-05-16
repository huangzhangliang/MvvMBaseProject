package com.android.project.utils;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.DeleteRequest;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;


/**
 * 网络框架包装
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/4/1
 *
 * 为什么要包装一层？
 * 为了以后方便替换网络框架和做一些统一处理
 *
 */

public class OkUtils {


    public static OkGo getInstance(){
        return OkGo.getInstance();
    }

    public static Get get(String url){
        return new Get(url);
    }

    public static Post post(String url){
        return new Post(url);
    }
    public static Delete delete(String url){
        return new Delete(url);
    }


    public static class Get{

        GetRequest mGetRequest;

        public Get(String url) {
            mGetRequest = OkGo.get(url);
        }

        public GetRequest tag(Object tag){
            return mGetRequest.tag(tag);
        }


        public GetRequest cacheKey(String cacheKey){
            return mGetRequest.cacheKey(cacheKey);
        }


        public GetRequest headers(HttpHeaders headers){
            return mGetRequest.headers(headers);
        }

        public GetRequest headers(String key, String value){
            return mGetRequest.headers(key,value);
        }

        public GetRequest cacheMode(CacheMode cacheMode){
            return mGetRequest.cacheMode(cacheMode);
        }


        public GetRequest params(String key,String value){
            return mGetRequest.params(key,value);
        }

        public GetRequest params(HttpParams httpParams){
            return mGetRequest.params(httpParams);
        }

        /** 非阻塞方法，异步请求，但是回调在子线程中执行 */
        @SuppressWarnings("unchecked")
        public <T> void execute(AbsCallback<T> callback) {
            mGetRequest.execute(callback);
        }
    }


    public static class Post{

        PostRequest mPostRequest;

        public Post(String url) {
            mPostRequest = OkGo.post(url);
        }


        public PostRequest tag(Object tag){
            return mPostRequest.tag(tag);
        }


        public PostRequest cacheKey(String cacheKey){
            return mPostRequest.cacheKey(cacheKey);
        }


        public PostRequest headers(HttpHeaders headers){
            return mPostRequest.headers(headers);
        }

        public PostRequest headers(String key, String value){
            return mPostRequest.headers(key,value);
        }

        public PostRequest cacheMode(CacheMode cacheMode){
            return mPostRequest.cacheMode(cacheMode);
        }


        public PostRequest params(String key,String value){
            return mPostRequest.params(key,value);
        }

        public PostRequest params(HttpParams httpParams){
            return mPostRequest.params(httpParams);
        }

        /** 非阻塞方法，异步请求，但是回调在子线程中执行 */
        @SuppressWarnings("unchecked")
        public <T> void execute(AbsCallback<T> callback) {
            mPostRequest.execute(callback);
        }

    }



    public static class Delete{

        DeleteRequest mDeleteRequest;

        public Delete(String url) {
            mDeleteRequest = OkGo.delete(url);
        }


        public DeleteRequest tag(Object tag){
            return mDeleteRequest.tag(tag);
        }


        public DeleteRequest cacheKey(String cacheKey){
            return mDeleteRequest.cacheKey(cacheKey);
        }


        public DeleteRequest headers(HttpHeaders headers){
            return mDeleteRequest.headers(headers);
        }

        public DeleteRequest headers(String key, String value){
            return mDeleteRequest.headers(key,value);
        }

        public DeleteRequest cacheMode(CacheMode cacheMode){
            return mDeleteRequest.cacheMode(cacheMode);
        }


        public DeleteRequest params(String key,String value){
            return mDeleteRequest.params(key,value);
        }

        public DeleteRequest params(HttpParams httpParams){
            return mDeleteRequest.params(httpParams);
        }

        /** 非阻塞方法，异步请求，但是回调在子线程中执行 */
        @SuppressWarnings("unchecked")
        public <T> void execute(AbsCallback<T> callback) {
            mDeleteRequest.execute(callback);
        }

    }




}
