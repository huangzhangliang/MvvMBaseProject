package com.android.project.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.barball_library.LoadingLayout.LoadingLayout;
import com.android.barball_library.Utils.NetWorkUtils;
import com.android.barball_library.Utils.StatusBarUtil;
import com.android.project.R;
import com.android.project.base.BaseActivity;
import com.android.project.databinding.ActivityBaseBinding;
import com.android.project.utils.LogUtils;
import com.android.project.utils.NavigationBar;

import java.util.List;
import java.util.Map;


/**
 * 带有头部导航条的Activity
 * 方法执行顺序：
 * 1.init() 初始化一些页面设置（必须） 如设置导航条的标题文字
 * 2.intiData() 初始化数据（必须）用于加载页面数据，得到页面数据后调用checkState()方法检查数据，根据数据结果显示不同的页面，如空数据页面
 * 3.initSuccessView() 初始成功页面（必须）只有加载数据成功，且为有效数据时才会调用
 * 4.showData() 显示数据（必须）
 * 5.initListener() 初始监听（可选） 用于设置页面各种监听操作
 */

public abstract class NavigationActivity<B extends ViewDataBinding> extends BaseActivity {

    public ActivityBaseBinding mDetailsBinding;
    private NavigationBar mNavigationBar;
    private LoadingLayout mLoadingLayout;
    private boolean mIsInfoSuccessView; // 是否主动加载成功页面
    private B mViewBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isImmerse()){
            StatusBarUtil.transparencyBar(this);
        }else {
            StatusBarUtil.setStatusBarColor(this, setStatusBarColor());
        }
        mDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);
//        StatusBarUtil.StatusBarLightMode(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mDetailsBinding.superContainer.setPadding(0, UIUtils.getStatusBarHeight(), 0, 0);
//        }

        if (isInitNavigationBar()){
            initHeaderView();
        }
        init();
        initContentView();
        if (mIsInfoSuccessView){
            mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
        }
