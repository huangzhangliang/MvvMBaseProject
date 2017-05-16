package com.android.project.base;

import com.android.project.common.OnRefreshListener;
import com.android.project.common.PresenterCallback;
import com.android.project.ui.view.RefreshLayout;
import com.android.project.utils.LogUtils;
import com.android.project.utils.OkUtils;

/**
 * 作　　者：Leon（黄长亮）
 * 创建日期：2017/4/1
 */

public abstract class BasePresenter{

    private BaseActivity mActivity;
    private PresenterCallback mPresenterCallback;
    private int mPageSize = 10;
    private int mPage = 1;
    private int mTempPage;
    private boolean isAutoRefresh;
    private boolean isSubtracted; // 避免重复取消上拉加载
    private RefreshLayout mRefreshLayout;

    public BasePresenter(PresenterCallback presenterCallback) {
        this(null,presenterCallback);
    }

    public BasePresenter(BaseActivity activity,PresenterCallback presenterCallback) {
        this(activity,10,presenterCallback);
    }

    public BasePresenter(int pageSize,PresenterCallback presenterCallback) {
        this(null,pageSize,presenterCallback);
    }


    public BasePresenter(BaseActivity activity,int pageSize,PresenterCallback presenterCallback) {
        mActivity = activity;
        mPresenterCallback = presenterCallback;
        mPageSize = pageSize;
        loadData();
    }

    public abstract void loadData();


    public void subtractPage(){
        if (mPage > 1 && !isSubtracted){
            LogUtils.sf("加载更多请求取消Page--");
            isSubtracted = true;
            mPage--;
        }
    }

    public void finishRefresh(){
        if (mRefreshLayout != null){
            mRefreshLayout.finishRefreshing();
        }
    }

    public void finishLoadMore(){
        if (mRefreshLayout != null){
            mRefreshLayout.finishLoadmore();
        }
    }

    public void setRefreshLayout(RefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
        settingRefreshLayout();
    }

    private void settingRefreshLayout(){
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener(){
            @Override
            public void onRefresh() {
                super.onRefresh();
                mTempPage = mPage;
                mPage = 1;
                mPresenterCallback.isLoadMore(false);
                BasePresenter.this.loadData();
            }

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                mPage++;
                isSubtracted = false;
                mPresenterCallback.isLoadMore(true);
                BasePresenter.this.loadData();
            }

            @Override
            public void onRefreshCanceled() {
                super.onRefreshCanceled();
                mPage = mTempPage;
                OkUtils.getInstance().cancelTag(BasePresenter.this);
                finishRefresh();
            }

            @Override
            public void onLoadMoreCanceled() {
                super.onLoadMoreCanceled();
                OkUtils.getInstance().cancelTag(BasePresenter.this);
                subtractPage();
                finishLoadMore();
            }
        });
        mPresenterCallback.setOnErrorListener(new PresenterCallback.OnErrorListener() {
            @Override
            public void onError() {
                if (mPresenterCallback.isLoadMore()){
                    subtractPage();
                    finishLoadMore();
                }else {
                    finishRefresh();
                }
            }
        });
        mPresenterCallback.setOnSuccessListener(new PresenterCallback.OnSuccessListener() {
            @Override
            public void onSuccess() {
                if (mPresenterCallback.isLoadMore()){
                    finishLoadMore();
                }else {
                    finishRefresh();
                }
            }
        });
    }

    public BaseActivity getActivity() {
        return mActivity;
    }

    public PresenterCallback getPresenterCallback() {
        return mPresenterCallback;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public int getPage() {
        return mPage;
    }

    public boolean isAutoRefresh() {
        return isAutoRefresh;
    }

    public RefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }
}
