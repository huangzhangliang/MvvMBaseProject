package com.android.project.presenter;


import com.android.project.base.BasePresenter;
import com.android.project.common.Const;
import com.android.project.common.PresenterCallback;
import com.android.project.utils.OkUtils;
import com.lzy.okgo.cache.CacheMode;


/**
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/4/5
 */
public class TestPresenter extends BasePresenter {

    public TestPresenter(PresenterCallback presenterCallback) {
        super(presenterCallback);
    }

    public TestPresenter(int pageSize, PresenterCallback presenterCallback) {
        super(pageSize, presenterCallback);
    }


    /**
     * 加载列表数据和加载更多数据都在loadData中处理 page和pageSize交给BasePresenter自动处理 , 加载更多时page会++
     * 如果不是列表数据可忽略
     */
    @Override
    public void loadData() {
        OkUtils.get(Const.serviceMethod.SENDSMSCAPTCHA)
                .tag(this)
                .params("page",getPage())
                .params("pageSize",getPageSize())
                .cacheKey(Const.serviceMethod.SENDSMSCAPTCHA)
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(getPresenterCallback().getCallback());
    }

}
