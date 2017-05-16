package com.android.project.common;

import android.util.Log;

import com.android.project.BuildConfig;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.BaseRequest;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Response;

/**
 * OkGo 泛型参数回调
 */

public abstract class GenericCallback<T> extends AbsCallback<T> {


    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
    }

    @Override
    public void onSuccess(T t, Call call, Response response) {

    }

    @Override
    public T convertSuccess(Response response) throws Exception{
        //以下代码是通过泛型解析实际参数,泛型必须传

        //DialogCallback<LzyResponse<ServerModel>> 得到类的泛型，包括了泛型参数
        Type genType = getClass().getGenericSuperclass();
        //从上述的类中取出真实的泛型参数，有些类可能有多个泛型，所以是数值
        String str = response.body().string();
        JSONObject jsonObject = new JSONObject(str);
        Log.d("status",jsonObject.getString("status"));
        if (BuildConfig.DEBUG){
            Logger.json(str);
        }
        if (!(genType instanceof ParameterizedType)){
            return (T) str;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];


        //这里我们既然都已经拿到了泛型的真实类型，即对应的 class ，那么当然可以开始解析数据了，我们采用 Gson 解析
        //以下代码是根据泛型解析数据，返回对象，返回的对象自动以参数的形式传递到 onSuccess 中，可以直接使用
//        JsonReader jsonReader = new JsonReader(response.body().charStream());
        //有数据类型，表示有data
        T data = Convert.fromJson(str, type);
        response.close();
        return data;
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        Log.d("GenericCallback",e.getMessage());
    }
}