//        initFooterView(headerView);
    }

    /**
     * 初始化一个头布局
     * @headerView 新创建的头布局
     */
    public void initHeaderView(){
        mNavigationBar = new NavigationBar(this);
        mDetailsBinding.headerContainer.addView(mNavigationBar);
    }
    /**
     * 替换一个头布局
     * @headerView 替换一个头布局
     */
    public void replaceHeaderView(View headerView){
        mDetailsBinding.headerContainer.removeAllViews();
        mDetailsBinding.headerContainer.addView(headerView);
    }


    public void initContentView(){
        if (mLoadingLayout == null) {// 第一次执行
            Log.d("LoadingLayout","initData");
            mLoadingLayout = new LoadingLayout(this,isAutoLoadData()) {
                @Override
                public void initData() {

                    if (NetWorkUtils.getCurrentNetworkState(NavigationActivity.this) == null){
                        // 没有网络
                    }
                    NavigationActivity.this.initData();

                }

                @Override
                public View initSuccessView() {
                    View tempView = LayoutInflater.from(getContext()).inflate(NavigationActivity.this.initSuccessView(),null);
                    mViewBinding = DataBindingUtil.bind(tempView);
                    displayPage(mViewBinding);
                    return tempView;
                }

                @Override
                public void initListener(View successView) {
                    NavigationActivity.this.initListener(mViewBinding);
                }
            };
        } else {// 第2次执行
            ((ViewGroup) mLoadingLayout.getParent()).removeView(mLoadingLayout);
        }
        mDetailsBinding.contentContainer.addView(mLoadingLayout);
    }

    /**
     * 获取LoadingLayout
     * @return
     */
    public LoadingLayout getLoadingLayout() {
        return mLoadingLayout;
    }

    /**
     * 是否在加载数据前加载成功页面（如果需要在加载数据前显示成功页面，请在init方法中调用该方法）
     * 默认不主动加载
     */
    public void advanceInitSuccessView(boolean isInitSuccessView){
        mIsInfoSuccessView = isInitSuccessView;
    }

    /**
     * 设置状态栏颜色
     */
    public int setStatusBarColor(){
        return R.color.colorTheme;
    }

    /**
     * 是否初始化默认导航条
     */
    public boolean isInitNavigationBar(){
        return true;
    }

    /**
     * 设置是否自动加载数据（自动调用initData方法）
     * 默认为自动加载
     */
    public boolean isAutoLoadData(){
        return true;
    }

    /**
     * 设置是否为沉浸式
     */
    public boolean isImmerse(){
        return false;
    }

    /**
     * @des 设置导航条标题
     * @title 标题文字
     */
    public void setNavTitle(String title) {
        if (mNavigationBar != null) {
            mNavigationBar.setNavTitle(title);
        }
    }


    /**
     * @des 隐藏/显示导航条的返回按钮
     * @isHide true=隐藏/false=显示
     */
    public void setHideBackBtn(boolean isHide) {
//        ActionBar actionBar = getSupportActionBar();
        if (mNavigationBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(!isHide);
            if (isHide){
                mNavigationBar.mNavigationBinding.btnNavBack.setVisibility(View.GONE);
            }else{
                mNavigationBar.mNavigationBinding.btnNavBack.setVisibility(View.VISIBLE);
            }
        }

    }


    /**
     * @des 隐藏/显示导航条
     * @isHide true=隐藏/false=显示
     */
    public void setHideNavBar(boolean isHide) {
        if (mNavigationBar != null) {
            if (isHide){
                mNavigationBar.mNavigationBinding.navContainer.setVisibility(View.GONE);
            }else{
                mNavigationBar.mNavigationBinding.navContainer.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * @des 隐藏/显示导航条自带的View
     * @isHide true=隐藏/false=显示
     */
    public void setHideNavBarOwnView(boolean isHide) {
        if (mNavigationBar != null) {
            if (isHide){
                mNavigationBar.mNavigationBinding.headerLeftLayout.setVisibility(View.GONE);
                mNavigationBar.mNavigationBinding.headerCenterLayout.setVisibility(View.GONE);
                mNavigationBar.mNavigationBinding.headerRightLayout.setVisibility(View.GONE);
            }else{
                mNavigationBar.mNavigationBinding.headerLeftLayout.setVisibility(View.VISIBLE);
                mNavigationBar.mNavigationBinding.headerCenterLayout.setVisibility(View.VISIBLE);
                mNavigationBar.mNavigationBinding.headerRightLayout.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 添加自定义View在导航条
     * @param view
     */
    public void addViewOnNavBar(View view){
        if (mNavigationBar != null){
            mNavigationBar.mNavigationBinding.placeholderContainer.addView(view);
        }
    }

    /**
     * 获取头部导航栏
     * @return 头部导航栏
     */
    public NavigationBar getNavigationBar() {
        return mNavigationBar;
    }

    /**
     * 初始化页面（该方法中不能初始化成功页面元素，成功页面为空）
     */
    public abstract void init();

    /**
     * 初始化数据 （加载数据在该方法中实现）
     */
    public abstract void initData();

    /**
     * 初始化成功页面
     * @return R.layout.id
     */
    public abstract int initSuccessView();

    /**
     * 显示数据
     * @param viewBinding successView的binding类
     */
    public abstract void displayPage(B viewBinding);

    /**
     * 初始化监听
     * @param viewBinding successView的binding类
     */
    public void initListener(B viewBinding){}


    /**
     * @des 网络数据Json化对象检测
     */
    public void checkState(Object obj) {
        try {
            if (obj == null) {
                mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.EMPTY);
            } else if (obj instanceof List) {
                if (((List) obj).size() == 0) {
                    mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.EMPTY);
                } else {
                    mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
                }
            } else if (obj instanceof Map) {
                if (((Map) obj).size() == 0) {
                    mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.EMPTY);
                } else {
                    mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
                }
            } else {
                mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
            }
        }catch (Exception e){
            LogUtils.sf("网络数据Json化对象检测 --- " + e.getMessage());
        }
    }


}
