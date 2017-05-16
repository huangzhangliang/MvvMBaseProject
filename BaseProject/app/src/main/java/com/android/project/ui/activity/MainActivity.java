package com.android.project.ui.activity;


import android.Manifest;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.android.barball_library.FlyTabLayout.TabBean;
import com.android.barball_library.FlyTabLayout.listener.CustomTabEntity;
import com.android.barball_library.FlyTabLayout.listener.OnTabSelectListener;
import com.android.project.R;
import com.android.project.base.BaseFragment;
import com.android.project.bean.EventBusModel;
import com.android.project.common.NavigationActivity;
import com.android.project.databinding.ActivityMainBinding;
import com.android.project.databinding.ViewSearchBinding;
import com.android.project.ui.fragment.AdvanceFragment;
import com.android.project.ui.fragment.EmptyFragment;
import com.android.project.ui.fragment.ErrorFragment;
import com.android.project.ui.fragment.ListEmptyFragment;
import com.android.project.ui.fragment.ListErrorFragment;
import com.android.project.ui.fragment.ListFragment;
import com.android.project.utils.PermissionsUtils;
import com.android.project.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NavigationActivity<ActivityMainBinding> {

    private View mSearchView;
    public ViewSearchBinding mSearchBinding;
    private SparseArrayCompat<BaseFragment> mCachesFragment;


    @Override
    public void init() {
        useEventBus(true); // 使用EventBus
        advanceInitSuccessView(true); // 提先加载成功页面
        setHideBackBtn(true); // 隐藏返回按钮
        setHideNavBarOwnView(true); // 隐藏NavBar自带View
        mSearchView = LayoutInflater.from(this).inflate(R.layout.view_search,getNavigationBar().mNavigationBinding.placeholderContainer,false);
        mSearchBinding = DataBindingUtil.bind(mSearchView);
        addViewOnNavBar(mSearchView);


        PermissionsUtils.apply(this,
                "您拒绝权限申请，地图定位功能将不能正常使用，您可以去设置页面重新授权",
                "地图定位功能需要您授权，否则将不能正常使用",
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                new PermissionsUtils.OnGrantedListener() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        ToastUtils.normal("权限失败");
                    }
                }
        );
    }

    @Override
    public void initData() {

    }

    @Override
    public int initSuccessView() {
        return R.layout.activity_main;
    }

    @Override
    public void displayPage(ActivityMainBinding viewBinding) {
        String[] titles = new String[]{"列表", "列表空页面", "列表错误", "直接Init","空页面","错误页面"};
        int[] iconUnSelectIds = {
                R.mipmap.ic_me, R.mipmap.ic_me, R.mipmap.ic_me, R.mipmap.ic_me, R.mipmap.ic_me, R.mipmap.ic_me};
        int[] iconSelectIds = {
                R.mipmap.ic_me_select, R.mipmap.ic_me_select,R.mipmap.ic_me_select, R.mipmap.ic_me_select, R.mipmap.ic_me_select, R.mipmap.ic_me_select};
        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            tabEntities.add(new TabBean(titles[i], iconSelectIds[i], iconUnSelectIds[i]));
        }
        viewBinding.mainMenu.setTabData(tabEntities);
        mCachesFragment = new SparseArrayCompat<>();
        if (mCachesFragment.get(0) == null){
            mCachesFragment.put(0,new ListFragment());
        }
        if (mCachesFragment.get(1) == null){
            mCachesFragment.put(1,new ListEmptyFragment());
        }
        if (mCachesFragment.get(2) == null){
            mCachesFragment.put(2,new ListErrorFragment());
        }
        if (mCachesFragment.get(3) == null){
            mCachesFragment.put(3,new AdvanceFragment());
        }
        if (mCachesFragment.get(4) == null){
            mCachesFragment.put(4,new EmptyFragment());
        }
        if (mCachesFragment.get(5) == null){
            mCachesFragment.put(5,new ErrorFragment());
        }
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(MainActivity.this.getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mCachesFragment.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mCachesFragment.get(position);
            }
        };
        viewBinding.viewPagerContainer.setAdapter(pagerAdapter);
        mCachesFragment.get(0).lazyData();
    }

    @Override
    public void initListener(final ActivityMainBinding viewBinding) {
        viewBinding.mainMenu.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewBinding.viewPagerContainer.setCurrentItem(position,false);
                mCachesFragment.get(position).lazyData();
                switch (position){
                    case 0:
                        setHideNavBarOwnView(true);
                        mSearchView.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setHideNavBarOwnView(true);
                        mSearchView.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setHideNavBarOwnView(true);
                        mSearchView.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        setHideNavBarOwnView(false);
                        setNavTitle("直接初始化页面");
                        mSearchView.setVisibility(View.GONE);
                        break;
                    case 4:
                        setHideNavBarOwnView(false);
                        setNavTitle("空页面");
                        mSearchView.setVisibility(View.GONE);
                        break;
                    case 5:
                        setHideNavBarOwnView(false);
                        setNavTitle("错误页面");
                        mSearchView.setVisibility(View.GONE);
                        break;
                }
            }
            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    @Override
    public void onEvent(EventBusModel event) {
        super.onEvent(event);
        //TODO 这里接收EventBus
    }
}
