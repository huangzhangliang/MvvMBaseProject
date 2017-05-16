package com.android.project.utils;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * OKHttp日志打印工具类
 *
 * @author Leon(黄长亮)
 * @date 17/5/13
 */

public class LoggerInterceptor implements Interceptor
{
    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean isPrint; // 是否打印


    public LoggerInterceptor(String tag, boolean isPrint)
    {
        if (TextUtils.isEmpty(tag))
        {
            tag = TAG;
        }
        this.isPrint = isPrint;
        this.tag = tag;
    }


    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        if (isPrint){
            logForRequest(request);
        }
        Response response = chain.proceed(request);
        if (isPrint){
            return logForResponse(response);
        }else {
            return response;
        }
    }

    private Response logForResponse(Response response)
    {
        try
        {
            Logger.init(tag).methodCount(0).hideThreadInfo();
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            String contentType = "";
            ResponseBody body = null;
            MediaType mediaType = null;
            if (isPrint){
                body = clone.body();
                if (body != null){
                    mediaType = body.contentType();
                    if (mediaType != null){
                        contentType = mediaType.toString().trim();
                    }
                }
            }

            String logInfo = "type : response（响应）"
                    +"\n"+"url : " + clone.request().url()
                    +"\n"+"code : " + clone.code()
                    +"\n"+"protocol : " + clone.protocol()
                    +"\n"+ (!TextUtils.isEmpty(clone.message())?"message : " + clone.message():"message : ")
                    +"\n"+ "responseBody's contentType : " + contentType;

            if (clone.code() == 200){
                Logger.d(logInfo);
            }else {
                Logger.e(logInfo);
            }

            if (body != null)
            {
                if (mediaType != null)
                {
                    if (isText(mediaType))
                    {
                        String resp = body.string();
                        Logger.json(resp);
                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    } else
                    {
                        Logger.d("responseBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }

        } catch (Exception e)
        {
//            e.printStackTrace();
        }

        return response;
    }

    private void logForRequest(Request request)
    {
        try
        {
            Logger.init(tag).methodCount(0).hideThreadInfo();
            String url = request.url().toString();
            Headers headers = request.headers();

            RequestBody requestBody = request.body();
            String contentType = "";
            String content = "";
            if (requestBody != null)
            {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null)
                {
                    contentType = mediaType.toString();
                    if (true)
                    {
                        content = bodyToString(request);
                    } else
                    {
                        content = "requestBody's content : " + " maybe [file part] , too large too print , ignored!";
                    }
                }
            }
            Logger.v("type : request（请求）"
                    +"\n"+"method : " + request.method()
                    +"\n"+"url : " + url
                    +"\n"+(headers != null && headers.size() > 0?"headers : " + headers.toString().trim():"headers : ")
                    +"\n"+"requestBody's contentType : " + contentType
                    +"\n"+"requestBody's content : "+(!TextUtils.isEmpty(content)?"\n{\n  "+content.replaceAll("&", "\n  ").replaceAll("="," : ")+"\n}":content));
        } catch (Exception e)
        {
//            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType)
    {
        if (mediaType.type() != null && mediaType.type().equals("text"))
        {
            return true;
        }
        if (mediaType.subtype() != null)
        {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request)
    {
        try
        {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e)
        {
            return "something error when show requestBody.";
        }
    }
}
