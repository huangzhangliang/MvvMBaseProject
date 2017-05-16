package com.android.project.ui.fragment;

import com.android.barball_library.LoadingLayout.LoadingLayout;
import com.android.project.R;
import com.android.project.base.BaseFragment;
import com.android.project.databinding.FragmentListBinding;
import com.android.project.utils.LogUtils;
import com.android.project.utils.UIUtils;

/**
 * 错误页面
 *
 * @author Leon(黄长亮)
 * @date 2017/5/16.
 */
public class ErrorFragment extends BaseFragment<FragmentListBinding>{

    @Override
    public boolean isAutoLoadData() { // 不自动加载数据
        return false;
    }

    @Override
    public void init() {

    }

    @Override
    public void initData() { // 用户点击重试按钮会调用initData()方法，尝试重新加载数据
        // 模拟网络请求 延迟1秒加载为错误页面
        UIUtils.postTaskDelay(new Runnable() {
            @Override
            public void run() {
                mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.ERROR);
            }
        },1000);
    }

    @Override
    public int initSuccessView() {
        return R.layout.fragment_list;
    }

    @Override
    public void displayPage(FragmentListBinding viewBinding) {
        LogUtils.sf("初始化ErrorFragment");

    }

}
