package com.android.project.ui.fragment;

import android.view.View;

import com.android.project.R;
import com.android.project.base.BaseFragment;
import com.android.project.common.Const;
import com.android.project.databinding.FragmentMeBinding;
import com.android.project.utils.ImageUtils;
import com.android.project.utils.LogUtils;

/**
 * 直接初始化页面
 *
 * @author Leon(黄长亮)
 * @date 2017/5/16.
 */
public class AdvanceFragment extends BaseFragment<FragmentMeBinding> implements View.OnClickListener{

    @Override
    public boolean isAutoLoadData() { // 不自动加载数据
        return false;
    }

    @Override
    public void init() {
        advanceInitSuccessView(true); // 在请求网络前初始成功页面
    }

    @Override
    public void initData() {

    }

    @Override
    public int initSuccessView() {
        return R.layout.fragment_me;
    }

    @Override
    public void displayPage(FragmentMeBinding viewBinding) {
        LogUtils.sf("初始化AdvanceFragment");
        viewBinding.itemUserActivities.tvText.setText(mActivity.getString(R.string.app_name));
        viewBinding.itemUserActivities.ivIcon.setImageResource(R.mipmap.ic_help);
        viewBinding.itemUserActivities.labelContainer.setTag(1);
        viewBinding.itemUserShop.tvText.setText(mActivity.getString(R.string.app_name));
        viewBinding.itemUserShop.ivIcon.setImageResource(R.mipmap.ic_help);
        viewBinding.itemUserShop.labelContainer.setTag(2);
        viewBinding.itemAccountManage.tvText.setText(mActivity.getString(R.string.app_name));
        viewBinding.itemAccountManage.ivIcon.setImageResource(R.mipmap.ic_help);
        viewBinding.itemAccountManage.labelContainer.setTag(3);
        viewBinding.itemHelpSupport.tvText.setText(mActivity.getString(R.string.app_name));
        viewBinding.itemHelpSupport.ivIcon.setImageResource(R.mipmap.ic_help);
        viewBinding.itemHelpSupport.labelContainer.setTag(4);
        ImageUtils.with(viewBinding.sdvUserHeader).load(Const.TEST_AVATAR);
        viewBinding.itemUserCentre.setTag(5);
    }

    @Override
    public void initListener(FragmentMeBinding viewBinding) {
        viewBinding.itemUserActivities.labelContainer.setOnClickListener(this);
        viewBinding.itemUserShop.labelContainer.setOnClickListener(this);
        viewBinding.itemAccountManage.labelContainer.setOnClickListener(this);
        viewBinding.itemHelpSupport.labelContainer.setOnClickListener(this);
        viewBinding.itemUserCentre.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch ((int)v.getTag()){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }
}
