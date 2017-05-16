package com.android.project.ui.fragment;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.android.barball_library.LoadingLayout.LoadingLayout;
import com.android.project.R;
import com.android.project.base.BaseFragment;
import com.android.project.bean.HomeModelInfo;
import com.android.project.bean.UserModel;
import com.android.project.common.Const;
import com.android.project.common.DataBindRecyclerViewAdapter;
import com.android.project.common.PresenterCallback;
import com.android.project.databinding.FragmentHomeBinding;
import com.android.project.databinding.ItemBinding;
import com.android.project.databinding.LayoutHomeHeaderBinding;
import com.android.project.databinding.LayoutLoadingBinding;
import com.android.project.presenter.TestPresenter;
import com.android.project.ui.activity.MainActivity;
import com.android.project.ui.activity.SearchActivity;
import com.android.project.utils.ImageUtils;
import com.android.project.utils.LogUtils;
import com.android.project.utils.UIUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 列表错误
 *
 * @author Leon(黄长亮)
 * @date 2017/5/16.
 */
public class ListErrorFragment extends BaseFragment<FragmentHomeBinding>{

    private TestPresenter mTestPresenter;
    private HomeModelInfo mHomeModelInfo = new HomeModelInfo();
    private List<String> imgs = new ArrayList<>();
    String url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494934811541&di=89b496d0bbc0b0a09dfee8c3d8d66110&imgtype=0&src=http%3A%2F%2Fresource.58game.com%2Fuploads%2Ftuku%2Fthumb%2F20160126%2F56a6d7ba3896f.jpg";
    String url2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495529580&di=cb5a6c420b86d24bffd667fc15aaf3b4&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.tupianzj.com%2Fuploads%2Fallimg%2F160520%2F9-160520091319.jpg";
    String url3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494934855796&di=0e360cb1aee56f4df0e6217b98e43786&imgtype=0&src=http%3A%2F%2Fimg01.taopic.com%2F150702%2F318754-150F21P94158.jpg";
    private DataBindRecyclerViewAdapter mAdapter;
    private LayoutHomeHeaderBinding mHomeHeaderBinding;
    private FragmentHomeBinding mFragmentHomeBinding;
    private LayoutLoadingBinding mLoadingBinding;


    @Override
    public void init() {

    }

    @Override
    public void initData() {

        imgs.add(url1);
        imgs.add(url2);
        imgs.add(url3);

        mTestPresenter = new TestPresenter(new PresenterCallback<HomeModelInfo>() {
            @Override
            public void onSuccess(HomeModelInfo sd, boolean isLoadMore) {

            }

            @Override
            public void onError(Exception e) {
                super.onError(e);

                UIUtils.postTaskDelay(new Runnable() {// 没有正确的网络请求，只能在错误方法中模拟网络数据回调
                    @Override
                    public void run() {
                        // 真正的网络数据在这里
                        mTestPresenter.finishLoadMore();
                        mTestPresenter.finishRefresh();
                        // 显示空页面
                        mLoadingBinding.errorView.setVisibility(View.VISIBLE);
                        mLoadingBinding.loadingView.setVisibility(View.GONE);
                        if (mHomeHeaderBinding != null){
                            mHomeHeaderBinding.bannerPhotoPlayer.setData(R.layout.banner_image,imgs,null);
                        }
                    }
                },3000);

            }
        });

        if (mFragmentHomeBinding == null){
            mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
        }


    }

    @Override
    public int initSuccessView() {
        return R.layout.fragment_home;
    }

    @Override
    public void displayPage(final FragmentHomeBinding viewBinding) {
        LogUtils.sf("初始化ListFragment");
        viewBinding.mask.setClickable(false);
        mFragmentHomeBinding = viewBinding;
        mTestPresenter.setRefreshLayout(viewBinding.trl);
        viewBinding.rvList.setLayoutManager(new LinearLayoutManager(mActivity));
        // 点击错误按钮时重新加载数据
        mAdapter = new DataBindRecyclerViewAdapter<UserModel,ViewDataBinding>() {

            @Override
            public int getItemViewType(int position) {

                if (getData().get(position).type == 0){
                    return R.layout.layout_home_header;
                }else if (getData().get(position).type == 1){
                    // 主页是一进来初始化完成的所以
                    // 把LoadingLayout加入到RecyclerView中，这个页面有多种布局组成，主要就是为了把《圈圈动画控制在RecyclerView中间》
                    return R.layout.layout_loading;
                }else {
                    return R.layout.item;
                }
            }

            @Override
            public void onBindHolder(ViewDataBinding binding, UserModel itemData, int position) {
                if (getData().get(position).type == 0){
                    mHomeHeaderBinding = (LayoutHomeHeaderBinding) binding;
                    mHomeHeaderBinding.bannerPhotoPlayer.setAdapter(new BGABanner.Adapter<CardView, String>() {
                        @Override
                        public void fillBannerItem(BGABanner banner, CardView itemView, String model, int position) {
                            SimpleDraweeView sdv = (SimpleDraweeView) itemView.findViewById(R.id.sdv);
                            ImageUtils.with(sdv).load(model);
                        }
                    });
                }else if (getData().get(position).type == 1) {
                    mLoadingBinding = (LayoutLoadingBinding) binding;
                    ViewGroup.LayoutParams layoutParams = mLoadingBinding.superContainer.getLayoutParams();
                    layoutParams.height = viewBinding.trl.getHeight() - viewBinding.headerLayout.superContainer.getHeight();
                    mLoadingBinding.superContainer.setLayoutParams(layoutParams);
                    mLoadingBinding.progressWheel.requestLayout();
                    mLoadingBinding.btnErrorRetry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 点击错误按钮时重新加载数据
                            mLoadingBinding.loadingView.setVisibility(View.VISIBLE);
                            mLoadingBinding.errorView.setVisibility(View.GONE);
                            initData();
                        }
                    });
                }else {
                    ItemBinding activitiesBinding = (ItemBinding) binding;
                    ImageUtils.with(activitiesBinding.sdvShopLogo).load(Const.TEST_AVATAR);
                }
            }
        };

        // 主页是一进来初始化完成的所以先给mAdapter一些假数据
        viewBinding.rvList.setAdapter(mAdapter);
        final List<UserModel> data = new ArrayList<>();
        UserModel headerModel = new UserModel();
        UserModel loadingModel = new UserModel();
        headerModel.type = 0;
        loadingModel.type = 1;
        data.add(headerModel);
        data.add(loadingModel);
        mAdapter.setData(data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mHomeHeaderBinding != null && mHomeHeaderBinding.bannerPhotoPlayer != null){
            mHomeHeaderBinding.bannerPhotoPlayer.startAutoPlay();
        }
    }


    @Override
    public void initListener(FragmentHomeBinding viewBinding) {
        final MainActivity mainActivity = (MainActivity) mActivity;
        mainActivity.mSearchBinding.searchContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(new IntentExpand(SearchActivity.class) {
                    @Override
                    public void extraValue(Intent intent) {
                        // 为了优化搜索框动画，跳转前先停止Banner动画
                        mHomeHeaderBinding.bannerPhotoPlayer.stopAutoPlay();
                        intent.putExtra("width",mainActivity.mSearchBinding.searchContainer.getWidth());
                        int leftMargin = mainActivity.mSearchBinding.searchContainer.getWidth() - mainActivity.mSearchBinding.layoutSearch.getWidth();
                        intent.putExtra("leftMargin",leftMargin / 2);
                    }
                });
            }
        });
    }
}
