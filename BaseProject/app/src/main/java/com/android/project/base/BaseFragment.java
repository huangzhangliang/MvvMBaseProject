package com.android.project.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.barball_library.LoadingLayout.LoadingLayout;
import com.android.project.R;
import com.android.project.bean.EventBusModel;
import com.android.project.databinding.FragmentBaseBinding;
import com.android.project.utils.NavigationBar;
import com.android.project.utils.OkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class BaseFragment<B extends ViewDataBinding> extends Fragment implements BaseActivity.OnLoadingDialogCancel{

    public LoadingLayout mLoadingLayout;
    public FragmentBaseBinding mSuperBinding;
    public BaseActivity mActivity;
    private B mViewBinding;
    private boolean mIsUseEventBus;
    private boolean mIsVisibleToUser; // 是否可见
    private boolean mIsInitSuccessView; // 是否主动加载成功页面
    private boolean mLoadedFlag; // 加载过数据标记
    private List<OnFragmentDestroyListener> mOnFragmentDestroyListeners = new ArrayList<>();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        if (mIsUseEventBus){
            EventBus.getDefault().register(this);
        }
        mActivity.addOnLoadingDialogCancel(this);
        if (mIsInitSuccessView && mLoadingLayout !=null){
            mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
        }
    }

    public View initView(LayoutInflater inflater) {
        mActivity = (BaseActivity) getActivity();
        mSuperBinding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_base, null));
//        mSuperBinding = DataBindingUtil.bind(View.inflate(getContext(), R.layout.fragment_base, null));
        if (mLoadingLayout == null) {// 第一次执行
            mLoadingLayout = new LoadingLayout(getContext(),isAutoLoadData()) {
                @Override
                public void initData() {
                    mLoadedFlag = false;
                    BaseFragment.this.lazyData();
                }

                @Override
                public View initSuccessView() {
                    View tempView = LayoutInflater.from(getContext()).inflate(BaseFragment.this.initSuccessView(),null);
                    mViewBinding = DataBindingUtil.bind(tempView);
                    displayPage(mViewBinding);
                    return tempView;
                }
                @Override
                public void initListener(View successView) {
                    BaseFragment.this.initListener(mViewBinding);
                }

            };
        } else {// 第2次执行
            ((ViewGroup) mLoadingLayout.getParent()).removeView(mLoadingLayout);
        }
        mSuperBinding.contentContainer.addView(mLoadingLayout);
        return mSuperBinding.contentContainer;
    }

    public void setupNavigationBar(NavigationBar navigationBar){

    }

    /**
     * 加载initData方法拦截，避免Fragment切换时重复加载数据
     */
    public void lazyData(){
        if (!mLoadedFlag){
            initData();
            mLoadedFlag = true;
        }
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
     * 是否在加载数据前加载成功页面（如果需要在加载数据前显示成功页面，请在init方法中调用该方法）
     * 默认不主动加载
     */
    public void advanceInitSuccessView(boolean isInitSuccessView){
        mIsInitSuccessView = isInitSuccessView;
    }

    /**
     * 是否使用EventBus
     * @return
     */
    public void useEventBus(boolean isUseEventBus){
        mIsUseEventBus = isUseEventBus;
    }

    /**
     * 设置是否自动加载数据（自动调用initData方法）
     * 默认为自动加载
     */
    public boolean isAutoLoadData(){
        return true;
    }

    /**
     * 显示加载对话框
     */
    public void showLoadingDialog() {
        if (mActivity != null){
            mActivity.showLoadingDialog();
        }
    }

    /**
     * 隐藏加载对话框
     */
    public void hideLoadingDialog() {
        if (mActivity != null){
            mActivity.hideLoadingDialog();
        }
    }

    public void launchActivity(IntentExpand intent) {
        startActivity(intent.intent);
    }

    public abstract class IntentExpand{
        public Intent intent;
        public IntentExpand(Class clazz) {
            intent = new Intent(mActivity,clazz);
            extraValue(intent);
        }
        public abstract void extraValue(Intent intent);
    }


    public void postEvent(String eventMessage) {
        postEvent(eventMessage,null);
    }

    public void postEvent(String eventMessage,Object model) {
        EventBus.getDefault().post(new EventBusModel(eventMessage,model));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusModel event) {

    }

    /**
     * 当用户取消LoadingDialog时，只取消当前的fragment网络请求
     * mIsVisibleToUser -> 判断当前fragment是否为显示状态
     */
    @Override
    public void onCancel() {
        if (mIsVisibleToUser){
            OkUtils.getInstance().cancelTag(BaseFragment.this);
        }
    }

    @Override
    public void onDestroy() {
        if (mIsUseEventBus){
            EventBus.getDefault().unregister(this);
        }
        OkUtils.getInstance().cancelTag(this);
        mActivity.removeOnLoadingDialogCancel(this);
        if (mOnFragmentDestroyListeners.size() > 0){
            for (OnFragmentDestroyListener listener : mOnFragmentDestroyListeners) {
                listener.onDestroy();
            }
            mOnFragmentDestroyListeners.clear();
        }
        mOnFragmentDestroyListeners = null;
        mActivity = null;
        mLoadingLayout = null;
        mSuperBinding = null;
        super.onDestroy();
    }


    public interface OnFragmentDestroyListener{
        void onDestroy();
    }

    /**
     * @des 网络数据Json化对象检测
     */
    public void checkState(Object obj) {
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
    }
}
