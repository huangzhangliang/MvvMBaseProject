package com.android.project.ui.fragment;

import com.android.project.R;
import com.android.project.base.BaseFragment;
import com.android.project.databinding.FragmentListBinding;
import com.android.project.utils.LogUtils;
import com.android.project.utils.UIUtils;

/**
 * 空页面
 *
 * @author Leon(黄长亮)
 * @date 2017/5/16.
 */
public class EmptyFragment extends BaseFragment<FragmentListBinding>{

    @Override
    public void init() {

    }

    @Override
    public void initData() {
        // 模拟网络请求 延迟1秒加载为空页面
        UIUtils.postTaskDelay(new Runnable() {
            @Override
            public void run() {
                // 该方法会校验对外象来判断展示什么页面
                checkState(null);
            }
        },1000);
    }

    @Override
    public int initSuccessView() {
        return R.layout.fragment_list;
    }

    @Override
    public void displayPage(FragmentListBinding viewBinding) {
        LogUtils.sf("初始化EmptyFragment");

    }

}
